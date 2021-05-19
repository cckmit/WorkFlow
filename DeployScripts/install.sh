#!/bin/bash
#*****************************************************************************#
#   SCRIPT NAME: install.sh                                                   #
#                                                                             #
#   DESCRIPTION:                                                              #
#      Script to perform WorkFlow application deployments                     #
#                                                                             #
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
#   03022018    ARUL    Created the script                                    #
#   04062018    ARUL    Updated to support MKS Projects                       #
#   07052018    ARUL    Updated to support zVM Update                         #
#   08102018    ARUL    Updated to support Generic build                      #
#   12052018    VINOTH  Reconsile job added to Webserver                      #
#   12242018    VINOTH  Two tomcat confirmation setup                         #
#   12262018    ARUL    Backup script for application                         #
#   04152019    ARUL    Gitblit application setup updated                     #
#   05252019    VINOTH  Backup rotate for 5 dates                             #
#*****************************************************************************#
declare -i _ReIP="$#";                 #Received number of input parms
declare -i _MiIP="27";                 #Expected number of input parms
#
ENV="${1^^}";                          #Input Deployment env (DEV,QA,PROD)
COMPANY="${2^^}";                      #Input Company name (TP, DL)
BUILDID="${3}";                        #Input Build number (1.20_2)
APP_SERVER="${4,,}";                   #Input Tomcat Application server
GIT_SERVER="${5,,}";                   #Input Git server URL or Load balancer DNS
TOS_SERVER="${6}";                     #Input TOS Server IP address
ZLX_SERVER="${7,,}";                   #Input zLinux Servers
APP_SRV_USER="${8}";                   #Input App deployment sudo user name
APP_SRV_PASS="${9}";                   #Input App deployment sudo user salt
TOS_SRV_USER="${10}";                  #Input Tos deployment user name
TOS_SRV_PASS="${11//\'/}";             #Input Tos deployment user salt - Replace quote
ADB_WFW_USER="${12}";                  #Workflow application database user name
ADB_WFW_PASS="${13}";                  #Workflow application database user salt
XDB_TIK_USER="${14}";                  #External Ticket service database user name
XDB_TIK_PASS="${15}";                  #External Ticket service database user salt
XDB_CSR_USER="${16}";                  #External CSR service database user name
XDB_CSR_PASS="${17}";                  #External CSR service database user salt
XDB_VPR_USER="${18}";                  #External VPARS database user name
XDB_VPR_PASS="${19}";                  #External VPARS database user salt
MTP_SRV_USER="${20}";                  #Input App Service user name
MTP_SRV_PASS="${21}";                  #Input App Service user salt
encrypted="${22}";                     #Encrypted values
tomcattype="${23}";                    #single or dual
ADB_ACT_USER="${24}";                  #Activity application database user name
ADB_ACT_PASS="${25}";                  #Activity application database user salt
TCS_MGR_USER="${26}";                  #Tomcat Server manager access user name
TCS_MGR_PASS="${27}";                  #Tomcat Server manager access user salt
#
APP="WF";                              #Application short name
SUF_SERVER="tvlport.net";              #Servers domain suffix
MTP_DOMAIN="galileo";                  #Network domain name(Windows NT)
TCS_SALT01="$(echo -en ${TCS_MGR_USER}:${TCS_MGR_PASS}|base64)"; #Format-2 Salt
TCS_CONN="https";                      #Tomcat Server manager port connector
TCS_PORT="8446";                       #Tomcat Server manager port number
TOS_SALT01="${MTP_DOMAIN}/${TOS_SRV_USER}%${TOS_SRV_PASS}"; #Format-1 Salt
TOS_SALT02="${TOS_SRV_USER}:${TOS_SRV_PASS}"; #Format-2 Salt
REL_PATH="/opt/delivery/release";      #Release path for the deployables
APP_PATH="/var/lib/tomcat/webapps";    #Application deployment path
AP2_PATH="/opt/tomcat2/webapps";       #Application deployment path for tomcat2
MTP_PATH="/opt/mtp";                   #Support scripts deployment path
BCK_PATH="/ztpfrepos/backup";          #Application Backup path
PDB_PORT="5432";                       #PostgrSQL database ports
GBL_DATA="gitblit/WEB-INF/data";       #Gitblit application data path
GBL_LIBS="gitblit/WEB-INF/lib";        #Gitblit application library path
PDB_SALT_WFW="${ADB_WFW_USER}:${ADB_WFW_PASS}"; #PostgreSQL WorkFlow db salt
PDB_SALT_ACT="${ADB_ACT_USER}:${ADB_ACT_PASS}"; #PostgreSQL activity db salt
RC=0;                                  #Success return code
EC=8;                                  #Failed return code
https_proxy="";                        #Unset existing Proxy setup if any
#
#-----------------------------------------------------------------------------#
# Input validation
function minInputValidation {
  # $1 - Expected input
  # $2 - Received input
  local x;
  if [ "$2" -lt "$1" ]; then
    echo "ERROR: Missing inputs"; RC=$EC;
  elif [ "$2" -gt "$1" ]; then
    echo "ERROR: Unexpected no of arguments - $2"; RC=$EC;
  else
    for x; do
      if [ -z "$x" ]; then
        echo "ERROR: Verify input arguments some of item is empty"; RC=$EC;
      fi
    done
  fi
  return "$RC"
}
#
#-----------------------------------------------------------------------------#
# Stop TOS Service which running in Windows Servers
function stopTOS {
  local _RC=0;
  local TOSSERVER;
  local TOSServer;
  IFS=',' read -r -a TOSSERVER <<< "$TOS_SERVER"
  for TOSServer in "${TOSSERVER[@]}"
  do
    # Stop TOS service if it's already running
    if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
      net rpc service stop WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service &> /dev/null
      echo -e "INFO: ${TOSServer} TOS Service stop initiated.";
      sleep 5;
    fi
    # Wait 120s if it's not stop TOS service
    if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ pending ]]; then
      echo -e "INFO: ${TOSServer} TOS Service pending to stop.";
      sleep 120;
    fi
    # Stop installation process if not stop TOS services
    if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ stopped ]]; then
      echo -e "INFO: ${TOSServer} TOS Service stopped - RC=${_RC}";
    else
      _RC="${EC}";
      echo -e "ERROR: ${TOSServer} TOS Service failed to stop - RC=${_RC}";
      break;
    fi
  done
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Stop Tomcat Service which running in Linux Servers
function stopTomcat {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for APPServer in "${APPSERVER[@]}"
  do
    # Stop tomcat service if it's already running
    if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ running ]]; then
      echo -e "INFO: ${APPServer} Tomcat Service stop initiated.";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl stop tomcat" &> /dev/null; _RC=$?;
      if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
        sleep 5;
        # Wait 5s more if tomcat service not stop
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ dead ]]; then
          echo -e "INFO: ${APPServer} Tomcat Service stopped - RC=${_RC}";
        else
          sleep 5;
        fi
      else
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ dead ]]; then
          echo -e "INFO: ${APPServer} Tomcat Service already stopped - RC=${_RC}";
        else
          _RC="${EC}";
          echo -e "ERROR: ${APPServer} Tomcat Service failed to stop - RC=${_RC}";
          break;
        fi
      fi
    else
      echo -e "INFO: Tomcat Service already stopped - RC-${_RC}";
    fi
  done
  unset IFS;
  return "${_RC}";
}
#-----------------------------------------------------------------------------#
# Stop Tomcat2 Service which running in Linux Servers
function stopTomcat2 {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for APPServer in "${APPSERVER[@]}"
  do
    # Stop tomcat service if it's already running
    if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ running ]]; then
      echo -e "INFO: ${APPServer} Tomcat-1 Service stop initiated.";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl stop tomcat" &> /dev/null; _RC=$?;
      if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
        sleep 5;
        # Wait 5s more if tomcat service not stop
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ dead ]]; then
          echo -e "INFO: ${APPServer} Tomcat-1 Service stopped - RC=${_RC}";
        else
          sleep 5;
        fi
      else
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ dead ]]; then
          echo -e "INFO: ${APPServer} Tomcat-1 Service already stopped - RC=${_RC}";
        else
          _RC="${EC}";
          echo -e "ERROR: ${APPServer} Tomcat-1 Service failed to stop - RC=${_RC}";
          break;
        fi
      fi
    else
      echo -e "INFO: Tomcat-1 Service already stopped - RC-${_RC}";
    fi
    # Force to stop tomcat service if it's keep running
    if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ running ]]; then
      echo -e "INFO: ${APPServer} Tomcat-2 Service stop initiated.";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl stop tomcat2" &> /dev/null; _RC=$?;
      if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
        sleep 5;
        # Wait 5s more if tomcat service not stop
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ dead ]]; then
          echo -e "INFO: ${APPServer} Tomcat-2 Service stopped - RC=${_RC}";
        else
          sleep 5;
        fi
      else
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ dead ]]; then
          echo -e "INFO: ${APPServer} Tomcat-2 Service already stopped - RC=${_RC}";
        else
          _RC="${EC}";
          echo -e "ERROR: ${APPServer} Tomcat-2 Service failed to stop - RC=${_RC}";
          break;
        fi
      fi
    else
      echo -e "INFO: Tomcat-2 Service already stopped - RC-${_RC}";
    fi
  done
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Backup applications from the servers
function backup {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local BACKUP_PATH;
  local tomcatOrder;
  local webappsName;
  local ZLXSERVER;
  local ZLXServer;
  local TOSSERVER;
  local TOSServer;
  local ZLINUX;
  local i;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}"; #Override with FQDN input
  local ZLX_SERVER="${ZLX_SERVER//.tvlport.net/}"; #Override with FQDN input
  IFS=',' read -r -a ZLXSERVER <<< "$ZLX_SERVER";
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER";
  IFS=',' read -r -a TOSSERVER <<< "$TOS_SERVER";
  for APPServer in "${APPSERVER[@]}"
  do
    echo -e "INFO: ======================================================================";
    echo -e "INFO: BACKUP PROCESS STARTED for ${APPServer}...";
    BACKUP_PATH="${BCK_PATH}/backup_$(date +%Y%m%d%H%M%S)"
    if [[ "$(sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "sudo mkdir -p ${BACKUP_PATH}")" =~ not ]]; then
      echo -e "ERROR: Failed to create backup directory - RC-${_RC}";
    else
      #
      # Backup from TOS Clients applications
      for((i=0; i<${#TOSSERVER[@]}; i++))
      do
        echo -e "INFO: ${TOSSERVER[$i]} in TOS Client backup initiated...";
        sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "sudo chmod 777 ${BACKUP_PATH}; cd ${BACKUP_PATH}; sudo curl -q -N --user ${TOS_SALT02} ftp://${TOSSERVER[$i]}/TOSAutomation.jar > ${TOSSERVER[$i]}_TOSAutomation.jar" &> /dev/null; _RC=$?;
        if [ "${_RC}" -eq 0 ]; then
          sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "sudo chmod 777 ${BACKUP_PATH}; cd ${BACKUP_PATH}; sudo curl -q -N --user ${TOS_SALT02} ftp://${TOSSERVER[$i]}/app.properties > ${TOSSERVER[$i]}_app.properties" &> /dev/null; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            echo -e "INFO: ${TOSSERVER[$i]} in TOS Client backup completed - RC=${_RC}";
          else
            _RC="${EC}";
            echo -e "ERROR: ${TOSSERVER[$i]} in TOS Client  Script backup failed to complete - RC=${_RC}";
            break;
          fi
        else
          _RC="${EC}";
          echo -e "ERROR: ${TOSSERVER[$i]} in TOS Client backup failed to complete - RC=${_RC}";
          break;
        fi
      done
      #
      # Backup from tomcat applications
      echo -e "INFO: ${APPServer} Tomcat backup intiated...";
      # Check tomcat2 instance setup
      if [ "${tomcattype}" == 'dual' ]; then
        tomcatOrder=("TC1_Webapps" "TC2_Webapps");
      else
        tomcatOrder=("TC1_Webapps");
      fi
      for webappsName in "${tomcatOrder[@]}"
      do
        if [[ "${webappsName}" =~ TC1 ]]; then
          sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "cd ${APP_PATH%/*}; sudo tar czvf ${BACKUP_PATH}/${webappsName}.tgz webapps/" &> /dev/null; _RC=$?;
        elif [[ "${webappsName}" =~ TC2 ]]; then
          sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "cd ${AP2_PATH%/*}; sudo tar czvf ${BACKUP_PATH}/${webappsName}.tgz webapps/" &> /dev/null; _RC=$?;
        fi
        if [ "${_RC}" -eq 0 ]; then
          echo -e "INFO: ${APPServer} in Tomcat ${webappsName} backup completed - RC=${_RC}";
          #
          # Backup from Database applications
          echo -e "INFO: ${APPServer} in database workflow backup intiated...";
          sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "cd ${BACKUP_PATH}; sudo pg_dump --dbname=postgresql://${PDB_SALT_WFW}@${APPServer}.${SUF_SERVER}:${PDB_PORT}/workflow > ${APPServer}_workflow.sql" &> /dev/null; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            echo -e "INFO: ${APPServer} in database workflow backup completed - RC=${_RC}";
            echo -e "INFO: ${APPServer} in database workflow backup intiated...";
            sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "cd ${BACKUP_PATH}; sudo pg_dump --dbname=postgresql://${PDB_SALT_ACT}@${APPServer}.${SUF_SERVER}:${PDB_PORT}/activiti > ${APPServer}_activiti.sql" &> /dev/null; _RC=$?;
            if [ "${_RC}" -eq 0 ]; then
              echo -e "INFO: ${APPServer} in database activiti backup completed - RC=${_RC}";
            else
              _RC="${EC}";
              echo -e "ERROR: ${APPServer} in database activiti backup failed to complete - RC=${_RC}";
              break;
            fi
            #
            # Backup from mtp scripts applications
            echo -e "INFO: ${APPServer} MTP Scripts backup intiated...";
            sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "cd ${MTP_PATH%/*}; sudo tar czvf ${BACKUP_PATH}/${APPServer}_mtp.tgz mtp/" &> /dev/null; _RC=$?;
            if [ "${_RC}" -eq 0 ]; then
              echo -e "INFO: ${APPServer} in MTP Script backup completed - RC=${_RC}";
              for ZLXServer in "${ZLXSERVER[@]}"
              do
                echo -e "INFO: ${ZLXServer} in MTP Script backup initiated...";
                ZLINUX="sshpass -p ${APP_SRV_PASS} scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 ${APP_SRV_USER}@${ZLXServer}.${SUF_SERVER}:${MTP_PATH} mtp"
                sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "cd ${BACKUP_PATH}; ${ZLINUX}" &> /dev/null; _RC=$?;
                if [ "${_RC}" -eq 0 ]; then
                  sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "cd ${BACKUP_PATH}; sudo tar czvf ${ZLXServer}_mtp.tgz mtp/; sudo rm mtp/ -rf;" &> /dev/null; _RC=$?;
                  if [ "${_RC}" -eq 0 ]; then
                    echo -e "INFO: ${ZLXServer} in MTP Script backup completed - RC=${_RC}";
                  else
                    _RC="${EC}";
                    echo -e "ERROR: ${ZLXServer} in MTP Script backup failed to complete - RC=${_RC}";
                    break;
                  fi
                else
                  _RC="${EC}";
                  echo -e "ERROR: ${APPServer} in MTP Script backup failed to complete - RC=${_RC}";
                  break;
                fi
              done
            else
              _RC="${EC}";
              echo -e "ERROR: ${APPServer} in MTP Script backup failed to complete - RC=${_RC}";
              break;
            fi
          else
            _RC="${EC}";
            echo -e "ERROR: ${APPServer} in database workflow backup failed to complete - RC=${_RC}";
            break;
          fi
        else
          _RC="${EC}";
          echo -e "ERROR: ${APPServer} in Tomcat ${webappsName} backup failed to complete - RC=${_RC}";
          break;
        fi
      done
      sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${APPServer}.${SUF_SERVER}" "sudo chown $APP_SRV_USER ${BCK_PATH};ls -dt ${BCK_PATH}/backup_* | tail -n +6|xargs rm -rf;" &> /dev/null;
    fi
    echo -e "INFO: BACKUP PROCESS COMPLETED for ${APPServer}.";
    echo -e "INFO: ======================================================================";
  done
  unset IFS;
  return "${_RC}";
}
#-----------------------------------------------------------------------------#
# deploy TOS Service which running in Windows Servers
function deployTOS {
  local _RC=0;
  local _RC=0;
  local TOSSERVER;
  local APPSERVER;
  local APPServer;
  local i;
  IFS=',' read -r -a TOSSERVER <<< "$TOS_SERVER"
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for((i=0; i<${#TOSSERVER[@]}; i++))
  do
    if [ -d "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" ]; then
      #shellcheck disable=2164
      pushd "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" &> /dev/null
        echo -e "INFO: ${TOSSERVER[$i]} TOS Deploy in progress - RC=${_RC}";
        #Check FTP Service check
        if [[ "$(net rpc service status FTPSVC -I "${TOSSERVER[$i]}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
          echo -e "INFO: FTP Service running...";
        else
          echo -e "INFO: FTP Serice start initiated...";
          net rpc service start FTPSVC -I "${TOSSERVER[$i]}" -U "${TOS_SALT01}" | grep service &> /dev/null
          sleep 5;
        fi
        echo -e "INFO: ${TOSSERVER[$i]} TOS Deploy initiated...";
        curl -q -T TOSAutomation.jar ftp://"${TOSSERVER[$i]}" --user "${TOS_SALT02}" &> /dev/null; _RC=$?;
        if [ "${_RC}" -eq 0 ]; then
          curl -q -T "${APPSERVER[$i]}/app.properties" ftp://"${TOSSERVER[$i]}" --user "${TOS_SALT02}" &> /dev/null; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            echo -e "INFO: ${TOSSERVER[$i]} TOS Deploy in success- RC=${_RC}";
          fi
        fi
        if [ "${_RC}" -ne 0 ]; then
          _RC="${EC}"
          echo -e "ERROR: ${TOSSERVER[i]} TOS deploy failed to stop - RC=${_RC}";
          break;
        fi
      #shellcheck disable=2164
      popd
    else
      _RC="${EC}";
      echo -e "ERROR: Release dir not found RC=${_RC}";
      break;
    fi
  done
  unset IFS;
  return "${_RC}"
}
#
#-----------------------------------------------------------------------------#
# Deploy Single Tomcat Service which running in Linux Servers
function deploySingleTomcat {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for APPServer in "${APPSERVER[@]}"
  do
    reconsile_Flag='N'
    if [ -d "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" ]; then
      echo "INFO: ${APPServer} Tomcat application deployment clean-up...";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo chown $APP_SRV_USER ${APP_PATH}/*.war ${APP_PATH}/${GBL_DATA}/*.properties ${APP_PATH}/${GBL_LIBS}/tp-git*;" &> /dev/null; _RC=$?;
      if [ "${_RC}" -eq 0 ]; then
        sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo rm -rf ${APP_PATH}/WorkFlow ${APP_PATH}/WorkFlowAPI ${APP_PATH}/JGitAPI" &> /dev/null; _RC=$?;
        if [ "${_RC}" -eq 0 ]; then
          echo "INFO: ${APPServer} Tomcat application deployment...";
          sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}"/*.properties "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${APP_PATH}/${GBL_DATA}; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}"/tp-gitblit.jar "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${APP_PATH}/${GBL_LIBS}; _RC=$?;
            if [ "${_RC}" -eq 0 ]; then
              sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}"/*.war "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${APP_PATH}; _RC=$?;
              if [ "${_RC}" -eq 0 ]; then
                sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}/${APPServer}.${SUF_SERVER}/app.properties" "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${MTP_PATH}; _RC=$?;
                if [ "${_RC}" -eq 0 ] && [ "${reconsile_Flag}" == 'N' ]; then
                  sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}/RepoReconsile.jar" "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${MTP_PATH}; _RC=$?;
                  if [ "${_RC}" -eq 0 ]; then
                    reconsile_Flag='Y';
                    echo "INFO: ${APPServer} Tomcat application deployment succcess RC=${_RC}";
                  else
                    _RC="${EC}";
                    echo -e "ERROR: ${APPServer} Tomcat application deployment failed RC=${_RC}";
                    break;
                  fi
                else
                  echo "INFO: ${APPServer} Tomcat application deployment succcess RC=${_RC}";
                fi
              fi
            else
              _RC="${EC}";
              echo -e "ERROR: ${APPServer} Gitblit library deployment failed RC=${_RC}";
              break;
            fi
          else
            _RC="${EC}";
            echo -e "ERROR: ${APPServer} Gitblit properties deployment failed RC=${_RC}";
            break;
          fi
        else
          _RC="${EC}";
          echo -e "ERROR: ${APPServer} Application undeploy failed RC=${_RC}";
          break;
        fi
        sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo chown tomcat ${APP_PATH}/${GBL_DATA}/*.properties ${APP_PATH}/${GBL_LIBS}/tp-git*;" &> /dev/null; _RC=$?;
        if [ "${_RC}" -ne 0 ]; then
          _RC="${EC}";
          echo -e "ERROR: ${APPServer} Gitblit permission reset failed RC=${_RC}";
          break;
        fi
      else
        _RC="${EC}";
        echo -e "ERROR: ${APPServer} Application permission set failed RC=${_RC}";
        break;
      fi
    else
      _RC="${EC}";
      echo -e "ERROR: Release dir not found RC=${_RC}";
      break;
    fi
  done
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Deploy Dual Tomcat Service which running in Linux Servers
function deployDualTomcat {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for APPServer in "${APPSERVER[@]}"
  do
    reconsile_Flag='N'
    if [ -d "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" ]; then
      echo "INFO: ${APPServer} Tomcat application deployment clean-up...";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo touch ${APP_PATH}/${GBL_LIBS}/tp-gitblit.jar; sudo chown $APP_SRV_USER ${APP_PATH}/*.war ${AP2_PATH}/*.war ${APP_PATH}/${GBL_DATA}/*.properties ${APP_PATH}/${GBL_LIBS}/tp-git*;" &> /dev/null; _RC=$?;
      if [ "${_RC}" -eq 0 ]; then
        sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo rm -rf ${AP2_PATH}/WorkFlow ${AP2_PATH}/WorkFlowAPI ${APP_PATH}/JGitAPI" &> /dev/null; _RC=$?;
        if [ "${_RC}" -eq 0 ]; then
          echo "INFO: ${APPServer} Tomcat application deployment...";
          sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}"/*.properties "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${APP_PATH}/${GBL_DATA}; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}"/tp-gitblit.jar "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${APP_PATH}/${GBL_LIBS}; _RC=$?;
            if [ "${_RC}" -eq 0 ]; then
              #shellcheck disable=SC1083
              sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}"/*WorkFlow*.war "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${AP2_PATH}; _RC=$?;
              if [ "${_RC}" -eq 0 ]; then
                sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}"/*JGitAPI.war "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${APP_PATH}; _RC=$?;
                if [ "${_RC}" -eq 0 ]; then
                  sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}/${APPServer}.${SUF_SERVER}/app.properties" "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${MTP_PATH}; _RC=$?;
                  if [ "${_RC}" -eq 0 ] && [ "${reconsile_Flag}" == 'N' ]; then
                    sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}/RepoReconsile.jar" "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${MTP_PATH}; _RC=$?;
                    if [ "${_RC}" -eq 0 ]; then
                      reconsile_Flag='Y';
                      echo "INFO: ${APPServer} Tomcat application deployment succcess RC=${_RC}";
                    else
                      _RC="${EC}";
                      echo -e "ERROR: ${APPServer} Tomcat application deployment failed RC=${_RC}";
                      break;
                    fi
                  else
                    echo "INFO: ${APPServer} Tomcat application deployment succcess RC=${_RC}";
                  fi
                else
                  _RC="${EC}";
                  echo -e "ERROR: ${APPServer} JGitAPI deploy failed RC=${_RC}";
                  break;
                fi
              else
                _RC="${EC}";
                echo -e "ERROR: ${APPServer} Workflow deploy failed RC=${_RC}";
                break;
              fi
            else
              _RC="${EC}";
              echo -e "ERROR: ${APPServer} Gitblit deploy failed RC=${_RC}";
              break;
            fi
          else
            _RC="${EC}";
            echo -e "ERROR: ${APPServer} Gitblit deploy failed RC=${_RC}";
            break;
          fi
        else
          _RC="${EC}";
          echo -e "ERROR: ${APPServer} Application undeploy failed RC=${_RC}";
          break;
        fi
      else
        _RC="${EC}";
        echo -e "ERROR: ${APPServer} Application permission set failed RC=${_RC}";
        break;
      fi
    else
      _RC="${EC}";
      echo -e "ERROR: Release dir not found RC=${_RC}";
      break;
    fi
  done
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Deploy ShellSCripts Service which running in Linux Servers
function deployLINUX {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local _APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for APPServer in "${APPSERVER[@]}"
  do
    _APPServer="${APPServer}.${SUF_SERVER}";
    if [ -d "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" ]; then
      echo "INFO: ${APPServer} Scripts deployment in-progress...";
      #shellcheck disable=SC2164
      pushd "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" &> /dev/null
        unzip -o ShellScripts.jar -d ShellScripts  -x "META-INF*" ".git*" "test*" "Jenkinsfile" "build.sh";
        chmod +rx ShellScripts -R
        if [ -d ShellScripts ]; then
          #Replace config with F5-DNS Address
          sed -i "s/vhldvztdt001.tvlport.net/${_APPServer}/g" ShellScripts/\.mtpconfig
          #Replace API Port except DEV Servers
          sed -i 's/PRD_BIN_PORT="8443"/PRD_BIN_PORT="8446"/g' ShellScripts/\.mtpconfig
          sed -i "s/vhldvztdt001,/${APP_SERVER}/g" ShellScripts/\.mtpconfig
          if [ "${tomcattype}" == "dual" ]; then
            sed -i 's/PRD_API_PORT="${PRD_BIN_PORT}"/PRD_API_PORT="9446"/g' ShellScripts/\.mtpconfig
          fi
          #Replace Server name for REXX Python
          sed -i "s/vhldvztdt001.tvlport.net/${APPSERVER[$i]}.tvlport.net/g" ShellScripts/mtpzrxretrive
          #Set permission and Transfer files
          sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${_APPServer}" "sudo chown ${APP_SRV_USER} ${MTP_PATH} -R" &> /dev/null; _RC=$?;
          sshpass -p "${APP_SRV_PASS}" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 ShellScripts/. "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}":${MTP_PATH}; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            echo -e "INFO: ${APPServer} Scripts deployment completed.";
          else
            _RC="${EC}";
            echo -e "ERROR: Scripts deployment to Application Linux Server failed RC=${_RC}";
            break;
          fi
        fi
        rm -rf ShellScripts/
      #shellcheck disable=SC2164
      popd &> /dev/null;
    else
      _RC="${EC}";
      echo -e "ERROR: Release dir not found RC=${_RC}";
      break;
    fi
  done
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Deploy ShellScript Service which running in zLinux Servers
function deployZLINUX {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local ZLXSERVER;
  local ZLXServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  local ZLX_SERVER="${ZLX_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a ZLXSERVER <<< "$ZLX_SERVER";
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER";
  if [ -d "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" ]; then
    #shellcheck disable=SC2164
    pushd "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" &> /dev/null
      unzip -o ShellScripts.jar -d ShellScripts  -x "META-INF*" ".git*" "test*" "Jenkinsfile" "build.sh" &> /dev/null;
      chmod +rx ShellScripts -R
      if [ -d ShellScripts ]; then
        #Replace config with F5-DNS Address
        sed -i "s/vhldvztdt001.tvlport.net/${GIT_SERVER}/g" ShellScripts/\.mtpconfig
        sed -i "s/vhldvztdt001,/${APP_SERVER}/g" ShellScripts/\.mtpconfig
        if [ "${tomcattype}" == "dual" ]; then
          sed -i 's/PRD_API_PORT="${PRD_BIN_PORT}"/PRD_API_PORT="9443"/g' ShellScripts/\.mtpconfig
        fi
        #Replace Server name for REXX Python
        sed -i "s/vhldvztdt001.tvlport.net/${APPSERVER[0]}.tvlport.net/g" ShellScripts/mtpzrxretrive
        for ZLXServer in "${ZLXSERVER[@]}"
        do
          echo "INFO: ${ZLXServer} Scripts deployment in-progress...";
          #Set zVM Configurations based on servers
          if [[ "${ZLXServer}" =~ 01 ]]; then
            sed -i "s/wspvm4.worldspan.com/wspdl2.worldspan.com/g" ShellScripts/\.mtpconfig
          elif [[ "${ZLXServer}" =~ 03 ]]; then
            sed -i "s/wspvm4.worldspan.com/gvm.galileo.com/g" ShellScripts/\.mtpconfig
          fi
          #Set DevCtr mail address in production servers
          if [[ "${ZLXServer,,}" =~ pn ]]; then
            sed -i "s/Arul.Dhandapani@travelport.com/EA.VM.DevCtr@travelport.com/g" ShellScripts/\.mtpconfig
          fi
          #Set production version of new macro update utility specific to Delta plans
          #sed -i "s/UTILJCLZ/UTILJCLM/g" ShellScripts/\.mtpconfig
          #Set permission and Transfer files
          sshpass -p "${APP_SRV_PASS}" ssh -p 22 -o StrictHostKeyChecking=no -l "${APP_SRV_USER}" "${ZLXServer}.${SUF_SERVER}" "sudo chown ${APP_SRV_USER} ${MTP_PATH} -R" &> /dev/null; _RC=$?;
          sshpass -p "${APP_SRV_PASS}" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 ShellScripts/. "${APP_SRV_USER}@${ZLXServer}.${SUF_SERVER}":${MTP_PATH} &> /dev/null; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            echo "INFO: ${ZLXServer} Scripts deployment completed RC=${_RC}";
          else
            _RC="${EC}";
            echo -e "ERROR: ${ZLXServer} Scripts deployment failed RC=${_RC}";
            break;
          fi
          #Re-Set zVM Configurations based on servers
          if [[ "${ZLXServer}" =~ 01 ]]; then
            sed -i "s/wspdl2.worldspan.com/wspvm4.worldspan.com/g" ShellScripts/\.mtpconfig
          elif [[ "${ZLXServer}" =~ 03 ]]; then
            sed -i "s/gvm.galileo.com/wspvm4.worldspan.com/g" ShellScripts/\.mtpconfig
          fi
        done
      else
        _RC="${EC}";
        echo -e "ERROR: Scripts extraction failed RC=${_RC}";
      fi
      rm -rf ShellScripts/
    #shellcheck disable=SC2164
    popd &> /dev/null;
  else
    _RC="${EC}";
    echo -e "ERROR: Release dir not found RC=${_RC}";
  fi
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Start TOS Service which running in Windows Servers (TPF OPERATION SERVER QUE)
function startTOS {
  local _RC=0;
  local TOSSERVER;
  local TOSServer;
  IFS=',' read -r -a TOSSERVER <<< "$TOS_SERVER"
  for TOSServer in "${TOSSERVER[@]}"
  do
    # ActiveMQ Service status
    if [[ "$(net rpc service status ActiveMQ -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
      echo -e "INFO: ActiveMQ Service already running..";
    elif [[ "$(net rpc service status ActiveMQ -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ pending ]]; then
      echo -e "INFO: ActiveMQ Service re-start initiated...";
      net rpc service restart ActiveMQ -I "${TOSServer}" -U "${TOS_SALT01}" | grep service
      sleep 8;
      if [[ "$(net rpc service status ActiveMQ -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
        echo -e "INFO: ActiveMQ Service started RC=${_RC}";
      else
        _RC="${EC}";
        echo -e "ERROR: ${TOSServer} ActiveMQ Service failed to start - RC=${_RC}";
        break;
      fi
    elif [[ "$(net rpc service status ActiveMQ -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ stopped ]]; then
      echo -e "INFO: ActiveMQ Service start initiated...";
      net rpc service start ActiveMQ -I "${TOSServer}" -U "${TOS_SALT01}" | grep service
      sleep 8;
      if [[ "$(net rpc service status ActiveMQ -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
        echo -e "INFO: ActiveMQ Service started RC=${_RC}";
      else
        _RC="${EC}";
        echo -e "ERROR: ${TOSServer} ActiveMQ Service failed to start - RC=${_RC}";
        break;
      fi
    else
      echo -e "INFO: ActiveMQ Service running...";
    fi
    # TOS Service status
    if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
      net rpc service restart WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service
      sleep 5;
      echo -e "INFO: ${TOSServer} TOS Service re-start initiated.";
      # Force to start TOS Service
      if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ pending ]]; then
        echo -e "INFO: ${TOSServer} TOS Service pending to start.";
        sleep 10;
      fi
      if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
        echo -e "INFO: ${TOSServer} TOS Service started - RC=${_RC}";
      else
        _RC="${EC}";
        echo -e "ERROR: ${TOSServer} TOS Service failed to start - RC=${_RC}";
        break;
      fi
    elif [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ stopped ]] || \
         [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ pending ]]; then
      net rpc service start WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service
      sleep 5;
      echo -e "INFO: ${TOSServer} TOS Service start initiated.";
      # Force to start TOS Service
      if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ pending ]]; then
        echo -e "INFO: ${TOSServer} TOS Service pending to start.";
        sleep 10;
      fi
      if [[ "$(net rpc service status WFTOSClient -I "${TOSServer}" -U "${TOS_SALT01}" | grep service)" =~ running ]]; then
        echo -e "INFO: ${TOSServer} TOS Service started - RC=${_RC}";
      else
        _RC="${EC}";
        echo -e "ERROR: ${TOSServer} TOS Service failed to start - RC=${_RC}";
        break;
      fi
    fi
  done
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Start Tomcat Service which running in Linux Servers (APPLICATION SERVERS)
function startTomcat {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for APPServer in "${APPSERVER[@]}"
  do
    if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ running ]]; then
      echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service already running - restart initiated.";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl restart tomcat"; _RC=$?;
      if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
        echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service waiting to bring-up all the applications...";
        sleep 3; #sleep 120;
        #Check JGitAPI application status to run or not
        until curl -s -k -X GET -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic ${TCS_SALT01}" "${TCS_CONN}://${APPServer}.${SUF_SERVER}:${TCS_PORT}/manager/text/list" | grep 'JGitAPI:running'
        do
          echo -e "INFO: JGitAPI application still loading..."
          sleep 1;
        done
        echo -e "INFO: JGit application started."
        #Check Gitblit application to start
        curl -s -k -X GET -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic ${TCS_SALT01}" "${TCS_CONN}://${APPServer}.${SUF_SERVER}:${TCS_PORT}/manager/text/start?path=/gitblit"; _RC=$?;
        if [ "${_RC}" -eq 0 ]; then
          until curl -s -k -X GET -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic ${TCS_SALT01}" "${TCS_CONN}://${APPServer}.${SUF_SERVER}:${TCS_PORT}/manager/text/list" | grep 'gitblit:running'
          do
            echo -e "INFO: Gitblit application still loading..."
            sleep 5;
          done
        fi
        echo -e "INFO: Gitblit application started."
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ running ]]; then
          echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service re-started - RC=${_RC}";
        elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ dead ]]; then
          _RC="${EC}";
          echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service re-started failed - RC=${_RC}";
          break;
        fi
      else
        echo -e "ERROR: ${APPServer}.${SUF_SERVER} Tomcat Service failed to restart - RC=${_RC}";
        _RC="${EC}";
        break;
      fi
    elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ dead ]]; then
      echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service start initiated.";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl start tomcat"; _RC=$?;
      if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
        echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service waiting to bring-up all the applications...";
        sleep 5; #sleep 120;
        #Check JGitAPI application status to run or not
        until curl -s -k -X GET -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic ${TCS_SALT01}" "${TCS_CONN}://${APPServer}.${SUF_SERVER}:${TCS_PORT}/manager/text/list" | grep 'JGitAPI:running'
        do
          echo -e "INFO: JGitAPI application still loading..."
          sleep 1;
        done
        echo -e "INFO: JGit application started."
        #Check Gitblit application to start
        curl -s -k -X GET -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic ${TCS_SALT01}" "${TCS_CONN}://${APPServer}.${SUF_SERVER}:${TCS_PORT}/manager/text/start?path=/gitblit"; _RC=$?;
        if [ "${_RC}" -eq 0 ]; then
          until curl -s -k -X GET -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic ${TCS_SALT01}" "${TCS_CONN}://${APPServer}.${SUF_SERVER}:${TCS_PORT}/manager/text/list" | grep 'gitblit:running'
          do
            echo -e "INFO: Gitblit application still loading..."
            sleep 5;
          done
        fi
        echo -e "INFO: Gitblit application started."
        if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ running ]]; then
          echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service started - RC=${_RC}";
        elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat -l" | grep Active:)" =~ dead ]]; then
          _RC="${EC}";
          echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat Service re-started failed - RC=${_RC}";
          break;
        fi
      else
        echo -e "ERROR: ${APPServer}.${SUF_SERVER} Tomcat Service failed to start - RC=${_RC}";
        _RC="${EC}";
        break;
      fi
    fi
    #Start tomcat2 if it's dual tomcat server request
    if [ "${tomcattype}" == "dual" ]; then
      if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ running ]]; then
        echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service already running - restart initiated.";
        sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl restart tomcat2"; _RC=$?;
        if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
          echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service waiting to bring-up all the applications...";
          sleep 15; #sleep 120;
          if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ running ]]; then
           echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service re-started - RC=${_RC}";
          elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ dead ]]; then
            _RC="${EC}";
            echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service re-started failed - RC=${_RC}";
            break;
          fi
        else
          echo -e "ERROR: ${APPServer}.${SUF_SERVER} Tomcat-2 Service failed to restart - RC=${_RC}";
          _RC="${EC}";
          break;
        fi
      elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ dead ]]; then
        echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service start initiated.";
        sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl start tomcat2"; _RC=$?;
        if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
          echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service waiting to bring-up all the applications...";
          sleep 15; #sleep 120;
          if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ running ]]; then
            echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service started - RC=${_RC}";
          elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ dead ]]; then
            _RC="${EC}";
            echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service re-started failed - RC=${_RC}";
            break;
          fi
        else
          echo -e "ERROR: ${APPServer}.${SUF_SERVER} Tomcat-2 Service failed to start - RC=${_RC}";
          _RC="${EC}";
          break;
        fi
      elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ failed ]]; then
        echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service start initiated.";
        sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl start tomcat2"; _RC=$?;
        if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
          echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service waiting to bring-up all the applications...";
          sleep 15; #sleep 120;
          if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ running ]]; then
            echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service started - RC=${_RC}";
          elif [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ dead ]]; then
            _RC="${EC}";
            echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service re-started failed - RC=${_RC}";
            break;
          fi
        else
          echo -e "ERROR: ${APPServer}.${SUF_SERVER} Tomcat-2 Service failed to start - RC=${_RC}";
          _RC="${EC}";
          break;
        fi
      fi
    fi
  done
  unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Generate configurations files for Linux Application Servers
function generateConfig {
  local _RC=0;
  if [ -d "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" ]; then
    #shellcheck disable=2164
    pushd "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}" &> /dev/null
      echo "INFO: Application configuration generate...";
      if [ -f ConfigGenerator.jar ]; then
        java -jar -Ddb.app.username="${ADB_WFW_USER}" -Ddb.app.password="${ADB_WFW_PASS}" \
                  -Ddb.ticket.username="${XDB_TIK_USER}" -Ddb.ticket.password="${XDB_TIK_PASS}" \
                  -Ddb.csr.username="${XDB_CSR_USER}" -Ddb.csr.password="${XDB_CSR_PASS}" \
                  -Ddb.vpar.username="${XDB_VPR_USER}" -Ddb.vpar.password="${XDB_VPR_PASS}" \
                  -Dservice.username="${MTP_SRV_USER}" -Dservice.password="${MTP_SRV_PASS}" \
                  -Dtos.file.domain="${MTP_DOMAIN}" \
                  -Dtos.file.username="${TOS_SRV_USER}" -Dtos.file.password="${TOS_SRV_PASS}" \
                  ConfigGenerator.jar -buildId "${BUILDID}" -company "${COMPANY}" -environment "${ENV}" -linux "${APP_SERVER}" -tos "${TOS_SERVER}" -encrypted "${encrypted}" -tomcattype "${tomcattype}"
        _RC=$?
        if [ "${_RC}" -eq 0 ]; then
          echo "INFO: Application configuration generate succcess RC=${_RC}";
        fi
        if [ "${_RC}" -ne 0 ]; then
          _RC="${EC}";
          echo -e "ERROR: Application configuration generate failed RC=${_RC}";
          #break;
        fi
      else
        _RC="${EC}";
        echo -e "ERROR: Config Generator application not found RC=${_RC}";
        #break;
      fi
    #shellcheck disable=2164
    popd &> /dev/null
  else
    _RC="${EC}";
    echo -e "ERROR: Release dir not found RC=${_RC}";
    #break;
  fi
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Main execution
echo -e "INFO: StartTime - $(date +%Y%m%d%H%M%S)";
minInputValidation "${_MiIP}" "${_ReIP}"; RC=$?;
#
if [ "${RC}" -eq 0 ]; then
  #Installation Order by Task, this sequance shouldn't change for this application
  if [ "${tomcattype}" == 'single' ]; then
    taskOrder=("generateConfig" "stopTOS" "stopTomcat" "backup" "deployTOS" "deploySingleTomcat" "deployLINUX" "deployZLINUX" "startTOS" "startTomcat");
  elif [ "${tomcattype}" == 'dual' ]; then
    taskOrder=("generateConfig" "stopTOS" "stopTomcat2" "backup" "deployTOS" "deployDualTomcat" "deployLINUX" "deployZLINUX" "startTOS" "startTomcat");
  else
    echo -e "ERROR: Invaild input for tomcat environment"
    RC="${EC}"
  fi
  if [ "${RC}" -eq 0 ]; then
    for taskName in "${taskOrder[@]}"
    do
      $taskName; RC=$?;                #Calling task by order
      if [ "$RC" -ne 0 ]; then
        break;                         #Intruppt if any error occured
      fi
    done
  fi
fi
echo -e "INFO: EndTime - $(date +%Y%m%d%H%M%S)";
#-----------------------------------------------------------------------------#
exit "$RC";
#-----------------------------------------------------------------------------#
# vim: filetype=bash
