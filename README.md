Comparegrids
============

Sample order management application for comparing various JavaScript grid solutions.

Requirements (see details below) are defined to demonstrate several aspects of the grid:
- pagination
- CRUD functionality
- real-time (dynamic) grid updates
- visual row identification based on row parameters

JavaScript Grid compared
========================
- JQGrid (status: implemented)
- Backgrid.js (status: in progress)
- Grid based on Angular.js status: not started)
- Flexigrid (status: not started)
- SlickGrid (status: not started)
- ...

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

Requirements
============
- Order Management System (in further text 'System') is used to process orders for products
- System supports Product maintenance (create, update, delete)
- System will support auto-generation of the random orders
- System will support manual creation of the orders
- Upon receival, order goes to status 'New'
- System will process order 10 minutes after receiving it
- System will move order to status 'Processed' if product requested is available and quantity requested is available
- System will move order to status 'Errored' if product requested is not available or quantity requested is not available
- System will support showing orders in real-time blotter (Content of the blotter will be refreshed periodically)
- Line in blotter will represent one order
- User can change status of the order
- Order with different status will have different background color ('New' - Yellow; 'Processed' - Green; 'Errored' and 'Cancelled' - Red