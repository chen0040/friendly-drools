package com.github.chen0040.drools.tutorials;


import org.kie.api.runtime.rule.AccumulateFunction;

import java.io.*;


/**
 * Created by xschen on 11/8/16.
 */
public class BiggestOrderFunction implements AccumulateFunction {

   public static class Context implements Externalizable {
      public Order maxOrder = null;
      public double maxTotal = - Double.MAX_VALUE;

      @Override public void writeExternal(ObjectOutput out) throws IOException {

      }


      @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

      }
   }

   @Override public Serializable createContext() {
      return new Context();
   }


   @Override public void init(Serializable serializable) throws Exception {

   }


   @Override public void accumulate(Serializable context, Object value) {
      Context c = (Context) context;
      Order order = (Order) value;
      double discount = order.getDiscount() == null ? 0 : order.getDiscount().getPercentage();
      double orderTotal = order.getTotal() - (order.getTotal() * discount);
      if(orderTotal > c.maxTotal) {
         c.maxTotal = orderTotal;
         c.maxOrder = order;
      }
   }


   @Override public void reverse(Serializable serializable, Object o) throws Exception {

   }


   @Override public Object getResult(Serializable context) throws Exception {
      return ((Context)context).maxOrder;
   }


   @Override public boolean supportsReverse() {
      return false;
   }


   @Override public Class<?> getResultType() {
      return Order.class;
   }


   @Override public void writeExternal(ObjectOutput out) throws IOException {

   }


   @Override public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

   }
}
