MASSBITROUTE_CORE_IP=$(cat MASSBITROUTE_CORE_IP)
MASSBITROUTE_PORTAL_IP=$(cat MASSBITROUTE_PORTAL_IP)
MASSBITROUTE_RUST_IP=$(cat MASSBITROUTE_RUST_IP)

cat hosts-template | \
    sed "s/\[\[MASSBITROUTE_CORE_IP\]\]/$MASSBITROUTE_CORE_IP/g" | \
    sed "s/\[\[MASSBITROUTE_PORTAL_IP\]\]/$MASSBITROUTE_PORTAL_IP/g" | \
    sed "s/\[\[MASSBITROUTE_RUST_IP\]\]/$MASSBITROUTE_RUST_IP/g" > test-hosts-file


scp test-hosts-file hoang@$MASSBITROUTE_CORE_IP:/home/hoang/hosts
ssh hoang@$MASSBITROUTE_CORE_IP "sudo cp /home/hoang/hosts1 /etc/hosts1"

scp test-hosts-file hoang@$MASSBITROUTE_PORTAL_IP:/home/hoang/hosts
ssh hoang@$MASSBITROUTE_PORTAL_IP "sudo cp /home/hoang/hosts1 /etc/hosts1"

scp test-hosts-file hoang@$MASSBITROUTE_RUST_IP:/home/hoang/hosts
ssh hoang@$MASSBITROUTE_RUST_IP "sudo cp /home/hoang/hosts1 /etc/hosts1"

cp test-hosts-file /etc/hosts