package com.github.chen0040.drools.tutorials;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xschen on 11/8/16.
 */
public class OrderServiceImpl implements OrderService {
   private List<Order> orders = new ArrayList<>();

   @Override public List<Order> getOrdersByCustomer(Customer customer) {
      return orders.stream().filter(o -> o.getCustomer().equals(customer)).collect(Collectors.toList());
   }


   @Override public Order save(Order order) {
      orders.add(order);
      return order;
   }


   @Override public List<Order> findAll() {
      return orders;
   }
}
