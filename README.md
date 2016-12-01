Brady Shutt's project for CPE-305


[coplan.bshutt.com](http://coplan.bshutt.com/)

[![Build Status](https://travis-ci.org/cpe305/fall2016-project-bradyshutt.svg?branch=master)](https://travis-ci.org/cpe305/fall2016-project-bradyshutt)


# Coplan

_The College Planner_

> Manage important due dates, deadlines, and test days with Coplan!

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

# Commands

The Java program will handle all business logic associated with CoPlan.

This will be accomplished by running a Node.js webserver that 
spawns the Java program as a child process. The two programs 
will communicate with each other through STDIO. Whenever Node
needs business logic accomplished, it will create a "request"
object as a JSON-formatted string with the details needed for
the request to be fulfilled, and then send this request string 
to the Java child process through its STDIN.  

The Java program will be continuously reading its STDIN in a 
blocking fashion until it reads a full JSON string. At that 
point, the Java program will fulfill the request, and finally
respond to Node by simply printing out a response as a 
JSON-formatted string.

Node will be listening for that, and finally response to the 
original HTTP request. 

-------------------------------------------------------------

Example format of requests:
{ 
   subsystem: "users", 
   action: "create", 
   data: {
      username: "bshutt",
      firstName: "Brady",
      lastName: "Shutt",
      major: "Computer Science"
   }
}


The Java process should be able to handle the 
following JSON formatted requests appropriately: 

      
-------------------------------------------------------------

Users.readUsers <query, count>

Users.createUser <user-info>
Users.readUser <filter>
Users.updateUser <filter, info>
Users.deleteUser <filter>
Users.isUsernameAvailable <username>

Courses.readCourses <query, count>

Courses.createCourse <course-info>
Courses.readCourse <course-info>
Courses.updateCourse <filter, info>
Courses.deleteCourse <filter>


example: createUser
{ 
   route: 'Users.createUser',
   data: {
      username: 'bshutt',
      firstName: 'Brady',
      lastName: 'Shutt',
      age: 21
   }
}


example: readUser
{ 
   route: 'Users.readUser',
   data: {
      username: 'bshutt'
   }
}


