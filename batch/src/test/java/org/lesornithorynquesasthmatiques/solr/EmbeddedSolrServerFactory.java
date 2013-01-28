package org.lesornithorynquesasthmatiques.solr;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.util.TestHarness;
import org.lesornithorynquesasthmatiques.mongo.EmbeddedMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddedSolrServerFactory {

	private static final Logger LOG = LoggerFactory.getLogger(EmbeddedMongo.class);

	public static EmbeddedSolrServer createEmbeddedSolrServer(File solrConfigFile, File schemaFile) throws IOException {
		
		final File solrHome = new File(System.getProperty("java.io.tmpdir"), "solr-" + System.nanoTime());
		solrHome.mkdirs();
		
		SolrConfig solrConfig = TestHarness.createConfig(solrHome.getAbsolutePath(), solrConfigFile.getAbsolutePath());

		final TestHarness solrTestHarness = new TestHarness(solrHome.getAbsolutePath(), solrConfig, schemaFile.getAbsolutePath());
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				if (solrTestHarness != null) {
					solrTestHarness.close();
				}
				if (solrHome.exists()) {
					try {
						FileUtils.deleteDirectory(solrHome);
					} catch (IOException e) {
						LOG.error("Could not delete directory: " + solrHome, e);
					}
				}
			}
		});
		
		return new EmbeddedSolrServer(solrTestHarness.getCoreContainer(), solrTestHarness.getCore().getName());

	}


}
