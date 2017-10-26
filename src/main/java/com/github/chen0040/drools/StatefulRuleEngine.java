package com.github.chen0040.drools;


import org.kie.api.runtime.Channel;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;

import java.util.List;


/**
 * Created by xschen on 11/7/16.
 */
public interface StatefulRuleEngine extends RuleEngine {
   FactHandle insert(Object fact);

   int fireAllRules(); // for discrete event rule firing
   void fireUntilHalt(); // for continuous event rule firing
   void halt(); // for continuous event rule firing

   void enableEventStreaming();
   void enablePseudoClock();


   <T> List<T> getFacts(Class<T> clazz);

   void setGlobal(String identifier, Object value);
   <T> T getGlobal(String identifier, Class<T> clazz);

   void registerChannel(String channelIdentifier, Channel channel);
   void unregisterChannel(String channelIdentifier);

   void dispose();
   KieSession getSession();

   QueryResults getQueryResult(String queryIdentifier, Object... args);


}
