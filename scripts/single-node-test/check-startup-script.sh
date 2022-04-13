wait_startup_script_to_finish() {
    vm_name=$1
    vm_zone=$2
    echo -n "Wait for \"$vm_name\" startup script to exit."
    status=""
    while [[ -z "$status" ]]
    do
        sleep 3;
        echo -n "."
        status=$(gcloud compute ssh $vm_name --zone=$vm_zone --ssh-flag="-q" --command 'grep -m 1 "startup-script exit status" /var/log/syslog' 2>&-)
    done
    echo ""
}