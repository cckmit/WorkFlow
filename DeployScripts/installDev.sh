#!/bin/bash
#*****************************************************************************#
#   SCRIPT NAME: install.sh                                                   #
#                                                                             #
#   DESCRIPTION:                                                              #
#      Script to perform WorkFlow application installation                    #
#                                                                             #
#   INPUT:                                                                    #
#      install.sh TOS TOS_SERVER_IP TOS_SERVER_USER TOS_SERVER_PASS           #
#      install.sh SHELL APPSRVER SRV_USER SRV_PASS ZTPF_TD                    #
#                                                                             #
#      $2  - Core or system                                                   #
#      $3  - Production path                                                  #
#      $4  - Segment full path                                                #
#                                                                             #
#   OUTPUT:                                                                   #
#      exit with 0, successful process                                        #
#      exit with 8, error message                                             #
#                                                                             #
#*****************************************************************************#
#*****************************************************************************#
#                                                                             #
#                            M A I N T E N A N C E                            #
#                                                                             #
#-----------------------------------------------------------------------------#
#   MMDDYYYY    INIT    COMMENT                                               #
#   --------    ----    ------------------------------------------------------#
#   03022017    ARUL    Created the script                                    #
#   08312018    ARUL    TOS Deployment updated                                #
#*****************************************************************************#
declare -i _ReIP="$#";                 #Received number of input parms
INSTALL_MENU="$1"
#
RC=0; #Success return code
EC=8; #Failed return code
#
#-----------------------------------------------------------------------------#
# TOS Deployment - Windows Servers
function tosDeploy {
  local _RC=0;
  echo pwd
  cp TOSAutomation/target/TOSAutomation*dependencies.jar TOSAutomation.jar
  if [[ "$(net rpc service status FTPSVC -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service)" =~ running ]]; then
    echo -e "INFO: FTP Service running..."
  else
    net rpc service start FTPSVC -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service
    sleep 5
    echo -e "INFO: FTP Serice start initiated..."
  fi
  if [[ "$(net rpc service status WFTOSClient -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service)" =~ running ]]; then
    net rpc service stop WFTOSClient -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service
    sleep 5
    echo -e "INFO: TOS Service stopped."
  fi
  if [[ "$(net rpc service status WFTOSClient -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service)" =~ stop ]]; then
    echo -e "INFO: TOS deploy initiated..."
    curl -q -T TOSAutomation.jar ftp://"$TOS_SERVER_IP" --user "${TOS_SERVER_USER}:${TOS_SERVER_PASS}"; RC=$?;
    if [ "$RC" -eq 0 ]; then
      rm -f TOSAutomation.jar
      curl -q -T "/opt/mtp/app.properties" ftp://"$TOS_SERVER_IP" --user "${TOS_SERVER_USER}:${TOS_SERVER_PASS}" &> /dev/null; _RC=$?;
      if [ "$RC" -eq 0 ]; then
        if [[ "$(net rpc service status ActiveMQ -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service)" =~ stop ]]; then
          echo -e "INFO: ActiveMQ Service start initiated..."
          net rpc service start ActiveMQ -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service
          sleep 8
        else
          echo -e "INFO: ActiveMQ Service running..."
        fi
        TOSSRV="$(net rpc service start WFTOSClient -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service &> /dev/null)"
        echo "$TOSSRV"
        sleep 5
        if [[ "$(net rpc service status WFTOSClient -I "$TOS_SERVER_IP" -U "galileo/${TOS_SERVER_USER}%${TOS_SERVER_PASS}" | grep service)" =~ running ]]; then
          echo -e "INFO: TOS - Service restarted"
        else
          echo -e "INFO: TOS - Service delayed/failed to start"
        fi
      fi
    else
      echo -e "ERROR: FTP failed to TOS Server"
      _RC="$EC"
    fi
  fi
  return "${_RC}"
}
#
#-----------------------------------------------------------------------------#
# ShellScript Deployment in APP server
function shellLinuxDeploy {
  local _ZRC=0;
  local _RC=0;
  sshpass -p "$SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 ShellScripts/src/main/resources/{mtp*,.mtp*,tsi*} "${SRV_USER}@${APPSRVER}":/opt/mtp; _ZRC=$?;
  if [ "${_ZRC}" -ne 0 ]; then
    echo -e "ERROR: Shell script deploy failed - ${APPSRVER}"
    _RC="$EC";
  else
    sshpass -p "$SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$SRV_USER" "$APPSRVER" sed -i "s/vhldvztdt001.tvlport.net/$APPSRVER/g" /opt/mtp/.mtpconfig; _ZRC=$?;
    if [ "${_ZRC}" -ne 0 ]; then
      echo -e "ERROR: Shell script config failed - ${APPSRVER}"
      _RC="$EC";
    fi
    sshpass -p "$SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$SRV_USER" "$APPSRVER" sed -i "s/vhldvztdt001.tvlport.net/$APPSRVER/g" /opt/mtp/mtpzrxretrive; _ZRC=$?;
    if [ "${_ZRC}" -ne 0 ]; then
      echo -e "ERROR: Shell script config failed - ${APPSRVER}"
      _RC="$EC";
    fi
  fi
  return "${_RC}"
}
#
#-----------------------------------------------------------------------------#
# ShellScript Deployment in ZLINUX servers
function shellZLinuxDeploy {
  local _ZRC=0;
  local _RC=0
  IFS=',' read -r -a ZLINUX <<< "$ZTPF_TD"
  for zLinux in "${ZLINUX[@]}"
  do
    sshpass -p "$SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 ShellScripts/src/main/resources/{mtp*,.mtp*,tsi*} "${SRV_USER}@${zLinux}":/opt/mtp; _ZRC=$?;
    if [ "${_ZRC}" -ne 0 ]; then
      echo -e "ERROR: Shell script deploy failed - ${zLinux}"
      _RC="$EC";
    else
      sshpass -p "$SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$SRV_USER" "${zLinux}" sed -i "s/vhldvztdt001.tvlport.net/$APPSRVER/g" /opt/mtp/.mtpconfig; _ZRC=$?;
      if [ "${_ZRC}" -ne 0 ]; then
        echo -e "ERROR: Shell script config failed - ${APPSRVER}"
        _RC="$EC";
      fi
      if [[ "${zLinux}" =~ 01 ]]; then
        sshpass -p "$SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$SRV_USER" "${zLinux}" sed -i "s/wspvm4.worldspan.com/wspdl2.worldspan.com/g" /opt/mtp/.mtpconfig; _ZRC=$?;
        if [ "${_ZRC}" -ne 0 ]; then
          echo -e "ERROR: Shell script config failed - ${APPSRVER}"
          _RC="$EC";
        fi
      elif [[ "${zLinux}" =~ 03 ]]; then
        sshpass -p "$SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$SRV_USER" "${zLinux}" sed -i "s/wspvm4.worldspan.com/gvm.galileo.com/g" /opt/mtp/.mtpconfig; _ZRC=$?;
        if [ "${_ZRC}" -ne 0 ]; then
          echo -e "ERROR: Shell script config failed - ${APPSRVER}"
          _RC="$EC";
        fi
      fi
    fi
  done
  unset IFS
  return "${_RC}" 
}
#-----------------------------------------------------------------------------#
# Input validation
function minInputValidation {
  # $1 - Expected input
  # $2 - Received input
  local x;
  local _RC=0;
  if [ "$2" -lt "$1" ]; then
    echo "ERROR: Missing inputs"; _RC=$EC;
  elif [ "$2" -gt "$1" ]; then
    echo "ERROR: Unexpected no of arguments - $2"; _RC=$EC;
  else
    for x; do
      if [ -z "$x" ]; then
        echo "ERROR: Verify input arguments some of item is empty"; _RC=$EC;
      fi
    done
  fi
  return "$_RC"
}
#-----------------------------------------------------------------------------#
# Main call
if [[ "$INSTALL_MENU" == TOS ]]; then
  declare -i _ExIP=4;                    #Expected number of input parms
  minInputValidation "${_ExIP}" "${_ReIP}"; RC="$?";
  if [ "$RC" -eq 0 ]; then
    TOS_SERVER_IP="$2"
    TOS_SERVER_USER="$3"
    TOS_SERVER_PASS="$4"
    tosDeploy; RC=$?;
  fi
elif [[ "$INSTALL_MENU" == LINUX ]]; then
  declare -i _ExIP=4;                    #Expected number of input parms
  minInputValidation "${_ExIP}" "${_ReIP}"; RC="$?";
  if [ "$RC" -eq 0 ]; then
    APPSRVER="$2"
    SRV_USER="$3"
    SRV_PASS="$4"
    shellLinuxDeploy; RC=$?;
  fi
elif [[ "$INSTALL_MENU" == ZLINUX ]]; then
  declare -i _ExIP=5;                    #Expected number of input parms
  minInputValidation "${_ExIP}" "${_ReIP}"; RC="$?";
  if [ "$RC" -eq 0 ]; then
    APPSRVER="$2"
    SRV_USER="$3"
    SRV_PASS="$4"
    ZTPF_TD="$5"
    shellZLinuxDeploy; RC=$?;
  fi
else
  echo -e "ERROR: Invalid install request - $INSTALL_MENU"
  RC=8;
fi
#-----------------------------------------------------------------------------#
exit "$RC"
#-----------------------------------------------------------------------------#
# vim: filetype=bash
