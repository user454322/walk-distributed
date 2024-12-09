# Replication

In general terms, there are two main approaches to replication
1. Centralized. Usually involves a master-slave architecture.
2. Decentralized. Peer-to-peer where the peers do not depend on a controller like a master.


## 1. Centralized
```
                 ┌──────────┐
                 │  Client  │
                 └────┬─────┘
                      │
                      │ 1. Write
                      │ Request always go to the master
                      ▼
              ┌───────────────┐
              │    Master     │
              └───────┬───────┘
                      │
                      │ 2. Replicate
           ┌──────────┼──────────┐
           │          │          │
           ▼          ▼          ▼
┌───────────┐  ┌────────────┐  ┌────────────┐
│  Slave 1  │  │  Slave 2   │  │  Slave 3   │
└───────────┘  └────────────┘  └────────────┘

3. Usually the reponse comes after the write has been replicated to the majority of the nodes in the cluster
```
### Characteristics
* ⬆️ Immediate consistency,  
at the cost of  
 ⬇️  delaying the response until the write has been replicated to the majority of the nodes in the cluster.

* Difficult to scale

### Associated with
Relational databases, quorum, leader election, Raft, consensus


## 2. Descentarlized 
```
                  ┌──────────┐
                  │  Client  │
                  └────┬─────┘
                       │
                       │ 1. Write
                       │ Request happens to be to node B, but it can be to any node.
                       │
                       ▼
┌────────┐   sync   ┌────────┐   sync   ┌────────┐
│ Node A │◄────────►│ Node B │◄────────►│ Node C │
└────────┘          └────────┘          └────────┘
    

1. Any node can accept the write request
2. Node coordinates with others using asynchronous replication
3. All nodes eventually become consistent
```

### Characteristics
* ⬇️ Eventual consistency,  
but
   ⬆️ the response can be returned immediatly to the client  
⬇️ at the risk of losing the update if the node goes down before it has been sufficiently replicated.

* Easier to scale

### Associated with
Distributed databases like Cassandra, P2P, asynchronous replication
