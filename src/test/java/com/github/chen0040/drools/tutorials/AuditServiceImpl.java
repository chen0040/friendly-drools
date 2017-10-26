package com.github.chen0040.drools.tutorials;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by xschen on 11/8/16.
 */
public class AuditServiceImpl implements AuditService {
   private static final Logger logger = LoggerFactory.getLogger(AuditServiceImpl.class);

   @Override public void notifySuspiciousOperation(SuspiciousOperation so) {
      logger.info("audit-sos: {}", so);
   }
}
