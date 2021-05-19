#!/bin/ksh -x
#--------------------------------------------------------------------------
# automated_backup.ksh - Logical Backups for Postgres Cluster and Databases
#
# USAGE:
#    automated_backup.ksh <Cluster Abbreviation ex. QA, INT, DEV>
#
# DESCRIPTION:
#    Use this script to execute PostgreSQL exports using pg_dump and pg_dumpall
#    to create logical backups of all databases within a cluster.
#
#--------------------------------------------------------------------------

# check number of command line arguments
if [ $# -ne 2 ]; then
   echo "Usage: $0 <App Name and Cluster Abbreviation ex. TRAM QA, CONTENT PP, REDMINE PN> "
   exit 1
fi

APP_NAME=$1
CLUSTER_NAME=$2

DBA_EMAIL="vaijayanthy.mahadevan@travelport.com,tooracledba@travelport.com,oracledbasupport@travelport.com,arul.dhandapani@travelport.com"
DBA_EMAIL_SUCCESS="vaijayanthy.@travelport.com,arul.dhandapani@travelport.com"

#. /opt/PostgreSQL/postgresql${APP_NAME}_${CLUSTER_NAME}.profile
export PGPORT=5432
export PGDATA=/opt/patroni/patroni
export PGUSER=postgres

PGARGS="-Fc -p $PGPORT -U postgres -h localhost"

# directory to save backups in, must be rwx by postgres user
BACKUP_DIR="/opt/PostgreSQL/${CLUSTER_NAME}_backups/logical_backups"
YMD=$(date "+%m%d%Y")
mkdir -p $BACKUP_DIR
cd $BACKUP_DIR

# get list of databases in the cluster, exclude the template and postgres databases
DBS=$(/bin/psql -U postgres -h localhost -A -t -c 'select datname from pg_database;' | egrep -v 'template[01]' | egrep -v 'postgres')

# first dump entire postgres cluster without data.
/bin/pg_dumpall -p $PGPORT -U postgres -h localhost -s > "${BACKUP_DIR}/${YMD}_${APP_NAME}_${CLUSTER_NAME}_cluster_backup_nodata.sql"
if [ $? != 0 ]
    then
        echo "Subject: "" Logical Backup Failed on " `hostname` " "`date` | /usr/sbin/sendmail $DBA_EMAIL
    exit 1
fi

# next dump globals (roles and tablespaces) only
/bin/pg_dumpall -p $PGPORT -U postgres -h localhost -g > "${BACKUP_DIR}/${YMD}_${APP_NAME}_${CLUSTER_NAME}_cluster_backup_globals.sql"
if [ $? != 0 ]
    then
        echo "Subject: "" Logical Backup Failed on " `hostname` " "`date` | /usr/sbin/sendmail $DBA_EMAIL
    exit 1
fi

# now loop through each individual database
for DATABASE in $DBS; do
    FULLDATABASE=$BACKUP_DIR/${YMD}_${APP_NAME}_${CLUSTER_NAME}_${DATABASE}_database_backup.sql

    # dump databases
    /bin/pg_dump $PGARGS $DATABASE > $FULLDATABASE
if [ $? != 0 ]
    then
        echo "Subject: "" Logical Backup Failed on " `hostname` " "`date` | /usr/sbin/sendmail $DBA_EMAIL
    exit 1
fi
done

# delete backup files older than 3 days
find $BACKUP_DIR \( -daystart -name '*.sql' \) -mtime +1 -exec rm {} \;

echo "Subject: "" Logical Backup SUCCESSFUL on " `hostname` " "`date` | /usr/sbin/sendmail $DBA_EMAIL_SUCCESS

exit
