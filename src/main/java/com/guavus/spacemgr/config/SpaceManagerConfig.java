package com.guavus.spacemgr.config;

import com.guavus.spacemgr.processing.*;
import com.guavus.spacemgr.scheduler.MonitorDirectoryScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

/**
 * Spring Configuration class to declare beans
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */
@Configuration
public class SpaceManagerConfig {

	private static final Logger LOG = LoggerFactory.getLogger(SpaceManagerConfig.class);

	@Autowired
	private MonitorDirectoryScheduler monitorDirectoryScheduler;

	private ConfigurationParser parser;

	@Value("${scheduler.onstartup:true}")
	private boolean loadSchedulerOnStartUp;

	public SpaceManagerConfig(ConfigurationParser configurationParser){
		this.parser = configurationParser;
	}

	@PostConstruct
	public void init(){
		if (loadSchedulerOnStartUp)
			monitorDirectoryScheduler.start();
	}

	@Bean(name = "preprocessor")
	CommandProcessor preProcessor(){
		return new PreProcessorImpl(parser.getProcessorCommands(ProcessorType.PREPROCESS));
	}

	@Bean(name = "postprocessor")
	CommandProcessor postProcessor(){
		return new PostProcessorImpl(parser.getProcessorCommands(ProcessorType.POSTPROCESS));
	}

	@Bean
	public MonitorDirectoryTask monitorDirectoryTask(){
		return new MonitorDirectoryTask(preProcessor(), postProcessor(), parser.getDirectories());
	}

	@Bean
	public MonitorDirectoryScheduler monitorDirectoryScheduler(MonitorDirectoryTask monitorDirectoryTask) {
		return new MonitorDirectoryScheduler(monitorDirectoryTask, Executors::newSingleThreadScheduledExecutor, parser.getrefreshInterval(), parser.getInitialDelay());
	}
}

