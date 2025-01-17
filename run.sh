#! /bin/bash

us=user-service
ps=package-service
mvn -f $us clean install
mvn -f $ps clean install

docker compose up
docker compose rm -fsv
mvn clean -f $us
mvn clean -f $ps

prefix=hh-school-arch-practice
docker image rm $prefix-$us-1
docker image rm $prefix-$us-2
docker image rm $prefix-$ps-1
