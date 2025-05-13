def call(String imageName, String tag) {
    echo "Scanning Docker image ${imageName}:${tag} for vulnerabilities..."
    withCredentials([string(credentialsId: 'snyk-token', variable: 'SNYK_TOKEN')]) {
        sh """
            snyk auth \$SNYK_TOKEN
            snyk test --docker ${imageName}:${tag} --file=Dockerfile --severity-threshold=high || true
        """
    }
}
