package org.lesornithorynquesasthmatiques.mongo;

import org.slf4j.Logger;

import de.flapdoodle.embed.process.io.IStreamProcessor;
import de.flapdoodle.embed.process.io.StreamToLineProcessor;

/**
 * {@link IStreamProcessor} using SLF4J to log messages.
 * Should be preferably wrapped within a {@link StreamToLineProcessor}
 * for a better output.
 * 
 * The constructor takes a {@link Logger} that will be used to log messages,
 * and an instance of {@link Slf4jLevel} to determine which method to call
 * on the {@link Logger} instance.
 * 
 * Example of usage:
 * 
 * <pre>
 * Logger logger = ...
 * IStreamProcessor mongodOutput =
 *   new NamedOutputStreamProcessor("[mongod out]",
 *     new StreamToLineProcessor(
 *         new Slf4jStreamProcessor(logger, Slf4jLevel.INFO)));
 * </pre>
 * 
 * @author Alexandre Dutra
 */
public class Slf4jStreamProcessor implements IStreamProcessor {

	private final Logger logger;

	private final Slf4jLevel level;

	public Slf4jStreamProcessor(Logger logger, Slf4jLevel level) {
		this.logger = logger;
		this.level = level;
	}

	@Override
	public void process(String line) {
		level.log(logger, stripLineEndings(line));
	}

	@Override
	public void onProcessed() {
	}

	protected String stripLineEndings(String line) {
		//we still need to remove line endings that are passed on by StreamToLineProcessor...
		return line.replaceAll("[\n\r]+", "");
	}

}