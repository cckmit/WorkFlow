#!/bin/bash
#--------------------------------------------------------------------------
# postgres_backup.sh - Wrapper support for crone job
#
# USAGE:
#    crontab -e
#    5 0 * * * /opt/PostgreSQL/postgres_backup.sh
#
# DESCRIPTION:
#    Use this script in cron job to execute exiting logival backup script
#
#--------------------------------------------------------------------------
master=$(etcdctl member list | grep "isLeader=true" | cut -d' ' -f3)
tmp1="${master##*/}"
masterIP="${tmp1%:*}"
/sbin/ifconfig | grep "${masterIP}" &> /dev/null; RC=$?;
if [ "$RC" -eq 0 ]; then
  if [[ "$(hostname)" =~ qa ]]; then
    /opt/PostgreSQL/logical_backup.ksh TPFDVOPS QA
  elif [[ "$(hostname)" =~ pp ]]; then
    /opt/PostgreSQL/logical_backup.ksh TPFDVOPS PP
  elif [[ "$(hostname)" =~ pn ]]; then
    /opt/PostgreSQL/logical_backup.ksh TPFDVOPS PN
  fi
fi
