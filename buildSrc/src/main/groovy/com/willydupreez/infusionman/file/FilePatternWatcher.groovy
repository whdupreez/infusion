package com.willydupreez.infusionman.file

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey
import java.nio.file.WatchService

class FilePatternWatcher implements Runnable {

	private File directory
	private String pattern

	Thread thread
	private boolean run

	private final WatchService watchService
	private final Map<WatchKey,Path> keys

	FilePatternWatcher(File directory) {
		this.directory = directory
		this.watchService = FileSystems.getDefault().newWatchService()
		this.keys = new HashMap()
	}

	void start() {
		run = true
	}

	void stop() {
		run = false
		thread.join()
	}

	void run() {
		watchRecursively(directory)
	}

	private void watchRecursively(File dir) {
		dir.eachFileRecurse { File file ->
			if (file.isDirectory()) {
				watch(file)
			}
		}

		// Note: Recursive create directories!
		// Note: key.reset() is invalid, directory inaccessible
	}

	private void watch(File dir) {
		Paths.get(dir.toURI()).register(
			watchService,
			StandardWatchEventKinds.ENTRY_CREATE,
			StandardWatchEventKinds.ENTRY_DELETE,
			StandardWatchEventKinds.ENTRY_MODIFY)
	}

}
