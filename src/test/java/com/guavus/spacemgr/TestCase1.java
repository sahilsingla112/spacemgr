package com.guavus.spacemgr;

import com.guavus.spacemgr.config.ConfigurationParser;
import com.guavus.spacemgr.config.SpaceManagerConfig;
import com.guavus.spacemgr.model.DirectoryInfo;
import com.guavus.spacemgr.processing.MonitorDirectoryTask;
import com.guavus.spacemgr.scheduler.MonitorDirectoryScheduler;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 05-06-2020
 */
public class TestCase1 extends SpacemgrApplicationTests {

	private static final String MASTER_LIST1 = "src/test/resources/masterlist1";
	private static final String MASTER_LIST2 = "src/test/resources/masterlist2";

	private List<File> masterListDirectories = new ArrayList<>();

	private List<DirectoryInfo> directories;

	@Autowired
	private SpaceManagerConfig spaceManagerConfig;

	@Autowired
	private ConfigurationParser parser;

	@Autowired
	private MonitorDirectoryTask monitorDirectoryTask;

	@PostConstruct
	public void init(){
		this.directories = parser.getDirectories();
		File masterList1 = new File(MASTER_LIST1);
		File masterList2 = new File(MASTER_LIST2);
		masterListDirectories.add(masterList1);
		masterListDirectories.add(masterList2);
	}

	@Before
	public void setup(){
		for(int i=0; i < directories.size(); i++){
			final DirectoryInfo directoryInfo = directories.get(i);
			directoryInfo.getDirectory().mkdirs();
			try {
				FileUtils.copyDirectory(masterListDirectories.get(i), directoryInfo.getDirectory());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("***********Before test**********");
	}

	@After
	public void cleanUp(){
		for (DirectoryInfo directoryInfo: directories){
			try {
				FileUtils.deleteDirectory(directoryInfo.getDirectory());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void test(){
		System.out.println("***********case test**********");

		DirectoryInfo testDirectory1;
		DirectoryInfo testDirectory2;

		if (directories.get(0).getDirectory().getName().contains("testdirectory1")){
			testDirectory1 = directories.get(0);
			testDirectory2 = directories.get(1);
		}else{
			testDirectory1 = directories.get(1);
			testDirectory2 = directories.get(0);
		}

		final int numFiles1Before = testDirectory1.getDirectory().listFiles().length;
		final int numFiles2Before = testDirectory2.getDirectory().listFiles().length;

		monitorDirectoryTask.run();

		final long sizeOfDirectory1 = FileUtils.sizeOfDirectory(testDirectory1.getDirectory());
		final long sizeOfDirectory2 = FileUtils.sizeOfDirectory(testDirectory2.getDirectory());

		final int numFiles1After = testDirectory1.getDirectory().listFiles().length;
		final int numFiles2After = testDirectory2.getDirectory().listFiles().length;

		assert(sizeOfDirectory1 < testDirectory1.getMinSize());
		assert(sizeOfDirectory2 < testDirectory2.getMinSize());
		assert(numFiles1Before > numFiles1After);
		assert(numFiles2Before > numFiles2After);
	//	assert(numFiles1After == 2);  // It may fail if copy of directory is not done with preserving timestamps
	//	assert(numFiles2After == 1); // // It may fail if copy of directory is not done with preserving timestamps
	}
}
