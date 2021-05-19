#!/bin/bash
#*****************************************************************************#
#-----------------------------------------------------------------------------#
RC=0;
FC=1;
ER=8;
#-----------------------------------------------------------------------------#
function codeCheck {
  echo "INFO: Code quality checking..."
  for file in mtp*; do
    if [ "$file" != "mtp*" ];then 
      shellcheck "./$file"
      if [ ! $? -eq 0 ]; then
        echo -e "\e[31mERROR: Code quality BAD - $file\e[0m"; RC=$ER;
      else
        echo "INFO: Code quality GOOD - $file";
      fi
    fi
  done
  return $RC;
}
#-----------------------------------------------------------------------------#
echo "******** ShellScript build started"
codeCheck; RC=$?;
echo "******** ShellScript build completed"
exit $RC;
#-----------------------------------------------------------------------------#
# vim: filetype=bash
