#!./libs/bats/bin/bats
#-----------------------------------------------------------------------------#
load 'libs/bats-support/load'
load 'libs/bats-assert/load'
load 'helpers'

setup() {
  setupMTPEnv
}

teardown() {
  teardownMTPEnv
}

#
#-----------------------------------------------------------------------------#
testfile="./mtpgitcmdmerge"


@test "MERGE: No input validation" {
   run $testfile
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}

@test "MERGE: Input validation" {
   run $testfile "a" "b" "c"
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}

@test "MERGE: Invalid GIT SCM repository URL" {
   run $testfile "/home/$USER/"
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}

@test "MERGE: Invalid local Destination repository structure" {
   run $testfile "a"
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}
 
