#!/bin/bash
# Name - javaDump.sh
# This script used to generate JVM memory and thread dump of tomcats
# Input 1 - Tomcat1 PID and Input 2 - Tomcat2 PID
# Note: Install this script to /opt/java-tools/javaDump.sh path
#
if [[ -z $(pgrep -f /opt/java-tools/javaDump.sh) ]]; then
conDate=$(date +%Y%m%d%H%M%S)
JAVA_HOME=/opt/java-tools/jdk1.8.0_191
pathTomcat1="/opt/logArchive/001/dump/tomcat1"
pathTomcat2="/opt/logArchive/001/dump/tomcat2"
if [ ! -z ${1} ] && [ ! -z ${2} ]; then
  echo "Tomcat  : PID-${1}"
  sudo -u tomcat ${JAVA_HOME}/bin/jstack -J-d64 -l ${1} > "${pathTomcat1}/jstack_tomcat1_$(date +%Y%m%d%H%M%S).dump"
  sudo -u tomcat ${JAVA_HOME}/bin/jmap -dump:format=b,file="${pathTomcat1}/jmap_tomcat1$(date +%Y%m%d%H%M%S).hprof" ${1}
#
  echo "Tomcat  : PID-${2}"
  sudo -u tomcat ${JAVA_HOME}/bin/jstack -J-d64 -l ${2} > "${pathTomcat2}/jstack_tomcat2_$(date +%Y%m%d%H%M%S).dump"
  sudo -u tomcat ${JAVA_HOME}/bin/jmap -dump:format=b,file="${pathTomcat2}/jmap_tomcat2_$(date +%Y%m%d%H%M%S).hprof" ${2}
else
  echo "ERROR: Enter tomcat PID as input"
fi
fi
