# Release Process

This guide provides a chronological steps which goes through release tagging, staging, verification and publishing.

## Check the SNAPSHOT builds and pass the tests

Check that the project builds in java 11, 17 and 21

```bash
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64/ mvn clean package install verify
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/ mvn clean package install verify
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64/ mvn clean package install verify
```


## Set version and build 

```bash
# change release in pom.xml
mvn clean package install verify
mvn clean deploy -Pdeploy
git add -A
git commit -S -m 'Release <1.1.1>'
git tag -a <1.1.1.> -m "Tagging release <1.1.1>"
git push
git push --tags
```


## Prepare next iteration

```bash
# change release in pom.xml
git add -A
git commit -S -m 'Next release cycle'
git push
```

## Create release and upload artifacts to Github

Manually creating the release in Github project page, and upload generated artifacts.