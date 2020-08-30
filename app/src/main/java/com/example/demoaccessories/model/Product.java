package com.example.demoaccessories.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("nameSeller")
    @Expose
    private String nameSeller;
    @SerializedName("nameProduct")
    @Expose
    private String nameProduct;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("imageSeller")
    @Expose
    private String imageSeller;
    @SerializedName("createAt")
    @Expose
    private String createAt;
    @SerializedName("titleNotification")
    @Expose
    private String titleNotification;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public Product(String id, String nameSeller, String nameProduct, String price, String address, String category, String description, String image, String imageSeller, String createAt, String titleNotification, Integer v) {
        this.id = id;
        this.nameSeller = nameSeller;
        this.nameProduct = nameProduct;
        this.price = price;
        this.address = address;
        this.category = category;
        this.description = description;
        this.image = image;
        this.createAt = createAt;
        this.titleNotification = titleNotification;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameSeller() {
        return nameSeller;
    }

    public void setNameSeller(String nameSeller) {
        this.nameSeller = nameSeller;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageSeller() {
        return imageSeller;
    }

    public void setImageSeller(String imageSeller) {
        this.imageSeller = imageSeller;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getTitleNotification() {
        return titleNotification;
    }

    public void setTitleNotification(String titleNotification) {
        this.titleNotification = titleNotification;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}


