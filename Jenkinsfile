def runCommand(command) {
    if (isUnix()) {
        // macOS and Linux
        sh command
    } else {
        // Windows
        bat command
    }
}

pipeline {
    agent any
    environment {
        DOCKER_IMAGE_NAME = 'mirovaltonen2/sep01-project'
        DOCKER_CREDENTIALS_ID = 'Docker_Miro_Hub'
        DOCKER_IMAGE_TAG = 'latest'
        PATH = "/usr/local/bin:${env.PATH}"
    }

    tools {
        maven 'Maven 3'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'miro-week7-1', url: 'https://github.com/Miro193/SEP01_Project.git'
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    runCommand('mvn clean install')
                }
            }
        }

        stage('Unit Tests') {
            steps {
                script {
                    runCommand('mvn test')
                }
            }
        }

        stage('Code Coverage') {
            steps {
                script {
                    runCommand('mvn jacoco:report')
                }
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                jacoco execPattern: 'target/jacoco.exec'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .'
                    } else {
                        bat 'docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .'
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG
                    """
                }
            }
        }

    }

    post {
        always {
            junit(testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true)
            jacoco(execPattern: '**/target/jacoco.exec', classPattern: '**/target/classes', sourcePattern: '**/src/main/java', inclusionPattern: '**/*.class', exclusionPattern: '')
        }
    }
}