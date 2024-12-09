package info.modprobe.crdt.demo;

import info.modprobe.crdt.BasicPNCounter;
import info.modprobe.crdt.PNCounter;
import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterConfig;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import io.scalecube.transport.netty.tcp.TcpTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

public class CounterNode {

    private static final Logger logger = LoggerFactory.getLogger(CounterNode.class);

    public static final String sessionId = "session-" + UUID.randomUUID();

    private final PNCounter counter;
    private final String id;
    private final int port;
    private final Address address;
    private static Address seed;
    private static final Object INIT_LOCK = new Object();
    private final Cluster cluster;

    public CounterNode() {
        final String host = "127.0.0.1";
        port = new Random().nextInt(1030, 59_700);
        id = sessionId + "-" + host + ":" + port;
        address = Address.create(host, port);
        counter = new BasicPNCounter();

        synchronized (INIT_LOCK) {
            if (seed == null) {
                seed = address;
                logger.info(sessionId + " Started cluster with seed " + host + ":" + port);
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

    public Cluster start() {
        final ClusterConfig config =
                new ClusterConfig()
                        .memberAlias(id)
                        .transport(opts -> opts.port(port));

//        if (!isSeed()) {
//            config.membership(opts -> opts.seedMembers(getSeed()));
//        }


        Cluster cluster = null;
        if (!isSeed()) {
            cluster = new ClusterImpl(config)
                    .membership(opts -> opts.seedMembers(getSeed()))
                    .transportFactory(TcpTransportFactory::new)
                    .handler(handler -> new CounterClusterMessageHandler(counter))
                    .startAwait();

        } else {
            cluster = new ClusterImpl(config)
                    .transportFactory(TcpTransportFactory::new)
                    .handler(handler -> new CounterClusterMessageHandler(counter))
                    .startAwait();
        }
        logger.debug(sessionId  + address + "  started");

//        ScheduledExecutorService executor =
//                Executors.newSingleThreadScheduledExecutor();
//
//        executor.scheduleAtFixedRate(() -> sync(),
//                1_000, 500, TimeUnit.MILLISECONDS);

        return cluster;
    }



//
//
//
//    private void registry() throws IOException {
//        String tempDir = System.getProperty("java.io.tmpdir");
//
//        Path registry = Paths.get(tempDir, sessionId + ".dir");
//
//        if (!Files.exists(registry)) {
//            Files.createFile(registry);
//        }
//        Files.write(
//                registry,
//                (host+":"+port).getBytes(),
//                APPEND
//        );
//    }


    public long increment() {
        counter.increment(id);
        long value = counter.getValue();
        logger.info(id + "incremented to " + value);
        return value;
    }

    public long decrement() {
        counter.decrement(id);
        long value = counter.getValue();
        logger.info(id + "decremented to " + value);
        return value;
    }

    public long getValue() {
        long value = counter.getValue();
        logger.info(id + "current value is " + value);
        return value;
    }

    public void sync() {
        logger.debug(id + " sync in progress");
        cluster.spreadGossip(Message.fromData(counter))
                .doOnError(error -> logger.error(id + " " + error))
                .subscribe(null, Throwable::printStackTrace);
    }
}
