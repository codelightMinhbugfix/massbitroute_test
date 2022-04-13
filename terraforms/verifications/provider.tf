provider "google" {
    credentials = file("../../credentials/project_key.json")
    project = "massbit-dev-335203"
    region  = "asia-southeast2"
    zone    = "asia-southeast2-a"
}
