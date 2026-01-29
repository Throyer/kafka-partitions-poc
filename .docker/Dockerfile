FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

COPY .maven/settings.xml /root/.m2/settings.xml

ENV DOCKERIZE_VERSION=v0.6.1

RUN apt-get update && apt-get install -y wget
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz 

ENTRYPOINT ["/bin/bash", "./entrypoint.sh"]