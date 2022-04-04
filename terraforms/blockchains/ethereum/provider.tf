provider "google" {
    credentials = file("../massbit-blockchains-673fd76dbdec.json")
    project = "massbit-blockchains"
    region  = "asia-southeast2"
    zone    = "asia-southeast2-a"
}
