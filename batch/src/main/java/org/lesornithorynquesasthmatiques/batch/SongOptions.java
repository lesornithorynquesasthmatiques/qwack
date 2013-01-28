package org.lesornithorynquesasthmatiques.batch;

import java.io.File;
import java.io.PrintStream;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class SongOptions {
	
	@Option(name = "-d", aliases = {"--directory"}, usage = "Full path to base directory where to discover H5 files", required = true) 
	private File directory = null;

	@Option(name = "-ps", aliases = {"--pool-size"}, usage = "Thread pool size, default: available CPUs") 
	private int poolSize = Runtime.getRuntime().availableProcessors();

	@Option(name = "-qs", aliases = {"--queue-size"}, usage = "Thread pool queue size, default: 1000") 
	private int queueSize = 1000;

	@Option(name = "-mh", aliases = {"--host"}, usage = "Mongo host, default: 127.0.0.1") 
	private String mongoHost = "127.0.0.1";

	@Option(name = "-mp", aliases = {"--port"}, usage = "Mongo port, default: 27017") 
	private Integer mongoPort = 27017;

	@Option(name = "-mu", aliases = {"--user"}, usage = "Mongo user, default: blank") 
	private String mongoUser = "";

	@Option(name = "-mpw", aliases = {"--password"}, usage = "Mongo password, default: blank") 
	private String mongoPassword = "";

	@Option(name = "-mwc", aliases = {"--write-concern"}, usage = "Mongo write concern, default: UNACKNOWLEDGED") 
	private String mongoWriteConcern = "UNACKNOWLEDGED";

	@Option(name = "-md", aliases = {"--database"}, usage = "Mongo database, default: main") 
	private String mongoDatabaseName = "main";

	@Option(name = "-mc", aliases = {"--collection"}, usage = "Mongo password, default: data") 
	private String mongoCollectionName = "data";

	@Option(name = "-s", aliases = {"--solr-url"}, usage = "Solr URL, default: http://localhost:8983/solr") 
	private String solrUrl = "http://localhost:8983/solr";

    @Option(name = "-?", aliases = {"--help"}, usage = "Displays usage help") 
    private boolean help;

	public File getDirectory() {
		return directory;
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

	public String getSolrUrl() {
		return solrUrl;
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