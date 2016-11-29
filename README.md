Brady Shutt's project for CPE-305


[coplan.bshutt.com](http://coplan.bshutt.com/)

[![Build Status](https://travis-ci.org/cpe305/fall2016-project-bradyshutt.svg?branch=master)](https://travis-ci.org/cpe305/fall2016-project-bradyshutt)




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


|--------+-----------------+----------------------------+------------------------|
| Subsys | Action          | Data                       | Returns                |
|--------+-----------------+----------------------------+------------------------|
| Users  | get             | how many & offset          | flat list of usernames |
| Users  | isNameAvailable | username to check          |                        |
| User   | create          | new users details          |                        |
| User   | delete          | username of user to delete |                        |
| User   | get             | username, attributes       | user details           |
|--------+-----------------+----------------------------+------------------------|

      
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


