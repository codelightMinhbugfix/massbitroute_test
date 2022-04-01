provider "google" {
    credentials = file("../project_key.json")
    project = "project-name"
    region  = "asia-southeast2"
    zone    = "asia-southeast2-a"
}
