def call(String branchName, String imageTag, String SSH_USER, String SSH_KEY_PATH, String VM_IP, String imageName) {
    def composeFile = ''
    if (branchName == 'testing') {
        composeFile = '/home/tayelolu/pythonapp2/docker-compose.testing.yml'
    } else if (branchName == 'staging') {
        composeFile = '/home/tayelolu/pythonapp2/docker-compose.staging.yml'
    } else if (branchName == 'main') {
        composeFile = '/home/tayelolu/pythonapp2/docker-compose.yml'
    } else {
        echo "Branch ${branchName} has no deployment config."
        return
    }

    sh "chmod 600 ${SSH_KEY_PATH}"

    echo "Deploying Docker image to VM..."

    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'cd /home/tayelolu/pythonapp2'"
    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'git fetch origin'"
    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'git checkout ${branchName}'"
    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'git pull origin ${branchName}'"
    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'docker-compose down --volumes --remove-orphans'"
    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'docker-compose build --no-cache'"
    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'docker pull ${imageName}:${imageTag}'"
    sh "ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${VM_IP} 'docker-compose -f ${composeFile} up -d'"
}
