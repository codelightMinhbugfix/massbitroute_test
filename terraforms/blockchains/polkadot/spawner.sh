#! /bin/bash
# while IFS="," read -r region zone
# do
#   cat polkadot.template |  sed "s/\[\[REGION\]\]/$region/g" | sed  "s/\[\[ZONE\]\]/$zone/g" >> polkadot.tf
# done < <(tail nodelist.csv)
terraform init
terraform plan -var-file=../terraform.tfvars -out=polkadot.plan
terraform apply polkadot.plan
