#!/bin/bash

set -e

echo "Attempting to stop containers..."

if docker-compose down; then
  echo "✅ Containers stopped using Docker Compose."
  exit 0
else
  echo "⚠️ Docker Compose down failed. Trying with Podman Compose..."
fi

if podman compose down; then
  echo "✅ Containers stopped using Podman Compose."
  exit 0
else
  echo "❌ Failed to stop containers with both Docker and Podman."
  exit 1
fi