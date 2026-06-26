#!/usr/bin/env bash
set -euo pipefail

exec mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,address=*:5005,suspend=n"
