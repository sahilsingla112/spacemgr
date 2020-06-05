package com.guavus.spacemgr.model;

import java.io.File;
import java.util.Comparator;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 04-06-2020
 */
public class FileComparator implements Comparator<File> {
	public int compare(File f1, File f2)
	{
		return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
	}
}
