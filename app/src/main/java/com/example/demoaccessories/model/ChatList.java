package com.example.demoaccessories.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatList {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("id_product")
    @Expose
    private String idProduct;
    @SerializedName("admin")
    @Expose
    private String admin;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("image_admin")
    @Expose
    private String imageAdmin;
    @SerializedName("image_user")
    @Expose
    private String imageUser;
    @SerializedName("messenger")
    @Expose
    private String messenger;
    @SerializedName("create_at")
    @Expose
    private String createAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImageAdmin() {
        return imageAdmin;
    }

    public void setImageAdmin(String imageAdmin) {
        this.imageAdmin = imageAdmin;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
