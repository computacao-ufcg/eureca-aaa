#!/bin/bash

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <git_branch> <docker_tag>"
  exit 1
fi

git_branch=$1
docker_tag=$2

sudo docker build --build-arg EURECA_AS_BRANCH=$git_branch --build-arg EURECA_COMMON_BRANCH=$git_branch \
  --no-cache -t eureca/eureca-as:$docker_tag .

sudo docker push eureca/eureca-as:$docker_tag
