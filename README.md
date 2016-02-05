Comparegrids
============

Sample application for comparing various JavaScript grid solutions.

Requirements (see details below) are defined to demonstrate several aspects of the grid:
- pagination
- CRUD functionality
- real-time (dynamic) grid updates
- visual row identification based on row parameters

Server side technology used
===========================
- Java with Spring Framework (plan to implement PHP version)
- MySql DB

Import project into eclipse:
============================
- run: mvn eclipse:eclipse -Dwtpversion=2.0

Running the application
=======================
- ensure MySql is installed and up and running with schema 'comparegrids' created
- run: mvn tomcat:run
- applicaton URL: http://localhost:8080/comparegrids/ 

