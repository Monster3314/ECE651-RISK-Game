#!/bin/bash

# first terminate any old ones
docker kill riskGame
docker rm riskGame

# now run the new one
docker run -d --name riskGame -p 1651:1651 -t ece651 ./gradlew run

