package com.github.chen0040.drools.tutorials;


import java.util.Date;


/**
 * Created by xschen on 11/7/16.
 */
public class Coupon {
   public enum CouponType {
      POINTS,
      NA
   }

   private long couponId;
   private Customer customer;
   private Date validFrom;
   private Date validUntil;
   private CouponType couponType = CouponType.NA;
   private Order order;

   public Coupon(Customer customer, Order order, CouponType couponType) {
      this.customer = customer;
      this.order = order;
      this.couponType = couponType;
   }


   public long getCouponId() {
      return couponId;
   }


   public void setCouponId(long couponId) {
      this.couponId = couponId;
   }


   public Customer getCustomer() {
      return customer;
   }


   public void setCustomer(Customer customer) {
      this.customer = customer;
   }


   public Date getValidFrom() {
      return validFrom;
   }


   public void setValidFrom(Date validFrom) {
      this.validFrom = validFrom;
   }


   public Date getValidUntil() {
      return validUntil;
   }


   public void setValidUntil(Date validUntil) {
      this.validUntil = validUntil;
   }


   public Order getOrder() {
      return order;
   }


   public void setOrder(Order order) {
      this.order = order;
   }
}
