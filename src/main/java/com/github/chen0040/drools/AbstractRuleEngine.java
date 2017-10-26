package com.github.chen0040.drools;


import com.github.chen0040.drools.models.RuleContent;
import org.apache.poi.util.IOUtils;
import org.drools.core.util.StringUtils;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.event.KieRuntimeEventManager;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.builder.DecisionTableInputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by xschen on 11/7/16.
 */
public abstract class AbstractRuleEngine implements RuleEngine {

   private static final Logger logger = LoggerFactory.getLogger(AbstractRuleEngine.class);

   protected KieServices kieServices;
   protected KieContainer kieContainer;
   protected KieResources kieResources;
   protected KieFileSystem kieFileSystem;
   protected KieRepository kieRepository;
   protected KieRuntimeEventManager runtime;

   protected Set<String> resourcePaths = new HashSet<>();
   private Map<String, RuleContent> ruleContents = new HashMap<>();




   @Override public String expandDecisionTable(InputStream dtableIS, DecisionTableInputType inputType, String workSheetName) throws IOException {


      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      IOUtils.copy(dtableIS, baos);
      byte[] bytes = baos.toByteArray();
      baos.close();

      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

      SpreadsheetCompiler compiler = new SpreadsheetCompiler();

      String result;
      if(!StringUtils.isEmpty(workSheetName)){
         result = compiler.compile(bais, workSheetName);
      } else {
         InputType type = inputType == DecisionTableInputType.CSV ? InputType.CSV : InputType.XLS;
         result = compiler.compile(bais, type);
      }

      bais.close();


      return result;


   }


   public AbstractRuleEngine(){
      this.kieServices = KieServices.Factory.get();
      this.kieResources = kieServices.getResources();
      this.kieFileSystem = kieServices.newKieFileSystem();
      this.kieRepository = kieServices.getRepository();
   }

   @Override public boolean addClassPathRuleFile(String packagename, String rulefile) {
      if(rulefile.contains("\\")){
         rulefile = rulefile.replace("\\", "/");
      }

      Resource resource = kieResources.newClassPathResource(rulefile);

      String ruleFileName = rulefile;
      if(rulefile.contains("/")) {
         ruleFileName = rulefile.substring(rulefile.lastIndexOf("/") + 1);
      }

      packagename = packagename.replace(".","/");
      String resourcepath = "src/main/resources/"+packagename+"/"+ruleFileName;

      if(resourcePaths.contains(resourcepath)) {
         logger.warn("adding rule is rejected as the resource path already added: {}", resourcepath);
         return false;
      }

      kieFileSystem.write(resourcepath, resource);
      resourcePaths.add(resourcepath);

      return true;
   }

   @Override public boolean addDecisionTable(String filename, InputStream is, DecisionTableInputType inputType, String workSheetName) {
      Resource resource = kieResources.newInputStreamResource(is);

      String filenameLower = filename.toLowerCase();
      if(inputType == DecisionTableInputType.XLS && !filenameLower.endsWith(".xls")) {
         filename = filename + ".xls";
      }
      if(inputType == DecisionTableInputType.CSV && !filenameLower.endsWith(".csv")) {
         filename = filename + ".csv";
      }
      if(inputType == DecisionTableInputType.XLSX && !filenameLower.endsWith(".xlsx")) {
         filename = filename + ".xlsx";
      }

      String resourcepath = "src/main/resources/" + filename;

      if(resourcePaths.contains(resourcepath)) {
         logger.warn("adding rule is rejected as the resource path already added: {}", resourcepath);
         return false;
      }

      kieFileSystem.write(resourcepath, resource);
      resourcePaths.add(resourcepath);

      return true;
   }


   @Override public boolean addRuleFile(String rulefile, String filepath) {
      Resource resource = null;
      try {
         resource = kieResources.newByteArrayResource(Files.readAllBytes(Paths.get(filepath)));
      }
      catch (IOException e) {
         logger.error("Failed to read file " + filepath, e);
      }

      if(resource == null){
         return false;
      }

      String resourcepath = "src/main/resources/"+rulefile;

      if(resourcePaths.contains(resourcepath)) {
         logger.warn("adding rule is rejected as the resource path already added: {}", resourcepath);
         return false;
      }

      kieFileSystem.write(resourcepath, resource);
      resourcePaths.add(resourcepath);

      return true;
   }


   @Override public Optional<RuleContent> addRules(String rulefile, String content) {

      Resource resource = null;
      try {
         resource = kieResources.newByteArrayResource(content.getBytes("UTF-8"));
      }
      catch (UnsupportedEncodingException e) {
         logger.error("failed to convert " + content, e);
      }

      if(resource == null) {
         return Optional.empty();
      }

      String resourcepath = "src/main/resources/"+rulefile;

      if(resourcePaths.contains(resourcepath)){
         logger.warn("adding rule is rejected as the resource path already added: {}", resourcepath);
         return Optional.empty();
      }

      kieFileSystem.write(resourcepath, resource);
      resourcePaths.add(resourcepath);

      RuleContent ruleContent = new RuleContent();
      ruleContent.setPath(rulefile);
      ruleContent.setContent(content);
      ruleContents.put(rulefile, ruleContent);

      return Optional.of(ruleContent);
   }

   @Override
   public List<RuleContent> getRuleContents(){
      List<RuleContent> contents = new ArrayList<>();
      contents.addAll(ruleContents.values().stream().collect(Collectors.toList()));
      return contents;
   }


   @Override public KieRuntimeEventManager buildKnowledgeSession() {
      KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);

      kb.buildAll();

      if (kb.getResults().hasMessages(Message.Level.ERROR))
      {
         throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
      }

      kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());

      return runtime = createSession();
   }

   protected abstract KieRuntimeEventManager createSession();


   @Override public KieRuntimeEventManager getRunTime() {
      return runtime;
   }

   public KieContainer getContainer() {
      return kieContainer;
   }




}
