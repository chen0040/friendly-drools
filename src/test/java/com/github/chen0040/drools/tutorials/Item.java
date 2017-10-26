package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/7/16.
 */
public class Item {
   public enum Category {
      LOW_RANGE,
      HIGH_RANGE,
      NA
   }

   private double cost;
   private Category category = Category.NA;
   private double salePrice;
   private String name;
   private long itemId;

   public Item(String name, double cost, double salePrice) {
      this.name = name;
      this.cost = cost;
      this.salePrice = salePrice;
   }

   public Item(){

   }


   public String getName() {
      return name;
   }


   public void setName(String name) {
      this.name = name;
   }


   public double getCost() {
      return cost;
   }


   public void setCost(double cost) {
      this.cost = cost;
   }


   public Category getCategory() {
      return category;
   }


   public void setCategory(Category category) {
      this.category = category;
   }


   public double getSalePrice() {
      return salePrice;
   }


   public void setSalePrice(double salePrice) {
      this.salePrice = salePrice;
   }



}
