# friendly-drools

Drools engine friendly wrapper

# Features

* User can load an external drools file into the rule engine to run
* User can load an external text string as drool rules into the rule engine to run
* User can load external domain specific language (DSL) files or text string
* user can load external decision table specified in external excel files

# Install

Add the following dependency to your POM file:

```xml
<dependency>
    <groupId>com.github.chen0040</groupId>
    <artifactId>friendly-drools</artifactId>
    <version>1.0.1</version>
</dependency>
```

# Usage

The sample codes can be found in the src/test/java folder. 

## Simple example of loading a an external text string as drool rules to run

```java

StatefulRuleEngine ruleRunner = new StatefulKieRuleEngine();

String ruleContent = FileUtils.readToEnd("tutorials/customer-functions.drl");

ruleRunner.addRules("tutorials/customer-functions.drl", ruleContent);
ruleRunner.buildKnowledgeSession();

Customer customer = new Customer(1L, "Jack", Customer.Category.SILVER);

ruleRunner.insert(customer);
ruleRunner.setGlobal("globalList", new ArrayList<>());

ruleRunner.fireAllRules();
List<String> globalList = ruleRunner.getGlobal("globalList", List.class);
globalList.forEach(c -> logger.info("customer: {}", c));
```

Where tutorials/customer-functions.drl is a simple drools file containing the following content:

```java
package tutorials;

import function com.github.chen0040.drools.tutorials.CustomerUtils.formatCustomer;
import com.github.chen0040.drools.tutorials.Customer;
import java.util.List;

global List globalList;

rule "Prepare customers list"
    when
        $c: Customer()
    then
        globalList.add(formatCustomer($c));
end
```

## Load External Drools File

The following codes loads the anomaly detection rules for customer orders from external files:

```java
StatefulRuleEngine ruleRunner = new StatefulKieRuleEngine();

ruleRunner.addClassPathRuleFile("tutorials", "tutorials/classify-item-rules.drl");
ruleRunner.addClassPathRuleFile("tutorials/classify-customer-rules.drl", "tutorials/classify-customer-rules.drl");
ruleRunner.addClassPathRuleFile("tutorials", "tutorials/coupon-creation-rules.drl");
ruleRunner.addClassPathRuleFile("tutorials/coupon-execution-rules.drl", "tutorials/coupon-execution-rules.drl");
ruleRunner.addClassPathRuleFile("tutorials", "tutorials/order-discount-rules.drl");

ruleRunner.buildKnowledgeSession();

Order o = ModelFactory.getOrderWithFiveHighRangeItems();

ruleRunner.insert(o.getCustomer());

for(int i=0; i < o.getOrderLines().size(); ++i) {
 ruleRunner.insert(o.getOrderLines().get(i));
 ruleRunner.insert(o.getOrderLines().get(i).getItem());
}

ruleRunner.insert(o);


int fired = ruleRunner.fireAllRules();


// We have 5 Items that are categorized -> 5 rules were fired
// We have 1 Customer that needs to be categorized -> 1 rule fired
// We have just one order with all HIGH RAnge items -> 1 rule fired
// One Coupon is created for the SILVER Customer -> 1 rule fired
// One Coupon is executed after its creation -> 1 rule fired
logger.info("rules fired: {}", fired);
assertThat(fired).isEqualTo(9);
assertThat(o.getCustomer().getCategory()).isEqualTo(Customer.Category.SILVER);
assertThat(o.getDiscount()).isNotNull();

for(int i=0; i < o.getOrderLines().size(); ++i) {
 assertThat(o.getOrderLines().get(i).getItem().getCategory()).isEqualTo(Item.Category.HIGH_RANGE);
}

List<Coupon> coupons = ruleRunner.getFacts(Coupon.class);

logger.info("orders: {}", ruleRunner.getFacts(Order.class).size());
logger.info("customers: {}", ruleRunner.getFacts(Customer.class).size());

assertThat(coupons.size()).isEqualTo(1);
```

## Load Excel Decision Table

The following codes how to load rules from external excel decision table.

```java
StatefulRuleEngine ruleRunner = new StatefulKieRuleEngine();

InputStream is = new FileInputStream("tutorials/classify-customer.xls");

String rules = ruleRunner.expandDecisionTable(is, DecisionTableInputType.XLS, null);
logger.info("rules: \n{}", rules);

ruleRunner.addDecisionTable("classify-customer.xls", is, DecisionTableInputType.XLS, null);

ruleRunner.buildKnowledgeSession();

for(int age = 16; age < 45; ++age){
 ruleRunner.insert(ModelFactory.getCustomerWithAge(age));
}

ruleRunner.fireAllRules();
```

## Load Dsl (Domain-Specific-Language) File

The following codes show how to load domain specific language (DSL) files as well as drools files written using DSL.

```java
StatefulRuleEngine ruleRunner = new StatefulKieRuleEngine();

String dslContent = FileUtils.readToEnd("tutorials/classify-customer.dsl");
String dslrContent = FileUtils.readToEnd("tutorials/classify-customer-rules.dslr");

String drlContent = RuleEngine.expandDSLRules(dslContent, dslrContent);
logger.info("expanded drl: \n{}", drlContent);

ruleRunner.addDsl("tutorials/classify-customer.dsl", dslContent);
ruleRunner.addDslRules("tutorials/classify-customer-rules.dslr", dslrContent);

ruleRunner.buildKnowledgeSession();

for(int age = 21; age <= 31; ++age) {
 ruleRunner.insert(ModelFactory.getCustomerWithAge(age));
}

ruleRunner.fireAllRules();
```

## CEP 

The following codes show how to use the rule engine for complex event processing by loading the cep rule files from external text string.

```java
StatefulRuleEngine ruleRunner = new StatefulKieRuleEngine();

ruleRunner.addRules("tutorials/transaction-cep-rules.drl", FileUtils.readToEnd("tutorials/transaction-cep-rules.drl"));

ruleRunner.enableEventStreaming();
ruleRunner.buildKnowledgeSession();

long now = new Date().getTime();

for(int i=0; i < 11; ++i) {
 ruleRunner.insert(new TransactionEvent(1L, 100.0, new Date(now - 10000 + 1000 * i), 100L));
}

ruleRunner.insert(new TransactionEvent(1L, 1010.0, new Date(now), 100L));

ruleRunner.fireAllRules();
```

## CEP with window declaration

The following codes show how to perform CEP with window declaration:

```java
StatefulRuleEngine ruleRunner = new StatefulKieRuleEngine();
ruleRunner.addRules("tutorials/heartbeat-cep-rules.drl", FileUtils.readToEnd("tutorials/heartbeat-cep-rules.drl"));

ruleRunner.enableEventStreaming();
ruleRunner.buildKnowledgeSession();

long now = new Date().getTime();

for(int i=0; i < 11; ++i) {
 ruleRunner.getSession().getEntryPoint("heart beat monitor").insert(new HeartBeat(new Date(now - 10000 + 5 * i), 100L));
}

ruleRunner.getSession().getEntryPoint("heart beat monitor").insert(new HeartBeat(new Date(now), 100L));

ruleRunner.fireAllRules();
```

## Load drools that interact with java delclared functions

The following codes shows how to work with drool file that access the java declared functions:

```java
StatefulRuleEngine ruleRunner = new StatefulKieRuleEngine();
ruleRunner.addRules("tutorials/order-total-rules.drl", FileUtils.readToEnd("tutorials/order-total-rules.drl"));
ruleRunner.addRules("tutorials/order-functions.drl", FileUtils.readToEnd("tutorials/order-functions.drl"));

ruleRunner.buildKnowledgeSession();

Customer customer1 = new Customer();
customer1.setCustomerId(1L);

Order customer1Order = ModelFactory.getPendingOrderWithTotalValueGreaterThan10000(customer1);

Customer customer2 = new Customer();
customer2.setCustomerId(2L);

Order customer2Order = ModelFactory.getPendingOrderWithTotalValueLessThan10000(customer1);


OrderService orderService = new OrderServiceImpl();
orderService.save(customer1Order);
orderService.save(customer2Order);

ruleRunner.insert(customer1);
ruleRunner.insert(customer2);

ruleRunner.setGlobal("biggestOrder", new BiggestOrder());
ruleRunner.setGlobal("orderService", orderService);

ruleRunner.fireAllRules();

BiggestOrder biggestOrder = ruleRunner.getGlobal("biggestOrder", BiggestOrder.class);
logger.info("biggest order: {}", biggestOrder.getObject());
```

The content of tutorials/order-functions.drl is shown below:

```java
package tutorials;

import accumulate com.github.chen0040.drools.tutorials.BiggestOrderFunction biggestOrder
import com.github.chen0040.drools.tutorials.BiggestOrder;

global BiggestOrder biggestOrder;

rule "Find Biggest Order"
when
    $bigO: Order() from accumulate(
        $o: Order(),
        biggestOrder($o)
    )
then
    biggestOrder.setObject($bigO);
end
```

Where BiggestOrderFunction is defined in the java code instead:
 
```java
 import org.kie.api.runtime.rule.AccumulateFunction;
 
 import java.io.*;

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
```
