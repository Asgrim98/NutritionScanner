package com.example.nutritionscanner;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Product implements Serializable {

    private String productName;
    private String kcal;
    private String fat;
    private String carbohydrates;
    private String proteins;
    private String link;

    public Product(){}

    public Product(String kcal, String fat, String carbohydrates, String proteins) {
        this.kcal = kcal;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
    }

    public Product(String productName, String kcal, String fat, String carbohydrates, String proteins, String link) {
        this.productName = productName;
        this.kcal = kcal;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.link = link;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
