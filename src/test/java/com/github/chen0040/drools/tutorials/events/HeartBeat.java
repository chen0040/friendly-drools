package com.github.chen0040.drools.tutorials.events;


import org.kie.api.definition.type.Duration;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;


/**
 * Created by xschen on 11/8/16.
 */
@Role(Role.Type.EVENT)
@Duration("duration")
@Timestamp("timestamp")
@Expires("2h30m")
public class HeartBeat {
   private Long duration;
   private Date timestamp;


   public HeartBeat(Date timestamp, long duration) {
      this.timestamp = timestamp;
      this.duration = duration;
   }

   public HeartBeat(){

   }


   public Long getDuration() {
      return duration;
   }


   public void setDuration(Long duration) {
      this.duration = duration;
   }


   public Date getTimestamp() {
      return timestamp;
   }


   public void setTimestamp(Date timestamp) {
      this.timestamp = timestamp;
   }
}
