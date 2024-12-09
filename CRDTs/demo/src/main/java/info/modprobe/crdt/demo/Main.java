package info.modprobe.crdt.demo;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CounterNode seed = new CounterNode();

        CounterNode node = new CounterNode();

        CounterNode otherNode = new CounterNode();

        seed.increment();
        node.increment();
        node.increment();
        otherNode.increment();
        otherNode.increment();
        otherNode.increment();

        node.decrement();

        long seedValue = seed.getValue();
        long nodeValue = node.getValue();
        long otherNodeValue = otherNode.getValue();

        final int retries = 10;
        for (int attempt = 1; attempt < retries; attempt++) {
            seedValue = seed.getValue();
            nodeValue = node.getValue();
            otherNodeValue = otherNode.getValue();
            System.out.println("attempt " + attempt);
            System.out.println(" seed counter= " + seed.getValue());
            System.out.println(" node counter= " + node.getValue());
            System.out.println(" other counter= " + otherNode.getValue());

            if (seedValue == nodeValue && seedValue== otherNodeValue) {
                System.out.println("values have converged in attempt " + attempt);
                break;
            }

            seed.sync();
            node.sync();
            otherNode.sync();

            SECONDS.sleep(1);
        }
        
    }
}
