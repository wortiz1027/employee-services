#!/usr/bin/env groovy

library(
  identifier: 'jenkins-shared-library@master',
  retriever: modernSCM(
    [
      $class: 'GitSCMSource',
      remote: 'git@github.com:wortiz1027/jenkins-shared-library.git',
      credentialsId: 'GITHUB-LOGIN'
    ]
  )
)

pipeline {

	agent any

	/*parameters {
			choice(name: 'PARAM_BUILD_BRANCH', choices: ['master', 'develop'], description: 'Seleccione una rama:')
			string(name: 'PARAM_BUILD_VERSION', defaultValue: '1.0.0', description: 'Especifique la version de compilacion:')
    }*/

	triggers {
		pollSCM '* * * * *'
	}

	tools {
		maven 'MVN_3'
		jdk 'JDK_11'
    }

	environment {
		JOB_NAME      = "${env.JOB_NAME}"

		SLACK_USER    = "Jenkins";
		SLACK_CHANNEL = "#desarrollo"
        SLACK_URL     = 'https://io-developer.slack.com/services/hooks/jenkins-ci?token='
        SLACK_ICON    = 'https://wiki.jenkins-ci.org/download/attachments/2916393/logo.png'
        SLACK_TOKEN   = credentials("SLACK-TOKEN") //6sXFW1BR5BmAaFlhlTNWp50W

		REGISTRY = "wortiz1027/employee-services"
    	DOCKER_CREDENTIAL = "DHUB-TOKEN"
    	SYSTEM_TIME = sh (returnStdout: true, script: "date '+%Y%m%d%H%M%S'").trim()
	}

	stages {
		stage("initialization") {
               steps {
                   sh "mvn --version"
                   sh "java -version"
               }
        }

		stage('setup') {
			steps {
				sh 'export MAVEN_OPTS="-Xmx512m"'
				//git branch: "${PARAM_BUILD_BRANCH}", url: "git@github.com:wortiz1027/employee-services.git"
				checkOut("master", "git@github.com:wortiz1027/employee-services.git")
				sh 'mvn clean compile'
			}
		}

		/*stage('sonar') {
			steps {
				//sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.organization=SONAR_ORGANIZATION -Dsonar.login=SONAR_TOKEN'
				withSonarQubeEnv('SERVER-SONARQUBE') {
		            //sh 'mvn package sonar:sonar -Dsonar.branch.name=${PARAM_BUILD_BRANCH}' -> el parametro 'sonar.branch.name' solo está disponible para licencias comerciales
		            sh 'mvn package sonar:sonar'
		        }

		        //timeout(time: 10, unit: 'SECONDS') {
		        //    waitForQualityGate abortPipeline: true
		        //}
			}
		}*/

		/*stage('test') {
			parallel {
				stage('unit-test') {
					steps {
						sh 'mvn test -P dev'
					}
				}
				stage('integration-test') {
					steps {
						sh 'mvn verify -P itest'
					}
				}
			}
		}*/

		/*stage('reports') {
			parallel {
				stage('ut-reports') {
					steps {
							junit 'target/surefire-reports/*.xml'
						  }
				}

				stage('it-reports') {
					steps {
			         		junit 'target/failsafe-reports/*.xml'
			         	  }
				}
			}
		}*/

		/*stage('docker') {
			environment {
                   SYSTEM_TIME_FORMATED = sh (returnStdout: true, script: "date '+%Y-%m-%d %H:%M:%S'").trim()
            }
			steps {
				sh 'docker build --no-cache=true --build-arg BUILD_DATE="$SYSTEM_TIME_FORMATED" --build-arg BUILD_VERSION="$PARAM_BUILD_VERSION-$SYSTEM_TIME" --tag=$REGISTRY:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME" --rm=true .'
			}
		}

		stage('archive') {
			parallel {
				stage('artifactory') {
					steps {
							sh 'mvn deploy -Dmaven.test.skip=true'
						  }
				}

				stage('registry') {
					steps {
							script {
							 	withDockerRegistry(credentialsId: "$DOCKER_CREDENTIAL", url: "") {
				         			sh 'docker push $REGISTRY:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME"'
				         		}
							}
			         	  }
				}
			}
		}

		stage('clean') {
			steps {
                	sh 'mvn clean'
                	sh 'docker rmi $REGISTRY:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME"'
                	sh 'docker rmi $(docker images -f "dangling=true" -q)'
                	sh 'docker logout'
			}
		}*/

	}

	post {
		success {
			echo 'I will always say Hello again! 1'
		}

		unstable {
			echo 'I will always say Hello again! 2'
		}

		failure {
			echo 'I will always say Hello again! 3'
		}
	}

}
