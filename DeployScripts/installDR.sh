#!/bin/bash
#*****************************************************************************#
#   SCRIPT NAME: installDR.sh                                                 #
#                                                                             #
#   DESCRIPTION:                                                              #
#      Script to start up DR webservers and DR zlinux server                  #
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
#   04032019    VINOTH  Created the script                                    #
#   05012019    ARUL    Updated with tomcat manager curl fix                  #
#*****************************************************************************#
#*****************************************************************************#
declare -i _ReIP="$#";                 #Received number of input parms
declare -i _MiIP="23";                 #Expected number of input parms
#
ENV="${1^^}";                          #Input Deployment env (DEV,QA,PROD)
COMPANY="${2^^}";                      #Input Company name (TP, DL)
APP_SERVER="${3,,}";                   #Input Tomcat Application server
DR_SERVER="${4}";                      #Input DR server IP address to validate.
DL_PRDSERV="${5,,}";                   #Input Git server URL or Load balancer DNS
DL_ZLINUX="${6,,}";                    #Input DR server Input hostname
DL_IP_ZLINUX="${7}";                   #Input Dr server IP address
DLDNSADDR="${8,,}";                    #Input DNS address
APP_SRV_USER="${9}";                   #Input App deployment sudo user name
APP_SRV_PASS="${10}";                  #Input App deployment sudo user salt
TOS_SRV_USER="${11}";                  #Input Tos deployment user name
TOS_SRV_PASS="${12//\'/}";             #Input Tos deployment user salt - Replace quote
ADB_WFW_USER="${13}";                  #Workflow application database user name
ADB_WFW_PASS="${14}";                  #Workflow application database user salt
MTP_SRV_USER="${15}";                  #Input App Service user name
MTP_SRV_PASS="${16}";                  #Input App Service user salt
ADB_ACT_USER="${17}";                  #Activity application database user name
ADB_ACT_PASS="${18}";                  #Activity application database user salt
SRV_SWITCH="${19}";                    #start or stop server.
PGS_ZTPF_USER="${20}";                 #postgres user name
PGS_ZTPF_PASS="${21}";                 #postgres passwd
TCS_MGR_USER="${22}"                   #Tomcat Server manager access user name
TCS_MGR_PASS="${23}"                   #Tomcat Server manager access user salt
#
TCS_SALT01="$(echo -en ${TCS_MGR_USER}:${TCS_MGR_PASS}|base64)"; #Format-2 Salt
TCS_CONN="https";                      #Tomcat Server manager port connector
TCS_PORT="8446";                       #Tomcat Server manager port number
APP="WF";                              #Application short name
SUF_SERVER="tvlport.net";              #Servers domain suffix
MTP_DOMAIN="galileo";                  #Network domain name(Windows NT)
TOS_SALT01="${MTP_DOMAIN}/${TOS_SRV_USER}%${TOS_SRV_PASS}"; #Format-1 Salt
TOS_SALT02="${TOS_SRV_USER}:${TOS_SRV_PASS}"; #Format-2 Salt
REL_PATH="/opt/delivery/release";      #Release path for the deployables
APP_PATH="/var/lib/tomcat/webapps";    #Application deployment path
APP_PATH2="/opt/tomcat2/webapps";      #Application deployment path 2 tomcat
MTP_PATH="/opt/mtp";                   #Support scripts deployment path
BCK_PATH="/ztpfrepos/backup";          #Application Backup path
PDB_PORT="5432";                       #PostgrSQL database ports
PDB_SALT_WFW="${ADB_WFW_USER}:${ADB_WFW_PASS}"; #PostgreSQL WorkFlow db salt
PDB_SALT_ACT="${ADB_ACT_USER}:${ADB_ACT_PASS}"; #PostgreSQL activity db salt
RC=0;                                  #Success return code
EC=8;                                  #Failed return code
https_proxy="";                        #Unset existing Proxy setup if any
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
# Check Permission for /opt/postgresql and /opt/mtpserver
function permissionChk {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  for APPServer in "${APPSERVER[@]}"
  do
    sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo touch /opt/PostgreSQL/test"; _RC=$?;
    if [ "${_RC}" -ne 0 ]; then
      echo -e "ERROR: Read-only file system - touch: cannot /opt/PostgreSQL/test please verify"
      break
    else
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo rm -f /opt/PostgreSQL/test"
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo touch /opt/mtpserver/test"; _RC=$?;
      if [ "${_RC}" -ne 0 ]; then
        echo -e "ERROR: Read-only file system - touch: cannot /opt/mtpserver/test please verify"
        break
      else
        sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo rm -f /opt/mtpserver/test"
      fi
    fi
  done
return "${_RC}";
}
#-----------------------------------------------------------------------------#
function updateLnxHosts {
  local _RC=0;
  local APPSERVER;
  local APPServer;
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  local DR_SERVERIP1="${DR_SERVER%%,*}";
  local DR_SERVERIP2="${DR_SERVER##*,}";
  local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
  local APP_SVRHST1="${APP_SERVER%%,*}";
  local APP_SVRHST2="${APP_SERVER##*,}";
  local DL_PRDSERV="${DL_PRDSERV//.tvlport.net/}" #Override with FQDN input
  local DL_PRDSERV1="${DL_PRDSERV%%,*}";
  local DL_PRDSERV2="${DL_PRDSERV##*,}";
  local DLZLNX="${DL_ZLINUX//.tvlport.net/}";     #Override with FQDN input
  local DLZLNXIP="${DL_IP_ZLINUX}";
  local DLDNSADDR="${DLDNSADDR}";
  #Verify Host to check valid ip address.
  nslookup "${DR_SERVERIP1}" | grep -i "${APP_SVRHST1}"; _RC=$?;
  if [ "${_RC}" -eq 0 ]; then
    nslookup "${DR_SERVERIP2}" | grep -i "${APP_SVRHST2}"; _RC=$?;
    if [ "${_RC}" -eq 0 ]; then
      sudo rm -f /tmp/hosts_test
      IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
      echo -e "127.0.0.1\tlocalhost localhost.localdomain localhost4 localhost4.localdomain4\n::1\tlocalhost \
      localhost.localdomain localhost6 localhost6.localdomain6\n#\n# Delta DR Setup - IP Address mapping\
      \n${DR_SERVERIP1}\t${APP_SVRHST1}.tvlport.net ${APP_SVRHST1}\n${DR_SERVERIP1}\t${DL_PRDSERV1}.tvlport.net ${DL_PRDSERV1}\
      \n${DR_SERVERIP2}\t${APP_SVRHST2}.tvlport.net ${APP_SVRHST2}\n${DR_SERVERIP2}\t${DL_PRDSERV2}.tvlport.net ${DL_PRDSERV2}\
      \n${DLZLNXIP}\t${DLZLNX}.tvlport.net ${DLZLNX}\n${DR_SERVERIP1}\t${DLDNSADDR}\
      \n${DR_SERVERIP2}\t${DLDNSADDR}" > /tmp/hosts
      for APPServer in "${APPSERVER[@]}"
      do
        sshpass -p "$APP_SRV_PASS" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "/tmp/hosts" "${APP_SRV_USER}@${APPServer}.${SUF_SERVER}:/tmp/hosts" ;_RC=$?;
        if [ "${_RC}" -ne 0 ]; then
          echo -e "ERROR: File SCP failed to ${APPServer}.${SUF_SERVER}"
          _RC=${EC};
        else
          sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo mv /tmp/hosts /etc/hosts_test; sudo chown root:root /etc/hosts_test;" &> /dev/null; _RC=$?;
        fi
      done
    else
      echo -e "ERROR:Invalid IP and Hostname unable to match ${DR_SERVERIP2} & ${APP_SVRHST2}. Please check jenkins to give correct order."
      _RC=${EC};
    fi
  else
    echo -e "ERROR:Invalid IP and Hostname unable to match ${DR_SERVERIP1} & ${APP_SVRHST1}. Please check jenkins to give correct order."
    _RC=${EC};
  fi
  unset IFS;
  return "${_RC}";
  }
#-----------------------------------------------------------------------------#
#verfiy and upload host file zlinux (ZLINUX)
function updateZlinuxHost {
  local _RC=0;
  if [ -f "/tmp/hosts" ]; then
    sshpass -p "$(echo "${MTP_SRV_PASS}"|base64 --decode)" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "/tmp/hosts" "${MTP_SRV_USER}@${DL_IP_ZLINUX}:/tmp/hosts"; _RC=$?;
    #sshpass -p "${MTP_SRV_PASS}" scp -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -r -P 22 "/tmp/hosts" "${MTP_SRV_USER}@${DL_IP_ZLINUX}:/tmp/hosts"; _RC=$?;
    if [ "${_RC}" -ne 0 ]; then
      echo -e "ERROR: File SCP failed to ${zlinuxIPDR}"
      _RC=${EC};
    else
      sshpass -p "$(echo "${MTP_SRV_PASS}"|base64 --decode)" ssh -p 22 -o StrictHostKeyChecking=no -l "${MTP_SRV_USER}" "${DL_IP_ZLINUX}" "sudo mv /tmp/hosts /etc/hosts_test;sudo chown root:root /etc/hosts_test" &> /dev/null; _RC=$?;
    fi
    sudo rm /tmp/hosts_test;
  fi
 return "${_RC}";
}
#-----------------------------------------------------------------------------#
# Workflow and Activiti DB restore in Linux Server
function dbDoRestore {
   local _RC=0;
   local APPSERVER;
   local APPServer;
   local APP_SERVER="${APP_SERVER//.tvlport.net/}" #Override with FQDN input
   BackupLoc="/opt/PostgreSQL/PN_backups/logical_backups/";
   IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
   for APPServer in "${APPSERVER[@]}"
   do
    _sshresponse="$(sshpass -p "$APP_SRV_PASS" ssh -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" 'etcdctl member list | grep isLeader=true')"
    _respondomain="$(echo ${_sshresponse} | cut -d ' ' -f2)"
    _trueServer="${_respondomain##*=}.tvlport.net"
    #Drop db and create DB.
    export PGPASSWORD=${PGS_ZTPF_PASS}; dropdb -h "${_trueServer}" -p "${PDB_PORT}" -U "${PGS_ZTPF_USER}" -e workflow ; _RC=$?;
    export PGPASSWORD=${PGS_ZTPF_PASS}; createdb -h "${_trueServer}" -p "${PDB_PORT}" -U "${PGS_ZTPF_USER}" -e workflow --encoding='utf-8' --locale=en_US.UTF-8 --template=template0; _FC=$?;
    if [[ "${_RC}" -eq 0 ]] && [[ ${_FC} -eq 0 ]]; then
      echo -e "INFO: ${_trueServer} database drop success - DB:workflow. and successfully created empty DB:workflow."
      export PGPASSWORD=${PGS_ZTPF_PASS}; dropdb -h "${_trueServer}" -p "${PDB_PORT}" -U "${PGS_ZTPF_USER}" -e activiti ; _RC=$?;
      export PGPASSWORD=${PGS_ZTPF_PASS}; createdb -h "${_trueServer}" -p "${PDB_PORT}" -U "${PGS_ZTPF_USER}" -e activiti --encoding='utf-8' --locale=en_US.UTF-8 --template=template0; _FC=$?;
      if [[ "${_RC}" -eq 0 ]] && [[ ${_FC} -eq 0 ]]; then
        _WFsql=$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${_trueServer}" "cd ${BackupLoc}; "ls -last| grep workflow|head -1|awk '{print$10}'" ")
        if [ ! -z "${_WFsql[0]}" ]; then
          echo -e "INFO: ${_trueServer} in database workflow restore intiated..."
          sleep 20
          sshpass -p "$APP_SRV_PASS" ssh -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${_trueServer}" "cd ${BackupLoc}; export PGPASSWORD=${PGS_ZTPF_PASS}; pg_restore -1 -v -h localhost -p ${PDB_PORT}  -U ${PGS_ZTPF_USER} -d workflow ${_WFsql}" &> /dev/null; _RC=$?;
          if [ "${_RC}" -eq 0 ]; then
            echo -e "INFO: ${_trueServer} in database workflow restore Completed - RC=${_RC}";
          else
            _RC="${EC}";
            echo -e "ERROR: ${_trueServer} in database WorkFlow restore failed to complete - RC=${_RC}";
            break
          fi
        fi
        if [ "${_RC}" -eq 0 ]; then
          _AVsql=$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${_trueServer}" "cd ${BackupLoc}; "ls -last| grep activiti|head -1|awk '{print$10}'" ")
          if [ ! -z "${_AVsql[0]}" ]; then
            echo -e "INFO: ${_trueServer} in database activity restore intiated..."
            sleep 20
            sshpass -p "$APP_SRV_PASS" ssh -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${_trueServer}" "cd ${BackupLoc}; export PGPASSWORD=${PGS_ZTPF_PASS}; pg_restore -1 -v -h localhost -p ${PDB_PORT}  -U ${PGS_ZTPF_USER} -d activiti ${_AVsql}" &> /dev/null; _RC=$?;
            if [ "${_RC}" -eq 0 ]; then
              echo -e "INFO: ${_trueServer} in database activity restore Completed - RC=${_RC}";
              break
            else
              _RC="${EC}";
              echo -e "ERROR: ${_trueServer} in database activity restore failed to complete - RC=${_RC}";
              break
            fi
          fi
        else
          echo -e "ERROR: ${_trueServer} in database WorkFlow restore failed to complete - RC=${_RC}"
          break
        fi
      else
        echo -e "ERROR: Failed drop and create database in ${_trueServer} for activiti RC=${_RC} and FC=${_FC}"
        break
      fi
    else
      echo -e "ERROR: Failed drop and create database in ${_trueServer} for workflow RC=${_RC} and FC=${_FC}"
      break
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
        sleep 3;#sleep 120;
        until curl -s -k -X GET -H "Content-Type: application/x-www-form-urlencoded" -H "Authorization: Basic ${TCS_SALT01}" "${TCS_CONN}://${APPServer}.${SUF_SERVER}:${TCS_PORT}/manager/text/list" | grep 'JGitAPI:running'
        do
          echo -e "INFO: JGitAPI application still loading..."
          sleep 1;
        done
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
    if [[ "$(sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl status tomcat2 -l" | grep Active:)" =~ running ]]; then
      echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service already running - restart initiated.";
      sshpass -p "$APP_SRV_PASS" ssh -p 22 -o StrictHostKeyChecking=no -l "$APP_SRV_USER" "${APPServer}.${SUF_SERVER}" "sudo systemctl restart tomcat2"; _RC=$?;
      if [ "${_RC}" -eq 0 ] || [ "${_RC}" -eq 3 ]; then
        echo -e "INFO: ${APPServer}.${SUF_SERVER} Tomcat-2 Service waiting to bring-up all the applications...";
        sleep 120;
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
        sleep 120;
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
        sleep 120;
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
  done
  unset IFS;
  return "${_RC}";
}
#-----------------------------------------------------------------------------#
# Main execution
echo -e "INFO: StartTime - $(date +%Y%m%d%H%M%S)";
minInputValidation "${_MiIP}" "${_ReIP}"; RC=$?;
if [ "${RC}" -eq 0 ]; then
  #Installation Order by Task, this sequance shouldn't change for this application
  if [[ "${SRV_SWITCH}" == "start" ]]; then
    #taskOrder=("permissionChk" "updateLnxHosts" "updateZlinuxHost" "dbDoRestore" "startTomcat");
    taskOrder=("permissionChk" "updateLnxHosts" "updateZlinuxHost" "dbDoRestore" "startTomcat");
  elif [[ "${SRV_SWITCH}" == "stop" ]]; then
    taskOrder=("stopTomcat2");
  fi
  for taskName in "${taskOrder[@]}"
  do
    $taskName; RC=$?;                #Calling task by order
    if [ "$RC" -ne 0 ]; then
      break;                         #Intruppt if any error occured
    fi
  done
fi
echo -e "INFO: EndTime - $(date +%Y%m%d%H%M%S)";
#-----------------------------------------------------------------------------#
exit "$RC";
#-----------------------------------------------------------------------------#
# vim: filetype=bash
