package com.guavus.spacemgr.processing;

import com.guavus.spacemgr.aop.CollectStats;
import com.guavus.spacemgr.model.DirectoryInfo;
import com.guavus.spacemgr.model.FileComparator;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */
public class MonitorDirectoryTask implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(MonitorDirectoryTask.class);

	private List<DirectoryInfo> directories;
	private CommandProcessor preProcessor;
	private CommandProcessor postProcessor;

	@Autowired
	private MonitorDirectoryTask self;

	public MonitorDirectoryTask(@Qualifier("preprocessor") CommandProcessor preProcessor, @Qualifier("postprocessor") CommandProcessor postProcessor, List<DirectoryInfo> directories){
		this.preProcessor = preProcessor;
		this.postProcessor = postProcessor;
		this.directories = directories;
	}

	@Override
	public void run() {
		// sorted order of files to be directory = {File@5773} "src\test\resources\testdirectory1"deleted
		self.processDirectories(directories);
		LOG.info("Scheduled task is complete");
	}

	@CollectStats
	void processDirectories(List<DirectoryInfo> directoryInfos){
		for (DirectoryInfo directoryInfo: directoryInfos){
			if (!directoryInfo.getDirectory().exists()){
				LOG.warn(String.format("Directory %s doesn't exist, skipping this", directoryInfo.getDirectory().getName()));
				continue;
			}
			long currentSize = getDirectorySize(directoryInfo.getDirectory());

			// processing is needed only if the directory passes the filter criteria
			if (isCleanupRequired(directoryInfo, currentSize)) {

				LOG.info(String.format("****Clean up started for %s...", directoryInfo.getDirectory().getName()));

				// Execute Preprocessor commands
				preProcessor.execute();

				// Delete the oldest files till the directory size is equal to minSize.
				cleanUpDirectory(directoryInfo, currentSize);

				// Execute Preprocessor commands
				postProcessor.execute();

				LOG.info(String.format("****Clean up completed for %s...\n", directoryInfo.getDirectory().getName()));
			}
		}
	}

	private void cleanUpDirectory(DirectoryInfo directoryInfo, final long currentSize){
		LOG.info("****Deleting files...");

		File directory = directoryInfo.getDirectory();
		File[] files = directory.listFiles();

		// Sort files by last modified time, oldest file first
		Arrays.sort(files, new FileComparator());

		long curSize = currentSize;

		int i = 0;
		while (curSize >= directoryInfo.getMinSize() && i < files.length){
			File file = files[i];

			// Remove only regular files, not directories
			if (file.isDirectory()) {
				i++;
				continue;
			}

			// remove the file
			long fileSizeKB = file.length();
			boolean deleted = file.delete();

			if (deleted) {
				curSize -= fileSizeKB;
				LOG.info(String.format("File %s is deleted", file.getName()));
			} else {
				LOG.error(String.format("File %s could not be deleted", file.getName()));
			}

			i++;
		}
	}

	/**
	 *
	 * @param directory
	 * @return Returns the directory size in KB
	 */
	private long getDirectorySize(File directory){
		return FileUtils.sizeOfDirectory(directory);
	}

	private boolean isCleanupRequired(DirectoryInfo directoryInfo, long currentSize){
		return currentSize >= directoryInfo.getMaxSize();
	}
}
