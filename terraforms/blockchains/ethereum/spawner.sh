#! /bin/bash

while IFS="," read -r region zone
do
  cat eth.template |  sed "s/\[\[REGION\]\]/$region/g" | sed  "s/\[\[ZONE\]\]/$zone/g" >> eth.tf
done < <(tail nodelist.csv)
