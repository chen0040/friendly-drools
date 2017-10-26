package com.github.chen0040.drools.tutorials;


import java.util.List;


/**
 * Created by xschen on 11/8/16.
 */
public interface OrderService {
   List<Order> getOrdersByCustomer(Customer customer);
   Order save(Order order);
   List<Order> findAll();
}
