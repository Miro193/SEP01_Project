pipeline {
    agent any
    environment {
        DOCKER_IMAGE_NAME = 'michabl/sep01-project'
        DOCKER_CREDENTIALS_ID = 'Docker_Hub'
        DOCKER_IMAGE_TAG = 'latest'
        PATH = "/usr/local/bin:${env.PATH}"
    }

    tools {
        maven 'Maven3'
        stage('Build & Package') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean package -DskipTests'
                    } else {
                        bat 'mvn clean package -DskipTests'
                    }
                }
            }
        }
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Michael_return27_11_2025', url: 'https://github.com/Miro193/SEP01_Project.git'
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean install'
                    } else {
                        bat 'mvn clean install'
                    }
                }
            }
        }

        stage('Unit Tests') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
            }
        }

        stage('Code Coverage') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn jacoco:report'
                    } else {
                        bat 'mvn jacoco:report'
                    }
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
                jacoco (path: 'target/jacoco.exec')
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = "${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                    if (isUnix()) {
                        sh "docker build -t ${imageTag} ."
                    } else {
                        bat "docker build -t ${imageTag} ."
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
            jacoco(path: '**/target/jacoco.exec')
        }
    }
}