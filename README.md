# Client Array Search and Sort 

This command line interface program was created to fulfill lab 4 of Oakton's CSC 241 Java Data Structures, a class I took in the spring of 2021 while working towards a [General Programmer Certificate](https://catalog.oakton.edu/career-programs-pathways/computer-information-systems/general-programmer-certificate/). A description of the course can be found in [the Computer Information Systems course descriptions list](https://catalog.oakton.edu/course-descriptions/course-descriptions-discipline/cis/). In addition, the course syllabus and lab 4's pdf for CSC 241 can be found in the course_info folder of the repository. The starter code for this assignment can be found in lab 4's pdf. 

The command line program can be run at [this Replit link](https://replit.com/@john-albright/client-array-search-and-sort-console).

The project is linked to an SQLite database using the [sqlite-jdbc driver](https://github.com/xerial/sqlite-jdbc). Therefore, to successfully run the program, one may have to use the command

```
java -classpath ".:sqlite-jdbc-3.36.0.3.jar" ArraySearchAndSort   # in Mac or Linux
```
or
```
java -classpath ".;sqlite-jdbc-3.36.0.3.jar" ArraySearchAndSort   # in Windows
```

after compiling the java program.

The program allows the user to work with arrays populated with client names and numbers. There are three pathways: 
- add (A): user can add a client to the database
- binary search (B): user can perform a binary search using the roster
- modify (M): user can modify a client name or number
- print (P): user can print out the entire contents of the clients table in the clients.db
- remove (R): user can remove a client name or number
- search (S): user can search for a client name or number
- exit (X): user can exit the program

The binary search pathway requires that the array be sorted. The user can select from three sort methods: 
- A (java's built-in array sort)
- B (bubble sort)
- I (insertion sort)

For modify, remove, and search pathways, the user must select the client using the client's name or number. The position on the roster and the client's name and number are returned to the console as well as the full roster (potentially sorted). 

The clients.sql file contains the statements used to create the original table clients in the clients.db file. The table contains two field client_number and client_name where the number has to be unique. 