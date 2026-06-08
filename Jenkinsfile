pipeline {

    agent any

    // Environment variables available to all stages
    environment {
        APP_URL = 'http://localhost:3000'
    }

    stages {

        // Stage 1 — Get the latest code from source control
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        // Stage 2 — Give execute permission to gradlew (needed on Linux/Mac)
        stage('Setup') {
            steps {
                echo 'Setting up Gradle wrapper...'
                sh 'chmod +x gradlew'
            }
        }

        // Stage 3 — Run all tests
        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                sh './gradlew test'
            }
        }

        // Stage 4 — Generate the Allure HTML report from test results
        stage('Generate Report') {
            steps {
                echo 'Generating Allure report...'
                sh './gradlew allureReport'
            }
        }

        // Stage 5 — Copy Allure report to test-output folder
        stage('Copy Report') {
            steps {
                echo 'Copying report to test-output...'
                sh './gradlew copyAllureReport'
            }
        }
    }

    // Post section runs AFTER all stages — regardless of pass or fail
    post {

        // Always archive logs, screenshots and the report
        always {
            echo 'Archiving test output...'

            // Archive everything in test-output folder
            archiveArtifacts artifacts: 'test-output/**/*', allowEmptyArchive: true

            // Publish JUnit test results so Jenkins shows pass/fail graph
            junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true

            // Publish Allure report as a tab in Jenkins
            allure includeProperties: false, results: [[path: 'test-output/allure-results']]
        }

        success {
            echo '✅ All tests PASSED'
        }

        failure {
            echo '❌ Some tests FAILED — check the Allure report and screenshots in test-output/'
        }
    }
}
