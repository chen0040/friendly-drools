package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/7/16.
 */
public class IsCouponExecuted {
   private Coupon coupon;

   public IsCouponExecuted(Coupon coupon) {
      this.coupon = coupon;
   }

   public IsCouponExecuted(){

   }


   public Coupon getCoupon() {
      return coupon;
   }


   public void setCoupon(Coupon coupon) {
      this.coupon = coupon;
   }
}
