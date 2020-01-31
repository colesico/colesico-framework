#!/bin/bash

# Up the version of root pom then run the follow command to update parent version 
# in the children poms:

mvn -N versions:update-child-modules

# to display dependency updates run:
# mvn versions:display-dependency-updates
