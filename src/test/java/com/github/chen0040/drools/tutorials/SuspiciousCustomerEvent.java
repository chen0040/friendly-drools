package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/8/16.
 */
public class SuspiciousCustomerEvent {
   private long customerId;
   private String reason;

   public SuspiciousCustomerEvent(){

   }

   public SuspiciousCustomerEvent(long customerId, String reason) {
      this.customerId = customerId;
      this.reason = reason;
   }

   public long getCustomerId() {
      return customerId;
   }


   public void setCustomerId(long customerId) {
      this.customerId = customerId;
   }


   public String getReason() {
      return reason;
   }


   public void setReason(String reason) {
      this.reason = reason;
   }
}
