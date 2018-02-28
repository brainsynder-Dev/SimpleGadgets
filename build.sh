#!/bin/bash
YEAR=$(date +"%Y")
MONTH=$(date +"%m")
GIT_TAG=$YEAR-$MONTH.$TRAVIS_BUILD_NUMBER
git config --local user.name ${GIT_NAME}
git config --local user.email ${GIT_EMAIL}
git tag $GIT_TAG