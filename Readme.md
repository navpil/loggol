# Logging in Java

There are several libraries, which can be used for logging.
They split into API-only, Implementation-only and both.

Implementation only:

 - JUL (java.util.logging)
 - Log4j - deprecated, but still widely used
 - Logback - uses slf4j API natively

API only:

 - Slf4j - default used implementation is Logback
 - Apache commons logging (shortened JCL sometimes) - default implementation is JUL

API + imlementation:
 
 - Log4j2 (log4j2-api and log4j2-core)

Implementation libraries can be used directly, but they can't be simply switched with another implementation.
Often implementation library is used through the API, so they can be easily switched with another one.
It's the implementation library which is configured.

API libraries require usage of some implementation.

## Logging library description

### JUL

Java util logging came too late to the scene and is not too convenient to configure, but is provided in Java out of the box.
Can be configured programmatically or through the property file.
Unfortunately there is no file format which will be automatically picked up from classpath.
Need to provide the system property to Java machine somehow.
Process-wide configuration.

[Configure JUL](http://tutorials.jenkov.com/java-logging/configuration.html)

[Configure JUL File Location](https://www.logicbig.com/tutorials/core-java-tutorial/logging/loading-properties.html)

https://github.com/openjdk/jdk/tree/master/src/java.logging/share/classes/java/util/logging

### Log4j

Deprecated. Please Use Log4j2.

https://logging.apache.org/log4j/1.2/manual.html 

### Slf4j

http://www.slf4j.org/

API only, can use following implementations:

 - Logback `logback-classic` (native implementation)
 - Log4j 1.x `slf4j-log4j12`
 - JUL `slf4j-jdk14`
 - NOP (no logging) `slf4j-nop` (not discussed further)
 - Console `slf4j-simple` (not discussed further)
 - JCL `slf4j-jcl` - which in turn will use log4j or JUL or whatever (not discussed further)

Uses SPI to find implementation.

### Logback

http://logback.qos.ch/

Note the similarity to the slf4j site

Reuses SLF4j API, so there is no sample of Logback-only

### JCL 

Apache Commons Logging, previously known as Jakarta Commons Logging.
Obsolete.

### Log4j2

Has strong emphasis on telling everyone logging through itself into log4j2-core.

If someone wants to use another logging implementation, then the log4j2-to-slf4 bridge should be used.

## Example project

We have a project `impls` which uses all the logging libraries:

 - JUL
 - Log4j
 - Log4j2 API
 - JCL
 - Slf4j
 
And we have a project `ourlib` which uses `impls`.
 
In case libraries added some implementations, such as Log4j2-core or logback, we may exclude them.

### Error output

JUL will output something, because it always exists and has basic configuration.
Note the default two-line logging.

JCL will output through JUL or fail with Log4j error.

Log4j will say:

    log4j:WARN No appenders could be found for logger (io.github.navpil.testlogging.UsesLog4j).
    log4j:WARN Please initialize the log4j system properly.
    log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
    
Log4j2 will say:

    ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console...
    
It will output to console only ERROR levels.

Slf4j will say:

    SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
    SLF4J: Defaulting to no-operation (NOP) logger implementation
    SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

Slf4j can fallthrough to the Log4j2 api and vice versa.

### Fixing JUL and Log4j

`-Plog-config`

JUL still works, but maybe we will have to provide some additional configuration for it.
You may use another location:

    java -Djava.util.logging.config.file=/path/to/app.properties MainClass

Log4j simply requires log4j.xml file for it to work. You may use another location:

    java -Dlog4j.configuration=file:/path/to/log4j.properties myApp 

### Fallthrough to Log4j2-core

`-Plog4j2`

Read [which jars](https://logging.apache.org/log4j/2.0/faq.html#which_jars)

All bridges, except `log4j-to-slf4j`, provided by Log4j2 expect you to log to log4j2-core

**JUL** (`log4j-jul`) has to be configured manually through the System property before JUL is ever used

    System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
    
JUL will use this property in static initializer block.
    
**JCL** (`log4j-jcl`) will find log4j2 implementation through the SPI (or something similar).

**Slf4j** (`log4j-slf4j-impl`) and **Log4j2 API** (natively supports log4j2-core) is configured with no problems.

**Log4j 1.x** dependencies have to be excluded and instead include jar (`log4j-1.2-api`) which mirrors the API, but logs through Log4j2.

By default reads `log4j2.xml`, but can be reset with

    -Dlog4j.configurationFile=MyConfigurationFile.xml

### Fallthrough to Logback

`-Plogback`

Logback is natively supported by **Slf4j**

**Log4j** is excluded and `log4j-over-slf4j` is added

**JCL** is excluded and `jcl-over-slf4j` is added

**Log4j2** provides `log4j-to-slf4j` bridge

**JUL** (`jul-to-slf4j`) handling is not elegant. Requires the call to:

    SLF4JBridgeHandler.install();
    
and outputs both the Native JUL logging and Slf4j logging.

This is because in the background `SLF4JBridgeHandler.install()` does this:

    LogManager.getLogManager().getLogger("").addHandler(new SLF4JBridgeHandler());

There is a fix though, used by profile `-Plogback-fixed`:

Instead of `jul-to-slf4j` use approach as in Log4j2-core, `log4j-jul`

### Fallthrough to JUL

`-Pjul`

Rather strange choice, but it is still possible:

**Log4j**, **Log4j2** and **JCL** are handled in the same way as in falling back to Logback (`-Plogback`).

Just instead of logback, use the `slf4j-jdk14` instead

### Fallthrough to Log4j

Don't do it. Fallback to log4j2.

But if you really want to do it:

Use **Log4j2** bridge `log4j-jul` for **JUL**, because it works better.

Use Log4j2 bridge `log4j-jcl` for **JCL** because it plays nicer and does not require exclusions.

Use Log4j2 bridge `log4j-to-slf4j` to fallback into **Slf4j**.

Use `slf4j-log412` to use log4j
 
## Log4j2 Configuration description

 - Loggers - they will accept logging messages
 - Appenders - they will output logging messages
 - Filters - optional, will filter messages by some parameters 

## ELK Stack

 - ElasticSearch 
    - DB which supports fulltext search and document based approach (similar to MongoDB or CouchDB)
    - Indexes data
    - Can transform data via pipeline on receive
    - Can reindex data
    - Should not be used as primary data source
 - Kibana 
    - visualization tool for ElasticSearch
    - calls ElasticSearch API to show data
    - Grafana is another tool.
 - Logstash
    - Inputs - various types of inputs (http, txt etc)
    - Filter - filters and transforms data
    - Output - varous types of outputs (elastic search, text file)
 - Beats
    - Monitors some source
    - Sends events to ElasticSearch

## Logging in Tomcat

It is possible to make Tomcat use log4j2 instead of JUL by default for logging.

https://logging.apache.org/log4j/2.0/log4j-appserver/index.html

If we want our web application to use log4j2 we should add `log4j-web` dependency.
If the dependency is included it searches for `log4j2.xml` in `WEB-INF` folder.
In case the file is found other log4j2 config files are ignored.

The issue with `log4j-jul` is that configuration should be called before any logging is done with JUL,
but Tomcat uses JUL by default and JUL can't be reconfigured after it's started.
Therefore `jul-to-slf4j` is used in the `-Plog4j2-webapp` example, which works better in webapp context.

    > mvn -Plog4j2 clean package
    > copy target/webproj.war %CATALINA_BASE%/webapps

    URL: http://localhost:8080/webproj/
    
See the `webproj/resources/webapp/WEB-INF/log4j.xml` for configuration of logging into file.

Start `jconsole.exe`, connect to catalina, open `org.apache.logging.log4j -> StatusLogger/Loggers -> Attributes`
 
[Webapp logging Log4j](https://logging.apache.org/log4j/2.0/manual/webapp.html#Servlet-3.0)

[Tomcat logging](https://tomcat.apache.org/tomcat-8.0-doc/logging.html)

### Log files description in Tomcat
 
There are many log files in the `tomcat\logs` folder.
Some of them are Tomcat generated.

`commons-daemon`, `tomcat8-stderr` and `tomcat8-stdout` are the log files generated by the Tomcat Service.
Because Tomcat runs as a service, there is no opened output console, and the `tomcat8-stderr` and `tomcat8-stdout` are
the files, which capture the console output and they may contain interesting information, especially the `tomcat8-stderr`,
which captures errors.
The `commons-daemon` is the logger specially for Tomcat Service and does not contain anything interesting for us.

The following log files are provided by Tomcat by default:
  
 - `catalina` - Tomcat output, similar to `tomcat8-stderr` and `tomcat8-stdout`
 - `localhost` - Usually of no interest, only when something is really wrong this file may help. ServletAppender logs go here.
 - `localhost_access_log` - All requests to the server are logged here
 - `manager` - Log for the `manager` application which is shipped with Tomcat by default
 - `host-manager` - Log for the `host-manager` application which is shipped with Tomcat by default

## Guidelines

Do not put logging implementation/configuration into a library which is used by others.

Do not forget to close MDC. 

Use parametrized logging or lambda syntax to avoid `LOGGER.isDebugEnabled()` 

### Usage of logging levels

    ERROR   you need        to do something
    WARN    you might need  to do something
    INFO    you need        to log this in production
    DEBUG   you might need  to log this in production
    TRACE   everything that is happening (no performance concerns)
    
The assumption behind this is that ops team will:
    
    always have production set to log-level info
    might have production set to log-level debug
    never have production set to log-level trace

https://softwareengineering.stackexchange.com/questions/279690/why-does-the-trace-level-exist-and-when-should-i-use-it-rather-than-debug

Answer by Alexander Bird

## Sources

[Common logging with JUL](http://javarushi.blogspot.com/2012/04/how-to-use-apache-commons-logging-with.html)

[Blog post about comparing JUL, Log4j, Log4j2, logback](https://blog.overops.com/is-standard-java-logging-dead-log4j-vs-log4j2-vs-logback-vs-java-util-logging/)

[Baeldung logging introduction](https://www.baeldung.com/java-logging-intro)

[Yet another comparison](https://stackify.com/compare-java-logging-frameworks/)
