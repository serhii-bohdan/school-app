# School App

## Motivation
From the very beginning, the main motivation for writing this project was:
1. Desire to acquire new knowledge in the field of such technologies: `Spring Boot`, `SQL`, `PostgreSQL`, `JDBC`, `Spring JDBС`, `Hibernate`, `Spring Data JPA`, `Docker Compose`, `Testcontainers`.
2. On the other hand, the task was to complete a task that belongs to the Java Spring Development course.

## Important

From the very beginning, the development of this project was carried out on [GitLab](https://gitlab.com/), this explains the fact that there is a configuration file `.gitlab-ci.yml` for CI/CD.
At the end of the main part of the development, the project was moved to [GitHub](https://github.com/). Accordingly, `.github/workflows/github-ci-cd.yml` was added in order to automate the development process here.

## Description
The main purpose of the `School App` is to simplify and automate the management of groups, students and courses in a particular educational institution. Therefore, the application interface provides thirteen options for controlling the above-mentioned participants in the educational process. These thirteen options are listed below:

1. Find all groups with less or equal students’ number.
2. Find all students related to the course with the given name.
3. Add a student to the course.
4. Remove the student from one of their courses.
5. Add a new group.
6. Update group information.
7. Delete a group.
8. Add a new student.
9. Update student information.
10. Delete a student.
11. Add a new course.
12. Update course information.
13. Delete a course.

**What happens at startup?** At the beginning of the program, the database is filled with randomly formed groups, students and courses (this is if before the database was completely empty, if not, then you can work with data that is already present in the database). This is done in order to facilitate the development of the program. So don't be surprised if you run the app and see some students, groups and courses, it's just *artificially generated data*. After successful initialization, you will see a menu in the console. Next, you can choose one of the options and execute it by entering the number of the option and pressing Enter.

**Technologies used:**
- *Java 17*;
- *Spring Boot*, *Spring Data JPA*;
- *Hibernate*;
- *PostgreSQL*, *Flyway*;
- *JUnit 5*, *Mockito*, *Testcontainers*;
- *Maven*, *Git*;
- *Docker*, *Docker Compose*;
- *GitLab CI/CD*, *GitHub Actions*.

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
   >Because this will lead to the fact that the application will immediately finish its work after launch.

   >**Note:** When you're done with the application in the container, don't forget to stop the database container (the application container will stop itself). You can also delete these containers if you no longer plan to use them. 
2) Maven command<br>
   This path requires more settings and services. You must have installed:
   - [x] Java 17 (JDK)
   - [x] PostgreSQL

   You should make the following settings:
      - Create a database, name it `school`.
      - Next, you should change some settings in the [application.yml](src/main/resources/application.yml) file:<br>
           a) replace `jdbc:postgresql://postgresqldb:5432/school` with `jdbc:postgresql://localhost:5432/school`;<br>
           b) change the username `serhii` to the name of the database owner `school` (usually the database owner is `postgres`);<br>
           c) finally replace `pass` with the database user password you use to connect to your local database. 

    At the end of these settings, you should get the following [application.yml](src/main/resources/application.yml) content:
    ```yml
    spring:
      datasource:
        driver-class-name: org.postgresql.Driver
        url: jdbc:postgresql://localhost:5432/school
        username: your-database-owner-name
        password: your-local-database-password
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
