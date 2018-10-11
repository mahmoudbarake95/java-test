Java Refactoring Test Project
=============================

**Please, before starting the test read these instructions carefully.**

Introduction
------------

This is a refactoring testing project used by SAP CX hiring process.

The idea of this exercise is to evaluate your ability to identify poor coding practices and improve the code through the use of best practices.

The main project is a very basic user management application. We are not looking to add any supplementary features, instead we are verifying the following:

* Your knowledge of REST.
* Your knowledge of Maven.
* Your knowledge of Spring.
* Your ability to identify and refactor poor Java code.
* Your ability to identify and fix bugs.
* Your ability to apply proven design principles.
* Your ability to write useful and effective tests.

Feel free to modify whatever you want! :)

Pre-reqs
--------

* The first thing you need is to have a github  account. If you don't have it yet, you can create it on [github website][2].

* This repo uses GIT as SCM. If you don't have it you need to install it on your machine. You can choose between installing a [command line version][4] or a  [graphical tool][5] to manage it.

* To build this project you must install Maven. If you do not have it installed, please refer to the [maven website][1] for assistance.

* For deployment you can use any web container/application server you want. We used tomcat version 8.5.x to validate if the application was starting up correctly.

Instructions
------------

1. Fork this repo using the github website.
1. Clone your new forked repo from github/gitlab .
1. At the project root directory, run:
    `$ mvn package`
1. At this point the maven build should run successfully and every test should be green.
1. Perform the refactoring you deem necessary, following what you know to be the best practices. (feel free to innovate!).
1. Please make sure your code compiles and that all tests are green when you are done.
1. At the end of your work you should push the code to your forked repo.**DO NOT** do a pull request into the original repository.
1. The final step is to send an email to your contact at the company informing him/her that you finished the test including the **public** Git Repo URL where the code changes can be found.

Business Requirements
---------------------

* The user's email is a unique identifier and should be handled accordingly.
* A user should have at least 1 role.

Tips
----

* Unit tests != integration tests.
* Spring dependency is provided, feel free to use it.
* Don't be afraid to import additional dependencies if you think you need them.
* Remember that you will have to handle concurrent requests.
* Your final architecture should be portable, extensible and easily maintainable.

Good luck!

[1]: http://maven.apache.org/
[2]: https://github.com
[3]: https://bitbucket.org/
[4]: https://git-scm.com/book/en/v2/Getting-Started-Installing-Git
[5]: https://git-scm.com/downloads/guis
