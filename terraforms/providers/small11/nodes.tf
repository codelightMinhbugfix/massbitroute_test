variable "project_prefix" {
    type        = string
    description = "The project prefix (mbr)."
  }
  variable "environment" {
    type        = string
    description = "Environment: dev, test..."
  }
  variable "default_zone" {
    type = string
  }
  variable "network_interface" {
    type = string
  }
  variable "map_machine_types" {
    type = map
  }

resource "google_compute_instance" "node-7560ac61-b767-4c70-98ce-0b38af6b2d8f" {
  name         = "mbr-node-small11-asia-southeast1-b-02"
  machine_type = var.map_machine_types["node"]
  zone         = "asia-southeast1-b"

  tags = ["http-server", "https-server", "node", "ethereum"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 10
    }
  }

  network_interface {
    network = "${var.network_interface}"

    access_config {
      // Ephemeral IP
    }
  }

  metadata_startup_script =  <<EOH
  #!/bin/bash
  sudo bash -c "$(curl -sSfL 'https://dapi.massbitroute.dev/api/v1/node_install?id=7560ac61-b767-4c70-98ce-0b38af6b2d8f&user_id=9f0d9491-c657-45e4-a9c8-e40532779278&blockchain=eth&network=mainnet&zone=AS&data_url=http://34.101.81.225:8545&app_key=GxH8dbvRoFJ51Zq55jrclQ&portal_url=https://portal.massbitroute.dev&env=dev')"  >> /home/verification.log
  EOH

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "massbit-dev@massbit-dev-335203.iam.gserviceaccount.com"
    scopes = ["cloud-platform"]
  }
}

resource "google_compute_instance" "gateway-e9368214-5852-48bd-8500-55aaf9f11282" {
  name         = "mbr-gw-small11-asia-southeast1-b-02-1"
  machine_type = var.map_machine_types["gateway"]
  zone         = "asia-southeast1-b"

  tags = ["http-server", "https-server"]

  boot_disk {
    initialize_params {
      image = "projects/ubuntu-os-cloud/global/images/ubuntu-2004-focal-v20210720"
      size = 10
    }
  }

  network_interface {
    network = "${var.network_interface}"

    access_config {
      // Ephemeral IP
    }
  }

  metadata_startup_script =  <<EOH
  #!/bin/bash
  sudo bash -c "$(curl -sSfL 'https://dapi.massbitroute.dev/api/v1/gateway_install?id=e9368214-5852-48bd-8500-55aaf9f11282&user_id=9f0d9491-c657-45e4-a9c8-e40532779278&blockchain=eth&network=mainnet&zone=AS&app_key=sPeU6yoLMISbGtgQvguxHw&portal_url=https://portal.massbitroute.dev&env=dev')"  >> /home/verification.log
  EOH

  service_account {
    # Google recommends custom service accounts that have cloud-platform scope and permissions granted via IAM Roles.
    email = "massbit-dev@massbit-dev-335203.iam.gserviceaccount.com"
    scopes = ["cloud-platform"]
  }
}
