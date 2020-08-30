package com.example.demoaccessories.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class User {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DataUser data;
    public User(Boolean status, String message, DataUser data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataUser getData() {
        return data;
    }

    public void setData(DataUser data) {
        this.data = data;
    }

}
