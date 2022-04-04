NODES_EACH_ATTEMPT=1

ZONE="EU"

# # check if zone source exist 
# source_info=$(cat eth-sources.csv | grep ",$ZONE,")
# if [[ $? != 0 ]]
# then 
#     echo "Zone does $ZONE not exist in source file"
# fi


# CLOUD_REGION=$(echo $source_info | cut -d "," -f 2)
# CLOUD_ZONE=$(echo $source_info | cut -d "," -f 3)
# ZONE=$(echo $source_info | cut -d "," -f 4)
# DATASOURCE=$(echo $source_info | cut -d "," -f 5)



echo -n > gatewaylist.csv
echo -n > nodelist.csv

echo 'variable "project_prefix" {
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
}' > test-nodes.tf

echo "--------------------------------------"
echo "- Create test with prefix $nodePrefix"
echo "--------------------------------------"

source .env
while IFS="," read -r id region zone zoneCode dataSource totalNode
do
    nodeCount=0

    while [ "$nodeCount" -lt "$totalNode" ]
    do
        echo "Test"
    done






done < <(tail eth-sources.csv)


