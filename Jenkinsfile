
pipeline {
    agent any

    environment {

        registryCredentials = 'docker-hub-credentials'
        dockerImageName = 'krystianzc/backend'
    }

    stages {
        stage('Clone repository') {
            steps {
                // Let's make sure we have the repository cloned to our workspace
                checkout scm
            }
        }

        stage('Build image') {
            steps {
                // This builds the actual image
                script {
                    app = docker.build(env.dockerImageName)
                }
            }
        }

        stage('Test image') {
            steps {
                // Ideally, you would run a test framework against the image.
                // For this example, we're using a simple echo statement as a placeholder for tests.
                script {
                    app.inside {
                        sh 'echo "Tests passed"'
                    }
                }
            }
        }

        stage('Push image') {
            steps {
                // Finally, push the image with two tags: BUILD_NUMBER and 'latest'
                script {
                    docker.withRegistry('https://registry.hub.docker.com', env.registryCredentials) {
                        app.push("${env.BUILD_NUMBER}")
                        app.push("latest")
                    }
                }
            }
        }
    }
}
