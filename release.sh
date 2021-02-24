#!/usr/bin/env bash

set -e

#!/bin/sh
read -r -p "Enter the current build version (without snapshot): " currentVersion
read -r -p "Enter the version to release: " releaseVersion
read -r -p "Enter the next build version (without snapshot): " nextVersion
echo "Starting to release structurizr-clj $releaseVersion" && \
git pull --rebase && \
lein clean && \
lein test && \
echo "Changing Build version to $releaseVersion" && \
sed -i "s/\"${currentVersion}-SNAPSHOT\"/\"${releaseVersion}\"/g" project.clj && \
echo "Pushing changes to git" && \
git add project.clj && \
git commit -m "Version ${releaseVersion}" && \
git push && \
echo "Will create and push git tags.." && \
git tag -a "${releaseVersion}" -m "Released ${releaseVersion}" && \
git push --tags && \
echo "Updating version to ${nextVersion}" && \
sed -i "s/\"${releaseVersion}\"/\"${nextVersion}-SNAPSHOT\"/g" project.clj && \
git add project.clj
git commit -m "Version ${nextVersion}-SNAPSHOT" && \
git push && \
echo "Release of structurizr-clj $releaseVersion completed successfully!"
