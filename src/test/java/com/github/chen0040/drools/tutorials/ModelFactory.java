package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/7/16.
 */
public class ModelFactory {
   public static Order getOrderWithFiveHighRangeItems(){
      Order order = new Order();
      Item[] items = new Item[] {
              new Item("A", 201, 301),
              new Item("B", 202, 302),
              new Item("C", 203, 303),
              new Item("D", 204, 304),
              new Item("E", 205, 305)
      };
      for(int i=0; i < items.length; ++i){
         order.addLine(items[i], 10);
      }

      Customer customer = new Customer();
      customer.setName("consumer-1");
      order.setCustomer(customer);

      return order;
   }


   public static Order getPendingOrderWithTotalValueGreaterThan10000(Customer customer) {
      Order order = new Order();
      Item[] items = new Item[] {
              new Item("A", 2010, 3010),
              new Item("B", 2020, 3020),
              new Item("C", 2030, 3030),
              new Item("D", 2040, 3040),
              new Item("E", 2050, 3050)
      };
      for(int i=0; i < items.length; ++i){
         order.addLine(items[i], 2);
      }

      order.setCustomer(customer);

      return order;
   }


   public static Order getPendingOrderWithTotalValueLessThan10000(Customer customer) {
      Order order = new Order();
      Item[] items = new Item[] {
              new Item("A", 201, 301),
              new Item("B", 202, 302),
              new Item("C", 203, 303),
              new Item("D", 204, 304),
              new Item("E", 205, 305)
      };
      for(int i=0; i < items.length; ++i){
         order.addLine(items[i], 2);
      }

      order.setCustomer(customer);

      return order;
   }


   public static Customer getCustomerWithAge(int age) {
      Customer customer = new Customer();
      customer.setAge(age);
      customer.setCustomerId(age - 20);
      return customer;
   }
}
