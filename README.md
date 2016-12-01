Brady Shutt's project for CPE-305


# Coplan

[coplan.bshutt.com](http://coplan.bshutt.com/)

[![Build Status](https://travis-ci.org/cpe305/fall2016-project-bradyshutt.svg?branch=master)](https://travis-ci.org/cpe305/fall2016-project-bradyshutt)

_Coplan: The College Planner_

Manage important due dates, deadlines, and test days with Coplan!

## Project Structure
The way in which this project behaves is a little strange, 
so some explanation is necesarry.

The core business logic of this project is written in Java. 
The application works by letting the JVM endlessly run the 
exported .jar file. The .jar then, indefinitely listens to 
it's STDIN in a blocking fashion. Upon receiving input, which
must be preformmated JSON, the running Java application will 
handle the 'request', and eventually return a response by 
writing a JSON message to STDOUT.

A Node.js application is also running. Node.js acts as the
HTTP server that clients interract with. Upon receiving a 
HTTP request, node will, if necesarry, talk to the running 
JVM via STDIN/STDOUT to fulfill requests.

The Node.js process is essentially acting as the bridge
between the client and the business logic.

## Commands

The Java program handles all the business logic associated with
Coplan, by communicating with a Node.js process via JSON-formatted
commands sent through STDIN/STDOUT.

Commands are to be formatted in the following way:
   - There are two essentail components: 'route', and 'data'
   - 'route' specifies the endpoint you want to talk you
   - 'data' specifies an object with keys and values pertinant to the endpoint
   - If you attempt to access an endpoint without the needed requirements, an error message will be returned
 
## Request Example 
{ 
   route: "createUser", 
   data: {
      username: "bshutt",
      firstName: "Brady",
      lastName: "Shutt",
      major: "Computer Science"
   }
}


## Class Diagram
![Class Diagram](/coplanDiagram.png)

## Picture of Application 
![Application Picture](/currentState.png)

