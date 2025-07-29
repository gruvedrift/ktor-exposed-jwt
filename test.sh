#!/bin/bash

set -e

# Refers to services in docker-compose.yml file
TEST_SERVICES=("podracing-test" "auth-test")
USED_TOOL=""

cleanup(){
  echo "🧹 Cleaning up containers..."
  for SERVICE in "${TEST_SERVICES[@]}"; do
    if [[ "$USED_TOOL" == "docker" ]]; then
      docker-compose stop $SERVICE
      docker-compose rm -f $SERVICE
    elif [[ "$USED_TOOL" == "podman" ]]; then
        podman compose stop $SERVICE
        podman compose rm -f $SERVICE
    else
      echo "⚠️  No container tool recorded for cleanup."
    fi
  done
}

# Clean up, no matter what.
trap cleanup EXIT

if docker-compose up -d "${TEST_SERVICES[@]}"; then
  echo "✅  Started containers with Docker Compose."
  USED_TOOL="docker"
  else
    echo "⚠️ Docker Compose failed. Trying with Podman Compose ..."
    if podman compose up -d "${TEST_SERVICES[@]}"; then
      echo "✅  Started containers with Podman Compose."
      USED_TOOL="podman"
    else
      echo "❌ Failed to start containers with both Docker and Podman."
      exit 1
    fi
fi

echo "Running tests ..."
./gradlew test