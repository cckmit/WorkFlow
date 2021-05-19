package com.tsi.workflow.websocket;

import static org.atmosphere.cpr.ApplicationConfig.MAX_INACTIVE;

import com.google.gson.Gson;
import com.hazelcast.core.MessageListener;
import java.io.IOException;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.Heartbeat;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.DefaultMetaBroadcaster;
import org.atmosphere.cpr.MetaBroadcaster;

@ManagedService(path = "/workflowStatus", atmosphereConfig = MAX_INACTIVE + "=" + 24 * 60 * 60 * 1000)
public class WSBroadCaster implements MessageListener<String> {

    private static final Logger LOG = Logger.getLogger(WSBroadCaster.class.getName());
    // @Inject
    // private BroadcasterFactory factory;

    // @Inject
    // private AtmosphereResource r;
    @Inject
    private AtmosphereResourceEvent event;

    static MetaBroadcaster metaBroadcaster;

    @Override
    public void onMessage(com.hazelcast.core.Message<String> message) {
	String lMessage1 = message.getMessageObject();
	LOG.info("Got Message from Cache - " + lMessage1);
	WSSMessage lMessage = new Gson().fromJson(lMessage1, WSSMessage.class);
	lMessage.switchChannel();
	String lMessage2 = new Gson().toJson(lMessage);
	try {
	    metaBroadcaster.broadcastTo("/workflowStatus", lMessage1);
	    metaBroadcaster.broadcastTo("/workflowStatus", lMessage2);
	} catch (Exception e) {
	    LOG.error("", e);
	}
    }

    @Heartbeat
    public void onHeartbeat(final AtmosphereResourceEvent event) {
    }

    @Ready
    public void onReady(AtmosphereResource r) {
	if (metaBroadcaster == null) {
	    metaBroadcaster = new DefaultMetaBroadcaster();
	    metaBroadcaster.configure(r.getAtmosphereConfig());
	}
	// r.getBroadcaster().broadcast(r.uuid() + " connected");
	LOG.info(r.uuid() + " connected");
    }

    @Disconnect
    public void onDisconnect() {
	if (event.isCancelled()) {
	    LOG.info(event.getResource().uuid() + " unexpectedly disconnected");
	} else if (event.isClosedByClient()) {
	    LOG.info(event.getResource().uuid() + " closed the connection");
	}
    }

    @Message
    public String onMessage(String message) throws IOException {
	return message;
    }
}
