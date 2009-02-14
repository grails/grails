Implementation of Grails.org in Grails with example Wiki engine
----------------------------------------------------------------


Requirements
------------

This application was developed against Grails 1.1-SNAPSHOT uses the following plug-in:

- Searchable 0.4.2-SNAPSHOT
- JSecurity 0.3
- Mail 0.1-ALPHA
- Quartz 0.2
- Feeds 1.2
- Liquibase 1.0.9 (patched)

The Liquibase plugin has been updated to work with Grails 1.1-SNAPSHOT, and is temporarily installed within this application
in the plugins/liquibase directory.  There is no need to install this plugin at the moment.  Once the Liquibase team
has applied the patch to update it for Grails 1.1, we'll remove this internal plugin.

In addition, the application requires a persistent database (an in-memory HSQLDB won't do) at the moment its configured to
look for a MySQL db configured with:

url = "jdbc:mysql://localhost/grails"
username = "root"
password = ""

Getting Started
---------------

First you should run the application and configure an initial admin account:

grails -Dinitial.admin.password=changeit run-app

Once this is done CTRL-C or quit the application and import the data from Confluence using

grails import-confluence-xml ./data/confluence/entities.xml

If you want to translate the Plugin Wiki pages into the new Plugin model, run this script

grails transfer-content-to-plugins

With this done you can now run-app

grails run-app

And you have a successfully configured Grails wiki.  After one minute, a job will run that will attempt to update local
plugin data with master data located on the grails.org server.


Continuing after an update
--------------------------

If other developers have made database migrations and properly annotated them in a Liquibase changelog, you'll need to
run 'grails migrate' to update your local database.  This should be run whenever anything in the grails-app/migrations
folder has changed.

If you start seeing unexplained SQL exceptions, it is probably because you or someone else didn't keep the changelog
current.