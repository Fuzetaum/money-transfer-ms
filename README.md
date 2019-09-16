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

> Note: if you're running the application as a Docker container, the following variables are unnecessary:

# Technology stack

Javalin - web service boot (uses Tomcat "under the hood")

jOOQ - ORM

MySQL database

Docker - modeling of application as a container, composition with MySQL container

# Assumptions

The following list contains all assumptions that outline the scope of this project:

* This is a microservice, part of a web service's ecosystem;
* There is another microservice, that handles management of bank accounts. So, this microservice doesn't need to deal with bank accounts' CRUD operations;
* This microservice will only handle transferences between bank accounts;

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