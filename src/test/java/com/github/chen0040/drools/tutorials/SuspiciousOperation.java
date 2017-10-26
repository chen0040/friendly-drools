package com.github.chen0040.drools.tutorials;


import java.util.Date;


/**
 * Created by xschen on 11/7/16.
 */
public class SuspiciousOperation {
   public static enum Type {
      SUSPICIOUS_AMOUNT,
      SUSPICIOUS_FREQUENCY;
   }
   private Customer customer;
   private Type type;
   private Date date;
   private String comment;
   public SuspiciousOperation(Customer customer, Type type) {
      this.customer = customer;
      this.type = type;
   }


   public Customer getCustomer() {
      return customer;
   }


   public void setCustomer(Customer customer) {
      this.customer = customer;
   }


   public Type getType() {
      return type;
   }


   public void setType(Type type) {
      this.type = type;
   }


   public Date getDate() {
      return date;
   }


   public void setDate(Date date) {
      this.date = date;
   }


   public String getComment() {
      return comment;
   }


   public void setComment(String comment) {
      this.comment = comment;
   }


   @Override public String toString() {
      return "SuspiciousOperation{" +
              "customer=" + customer +
              ", type=" + type +
              ", date=" + date +
              ", comment='" + comment + '\'' +
              '}';
   }
}
