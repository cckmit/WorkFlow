package com.tsi.workflow.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.tsi.workflow.AuthSystem;
import com.tsi.workflow.User;
import com.tsi.workflow.interfaces.IGitConfig;
import com.tsi.workflow.interfaces.ISystem;
import com.tsi.workflow.service.TDBoxAuthService;
import com.tsi.workflow.utils.JSONResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class SSHUtil {

    private static final Logger LOG = Logger.getLogger(SSHUtil.class.getName());
    JSch lJsch = null;
    Session lSession = null;
    IGitConfig config;
    String connectedUser;
    String connectedHost;
    TDBoxAuthService lTDBoxAuthService;

    public SSHUtil(IGitConfig config, TDBoxAuthService pTDBoxAuthService) {
	lJsch = new JSch();
	this.config = config;
	this.lTDBoxAuthService = pTDBoxAuthService;
    }

    public String getConnectedUser() {
	return connectedUser;
    }

    public String getConnectedHost() {
	return connectedHost;
    }

    private void setLogValue(String host, String user) {
	connectedUser = user;
	connectedHost = host;
    }

    private ISystem getGITHOST() {
	return new AuthSystem(config.getGitHost(), config.getGitSshPort(), "GIT HOST");
    }

    private User getServiceUser() {
	User user = new User(config.getServiceUserID());
	user.setDisplayName("MTPSERVICE");
	user.setEncryptedPassword(config.getServiceSecret());
	return user;
    }

    // <editor-fold defaultstate="collapsed" desc="Connect Methods">
    public Boolean connectSSH(ISystem pSystem) {
	LOG.info("SSH: Creating Connection to " + pSystem.getName() + " using Service ID");
	setLogValue(pSystem.getName(), "Service ID");
	return baseConnectSSH(getServiceUser(), pSystem);
    }

    public Boolean connectSSH(User user, ISystem pSystem) {
	User lUser = user.getCurrentOrDelagateUser();
	LOG.info("SSH: Creating Connection to " + pSystem.getName() + " using " + lUser.getDisplayName());
	setLogValue(pSystem.getName(), lUser.getDisplayName());
	return baseConnectSSH(lUser, pSystem);
    }

    public Boolean connectSSH(User user) {
	User lUser = user.getCurrentOrDelagateUser();
	LOG.info("SSH: Creating Connection to GIT Host using " + lUser.getDisplayName());
	setLogValue("GIT Host", lUser.getDisplayName());
	return baseConnectSSH(lUser, getGITHOST());
    }

    public Boolean connectSSH() {
	LOG.info("SSH: Creating Connection to GIT Host using Service ID");
	setLogValue("GIT Host", "Service ID");
	return baseConnectSSH(getServiceUser(), getGITHOST());
    }

    public Boolean connectSSHWithPassword(ISystem pSystem) {
	LOG.info("SSH: Creating Connection to " + pSystem.getName() + " using Service ID");
	setLogValue(pSystem.getName(), "Service ID");
	return baseConnectSSHWithPassword(getServiceUser(), pSystem);
    }

    public Boolean connectSSHWithPassword(User user, ISystem pSystem) {
	User lUser = user.getCurrentOrDelagateUser();
	LOG.info("SSH: Creating Connection to " + pSystem.getName() + " using " + lUser.getDisplayName());
	setLogValue(pSystem.getName(), lUser.getDisplayName());
	return baseConnectSSHWithPassword(lUser, pSystem);
    }

    public Boolean connectSSHWithPassword(User user) {
	User lUser = user.getCurrentOrDelagateUser();
	LOG.info("SSH: Creating Connection to GIT Host using " + lUser.getDisplayName());
	setLogValue("GIT Host", lUser.getDisplayName());
	return baseConnectSSHWithPassword(lUser, getGITHOST());
    }

    private Boolean baseConnectSSH(User user, ISystem pSystem) {
	boolean lReturn = true;
	for (int i = 0; i < 3; i++) {
	    try {
		lReturn = true;
		String serverMapped = config.getSSHKeysPath() + config.getGitHost() + "/" + user.getId() + "/" + pSystem.getIpaddress() + "/" + "id_rsa";
		String privateKeyPath = config.getSSHKeysPath() + config.getGitHost() + "/" + user.getId() + "/" + config.getGitHost() + "/" + "id_rsa";
		if (!new File(serverMapped).exists()) {
		    LOG.info("File not exist so Key Mapping Started for " + pSystem.getName());
		    List<ISystem> lList = new ArrayList<>();
		    lList.add(pSystem);
		    lTDBoxAuthService.checkForKeys(user, lList);
		}
		lJsch.removeAllIdentity();
		lJsch.addIdentity(privateKeyPath);
		lSession = lJsch.getSession(user.getId(), pSystem.getIpaddress(), pSystem.getPortno());
		lSession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		java.util.Properties propsConfig = new java.util.Properties();
		propsConfig.put("StrictHostKeyChecking", "no");
		lSession.setConfig(propsConfig);
		lSession.connect(30000);
		break;
	    } catch (JSchException ex) {
		LOG.error("Error in Connecting SSH with " + pSystem.getName() + " for User " + user.getDisplayName(), ex);
		lReturn = false;
	    }
	}
	return lReturn;
    }

    private Boolean baseConnectSSHWithPassword(User user, ISystem pSystem) {
	boolean lReturn = true;
	for (int i = 0; i < 3; i++) {
	    try {
		lReturn = true;
		lJsch.removeAllIdentity();
		lSession = lJsch.getSession(user.getId(), pSystem.getIpaddress(), pSystem.getPortno());
		lSession.setConfig("StrictHostKeyChecking", "no");
		lSession.setUserInfo(new WFUserInfo(user.getDecryptedPassword()));
		lSession.setPassword(user.getDecryptedPassword());
		lSession.connect(30000);
		break;
	    } catch (JSchException ex) {
		LOG.error("Error in Connecting SSH with " + pSystem.getName() + " for User " + user.getDisplayName(), ex);
		lReturn = false;
	    }
	}
	return lReturn;
    }

    public void disconnectSSH() {
	if (lSession != null) {
	    lSession.disconnect();
	    LOG.info("SSH: Disconnecting User: " + getConnectedUser() + " Host: " + getConnectedHost());
	}
    }
    // </editor-fold>

    public JSONResponse executeCommand(String command) {
	return executeCommand(command, false);
    }

    public JSONResponse executeCommand(String command, Boolean isSecure) {
	StringBuilder output = new StringBuilder();
	int status = -1;
	JSONResponse lResponseData = new JSONResponse();
	ChannelExec lChannel = null;
	long lStartDate = System.currentTimeMillis();
	try {
	    if (!isSecure) {
		LOG.info("SSH: User: " + getConnectedUser() + " Host: " + getConnectedHost() + " Command: " + command);
	    } else {
		LOG.info("SSH: User: " + getConnectedUser() + " Host: " + getConnectedHost() + " Command: SECURED");
	    }

	    lChannel = (ChannelExec) lSession.openChannel("exec");
	    BufferedReader lInputStream = new BufferedReader(new InputStreamReader(lChannel.getInputStream()));
	    lChannel.setCommand(command);
	    lChannel.connect();

	    String msg = "";
	    while ((msg = lInputStream.readLine()) != null) {
		output.append(msg).append(System.lineSeparator());
	    }
	    while (!lChannel.isClosed()) {

	    }
	    lInputStream.close();
	    status = lChannel.getExitStatus();
	    if (status == 0) {
		lResponseData.setStatus(Boolean.TRUE);
		lResponseData.setData(output.toString());
	    } else {
		lResponseData.setStatus(Boolean.FALSE);
		lResponseData.setErrorMessage("Error Code: " + status + " " + output.toString().trim());
		lResponseData.setCount(status);
		LOG.error("Error Code: " + status + " " + output.toString().trim());
	    }
	} catch (Exception ex) {
	    LOG.error("Error in Communicating in SSH", ex);
	    lResponseData.setStatus(Boolean.FALSE);
	    lResponseData.setErrorMessage("Error in Communicating in SSH");
	} finally {
	    if (!isSecure) {
		LOG.info("SSH: User: " + getConnectedUser() + " Host: " + getConnectedHost() + " Command: " + command + " / " + (System.currentTimeMillis() - lStartDate) + "ms");
	    } else {
		LOG.info("SSH: User: " + getConnectedUser() + " Host: " + getConnectedHost() + " Command: SECURED" + " / " + (System.currentTimeMillis() - lStartDate) + "ms");
	    }
	    if (lChannel != null) {
		lChannel.disconnect();
	    }
	}
	return lResponseData;
    }

    public static class WFUserInfo implements UserInfo {

	private String password;

	public WFUserInfo(String password) {
	    this.password = password;
	}

	@Override
	public String getPassword() {
	    return this.password;
	}

	@Override
	public boolean promptYesNo(String str) {
	    return true;
	}

	@Override
	public String getPassphrase() {
	    return "";
	}

	@Override
	public boolean promptPassphrase(String message) {
	    return true;
	}

	@Override
	public boolean promptPassword(String message) {
	    return true;
	}

	@Override
	public void showMessage(String message) {
	    LOG.info("message: " + message);
	}

    }
}
