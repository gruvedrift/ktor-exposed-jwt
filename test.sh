#!/bin/bash

set -e

SERVICE_NAME="postgres-test"
USED_TOOL=""

cleanup(){
  echo "🧹 Cleaning up containers..."
  if [[ "$USED_TOOL" == "docker" ]]; then
    docker-compose stop $SERVICE_NAME
    docker-compose rm -f $SERVICE_NAME
  elif [[ "$USED_TOOL" == "podman" ]]; then
      podman compose stop $SERVICE_NAME
      podman compose rm -f $SERVICE_NAME
  else
    echo "⚠️  No container tool recorded for cleanup."
  fi
}

# Clean up, no matter what.
trap cleanup EXIT

if docker-compose up $SERVICE_NAME -d; then
  echo "✅  Started containers with Docker Compose."
  USED_TOOL="docker"
  else
    echo "⚠️ Docker Compose failed. Trying with Podman Compose ..."
    if podman compose up $SERVICE_NAME -d; then
      echo "✅  Started containers with Podman Compose."
      USED_TOOL="podman"
    else
      echo "❌ Failed to start containers with both Docker and Podman."
      exit 1
    fi
fi

echo "Running tests ..."
./gradlew test
