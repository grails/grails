Implementation of Grails.org in Grails with example Wiki engine
----------------------------------------------------------------


Requirements
------------

This application was developed against Grails 1.0.1 and requires the following plug-ins before running:

- Searchable 0.4-SNAPSHOT
- JSecurity 0.1.1

These can be installed with:

grails install-plugin searchable 0.4-SNAPSHOT
grails install-plugin jsecurity 0.1.1

In addition, the application requires a persistent database (an in-memory HSQLDB won't do) at the moment its configured to
look for a MySQL db configured with:

url = "jdbc:mysql://localhost/grails"
username = "root"
password = ""

Getting Started
---------------

First you need to run the application and configure an initial admin account:

grails -Dinitial.admin.password=changeit run-app

Once this is done CTRL-C or quit the application and import the data from Confluence using

grails import-confluence-xml ./data/confluence/entities.xml

With this done you can now run-app

grails run-app

And you have a successfully configured Grails wiki



