#!/bin/bash
#************************************************************************************#
#   SCRIPT NAME: tsigitimpcntlfile.sh                                                #
#                                                                                    #
#   DESCRIPTION:                                                                     #
#      Script to create and update the implementation control file in local workspace#
#                                                                                    #
#   INPUT:                                                                           #
#      $1  - User ID                                                                 #
#      $2  - Request (ADD/DELETE/REFRESH)                                            #
#      $3  - Implementation name:Repository:Project Destination path                 #
#                                                                                    #
#   OUTPUT:                                                                          #
#      exit with 0, successfully control file is updated                             #
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
#   13072016    UVAIS   Created the script                                           #
#************************************************************************************#
if [ $# -lt 3 ]; then
    echo "ERROR: Missing Mandatory arguments";
    exit 8;
elif [ $# -gt 3 ]; then
  echo "ERROR: Unexpected no of arguments - '$#'";
  exit 8;
fi
User=$1
Req=$2
Parm=$3

if [ $Req = "ADD" ]; then
  echo $Parm >> /home/$User/.implementation
elif [ $Req = "REFRESH" ]; then
   cat /home/$User/.implementation | while read Line;
   do
    Dest_path=`echo $Line | cut -d":" -f3`;
    if [ ! -d "${Dest_path}" ]; then 
      Newparm=$(echo ${Line//\//\\/})
      sed -i "/$Newparm/d" /home/$User/.implementation 
    fi
  done 
elif [ $Req = "DELETE" ]; then
  Dest_path=`echo $Parm | cut -d":" -f3`;
  rm -rf $Dest_path;
  Newparm=$(echo ${Parm//\//\\/}) 
  sed -i "/$Newparm/d" /home/$User/.implementation 
else
  echo "ERROR: Invalid Request";
  exit 8;
fi
exit 0
