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
resource "google_compute_instance" "eth-northamerica-northeast2-a" {
  name         = "${var.project_prefix}-${var.environment}-eth-northamerica-northeast2-a"
  machine_type = "e2-small"
  zone         = "northamerica-northeast2-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 20
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
resource "google_compute_instance" "eth-us-central1-a" {
  name         = "${var.project_prefix}-${var.environment}-eth-us-central1-a"
  machine_type = "e2-small"
  zone         = "us-central1-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 20
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
resource "google_compute_instance" "eth-europe-central2-a" {
  name         = "${var.project_prefix}-${var.environment}-eth-europe-central2-a"
  machine_type = "e2-small"
  zone         = "europe-central2-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 20
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
resource "google_compute_instance" "eth-asia-southeast2-a" {
  name         = "${var.project_prefix}-${var.environment}-eth-asia-southeast2-a"
  machine_type = "e2-small"
  zone         = "asia-southeast2-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 20
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
resource "google_compute_instance" "eth-australia-southeast1-a" {
  name         = "${var.project_prefix}-${var.environment}-eth-australia-southeast1-a"
  machine_type = "e2-small"
  zone         = "australia-southeast1-a"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 20
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
