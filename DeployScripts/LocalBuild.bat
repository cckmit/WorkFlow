@ECHO off
ECHO [INFO] ------------------------------------------------------------------------
CD ..

IF /I "%WF_DEV_HOME%"=="" (
	SETX WF_DEV_HOME %CD%
	SET WF_DEV_HOME=%CD%
) ELSE (
	ECHO [INFO] Project path: %WF_DEV_HOME%
)

IF /I "%WFW_ENV%"=="" (
	MKDIR C:\opt\workflow
	MKDIR C:\var\log\workflow
	SETX WFW_ENV C:\opt\workflow
) ELSE (
	ECHO [INFO] Project data path: %WFW_ENV%
)

IF /I "%MTP_ENV%"=="" (
	MKDIR C:\opt\mtp
	SETX MTP_ENV C:\opt\mtp
) ELSE (
	ECHO [INFO] Project script path: %MTP_ENV%
)

IF /I "%JAVA_HOME%"=="" (
	SET /p JAVA="Enter Oracle JDK Path:"
) ELSE (
	ECHO [INFO] Oracle JDK path: %JAVA_HOME%
)

IF NOT "%JAVA%" == "" (
	SETX JAVA_HOME "%JAVA%"
	SET JAVA_HOME="%JAVA%"
) ELSE IF /I "%JAVA_HOME%"=="" (
	ECHO [ERROR] Oracle JDK required to do local deploy, please refer README.md file
	GOTO CASE_FAIL
)

IF /I "%CATALINA_HOME%"=="" (
	SET /p TOMCAT="Enter Tomcat Path:"
) ELSE (
	ECHO [INFO] Tomcat path: %CATALINA_HOME%
)

IF NOT "%TOMCAT%" == "" (
	SETX CATALINA_HOME %TOMCAT%
	SET CATALINA_HOME=%TOMCAT%
) ELSE IF /I "%CATALINA_HOME%"=="" (
	ECHO [ERROR] Tomcat Required to do local deploy, please refer README.md file
	GOTO CASE_FAIL
)

IF /I "%MAVEN_HOME%"=="" (
	SET /p MAVEN="Enter Maven Path : "
) ELSE (
	ECHO [INFO] Maven path: %MAVEN_HOME%
)

IF NOT "%MAVEN%" == "" (
	SETX MAVEN_HOME %MAVEN%
	SET MAVEN_HOME=%MAVEN%
) ELSE IF /I "%MAVEN_HOME%"=="" (
	ECHO [ERROR] Maven Required to do local build, please refer README.md file
	GOTO CASE_FAIL
)

ECHO [INFO] --------------------------[ Menu ]--------------------------------------

SET /p input="[MENU] Enter 1. Clean Only, 2. Build & Deploy, 3. Deploy Only, 4. Exit : "

2>NUL CALL :CASE_%input%
IF ERRORLEVEL 1 CALL :CASE_FAIL

ECHO [INFO] --------------------------[ Completed ]---------------------------------
EXIT

:CASE_1
:CASE_2
	ECHO [INFO] --------------------------[ 1. Clean Only ]-----------------------------
	REM Clearing the Old File
	CD %WF_DEV_HOME%
	ECHO [INFO] Setup maven proxy
	MKDIR %USERPROFILE%\.m2
	COPY %WF_DEV_HOME%\DeployScripts\settings.xml %USERPROFILE%\.m2
	ECHO [INFO] Switch to %WF_DEV_HOME%
	ECHO "" > C:\var\log\workflow\workflow.log
	ECHO [INFO] Cleared workflow.log ...
	DEL %MTP_ENV%\app.properties
	ECHO [INFO] Removed workflow app.properties ...
	RMDIR %CATALINA_HOME%\webapps\JGitAPI /S /Q
	RMDIR %CATALINA_HOME%\webapps\WorkFlowAPI /S /Q
	RMDIR %CATALINA_HOME%\webapps\WorkFlow /S /Q
	DEL %CATALINA_HOME%\webapps\*.war
	DEL %CATALINA_HOME%\conf\Catalina\localhost\*.xml
	ECHO [INFO] Cleaned tomcat webapps container ...
	IF "%input%" == "1" (
		CALL %MAVEN_HOME%\bin\mvn.cmd -PTP_DEV resources:copy-resources@localCommitCheck clean impsort:sort formatter:format exec:exec@reset-config
		ECHO [INFO] Cleaned maven local workspace ...
	)	
	DEL %WF_DEV_HOME%\deployment\localProfile\*.properties
	ECHO [INFO] Cleaned local profiles at local workspace ...
	CALL %MAVEN_HOME%\bin\mvn.cmd -Dfile=%WF_DEV_HOME%\Derived\TOSIBM\TOSApi_1.2.6.jar -DgroupId=com.ibm.tpf -DartifactId=TOSApi -Dversion=1.2.06 -Dpackaging=jar -Dfile.encoding=UTF-8 --non-recursive install:install-file
	ECHO [INFO] Installed TOS Library ...
	ECHO [INFO] ------------------------------------------------------------------------
	IF "%input%" == "1" (
		GOTO END_CASE
	)
	REM Local BUILD
	ECHO [INFO] ------------------------[ 2. Build and Deploy ]--------------------------
	for /f "delims=[] tokens=2" %%a in ('ping -4 -n 1 %ComputerName% ^| findstr [') do set NetworkIP=%%a
	ECHO [INFO] Compile and creating artefacts ...
	CALL %MAVEN_HOME%\bin\mvn.cmd -Dbuild.number=404 -Dwf.api.url=http://%NetworkIP%:8080/WorkFlowAPI -DskipTests=true -PTP_DEV resources:copy-resources@localCommitCheck impsort:sort formatter:format clean install exec:exec@reset-config
	IF ERRORLEVEL 1 GOTO CASE_FAIL
	ECHO [INFO] ------------------------------------------------------------------------
:CASE_3
	REM Local Config Generation
	ECHO [INFO] Generate local config ...
	CALL %MAVEN_HOME%\bin\mvn.cmd -Ddb.app.username=workflow -Ddb.app.password=ENC(4E6575BE2A2BCB3EDEF5AB0FFFFBFB8D7A4623F20AAEA645) -Ddb.ticket.username=SVCSDM15 -Ddb.ticket.password=ENC(B6A77DAA085EB74E43131E0DA5BE340480E2056F92655BB9) -Ddb.csr.username=SVCMSPZTPFM -Ddb.csr.password=ENC(7F75B407545EA690A2A101B6F5A34456135902FCA9835F4B) -Ddb.vpar.username=CTI001 -Ddb.vpar.password=ENC(C76BF46801AD0C9A88E3B0524F8157D57086C44ED4C362F5) -Dservice.username=mtpservice -Dservice.password=ENC(A24D584F9DC0F0709BC7D0DD2FF026AF0D934CFAF8508003789C8DF41ED76281) -Dtos.file.domain=galileo -Dtos.file.username=svcTOSdata -Dtos.file.password=ENC(E6C711AAFC156E4804EB1832F6D8E6553F4B954E7563F060) -DskipTests=true -PlocalProfile resources:copy-resources@localCommitCheck -N generate-resources resources:copy-resources@localConfigDeploy
	IF ERRORLEVEL 1 GOTO CASE_FAIL
	REM Local Deploy
	ECHO [INFO] -------------------------[ 3. Deploy Only ]-----------------------------
	ECHO [INFO] Local Tomcat deploy ...
	COPY %WF_DEV_HOME%\deployment\localProfile\app.properties %MTP_ENV%
	COPY %WF_DEV_HOME%\API\target\WorkFlowAPI-1.29.war %CATALINA_HOME%\webapps\WorkFlowAPI.war
	COPY %WF_DEV_HOME%\UI\target\WorkFlowUI-1.29.war %CATALINA_HOME%\webapps\WorkFlow.war
	REM Start Tomcat
	ECHO [INFO] Local Tomcat startup ...
	SET CATALINA_OPTS=-Duser.timezone=GMT
	CALL %CATALINA_HOME%\bin\startup.bat 
	IF ERRORLEVEL 1 GOTO CASE_FAIL
	ECHO [INFO] ------------------------------------------------------------------------
	GOTO END_CASE
:CASE_FAIL
	ECHO ******************************** BUILD FAILURE ********************************
	PAUSE
	EXIT /b 1
:END_CASE
	VER > NUL
	GOTO :EOF
