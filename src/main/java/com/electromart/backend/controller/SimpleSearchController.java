package com.electromart.backend.controller;

import com.electromart.backend.model.Product;
import com.electromart.backend.repository.ProductRepository;
import com.electromart.backend.service.GoogleVisionSearchService;
import com.electromart.backend.service.GoogleVisionSearchService.ProductSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SimpleSearchController {

    private static final Logger log = LoggerFactory.getLogger(SimpleSearchController.class);

    private final GoogleVisionSearchService searchService;
    private final ProductRepository productRepository;

    public SimpleSearchController(
            GoogleVisionSearchService searchService,
            ProductRepository productRepository) {
        this.searchService = searchService;
        this.productRepository = productRepository;
    }

    /**
     * NEW: Search by single image using LABEL DETECTION
     */
    @PostMapping("/by-image")
    public ResponseEntity<Map<String, Object>> searchByImage(
            @RequestBody Map<String, String> request) {
        
        try {
            String base64Image = request.get("imageBase64");
            
            if (base64Image == null || base64Image.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Missing imageBase64",
                    "data", Collections.emptyList()
                ));
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            
            log.info("🔍 Searching with 1 image using Label Detection");
            
            // Use label detection
            ProductSearchResult searchResult = searchService.searchByLabelsAndKeywords(imageBytes);
            
            if (searchResult.getKeywords().isEmpty()) {
                log.warn("⚠️ No keywords extracted from image");
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Không thể nhận diện sản phẩm từ ảnh",
                    "data", Collections.emptyList(),
                    "debug", Map.of(
                        "labels", searchResult.getLabels(),
                        "text", searchResult.getDetectedText()
                    )
                ));
            }
            
            // Match products by keywords
            List<Product> products = matchProductsByKeywords(searchResult.getKeywords());
            
            String message = products.isEmpty() 
                ? "Không tìm thấy sản phẩm phù hợp với: " + searchResult.getKeywords()
                : "Tìm thấy " + products.size() + " sản phẩm";
            
            log.info("✅ " + message);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", message,
                "data", products,
                "debug", Map.of(
                    "keywords", searchResult.getKeywords(),
                    "labels", searchResult.getLabels(),
                    "text", searchResult.getDetectedText()
                )
            ));
            
        } catch (Exception e) {
            log.error("Search error", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Lỗi tìm kiếm: " + e.getMessage(),
                "data", Collections.emptyList()
            ));
        }
    }

    /**
     * NEW: Search by multiple images using LABEL DETECTION
     */
    @PostMapping("/by-multiple-images")
    public ResponseEntity<Map<String, Object>> searchByMultipleImages(
            @RequestBody Map<String, List<String>> request) {
        
        try {
            List<String> base64Images = request.get("imageBase64List");
            
            if (base64Images == null || base64Images.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Missing imageBase64List",
                    "data", Collections.emptyList()
                ));
            }

            log.info("🔍 Searching with {} images using Label Detection", base64Images.size());
            
            // Aggregate keywords from all images
            Set<String> allKeywords = new HashSet<>();
            List<String> allLabels = new ArrayList<>();
            
            for (int i = 0; i < base64Images.size(); i++) {
                byte[] imageBytes = Base64.getDecoder().decode(base64Images.get(i));
                
                log.info("  Processing image {}/{}", i + 1, base64Images.size());
                ProductSearchResult result = searchService.searchByLabelsAndKeywords(imageBytes);
                
                allKeywords.addAll(result.getKeywords());
                allLabels.addAll(result.getLabels());
            }
            
            log.info("📊 Aggregated {} keywords: {}", allKeywords.size(), allKeywords);
            
            if (allKeywords.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Không thể nhận diện sản phẩm từ các ảnh",
                    "data", Collections.emptyList(),
                    "debug", Map.of("labels", allLabels)
                ));
            }
            
            // Match products
            List<Product> products = matchProductsByKeywords(allKeywords);
            
            String message = products.isEmpty()
                ? "Không tìm thấy sản phẩm phù hợp"
                : "Tìm thấy " + products.size() + " sản phẩm từ " + base64Images.size() + " ảnh";
            
            log.info("✅ " + message);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", message,
                "data", products,
                "debug", Map.of(
                    "keywords", allKeywords,
                    "labels", allLabels.stream().distinct().collect(Collectors.toList())
                )
            ));
            
        } catch (Exception e) {
            log.error("Search error", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Lỗi tìm kiếm: " + e.getMessage(),
                "data", Collections.emptyList()
            ));
        }
    }

    /**
     * Search by file upload using LABEL DETECTION
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> searchByUpload(
            @RequestParam("file") MultipartFile file) {
        
        try {
            byte[] imageBytes = file.getBytes();
            
            log.info("🔍 Searching with uploaded file using Label Detection");
            
            ProductSearchResult searchResult = searchService.searchByLabelsAndKeywords(imageBytes);
            
            if (searchResult.getKeywords().isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Không thể nhận diện sản phẩm từ ảnh",
                    "data", Collections.emptyList(),
                    "debug", Map.of(
                        "labels", searchResult.getLabels(),
                        "text", searchResult.getDetectedText()
                    )
                ));
            }
            
            List<Product> products = matchProductsByKeywords(searchResult.getKeywords());
            
            String message = products.isEmpty()
                ? "Không tìm thấy sản phẩm"
                : "Tìm thấy " + products.size() + " sản phẩm";
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", message,
                "data", products,
                "debug", Map.of(
                    "keywords", searchResult.getKeywords(),
                    "labels", searchResult.getLabels()
                )
            ));
            
        } catch (Exception e) {
            log.error("Upload search error", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Lỗi tìm kiếm: " + e.getMessage(),
                "data", Collections.emptyList()
            ));
        }
    }

    /**
     * Match products by keywords extracted from image
     */
    private List<Product> matchProductsByKeywords(Collection<String> keywords) {
        if (keywords.isEmpty()) {
            return Collections.emptyList();
        }
        
        log.info("🔍 Matching products with keywords: {}", keywords);
        
        // Fetch all products
        List<Product> allProducts = productRepository.findAll();
        
        // Score each product based on keyword matches
        Map<Product, Integer> productScores = new HashMap<>();
        
        for (Product product : allProducts) {
            int score = calculateProductScore(product, keywords);
            if (score > 0) {
                productScores.put(product, score);
            }
        }
        
        // Sort by score descending and return top 10
        List<Product> matchedProducts = productScores.entrySet().stream()
            .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
            .limit(10)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        log.info("📊 Matched {} products", matchedProducts.size());
        
        return matchedProducts;
    }

    /**
     * Calculate match score for a product
     */
    private int calculateProductScore(Product product, Collection<String> keywords) {
        int score = 0;
        
        String productName = product.getName() != null ? product.getName().toLowerCase() : "";
        String brandName = product.getBrand() != null ? 
            product.getBrand().toLowerCase() : "";
        
        for (String keyword : keywords) {
            String kw = keyword.toLowerCase();
            
            // Exact match in name = 10 points
            if (productName.contains(kw)) {
                score += 10;
            }
            
            
            // Match in brand = 15 points (brand is important)
            if (brandName.contains(kw)) {
                score += 15;
            }
        }
        
        return score;
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        boolean isReady = searchService.isReady();
        
        return ResponseEntity.ok(Map.of(
            "success", isReady,
            "message", isReady ? "Search service is healthy" : "Service not ready",
            "data", isReady ? "OK" : "NOT_READY"
        ));
    }

    /**
     * Detect labels (debug)
     */
    @PostMapping("/detect-labels")
    public ResponseEntity<Map<String, Object>> detectLabels(
            @RequestParam("file") MultipartFile file) {
        
        try {
            byte[] imageBytes = file.getBytes();
            List<String> labels = searchService.detectLabels(imageBytes);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Detected " + labels.size() + " labels",
                "data", labels
            ));
            
        } catch (Exception e) {
            log.error("Label detection error", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", Collections.emptyList()
            ));
        }
    }

    /**
     * Detect text (debug)
     */
    @PostMapping("/detect-text")
    public ResponseEntity<Map<String, Object>> detectText(
            @RequestParam("file") MultipartFile file) {
        
        try {
            byte[] imageBytes = file.getBytes();
            String text = searchService.detectText(imageBytes);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Text detected",
                "data", text
            ));
            
        } catch (Exception e) {
            log.error("Text detection error", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", ""
            ));
        }
    }

    /**
     * Get statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("service", "Google Cloud Vision - Label Detection");
        stats.put("ready", searchService.isReady());
        stats.put("timestamp", new Date());
        stats.put("searchMethod", "Label Detection + Keyword Matching");
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Statistics",
            "data", stats
        ));
    }
}