package org.lesornithorynquesasthmatiques.batch;

import java.io.File;
import java.io.PrintStream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class Options {
	
	@Option(name = "-f", aliases = {"--file"}, usage = "Full path to HDF5 file", required = true) 
	private File h5file = null;

	@Option(name = "-ds", aliases = {"--dataset"}, usage = "Full path to HDF5 dataset inside file", required = true) 
	private String datasetPath = null;

	@Option(name = "-cs", aliases = {"--chunk-size"}, usage = "Chunk size, default: 1000") 
	private int chunkSize = 1000;

	@Option(name = "-ps", aliases = {"--pool-size"}, usage = "Thread pool size, default: available CPUs") 
	private int poolSize = Runtime.getRuntime().availableProcessors();

	@Option(name = "-qs", aliases = {"--queue-size"}, usage = "Thread pool queue size, default: 1000") 
	private int queueSize = 1000;

	@Option(name = "-h", aliases = {"--host"}, usage = "Mongo host, default: 127.0.0.1") 
	private String mongoHost = "127.0.0.1";

	@Option(name = "-p", aliases = {"--port"}, usage = "Mongo port, default: 27017") 
	private Integer mongoPort = 27017;

	@Option(name = "-u", aliases = {"--user"}, usage = "Mongo user, default: blank") 
	private String mongoUser = "";

	@Option(name = "-pw", aliases = {"--password"}, usage = "Mongo password, default: blank") 
	private String mongoPassword = "";

	@Option(name = "-wc", aliases = {"--write-concern"}, usage = "Mongo write concern, default: UNACKNOWLEDGED") 
	private String mongoWriteConcern = "UNACKNOWLEDGED";

	@Option(name = "-d", aliases = {"--database"}, usage = "Mongo database, default: main") 
	private String mongoDatabaseName = "main";

	@Option(name = "-c", aliases = {"--collection"}, usage = "Mongo password, default: data") 
	private String mongoCollectionName = "data";

    @Option(name = "-?", aliases = {"--help"}, usage = "Displays usage help") 
    private boolean help;

    public File getH5file() {
		return h5file;
	}

	public String getDatasetPath() {
		return datasetPath;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public String getMongoHost() {
		return mongoHost;
	}

	public Integer getMongoPort() {
		return mongoPort;
	}

	public String getMongoUser() {
		return mongoUser;
	}

	public String getMongoPassword() {
		return mongoPassword;
	}

	public String getMongoWriteConcern() {
		return mongoWriteConcern;
	}

	public String getMongoDatabaseName() {
		return mongoDatabaseName;
	}

	public String getMongoCollectionName() {
		return mongoCollectionName;
	}

	public boolean isHelp() {
		return help;
	}

	public void populate(String... args) {
    	CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            this.help = true;
        }
    }

	public void printUsage(PrintStream out) {
		out.println("Usage:");
		CmdLineParser parser = new CmdLineParser(this);
		parser.setUsageWidth(100);
		parser.printUsage(out);
	}

	
}