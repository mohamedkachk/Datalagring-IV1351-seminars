# A JDBC application with an appropriately layered architecture

This is an example of how an integration layer can be used to organize an application containing database calls.

## How to execute

1. Clone this git repository
1. Change to the newly created directory `cd jdbc-Soundgood`
1. Make sure there is a database which can be reached with the url on line 276-277 in `Soundgood.java`.
   1. Create a database that can be reached with one of the existing urls. If
      postgres is used, that is a database called Soundgood, wich can be
      reached on port 5432 at localhost, by the user 'postgres' with the
      password 'postgres'.
   1. Change the url to match your database.
1. Create the tables described by  `src/main/resources/postgres-db.sql` (if you use postgres).
1. Build the project with the command `mvn install`
1. Run the program with the command `mvn exec:java`

## Commands for the Soundgood school program

* `help` displays all commands.
* `list` lists all available instruments for rental.
* `list <instrument name>` list all instruments of a certain kind (guitar, saxophone, etc) that are available to rent.
* `rent <instrument's id> <student's id> <number of month>` rent an instrument to a specific student.
* `Terminate <instrument's id>` terminate an ongoing rental.
* `quit` quits the application.
