#!/bin/bash
#!/bin/bash
ROOT_DIR=$(realpath $(dirname $(realpath $0)))

DOCKER_ENV=/test/postman/docker.postman_environment.json
collections=( "Portal-Node" "Portal-Gateway" )
for collection in ${collections[@]}; do
  echo "Run newman test for collection $collection"
  COLLECTION_PATH="/test/postman/$collection.postman_collection.json";
  docker exec mbr_proxy_$network_number newman run $COLLECTION_PATH -e $DOCKER_ENV
done
