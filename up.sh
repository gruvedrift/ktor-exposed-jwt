#!/bin/bash

set -e

if docker-compose up -d; then
  echo "✅  Started containers with Docker Compose."
  echo "Starting application ..."
  ./gradlew build && ./gradlew run
  else
    echo "⚠️ Docker Compose failed. Trying with Podman Compose ..."
    if podman compose up -d; then
      echo "✅  Started containers with Podman Compose."
      echo "Starting application ..."
      ./gradlew build && ./gradlew run
    else
      echo "❌ Failed to start containers with both Docker and Podman."
      exit 1
    fi
fi
exit 1