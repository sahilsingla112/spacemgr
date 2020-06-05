package com.guavus.spacemgr.model;

import java.util.List;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */
public class LinuxCommand {
	private String commandName;
	private List<String> arguments;

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
}
