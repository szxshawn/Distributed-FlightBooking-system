## flight Information System for DS6103

Author(in alphabetical order): LIU Yulin, SHAO Zixuan, WANG Junzuo, WU Qile

## Running requirements

1. Please install Java and Java Development Kit according to the official website.
   https://www.oracle.com/sg/java/technologies/downloads/

2. Please install Node.js according to the official websites.
   https://docs.npmjs.com/downloading-and-installing-node-js-and-npm
   https://nodejs.org/en/download/package-manager


## To run the program


1. For Server:

   1.1 run "ipconfig" in terminal to check the device's ip address
   1.2 run "javac Main.java" in path ".../DS6103/ds_backend/src" to compile the file
   1.3 run "java Main" in the same path to start the server
   1.4 use Ctrl+C in terminal to stop the server

2. For Client:

   1.1 copy the server's ip adress into "...DS6103/ds_frontend/client.js" line 7
   1.2 run "node client.js" in path ".../DS6103/ds_frontend"
   1.3 select from:

       Query flights by source and destination
       Query flight by ID
       Reserve seats by flight ID
       Subscribe to flight updates
       Randomly choose seats
       Get booking information

       by typing the seriel number into terminal
   1.4 choose Exit or use Ctrl+C to exit the program

