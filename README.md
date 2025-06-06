# Spring boot learning project
Learn Spring boot through examples


### Java Style guide setup
`./gradlew spotlessCheck` - Check for Google Java Style
`./gradlew spotlessApply` - Apply Google Java Style changes

### Java static analysis setup
Run SonarQube community server
`docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:community`

- Login using admin/admin
- Create project
- Create API Token and configure in build.gradle
- Run the following command

`./gradlew sonar` - Run sonar scanning
