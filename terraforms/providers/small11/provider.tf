provider "google" {
    credentials = file("../../../credentials/massbit-dev-335203-c3bcd3a3da7f.json")
    project = "massbit-dev-335203"
    region  = "asia-southeast2"
    zone    = "asia-southeast2-a"
}
