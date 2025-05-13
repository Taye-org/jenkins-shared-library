def call(String branchName, String imageTag, String sshUser, String sshKeyPath, String vmIp, String imageName) {
    def composeFile = ''
    if (branchName == 'testing') {
        composeFile = '/home/tayelolu/pythonapp2/docker-compose.testing.yml'
    } else if (branchName == 'staging') {
        composeFile = '/home/tayelolu/pythonapp2/docker-compose.staging.yml'
    } else if (branchName == 'main') {
        composeFile = '/home/tayelolu/pythonapp2/docker-compose.yaml'
    } else {
        echo "Branch ${branchName} has no deployment config."
        return
    }

   sh "chmod 600 ${sshKeyPath}"

echo "Deploying Docker image to VM..."

sh """
    ssh -o StrictHostKeyChecking=no -i ${sshKeyPath} ${sshUser}@${vmIp} << EOF
        cd /home/tayelolu/pythonapp2 &&
        git fetch origin &&
        git checkout ${branchName} &&
        git pull origin ${branchName} &&
        docker pull ${imageName}:${imageTag} &&
        docker-compose -f ${composeFile} up -d
    EOF
"""

}
