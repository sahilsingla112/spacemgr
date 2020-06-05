package com.guavus.spacemgr.config;

import com.guavus.spacemgr.model.DirectoryInfo;
import com.guavus.spacemgr.model.LinuxCommand;
import com.guavus.spacemgr.processing.ProcessorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */

@Component
public class ConfigurationParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationParser.class);
	private static final String DIRS_PROPERTY = "dirs";
	private static final String PREPROCESS_PROPERTY = "preprocess";
	private static final String POSTPROCESS_PROPERTY = "postprocess";
	private static final String REFRESH_INTERVAL_PROPERTY = "refresh.interval";
	private static final String INITIAL_DELAY_PROPERTY = "initial.delay";
	private static final long DEFAULT_DELAY = 0;

	private Properties source = new Properties();

	@PostConstruct
	public void init(){
		initProperties();
	}

	private void initProperties() {
		InputStream is = null;
		try {
			String configFilePath = System.getProperty("CONFIG_FILE");
			if (StringUtils.isEmpty(configFilePath)){
				configFilePath = "application.properties";
				is = SpaceManagerConfig.class.getClassLoader().getResourceAsStream(configFilePath);
			}else{
				is = new FileInputStream(configFilePath);
			}

			this.source.load(is);
		} catch (final IOException e) {
			LOGGER.error("Error while loading configuration from config file", e);
		}finally{
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("Error while closing the input stream", e);
				}
		}
	}

	public List<DirectoryInfo> getDirectories(){
		List<DirectoryInfo> list = new ArrayList<>();
		String dirsValue = source.getProperty(DIRS_PROPERTY).trim();

		String[] fields = dirsValue.split(",");

		try {
			for (String field : fields) {
				String[] values = field.trim().split(" ");
				DirectoryInfo directoryInfo = new DirectoryInfo(values[0].trim());

				// Convert MBs into bytes
				directoryInfo.setMinSize(Long.parseLong(values[1].trim()) * 1048576);
				directoryInfo.setMaxSize(Long.parseLong(values[2].trim()) * 1048576);

				list.add(directoryInfo);
			}
		}catch (NumberFormatException e) {
			LOGGER.error("Exception while parsing dirs config input", e);
		}
		return list;
	}

	public List<LinuxCommand> getProcessorCommands(ProcessorType processorType){
		List<LinuxCommand> result = new ArrayList<>();

		final String processorProperty;
		if (processorType == ProcessorType.PREPROCESS)
			processorProperty = source.getProperty(PREPROCESS_PROPERTY);
		else
			processorProperty = source.getProperty(POSTPROCESS_PROPERTY);

		String[] processorPropertyValues = processorProperty.trim().split(",");

		for (String processVal: processorPropertyValues){
			String[] fields = processVal.trim().split(" ");

			LinuxCommand linuxCommand = new LinuxCommand();
			linuxCommand.setCommandName(fields[0]);

			List<String> arguments = new ArrayList<>();

			for (int i=1; i < fields.length; i++) {
				arguments.add(fields[i].trim());
			}
			linuxCommand.setArguments(arguments);
			result.add(linuxCommand);
		}

		return result;
	}

	public Long getInitialDelay(){

		final String property = source.getProperty(INITIAL_DELAY_PROPERTY);

		if (StringUtils.isEmpty(property))
			return DEFAULT_DELAY;

		return Long.parseLong(property.trim());
	}

	public Long getrefreshInterval(){
		return Long.parseLong(source.getProperty(REFRESH_INTERVAL_PROPERTY).trim());
	}

}
