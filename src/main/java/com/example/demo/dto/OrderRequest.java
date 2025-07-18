package com.example.demo.dto;

import java.util.List;

public class OrderRequest {
    
    private String name;
    private String mobile;
    private String address;
    private List<String> productList;
    private Double totalAmount;

    // Default constructor
    public OrderRequest() {}

    // Constructor
    public OrderRequest(String name, String mobile, String address, List<String> productList, Double totalAmount) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.productList = productList;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", productList=" + productList +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
