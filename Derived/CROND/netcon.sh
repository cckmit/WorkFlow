#!/bin/bash
# Name - netcon.sh
# Network sockets metrics collection for performance testing
#
# This will monitor lock file at specified location in every 5min
# and this will execute until the lock file present at the location
# Note: Install this script at /opt/java-tools/netcon.sh path
#       activate crontab with "*/5 * * * * /opt/java-tools/netcon.sh"
#
if [[ -z $(pgrep -f /opt/java-tools/netcon.sh) ]]; then
  conDate=$(date +%Y%m%d)
  conFlag="/tmp/netsh.lock"
  pathTomcat1="/opt/logArchive/001/dump/tomcat1/"
  pathTomcat2="/opt/logArchive/001/dump/tomcat2/"
  mkdir -p "${pathTomcat1}" "${pathTomcat2}"
  #
  while [[ -f ${conFlag} ]]
  do
    echo -e "$(date) - PORT-8443[ESTABLISHED - $(netstat -ntap | grep 8443 | grep ESTABLISHED | wc -l), CLOSE_WAIT - $(netstat -ntap | grep 8443 | grep CLOSE_WAIT | wc -l), FIN_WAIT2 - $(netstat -ntap | grep 8443 | grep FIN_WAIT2 | wc -l)]; PORT-8446[ESTABLISHED - $(netstat -ntap | grep 8446 | grep ESTABLISHED | wc -l), CLOSE_WAIT - $(netstat -ntap | grep 8446 | grep CLOSE_WAIT | wc -l), FIN_WAIT2 - $(netstat -ntap | grep 8446 | grep FIN_WAIT2 | wc -l)]; PORT - 8445[ESTABLISHED - $(netstat -ntap | grep 8445 | grep ESTABLISHED | wc -l), CLOSE_WAIT - $(netstat -ntap | grep 8445 | grep CLOSE_WAIT | wc -l), FIN_WAIT2 - $(netstat -ntap | grep 8445 | grep FIN_WAIT2 | wc -l)]" >> "${pathTomcat1}/netcon_tomcat1_${conDate}.log"
    #
    # Second tomcat instance ports
    #
    echo -e "$(date) - PORT-9443[ESTABLISHED - $(netstat -ntap | grep 9443 | grep ESTABLISHED | wc -l), CLOSE_WAIT - $(netstat -ntap | grep 9443 | grep CLOSE_WAIT | wc -l), FIN_WAIT2 - $(netstat -ntap | grep 9443 | grep FIN_WAIT2 | wc -l)]; PORT-9446[ESTABLISHED - $(netstat -ntap | grep 9446 | grep ESTABLISHED | wc -l), CLOSE_WAIT - $(netstat -ntap | grep 9446 | grep CLOSE_WAIT | wc -l), FIN_WAIT2 - $(netstat -ntap | grep 9446 | grep FIN_WAIT2 | wc -l)]" >> "${pathTomcat2}/netcon_tomcat2_${conDate}.log"
    #
  done
fi
exit 0
