def call(String imageName, String tag) {
    echo "Building Docker image ${imageName}:${tag}"
    def builtImage = docker.build("${imageName}:${tag}")
    return builtImage
}
