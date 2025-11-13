package com.electromart.backend.model;

import jakarta.persistence.*;

@Entity @Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brand;
    private Integer price;
    @Column(name="image_url") private String imageUrl;

    public Long getId(){return id;}
    public String getName(){return name;}
    public String getBrand(){return brand;}
    public Integer getPrice(){return price;}
    public String getImageUrl(){return imageUrl;}

    public void setId(Long id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setBrand(String brand){this.brand=brand;}
    public void setPrice(Integer price){this.price=price;}
    public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
}
