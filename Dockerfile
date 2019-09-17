FROM maven:3.6.2-jdk-12

RUN mkdir -p /usr/src/ms
WORKDIR /usr/src/ms

COPY ./target/test.backend.ricardofuzeto-0.1.0-jar-with-dependencies.jar /usr/src/ms/

EXPOSE 8080
CMD [ "java", "-jar", "test.backend.ricardofuzeto-0.1.0-jar-with-dependencies.jar"]