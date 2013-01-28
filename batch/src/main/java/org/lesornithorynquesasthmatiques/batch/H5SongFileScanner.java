package org.lesornithorynquesasthmatiques.batch;

import static java.nio.file.FileVisitResult.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.lesornithorynquesasthmatiques.converter.SongConverter;
import org.lesornithorynquesasthmatiques.hdf.H5SongFileReader;
import org.lesornithorynquesasthmatiques.model.Song;
import org.lesornithorynquesasthmatiques.mongo.MongoWriter;
import org.lesornithorynquesasthmatiques.solr.SolrWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H5SongFileScanner extends SimpleFileVisitor<Path> {
	
	private static final Logger LOG = LoggerFactory.getLogger(H5SongFileScanner.class);

	private SongConverter converter;
	
	private MongoWriter<Song> mongoWriter;

	private SolrWriter<Song> solrWriter;

	private ExecutorService taskPool;

	private TaskSynchronizer taskSynchronizer;
	
	private AtomicInteger totalFiles = new AtomicInteger();

	private AtomicInteger successFiles = new AtomicInteger();

	private AtomicInteger errorFiles = new AtomicInteger();

	private static final PathMatcher MATCHER = FileSystems.getDefault().getPathMatcher("glob:*.h5");
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		return CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exception) throws IOException {
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if (isH5File(file)) {
			LOG.debug("Found file: {}", file);
			totalFiles.incrementAndGet();
			H5SongFileTask task = createTask(file);
			taskSynchronizer.onBeforeTaskSubmitted();
			taskPool.submit(task);
		}
		return CONTINUE;
	}

	public void setConverter(SongConverter converter) {
		this.converter = converter;
	}

	public void setMongoWriter(MongoWriter<Song> mongoWriter) {
		this.mongoWriter = mongoWriter;
	}

	public void setSolrWriter(SolrWriter<Song> solrWriter) {
		this.solrWriter = solrWriter;
	}

	public void setTaskPool(ExecutorService taskPool) {
		this.taskPool = taskPool;
	}

	public void setTaskSynchronizer(TaskSynchronizer taskSynchronizer) {
		this.taskSynchronizer = taskSynchronizer;
	}

	public int getTotalFiles() {
		return totalFiles.get();
	}

	public int getSuccessFiles() {
		return successFiles.get();
	}

	public int getErrorFiles() {
		return errorFiles.get();
	}

	private boolean isH5File(Path file) {
		return MATCHER.matches(file.getFileName());
	}

	private H5SongFileTask createTask(Path file) {
		H5SongFileTask task = new H5SongFileTask();
		task.setReader(new H5SongFileReader(file));
		task.setConverter(converter);
		task.setMongoWriter(mongoWriter);
		task.setSolrWriter(solrWriter);
		task.setTaskSynchronizer(taskSynchronizer);
		task.setSuccessFiles(successFiles);
		task.setErrorFiles(errorFiles);
		return task;
	}

}