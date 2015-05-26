#RaftGame

A simple game demonstrating our implementation of the raft consensus protocol

### Description

Player is randomly spawned on the map, coins are also progressively spawned at intervals around the map. Picking up a coin results in gaining points. When one player reaches a limited amount of points, they win the game.

### Compilation instructions

Required libraries on classpath:

JGroups 3.6.3Final

raft-da

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