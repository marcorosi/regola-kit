find -name "pom.xml" -exec perl -pi -w -e 's/1\.2-SNAPSHOT/1.2/g;'  {} \;
