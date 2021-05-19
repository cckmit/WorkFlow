#!/bin/bash
#************************************************************************************#
#   SCRIPT NAME: tsiwfcreateimplementation.sh                                        #
#                                                                                    #
#   DESCRIPTION:                                                                     #
#      Script to create implemantion in implemantion plan repositories               #
#                                                                                    #
#   COMMANDS   :                                                                     #
#                                                                                    #
#   NOTE       :                                                                     #
#                                                                                    #
#   INPUT:                                                                           #
#      $1  - Implementaion  Plan repo list     (*)                                   #
#      $2  - Implementation Branch list        (*)                                   #
#                                                                                    #
#   OUTPUT:                                                                          #
#      exit with 0, successfull process                                              #
#      exit with 8, error message                                                    #
#                                                                                    #
#************************************************************************************#
#************************************************************************************#
#                                                                                    #
#                            M A I N T E N A N C E                                   #
#                                                                                    #
#------------------------------------------------------------------------------------#
#   MMDDYYYY    INIT    COMMENT                                                      #
#   --------    ------  -------------------------------------------------------------#
#   13072016    THILAK  Created the script                                           #
#   20092016    ARUL    Updated the script for empty branch                          #
#   13022017    UVAIS   Updated the script for workflow support                      #
#   27022017    ARUL    Updated with master creation support                         #
#************************************************************************************#
#
#------------------------------------------------------------------------------------#
# Validate Input Parameter                                                           #
#------------------------------------------------------------------------------------#
#
#Number of mandatory inputs
MIP=2
if [ $# -eq 0 ]; then
  echo "ERROR: Missing inputs";
  echo "USAGE: tsiwfcreateimplementation.sh <Implementaion Plan repo list> <Implementaion Name List>";
  exit 8;
elif [ $# -gt $MIP ]; then
  echo "ERROR: Unexpected no of arguments - '$#'";
  exit 8;
else
  for x; do
    if [ -z $x ]; then
      echo "ERROR: Verify input arguments some of item is empty";
      exit 8;
    fi
  done
fi

REMOTE_REPO_LIST=$1
BRANCH_NAME_LIST=$2
IFS=', ' read -r -a BRANCH_LIST <<< "$BRANCH_NAME_LIST"
IFS=', ' read -r -a REPO_LIST <<< "$REMOTE_REPO_LIST"
Branch_Creation_Count=0;
Success_Count=0;

#------------------------------------------------------------------------------------#
# Create empty branch for implemantion                                               #
#------------------------------------------------------------------------------------#
#
gitempty_branch(){
  true | git mktree | xargs git commit-tree -m "$DefaultImpl Branch Created" | xargs git branch $BRANCH_NAME &> /dev/null
  git checkout $BRANCH_NAME
  touch .gitignore
  git add .gitignore
  git commit -am "$BRANCH_NAME - Branch Created" &> /dev/null
  git push -u origin $BRANCH_NAME &> /dev/null
  if [ $? -ne 0 ]
  then 
    echo "STATUS: Branch "$BRANCH_NAME" failed [Push]."
  else
    echo "STATUS: Branch "$BRANCH_NAME" created."
    Success_Count=$((Success_Count+1))
  fi
}
#

for i in ${!REPO_LIST[*]};
do
#Assign input parameters to variable
  REMOTE_DIR_NAME=${REPO_LIST[$i]}
  BRANCH_NAME=${BRANCH_LIST[$i]}
#  echo "REMOTE_DIR_NAME:" $REMOTE_DIR_NAME
#  echo "BRANCH_NAME:" $BRANCH_NAME
  Branch_Creation_Count=$((Branch_Creation_Count+1))
#
#------------------------------------------------------------------------------------#
# Check for valid repository                                                         #
#------------------------------------------------------------------------------------#
#
  git ls-remote ssh://$USER@`hostname --fqdn`:8445/$REMOTE_DIR_NAME.git &> /dev/null
  if [ $? -ne 0 ]
  then
    echo "STATUS: Branch "$BRANCH_NAME" failed [Invalid Repository]."
  else 
    git ls-remote ssh://$USER@`hostname --fqdn`:8445/$REMOTE_DIR_NAME.git | grep "master" &> /dev/null
    if [ $? -ne 0 ]
    then
      mkdir repo_init
      cd repo_init
      touch .gitignore
      git init -q
      git add .gitignore
      git commit -m "REPOSITORY INITIALIZED" &> /dev/null
      git remote add origin ssh://$USER@`hostname --fqdn`:8445/$REMOTE_DIR_NAME.git 2> /dev/null
      git push -u origin master &> /dev/null
      gitempty_branch
    else
      git clone ssh://$USER@`hostname --fqdn`:8445/$REMOTE_DIR_NAME.git repo_init -q
      cd repo_init
      git branch -a | grep $BRANCH_NAME &> /dev/null
      status=$?
      if [ $status -eq 0 ]; then
        echo "STATUS: Branch "$BRANCH_NAME" exist [Branch Already found]."
        Success_Count=$((Success_Count+1))
      else 
        gitempty_branch
      fi
    fi 
#
#------------------------------------------------------------------------------------#
# Clean up folders                                                                   #
#------------------------------------------------------------------------------------#
#
    cd ..
    rm -Rf repo_init
  fi 
done
if [ $Success_Count -eq 0 ]; then 
  exit 8;
elif [ $Success_Count -eq $Branch_Creation_Count ]; then
  exit 0;
else
  exit 1;
fi
  
#
#------------------------------------------------------------------------------------#
