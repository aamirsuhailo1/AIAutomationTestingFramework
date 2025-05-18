#!/bin/bash

# Simple script to run only API tests
mvn clean test -DskipBrowser=true -DsuiteXmlFile=api-runner.xml -Denv=qa 