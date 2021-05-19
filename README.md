# z/TPF DevOps ToolChain (ZTPFM)

## IDE Support Setup - Visual Studio Code
1. Install below the listed softwares to support development
    - Install __Oracle JDK v1.8__ from [Oracle site](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
        - Setup Java Development environment variables in Windows (_Note: X - version refernces_)
            > JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx

            > JDK_HOME=C:\Program Files\Java\jdk1.8.0_xxx

            > JRE_JOME=C:\Program Files\Java\jdk1.8.0_xxx\jre

    - Install __Apache maven__ stable version from [Maven site](https://maven.apache.org/download.cgi) under C:\apache directory
        - Setup Apache Maven environment variables in Windows (_Note: X - version refernces_)
            > M2_HOME=C:\apache\apache-maven-x.x.x

            > MAVEN_HOME=C:\apache\apache-maven-x.x.x

    - Install __Apache Tomcat v8__ from [Tomcat site](https://tomcat.apache.org/download-80.cgi) under C:\apache directory
        - Setup Default Apache Tomcat environment variables in Windows (_Note: X - version refernces_)
            > CATALINA_HOME=C:\apache\apache-tomcat-8.x.x

    - Install __ShellCheck__ stable version under c:\shellcheck directory
        - Download from [Shellcheck](https://storage.googleapis.com/shellcheck/shellcheck-stable.zip)
        - Extract shellcheck-stable.zip and then move to C:\ location, this path looks like - _C:\\shellcheck-stable\\shellcheck-stable.exe_

    - Install __Git__ stable version from [Git SCM](https://git-scm.com/download/win)
        - Setup your name with exact LDAP reference of your gecos name (_Note: User Name - Your full name_)
            > git config --global user.name "Arul Dhandapani"

        - Setup your mail address with exact LDAP reference of your travelport e-mail name
            > git config --global user.email "arul.dhandapani@travelport.com"

        - Setup windows environment supports
            > git config --global core.autocrlf true

            > git config --global core.longpaths true

        - Verify your setup
            > git config --global --list

2. Setup Workflow application development environment
    - Create data and logs directory for application to be run in your Windows system
        > mkdir -p /c/var/log/workflow

        > mkdir -p /c/opt/mtp

        > mkdir -p /c/opt/workflow

    - Setup shell scripts environment variables in Windows
        >MTP_ENV=C:\opt\mtp

    - Setup Workflow data path variables in Windows environment (_Note: This path should be your project directory_)
        >WF_DEV_HOME=C:\Users\ArulDhandapani\Documents\WS-Eclipse\WorkFlow

3. Install below the extensions
    - __Java Extension Pack__ by Microsoft
        - This will automatically install below the listed plugins (_verify after install_)
            - __Debugger for Java__ by Microsoft
            - __Maven for Java__ by Microsoft
            - __Java Extension Pack__ by Microsoft
            - __Java Test Runner__ by Microsoft
            - __Java Dependancy__ Viewer by Microsoft
    - __PostgreSQL__ by Microsoft
    - __Python__ by Microsoft
    - __Visual Studio IntelliCode__ by Microsoft
    - __GitLens__ by Eric Amodio
    - __Git Graph__ by mhutchie
    - __Horizon Theme__ by Jonathan Olaleye
    - __Shellcheck__ by Timon Wong
    - __SonarQube__ by SilverBulleters
    - __SonarLint__ by SonarSource
    - __Language Support for Java__ by Red Hat
    - __Dependency Analytics__ by Red Hat
    - __XML__ by Red Hat
    - __Javadoc-Generator__ by Keegan Bruer
    - __Checkstyle for Java__ by ShengChen
    - __JSON Pretty Printer__ by Axel Etcheverry
    - __Tomcat for Java__ by Wei Shen
    - __yUML__ by Jaime Olivares
    - __Version Lense__ by pflannery
    - __Partial Diff__ by Ryuichi Inagaki
    - __L13 Diff__ by L13RARY

4. Setup SonarQube Support
    - Open Visual Studio Code
    - Create global config via "__SonarQube Inject: Create global config with credentials to servers__" and fill the values using __CTL+SHIFT+P__ key.
        - This will create __C:\user\user\.sonarlint\config\global.json__ file (_Note: Your user account_).
        - Login [SonarQube application](http://vhldvztdt001.tvlport.net:9000/sonar).
        - Drop down user name go to "__My Account__" -> __Security__ -> __Enter Token Name__ with your Laptop name (_Like, TPFLAP166_) then click "__Generate__" button to create token
        - Copy token id and then replace "__YOUR_SONAR_TOKEN__" in below JSON data to be copy to __global.json__ file
            ```json
            {
                "servers": [
                    {
                        "id":"vhldvtztdt001.tvlportnet",
                        "url": "http:/vztdt001.tvlportnet:9000/sonar",
                        "token":"YOUR_SONAR_TOKEN"
                    }
                ]
            }
            ```
        - Copy above JSON data and then replace it in __global.json__ file to save under __C:\user\user\.sonarlint\config__ location

    - Update project bindings via "__SonarQube Inject: Update bindings to SonarQube server__" by using __CTL+SHIFT+P__ key.
        - It'll take a time (_Approximatly to 2 to 3 min_) on first time.

    - If setup good then you will get "__Bindings updated successfully__" message.

5. Java Checkstyle Setup
    - Setup checkstyle support from provider from Google or Sun Java
    - To setup configurations for Google then enter below the command to select __Google__
        > Checkstyle: Set the Checkstyle Configuration file
    - Travelport standards for Java projects for your reference - _No Action Required_
        - Refer ___Travelport SonarQube Quality Profiles___ to get updated profile for [Java Language](http://sonar.dv.tvlport.com/profiles/permalinks/16).
        - Note: These rules we'll download and keep upto date in __DeployScripts__ directory
            - All rules (Location: _DeployScripts/TP_JavaRules.xml_)
            - Checkstyle (Location: _DeployScripts/TP_Checkstyle.xml_)
            - FindBugs (Location: _DeployScripts/TP_FindBugs.xml_)
            - PMD (Location: _DeployScripts/TP_PMD.xml_)

## How to use - Visual Studio Code
- Always use below the commands using __CTL+SHIFT+P__ to update projects
    > SonarQube Inject: Update bindings to SonarQube server

- Update sonarLint with SonarQube
    > Update SonarLint binding to SonarQube/SonarCloud

- To check checkstyle for your Java files
    > Checkstyle: Check code with Checkstyle

- To Generate Java documents for your Java files
    > Javadoc Generator: Built In Generator

- To Open Java server console
    > Java:Open Java Language Server log file

- To Clean Java server and clean workspace
    > Java:Clean the Java language server workspace

- To Access PostgreSQL database, refer PostgreSQL Quick Start steps in extentions

- To compare two directory
    > L13 diff
