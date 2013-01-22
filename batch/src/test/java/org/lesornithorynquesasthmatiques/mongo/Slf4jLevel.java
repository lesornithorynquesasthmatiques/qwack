package org.lesornithorynquesasthmatiques.mongo;

import org.slf4j.Logger;

public enum Slf4jLevel {
	
	TRACE {
		@Override
		public void log(Logger logger, String message) {
			logger.trace(message);
		}
	},
	
	DEBUG {
		@Override
		public void log(Logger logger, String message) {
			logger.debug(message);
		}
	},
	
	INFO {
		@Override
		public void log(Logger logger, String message) {
			logger.info(message);
		}
	},
	
	WARN {
		@Override
		public void log(Logger logger, String message) {
			logger.warn(message);
		}
	},
	
	ERROR {
		@Override
		public void log(Logger logger, String message) {
			logger.error(message);
		}
	}

	;

	public abstract void log(Logger logger, String message);
}