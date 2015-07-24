# Directories/files in this repository
app: android application development dir (android studio)

Appweb: backed for android application residing at server

DesktopWeb: web interface to be run using web interface

screenshots: screenshots of the application

database.sql: mysql database exported to text file in which all data is stored

# Problem introduction
Almost every academic institution requires an attendance management system to keep track record of the student’s attendance. As of now(Apr 2015), teachers are supposed to mark the attendance of the pupils in an attendance register by calling out roll numbers sequentially and marking the presence/absence of student in register by hand. This process wastes the precious time of both: students and teachers alike. Typically, the task of taking attendance in a class with approximate 70 students requires 5-6 minutes. Furthermore, at the end of year/semester the task of calculating the attendance percentage becomes increasingly tedious and time consuming. Also students are not sure in which subject they have short attendance because of which they can even get detained and not allowed to sit in end semester exams. There is also good chance that a teacher might mark attendace of one student as of other student.

# Project description
•	DTU attendance management system provides a solution to minimize the effort and time required in marking attendance as well as attendance management.

•	As DTU campus enjoys full Wifi coverage, the project presents a web interface that is supported by all the web browsers, and an Android mobile application.

Each faculty member is provided with a username-password pair as credentials to login into the attendance management system. The teacher while taking attendance has to select the class, subject, and no. of classes for which he/she wants to take attendance. He then just calls out the roll number of each student one by one and swipes to mark absent and taps on screen to mark present. The Android application replaces the task of writing in attendance register by simple intuitive gestures. The Android mobile application provides a better experience, and makes the task extremely effortless.
The project also provides a web interface to perform the same task. It which might be very useful in case the teacher does not have/forgot/lost their mobile phone

# Authentication and access control
The access to attendance data is write protected for everyone except the database administrator. The students can only view days they were present using the web interface, while the faculty members are restricted to only upload the attendance on the day the class is scheduled. Once uploaded to the database, the teachers cannot make any changes to the data. The login requires the faculty to input the username and password, which is provided to them explicitly and confidentially. If it is required by the teacher to make any changes to the data uploaded, the faculty can contact the Head of Department who further instructs the database administrator to allow the update.

# Views and reports
The system allows to print the student’s attendance report in pdf format. It lists the dates on which the student attended the class followed by the current attendance percentage. The report also includes the timestamp at which the report is generated.
The students and faculty alike are allowed to view the marked attendance at any time using the web interface. Viewing the report does not require any login or authentication.

# Technologies used
•	Android Studio

•	HTML

•	CSS

•	PHP

•	SQL (MySQL)