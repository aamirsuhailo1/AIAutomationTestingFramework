pipeline {
    agent any
    
    tools {
        maven 'Maven3' // Use the Maven installation configured in Jenkins
        jdk 'JDK11'    // Use the JDK installation configured in Jenkins
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Get code from repository
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                // Clean and compile the project
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                // Run the tests
                sh 'mvn test'
            }
            post {
                always {
                    // Archive test results
                    junit '**/target/surefire-reports/*.xml'
                    
                    // Archive Extent reports
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'test-output/reports',
                        reportFiles: '*.html',
                        reportName: 'Extent Test Report',
                        reportTitles: 'Selenium Test Report'
                    ])
                    
                    // Archive screenshots of failures
                    archiveArtifacts artifacts: 'test-output/screenshots/**/*.png', allowEmptyArchive: true
                }
            }
        }
    }
    
    post {
        always {
            // Clean up workspace
            cleanWs()
        }
        success {
            echo 'Tests executed successfully!'
        }
        failure {
            echo 'Tests failed to execute!'
        }
    }
} 