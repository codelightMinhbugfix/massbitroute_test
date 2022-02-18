provider "google" {
    credentials = file("massbit-dev-335203-a292379f726b.json")
    project = "massbit-dev-335203"
    region  = "asia-southeast2" 
    zone    = "asia-southeast2-a"
}
