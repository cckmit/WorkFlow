#!/bin/bash
#*****************************************************************************#
#   SCRIPT NAME: config.sh                                                    #
#                                                                             #
#   DESCRIPTION:                                                              #
#      Script to perform WorkFlow application config deployments              #
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
#   12242018    VINOTH  Two tomcat confirmation setup and Encryption          #
#*****************************************************************************#
declare -i _ReIP="$#";                 #Received number of input parms
declare -i _MiIP="23";                 #Expected number of input parms
#
ENV="${1^^}"                           #Input Deployment env (DEV,QA,PROD)
COMPANY="${2^^}"                       #Input Company name (TP, DL)
BUILDID="${3}"                         #Input Build number (1.20_2)
APP_SERVER="${4,,}"                    #Input Tomcat Application server
GIT_SERVER="${5,,}"                    #Input Git server URL or Load balancer DNS
TOS_SERVER="${6}"                      #Input TOS Server IP address
ZLX_SERVER="${7,,}"                    #Input zLinux Servers
APP_SRV_USER="${8}"                    #Input App deployment sudo user name
APP_SRV_PASS="${9}"                    #Input App deployment sudo user salt
TOS_SRV_USER="${10}"                   #Input Tos deployment user name
TOS_SRV_PASS="${11//\'/}"              #Input Tos deployment user salt - Replace quote
ADB_WFW_USER="${12}"                   #Workflow application database user name
ADB_WFW_PASS="${13}"                   #Workflow application database user salt
XDB_TIK_USER="${14}"                   #External Ticket service database user name
XDB_TIK_PASS="${15}"                   #External Ticket service database user salt
XDB_CSR_USER="${16}"                   #External CSR service database user name
XDB_CSR_PASS="${17}"                   #External CSR service database user salt
XDB_VPR_USER="${18}"                   #External VPARS database user name
XDB_VPR_PASS="${19}"                   #External VPARS database user salt
MTP_SRV_USER="${20}"                   #Input App Service user name
MTP_SRV_PASS="${21}"                   #Input App Service user salt
encrypted="${22}"                      #Encrypted values
tomcattype="${23}"                     #single or dual
#
APP="WF"                               #Application short name
SUF_SERVER="tvlport.net"               #Servers domain suffix
MTP_DOMAIN="galileo"                   #Network domain name(Windows NT)
TOS_SALT01="${MTP_DOMAIN}/${TOS_SRV_USER}%${TOS_SRV_PASS}" #Format-1 Salt
TOS_SALT02="${TOS_SRV_USER}:${TOS_SRV_PASS}" #Format-2 Salt
REL_PATH="/opt/delivery/release"       #Release path for the deployables
APP_PATH="/var/lib/tomcat/webapps"     #Application deployment path
APP_PATH2="/opt/tomcat2/webapps"       #Application deployment path 2 tomcat
MTP_PATH="/opt/mtp"                    #Support scripts deployment path
RC=0;                                  #Success return code
EC=8;                                  #Failed return code
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
# Generate configurations files for Linux Application Servers
function generateConfig {
  local _RC=0;
  #local APPSERVER;
  #local APPServer;
  #IFS=',' read -r -a APPSERVER <<< "$APP_SERVER"
  #for APPServer in "${APPSERVER[@]}"
  #do
    #if [ -d "${REL_PATH}/${APP}/${ENV}/${COMPANY}/${BUILDID}/${APPServer}" ]; then
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
  #done
  #unset IFS;
  return "${_RC}";
}
#
#-----------------------------------------------------------------------------#
# Main execution
echo -e "INFO: StartTime - $(date +%Y%m%d%H%M%S)";
minInputValidation "${_MiIP}" "${_ReIP}"; RC=$?;
#
if [ "${RC}" -eq 0 ]; then
  generateConfig; RC=$?;                #Calling task by order
  if [ "$RC" -ne 0 ]; then
    echo -e "ERROR: Generate Config failed to generate."
  fi
fi
echo -e "INFO: EndTime - $(date +%Y%m%d%H%M%S)";
#-----------------------------------------------------------------------------#
exit "$RC";
#-----------------------------------------------------------------------------#
# vim: filetype=bash