
Space Manager project for cleaning up directories

It will periodically launch a runnable task which will delete the files from the configured directories if their total size is greater than the specified max size. After deletion, size will come under min size. If the size of directories is less than max size, task will not do anything. 


 
#Takes configuration from src/main/resources/application.properties or you can supply external configuration file.
#Takes comma separated multiple directories in property 'dirs'. Params are space separted. First param is directory path, second is min size in MB, third is max size in MB.
#Takes preprocessor commands in 'preprocess' property. Has the ability to take comma separated multiple commands. Command name & args are space separated. Similarly, postprocessor commands should be defined in 'postprocess' property.
#Config property 'refresh.interval' defines the frequency of the task. 

Steps to run the project:

1) git clone https://github.com/sahilsingla112/spacemgr.git
2) Goto the directory 'spacemgr' containing pom.xml
3) Run 'mvn clean install'
4) On linux, execute ./start.sh


How to stop the project:

1) Either Ctrl-C on the terminal if the process is running in foreground 
2) Or if you are running in background, kill -9 <pid>

Assumptions:
If you are using default config, create directories '../../testdirectory' and '../../testdirectory4' on your system. Path is relative to the project directory containing pom.xml. Put a bunch of files with total size above 5MB to see some deletion.

If you want to specify different directories, modify application.properties in src/main/resources or supply external config as explained in the next section.


External configuration file:

If you want to specify the external configuration file outside of the jar. Run the application with the following command:

java -DCONFIG_FILE=<configpath> -jar <jarpath>

Performance stats:

System is able to delete around 20k files in less than 2 sec. Have tried upto 1.28lakh files which took around 12sec on my machine (4GB RAM, 4core). 


How to run the load test:

1) Go to <projdir>/loadtest directory.
2) Execute ./run.sh [numfiles]. 'numFiles' is radix of 2. If you pass 5 for example, it will produce 32 files. Default value is 14, so by default it will produce 2^14 = 16384 files. 
4) Upon execution, it will first produce files in loadtest/bak folder, it will then launch the application to cleanup the bak folder. Performance stat will be published after the scheduled task is finished. 
5) You can customize the behavior of application by modiying load.config





