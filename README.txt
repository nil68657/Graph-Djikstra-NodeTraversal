Breakdown of Files: The program consists of four files:
1. Graph.java
2. Edge.java
3. Heap.java
4. Vertex.java

Programming Language Used: Java

Data Structures used: Binary heap and Priority queue were used to store the data in the table.

Istructions to run the program:
>>javac Graph.java
>>java Graph network.txt<queries.txt >output.txt

Program Design:

1.The input text file is read and two directed edges are created for every two vertices listed in a line in the text file.
2.All the vertices are stored in vertex class and all the edges are stored in the edge class with the edge name as source_destination.
3.The shortest path between two vertices has been calculated using dijkstra's algorithm and we have used Binary Heap.
4.A min priority queue hase been implemented and used for the dijkstra's algorithm.
5.All the float values of transmission time have been reduced to two decimal places.
6.The state of the vertices and edges are being tracked using state variable in the respective classes.
7.Based on the queries from queries.txt , the output of the command is written into output.txt.

summary:All the queries executed properly with the code.