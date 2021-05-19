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
testfile="./mtpgitmigrateprodrepo"

@test "CODEMIGRATION: No input validation" {
  run $testfile
  [ $status -eq 8 ]
}
 
@test "CODEMIGRATION: Input validation" {
  run $testfile "a" "b" "c" "d"
  [ $status -eq 8 ]
}
 
@test "CODEMIGRATION: Invalid Product repository URL" {
  run $testfile "a" "b" "c"
  [ $status -eq 8 ]
}
 
@test "CODEMIGRATION: Invalid code repository structure" {
  run $testfile "a" "b" "c"
  [ $status -eq 8 ]
}
 
@test "CODEMIGRATION: Invalid Source path" {
  run $testfile "a" "b" "c"
    [ $status -eq 8 ]
}
 
@test "CODEMIGRATION: Source files invalid format" {
  run $testfile "a" "b" "c"
  [ $status -eq 8 ]
}
 
@test "CODEMIGRATION: New Prod repositpory migration" {
  createProdRepo
  run $testfile "$TEST_SRC" "$PROD_REPO_SSH" "apo"
}

@test "CODEMIGRATION: Recopying the existing Prod repository" { 
  run $testfile "$TEST_SRC" "$PROD_REPO_SSH" "apo"
  deleteProdRepo
}
#
#-----------------------------------------------------------------------------#
# vim: filetype=bats

