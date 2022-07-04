This personal project for the student George Duffield, studentID: 100934774

The aim of this project is to create an extendable clustering application with the aim of it being released to the greater scientific community upon its completion.

The main bulk of the application is stored within the clustering application folder. This folder contains the code of the application as well as some of the databases used during testing which can be found in the src/resources folder.

Included in this submission is also a collection of unit tests which can be found under the src/test folder.

# source file structure
* com/project/clusteringapllicaiton - front end javaFX files
* kernel - core clustering/ plugin loading files
* plugins - default plugins provided with the application (clustering algorithms, distance algorithms ect)
* spi - the interfaces which all plugins must inherit from


At the top level of this directory is a folder called "example plugins" which contains an example of each of the four types. These were used to test the plugin functionality of the application. Note that these file may not actually work and instead are included for testing whether the application would detect such plugins. 

This submission also contains an executable jar of the application name "ZHAC081-clusteringApplication-1.0". This can be run from the command line using the command "java -jar ZHAC081-clusteringApplication-1.0" .

Video demonstration of the application can be found at https://youtu.be/VpXzF32sLYY

The following components have been used with the application and are covered under the Apache 2.0 License:
Google AutoService - used as part of loading the plugin jars to the class path
Apache commons LevenshteinDistance - used to create a numerical distance value between two strings 