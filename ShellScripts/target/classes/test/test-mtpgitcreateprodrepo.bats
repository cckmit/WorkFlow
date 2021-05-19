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
testfile="./mtpgitcreateprodrepo"

@test "PRODREPO: Should print missing inputs successfully if requested" {
  run $testfile

  [ "$status" -eq 8 ]
  [ "$output" = "ERROR: Missing inputs" ]
}

@test "PRODREPO: Should print error message if package name contains special char if requested" {
  run $testfile "tpf" "tv" "mw" "ut@est" "unitTest" "jenkins"

  [ "$status" -eq 8 ]
  [ "$output" = "ERROR: Package Name contain special char" ]
}

@test "PRODREPO: Should create prod repo successfully if requested" {
  run $testfile "tpf" "tv" "mw" "utest" "unitTest" "jenkins"

  assert_success
  assert_line "INFO: Production repository tpf/tv/mw/mw_utest.git has been created."
}

@test "PRODREPO: Should print if prod repo already available if requested" {
  run $testfile "tpf" "tv" "mw" "utest" "unitTest" "jenkins"

  [ "$status" -eq 1 ]
  [ "$output" = "ERROR: Repository tpf/tv/mw/mw_utest.git already available" ]
  assert_failure
  run curl -X POST -H "Content-Type: application/json"\
                     -H "Authorization: Basic $MTP_SSAL"\
                                 -H "Cache-Control: no-cache"\
  -d@/tmp/"$USER"_json.data "${PRD_BIN_RURL}/gitblit/rpc/?req=DELETE_REPOSITORY"
}
#
#-----------------------------------------------------------------------------#
# vim: filetype=bats
