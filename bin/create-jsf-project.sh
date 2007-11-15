#!/bin/bash

mvn archetype:create -DarchetypeGroupId=org.regola  -DarchetypeArtifactId=regola-jsf-archetype -DarchetypeVersion=1.0-M1-SNAPSHOT -DgroupId=$1 -DartifactId=$2 
