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

	parameters {
			choice(name: 'PARAM_BUILD_BRANCH', choices: ['master', 'develop'], description: 'Seleccione una rama:')
			string(name: 'PARAM_BUILD_VERSION', defaultValue: '1.0.0', description: 'Especifique la version de compilacion:')
    }

	triggers {
		pollSCM '* * * * *'
	}

	tools {
		maven 'MVN_3'
		jdk 'JDK_11'
    }

	environment {
		JOB_NAME      = "${env.JOB_NAME}"

        PROJECT_NAME     = "employee-services"
		PUBLIC_REGISTRY  = "wortiz1027"
		PRIVATE_REGISTRY = "localhost:5000"
		DOCKER_TOKEN     = credentials("DHUB-TOKEN")
		PUBLIC_DOCKER_CREDENTIAL  = "DHUB-CREDENTIALS"
		PRIVATE_DOCKER_CREDENTIAL = "NEXUS-CREDENTIALS"
		SYSTEM_TIME = sh (returnStdout: true, script: "date '+%Y%m%d%H%M%S'").trim()
	}

	stages {
	    stage("initialization") {
		       steps {
                   sh "mvn --version"
                   sh "java -version"
		       }
		}

		stage('app-setup') {
			steps {
				sh 'export MAVEN_OPTS="-Xmx512m"'
				gitUtils "${PARAM_BUILD_BRANCH}", "git@github.com:wortiz1027/employee-services.git", 'GITHUB-LOGIN'
				sh 'mvn clean compile'
			}
		}

        stage('dependency-check') {
            steps {
                sh 'mvn org.owasp:dependency-check-maven:check'
            }
        }

		stage('tests') {
			parallel {
				stage('unit-test') {
					steps {
						sh 'mvn test -P test'
					}
				}
				stage('integration-test') {
					steps {
						sh 'mvn verify -P itest'
					}
				}
			}
		}

		stage('reports') {
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
		}

        stage('sonar') {
            steps {
                withSonarQubeEnv('SERVER-SONARQUBE') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=$PROJECT_NAME'
                }
            }
        }

		stage('docker') {
			environment {
                   SYSTEM_TIME_FORMATED = sh (returnStdout: true, script: "date '+%Y-%m-%d %H:%M:%S'").trim()
            }
			steps {
				sh 'DOCKER_BUILDKIT=1 docker build --no-cache=true --build-arg BUILD_DATE="$SYSTEM_TIME_FORMATED" --build-arg BUILD_VERSION="$PARAM_BUILD_VERSION-$SYSTEM_TIME" --tag=$PUBLIC_REGISTRY/$PROJECT_NAME:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME" --rm=true .'
			}
		}

		stage('archive') {
			parallel {
				stage('artifactory') {
					steps {
							sh 'mvn deploy -Dmaven.test.skip=true'
					}
				}

				stage('public-registry') {
					steps {
							script {
							 	docker.withRegistry("https://index.docker.io/v1/", "$PUBLIC_DOCKER_CREDENTIAL") {
                                  sh 'docker push $PUBLIC_REGISTRY/$PROJECT_NAME:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME"'
                                }
							}
			        }
				}

				stage('private-registry') {
                    steps {
                            script {
                                docker.withRegistry("http://localhost:5000", "$PRIVATE_DOCKER_CREDENTIAL") {
                                  sh 'docker tag $PUBLIC_REGISTRY/$PROJECT_NAME:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME" $PRIVATE_REGISTRY/$PROJECT_NAME:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME"'
                                  sh 'docker push $PRIVATE_REGISTRY/$PROJECT_NAME:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME"'
                                }
                            }
                    }
                }
			}
		}

		stage('clean') {
			steps {
                	sh 'mvn clean'
                	sh 'docker rmi $PUBLIC_REGISTRY/$PROJECT_NAME:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME"'
                	sh 'docker rmi $PRIVATE_REGISTRY/$PROJECT_NAME:"v$PARAM_BUILD_VERSION-$SYSTEM_TIME"'
                	sh 'docker logout'
			}
		}

        stage('k8s-setup') {
            steps {
                sh 'kubectl cluster-info'
                sh 'kubectl config view'
                gitUtils "master", "git@github.com:wortiz1027/k8s.git", 'GITHUB-LOGIN'
                sh '''
                    sed -i "s|{{IMAGE_NAME}}|$PUBLIC_REGISTRY/$PROJECT_NAME:v$PARAM_BUILD_VERSION-$SYSTEM_TIME|" lab2/development/07-employee-deployment.yaml
                '''
                sh 'cat lab2/development/07-employee-deployment.yaml'
            }
        }

        stage('k8s-deploy') {
            steps {
                sh 'export'
                sh 'kubectl apply -f lab2/development/00-employee-namespaces.yaml'
                sh 'kubectl apply -f lab2/development/01-employee-resource-quota.yaml'
                sh 'kubectl apply -f lab2/development/02-employee-externalservice-keycloak.yaml'
                sh 'kubectl apply -f lab2/development/02-employee-externalservice-mysql.yaml'
                sh 'kubectl apply -f lab2/development/03-employee-persistentvolume.yaml'
                sh 'kubectl apply -f lab2/development/04-employee-persistentvolumeclaim.yaml'
                sh 'kubectl apply -f lab2/development/05-employee-sealed-secrets-application.yaml'
                sh 'kubectl apply -f lab2/development/06-employees-configmap.yaml'
                sh 'cat lab2/development/07-employee-deployment.yaml'
                sh 'kubectl apply -f lab2/development/07-employee-deployment.yaml'
                sh 'kubectl apply -f lab2/development/08-employee-service.yaml'
                sh 'kubectl apply -f lab2/development/09-employee-sealed-secrets-tls.yaml'
                sh 'kubectl apply -f lab2/development/10-employee-ingress.yaml'
            }
        }

	}

	post {
		always {
            notificationUtils currentBuild.result
        }
	}

}