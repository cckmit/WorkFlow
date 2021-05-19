#!/bin/bash
# Name - dirGenerate.sh
# This script used to generate multiple git repository for testing
# Note: Don't install in Crontab
pushd /opt/gitblit/data/git/tpf/dl/ibm
  for i in {2..1000}
  do
    cp --preserve=all ibm_put1a.git ibm_put${i}a.git -rf
  done
popd
