cd /d %~dp0
call mvn jar:jar
call mvn source:jar
call mvn javadoc:jar
call mvn deploy:deploy-file -DgroupId=ch.ralscha -DartifactId=extclassgenerator-apt-jdyun -Dversion=1.0.0-SNAPSHOT -DgeneratePom=true -Dpackaging=jar -DrepositoryId=jdnexus -Durl=http://192.168.0.215:8081/repository/maven-snapshots -Dfile=target/extclassgenerator-apt-1.0.0.jar -Djavadoc=target/extclassgenerator-apt-1.0.0-javadoc.jar -Dsources=target/extclassgenerator-apt-1.0.0-sources.jar -s cmd_deploy_settings.xml -e
