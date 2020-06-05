package com.guavus.spacemgr.aop;

import com.guavus.spacemgr.model.DirectoryInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sahil Singla
 * @version 1.0
 * @since 05-06-2020
 */

@Aspect
@Component
public class StatsAdvice {
	private static final Logger LOG = LoggerFactory.getLogger(StatsAdvice.class);

	@Pointcut("@annotation(com.guavus.spacemgr.aop.CollectStats)")
	public void collectStats(){}

	@Around("collectStats()")
	public void log(ProceedingJoinPoint joinPoint){

		try {
			Object[] args = joinPoint.getArgs();
			int numFiles = 0;
			List<DirectoryInfo> directoryInfoList =(List<DirectoryInfo>)args[0];
			for (DirectoryInfo dir: directoryInfoList){
				if (dir.getDirectory().listFiles() != null)
					numFiles += dir.getDirectory().listFiles().length;
			}

			long start = System.currentTimeMillis();
			joinPoint.proceed();

			int numFilesRemaining = 0;
			for (DirectoryInfo dir: directoryInfoList){
				if (dir.getDirectory().listFiles() != null)
					numFilesRemaining += dir.getDirectory().listFiles().length;
			}

			int deletedFiles = numFiles - numFilesRemaining;
			if (deletedFiles  > 0) {
				long time = System.currentTimeMillis() - start;
				LOG.info("Deletion of files " + deletedFiles + " took time: " + time);
			}

		} catch (Throwable throwable) {
			LOG.error("Exception while processing stats advice", throwable);
		}
	}
}
