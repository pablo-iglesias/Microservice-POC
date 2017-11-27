# Microservice POC

## Introduction

*   The application is based in the specifications found in the file [requirements.txt](requirements.txt)
*   The application features are summarized in the file [features.md](features.md)

## Build

1.  This project has been build on top of a **gradle wrapper**
2.  Open a console, go to the root folder of the project
3.  Run the build command
	*   Linux run "./gradlew build"
	*   Windows run "gradlew build"
4.  The Jar file "Microservice.jar" has been deployed along with dependencies under build/jar

## Run

1.  Open a console, go to the folder of your JAR file
2.  Use "java -jar Microservice.jar"
3.  Go to http://localhost:8000 to see login page
4.  REST API is published under http://localhost:8000/api/users with Basic Authentication (use Postman)

*   By default SQLite works in-memory, each time that the JAR is run, the database is restored to defaults
*   Default users are as follows
	*   admin admin
	*   user1 pass1
	*   user2 pass2
	*   user3 pass3

## Run Tests

1.  Open a console, go to the root folder of the project
2.  Run the test command
	*   Linux run "./gradlew test"
	*   Windows run "gradlew test"
4.  Find tests report in build/reports/tests/test/index.html

## TODO list

Development never ends

1.  Extend unit tests to cover Domain Factories
2.  Extend unit tests to cover Controllers and Models
3.  Implement reactive programming where possible
4.  Convert Page to an entity, promote relation with Role from 1:1 to 1:n
5.  Have the REST API to honor JSON API specification ([jsonapi.org](http://jsonapi.org/))
6.  Add localization support for application templates and REST API error messages
7.  Add by language content negotiation
