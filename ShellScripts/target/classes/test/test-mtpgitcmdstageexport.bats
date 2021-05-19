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
testfile="./mtpgitcmdstageexport"

# @test "POPULATE: Should print missing inputs successfully if requested" {
  # run $testfile

  # echo "Status-$status"
  # echo "$output"

  # [ "$status" -eq 8 ]
  # [ "$output" = "ERROR: Missing inputs" ]
# }

# @test "POPULATE: Should return error for invalid prod source repo url" {
  # run $testfile "base/rt/ar/crda.c" "tpf/tp/ibm/ibm_putXX.git" "master_apo"

  # echo "Status-$status"
  # echo "$output"

  # assert_failure
# }

# @test "POPULATE: Should return fail while checkout form invalid source" {
  # run $testfile "base/rt/ar/cxxx.c" "tpf/tp/ibm/ibm_put13.git" "master_apo"
  # echo "Status-$status"
  # echo "Output"

  # assert_failure
# }

# @test "POPULATE: Should return fail while checkout using invalid branch name" {
  # run $testfile "base/rt/ar/crda.c" "tpf/tp/ibm/ibm_put13.git" "master_xxx"
  # echo "Status-$status"
  # echo "$output"

  # assert_failure
# }

#@test "POPULATE: Should return source exisit while checkout same source" {
#  run $testfile "base/rt/ar/crda.c" "tpf/tp/ibm/ibm_put13.git" "master_apo"

#  echo "Status-$status"
#  echo "$output"

#  [ "$status" -eq 0 ]
#  [ "${lines[0]}" = "INFO: Push to remote branch master_apo success." ];
#  assert_success
#}

#
#-----------------------------------------------------------------------------#
# vim: filetype=bats

