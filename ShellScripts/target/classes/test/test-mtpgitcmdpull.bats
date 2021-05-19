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
testfile="./mtpgitcmdpull"


@test "PULL: No input validation" {
   run $testfile   
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}
 
@test "PULL: Input validation" {
   run $testfile "a" "b" "c"
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}
 
@test "PULL: Invalid GIT SCM repository URL" {
   run $testfile "/home/$USER/" 
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}
 
@test "PULL: Invalid local Destination repository structure" {
   run $testfile "a"
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}

