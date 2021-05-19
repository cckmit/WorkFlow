/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.jenkins;

import static com.offbytwo.jenkins.helper.Range.build;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.Executable;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueItem;
import com.offbytwo.jenkins.model.QueueReference;
import com.tsi.workflow.User;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.interfaces.IJenkinsConfig;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

/**
 *
 * @author USER
 */
public class JenkinsClient {

    IJenkinsConfig jenkinsConfig;
    JenkinsServer lServer;
    private static final Logger LOG = Logger.getLogger(JenkinsClient.class.getName());

    public JenkinsClient() {
    }

    public JenkinsClient(IJenkinsConfig jenkinsConfig) throws IOException {
	try {
	    TrustAllTrustManager trustAllTrustManager = new TrustAllTrustManager();
	    SSLUtil sslUtil = new SSLUtil(trustAllTrustManager);
	    SSLContext lSSLContext = sslUtil.createSSLContext();
	    URI uri = new URI(jenkinsConfig.getURL());

	    CloseableHttpClient lBuilder = HttpClients.custom().setSSLContext(lSSLContext).addInterceptorFirst(new HttpRequestInterceptor() {

		@Override
		public void process(HttpRequest hr, HttpContext hc) throws HttpException, IOException {
		    hr.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((jenkinsConfig.getServiceUser() + ":" + jenkinsConfig.getServicePassword()).getBytes()));
		}
	    }).build();
	    JenkinsHttpClient lJenkinsHttpClient = new JenkinsHttpClient(uri, lBuilder);

	    lServer = new JenkinsServer(lJenkinsHttpClient);
	} catch (Exception ex) {
	    throw new IOException("Cannot create ssl socket");
	}
    }

    private Map<String, Job> getAllJobs() {
	try {
	    return lServer.getJobs();
	} catch (Exception ex) {
	    LOG.error("Error: on Getting Jobs List", ex);
	}
	return new HashMap<>();
    }

    private JobWithDetails getJobByName(String pJobName) {
	try {
	    return lServer.getJob(pJobName);
	} catch (IOException ex) {
	    LOG.error("Error: on Getting Job " + pJobName, ex);
	}
	return null;
    }

    private QueueItem getQueueItem(String pQueueUrl) {
	try {
	    return lServer.getQueueItem(new QueueReference(pQueueUrl));
	} catch (IOException ex) {
	    LOG.error("Error: on Getting Queue " + pQueueUrl, ex);
	}
	return null;
    }

    public Long getActualBuildNumber(String pQueueUrl) {
	QueueItem queueItem = getQueueItem(pQueueUrl);
	if (queueItem != null) {
	    Executable executable = queueItem.getExecutable();
	    if (executable != null) {
		return executable.getNumber();
	    }
	}
	return null;
    }

    public String getJobXMLByName(String pJobName) {
	try {
	    return lServer.getJobXml(pJobName);
	} catch (IOException ex) {
	    LOG.error("Error: on Getting Job XML " + pJobName, ex);
	}
	return null;
    }

    public void createJobByXML(String pJobName, String pXML) {
	try {
	    JobWithDetails job = getJobByName(pJobName);
	    if (job == null) {
		lServer.createJob(pJobName, pXML, true);
	    } else {
		LOG.warn(pJobName + " Job already Exist");
	    }
	} catch (IOException ex) {
	    LOG.error("Error: on Creating Job " + pJobName, ex);
	}
    }

    public void validateJob(String pJobName) {
	JobWithDetails job = getJobByName(pJobName);
	if (job == null) {
	    throw new WorkflowException(pJobName + " Job is not Exists");
	}
	if (!job.isBuildable()) {
	    throw new WorkflowException(pJobName + " Job is not Buildable");
	}
	// if (job.isInQueue()) {
	// throw new WorkflowException(pJobName + " Job is already in Queue");
	// }
    }

    public JenkinsBuild executeJob(User user, String pJobName, Map<String, String> pParams) throws WorkflowException {
	try {
	    JenkinsBuild jenkinsBuild = new JenkinsBuild(user);
	    JobWithDetails job = getJobByName(pJobName);
	    if (job == null) {
		throw new WorkflowException("Error: on Executing Job " + pJobName);
	    }
	    // int lBuildNumber = job.getNextBuildNumber();
	    QueueReference queueReference;
	    if (pParams == null || pParams.isEmpty()) {
		queueReference = job.build(true);
	    } else {
		queueReference = job.build(pParams, true);
	    }
	    long millis = job.getLastBuild().details().getTimestamp();
	    jenkinsBuild.setBuildTime(new Date(millis));
	    jenkinsBuild.setQueueUrl(queueReference.getQueueItemUrlPart());
	    jenkinsBuild.setJobName(pJobName);
	    jenkinsBuild.setBuildNumber(Integer.parseInt("-" + RandomStringUtils.randomNumeric(5)));
	    return jenkinsBuild;
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error: on Executing Job " + pJobName);
	}
    }

    public Boolean stopBuild(String pJobName, Integer pBuildNumber) throws IOException {
	Build build = lServer.getJob(pJobName).getBuildByNumber(pBuildNumber);
	if (build != null && build.details().isBuilding()) {
	    build.details().getClient().post(build.getUrl() + "stop", true);
	    return true;
	}
	return false;
    }

    public Boolean stopBuildInQueue(List<String> pQueueUrlList) throws IOException {
	for (String pQueueUrl : pQueueUrlList) {
	    QueueItem queueItem = lServer.getQueueItem(new QueueReference(pQueueUrl));
	    if (!queueItem.isCancelled() && queueItem.getExecutable() != null) {
		Build build = lServer.getBuild(queueItem);
		if (build != null && build.details().isBuilding()) {
		    build.details().getClient().post(build.getUrl() + "stop", true);
		    return true;
		}
	    }
	}
	return false;
    }

    public BuildResult getJobResult(String pJobName, Integer pBuildNumber) throws IOException {
	Build build = lServer.getJob(pJobName).getBuildByNumber(pBuildNumber);
	if (build != null) {
	    return build.details().getResult();
	}
	return null;
    }

    // public Long getPercentCompleted(String pJobName, Integer pBuildNumber) throws
    // IOException {
    // Build build = lServer.getJob(pJobName).getBuildByNumber(pBuildNumber);
    // if (build != null && build.details() != null && build.details().isBuilding())
    // {
    // return build.details().getDuration() / build.details().getEstimatedDuration()
    // * 100;
    // }
    // return 0L;
    // }
    //
    // public Long getPercentCompleted(String pQueueUrl) throws IOException {
    // QueueItem queueItem = lServer.getQueueItem(new QueueReference(pQueueUrl));
    // if (!queueItem.isCancelled() && queueItem.getExecutable() != null) {
    // Build build = lServer.getBuild(queueItem);
    // if (build != null && build.details().isBuilding()) {
    // return build.details().getDuration() / build.details().getEstimatedDuration()
    // * 100;
    // }
    // }
    // return 0L;
    // }
    public Integer getBuildNumber(String pQueueUrl) throws IOException {
	QueueItem queueItem = lServer.getQueueItem(new QueueReference(pQueueUrl));
	if (!queueItem.isCancelled()) {
	    return lServer.getBuild(queueItem).getNumber();
	}
	return 0;
    }

    public String getConsoleLog(String jobName, int buildNumber) throws IOException {
	Build build = lServer.getJob(jobName).getBuildByNumber(buildNumber);
	if (build != null) {
	    return build.details().getConsoleOutputText();
	}
	return "";
    }

    public Boolean isInQueue(String pJobName) {
	JobWithDetails job = getJobByName(pJobName);
	if (job == null) {
	    LOG.warn(pJobName + " Job is not Exists");
	    return false;
	}
	return job.isInQueue();
    }

    public Boolean isBuildable(String pJobName) {
	JobWithDetails job = getJobByName(pJobName);
	if (job == null) {
	    LOG.warn(pJobName + " Job is not Exists");
	    return false;
	}
	return job.isBuildable();
    }

    public Build getLastBuild(String pJobName) {
	JobWithDetails job = getJobByName(pJobName);
	if (job == null) {
	    LOG.warn(pJobName + " Job is not Exists");
	    return null;
	}
	return job.getLastBuild();
    }

}
