# Conflict-free Replicated Data Types

CRDTs are data structures designed for distributed systems that guarantee eventual consistency without central coordination. 

The operations on these data structures have the following properties:
1. Commutative: Order of updates doesn't matter.
2. Monotonic: Changes only move in one direction.
3. Convergent: All replicas eventually reach the same state. Usually through the `merge` operation.

So distributed variables can safely update without locking or coordination.

## Simple project in Java showing the basics of CRDTs
* [Library with basic counters](https://github.com/user454322/walk-distributed/tree/main/CRDTs/crdts)
* [A couple of demos using the libraries](https://github.com/user454322/walk-distributed/tree/main/CRDTs/demo)

## Resources
[Definitions and links to other resources](https://crdt.tech/glossary)

## Web Interactive Animantion
[An Interactive Intro to CRDTs](https://jakelazaroff.com/words/an-interactive-intro-to-crdts)

### Videos
[Akka Distributed Data Deep Dive](https://www.youtube.com/watch?v=yfK2IVA545k&ab_channel=Jfokus)
[Strong Eventual Consistency and Conflict-free Replicated Data Types](https://www.youtube.com/watch?v=oyUHd894w18&ab_channel=MicrosoftResearch)
