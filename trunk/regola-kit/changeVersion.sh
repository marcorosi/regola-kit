find -name "pom.xml" -exec perl -pi -w -e 's/1\.1-SNAPSHOT/2.0-SNAPSHOT/g;'  {} \;
