#!/bin/bash
# Name - netmon.sh
# Network traffic metrics collection for performance testing
#
# This will monitor lock file at specified location in every 5min
# and this will execute until the lock file present at the location
# Note: Install this script at /opt/java-tools/netmon.sh path
#       activate crontab with "*/5 * * * * /opt/java-tools/netmon.sh"
#
if [[ -z $(pgrep -f /opt/java-tools/netmon.sh) ]]; then
  conDate=$(date +%Y%m%d)
  conFlag="/tmp/netsh.lock"
  pathNetwork="/opt/logArchive/001/dump/network/"
  mkdir -p "${pathNetwork}"
  declare -A LOH0_RX2 LOH0_TX2 ETH0_RX2 ETH0_TX2;
  while [[ -f ${conFlag} ]]
  do
    DATA=""
    LOH0_RX1=$(cat /sys/class/net/lo/statistics/rx_bytes)
    LOH0_TX1=$(cat /sys/class/net/lo/statistics/tx_bytes)
    ETH0_RX1=$(cat /sys/class/net/eth0/statistics/rx_bytes)
    ETH0_TX1=$(cat /sys/class/net/eth0/statistics/tx_bytes)
    LOH0_DOWN=$(( LOH0_RX1 - LOH0_RX2[lo] ))
    LOH0_UP=$(( LOH0_TX1 - LOH0_TX2[lo] ))
    LOH0_RX2[lo]=$LOH0_RX1; LOH0_TX2[lo]=$LOH0_TX1
    ETH0_DOWN=$(( ETH0_RX1 - ETH0_RX2[lo] ))
    ETH0_UP=$(( ETH0_TX1 - ETH0_TX2[lo] ))
    ETH0_RX2[lo]=$ETH0_RX1; ETH0_TX2[lo]=$ETH0_TX1
    echo -e "$(date)\t$(hostname)\t|Kb/s\tlo:\tRX:\t$(( ${LOH0_DOWN} * 8 / 1000 ))\tTX:\t$(( ${LOH0_UP} * 8 / 1000 ))\teth0:\tRX:\t$(( ${ETH0_DOWN} * 8 / 1000 ))\tTX:\t$(( ${ETH0_UP} * 8 / 1000 ))" >> "${pathNetwork}/nettrans_network_${conDate}.log"
  done;
fi
exit 0
