package com.github.chen0040.drools;


import org.kie.api.command.Command;
import org.kie.api.event.KieRuntimeEventManager;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;

import java.util.List;


/**
 * Created by xschen on 11/7/16.
 */
public class StatelessKieRuleEngine extends AbstractRuleEngine implements StatelessRuleEngine {
   private StatelessKieSession kieSession;
   @Override protected KieRuntimeEventManager createSession() {
      return kieSession = kieContainer.newStatelessKieSession();
   }

   public ExecutionResults execute(List<Command> commands) {
      return kieSession.execute(CommandFactory.newBatchExecution(commands));
   }


   @Override public void insert(List<Command> commands, String identifier, Object obj) {
      commands.add(CommandFactory.newInsert(obj, identifier));
   }


   @Override public void setGlobal(List<Command> commands, String identifier, Object obj, boolean out) {
      commands.add(CommandFactory.newSetGlobal(identifier, obj, out));
   }


   @Override public void query(List<Command> commands, String identifier, String query) {
      commands.add(CommandFactory.newQuery(identifier, query));
   }


   @Override public StatelessKieSession getSession() {
      return kieSession;
   }

}
