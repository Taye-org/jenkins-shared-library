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

sh """
    ssh -o StrictHostKeyChecking=no -i ${SSH_KEY_PATH} ${SSH_USER}@${vmIp} << EOF
        cd /home/tayelolu/pythonapp2 &&
        git fetch origin &&
        git checkout ${branchName} &&
        git pull origin ${branchName} &&
        docker pull ${imageName}:${imageTag} &&
        docker-compose -f ${composeFile} up -d
    EOF
"""

}
