# file-reader
This is a very small akka application to demonstrate how to create an akka system, actors, cross actor communication
and message processing. It also includes one synchronous call (ask) and a few asynchronous calls (tell).

The objective of the application is to analyse a log file from a webserver and extract how many times an IP address
has been logged. Everything is done through concurrent actors.

