#!/bin/bash

# Script to build Docker images for all microservices using JIB plugin
# This script compiles and builds Docker images for: accounts, cards, eureka, gatewayserver, loans, configserver

set -e  # Exit on error

SERVICES=("accounts" "cards" "eureka" "gatewayserver" "loans" "configserver")
FAILED_SERVICES=()
SUCCESSFUL_SERVICES=()

echo "========================================"
echo "Building Docker Images for Microservices"
echo "========================================"
echo ""

for service in "${SERVICES[@]}"
do
    echo ""
    echo "----------------------------------------"
    echo "Building: $service"
    echo "----------------------------------------"

    SERVICE_PATH="$(pwd)/$service"

    if [ ! -d "$SERVICE_PATH" ]; then
        echo "❌ ERROR: Directory $SERVICE_PATH not found!"
        FAILED_SERVICES+=("$service")
        continue
    fi

    if [ ! -f "$SERVICE_PATH/mvnw" ]; then
        echo "❌ ERROR: mvnw not found in $SERVICE_PATH!"
        FAILED_SERVICES+=("$service")
        continue
    fi

    cd "$SERVICE_PATH"

    echo "Running: ./mvnw compile jib:dockerBuild"

    if ./mvnw compile jib:dockerBuild; then
        echo "✅ $service build completed successfully"
        SUCCESSFUL_SERVICES+=("$service")
    else
        echo "❌ $service build failed"
        FAILED_SERVICES+=("$service")
    fi

    cd - > /dev/null
done

echo ""
echo "========================================"
echo "BUILD SUMMARY"
echo "========================================"
echo ""
echo "✅ Successful builds (${#SUCCESSFUL_SERVICES[@]}):"
for service in "${SUCCESSFUL_SERVICES[@]}"
do
    echo "   - $service"
done

if [ ${#FAILED_SERVICES[@]} -gt 0 ]; then
    echo ""
    echo "❌ Failed builds (${#FAILED_SERVICES[@]}):"
    for service in "${FAILED_SERVICES[@]}"
    do
        echo "   - $service"
    done
    echo ""
    exit 1
else
    echo ""
    echo "✅ All microservices built successfully!"
    echo ""
    exit 0
fi
