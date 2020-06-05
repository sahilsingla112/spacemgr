
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

Performance stats:

System is able to delete around 20k files in less than 2 sec. Have tried upto 1.28lakh files which took around 12sec on my machine (4GB RAM, 4core). 


How to run the load test:

1) Go to <projdir>/loadtest directory.
2) Execute ./run.sh [numfiles]
3) By default, it will produce 2^14 files. If pass 5 for example, it will produce 32 files.
4) Execution will begin, first it will produce files in bak/ folder, it will then launch application to cleanup the bak folder.
5) You can customize the behavior of application by modiying load.config





