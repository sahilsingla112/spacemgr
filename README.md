
Space Manager project for cleaning up directories

#Takes configuration from src/main/resources/application.properties or you can supply external configuration file.
#Takes comma separated multiple directories in property 'dirs'. Params are space separted. First param is directory path, second is min size in MB, third is max size in MB.
#Takes preprocessor commands in 'preprocess' property. Has the ability to take comma separated multiple commands. Command name & args are space separated.


Steps to run the project:

1) git clone https://github.com/sahilsingla112/spacemgr.git
2) Goto the directory 'spacemgr' containing pom.xml
3) Run 'mvn clean install'
4) On linux, execute the following command:

java -jar target/spacemgr*.jar


How to stop the project:

1) Either Ctrl-C on the terminal if the process is running in foreground 
2) Or if you are running in background, kill -9 <pid>

Assumptions:
If you are using default config, there should be directories '../../testdirectory' and '../../testdirectory4' on your system. Path is relative to the project directory 
containing pom.xml

If you want to specify different directories, modify application.properties in src/main/resources or supply external config as explained in the next section.


External configuration file:

If you want to specific the external configuration file outside of the jar. Run the application with the following command:

java -DCONFIG_FILE=<configpath> -jar <jarpath>	


