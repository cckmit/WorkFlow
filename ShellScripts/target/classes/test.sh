#!/bin/bash
#*****************************************************************************#
#-----------------------------------------------------------------------------#
unset MTP_ENV;                         #Clear MTP_ENV from systems env
MTP_ENV="${PWD}"
RC=0;
FC=1;
ER=8;
#-----------------------------------------------------------------------------#
function unitTest {
  for bTest in test/*.bats; do
    ./test/libs/bats/bin/bats "$bTest"
    if [ ! $? -eq 0 ]; then
      echo -e "\e[31m Testing FAILED - $bTest \e[10m"; RC=$ER;
    else
      echo "INFO: Test PASS - $bTest";
    fi
  done
  return $RC;
}
#-----------------------------------------------------------------------------#
echo "******** ShellScript Testing started"
echo "Path: MTP_ENV-$MTP_ENV"
echo "Dir: PWD-$PWD"
echo "User: USER-$USER"
echo "Home: HOME-$HOME"
git submodule update --init 
unitTest; RC=$?;
echo "******** ShellScript Testing completed"
exit $RC;
#-----------------------------------------------------------------------------#
# vim: filetype=bash
