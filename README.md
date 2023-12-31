# LAB13-QIFAN-GROUP2-A2
## Description
This program is Assignment 2 for SOFT2412 by GROUP 2.

The program is a Virtual Scroll Access System (VSAS), which allows the user to access a database of scrolls, and are able to read scrolls, upload scrolls, edit their own scrolls, and delete their scrolls. The user is also able to edit their own details, and register an account if needed.

The user also has the ability to log into the admin interface as an admin, with the ability to delete users, create users, delete scrolls and view scroll statistics.

## Installation Guide
### Installing Gradle and Java

To run the program its important that you have at least Gradle 7.4 and Java 17 installed.

To install gradle, follow the instructions on the official gradle page : https://gradle.org/install/

To check your gradle version, go into your command line and type in "gradle - version" ("gradlew - version for Apple users)

To install Java, follow the instructions on the official java page : https://www.java.com/en/download/help/download_options.html

To check your java version, go into your command line and type in "java -version"

### Downloading the repository

To download the program and the repository, do the following:
- go to the repository (should be on this page of youre on github, if not, should be the following link : "https://github.sydney.edu.au/SOFT2412-COMP9412-2023S2/LAB13-QIFAN-GROUP2-A2.git"
- click on code (green button)
- click on download as zip
- unzip the repository

Then simply open the folder in your IDE (IntelliJ recommended).
## Using the Menu Manager
### Running the Program
Use the terminal input in the IDE, or simply open the folder in the terminal, then run the following command :

"gradle run"

This command will build and run the project with all required dependencies.

## Using VSAS
### User Login
Upon opening the program you are asked whether you want to login as a Guest. 

Declining this will allow you to login with your details (USERNAME AND PASSWORD).

Failing a login will prompt a user registration.

#### User Dashboard
The user can access 4 things in the dashboard:

#### Manage Profile
In this menu you can edt your username, password, email and phone number.

#### Edit Scroll
In this menu you can edit all scrolls you own.

#### Remove Scrolls
In this menu you can remove all scrolls you own (If you are the admin, you can remove all scrolls).

#### Seek Scrolls
In this menu, you can access the ScrollUI where you can do the following:
* View Scroll Details
* Add a new Scroll
* Download a scroll
* Search for scrolls based on user ID, scroll ID, scroll Name or Upload Date
* Preview and see the contents of a scroll
* View the next or previous page

### Admin Panel
As an admin, you can view the admin pane UI, which allows you:
* Do all previous functionality
* Delete Users
* View Statistics
* Make new Users
* Print all Users

## Running Tests
Tests will be run using gradle.

In the same terminal, type the following :

"gradle test"

This will run all tests for the program, then a JacocoTestReport will be automatically generated by gradle.

##

## Finding Test Coverage

To find test coverage, go to the following directory in the repository:

"../LAB13-QIFAN-GROUP2-A2/app/build/reports/jacoco/test/html/index.html"

And open the file in your browser. This will show you the test coverage of the project.

## DEFAULT LOGINS: 
- Admin: Username - admin, Password - admin

## Collaboration
This project is open source. If you would like to contribute, then please do the following:
- create a new branch
- add your own code
- commit and push to your branch
- request to pull your branch if you desire to do so

Admins of the repository will check through pull requests.
