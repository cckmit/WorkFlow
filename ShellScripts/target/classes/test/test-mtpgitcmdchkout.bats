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
  run bash -c "rm -rf $HOME/projects/t1700000"
}

#
#-----------------------------------------------------------------------------#
testfile="./mtpgitcmdchkout"

@test "CHECKOUT: Should print missing inputs successfully if requested" {
  run $testfile

  echo "Status-$status"
  echo "$output"

  [ "$status" -eq 8 ]
  [ "$output" = "ERROR: Missing inputs" ]

  teardownWorkspace
}

@test "CHECKOUT: Should return error for invalid prod source repo url" {
  run $testfile "t1700000_apo" "src/lmc2.asm" "3a3c1ad6fa3dc5825e157ce4c69c22086cfc5316" "3a334bba2fea96d35777f633d1a2a90e08710066" "ssh://$USER@dev-mtp.tpfsoftware.com:8445/tpf/tp/nonibm/nonibm_xutest.git"

  echo "Status-$status"
  echo "$output"

  assert_failure

}

#@test "CHECKOUT: Should return fail while checkout form invalid commit id" {
#  createImplPlanRepo
#  createImplBranch
#  createWorkspace
#    
#  run $testfile "t1700000_apo" "src/lmc2.asm" "3a3c1ad6fa3dc5825e157ce4c69c22086cfc5316" "x334bba2fea96d35777f633d1a2a90e08710066" "ssh://$USER@dev-mtp.tpfsoftware.com:8445/tpf/tp/nonibm/nonibm_utest.git"

#  echo "Status-$status"
#  echo "Output"

#  assert_failure
#}

@test "CHECKOUT: Should return fail while checkout using invalid source sha1" {
  run $testfile "t1700000_apo" "src/lmc2.asm" "xa3c1ad6fa3dc5825e157ce4c69c22086cfc5316" "3a334bba2fea96d35777f633d1a2a90e08710066" "ssh://$USER@dev-mtp.tpfsoftware.com:8445/tpf/tp/nonibm/nonibm_utest.git"

  echo "Status-$status"
  echo "$output"

  assert_failure
}

#@test "CHECKOUT: Should return fail while checkout using invlid source" {
#  run $testfile "t1700000_apo" "src/xlmc2.asm" "3a3c1ad6fa3dc5825e157ce4c69c22086cfc5316" "3a334bba2fea96d35777f633d1a2a90e08710066" "ssh://$USER@dev-mtp.tpfsoftware.com:8445/tpf/tp/nonibm/nonibm_utest.git"
#
#  echo "Status-$status"
#  echo "$output"

#  [ "$status" -eq 0 ]
#  [ "${lines[0]}" = "STATUS: t1700000_001_apo src/xlmc2.asm failed." ];
#}

#@test "CHECKOUT: Should return success while checkout valid source form the valid production source repo" {
#  run $testfile "t1700000_apo" "src/lmc2.asm" "3a3c1ad6fa3dc5825e157ce4c69c22086cfc5316" "3a334bba2fea96d35777f633d1a2a90e08710066" "ssh://$USER@dev-mtp.tpfsoftware.com:8445/tpf/tp/nonibm/nonibm_utest.git"

#  echo "Status-$status"
#  echo "$output"
#
#  [ "$status" -eq 0 ]
#  [ "${lines[0]}" = "STATUS: t1700000_001_apo src/lmc2.asm success." ];
#}

#@test "CHECKOUT: Should return source exisit while checkout same source" {
#  run $testfile "t1700000_apo" "src/lmc2.asm" "3a3c1ad6fa3dc5825e157ce4c69c22086cfc5316" "3a334bba2fea96d35777f633d1a2a90e08710066" "ssh://$USER@dev-mtp.tpfsoftware.com:8445/tpf/tp/nonibm/nonibm_utest.git"

#  echo "Status-$status"
#  echo "$output"
#
#  [ "$status" -eq 0 ]
#  [ "${lines[0]}" = "STATUS: t1700000_001_apo src/lmc2.asm exists at workspace." ];
#}

#
#-----------------------------------------------------------------------------#
# vim: filetype=bats

