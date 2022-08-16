#!/bin/bash
ROOT=$(realpath $(dirname $(realpath $0))/)

docker build -t massbit/massbitroute_test_client:v0.1.0 .
