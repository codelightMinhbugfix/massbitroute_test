#!/bin/bash
############# SENSITIVE DATA
# PRIVATE_GIT_GATEWAY_WRITE_USER=""
# GIT_GATEWAY_WRITE_PASSWORD=""
export PRIVATE_GIT_DOMAIN="git.massbitroute.dev"
export PRIVATE_GIT_READ_PASSWORD=$(echo massbit123 | sha1sum | cut -d " " -f 1)
export PRIVATE_GIT_READ_USERNAME="massbit"
PRIVATE_GIT_SSL_USERNAME="ssl"
PRIVATE_GIT_SSL_PASSWORD=$(echo sslssl | sha1sum | cut -d " " -f 1)
PRIVATE_GIT_SSH_USERNAME="ssh"
PRIVATE_GIT_SSH_PASSWORD=$(echo sshssh | sha1sum | cut -d " " -f 1)
SSH_RAW_PRIVATE_KEY='-----BEGIN OPENSSH PRIVATE KEY----- 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 -----END OPENSSH PRIVATE KEY-----'
SSH_PRIVATE_KEY=$(printf "%q" "$SSH_RAW_PRIVATE_KEY" | cut -d "'" -f 2)
SSH_PUBLIC_KEY="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDOhpCeqlJgJ+Ba+N0CKXkgZZJAye+TNfKs1yxw7r8DSnX2qvZRMgSRZ/hDVpq8Lh+RS3JLnwk23QG79l9i5hNGpj/RHnAk4n7yQ19B69lOJJygbMiu0Scbho8aTtBtXzvj5uvV5Dp1N81pJxW7MAWcwUvoA8qb4PZsdSbFc00cCMalsVO5rb2htwvmZiBUOAXkfQWGFt8o3Sf8WAePGswkiuKvc3lr0/+4cFoPxdi/2v3aK+5TKelyKAZ0QpK1aYucH7nqp0DidxGVxd+HY2etz6q/Q3kSpUInBR03f7S33n+9VtxFvgAdN1BbucoJ4bgTlpGEYrrOmY2Z1GCUvLGmgroMRDFSYCanT8RDQlJAz4j09MhVplYOK6VVLq1H4mNEz9g90O701dze+vRvaYu5d4fHgJNph0/HsMw5cwp6fMTuS4mnOl8I/jvYGb7mWMbRZmweU+7Ge+OE9aQ7l5LxO17j7e+hc2odCxdTPyKoCKF1ggBe4+S1BOm0CBRpiRk= hoang@DESKTOP-TS2FJML"
if [ -z "$1" ]
  then
    echo "ERROR: Git branch name is required"
    exit 1
fi
GIT_BRANCH=$1

if [ -z "$2" ]
  then
    echo "ERROR: Git MERGE branch name is required"
    exit 1
fi
GIT_MERGE_BRANCH=$2


#-------------------------------------------
# create terraform file for new node
#-------------------------------------------
sudo echo '
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
variable "email" {
  type = string
}
variable "map_machine_types" {
  type = map
}' >test-nodes.tf

nodeId="$(echo $RANDOM | md5sum | head -c 5)"
## CORE NODE
cat massbitroute-core-template-single | \
     sed "s/\[\[NODE_ID\]\]/$nodeId/g" | \
    sed "s/\[\[BRANCH_NAME\]\]/$GIT_BRANCH/g" | \
    sed "s/\[\[MERGE_BRANCH\]\]/$GIT_MERGE_BRANCH/g" | \
    sed "s/\[\[PRIVATE_GIT_READ_USERNAME\]\]/$PRIVATE_GIT_READ_USERNAME/g" | \
    sed "s/\[\[PRIVATE_GIT_READ_PASSWORD\]\]/$PRIVATE_GIT_READ_PASSWORD/g" | \
    sed "s/\[\[PRIVATE_GIT_DOMAIN\]\]/$PRIVATE_GIT_DOMAIN/g"  >> test-nodes.tf

## PORTAL NODE
cat massbitroute-portal-template-single | \
    sed "s/\[\[NODE_ID\]\]/$nodeId/g" | \
    sed "s/\[\[BRANCH_NAME\]\]/$GIT_BRANCH/g" | \
    sed "s/\[\[MERGE_BRANCH\]\]/$GIT_MERGE_BRANCH/g" | \
    sed "s/\[\[PRIVATE_GIT_READ_USERNAME\]\]/$PRIVATE_GIT_READ_USERNAME/g" | \
    sed "s/\[\[PRIVATE_GIT_READ_PASSWORD\]\]/$PRIVATE_GIT_READ_PASSWORD/g" | \
    sed "s/\[\[PRIVATE_GIT_DOMAIN\]\]/$PRIVATE_GIT_DOMAIN/g" | \
    sed "s/\[\[PRIVATE_GIT_SSL_USERNAME\]\]/$PRIVATE_GIT_SSL_USERNAME/g" | \
    sed "s/\[\[PRIVATE_GIT_SSL_PASSWORD\]\]/$PRIVATE_GIT_SSL_PASSWORD/g"  | \
    sed "s/\[\[PRIVATE_GIT_SSH_USERNAME\]\]/$PRIVATE_GIT_SSH_USERNAME/g" | \
    sed "s/\[\[PRIVATE_GIT_SSH_PASSWORD\]\]/$PRIVATE_GIT_SSH_PASSWORD/g" >> test-nodes.tf

# RUST NODE
cat massbitroute-rust-template-single | \
     sed "s/\[\[NODE_ID\]\]/$nodeId/g" | \
    sed "s/\[\[BRANCH_NAME\]\]/$GIT_BRANCH/g" | \
    sed "s/\[\[MERGE_BRANCH\]\]/$GIT_MERGE_BRANCH/g" | \
    sed "s/\[\[PRIVATE_GIT_READ_USERNAME\]\]/$PRIVATE_GIT_READ_USERNAME/g" | \
    sed "s/\[\[PRIVATE_GIT_READ_PASSWORD\]\]/$PRIVATE_GIT_READ_PASSWORD/g" | \
    sed "s/\[\[PRIVATE_GIT_DOMAIN\]\]/$PRIVATE_GIT_DOMAIN/g"  | \
    sed "s/\[\[PRIVATE_GIT_SSH_USERNAME\]\]/$PRIVATE_GIT_SSH_USERNAME/g" | \
    sed "s/\[\[PRIVATE_GIT_SSH_PASSWORD\]\]/$PRIVATE_GIT_SSH_PASSWORD/g" >> test-nodes.tf

#-------------------------------------------
#  Spin up new VM on GCE
#-------------------------------------------
echo "Create new node VMs on GCE: In Progress"
terraform init -input=false
if [[ "$?" != "0" ]]; then
  echo "terraform init: Failed "
  exit 1
fi
sudo terraform plan -out=tfplan -input=false
if [[ "$?" != "0" ]]; then
  echo "terraform plan: Failed "
  exit 1
fi
sudo terraform apply -input=false tfplan
if [[ "$?" != "0" ]]; then
  echo "terraform apply: Failed"
  exit 1
fi
echo "Create node VMs on GCE: Passed"

CORE_IP=$(terraform output -raw mbr_core_public_ip)
PORTAL_IP=$(terraform output -raw mbr_portal_public_ip)
RUST_IP=$(terraform output -raw mbr_rust_public_ip)

echo $CORE_IP > MASSBITROUTE_CORE_IP
echo $PORTAL_IP > MASSBITROUTE_PORTAL_IP
echo $RUST_IP > MASSBITROUTE_RUST_IP



# #-------------------------------------------
# #  Update new IP in GWMan
# #-------------------------------------------
# # save old IP 
# NEW_API_IP=$(terraform output -raw mbrcore_public_ip)

# sudo mkdir -p /massbit/gwman
# sudo chmod 766 /massbit/gwman
# cd /massbit/gwman
# git clone http://$PRIVATE_GIT_GATEWAY_WRITE_USER:$GIT_GATEWAY_WRITE_PASSWORD@$PRIVATE_GIT_DOMAIN/massbitroute/gwman.git 

# OLD_API_IP=$(egrep -i "^dapi A" data/zones/massbitroute.dev | cut -d " " -f 3)

# cd data/zones
# cp massbitroute.dev massbitroute.dev.bak

# # replace new IP 
# sed -i "s/^dapi A.*/dapi A $NEW_API_IP/g"

# git add .
# git commit -m "Update entry for dapi [OLD] $OLD_API_IP - [NEW] $NEW_API_IP"
# # git push origin master


# # waiting for DNS to udpate
# while [ "$(nslookup dapi.massbitroute.dev  | grep "Address: $NEW_API_IP")" != "Address: $NEW_API_IP" ]
# do
#   echo "Waiting for DNS to update new API IP ..."
#   sleep 10
# done 
# echo "DNS entry for api updated: ${nslookup dapi.massbitroute.dev  | grep "Address: $NEW_API_IP"}"
