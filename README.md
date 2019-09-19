# About

This project is a microservice designed to handle money transfer between two different bank accounts. Since it is part of a hiring process, all provided functionalities are much simpler than real-life systems. Also, refer to the [assumptions](#assumptions) section in order to understand the considered scope for this application.

# Environment variables

In order to successfully run the web service, set the following environment variables in advance:

* DATABASE_PASSWORD: password for MySQL username;
* DATABASE_PORT: MySQL instance port;
* DATABASE_SCHEMA: MySQL schema for this microservice;
* DATABASE_URL: MySQL instance URL;
* DATABASE_USERNAME: username used to connect to MySQL;
* PORT: port the application will listen to;
* TRANSFER_MAX_RETRIES: maximum amount of retries allowed for new transfer requests;

# Technology stack

[Docker](https://www.docker.com/) - modeling of application as a container, composition with MySQL container

[Flyway](https://flywaydb.org/) - database versioning and migration

[GSON](https://github.com/google/gson) - parsing of request and response bodies

[Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html) - programming language (JDK 12)

[Javalin](https://javalin.io/) - web service boot (uses Tomcat "under the hood")

[jOOQ](https://www.jooq.org/) - ORM

[Maven](https://maven.apache.org/) - dependency and application management

[MySQL](https://www.mysql.com/) - SQL database

[SLF4J](https://www.slf4j.org/) - information logging

# Running the application

## Maven

> NOTE: to run the application via this method, be sure to have all environment variables properly configured. Also, you will need a MySQL server instance running, as well as Maven and Java 12 properly installed and configured for CLI.

To run the application via Maven, just open a shell at the application folder's root, and run the command `mvn clean package`. This will generate a file `test.backend.ricardofuzeto-0.1.0.jar` inside the `target` folder.

Navigate to that folder and run the command `java -jar test.backend.ricardofuzeto-0.1.0.jar`, and the application will boot.

## Docker

> NOTE: to run the application via this method, it is needed only to configure the environment variable TRANSFER_MAX_RETRIES. You can use your Docker interface of choice.

To run the application as a Docker container, use your Docker interface to navigate to the application folder's root, and run both commands `docker-compose build` and `docker-compose up`. These will build the needed images for the application, and start both application and MySQL images.

# Assumptions

The following list contains all assumptions regarding the application's requirements and scope:

* This is a microservice, part of a web service's ecosystem;
* There is another microservice, that handles management of bank accounts. So, this microservice won't have to store and manage data regarding identification of bank accounts, neither their balances (each microservice shall have a very well defined responsibility and purpose);
* This microservice will only handle the logic behind transfering sums of money between two bank accounts - there will be a microservice that handles withdraws and deposits;

The application was designed to run inside a [Kubernetes](https://kubernetes.io/) orchestrated pod. Kubernetes is a complete microservice orchestration toolset, augmenting management and leveraging functionalities to better standards. The following list describes why Kubernetes was considered, outlining the benefits of this choice:

* Performs load balancing between instances of the same application by itself, being easily configured to do so. This way, the application don't have to be designed to handle load balancing;
* Ability to scale applications up and down, depending on the load. This way, it can be configured to scale applications automatically and faster, creating or dropping instances as needed;
* Each Kubernetes pod is just a Docker container, allowing applications to be created and composed with Docker while still compatible with running environment;
* Cron jobs allow the creation of scheduled jobs in an application without having to programatically create them, delegating such trigger to the environment. This allows a job trigger configuration to be changed without a redeploy of an application;
* Networks can be configured so pods may have special sets of allowed connections, thus improving the environment's security;
* It is possible to use Kubernetes REST API to implement service discovery, although complex to achieve.

Although the use of Kubernetes makes the setup and management of the running environment much more complex, it allows web services to be developed with much simpler designs, and to extract from them as much automated tasks as possible. Thus, for the sake of development, considering Kubernetes as available makes the development effort much lower.

## Application-level concurrency solutions

Although the assumptions above outline the solution for concurrency problems while minimizing the amount of lines of code needed to write, there are solutions that can be applied at application level. The known ones are:

* Read/Write model microservices: the deployment of microservices that have exclusive access to database read and write operations are a great solution for concurrency problems, while read/write operations can be synchronized and ordered, thus avoiding thread racing problems. However, this demands the development of at least one other microservice, which is responsible only for communicating with the database. It's really simple to build, however is another application to maintain, and troubleshoot if needed.

# Job configuration

According to the assumptions listed above, this application is expected to run inside a Kubernetes environment. This means that all Kubernetes features are available to use. To bring all jobs' configuration to a new level, it was considered the use of [cron jobs](https://kubernetes.io/docs/tasks/job/automated-tasks-with-cron-jobs/).

In order to successfully configure a cron job at Kubernetes, it is just needed to define which endpoint should be called from it.

The following description is an example of cron job, that runs every 60 minutes and send a GET request to the endpoint "/cron-job":

```
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: callout
spec:
  schedule: "*/60 * * * *"
  concurrencyPolicy: Forbid
  successfulJobsHistoryLimit: 1
  failedJobsHistoryLimit: 1
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: callout
            image: buildpack-deps:curl
            args:
            - /bin/sh
            - -ec
            - curl http://microservice-url/cron-job
          restartPolicy: Never
```

This application expects that the Kubernetes environment has cron jobs configured for all periodic tasks needed. This way, it's possible not only to oversee and manage jobs execution, but also to trigger them manually if needed by just sending a request to the job's endpoint.

Also, it's considered that it's possible to, at cron job level, count how many replicas of the same container are being executed. Since This application will have many `jar` container replicas running but only one MySQL container replica, it is best to use any resources available to handle concurrency between job executions, as well as attempting to distribute a job's work load among all replicas. To do so, the same cron job that will trigger job execution can also provide, in the request body, the current amount of replicas acknowledged by Kubernetes along with the instance index of each running instance.

This modeling leverages our jobs to a new level, where the running environment manages each job execution by itself, telling each microservice's running instance how exactly it should run its job instance. It also allows us to fully manage a job's execution, by keeping a job's trigger outside the application and being able to reconfigure it without the need of a redeploy.