# Task 2.2 Spring Boot JDBC Api

**Assignment:**
1. Based on previously created console application sql-jdbc-school, create spring boot application utilizing spring JDBC API.
2. Use already existing tests with spring test tools like @Sql.
3. No need for db drop functionality.

**Project bootstrap:** <br>

Use [Initializer](https://start.spring.io/) to bootstrap project with following dependencies:

- **JDBC API** (Database Connectivity API that defines how a client may connect and query a database.)
  - https://www.baeldung.com/spring-jdbc-jdbctemplate
  - https://www.concretepage.com/spring-5/sql-example-spring-test
- **Flyway Migration** (Version control for your database so you can migrate from any version (incl. an empty database) to the latest version of the schema.)
- **PostgreSQL** Database (The use of docker is recommended)
- Use **Testcontainers** for JdbcTests to instantiate a test database

**Database structure:**

Tables (the given types are Java types, use SQL analogs that fit the most:

```
groups(
	group_id int,
	group_name string
)
students(
	student_id int,
	group_id int,
	first_name string,
	last_name string
)
courses(
	course_id int,
	course_name string,
	course_description string
) 
```

Create a [DAO layer](https://www.baeldung.com/java-dao-pattern) using [JdbcTemplate](https://www.baeldung.com/spring-jdbc-jdbctemplate) and implement the basic CRUD functionality

```
package com.foxminded.spring.console.dao;

@Repository
public class JdbcCourseDao implements CourseDao {

    public static final String FIND_BY_NAME = "select * from courses where course_name = ?";
    
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Course> courseRowMapper = new CourseRowMapper();

    public JdbcCourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    Optional<Course> findByName(String name) {
        try {
            return Optional.of(jdbcTemplate
                    .queryForObject(FIND_BY_NAME, courseRowMapper, name))
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    // rest of code
}
```

### Testing
Use test containers to run your tests <br>
/src/test/resources/application.yml

```
spring:
  datasource:
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
    url: jdbc:tc:postgresql:14.6:///test?currentSchema=student_db&TC_REUSABLE=true
    username: root
    password: test
    hikari:
      minimum-idle: 1
      maximum-pool-size: 5
```

Then run your tests like this:

```
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class JDBCStudentDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StudentDao dao;

    @BeforeEach
    void setUp() {
        dao = new JDBCStudentDao(jdbcTemplate);
    }

    // tests here
}
```

# Task 2.1 Plain JDBC Console app

Create a sql-jdbc-school application  that inserts/updates/deletes data in the database using JDBC.

Use PostgreSQL DB.

Important: In the next series of tasks you're going to develop a School console application. Make sure to give a repo a meaningful name (ex.: school-console-app)

Tables (the given types are Java types, use SQL analogs that fit best:

```
groups(
	group_id int,
	group_name string
)
students(
	student_id int,
	group_id int,
	first_name string,
	last_name string
)
courses(
	course_id int,
	course_name string,
	course_description string
) 
```
1. Create SQL files with data:

    a. create a user and database. Assign all privileges on the database to the user. (DB and the user should be created before application runs)

    b. create a file with tables creation

2. Create a java application

    a. On startup, it should run SQL script with table creation from previously created files. If tables already exist - drop them.

    b. Generate the test data:
    * 10 groups with randomly generated names. The name should contain 2 characters, hyphen, 2 numbers

    * Create 10 courses (math, biology, etc)

    * 200 students. Take 20 first names and 20 last names and randomly combine them to generate students.

    * Randomly assign students to groups. Each group could contain from 10 to 30 students. It is possible that some groups will be without students or students without groups

    * Create the MANY-TO-MANY relation  between STUDENTS and COURSES tables. Randomly assign from 1 to 3 courses for each student

3. Write SQL Queries, it should be available from the console menu:
    
    a. Find all groups with less or equal students’ number

    b. Find all students related to the course with the given name

    c. Add a new student

    d. Delete a student by the STUDENT_ID

    e. Add a student to the course (from a list)

    f. Remove the student from one of their courses.

<br>

# School App

## Motivation
From the very beginning, the main motivation for writing this project was:
1. Desire to acquire new knowledge in the field of such technologies: `Spring Boot`, `SQL`, `PostgreSQL`, `JDBC`, `Spring JDBC API`, `Docker Compose`, `Testcontainers`.
2. I also wanted to create something useful that could automate some process and make life easier for other people.
3. On the other hand, the task was to complete a task that belongs to the Java Spring Development course.

If you are reading this file, it means that I managed to write a program that works and meets the basic requirements :). But I see some shortcomings that I plan to correct in the future:
- make changes to the program, add new functionality; 
- finally, I will complete the following tasks from the previously mentioned course, which means that such technologies will gradually be added to project: `Hibernate`, `Spring Data JPA`.

## Description
As mentioned earlier, one of the goals was the desire to create something useful. Therefore, the application itself can be used for the management of students, groups and courses in educational institutions. The main menu includes the following options for interacting with the application and managing students, groups, courses:

1. Find all groups with less or equal students’ number.
2. Find all students related to the course with the given name.
3. Add a new student.
4. Delete a student.
5. Add a student to the course.
6. Remove the student from one of their courses.

**What happens at startup?** At the beginning of the application launch, the database is filled with randomly generated groups of students and courses. This is done in order to facilitate the development of the program and its testing. So don't be surprised if you start the app and see some students, groups, and courses are *test data*. After successful initialization, you will see a menu in the console. Next, you can choose one of the options and execute it by entering the number of the option and pressing Enter.

**Technologies used:**
- *`Java 17`*;
- *`Spring (Boot, JDBC API)`*;
- *`PostgreSQL`*, *`Flyway`*;
- *`JUnit 5`*, *`Mockito`*, *`Testcontainers`*;
- *`Maven`*, *`Git`*;
- *`Docker`*, *`Docker Compose`*, *`GitLab CI`*.

## Install & Run
To **install** this project, you must have Git version control installed on your device. It would also be nice to have a basic knowledge of using Git. You can download and learn how to use the version control system [here](https://git-scm.com/book/en/v2). Go to the folder where you want to install the project. Open Git Bash in it and enter the command:

```
$git clone https://gitlab.com/SerhiiBohdan/school-app.git
```

This way you will have the app installed.

There are two ways to **run** the application. Let's consider both.
1) Docker Container<br>
   To use this method you must have [`Docker`](https://www.docker.com/products/docker-desktop/) installed on your machine ([more information](https://docs.docker.com/get-started/overview/#docker-objects)). Run it and make sure the docker daemon is running. Next, you should go to the root of the project you just downloaded. To run an application in a docker container, you should run the following command: 

   ```
   >docker compose run -it app
   ```
   >**Note:** Do not try to start the container with the application with the command: 
   ```
   >docker compose up
   ```
   This will lead to the error: 
   ```
   Exception in thread "main" ua.foxminded.schoolapp. exception.DaoException: An error occurred during the execution of the transferred SQL script.Connection to postgresqldb:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
   ```
   >**Note:** When you're done with the application in the container, don't forget to stop the database container (the application container will stop itself). You can also delete these containers if you no longer plan to use them. 
2) Maven command<br>
   This path requires more settings and services. You must have installed:
   - [x] Java 17 (JDK)
   - [x] PostgreSQL

   You should make the following settings:
      1) Create a database, name it `school`.
      2) Next, you should change some settings in the [application.yml](src/main/resources/application.yml) file:<br>
           a) replace `jdbc:postgresql://postgresqldb:5432/school` with `jdbc:postgresql://localhost:5432/school`;<br>
           b) change the username `serhii` to the name of the database owner `school` (usually the database owner is `postgres`);<br>
           c) finally replace `pass` with the database user password you use to connect to your local database. 

    At the end of these settings, you should get the following [application.yml](src/main/resources/application.yml) content:
    ```
    spring:
      datasource:
        driver-class-name: org.postgresql.Driver
        url: jdbc:postgresql://localhost:5432/school
        username: your-database-owner-name
        password: your-local-database-password

      flyway:
        baseline-on-migrate: true
    ```
    Now you have a database in which the necessary data will be stored. And modifying the [application.yml](src/main/resources/application.yml) file will ensure that the application can successfully connect to this database at runtime. Now you can run the application by executing the following commands in the root of the project:<br>

    - for Windows (cmd)
    ```
    >mvnw.cmd package -DskipTests
    ```
    - for Linux/MacOS
    ```
    >./mvnw package -DskipTests
    ```
    and further
    ```
    >java -jar target\school-app-0.0.1-SNAPSHOT.jar
    ```

Thus, after installation and launch, you will be able to use the application for the management of the educational institution.

>**WARNING**: If you want to run the program a second time, I recommend deleting the old database `school` and creating a new one with the same name. This action will ensure that new tables with new data are created in the new database when the application is launched.

## Tests
The application has a set of unit tests that you can also run and verify that they pass successfully. What you need to have to run the tests:
- [x] Java 17 (JDK)
- [x] Docker

*Why do you need Docker to run tests?* The reason is that the tests for some classes use a database that is deploying in a docker container, that is, we are dealing with Testcontainers. Therefore, before running the tests, make sure that the docker daemon is running on your device. Next, execute the following command in the root of the project:<br>
    
* for Windows (cmd)
```
>mvnw.cmd test
```
* for Linux/MacOS
```
>./mvnw test
```
She will do the tests.
