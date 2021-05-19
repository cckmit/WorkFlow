#!/bin/bash
# Name - nfsmon.sh
# Network traffic metrics collection for performance testing
#
# This will monitor lock file at specified location in every 5min
# and this will execute until the lock file present at the location
# Note: Install this script at /opt/java-tools/nfsmons.sh path
#       activate crontab with "*/5 * * * * /opt/java-tools/nfsmon.sh"
#
if [[ -z $(pgrep -f /opt/java-tools/nfsmon.sh) ]]; then
  conDate=$(date +%Y%m%d)
  conFlag="/tmp/netsh.lock"
  pathNetwork="/opt/logArchive/001/dump/network/"
  mkdir -p "${pathNetwork}"
  declare -A LOH0_RX2 LOH0_TX2 ETH0_RX2 ETH0_TX2;
  while [[ -f ${conFlag} ]]
  do
    echo -e "\n$(date) $(nfsiostat /opt/mtpserver)" >> "${pathNetwork}/nfstrans_network_${conDate}.log"
  done
fi
exit 0
