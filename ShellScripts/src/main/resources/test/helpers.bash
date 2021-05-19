#!/bin/bash
#*****************************************************************************#
MTP_SUSR="jenkins"                     #MTP Project super user account
MTP_SSAL="amVua2luczpKZW5rMjAxNg=="    #MTP Project super user account
#
#-----------------------------------------------------------------------------#
# PRODUCTION REMOTE GITBLIT URL CONFIGURATION                                 #
#-----------------------------------------------------------------------------#
PRD_RHOST="dev-mtp.tpfsoftware.com"    #Git remote host name FQDN
PRD_SRC_CHNL="ssh"                     #Git source repository protocol channel
PRD_SRC_PORT="8445"                    #Git source repository protocol port
PRD_BIN_CHNL="https"                   #Git binary repository protocol channel
PRD_BIN_PORT="8443"                    #Git binary repository protocol port
#
#-----------------------------------------------------------------------------#
# DEVELOPMENT REMOTE GIRBLIT URL CONFIGURATION                                #
#-----------------------------------------------------------------------------#
DEV_RHOST="${PRD_RHOST}"               #Git remote host name FQDN
DEV_SRC_CHNL="${PRD_SRC_CHNL}"         #Git source repository protocol channel
DEV_SRC_PORT="${PRD_SRC_PORT}"         #Git source repository protocol port
DEV_BIN_CHNL="${PRD_BIN_CHNL}"         #Git binary repository protocol channel
DEV_BIN_PORT="${PRD_BIN_PORT}"         #Git binary repository protocol port
#
#-----------------------------------------------------------------------------#
# DEVELOPER TPF & GIT PROJECT CONFIGURATION                                   #
#-----------------------------------------------------------------------------#
LNX_USER="$USER"                       #Get Linux User Name
GIT_USER="${LNX_USER}"                 #Get Git User Name
USR_TPF_PROJ="~/projects"          #TPF Project path for user workspace
#-----------------------------------------------------------------------------#
#-----------------------------------------------------------------------------#
# BUILD GIRBLIT URL CONFIGURATION - RESTRICTED TO EDIT                        #
#-----------------------------------------------------------------------------#
#
PRD_SRC_RURL="${PRD_SRC_CHNL}://${MTP_SUSR}@${PRD_RHOST}:${PRD_SRC_PORT}"                       #Production Source Git Repo Remote URL
#
PRD_BIN_RURL="${PRD_BIN_CHNL}://${MTP_SUSR}@${PRD_RHOST}:${PRD_BIN_PORT}"                       #Production Source Git Repo Remote URL
#-----------------------------------------------------------------------------#
DEV_SRC_RURL="${DEV_SRC_CHNL}://${GIT_USER}@${DEV_RHOST}:${DEV_SRC_PORT}"                       #Source Git Remote URL
#
DEV_BIN_RURL="${DEV_BIN_CHNL}://${GIT_USER}@${DEV_RHOST}:${DEV_BIN_PORT}"                       #Source Git Remote URL
#-----------------------------------------------------------------------------#
# UNIT TEST CONFIGURATION                                                     #
#-----------------------------------------------------------------------------#
TEST_SRC="$PWD/test/migrate"
PROD_REPO_NAME="tpf/tp/nonibm/nonibm_utest.git"
DEVL_REPO_NAME="/tpf/tp/source/t1700000"
DEVL_BRAN_LIST="t1700000_001_apo,t1700000_001_pgr,t1700000_001_pre"
#-----------------------------------------------------------------------------#
assert_exists() {
  assert [ -f "$1" ]
}
#
#-----------------------------------------------------------------------------#
setupMTPEnv() {
  export MTP_DIRECTORY="$(mktemp -d)"
}
#
#-----------------------------------------------------------------------------#
teardownMTPEnv() {
  if [ "$BATS_TEST_COMPLETED" ]; then
    rm -rf "$MTP_DIRECTORY"
  else
    echo "** Did not delete $MTP_DIRECTORY, as test failed **"
  fi
}
#-----------------------------------------------------------------------------#
createProdRepo() {
 run bash -c "${MTP_ENV}/mtpgitcreateprodrepo tpf tv mw utest unitTest jenkins"
}
#-----------------------------------------------------------------------------#
deleteProdRepo() {
  run curl -X POST -H "Content-Type: application/json"\
                   -H "Authorization: Basic $MTP_SSAL"\
                   -H "Cache-Control: no-cache"\
  -d@/tmp/"${USER}_utest.data" "${PRD_BIN_RURL}/gitblit/rpc/?req=DELETE_REPOSITORY"
}
#-----------------------------------------------------------------------------#
createImplPlanRepo() {
  echo "{\"name\": \"tpf/tp/source/t1700000\", \"description\": \"DO NOT DELETE\",\
         \"owners\": [\"$MTP_SUSR,$GIT_USER\"],\
         \"accessRestriction\": \"PUSH\", \"authorizationControl\": \"NAMED\",\
         \"allowAuthenticated\": \"true\",\
         \"size\": \"102 KB\"}">/tmp/"${USER}_utest.data";
  run curl -X POST -H "Content-Type: application/json"\
                   -H "Authorization: Basic $MTP_SSAL"\
                   -H "Cache-Control: no-cache"\
 -d@/tmp/"${USER}_utest.data" "${PRD_BIN_RURL}/gitblit/rpc/?req=CREATE_REPOSITORY"
}
#-----------------------------------------------------------------------------#
deleteImplPlanRepo() {
  run curl -X POST -H "Content-Type: application/json"\
                   -H "Authorization: Basic $MTP_SSAL"\
                   -H "Cache-Control: no-cache"\
 -d@/tmp/"${USER}_utest.data" "${PRD_BIN_RURL}/gitblit/rpc/?req=DELETE_REPOSITORY"
}
#-----------------------------------------------------------------------------#
createImplBranch() {
  local test_dir;
  local pwd_dir;
  pwd_dir="$PWD"
  test_dir="$(mktemp -d)"
  cd "$test_dir"
    git init &>/dev/null
    git remote add origin "$DEV_SRC_RURL/tpf/tp/source/t1700000.git"
    git fetch --all &>/dev/null
    if [ -f "README.md" ]; then
      ehco "Unit Test" >> README.md
    else
      touch README.md
    fi
    git add . &>/dev/null
    git commit -am "first commit" &>/dev/null
    git pull origin master &>/dev/null
    git push -u origin master &>/dev/null
    git checkout -b master_apo &>/dev/null
    git push -u origin master_apo &>/dev/null
    git checkout master &>/dev/null
    git checkout -b master_pgr &>/dev/null
    git push -u origin master_pgr &>/dev/null
    git checkout master &>/dev/null
    git checkout -b master_pre &>/dev/null
    git push -u origin master_pre &>/dev/null
    git checkout master &>/dev/null
    git checkout -b t1700000_001_apo &>/dev/null
    git push -u origin t1700000_001_apo &>/dev/null
    git checkout master &>/dev/null
    git checkout -b t1700000_001_pgr &>/dev/null
    git push -u origin t1700000_001_pgr &>/dev/null
    git checkout master &>/dev/null
    git checkout -b t1700000_001_pre &>/dev/null
    git push -u origin t1700000_001_pre &>/dev/null
  cd "$pwd_dir"
  rm -rf "$test_dir"
}
#-----------------------------------------------------------------------------#
createWorkspace() {
  local test_dir;
  test_dir="$(mkdir -p /home/$USER/projects/t1700000_001)"
  cd "$test_dir"
  git clone -b t1700000_001_apo "$DEV_SRC_RURL/tpf/tp/source/t1700000.git" apo
  git clone -b t1700000_001_pgr "$DEV_SRC_RURL/tpf/tp/source/t1700000.git" pgr
  git clone -b t1700000_001_pre "$DEV_SRC_RURL/tpf/tp/source/t1700000.git" pre
}
#-----------------------------------------------------------------------------#
# vim: filetype=bats
