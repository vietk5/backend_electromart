package com.electromart.backend.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.vision.v1.*;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GoogleVisionSearchService {

    private static final Logger log = LoggerFactory.getLogger(GoogleVisionSearchService.class);

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.credentials.location}")
    private String credentialsPath;

    @Value("${app.cloud-storage.bucket}")
    private String bucketName;

    private ImageAnnotatorClient visionClient;
    private Storage storageClient;

    @PostConstruct
    public void init() {
        log.info("============================================");
        log.info("🚀 INITIALIZING GOOGLE VISION SERVICE");
        log.info("============================================");
        
        log.info("📋 Configuration:");
        log.info("  - Project ID: {}", projectId);
        log.info("  - Credentials: {}", credentialsPath);
        log.info("  - Bucket: {}", bucketName);
        
        if (projectId == null || projectId.isEmpty()) {
            log.error("❌ Project ID not configured!");
            return;
        }
        
        if (credentialsPath == null || credentialsPath.isEmpty()) {
            log.error("❌ Credentials path not configured!");
            return;
        }
        
        try {
            String cleanPath = credentialsPath.replace("classpath:", "");
            log.info("🔍 Loading credentials from: {}", cleanPath);
            
            InputStream credentialsStream = getClass().getClassLoader()
                .getResourceAsStream(cleanPath);
            
            if (credentialsStream == null) {
                log.error("❌ Credentials file not found: {}", cleanPath);
                return;
            }
            
            log.info("✅ Credentials file found");
            
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Lists.newArrayList(
                    "https://www.googleapis.com/auth/cloud-platform",
                    "https://www.googleapis.com/auth/cloud-vision"
                ));
            
            log.info("✅ Credentials loaded successfully");
            
            log.info("🔧 Initializing Vision API client...");
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
            
            visionClient = ImageAnnotatorClient.create(settings);
            log.info("✅ Vision API client initialized");

            log.info("🔧 Initializing Cloud Storage client...");
            storageClient = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
            log.info("✅ Cloud Storage client initialized");
            
            log.info("============================================");
            log.info("✅ GOOGLE VISION SERVICE READY");
            log.info("============================================");

        } catch (IOException e) {
            log.error("❌ Failed to load credentials", e);
        } catch (Exception e) {
            log.error("❌ Initialization failed", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        log.info("🧹 Cleaning up Google Vision Service...");
        try {
            if (visionClient != null) {
                visionClient.close();
                log.info("✅ Vision client closed");
            }
        } catch (Exception e) {
            log.error("Error closing Vision client", e);
        }
    }

    /**
     * NEW: Search using Label Detection + Keyword Matching
     * Works better for images not indexed by Google
     */
    public ProductSearchResult searchByLabelsAndKeywords(byte[] imageBytes) {
        if (visionClient == null) {
            log.error("Vision client not initialized");
            return new ProductSearchResult(Collections.emptyList(), Collections.emptyList(), "");
        }

        try {
            log.info("🔍 Starting Label Detection search");

            // Step 1: Detect labels
            List<String> labels = detectLabels(imageBytes);
            log.info("📋 Detected {} labels: {}", labels.size(), labels);

            // Step 2: Detect text (for product names, model numbers)
            String detectedText = detectText(imageBytes);
            log.info("📝 Detected text: {}", detectedText);

            // Step 3: Extract keywords
            Set<String> keywords = extractKeywords(labels, detectedText);
            log.info("🔑 Extracted keywords: {}", keywords);

            if (keywords.isEmpty()) {
                log.warn("⚠️ No keywords extracted from image");
                return new ProductSearchResult(Collections.emptyList(), labels, detectedText);
            }

            // Return result with keywords for matching in controller
            return new ProductSearchResult(new ArrayList<>(keywords), labels, detectedText);

        } catch (Exception e) {
            log.error("Error in label detection search", e);
            return new ProductSearchResult(Collections.emptyList(), Collections.emptyList(), "");
        }
    }

    /**
     * Extract meaningful keywords from labels and text
     */
    private Set<String> extractKeywords(List<String> labels, String text) {
        Set<String> keywords = new HashSet<>();

        // Add labels (cleaned)
        for (String label : labels) {
            String cleaned = label.toLowerCase().trim();
            if (cleaned.length() >= 3) { // Ignore very short words
                keywords.add(cleaned);
            }
        }

        // Extract words from text
        if (text != null && !text.isEmpty()) {
            String[] words = text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .split("\\s+");
            
            for (String word : words) {
                if (word.length() >= 3) {
                    keywords.add(word);
                }
            }
        }

        // Common tech product keywords to prioritize
        Set<String> techKeywords = new HashSet<>(Arrays.asList(
            "laptop", "computer", "pc", "desktop",
            "ssd", "ram", "cpu", "gpu",
            "keyboard", "mouse", "monitor", "screen",
            "headphone", "speaker", "audio",
            "gaming", "asus", "msi", "dell", "samsung",
            "intel", "amd", "nvidia", "kingston"
        ));

        // Keep tech-related keywords
        Set<String> filtered = keywords.stream()
            .filter(k -> techKeywords.contains(k) || k.length() >= 4)
            .collect(Collectors.toSet());

        return filtered.isEmpty() ? keywords : filtered;
    }

    /**
     * Detect labels in image
     */
    public List<String> detectLabels(byte[] imageBytes) {
        if (visionClient == null) {
            log.error("Vision client not initialized");
            return Collections.emptyList();
        }

        try {
            ByteString imgBytes = ByteString.copyFrom(imageBytes);
            Image img = Image.newBuilder().setContent(imgBytes).build();

            Feature feat = Feature.newBuilder()
                .setType(Feature.Type.LABEL_DETECTION)
                .setMaxResults(20)
                .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

            BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(
                Collections.singletonList(request)
            );

            AnnotateImageResponse res = response.getResponsesList().get(0);

            if (res.hasError()) {
                log.error("Vision API error: {}", res.getError().getMessage());
                return Collections.emptyList();
            }

            List<String> labels = res.getLabelAnnotationsList().stream()
                .map(EntityAnnotation::getDescription)
                .collect(Collectors.toList());

            log.debug("Detected labels: {}", labels);
            return labels;

        } catch (Exception e) {
            log.error("Error detecting labels", e);
            return Collections.emptyList();
        }
    }

    /**
     * Detect text in image
     */
    public String detectText(byte[] imageBytes) {
        if (visionClient == null) {
            log.error("Vision client not initialized");
            return "";
        }

        try {
            ByteString imgBytes = ByteString.copyFrom(imageBytes);
            Image img = Image.newBuilder().setContent(imgBytes).build();

            Feature feat = Feature.newBuilder()
                .setType(Feature.Type.TEXT_DETECTION)
                .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

            BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(
                Collections.singletonList(request)
            );

            AnnotateImageResponse res = response.getResponsesList().get(0);

            if (res.hasError()) {
                log.error("Vision API error: {}", res.getError().getMessage());
                return "";
            }

            if (res.getTextAnnotationsCount() > 0) {
                String text = res.getTextAnnotations(0).getDescription();
                log.debug("Detected text: {}", text);
                return text;
            }

            return "";

        } catch (Exception e) {
            log.error("Error detecting text", e);
            return "";
        }
    }

    /**
     * ORIGINAL: Search using Web Detection (for indexed images)
     */
    public List<Long> searchSimilarProducts(byte[] imageBytes) {
        if (visionClient == null) {
            log.error("Vision client not initialized");
            return Collections.emptyList();
        }

        try {
            log.info("🔍 Starting Google Vision Web Detection search");

            ByteString imgBytes = ByteString.copyFrom(imageBytes);
            Image img = Image.newBuilder().setContent(imgBytes).build();

            Feature feat = Feature.newBuilder()
                .setType(Feature.Type.WEB_DETECTION)
                .build();

            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

            BatchAnnotateImagesResponse response = visionClient.batchAnnotateImages(
                Collections.singletonList(request)
            );

            AnnotateImageResponse res = response.getResponsesList().get(0);

            if (res.hasError()) {
                log.error("Vision API error: {}", res.getError().getMessage());
                return Collections.emptyList();
            }

            Set<String> matchedUrls = new HashSet<>();
            WebDetection webDetection = res.getWebDetection();

            if (webDetection.getFullMatchingImagesCount() > 0) {
                log.info("📸 Found {} full matching images", 
                    webDetection.getFullMatchingImagesCount());
                
                for (WebDetection.WebImage image : webDetection.getFullMatchingImagesList()) {
                    if (image.getUrl() != null && !image.getUrl().isEmpty()) {
                        matchedUrls.add(image.getUrl());
                    }
                }
            }

            if (webDetection.getPartialMatchingImagesCount() > 0) {
                log.info("📸 Found {} partial matching images", 
                    webDetection.getPartialMatchingImagesCount());
                
                for (WebDetection.WebImage image : webDetection.getPartialMatchingImagesList()) {
                    if (image.getUrl() != null && !image.getUrl().isEmpty()) {
                        matchedUrls.add(image.getUrl());
                    }
                }
            }

            if (webDetection.getVisuallySimilarImagesCount() > 0) {
                log.info("📸 Found {} visually similar images", 
                    webDetection.getVisuallySimilarImagesCount());
                
                for (WebDetection.WebImage image : webDetection.getVisuallySimilarImagesList()) {
                    if (image.getUrl() != null && !image.getUrl().isEmpty()) {
                        matchedUrls.add(image.getUrl());
                    }
                }
            }

            log.info("🔗 Total matched URLs: {}", matchedUrls.size());

            String bucketPrefix = "storage.googleapis.com/" + bucketName;
            List<Long> productIds = matchedUrls.stream()
                .filter(url -> url.contains(bucketPrefix))
                .map(this::extractProductIdFromUrl)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

            log.info("✅ Found {} product IDs: {}", productIds.size(), productIds);

            return productIds;

        } catch (Exception e) {
            log.error("Error in Vision API search", e);
            return Collections.emptyList();
        }
    }

    /**
     * Search with multiple images
     */
    public List<Long> searchWithMultipleImages(List<byte[]> imageBytesList) {
        log.info("🔍 Searching with {} images", imageBytesList.size());

        Map<Long, Integer> productScores = new HashMap<>();

        for (int i = 0; i < imageBytesList.size(); i++) {
            try {
                log.info("Processing image {}/{}", i + 1, imageBytesList.size());
                
                List<Long> productIds = searchSimilarProducts(imageBytesList.get(i));
                
                int weight = imageBytesList.size() - i;
                for (Long productId : productIds) {
                    productScores.merge(productId, weight, Integer::sum);
                }
                
            } catch (Exception e) {
                log.error("Error processing image {}: {}", i, e.getMessage());
            }
        }

        List<Long> sortedProductIds = productScores.entrySet().stream()
            .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        log.info("🎯 Found {} unique products from {} images", 
            sortedProductIds.size(), imageBytesList.size());

        return sortedProductIds;
    }

    private Long extractProductIdFromUrl(String url) {
        try {
            Pattern pattern = Pattern.compile("products?_(\\d+)\\.");
            Matcher matcher = pattern.matcher(url);
            
            if (matcher.find()) {
                return Long.parseLong(matcher.group(1));
            }
        } catch (Exception e) {
            log.warn("Failed to extract product ID from URL: {}", url);
        }
        return null;
    }

    public boolean isReady() {
        return visionClient != null && storageClient != null;
    }

    /**
     * Result class for label-based search
     */
    public static class ProductSearchResult {
        private final List<String> keywords;
        private final List<String> labels;
        private final String detectedText;

        public ProductSearchResult(List<String> keywords, List<String> labels, String detectedText) {
            this.keywords = keywords;
            this.labels = labels;
            this.detectedText = detectedText;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public List<String> getLabels() {
            return labels;
        }

        public String getDetectedText() {
            return detectedText;
        }
    }
}