resource "google_compute_instance" "default" {
  name         = "terraform-instance-node-ethereum-asia-new-02"
  machine_type = "e2-micro"
  zone         = "asia-southeast2-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 2000
    }
  }

  network_interface {
    network = "default"

    access_config {
      // Ephemeral IP
    }
  }

  metadata = {
    foo = "bar"
  }

  metadata_startup_script = "${file("init.sh")}"

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "massbit-dev@massbit-dev-335203.iam.gserviceaccount.com"
    scopes = ["cloud-platform"]
  }
}
