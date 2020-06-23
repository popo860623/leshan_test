package org.eclipse.leshan.client.demo;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.Severity;

import org.eclipse.leshan.client.resource.BaseInstanceEnabler;
import org.eclipse.leshan.client.servers.ServerIdentity;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class FirmUpdater extends BaseInstanceEnabler{
	
	private final ScheduledExecutorService scheduler;
	private static final Logger LOG = LoggerFactory.getLogger(FirmUpdater.class);
	public FirmUpdater() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Firmware Updater"));
        // This method sets up a ScheduledExecutorService to execute every 10 seconds
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                
            }
        }, 3, 3, TimeUnit.SECONDS);
    }
	
	@Override
	public ExecuteResponse execute(ServerIdentity identity, int resourceid, String params) {		
		String withParams = null;
        if (params != null && params.length() != 0)
            withParams = " with params " + params;
		LOG.info("Execute on Device resource /{}/{}/{} {}", getModel().id, getId(), resourceid,
                withParams != null ? withParams : "");
		
		
		
		return ExecuteResponse.success();
	}

}
