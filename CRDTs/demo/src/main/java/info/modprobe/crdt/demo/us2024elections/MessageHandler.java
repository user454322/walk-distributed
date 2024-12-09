package info.modprobe.crdt.demo.us2024elections;

import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.membership.MembershipEvent;
import io.scalecube.cluster.transport.api.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class MessageHandler implements ClusterMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final Counter counter;
    MessageHandler(final Counter counter) {
        this.counter = counter;
    }


    public void onMessage(final Message message) {
        logger.debug(message.toString());
    }

    public void onGossip(final Message gossip) {
        logger.debug("Received a gossip message");

        final Counter otherCounter = (Counter) gossip.data();
        counter.merge(otherCounter);
    }

    public void onMembershipEvent(final MembershipEvent event) {
        logger.debug(event.toString());
    }
}
