package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/7/16.
 */
public class OrderLine {
   private Item item;
   private int quantity;
   private double total;

   public OrderLine(){

   }


   public Item getItem() {
      return item;
   }


   public void setItem(Item item) {
      this.item = item;
   }


   public int getQuantity() {
      return quantity;
   }


   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }


   @Override public String toString() {
      return "OrderLine{" +
              "item=" + item +
              ", quantity=" + quantity +
              ", total=" + total +
              '}';
   }


   public double getTotal() {
      return total;
   }


   public void setTotal(double total) {
      this.total = total;
   }
}
