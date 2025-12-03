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
        DOCKER_IMAGE_NAME = 'saeid1993/otp2_project'
        DOCKER_CREDENTIALS_ID = 'Docker_Hub'
        DOCKER_IMAGE_TAG = 'latest'
        PATH = "/usr/local/bin:${env.PATH}"

        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-17'
        MAVEN_HOME = tool 'MAVEN_HOME'
        JMETER_HOME = 'C:\\Tools\\apache-jmeter-5.6.3\\apache-jmeter-5.6.3'
        PATH = "${JAVA_HOME}\\bin;${MAVEN_HOME}\\bin;${JMETER_HOME}\\bin;${env.PATH}"

    }

    tools {
        maven 'MAVEN_HOME'
    }


    stages {
        stage('Checkout') {
            steps {
                git branch: 'Saeid-with-jmeter', url: 'https://github.com/Miro193/SEP01_Project.git'
            }
        }

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

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQubeServer') {
                        withCredentials([string(credentialsId: 'sonar-token-jenkins', variable: 'SONAR_TOKEN')]) {
                            if (isUnix()) {
                                sh """
                                    ${tool 'SonarScanner'}/bin/sonar-scanner \
                                    -Dsonar.projectKey=sep01-project \
                                    -Dsonar.projectName=SEP01-Project \
                                    -Dsonar.sources=src \
                                    -Dsonar.java.binaries=target/classes \
                                    -Dsonar.token=$SONAR_TOKEN
                                """
                            } else {
                                bat """
                                    ${tool 'SonarScanner'}\\bin\\sonar-scanner ^
                                    -Dsonar.projectKey=sep01-project ^
                                    -Dsonar.projectName=SEP01-Project ^
                                    -Dsonar.sources=src ^
                                    -Dsonar.java.binaries=target/classes ^
                                    -Dsonar.token=%SONAR_TOKEN%
                                """
                            }
                        }
                    }
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
                    script {
                            if (isUnix()) {
                                sh '''
                                   echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                                   docker push $DOCKER_IMAGE_NAME:$DOCKER_IMAGE_TAG
                                '''
                                } else {
                                  bat """
                                  echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin
                                  docker push %DOCKER_IMAGE_NAME%:%DOCKER_IMAGE_TAG%
                                """
                                }
                            }

                }
            }
        }
        stages {
                stage('Build') {
                    steps {
                        bat 'mvn clean install'
                    }
                }
                stage('Non-Functional Test') {
                    steps {
                        bat 'jmeter -n -t tests/performance/demo.jmx -l result.jtl'
                    }
                }
            }

    post {
        always {
            archiveArtifacts artifacts: 'result.jtl', allowEmptyArchive: true
            perfReport sourceDataFiles: 'result.jtl'
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












