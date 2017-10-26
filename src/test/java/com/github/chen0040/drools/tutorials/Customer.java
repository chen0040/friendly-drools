package com.github.chen0040.drools.tutorials;


/**
 * Created by xschen on 11/7/16.
 */
public class Customer {
   public Customer(long id, String name, Category category) {
      this.customerId = id;
      this.name = name;
      this.category = category;
   }
   public Customer(){

   }


   public enum Category {
      NA,
      SILVER,
      GOLD,
      BRONZE
   }

   private Category category = Category.NA;
   private String name;
   private long customerId;
   private int age = 18;


   public Category getCategory() {
      return category;
   }


   public void setCategory(Category category) {
      this.category = category;
   }


   public String getName() {
      return name;
   }


   public void setName(String name) {
      this.name = name;
   }


   public long getCustomerId() {
      return customerId;
   }


   public void setCustomerId(long customerId) {
      this.customerId = customerId;
   }


   @Override public String toString() {
      return "Customer{" +
              "category=" + category +
              ", name='" + name + '\'' +
              ", customerId=" + customerId +
              ", age=" + age +
              '}';
   }


   public int getAge() {
      return age;
   }


   public void setAge(int age) {
      this.age = age;
   }


   @Override public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Customer customer = (Customer) o;

      if (customerId != customer.customerId)
         return false;
      if (category != customer.category)
         return false;
      return name != null ? name.equals(customer.name) : customer.name == null;

   }


   @Override public int hashCode() {
      int result = category != null ? category.hashCode() : 0;
      result = 31 * result + (name != null ? name.hashCode() : 0);
      result = 31 * result + (int) (customerId ^ (customerId >>> 32));
      return result;
   }
}
