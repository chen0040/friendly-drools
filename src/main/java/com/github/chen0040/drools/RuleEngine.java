package com.github.chen0040.drools;


import com.github.chen0040.drools.models.RuleContent;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.lang.dsl.DefaultExpanderResolver;
import org.kie.api.event.KieRuntimeEventManager;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.builder.DecisionTableInputType;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;


/**
 * Created by xschen on 11/7/16.
 */
public interface RuleEngine {
   boolean addClassPathRuleFile(String packageName, String drlFileName);
   boolean addRuleFile(String rulefile, String filePath);

   default boolean addClassPathDslFile(String packageName, String drlFileName) { return addClassPathRuleFile(packageName, drlFileName);}
   default boolean addDslFile(String dslFile, String filePath) { return addRuleFile(dslFile, filePath); }

   default void addClassPathDslRuleFile(String packageName, String drlFileName) { addClassPathRuleFile(packageName, drlFileName);}
   default boolean addDslRuleFile(String dslFile, String filePath) { return addRuleFile(dslFile, filePath); }

   List<RuleContent> getRuleContents();

   Optional<RuleContent> addRules(String rulefile, String content);

   default Optional<RuleContent> addRules(String packageName, String drlFileName, String content) {
      if(!drlFileName.toLowerCase().endsWith(".drl")){
         drlFileName = drlFileName + ".drl";
      }
      return addRules(packageName.replace(".", "/") + "/" + drlFileName, content);
   }

   default Optional<RuleContent> addDsl(String rulefile, String content) {
      return addRules(rulefile, content);
   }
   default Optional<RuleContent> addDsl(String packageName, String drlFileName, String content) {
      if(!drlFileName.toLowerCase().endsWith(".dsl")){
         drlFileName = drlFileName + ".dsl";
      }
      return addDsl(packageName.replace(".", "/") + "/" + drlFileName, content);
   }

   default Optional<RuleContent> addDslRules(String rulefile, String filePath) {
      return addRules(rulefile, filePath);
   }

   default Optional<RuleContent> addDslRules(String packageName, String drlFileName, String content) {
      if(!drlFileName.toLowerCase().endsWith(".dslr")){
         drlFileName = drlFileName + ".dslr";
      }
      return addDslRules(packageName.replace(".", "/") + "/" + drlFileName, content);
   }

   static String expandDSLRules(String dslContent, String dslrContent) throws IOException, DroolsParserException {
      DrlParser parser = new DrlParser();
      DefaultExpanderResolver resolver = new DefaultExpanderResolver(new StringReader(dslContent));
      return parser.getExpandedDRL(dslrContent, resolver);
   }

   String expandDecisionTable(InputStream dtableIS, DecisionTableInputType inputType, String workSheetName) throws IOException;

   KieRuntimeEventManager buildKnowledgeSession();
   KieRuntimeEventManager getRunTime();
   KieContainer getContainer();

   boolean addDecisionTable(String filename, InputStream is, DecisionTableInputType inputType, String workSheetName);
}
