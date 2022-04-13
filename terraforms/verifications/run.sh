#! /bin/bash
source .env

_prepare_terraform() {
  eth_base_url01=$(echo $eth_base_url01 | sed "s|\/|\\\/|g")
  eth_base_url02=$(echo $eth_base_url02 | sed "s|\/|\\\/|g")
  dot_base_url01=$(echo $dot_base_url01 | sed "s|\/|\\\/|g")
  eth_service_url=$(echo $eth_service_url | sed "s|\/|\\\/|g")
  dot_service_url=$(echo $dot_service_url | sed "s|\/|\\\/|g")
  echo 'variable "project_prefix" {
    type        = string
    description = "The project prefix (mbr)."
  }
  variable "environment" {
    type        = string
    description = "Environment: dev, test..."
  }
  variable "network_interface" {
    type = string
  }
  variable "map_machine_types" {
    type = map
  }' > verification.tf

  nodeId="$(echo $RANDOM | md5sum | head -c 5)"
  while IFS="," read -r region zone continent temp
  do
    cat init.tpl | sed "s/\[\[DOMAIN\]\]/$domain/g" \
                    | sed "s/\[\[ETH_BASE_URL01\]\]/$eth_base_url01/g" \
                    | sed "s/\[\[ETH_BASE_URL02\]\]/$eth_base_url02/g" \
                    | sed "s/\[\[DOT_BASE_URL01\]\]/$dot_base_url01/g" \
                    | sed "s/\[\[ETH_SERVICE_URL\]\]/${eth_service_url}/g" \
                    | sed "s/\[\[ETH_SERVICE_KEY\]\]/${eth_service_key}/g" \
                    | sed "s/\[\[DOT_SERVICE_URL\]\]/${dot_service_url}/g" \
                    | sed "s/\[\[ZONE\]\]/$continent/g" \
                    | sed "s/\[\[BLOCKCHAIN_ENDPOINT\]\]/${blockchain_endpoint}/g" \
                    | sed "s/\[\[NODEID\]\]/$nodeId/g" > init_$continent.sh
    cat template |  sed "s/\[\[REGION\]\]/$region/g" \
                | sed  "s/\[\[CLOUD_ZONE\]\]/$zone/g" \
                | sed  "s/\[\[ZONE\]\]/$continent/g" \
                | sed  "s/\[\[EMAIL\]\]/$email/g" \
                | sed "s/\[\[NODEID\]\]/$nodeId/g"  >> verification.tf
  done < <(tail ../../credentials/zonelist.csv)
}

_create_vms() {
  terraform init
  terraform plan -var-file=./terraform.tfvars -out=verification.plan
  terraform apply verification.plan
}

_setup() {
  _prepare_terraform
  _create_vms
}
_clean() {
  echo "Cleaning up VMs: In Progress"
  terraform destroy
  if [[ "$?" != "0" ]]; then echo "Faile to execute: terraform destroy "; exit 1; fi
  echo "Cleaning up VMs: Passed"
}

$@