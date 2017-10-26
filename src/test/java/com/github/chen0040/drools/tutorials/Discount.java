package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/7/16.
 */
public class Discount {
   private Order order;
   private double percentage;

   public Discount(double percentage){
      this.percentage = percentage;
   }

   public Discount() {

   }


   public Order getOrder() {
      return order;
   }


   public void setOrder(Order order) {
      this.order = order;
   }


   public double getPercentage() {
      return percentage;
   }


   public void setPercentage(double percentage) {
      this.percentage = percentage;
   }
}
