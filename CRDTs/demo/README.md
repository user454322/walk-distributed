# Demo using CRDT counters

There are 2 demos, both demos use the `scalecube-cluster` library for P2P gossiping communication, so the different nodes communicate over the network using the Netty transport to synchronize, merging theier values and reaching eventual consistency. 

## Multiple nodes using the counters  
See [ `Main.java` ](https://github.com/user454322/walk-distributed/blob/main/CRDTs/demo/src/main/java/info/modprobe/crdt/demo/Main.java), where 3 different nodes, i.e., `seed`, `node`, and `otherNode` each of them have a copy of the same counter and recieve different updates, i.e., increments and decrements. They later synchornize and converge to the same value.

Excertp of the output 
```
2024-12-08 15:37:02:034 [sc-cluster-24464-3] DEBUG FailureDetector - [default:session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:24464:2bbe0eacb261418d@127.0.0.1:24464][0] Member default:session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:17672:59c9218c8e174684@127.0.0.1:17672 detected as ALIVE
2024-12-08 15:37:02:051 [main] INFO CounterNode - session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:14775current value is 5
2024-12-08 15:37:02:051 [main] INFO CounterNode - session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:17672current value is 5
2024-12-08 15:37:02:051 [main] INFO CounterNode - session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:24464current value is 5
attempt 2
2024-12-08 15:37:02:051 [main] INFO CounterNode - session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:14775current value is 5
 seed counter= 5
2024-12-08 15:37:02:051 [main] INFO CounterNode - session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:17672current value is 5
 node counter= 5
2024-12-08 15:37:02:051 [main] INFO CounterNode - session-e42c7230-aba1-42a8-a143-1c62a1361477-127.0.0.1:24464current value is 5
 other counter= 5
```


## USA 2024 election
Each node counts regional votes and syncrhonizes with its peers, eventually all the nodes have the same results.
See [RunElection](https://github.com/user454322/walk-distributed/blob/main/CRDTs/demo/src/main/java/info/modprobe/crdt/demo/us2024elections/RunElection.java).
