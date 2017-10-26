package com.github.chen0040.drools;


import com.github.chen0040.drools.tutorials.*;
import com.github.chen0040.drools.tutorials.events.HeartBeat;
import com.github.chen0040.drools.tutorials.events.TransactionEvent;
import com.github.chen0040.drools.utils.FileUtils;
import org.drools.compiler.compiler.DroolsParserException;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.internal.builder.DecisionTableInputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * Created by xschen on 11/7/16.
 */
public class StatefulRuleRunnerKieRuleEngineUnitTest {

   private StatefulRuleEngine ruleRunner;
   private static final Logger logger = LoggerFactory.getLogger(StatefulRuleRunnerKieRuleEngineUnitTest.class);

   @BeforeMethod
   public void setUp(){
      ruleRunner = new StatefulKieRuleEngine();
   }

   @AfterMethod
   public void tearDown(){
      ruleRunner.dispose();
   }

   @Test
   public void testGlobalAndChannel(){
      ruleRunner.addRules("tutorials/order-total-rules.drl", FileUtils.readToEnd("tutorials/order-total-rules.drl"));
      ruleRunner.addRules("tutorials/anomaly-detection-rules.drl", FileUtils.readToEnd("tutorials/anomaly-detection-rules.drl"));

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

      AuditService auditService = new AuditServiceImpl();

      ruleRunner.insert(customer1);
      ruleRunner.insert(customer2);

      ruleRunner.setGlobal("orderService", orderService);
      ruleRunner.setGlobal("amountThreshold", 10000.00);
      ruleRunner.setGlobal("results", new HashSet<>());
      ruleRunner.setGlobal("auditService", auditService);

      ruleRunner.registerChannel("audit-channel", o -> logger.info("receive from channel: {}", o));

      ruleRunner.fireAllRules();

      logger.info("customer1: {}", customer1);
      logger.info("customer2: {}", customer2);

      List<SuspiciousOperation> soList = ruleRunner.getFacts(SuspiciousOperation.class);
      soList.stream().forEach(so -> logger.info("suspicious operation captured: {}", so));

      Set<SuspiciousOperation> results = ruleRunner.getGlobal("results", Set.class);
      results.forEach(so -> logger.info("suspicious operation from result set: {}", so));


   }

   @Test
   public void testQuery(){
      ruleRunner.addRules("tutorials/order-total-rules.drl", FileUtils.readToEnd("tutorials/order-total-rules.drl"));
      ruleRunner.addRules("tutorials/anomaly-detection-rules.drl", FileUtils.readToEnd("tutorials/anomaly-detection-rules.drl"));
      ruleRunner.addRules("tutorials/anomaly-detection-result-queries.drl", FileUtils.readToEnd("tutorials/anomaly-detection-result-queries.drl"));

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

      AuditService auditService = new AuditServiceImpl();

      ruleRunner.insert(customer1);
      ruleRunner.insert(customer2);

      ruleRunner.setGlobal("orderService", orderService);
      ruleRunner.setGlobal("amountThreshold", 10000.00);
      ruleRunner.setGlobal("results", new HashSet<>());
      ruleRunner.setGlobal("auditService", auditService);

      ruleRunner.registerChannel("audit-channel", o -> logger.info("receive from channel: {}", o));

      ruleRunner.fireAllRules();


      QueryResults queryResults = ruleRunner.getQueryResult("Get all suspicious operations");
      for(QueryResultsRow row : queryResults) {
         SuspiciousOperation so = (SuspiciousOperation) row.get("$so");
         logger.info("query-so: {}", so);
      }

      QueryResults queryResults2 = ruleRunner.getQueryResult("Get customer suspicious operations", 1L);
      for(QueryResultsRow row : queryResults2) {
         SuspiciousOperation so = (SuspiciousOperation) row.get("$so");
         logger.info("query2-so: {}", so);
      }


   }

   @Test
   public void testFunctions(){
      ruleRunner.addRules("tutorials/customer-functions.drl", FileUtils.readToEnd("tutorials/customer-functions.drl"));
      ruleRunner.buildKnowledgeSession();

      Customer customer = new Customer(1L, "Jack", Customer.Category.SILVER);

      ruleRunner.insert(customer);
      ruleRunner.setGlobal("globalList", new ArrayList<>());

      ruleRunner.fireAllRules();

      List<String> globalList = ruleRunner.getGlobal("globalList", List.class);
      globalList.forEach(c -> logger.info("customer: {}", c));
   }

   @Test
   public void testAccumulateFunctions(){
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


   }

   @Test
   public void testCEP() {

      ruleRunner.addRules("tutorials/transaction-cep-rules.drl", FileUtils.readToEnd("tutorials/transaction-cep-rules.drl"));

      ruleRunner.enableEventStreaming();
      ruleRunner.buildKnowledgeSession();

      long now = new Date().getTime();

      for(int i=0; i < 11; ++i) {
         ruleRunner.insert(new TransactionEvent(1L, 100.0, new Date(now - 10000 + 1000 * i), 100L));
      }

      ruleRunner.insert(new TransactionEvent(1L, 1010.0, new Date(now), 100L));

      ruleRunner.fireAllRules();
   }

   @Test
   public void testCEP_windowDeclaration(){
      ruleRunner.addRules("tutorials/heartbeat-cep-rules.drl", FileUtils.readToEnd("tutorials/heartbeat-cep-rules.drl"));

      ruleRunner.enableEventStreaming();
      ruleRunner.buildKnowledgeSession();

      long now = new Date().getTime();

      for(int i=0; i < 11; ++i) {
         ruleRunner.getSession().getEntryPoint("heart beat monitor").insert(new HeartBeat(new Date(now - 10000 + 5 * i), 100L));
      }

      ruleRunner.getSession().getEntryPoint("heart beat monitor").insert(new HeartBeat(new Date(now), 100L));

      ruleRunner.fireAllRules();
   }

   @Test
   public void testDsl() throws IOException, DroolsParserException {
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


   }

   @Test
   public void testDecisionTable() throws IOException {
      InputStream is = FileUtils.getResourceStream("tutorials/classify-customer.xls");

      String rules = ruleRunner.expandDecisionTable(is, DecisionTableInputType.XLS, null);
      logger.info("rules: \n{}", rules);


      ruleRunner.addDecisionTable("classify-customer.xls", is, DecisionTableInputType.XLS, null);

      ruleRunner.buildKnowledgeSession();

      for(int age = 16; age < 45; ++age){
         ruleRunner.insert(ModelFactory.getCustomerWithAge(age));
      }

      ruleRunner.fireAllRules();

   }

   @Test
   public void testEventListeners(){
      ruleRunner.addRules("tutorials/order-total-rules.drl", FileUtils.readToEnd("tutorials/order-total-rules.drl"));
      ruleRunner.addRules("tutorials/anomaly-detection-rules.drl", FileUtils.readToEnd("tutorials/anomaly-detection-rules.drl"));
      ruleRunner.addRules("tutorials/anomaly-detection-result-queries.drl", FileUtils.readToEnd("tutorials/anomaly-detection-result-queries.drl"));


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

      AuditService auditService = new AuditServiceImpl();

      ruleRunner.insert(customer1);
      ruleRunner.insert(customer2);

      ruleRunner.setGlobal("orderService", orderService);
      ruleRunner.setGlobal("amountThreshold", 10000.00);
      ruleRunner.setGlobal("results", new HashSet<>());
      ruleRunner.setGlobal("auditService", auditService);

      ruleRunner.registerChannel("audit-channel", o -> logger.info("receive from channel: {}", o));

      ruleRunner.getSession().addEventListener(new RuleRuntimeEventListener() {
         @Override public void objectInserted(ObjectInsertedEvent objectInsertedEvent) {

         }


         @Override public void objectUpdated(ObjectUpdatedEvent objectUpdatedEvent) {

         }


         @Override public void objectDeleted(ObjectDeletedEvent objectDeletedEvent) {

         }
      });

      ruleRunner.getSession().addEventListener(new AgendaEventListener() {
         @Override public void matchCreated(MatchCreatedEvent matchCreatedEvent) {

         }


         @Override public void matchCancelled(MatchCancelledEvent matchCancelledEvent) {

         }


         @Override public void beforeMatchFired(BeforeMatchFiredEvent beforeMatchFiredEvent) {
            logger.info("before match fired: {}", beforeMatchFiredEvent.getMatch().getRule().getName());
         }


         @Override public void afterMatchFired(AfterMatchFiredEvent afterMatchFiredEvent) {
            logger.info("after match fired: {}", afterMatchFiredEvent.getMatch().getRule().getName());
         }


         @Override public void agendaGroupPopped(AgendaGroupPoppedEvent agendaGroupPoppedEvent) {

         }


         @Override public void agendaGroupPushed(AgendaGroupPushedEvent agendaGroupPushedEvent) {

         }


         @Override public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {

         }


         @Override public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {

         }


         @Override public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

         }


         @Override public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

         }
      });

      ruleRunner.fireAllRules();




   }

   @Test
   public void testRuleRunner(){
      ruleRunner.addClassPathRuleFile("tutorials", "tutorials/classify-item-rules.drl");
      ruleRunner.addRules("tutorials/classify-customer-rules.drl", FileUtils.readToEnd("tutorials/classify-customer-rules.drl"));
      ruleRunner.addClassPathRuleFile("tutorials", "tutorials/coupon-creation-rules.drl");
      ruleRunner.addRules("tutorials/coupon-execution-rules.drl", FileUtils.readToEnd("tutorials/coupon-execution-rules.drl"));
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


   }
}
