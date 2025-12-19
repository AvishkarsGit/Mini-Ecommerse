package com.avishkar.mini_e_commerse.models;

public class ProductsModel {

    String productName,productImg;
    int id,productActualPrice, productDiscountPrice, productTax;
    boolean isDiscount,isCart;

    public ProductsModel() {
        //default constructor
    }

    public ProductsModel(int id,String productName, String productImg, int productActualPrice, int productDiscountPrice, int productTax, boolean isDiscount, boolean isCart) {
        this.id = id;
        this.productName = productName;
        this.productImg = productImg;
        this.productActualPrice = productActualPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.productTax = productTax;
        this.isDiscount = isDiscount;
        this.isCart = isCart;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getProductActualPrice() {
        return productActualPrice;
    }

    public void setProductActualPrice(int productActualPrice) {
        this.productActualPrice = productActualPrice;
    }

    public int getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(int productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }

    public int getProductTax() {
        return productTax;
    }

    public void setProductTax(int productTax) {
        this.productTax = productTax;
    }

    public boolean isDiscount() {
        return isDiscount;
    }

    public void setDiscount(boolean discount) {
        isDiscount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCart() {
        return isCart;
    }

    public void setCart(boolean cart) {
        isCart = cart;
    }
}
