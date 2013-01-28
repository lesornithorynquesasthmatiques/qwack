package org.lesornithorynquesasthmatiques.batch;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Alexandre Dutra
 *
 */
public class BaseDirectoryHelper implements TestRule {

	private File baseDirectory;

	public File getBaseDirectory() {
		return baseDirectory;
	}

	public Statement apply(final Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				before();
				try {
					base.evaluate();
				} finally {
					after();
				}
			}
		};
	}

	protected void before() {
		setUpTestBaseDir();
	}

	protected void after() {
		tearDownTestBaseDir();
	}

	private void setUpTestBaseDir() {
		baseDirectory = new File("target/baseDir" + System.nanoTime());
		baseDirectory.mkdirs();
	}

	private void tearDownTestBaseDir() {
		if (baseDirectory != null && baseDirectory.exists()) {
			FileUtils.deleteQuietly(baseDirectory);
		}
	}

	public void copyFileToBaseDir(File file) throws IOException {
		FileUtils.copyFileToDirectory(file, baseDirectory);
	}

}
