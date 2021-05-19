#!/bin/bash
# Name - activity.sh
# Application maintenance script to perform clean-up, backup and restore
#
# This will executed with super-user permission mode to handle application data
# Note: Install this script at /opt/java-tools/activity.sh path
# Note: Install this script at /opt/java-tools/activity.sh path
#
# Run: /opt/java-tools/activity.sh qa dl BACKUP
#
if [[ -z $(pgrep -f /opt/java-tools/activity.sh) ]]; then
  conDate=$(date +%Y%m%d)
  actEnv=${1,,};
  actCompany=${2,,};
  actTask=${3^^};
  gitDataPath="/opt/gitblit/data/git/tpf"
  dataStore="/opt/mtpserver/data"
  MTP_SUSR="mtpservice"
  PDB_SALT="workflow:Wfw802017"
  _Salt="$(echo bXRwc2VydmljZTpNMHNlc2lzYWcwMGRiMHkk | base64 --decode)"
  if [[ ${actTask} == dv ]]; then
    server="vhldvztdt003.tvlport.net vhldvztdt004.tvlport.net";
    zLinux="zhldvztbs001.tvlport.net zhldvztbs002.tvlport.net zhldvztbs002.tvlport.net";
  elif [[ ${actTask} == qa ]]; then
    server="vhlqaztdt003.tvlport.net vhlqaztdt004.tvlport.net";
    zlinux="zhlqaztbs001.tvlport.net zhlqaztbs002.tvlport.net zhlqaztbs003.tvlport.net";
  elif [[ ${actTask} == pp ]]; then
    if [[ ${actCompany} == tp ]]; then
      server="vhlppztdt001.tvlport.net vhlppztdt002.tvlport.net";
      zlinux="zhlppztbs002.tvlport.net zhlppztbs003.tvlport.net";
    elif if [[ ${actCompany} == dl ]]; then
      server="vhlppztdt003.tvlport.net vhlppztdt004.tvlport.net";
      zlinux="zhlppztbs001.tvlport.net";
    fi
  fi
  #-----------------------------------------------------------------------------#
  # Cleaning git data on the application server
  function gitDataTask {
    local _RC;
    local _Src="source";
    local _Drv="derived";
    local _Ibm="ibm";
    local _NonIbm="nonibm";
    local _Task="${1}"                 #Input1 - Task request
    local _serverName="${2}";          #Input2 server name
    local _gitPath="${gitDataPath}/${actCompany}";
    local _dataStore="${dataStore}/${_serverName}";
    if [[ "${_Task}" == "CLEANUP" ]]; then
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "cd ${_gitPath}/; rm -rf ${_Src} ${_Drv} ${_Ibm} ${_NonIbm};" _RC=$?;
    elif [[ "${_Task}" == "BACKUP" ]]; then
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "cd ${_gitPath}/; mkdir -p ${_dataStore}/${conDate}_Git; tar czvf ${_dataStore}/${conDate}_Git/git_nonprod.tgz source/ derived/; tar czvf ${_dataStore}/${conDate}_Git/git_prod.tgz ibm/ nonibm/;" _RC=$?;
    elif [[ "${_Task}" == "RESTORE" ]]; then
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "[ -f ${dataStore}/${_Src} ] && cp -Rf ${dataStore}/${_Src} ${_gitPath}/; [ -f ${dataStore}/${_Drv} ] && cp -Rf ${dataStore}/${_Drv} ${_gitPath}/; [ -f ${dataStore}/${_Ibm} ] && cp -Rf ${dataStore}/${_Ibm} ${_gitPath}/; [ -f ${dataStore}/${_NonIbm} ] && cp -Rf ${dataStore}/${_NonIbm} ${_gitPath}/; chown -R tomcat:tomcat ${_gitPath}/;" _RC=$?;
    fi
    return ${_RC}
  }
  #-----------------------------------------------------------------------------#
  # Cleaning users data on the zLinux server
  function userDataTask {
    local _RC;
    local _Task="${1}"                 #Input1 - Task request
    local _serverName="${2}";          #Input2 server name
    local _usersData="/home/*/projects/"
    local _ztpfsData="/ztpfrepos/dvl /ztpfrepos/stg /ztpfrepos/loadsets/dvl /ztpfrepos/loadsets/stg /ztpsys/dvl /ztpfsys/stg"
    if [[ "${_Task}" == "CLEANUP" ]]; then
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "cd ${_gitPath}/; rm -rf ${_usersData} ${_ztpfsData};" _RC=$?;
    fi
    return ${_RC}
  }
  #-----------------------------------------------------------------------------#
  # Cleaning database
  function databaseTask {
    local _RC;
    local _Task="${1}"                 #Input1 - Task request
    local _serverName="${2}";          #Input2 server name
    local _sshresponse="$(sshpass -p ${_Salt##*:} ssh -o StrictHostKeyChecking=no -l ${MTP_SUSR} ${_serverName} 'etcdctl member list | grep isLeader=false')"
    local _respondomain="$(echo ${_sshresponse} | cut -d ' ' -f2)"
    local _falsedomain="${_respondomain##*=}.tvlport.net"
    if [[ "${_Task}" == "CLEANUP" ]]; then
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "mkdir -p ${_dataStore}/${conDate}_DB && cd ${_dataStore}/${conDate}_DB; PGPASSWORD=Wfw802017 psql -U workflow -h ${_falsedomain} -p 5432 -U postgres -d workflow -A -t -c 'TRUNCATE imp_plan CASCADE'" &> /dev/null; _RC=$?;
    elif [[ "${_Task}" == "BACKUP" ]]; then
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "mkdir -p ${_dataStore}/${conDate}_DB && cd ${_dataStore}/${conDate}_DB; pg_dump --dbname=postgresql://${PDB_SALT}@${_falsedomain}:5432/workflow > workflow.sql" &> /dev/null; _RC=$?;
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "mkdir -p ${_dataStore}/${conDate}_DB && cd ${_dataStore}/${conDate}_DB; pg_dump --dbname=postgresql://${PDB_SALT}@${_falsedomain}:5432/activiti > activiti.sql" &> /dev/null; _RC=$?;
    elif [[ "${_Task}" == "RESTORE" ]]; then
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "mkdir -p ${_dataStore}/DB && cd ${_dataStore}/DB; psql --dbname=postgresql://${PDB_SALT}@${_falsedomain}:5432/workflow < workflow.sql" &> /dev/null; _RC=$?;
      sshpass -p "${_Salt##*:}" ssh -o StrictHostKeyChecking=no -l "${MTP_SUSR}" "${_serverName}" "mkdir -p ${_dataStore}/DB && cd ${_dataStore}/DB; psql --dbname=postgresql://${PDB_SALT}@${_falsedomain}:5432/activiti < activiti.sql" &> /dev/null; _RC=$?;
    fi
    return ${_RC}
  }
  #-----------------------------------------------------------------------------#
  if [[ ${actTask} == CLEANUP ]]; then
    for zLnx in ${zLinux[@]}
    do
      userDataTask "CLEANUP" "${zLnx}"
    done
    for webServer in ${server[@]}
    do
      gitDataTask "CLEANUP" "${webServer}"
      databaseTask "CLEANUP" "${webServer}"
    done
  elif [[ ${actTask} == BACKUP ]]; then
    for webServer in ${server[@]}
    do
      gitDataTask "CLEANUP" "${webServer}"
      databaseTask "CLEANUP" "${webServer}"
    done
  elif [[ ${actTask} == RESTORE ]]; then
    for webServer in ${server[@]}
    do
      gitDataTask "CLEANUP" "${webServer}"
      databaseTask "CLEANUP" "${webServer}"
    done
  fi
fi
exit 0
