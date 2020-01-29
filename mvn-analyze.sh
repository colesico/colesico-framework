#!/bin/bash
mvn spotbugs:check -P modules,examples,analyze
