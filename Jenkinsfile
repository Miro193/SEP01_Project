pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = "your-dockerhub-saeid1993/sep01-project"
        DOCKER_CREDENTIALS_ID = "dockerhub-credentials"
    }
    pipeline{
    agent any
     environment {
                PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"

                // Define Docker Hub credentials ID
                DOCKERHUB_CREDENTIALS_ID = 'docker_hub'
                // Define Docker Hub repository name
                DOCKERHUB_REPO = 'amirdirin/week6_livedemo_sep4'
                // Define Docker image tag
                DOCKER_IMAGE_TAG = 'latest'
            }

    tools{
    maven 'MAVEN_HOME'
    }

    stages{
         stage('checking'){
           steps{
           git branch:'main', url:'https://github.com/faripanah/week6_livedemo2_sep1.git'
           }


                   stage('Test') {
                       steps {
                           bat 'mvn test'
                       }
                   }
                   stage('Code Coverage') {
                       steps {
                           bat 'mvn jacoco:report'
                       }
                   }
                   stage('Publish Test Results') {
                       steps {
                           junit '**/target/surefire-reports/*.xml'
                       }
                   }
                   stage('Publish Coverage Report') {
                       steps {
                           jacoco()
                       }
                   }
         }
         }
    }

    stages {
        stage('Build & Test') {
            tools {
                maven 'MAVEN_HOME'
            }
            steps {
                bat "mvn clean install"
            }
        }

        stage('JaCoCo Code Coverage Report') {
            steps {
                jacoco execPattern: 'target/jacoco.exec'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image: ${DOCKER_IMAGE_NAME}"
                    docker.build(DOCKER_IMAGE_NAME)
                }
            }
        }

        stage('Push Docker Image') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "Pushing Docker image to Docker Hub..."
                    docker.withRegistry('https://registry.hub.docker.com', DOCKER_CREDENTIALS_ID) {
                        docker.image(DOCKER_IMAGE_NAME).push("latest")
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}