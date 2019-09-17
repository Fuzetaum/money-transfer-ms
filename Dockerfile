FROM maven:3.6.2-jdk-12

ENV DATABASE_PASSWORD r3volut!
ENV DATABASE_PORT 3306
ENV DATABASE_SCHEMA money_transfer_ms
ENV DATABASE_URL mysql
ENV DATABASE_USERNAME revolut
ENV PORT 8080

RUN mkdir -p /usr/src/ms
WORKDIR /usr/src/ms

COPY . /usr/src/ms

RUN mvn clean package

EXPOSE ${PORT}
CMD [ "java", "-jar", "target/test.backend.ricardofuzeto-0.1.0.jar"]