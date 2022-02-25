provider "google" {
    credentials = file("massbit-dev-335203-66590b8d2e1c.json")
    project = "massbit-dev-335203"
    region  = "asia-southeast2" 
    zone    = "asia-southeast2-a"
}
