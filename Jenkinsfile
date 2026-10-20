pipeline {
    agent any

    options {
        skipDefaultCheckout(true)   // Explicit check in the first stage
        disableConcurrentBuilds()   // One deploy at a time
    }

    environment {
        PATH = "/opt/homebrew/bin:/usr/local/bin:/Applications/Docker.app/Contents/Resources/bin:${env.PATH}"
        JAVA_HOME = "/Users/gautamkappagal/.sdkman/candidates/java/17.0.10-tem"
        IMAGE = "gautamkappagal10/calculator"
        IMAGE_TAG = "${env.BUILD_NUMBER}"      // Read by docker-compose.yml too
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build') {
            steps { sh 'mvn -B clean package -DskipTests' }
        }

        stage('Test') {
            steps { sh 'mvn -B test' }
            post {
                always { junit 'target/surefire-reports/*.xml '}    // Publish results even on failure
            }
        }

        stage('Docker Build') {
            steps {sh 'docker build -t $IMAGE:$IMAGE_TAG -t $IMAGE:latest .' }
        }

        stage('Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
                    sh '''
                        echo "$DH_PASS" | docker login -u "$DH_USER" --password-stdin
                        docker push $IMAGE:$IMAGE_TAG
                        docker push $IMAGE:latest
                    '''
                }
            }

            post { always { sh 'docker logout || true' } }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker compose pull
                    docker compose up -d --remove-orphans --force-recreate --wait --wait-timeout 120
                    docker compose ps
                    curl -fsS http://localhost:8088/actuator/health; echo
                    curl -fsS "http://localhost:8088/api/v1/add?a=2&b=3"; echo
                '''
            }
        }
    }
}