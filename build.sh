#!/bin/bash
GIT_TAG=1.5.$TRAVIS_BUILD_NUMBER-SNAPSHOT
git config --local user.name ${GIT_NAME}
git config --local user.email ${GIT_EMAIL}
git tag $GIT_TAG