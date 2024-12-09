package info.modprobe.crdt.demo;

import info.modprobe.crdt.BasicPNCounter;
import info.modprobe.crdt.PNCounter;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CounterClusterMessageHandler implements ClusterMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(CounterClusterMessageHandler.class);

    private final PNCounter counter;
    public CounterClusterMessageHandler( final PNCounter counter) {
        this.counter = counter;
    }


    public void onMessage(final Message message) {
        logger.debug(message.toString());
    }

    public void onGossip(final Message gossip) {
        logger.debug("Received a gossip message");

        final PNCounter otherCounter = (BasicPNCounter) gossip.data();

        counter.mergeExternal(otherCounter);
    }

    public void onMembershipEvent(final MembershipEvent event) {
        logger.debug(event.toString());
    }
}
