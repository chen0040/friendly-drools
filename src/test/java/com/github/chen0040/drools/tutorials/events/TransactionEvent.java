package com.github.chen0040.drools.tutorials.events;


import org.kie.api.definition.type.Duration;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;


/**
 * Created by xschen on 11/8/16.
 */
@Role(Role.Type.EVENT)
@Duration("duration")
@Timestamp("timestamp")
@Expires("2h30m")
public class TransactionEvent {
   private Long duration;
   private Date timestamp;
   private double totalAmount;
   private long customerId;

   public TransactionEvent(){

   }

   public TransactionEvent(long customerId, double totalAmount, Date timestamp, long duration) {
      this.customerId = customerId;
      this.totalAmount = totalAmount;
      this.timestamp = timestamp;
      this.duration = duration;
   }


   public Long getDuration() {
      return duration;
   }


   public void setDuration(Long duration) {
      this.duration = duration;
   }


   public Date getTimestamp() {
      return timestamp;
   }


   public void setTimestamp(Date timestamp) {
      this.timestamp = timestamp;
   }


   public double getTotalAmount() {
      return totalAmount;
   }


   public void setTotalAmount(double totalAmount) {
      this.totalAmount = totalAmount;
   }


   public long getCustomerId() {
      return customerId;
   }


   public void setCustomerId(long customerId) {
      this.customerId = customerId;
   }
}
