package com.github.chen0040.drools.models;


/**
 * Created by xschen on 23/12/2016.
 */
public class RuleContent {
   private String path;
   private String topic;
   private String content;


   public String getPath() {
      return path;
   }


   public void setPath(String path) {
      this.path = path;
   }


   public String getTopic() {
      return topic;
   }


   public void setTopic(String category) {
      this.topic = category;
   }


   public String getContent() {
      return content;
   }


   public void setContent(String content) {
      this.content = content;
   }
}
