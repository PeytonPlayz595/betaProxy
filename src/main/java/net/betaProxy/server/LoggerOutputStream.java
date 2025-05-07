package net.betaProxy.server;

import java.io.OutputStream;
import java.io.PrintStream;

import net.betaProxy.log4j.LogManager;
import net.betaProxy.log4j.Logger;

public class LoggerOutputStream extends PrintStream {
	private final Logger logger;
	private final boolean err;

	public LoggerOutputStream(String domainIn, boolean err, OutputStream outStream) {
		super(outStream);
		this.logger = LogManager.getLogger(domainIn);
		this.err = err;
	}

	public void println(String s) {
		this.logString(s);
	}

	public void println(Object parObject) {
		this.logString(String.valueOf(parObject));
	}

	private void logString(String string) {
		String callingClass = getCallingClass(3);
		if (callingClass == null) {
			if (err) {
				logger.error(string);
			} else {
				logger.info(string);
			}
		} else {
			if (err) {
				logger.error("@({}): {}", new Object[] { callingClass, string });
			} else {
				logger.info("@({}): {}", new Object[] { callingClass, string });
			}
		}
	}
	
	private static String getCallingClass(int backTrace) {
		StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
		StackTraceElement stacktraceelement = astacktraceelement[Math.min(backTrace + 1, astacktraceelement.length)];
		return "" + stacktraceelement.getFileName() + ":" + stacktraceelement.getLineNumber();
	}
}