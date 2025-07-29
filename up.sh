#!/bin/bash

set -e

# Refers to services in docker-compose.yml file
DEV_SERVICES=("podracing-dev" "auth-dev")
USED_TOOL=""


# Docker first
if docker-compose up -d "${DEV_SERVICES[@]}"; then
  echo "✅  Started containers with Docker Compose."
  echo "Starting application ..."
  ./gradlew build -x test && ./gradlew run
  else
    echo "⚠️ Docker Compose failed. Trying with Podman Compose ..."
    if podman compose up -d "${DEV_SERVICES[@]}"; then
      echo "✅  Started containers with Podman Compose."
      echo "Starting application ..."
      ./gradlew build -x test && ./gradlew run
    else
      echo "❌ Failed to start containers with both Docker and Podman."
      exit 1
    fi
fi
exit 1