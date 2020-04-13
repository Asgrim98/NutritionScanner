package com.example.nutritionscanner;

import android.content.ContentValues;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Product implements Serializable {

    private String productName;
    private String kcal;
    private String fat;
    private String carbohydrates;
    private String proteins;
    private String link;

    private transient ContentValues value;

    public Product(){}

    public Product(String kcal, String fat, String carbohydrates, String proteins) {
        this.kcal = kcal;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;

        //putValues();
    }

    public void putValues(){

        value = new ContentValues();

        value.put(DBHelper.COL1, productName);
        value.put(DBHelper.COL2, kcal);
        value.put(DBHelper.COL3, fat);
        value.put(DBHelper.COL4, carbohydrates);
        value.put(DBHelper.COL5, proteins);
        value.put(DBHelper.COL6, link);
    }

    public Product(ArrayList<String> list){

        this.productName = list.get(1);
        this.kcal = list.get(2);
        this.fat = list.get(3);
        this.carbohydrates = list.get(4);
        this.proteins = list.get(5);
        this.link = list.get(6);
    }

    public Product(String productName, String kcal, String fat, String carbohydrates, String proteins, String link) {

        this.productName = productName;
        this.kcal = kcal;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.link = link;

       // putValues();
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

    public ContentValues getValue() {
        return value;
    }
}
