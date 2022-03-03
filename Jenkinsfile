
pipeline {
    agent { label 'aws-tic-tac-toe' }

    environment {
        OLD_TAG="latest"
        NEW_TAG="latest"
        APP_NAME = "ticTac"
        IMG = "abdelrahmankha/tic-tac-toe-server"
    }

   stages {
       stage('build') {
           steps {
                echo "Building Docker image..."
                sh "docker stop ${APP_NAME}"
                sh "docker rm -f ${APP_NAME}"
                sh "docker rmi -f ${IMG}:${OLD_TAG}"
                sh "docker build -t ${IMG}:${NEW_TAG} ."
           }
       }
       stage('deploy') {
           steps {
               script {
                    echo 'deploying image....'
                    sh "docker run -d --name ${APP_NAME} --network appnw -p5000:5000 -p5001:5001 -e HOST_NAME=db_server -e DB_PORT=5432 ${IMG}:${NEW_TAG}"
                    // def shellCmd = "bash ./run.sh ${OLD_TAG} ${NEW_TAG}"
                    // def instanceName = "ubuntu@18.134.241.110"
                    // sshagent(['ec2-app']) {
                    //     sh "scp run.sh ${instanceName}:/home/ubuntu"
                    //     sh "ssh -o StrictHostKeyChecking=no ${instanceName} ${shellCmd}"
                    // }
               }
            }
        }
       stage('Push to Dockerhub'){
           steps {
               echo 'pushing to dockerhub repo...'
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'USERNAME', passwordVariable: 'PASS')]) {
                    sh 'echo $PASS | docker login -u $USERNAME --password-stdin'
                    sh "docker push ${IMG}:${NEW_TAG}"
                }
           }
       }
   }
}