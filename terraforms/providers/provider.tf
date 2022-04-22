provider "google" {
    credentials = file("[[CREDENTIALS_PATH]]")
    project = "massbit-dev-335203"
    region  = "asia-southeast2"
    zone    = "asia-southeast2-a"
}
