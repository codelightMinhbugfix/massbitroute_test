provider "google" {
    credentials = file("massbit-dev-335203-3163376e9a9a.json")
    project = "massbit-dev-335203"
    region  = "asia-southeast2" 
    zone    = "asia-southeast2-a"
}
