def call(def image, String tag, String branchTag) {
    echo "Pushing Docker image ${image.imageName()}:${tag}..."
    image.push(tag)
    image.push(branchTag)
}
