package com.github.chen0040.drools;

import org.drools.core.ClockType;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.event.KieRuntimeEventManager;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by xschen on 11/7/16.
 */
public class StatefulKieRuleEngine extends AbstractRuleEngine implements StatefulRuleEngine {

   private static final Logger logger = LoggerFactory.getLogger(StatefulKieRuleEngine.class);

   private KieSession kieSession;
   private boolean eventStreamingEnabled = false;
   private boolean pseudoClockEnabled = false;

   public StatefulKieRuleEngine(boolean eventStreamingEnabled) {
      this.eventStreamingEnabled = eventStreamingEnabled;
   }

   public StatefulKieRuleEngine(){

   }



   @Override
   protected KieRuntimeEventManager createSession()
   {

      KieBaseConfiguration kieBaseConfiguration = kieServices.newKieBaseConfiguration();
      if(eventStreamingEnabled) {
         kieBaseConfiguration.setOption(EventProcessingOption.STREAM); //default is CLOUD
      }

      KieBase kieBase = kieContainer.newKieBase(kieBaseConfiguration);

      kieSession = kieBase.newKieSession();

      if(pseudoClockEnabled)
      {
         kieSession.getSessionConfiguration().setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
      }

      return kieSession;
   }

   public void enableEventStreaming(){
      eventStreamingEnabled = true;
   }

   public void enablePseudoClock(){
      pseudoClockEnabled = true;
   }

   public void fireUntilHalt(){
      kieSession.fireUntilHalt();
   }

   public void halt(){
      kieSession.halt();
   }



   public int fireAllRules()
   {
      return kieSession.fireAllRules();
   }

   @Override
   public KieSession getSession(){
      return kieSession;
   }


   @Override public QueryResults getQueryResult(String queryIdentifier, Object... args) {
      return kieSession.getQueryResults(queryIdentifier, args);
   }


   @Override
   public void dispose()
   {
      if(kieSession != null) {
         this.kieSession.dispose();
      }
   }

   @Override public <T> List<T> getFacts(Class<T> clazz) {
      return kieSession.getObjects(new ClassObjectFilter(clazz)).stream()
              .map(c -> (T)c)
              .collect(Collectors.toList());
   }


   @Override public void setGlobal(String identifier, Object value) {
      kieSession.setGlobal(identifier, value);
   }


   @Override public <T> T getGlobal(String identifier, Class<T> clazz) {
      return (T)kieSession.getGlobal(identifier);
   }


   @Override public void registerChannel(String channelIdentifier, Channel channel) {
      kieSession.registerChannel(channelIdentifier, channel);
   }


   @Override public void unregisterChannel(String channelIdentifier) {
      kieSession.unregisterChannel(channelIdentifier);
   }


   @Override public FactHandle insert(Object fact) {
      return kieSession.insert(fact);
   }
}
