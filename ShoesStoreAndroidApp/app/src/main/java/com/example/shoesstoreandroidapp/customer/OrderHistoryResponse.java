package com.example.shoesstoreandroidapp.customer;


import java.time.LocalDateTime;

public class OrderHistoryResponse {
    private Long orderId;
    private String orderDate;
    private Double orderTotal;
    private String productName;
    private Integer quantity;
    private Integer size;
    private String commune;
    private String detailedAddress;
    private String district;
    private String province;

    private String image;
    private Integer isReview;

    public Integer getIsReview() {
        return isReview;
    }

    public void setIsReview(Integer isReview) {
        this.isReview = isReview;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
