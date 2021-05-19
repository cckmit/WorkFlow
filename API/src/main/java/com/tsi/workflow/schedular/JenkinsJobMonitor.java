package com.tsi.workflow.schedular;

import com.tsi.workflow.schedular.jenkins.DEVLBuildMonitor;
import com.tsi.workflow.schedular.jenkins.DEVLLoaderMonitor;
import com.tsi.workflow.schedular.jenkins.FallBackBuildMonitor;
import com.tsi.workflow.schedular.jenkins.OnlineBuildMonitor;
import com.tsi.workflow.schedular.jenkins.STAGEBuildMonitor;
import com.tsi.workflow.schedular.jenkins.STAGELoaderMonitor;
import com.tsi.workflow.schedular.jenkins.STAGEWorkspaceCreationMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class JenkinsJobMonitor {

    @Autowired
    DEVLBuildMonitor dEVLBuildMonitor;
    @Autowired
    DEVLLoaderMonitor dEVLLoaderMonitor;
    @Autowired
    STAGEBuildMonitor sTAGEBuildMonitor;
    @Autowired
    STAGELoaderMonitor sTAGELoaderMonitor;
    @Autowired
    STAGEWorkspaceCreationMonitor sTAGEWorkspaceCreationMonitor;
    @Autowired
    OnlineBuildMonitor onlineBuildMonitor;
    @Autowired
    FallBackBuildMonitor fallBackBuildMonitor;

    // @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    public void doMonitor() {
	checkIsRunning();
    }

    public void checkIsRunning() {
	dEVLBuildMonitor.doMonitor();
	dEVLLoaderMonitor.doMonitor();
	sTAGEBuildMonitor.doMonitor();
	sTAGELoaderMonitor.doMonitor();
	sTAGEWorkspaceCreationMonitor.doMonitor();
	onlineBuildMonitor.doMonitor();
	fallBackBuildMonitor.doMonitor();
    }
}
