#!/bin/bash

# To avoid application deployment simply
# create a symbolic link to src/main/webapp/
# in the webapps dir of Tomcat. Then run
# this script.

WAR_DIR=`find target/ -name lib`

rm -fr src/main/webapp/WEB-INF/classes	
rm -fr src/main/webapp/WEB-INF/lib
ln -s ../../../../$WAR_DIR/ src/main/webapp/WEB-INF/
ln -s ../../../../target/classes/ src/main/webapp/WEB-INF/