package com.guavus.spacemgr.model;

import java.io.File;
import java.util.List;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */
public class DirectoryInfo {
	private String path;
	private long currentSize;

	// Size in bytes
	private long maxSize;

	// Size in bytes
	private long minSize;
	private File directory;

	List<File> filesToBeDeleted; // unpopulated

	public DirectoryInfo(String path){
		this.path = path;
		directory = new File(path);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	public long getMinSize() {
		return minSize;
	}

	public void setMinSize(long minSize) {
		this.minSize = minSize;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public List<File> getFilesToBeDeleted() {
		return filesToBeDeleted;
	}

	public void setFilesToBeDeleted(List<File> filesToBeDeleted) {
		this.filesToBeDeleted = filesToBeDeleted;
	}

	public long getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
	}
}
