#!/bin/bash

dockerize \
  -wait tcp://rabbitmq:$RABBITMQ_PORT \
  -timeout 20s \
  -wait tcp://database:$DB_PORT \
  -timeout 20s \
  mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,address=*:$DEBUG_PORT,suspend=n"