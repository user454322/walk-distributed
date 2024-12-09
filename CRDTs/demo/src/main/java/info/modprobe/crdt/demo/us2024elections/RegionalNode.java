package info.modprobe.crdt.demo.us2024elections;

import io.scalecube.cluster.Cluster;
import io.scalecube.net.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;

import java.util.Random;
import java.util.UUID;

public class RegionalNode {

    private static final Logger logger = LoggerFactory.getLogger(RegionalNode.class);

    private final String id;
    private final int port;
    private final Address address;
    private static Address seed;
    private static final Object INIT_LOCK = new Object();
    private final Cluster cluster;
    private final String location;
    private final Counter counter;

    public RegionalNode(final String location) {
        this.location = location;
        final String host = "127.0.0.1";
        port = new Random().nextInt(1030, 59_700);
        id = location + port;
        address = Address.create(host, port);
        counter = new Counter(location);

        synchronized (INIT_LOCK) {
            if (seed == null) {
                seed = address;
                logger.info(" Started cluster with seed " + seed + " in " + location);
            }
        }
        cluster = start();
    }

    private boolean isSeed() {
        return seed.equals(address);
    }

    private Address getSeed() {
        return seed;
    }

    private Cluster start() {
        final ClusterConfig config = new ClusterConfig()
                .memberAlias(id)
                .transport(opts -> opts.port(port));

        Cluster cluster = null;
        if (!isSeed()) {
            cluster = new ClusterImpl(config)
                    .membership(opts -> opts.seedMembers(getSeed()))
                    .transportFactory(TcpTransportFactory::new)
                    .handler(handler -> new MessageHandler(counter))
                    .startAwait();

        } else {
            cluster = new ClusterImpl(config)
                    .transportFactory(TcpTransportFactory::new)
                    .handler(handler -> new MessageHandler(counter))
                    .startAwait();
        }
        logger.debug(id + " started");
        return  cluster;
    }


    public void sync() {
        logger.debug(id + " sync in progress");
        cluster.spreadGossip(Message.fromData(counter))
                .doOnError(error -> logger.error(id + " " + error))
                .subscribe(null, Throwable::printStackTrace);
    }


    public long voteForTrump() {
        long value = counter.voteForTrump();
        logger.info("Trump incremented to " + value + " in " + location);
        return value;
    }

    public long voteForKamala() {
        long value = counter.voteForKamala();
        logger.info("Kamala incremented to " + value + " in " + location);
        return value;
    }

    public Counter.Results getNationalResults() {
        return counter.getResults();
    }

}
