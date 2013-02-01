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

/**
 * Helper class to leverage the construction of an embedded Solr server for testing purposes.
 * 
 * @author Alexandre Dutra
 *
 */
public class EmbeddedSolrServerFactory {

	private static final Logger LOG = LoggerFactory.getLogger(EmbeddedMongo.class);

	public static EmbeddedSolrServer createEmbeddedSolrServer(File confDir) throws IOException {
		final File solrHome = new File(System.getProperty("java.io.tmpdir"), "solr-" + System.nanoTime());
		solrHome.mkdirs();
		File coreDir = new File(solrHome, "collection1");
		coreDir.mkdirs();
		File confTargetDir = new File(coreDir, "conf");
		FileUtils.copyDirectoryToDirectory(confDir, coreDir);
		File solrConfigFile = new File(confTargetDir, "solrconfig.xml");
		File schemaFile = new File(confTargetDir, "schema.xml");
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
