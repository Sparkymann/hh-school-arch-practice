#! /bin/bash

mvn -f $1 clean install

id=$(docker build -q --build-arg JAR_PATH=$1/target -f Dockerfile .)

mvn clean -f $1

docker run --rm --name $1 -p 8080:8080 $id

docker image rm $id
