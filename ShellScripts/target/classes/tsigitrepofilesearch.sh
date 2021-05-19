#!/bin/bash
#************************************************************************************#
#   SCRIPT NAME: tsigitrepofilesearch                                                #
#                                                                                    #
#   DESCRIPTION:                                                                     #
#      This script is to search the given files from multpile PROD/NON PROD SCM GIT  #
#      repositories                                                                  #
#                                                                                    #
#   INPUT:                                                                           #
#      $1  - GIT Data path                                                           #
#      $2  - Core (T4/WSP/DELTA)                                                     #
#      $3  - File name for search (Wild card AB* or full name)                       #
#      $4  - Prod/ Non Prod indicatior(PROD/NONPROD)                                 #
#                                                                                    #
#   OUTPUT:                                                                          #
#      exit with 0, successfull                                                      #
#      exit with 8, error message                                                    #
#                                                                                    #
#************************************************************************************#
#************************************************************************************#
#                                                                                    #
#                            M A I N T E N A N C E                                   #
#                                                                                    #
#------------------------------------------------------------------------------------#
#   MMDDYYYY    INIT    COMMENT                                                      #
#   --------    ----    -------------------------------------------------------------#
#   20102016    UVAIS   Created the script                                           #
#************************************************************************************#

MIP=4
if [ $# -eq 0 ]; then
  echo "ERROR: Missing inputs";
  echo "USAGE: tsigitrepofilesearch.sh <GIT Data path> <Core> <File name> <PROD/NONPROD>";
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

Git_Data_path=$1;
Req_core=$2;
Search_File=$3;
Prod_Indicator=$4
#
#------------------------------------------------------------------------------------#
# Prod File Search                                                                   #
#------------------------------------------------------------------------------------#
function Prod_File_Search() {

Package_path_list=$(find ./* -maxdepth 2 -mindepth 2 | grep ".git")

for Cur_package in $Package_path_list
do
  cd $Cur_package
  Repo_Url="ssh://$USER@`hostname --fqdn`:8445/$Req_core${Cur_package#.}"
  Package_name=`echo $Cur_package | cut -d"/" -f4`
  Repo_desc=$(git config --get gitblit.description)
  Dev_Status="NA"
  file_list=$(git ls-tree master --name-only -r | grep -v ".git*" | grep "/$Search_File" )
  for file in $file_list
  do
    curfile=`echo $file | cut -d"/" -f2 `
    git log --branches=refs/master --tags=refs/tags --pretty=format:"$curfile|${Package_name%%.git}|$Repo_Url|$Repo_desc|$Dev_Status|%h|%an|%ae|%cn|%ce|%cd|%s|%d|$Prod_Indicator%n" -- $file
  done
  cd $Git_Data_path/$Req_core
done
return;
}
#
#------------------------------------------------------------------------------------#
# Non Prod File Search                                                               #
#------------------------------------------------------------------------------------#
function Non_Prod_File_Search() {
Req_core=${Req_core:0:1}
Package_path_list=$(find ./*  -maxdepth 0 -mindepth 0 | grep ".git" | grep "./$Req_core" )

for Cur_package in $Package_path_list
do
  cd $Cur_package
  Cur_package=${Cur_package#./}
#  echo "Curent_package="$Cur_package
  Repo_Url="ssh://$USER@`hostname --fqdn`:8445/${Cur_package}"
  Repo_desc=$(git config --get gitblit.description)
  Repo_frozen=$(git config --get gitblit.isfrozen)
  if [ $Repo_frozen == "false" ]; then
    Dev_Status="DEV";
  else
    Dev_Status="SECURED";
  fi
  file_list=$(git ls-tree master --name-only -r | grep -v ".git*" | grep "/$Search_File" )
  for file in $file_list
  do
    curfile=`echo $file | cut -d"/" -f2 `
    git log -1 --branches=refs/master --tags=refs/tags --pretty=format:"$curfile|${Cur_package%%.git}|$Repo_Url|$Repo_desc|$Dev_Status|%h|%an|%ae|%cn|%ce|%cd|%s|%d|$Prod_Indicator%n" -- $file
  done
  cd $Git_Data_path/
done
return;
}
#
#------------------------------------------------------------------------------------#
# Check for GIT Data path validation                                                 #
#------------------------------------------------------------------------------------#

cd $Git_Data_path
if [ $? -ne 0 ]; then
  echo "ERROR: GIT Data path is invalid";
  exit 8;
fi
#
#------------------------------------------------------------------------------------#
# File Search Process Started                                                        #
#------------------------------------------------------------------------------------#
#
if [ $Prod_Indicator == "PROD" ]; then
  cd $Req_core
  if [ $? -ne 0 ]; then
    echo "ERROR: No Prodcution repository present for the core - $Req_core";
    exit 8;
  else
    Prod_File_Search;
  fi
else
  Non_Prod_File_Search;
fi
exit 0;
