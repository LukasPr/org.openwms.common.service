#!groovy

node {
   def mvnHome
   stage('\u27A1 Preparation') {
      git 'git@github.com:openwms/org.openwms.common.service.git'
      mvnHome = tool 'M3'
   }
   parallel (
     "default-build": {
       stage('\u27A1 Build') {
          configFileProvider(
              [configFile(fileId: 'maven-local-settings', variable: 'MAVEN_SETTINGS')]) {
                sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS clean deploy -Ddocumentation.dir=${WORKSPACE} -Psordocs,sonatype -U"
          }
       }
       stage('\u27A1 Results') {
          archive '**/target/*.jar'
       }
       stage('\u27A1 Heroku Staging') {
          sh '''
              if git remote | grep heroku > /dev/null; then
                 git remote remove heroku
              fi
              git remote add heroku https://:${HEROKU_API_KEY}@git.heroku.com/openwms-common-services.git
              git push heroku master -f
          '''
       }
     },
     "sonar-build": {
       stage('\u27A1 Sonar') {
          sh "'${mvnHome}/bin/mvn' clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Djacoco.propertyName=jacocoArgLine -Dbuild.number=${BUILD_NUMBER} -Dbuild.date=${BUILD_ID} -Ddocumentation.dir=${WORKSPACE} -Pjenkins"
          sh "'${mvnHome}/bin/mvn' sonar:sonar -Pjenkins"
       }
     }
   )
}

