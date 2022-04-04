variable "project_prefix" {
  type        = string
  description = "The project prefix (mbr)."
}
variable "environment" {
  type        = string
  description = "Environment: dev, test..."
}
variable "email" {
  type = string
}
resource "google_compute_instance" "polkadot-northamerica-northeast2-a" {
  name         = "${var.project_prefix}-${var.environment}-polkadot-northamerica-northeast2-a"
  machine_type = "e2-small"
  zone         = "northamerica-northeast2-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 200
    }
  }

  network_interface {
    network = "default"

    access_config {
      // Ephemeral IP
    }
  }

  metadata = {

  }

  metadata_startup_script = "${file("init.sh")}"

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "${var.email}"
    scopes = ["cloud-platform"]
  }
}
resource "google_compute_instance" "polkadot-us-central1-a" {
  name         = "${var.project_prefix}-${var.environment}-polkadot-us-central1-a"
  machine_type = "e2-small"
  zone         = "us-central1-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 200
    }
  }

  network_interface {
    network = "default"

    access_config {
      // Ephemeral IP
    }
  }

  metadata = {

  }

  metadata_startup_script = "${file("init.sh")}"

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "${var.email}"
    scopes = ["cloud-platform"]
  }
}
resource "google_compute_instance" "polkadot-europe-central2-a" {
  name         = "${var.project_prefix}-${var.environment}-polkadot-europe-central2-a"
  machine_type = "e2-small"
  zone         = "europe-central2-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 200
    }
  }

  network_interface {
    network = "default"

    access_config {
      // Ephemeral IP
    }
  }

  metadata = {

  }

  metadata_startup_script = "${file("init.sh")}"

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "${var.email}"
    scopes = ["cloud-platform"]
  }
}
resource "google_compute_instance" "polkadot-asia-southeast1-a" {
  name         = "${var.project_prefix}-${var.environment}-polkadot-asia-southeast1-a"
  machine_type = "e2-small"
  zone         = "asia-southeast1-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 200
    }
  }

  network_interface {
    network = "default"

    access_config {
      // Ephemeral IP
    }
  }

  metadata = {

  }

  metadata_startup_script = "${file("init.sh")}"

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "${var.email}"
    scopes = ["cloud-platform"]
  }
}
resource "google_compute_instance" "polkadot-australia-southeast1-a" {
  name         = "${var.project_prefix}-${var.environment}-polkadot-australia-southeast1-a"
  machine_type = "e2-small"
  zone         = "australia-southeast1-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 200
    }
  }

  network_interface {
    network = "default"

    access_config {
      // Ephemeral IP
    }
  }

  metadata = {

  }

  metadata_startup_script = "${file("init.sh")}"

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "${var.email}"
    scopes = ["cloud-platform"]
  }
}
