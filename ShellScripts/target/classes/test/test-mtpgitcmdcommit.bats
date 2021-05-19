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

teardownWorkspace() {
  run bash -c "rm -rf $HOME/projects/t1700000_001/"
} 
#
#-----------------------------------------------------------------------------#
testfile="./mtpgitcmdcommit"

@test "COMMIT: No input parameter " {
   run $testfile 
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
  
}
 
 
@test "COMMIT: Invalid user workspace" {
   run  $testfile "UnitTest" "/home/$USER/projects/xx"
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}
 
@test "COMMIT: Valid project but invalid git repo" {
   run  $testfile "UnitTest" "/home/$USER/projects/t1700000_001"
   [ $status -eq 8 ]
   echo "output: $output"
   echo "status: $status"
}
 
#@test "COMMIT: Valid commit should success with project" {
#   teardownWorkspace
#   createWorkspace
#
#   run bash -c "echo UnitTest >> $HOME/projects/t1700000_001/apo/README.md"
#   
#   run  $testfile "UnitTest" "/home/$USER/projects/t1700000_001/apo"
#
#   [ $status -eq 0 ]
#   echo "output: $output"
#   echo "status: $status"
#
#}

