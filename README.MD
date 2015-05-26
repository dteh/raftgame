#RaftGame

A simple game demonstrating our implementation of the raft consensus protocol

### Description

Player is randomly spawned on the map, coins are also progressively spawned at intervals around the map. Picking up a coin results in gaining points. When one player reaches a limited amount of points, they win the game.

### Compilation instructions

Required libraries on classpath:

JGroups 3.6.3Final

raft-da

### Running RaftGame:

Make sure the compiled JAR is in the same folder as the resources bg.png, player.png, coin.png
java -jar raftgame.jar

### Potential Issues
JGroups cannot connect to a cluster: Java attempts to use an IPv6 interface so IPv4 must be forced.

java -Djava.net.preferIPv4Stack=true -jar raftgame.jar

JGroups does not correctly bind to the interface: Force an address to bind to

java -Djgroups.bind_addr=127.0.0.1 -jar raftgame.jar (or replace the IP with local IP)


-----
Code Structure

**Package: raftgame**

Game - Game logic

GameBoard - Global game state

InstApplier - Implementation of the InstructionApplier class in the raft protocol to process Instructions

Instruction - Information used when an action is made

Menu - Main entry point for game, sets game parameters and allows game creation/joining

RaftGame for Distributed Algorithms COMP90020

Daniel Teh, Felipe Costa, Sergio Freschi