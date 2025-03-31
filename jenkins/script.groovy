def buildJar() {
    echo 'Build the application...'
    sh 'mvn package'
}

def runTest() {
    echo 'Run test and collect report...'
    try {
        sh 'mvn test'
    }finally {
        publishTestResults()
    }   
}

def publishTestResults() {
    try {
        junit 'target/surefire-reports/*.xml'
    }catch (Exception e) {
        echo "JUnit report collection failed: ${e.message}"
    }
}

def buillImage() {
    echo 'Buil image the docker and push docker hub...'
    withCredentials([usernamePassword(credentialsId: 'login_docker_hub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        try {
            sh 'docker build -t ngocdt1/simple-java-maven-app:ver-${BUILD_ID} .'
            sh 'echo $PASS | docker login -u $USER --password-stdin'
            sh 'docker push ngocdt1/simple-java-maven-app:ver-${BUILD_ID}'
            echo 'Docker images push success!'
        } catch (Exception e) {
            error 'Error when build/push Docker image: ' + e.getMessage()
        }
    }
}

def deployApp() {
    echo 'Start deploy appication to Kubernetes...'
    try {
        // // Đăng nhập vào Kubernetes cluster (nếu cần)
        // sh 'kubectl config use-context my-cluster'

        // // Triển khai ứng dụng từ file YAML
        // sh 'kubectl apply -f k8s/deployment.yaml'

        // // Kiểm tra trạng thái của pod
        // sh 'kubectl rollout status deployment/my-app'

        echo 'Deployment successful!'  
    } catch (Exception e) {
        echo "Deployment failed: ${e.message}"
        error("Stopping pipeline due to Kubernetes deployment failure")
    }
}

return this