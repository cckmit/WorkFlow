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
  run bash -c "rm -rf $HOME/projects/"
}

#
#-----------------------------------------------------------------------------#
testfile="./mtpgitcreateworkspace"

# @test "WORKSPACE: Should print missing inputs successfully if requested" {
  # teardownWorkspace
  # run $testfile

  # echo "Status-$status"
  # echo "$output"

  # [ "$status" -eq 8 ]
  # [ "$output" = "ERROR: Missing inputs" ]
# }

# @test "WORKSPACE: Should return error exit code for invalid implementation plan repo" {
  # run $testfile "/tpf/tp/source/x1700000" "t1700000_001_apo,t1700000_001_pgr,t1700000_001_pre"

  # echo "Status-$status"
  # echo "$output"

  # assert_failure
# }

# @test "WORKSPACE: Should return fail while create workspace for invalid implementation branch list" {
  # createImplPlanRepo
  # createImplBranch
    
  # run $testfile "/tpf/tp/source/t1700000" "x1700000_001_apo,x1700000_001_pgr,x1700000_001_pre" 

  # echo "Status-$status"
  # echo "Output"

  # [ "$status" -eq 8 ]
  # [ "${lines[0]}" = "STATUS: Workspace x1700000_001_apo failed." ];
  # [ "${lines[1]}" = "STATUS: Workspace x1700000_001_pgr failed." ];
  # [ "${lines[2]}" = "STATUS: Workspace x1700000_001_pre failed." ];
# }

# @test "WORKSPACE: Should return partial fail while create workspace if invalid implementation branch" {
  # run $testfile "/tpf/tp/source/t1700000" "t1700000_001_apo,x1700000_001_pgr,t1700000_001_pre"

  # echo "Status-$status"
  # echo "$output"

  # [ "$status" -eq 0 ]
  # [ "${lines[0]}" = "STATUS: Workspace t1700000_001_apo created." ];
  # [ "${lines[1]}" = "STATUS: Workspace x1700000_001_pgr failed." ];
  # [ "${lines[2]}" = "STATUS: Workspace t1700000_001_pre created." ];
# }

# @test "WORKSPACE: Should return partial fail while create workspace if implementation exist in workspace" {
  # run $testfile "/tpf/tp/source/t1700000" "t1700000_001_apo,x1700000_001_pgr,t1700000_001_pre"

  # echo "Status-$status"
  # echo "$output"

  # [ "$status" -eq 0 ]
  # [ "${lines[0]}" = "STATUS: Workspace t1700000_001_apo exist." ];
  # [ "${lines[1]}" = "STATUS: Workspace x1700000_001_pgr failed." ];
  # [ "${lines[2]}" = "STATUS: Workspace t1700000_001_pre exist." ];
# }

# @test "WORKSPACE: Should return success while create workspace even implementation exist in workspace" {
  # run $testfile "/tpf/tp/source/t1700000" "t1700000_001_apo,t1700000_001_pgr,t1700000_001_pre"

  # echo "Status-$status"
  # echo "$output"

  # [ "$status" -eq 0 ]
  # [ "${lines[0]}" = "STATUS: Workspace t1700000_001_apo exist." ];
  # [ "${lines[1]}" = "STATUS: Workspace t1700000_001_pgr created." ];
  # [ "${lines[2]}" = "STATUS: Workspace t1700000_001_pre exist." ];

  # teardownWorkspace
# }

# @test "WORKSAPCE: Should return success for valid workspace creation" {
  # run $testfile "/tpf/tp/source/t1700000" "t1700000_001_apo,t1700000_001_pgr,t1700000_001_pre"

  # echo "Status-$status"
  # echo "$output"

  # [ "$status" -eq 0 ]
  # [ "${lines[0]}" = "STATUS: Workspace t1700000_001_apo created." ];
  # [ "${lines[1]}" = "STATUS: Workspace t1700000_001_pgr created." ];
  # [ "${lines[2]}" = "STATUS: Workspace t1700000_001_pre created." ];
# }

# @test "WORKSPACE: Should return success for exist workspace creation" {
  # run $testfile "/tpf/tp/source/t1700000" "t1700000_001_apo,t1700000_001_pgr,t1700000_001_pre"

  # echo "Status-$status"
  # echo "$output"

  # [ "$status" -eq 0 ]
  # [ "${lines[0]}" = "STATUS: Workspace t1700000_001_apo exist." ];
  # [ "${lines[1]}" = "STATUS: Workspace t1700000_001_pgr exist." ];
  # [ "${lines[2]}" = "STATUS: Workspace t1700000_001_pre exist." ];
# }

#
#-----------------------------------------------------------------------------#
# vim: filetype=bats

