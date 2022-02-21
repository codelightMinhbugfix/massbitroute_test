provider "google" {
    credentials = file("massbit-dev-335203-6dbae6a6939f.json")
    project = "massbit-dev-335203"
    region  = "asia-southeast2" 
    zone    = "asia-southeast2-a"
}