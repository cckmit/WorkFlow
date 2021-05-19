/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static com.tsi.workflow.utils.Constants.FILE_PATH_PATTERN;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.interfaces.ISystem;
import com.tsi.workflow.ssh.SSHUtil;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
@Service
public class TDBoxAuthService {

    private static final Logger LOG = Logger.getLogger(TDBoxAuthService.class.getName());
    @Autowired
    GITConfig gITConfig;

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public SSHUtil getSSHUtil() {
	return new SSHUtil(getGITConfig(), this);
    }

    public Boolean checkForKeys(User pUser, List<? extends ISystem> lSystems) {
	if (!isLocalRSAKeyExist(pUser)) {
	    return checkForKeysWithPassword(pUser, lSystems);
	} else {
	    return checkForKeysWithOutPassword(pUser, lSystems);
	}
    }

    private Boolean isLocalRSAKeyExist(User user, ISystem system) {
	Boolean lReturn = false;
	String filePath = getGITConfig().getSSHKeysPath() + getGITConfig().getGitHost() + "/" + user.getId() + "/" + system.getIpaddress() + "/";
	if (FILE_PATH_PATTERN.matcher(filePath).matches()) {
	    File lLocalBackupFile = new File(filePath, "id_rsa");
	    lReturn = lLocalBackupFile.exists();
	}
	if (!lReturn) {
	    LOG.warn("LOGIN: Local RSA Key for  " + user.getDisplayName() + " not exists in " + system.getName());
	} else {
	    LOG.info("LOGIN: Local RSA Key for  " + user.getDisplayName() + " exists in " + system.getName());
	}
	return lReturn;
    }

    private Boolean isLocalRSAKeyExist(User user) {
	Boolean lReturn = false;
	String filePath = getGITConfig().getSSHKeysPath() + getGITConfig().getGitHost() + "/" + user.getId() + "/" + getGITConfig().getGitHost() + "/";
	if (FILE_PATH_PATTERN.matcher(filePath).matches()) {
	    File lLocalBackupFile = new File(filePath, "id_rsa");
	    lReturn = lLocalBackupFile.exists();
	}
	if (!lReturn) {
	    LOG.warn("LOGIN: Local RSA Key for  " + user.getDisplayName() + " not exists in GIT HOST");
	} else {
	    LOG.info("LOGIN: Local RSA Key for  " + user.getDisplayName() + " exists in GIT HOST");
	}
	return lReturn;
    }

    private Boolean checkForKeysWithPassword(User pUser, List<? extends ISystem> lSystems) {
	LOG.info("LOGIN: Checking for Keys With Password for " + pUser.getDisplayName());
	try {
	    SSHUtil lGitHostUserShell = getSSHUtil();
	    if (!lGitHostUserShell.connectSSHWithPassword(pUser)) {
		return false;
	    }
	    if (!generateAndCopyKey(pUser, lGitHostUserShell, getGITConfig().getGitHost())) {
		return false;
	    }
	    if (!authorize(pUser.getId(), lGitHostUserShell, getGITConfig().getGitHost())) {
		return false;
	    }
	    StringBuilder lBuilder = new StringBuilder();
	    String lIPAddress = getIPAddress();
	    if (lIPAddress == null) {
		return false;
	    }
	    lBuilder.append(getGITConfig().getGitHost()).append(",").append(lIPAddress);

	    if (getGITConfig().getWfLoadBalancerHost() != null && !getGITConfig().getWfLoadBalancerHost().isEmpty()) {
		lIPAddress = InetAddress.getByName(getGITConfig().getWfLoadBalancerHost()).getHostAddress();
		lBuilder.append(" ").append(getGITConfig().getWfLoadBalancerHost()).append(",").append(lIPAddress);
	    }

	    for (ISystem lSystem : lSystems) {
		lIPAddress = InetAddress.getByName(lSystem.getIpaddress()).getHostAddress();
		lBuilder.append(" ").append(lSystem.getIpaddress()).append(",").append(lIPAddress);
	    }

	    if (!addKnownHosts(lGitHostUserShell, lBuilder.toString())) {
		return false;
	    }
	    for (ISystem lSystem : lSystems) {
		if (!mapKeys(pUser, lSystem, lGitHostUserShell, lBuilder.toString())) {
		    return false;
		}
	    }

	    if (!addGitblitKey(lGitHostUserShell, pUser)) {
		return false;
	    }
	    lGitHostUserShell.executeCommand(Constants.SystemScripts.SSH_PERMISSION.getScript());
	    lGitHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_USER.getScript() + "\"" + pUser.getDisplayName() + "\"");
	    lGitHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_MAIL.getScript() + pUser.getMailId());
	    lGitHostUserShell.disconnectSSH();
	    return true;
	} catch (Exception ex) {
	    LOG.error("Unable resolve the IP Address", ex);
	} finally {
	    LOG.info("LOGIN: Checking for Keys With Password completed for " + pUser.getDisplayName());
	}
	return false;
    }

    private Boolean checkForKeysWithOutPassword(User pUser, List<? extends ISystem> lSystems) {
	LOG.info("LOGIN: Checking for Keys Without Password for " + pUser.getDisplayName());
	SSHUtil lGitHostUserShell = getSSHUtil();
	try {
	    if (!lGitHostUserShell.connectSSH(pUser)) {
		return false;
	    }
	    if (!authorize(pUser.getId(), lGitHostUserShell, getGITConfig().getGitHost())) {
		return false;
	    }
	    StringBuilder lBuilder = new StringBuilder();
	    String lIPAddress = getIPAddress();
	    if (lIPAddress == null) {
		return false;
	    }
	    lBuilder.append(getGITConfig().getGitHost()).append(",").append(lIPAddress);

	    if (getGITConfig().getWfLoadBalancerHost() != null && !getGITConfig().getWfLoadBalancerHost().isEmpty()) {
		lIPAddress = InetAddress.getByName(getGITConfig().getWfLoadBalancerHost()).getHostAddress();
		lBuilder.append(" ").append(getGITConfig().getWfLoadBalancerHost()).append(",").append(lIPAddress);
	    }

	    for (ISystem lSystem : lSystems) {
		lIPAddress = InetAddress.getByName(lSystem.getIpaddress()).getHostAddress();
		lBuilder.append(" ").append(lSystem.getIpaddress()).append(",").append(lIPAddress);
	    }

	    if (!addKnownHosts(lGitHostUserShell, lBuilder.toString())) {
		return false;
	    }
	    for (ISystem lSystem : lSystems) {
		if (!mapKeys(pUser, lSystem, lGitHostUserShell, lBuilder.toString())) {
		    return false;
		}
	    }

	    if (!addGitblitKey(lGitHostUserShell, pUser)) {
		return false;
	    }
	    lGitHostUserShell.executeCommand(Constants.SystemScripts.SSH_PERMISSION.getScript());
	    lGitHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_USER.getScript() + "\"" + pUser.getDisplayName() + "\"");
	    lGitHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_MAIL.getScript() + pUser.getMailId());
	    lGitHostUserShell.disconnectSSH();
	    return true;
	} catch (Exception ex) {
	    LOG.error("Unable resolve the IP Address", ex);
	} finally {
	    LOG.info("LOGIN: Checking for Keys Without Password Completed for " + pUser.getDisplayName());
	}
	return false;
    }

    private Boolean addKnownHosts(SSHUtil lShell, String pHosts) {
	LOG.info("LOGIN: Adding Host for user " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	JSONResponse lResponse = lShell.executeCommand("ssh-keyscan -t rsa,dsa,ecdsa,ed25519 -p " + getGITConfig().getGitSshPort() + " " + pHosts + " >> ~/.ssh/known_hosts");
	if (!lResponse.getStatus()) {
	    return false;
	}
	lResponse = lShell.executeCommand("ssh-keyscan -t rsa,dsa,ecdsa,ed25519 -p " + getGITConfig().getGitDataPort() + " " + pHosts + " >> ~/.ssh/known_hosts");
	if (!lResponse.getStatus()) {
	    return false;
	}
	lResponse = lShell.executeCommand("sort -u .ssh/known_hosts -o .ssh/known_hosts");
	if (!lResponse.getStatus()) {
	    return false;
	}
	return true;
    }

    private Boolean mapKeys(User pUser, ISystem lSystem, SSHUtil lShell, String pHosts) {
	if (!isLocalRSAKeyExist(pUser, lSystem)) {
	    return mapKeysWithPassword(pUser, lShell, lSystem, pHosts);
	} else {
	    return mapKeysWithOutPassword(pUser, lShell, lSystem, pHosts);
	}
    }

    private Boolean mapKeysWithPassword(User pUser, SSHUtil lShell, ISystem lSystem, String pHosts) {
	LOG.info("LOGIN: Mapping for Keys With Password for " + pUser.getDisplayName() + ", System : " + lSystem.getName());
	SSHUtil lSystemHostUserShell = getSSHUtil();
	try {
	    if (!lSystemHostUserShell.connectSSHWithPassword(pUser, lSystem)) {
		return false;
	    }
	    if (!generateAndCopyKey(pUser, lSystemHostUserShell, lSystem.getIpaddress())) {
		return false;
	    }
	    if (!addKnownHosts(lSystemHostUserShell, pHosts)) {
		return false;
	    }
	    if (!authorize(pUser.getId(), lSystemHostUserShell, getGITConfig().getGitHost())) {
		return false;
	    }
	    if (!authorize(pUser.getId(), lShell, lSystem.getIpaddress())) {
		return false;
	    }
	    lSystemHostUserShell.executeCommand(Constants.SystemScripts.SSH_PERMISSION.getScript());
	    lSystemHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_USER.getScript() + "\"" + pUser.getDisplayName() + "\"");
	    lSystemHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_MAIL.getScript() + pUser.getMailId());
	    lSystemHostUserShell.disconnectSSH();
	} finally {
	    LOG.info("LOGIN: Mapping for Keys With Password completed for " + pUser.getDisplayName() + ", System : " + lSystem.getName());
	}
	return true;
    }

    private Boolean mapKeysWithOutPassword(User pUser, SSHUtil pGITSystemShell, ISystem lSystem, String pHosts) {
	LOG.info("LOGIN: Mapping for Keys Without Password for " + pUser.getDisplayName() + ", System : " + lSystem.getName());
	SSHUtil lSystemHostUserShell = getSSHUtil();
	try {
	    if (!lSystemHostUserShell.connectSSH(pUser, lSystem)) {
		return false;
	    }
	    if (!addKnownHosts(lSystemHostUserShell, pHosts)) {
		return false;
	    }
	    if (!authorize(pUser.getId(), lSystemHostUserShell, getGITConfig().getGitHost())) {
		return false;
	    }
	    if (!authorize(pUser.getId(), pGITSystemShell, lSystem.getIpaddress())) {
		return false;
	    }
	    lSystemHostUserShell.executeCommand(Constants.SystemScripts.SSH_PERMISSION.getScript());
	    lSystemHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_USER.getScript() + "\"" + pUser.getDisplayName() + "\"");
	    lSystemHostUserShell.executeCommand(Constants.SystemScripts.GIT_CONFIG_MAIL.getScript() + pUser.getMailId());
	    lSystemHostUserShell.disconnectSSH();
	} finally {
	    LOG.info("LOGIN: Mapping for Keys Without Password completed for " + pUser.getDisplayName() + ", System : " + lSystem.getName());
	}
	return true;
    }

    private Boolean addGitblitKey(SSHUtil lSSHUtil, User pUser) {
	LOG.info("LOGIN: Adding Gitblit Key for  " + lSSHUtil.getConnectedUser() + " in " + lSSHUtil.getConnectedHost());
	JSONResponse lResult = lSSHUtil.executeCommand("export MTP_GIT_PASS=" + pUser.getDecryptedPassword().replaceAll("\\$", "\\\\\\$") + "; " + Constants.SystemScripts.LOGIN.getScript() + getGITConfig().getGitHost() + " " + getGITConfig().getGitDataPort() + " " + pUser.getId(), true);
	return lResult.getStatus();
    }

    private Boolean generateAndCopyKey(User pUser, SSHUtil lShell, String pHost) {
	File lLocalBackupFile;
	String filePath = getGITConfig().getSSHKeysPath() + getGITConfig().getGitHost() + "/" + pUser.getId() + "/" + pHost + "/";
	lLocalBackupFile = new File(filePath, "id_rsa");
	if (!lLocalBackupFile.exists()) {
	    LOG.info("LOGIN: Reading RSA Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	    JSONResponse lResponse = lShell.executeCommand("cat ~/.ssh/id_rsa");

	    if (!lResponse.getStatus()) {
		LOG.info("LOGIN: Generating New RSA Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
		lResponse = lShell.executeCommand("ssh-keygen -t rsa -b 4096 -q -N \"\" -f ~/.ssh/id_rsa");
		if (lResponse.getStatus()) {
		    lResponse = lShell.executeCommand("cat ~/.ssh/id_rsa");
		}
	    }

	    if (!lResponse.getStatus()) {
		LOG.error("LOGIN: Eror in reading RSA Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
		return false;
	    }

	    try {
		LOG.info("LOGIN: Writing RSA Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost() + " to Local");
		FileUtils.writeStringToFile(lLocalBackupFile, lResponse.getData().toString(), "UTF-8");
	    } catch (IOException ex) {
		LOG.error("Error in writing File", ex);
	    }

	    LOG.info("LOGIN: Reading RSA PUB Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	    lLocalBackupFile = new File(filePath, "id_rsa.pub");
	    lResponse = lShell.executeCommand("cat ~/.ssh/id_rsa.pub");

	    if (!lResponse.getStatus()) {
		return false;
	    }

	    try {
		LOG.info("LOGIN: Writing RSA PUB Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost() + " to Local");
		FileUtils.writeStringToFile(lLocalBackupFile, lResponse.getData().toString(), "UTF-8");
	    } catch (IOException ex) {
		LOG.error("Error in writing File", ex);
	    }

	    LOG.info("LOGIN: Reading RSA ECDSA Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	    lLocalBackupFile = new File(filePath, "ssh_host_ecdsa_key.pub");
	    List<String> lines = new ArrayList<>();
	    lResponse = lShell.executeCommand("echo $(hostname -s),$(hostname -I) $(cat /etc/ssh/ssh_host_ecdsa_key.pub)");

	    if (!lResponse.getStatus()) {
		return false;
	    }

	    lines.add(lResponse.getData().toString());

	    lResponse = lShell.executeCommand("echo $(hostname),$(hostname -I) $(cat /etc/ssh/ssh_host_ecdsa_key.pub)");

	    if (!lResponse.getStatus()) {
		return false;
	    }

	    lines.add(lResponse.getData().toString());

	    try {
		LOG.info("LOGIN: Writing RSA ECDSA Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost() + " to Local");
		FileUtils.writeLines(lLocalBackupFile, lines);
	    } catch (IOException ex) {
		LOG.error("Error in writing File", ex);
	    }
	}
	return true;
    }

    private Boolean authorize(String pUserName, SSHUtil pShell, String pHost) {
	String keyPath = getGITConfig().getSSHKeysPath() + getGITConfig().getGitHost() + "/" + pUserName + "/" + pHost + "/";
	File lKey = null;
	if (FILE_PATH_PATTERN.matcher(keyPath).matches()) {
	    lKey = new File(keyPath, "id_rsa.pub");
	} else {
	    return false;
	}
	String hostKeyPath = getGITConfig().getSSHKeysPath() + getGITConfig().getGitHost() + "/" + pUserName + "/" + pHost + "/";
	File lHost = null;
	if (FILE_PATH_PATTERN.matcher(hostKeyPath).matches()) {
	    lHost = new File(hostKeyPath, "ssh_host_ecdsa_key.pub");
	} else {
	    return false;
	}
	List<String> lKeyFileContent;
	try {
	    lKeyFileContent = FileUtils.readLines(lKey);
	    for (String lContent : lKeyFileContent) {
		if (!lContent.isEmpty()) {
		    if (!checkAuthorisedKeyPresent(pShell, lContent)) {
			if (!addAuthorisedKeyPresent(pShell, lContent)) {
			    return false;
			}
		    }
		}
	    }
	    lKeyFileContent = FileUtils.readLines(lHost);
	    for (String lContent : lKeyFileContent) {
		if (!lContent.isEmpty()) {
		    if (!checkAuthorisedHostPresent(pShell, lContent)) {
			if (!addAuthorisedHostPresent(pShell, lContent)) {
			    return false;
			}
		    }
		}
	    }
	    return true;
	} catch (IOException ex) {
	    LOG.error("Exception in Authenticating Keys", ex);
	}
	return false;
    }

    private Boolean checkAuthorisedKeyPresent(SSHUtil lShell, String pKey) {
	LOG.info("LOGIN: Checking Authorised Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	JSONResponse lResult = lShell.executeCommand("cat ~/.ssh/authorized_keys | grep -i \"" + pKey + "\"");
	return lResult.getStatus();
    }

    private Boolean checkAuthorisedHostPresent(SSHUtil lShell, String pKey) {
	LOG.info("LOGIN: Checking Authorised Host for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	JSONResponse lResult = lShell.executeCommand("cat ~/.ssh/known_hosts | grep -i \"" + pKey + "\"");
	return lResult.getStatus();
    }

    private Boolean addAuthorisedHostPresent(SSHUtil lShell, String pKey) {
	LOG.info("LOGIN: Adding Authorised Host for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	JSONResponse lResult = lShell.executeCommand("echo " + pKey + " >> ~/.ssh/known_hosts");
	return lResult.getStatus();
    }

    private Boolean addAuthorisedKeyPresent(SSHUtil lShell, String pKey) {
	LOG.info("LOGIN: Adding Authorised Key for  " + lShell.getConnectedUser() + " in " + lShell.getConnectedHost());
	JSONResponse lResult = lShell.executeCommand("echo \"" + pKey + "\" >> ~/.ssh/authorized_keys");
	return lResult.getStatus();
    }

    // private boolean addAuthorisedKey(SSHUtil lShell, String pPassword, String
    // pRemoteHostName, Integer pPort) {
    // JSONResponse lResult = lShell.executeCommand("sshpass -p " + pPassword + "
    // ssh-copy-id -o StrictHostKeyChecking=no -i ~/.ssh/id_rsa " +
    // lShell.getConnectedUser() + "@" + pRemoteHostName + " -p " + pPort);
    // return lResult.getStatus();
    // }
    private String getIPAddress() throws UnknownHostException, SocketException {
	Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
	for (NetworkInterface netint : Collections.list(nets)) {
	    if (netint.getName().startsWith("eth0")) {
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
		    if (inetAddress instanceof Inet4Address) {
			return inetAddress.getHostAddress();
		    }
		}
	    }
	}
	return null;
    }
}
