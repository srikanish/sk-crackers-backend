package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "mobile_no")
    private String mobileNo;
    
    @Column(name = "customer_address")
    private String customerAddress;
    
    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "product")
    private List<String> productList;
    
    @Column(name = "amount")
    private Double amount;
    
    @Column(name = "date_time")
    private LocalDateTime dateTime = LocalDateTime.now();
    
    @Column(name = "status")
    private String status = "Pending Payment";

    // Default constructor
    public Orders() {}

    // Constructor with parameters
    public Orders(String customerName, String mobileNo, String customerAddress, 
                  List<String> productList, Double amount) {
        this.customerName = customerName;
        this.mobileNo = mobileNo;
        this.customerAddress = customerAddress;
        this.productList = productList;
        this.amount = amount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
