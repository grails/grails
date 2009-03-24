The Definitive Guide To Grails - 2nd Edition
--------------------------------------------

This document describes how to get the sample code for "The Definitive Guide to Grails - 2nd Edition" (Apress). The sample application models an online music store called gTunes and is organized by chapter, with each directory related to a single chapter (eg. Chapter01). You will need to the following software installed before you can use the samples:

* Grails 1.1 - Available from http://grails.org/Download (installation instructions can be found the book)
* MySQL - Available from http://dev.mysql.com/

You will need to create databases (or catalogues in MySQL lingo) in MySQL for each chapter that follow the sequence gtunes_ch01, gtunes_ch02 etc. You also need to setup a user account that can access each database with the following details:

* username: gtunes
* password: password

With that done you can run each chapter using the following example steps from a terminal window:

$ cd Chapter01
$ grails upgrade
$ grails run-app

Importing Your iTunes Library
-----------------------------

In some of the chapters you can use your iTunes library as sample data for the application. To do so you can run the following command:

$ grails import-itunes-library ~/Music/iTunes/iTunes\ Music\ Library.xml

If you're on a Windows system the path will likely differ.

Importing Sample Data
---------------------

From Chapter 12 onwards there is also a Gant script that you can use to import some sample data:

$ grails import-library-from-xml ../gtunes-data.xml

Note: The paths to the physical files will probably not be correct for your machine, so playback of tracks that is implemented in Chapter 14 will not work unless you modify the paths. 


