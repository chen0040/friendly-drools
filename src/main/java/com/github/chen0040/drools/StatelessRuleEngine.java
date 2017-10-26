package com.github.chen0040.drools;


import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;

import java.util.List;


/**
 * Created by xschen on 11/7/16.
 */
public interface StatelessRuleEngine extends RuleEngine {
   ExecutionResults execute(List<Command> commands);
   void insert(List<Command> commands, String identifier, Object obj);
   void setGlobal(List<Command> commands, String identifier, Object obj, boolean out);
   void query(List<Command> commands, String identifier, String query);
   StatelessKieSession getSession();
}
