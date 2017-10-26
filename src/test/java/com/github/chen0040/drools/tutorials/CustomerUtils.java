package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/8/16.
 */
public class CustomerUtils {
   public static String formatCustomer(Customer customer) {
      return String.format("[%s] %s", customer.getCategory(), customer.getName());
   }
}
