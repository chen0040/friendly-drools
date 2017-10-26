package com.github.chen0040.drools.tutorials;


import org.kie.api.definition.type.PropertyReactive;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xschen on 11/7/16.
 */
@PropertyReactive
public class Order {

   public enum OrderState {
      COMPLETED,
      NA
   }

   private List<OrderLine> orderLines = new ArrayList<>();
   private Customer customer;
   private OrderState state = OrderState.NA;
   private long orderId;
   private Discount discount;
   private double total;

   public Customer getCustomer() {
      return customer;
   }


   public void setCustomer(Customer customer) {
      this.customer = customer;
   }


   public List<OrderLine> getOrderLines() {
      return orderLines;
   }


   public void setOrderLines(List<OrderLine> orderLines) {
      this.orderLines = orderLines;
   }


   public Discount getDiscount() {
      return discount;
   }


   public void setDiscount(Discount discount) {
      this.discount = discount;
   }


   public OrderState getState() {
      return state;
   }


   public void setState(OrderState state) {
      this.state = state;
   }


   public long getOrderId() {
      return orderId;
   }


   public void setOrderId(long orderId) {
      this.orderId = orderId;
   }

   public OrderLine addLine(Item item, int quantity) {
      OrderLine line = new OrderLine();
      line.setItem(item);
      line.setQuantity(quantity);
      line.setTotal(item.getSalePrice() * quantity);

      orderLines.add(line);

      return line;
   }


   @Override public String toString() {
      return "Order{" +
              "customer=" + customer +
              ", state=" + state +
              ", orderId=" + orderId +
              ", total=" + total +
              '}';
   }

   public void increaseDiscount(double discount) {
      if(this.discount == null){
         this.discount = new Discount(discount * 100);
      } else {
         this.discount.setPercentage(this.discount.getPercentage() + discount * 100);
      }
   }


   public double getTotal() {
      return total;
   }


   public void setTotal(double total) {
      this.total = total;
   }
}
