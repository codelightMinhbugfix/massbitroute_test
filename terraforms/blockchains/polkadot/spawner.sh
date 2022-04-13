#! /bin/bash
# while IFS="," read -r region zone
# do
#   cat polkadot.template |  sed "s/\[\[REGION\]\]/$region/g" | sed  "s/\[\[ZONE\]\]/$zone/g" >> client.tf
# done < <(tail zonelist-all.csv)
terraform init
terraform plan -var-file=../terraform.tfvars -out=polkadot.plan
terraform apply polkadot.plan
