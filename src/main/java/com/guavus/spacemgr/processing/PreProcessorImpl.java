package com.guavus.spacemgr.processing;

import com.guavus.spacemgr.model.LinuxCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */
public class PreProcessorImpl implements CommandProcessor{

	private static final Logger LOG = LoggerFactory.getLogger(PreProcessorImpl.class);

	// Space separated values
	private List<LinuxCommand> commands;

	public PreProcessorImpl(List<LinuxCommand> commands){
		this.commands = commands;
	}

	public void execute(){
		for (LinuxCommand command: commands){
			try {
				String fullCommand = String.format(command.getCommandName(), command.getArguments());
				Process process = Runtime.getRuntime()
						.exec(fullCommand);

				StreamGobbler streamGobbler =
						new StreamGobbler(process.getInputStream(), System.out::println);

				Executors.newSingleThreadExecutor().submit(streamGobbler);
				int exitCode = process.waitFor();

				LOG.info("****Pre-processing command completed with exit code: " + exitCode);

			} catch (IOException e) {
				LOG.error("Error while processing the linux command", e);
			}
			catch (InterruptedException e){
				LOG.error("Interrupted while processing the linux command", e);
				Thread.currentThread().interrupt();
			}
		}
	}
}
