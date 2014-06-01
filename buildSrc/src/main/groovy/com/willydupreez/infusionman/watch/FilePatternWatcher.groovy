package com.willydupreez.infusionman.watch

import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService

import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class FilePatternWatcher implements Runnable {

	private final Logger log = Logging.getLogger(FilePatternWatcher.class)

	private File directory
	private String pattern
	private Closure closure

	private Thread thread
	private boolean run

	private final WatchService watchService
	private final Map<WatchKey, File> keys

	FilePatternWatcher(File directory, Closure closure) {
		this.directory = directory
		this.closure = closure
		this.watchService = FileSystems.getDefault().newWatchService()
		this.keys = new HashMap()
	}

	void start() {
		log.lifecycle "Start watching directory: ${directory}"
		watchRecursively(directory)
		run = true
		thread = new Thread(this)
		thread.start()
	}

	void stop() {
		log.lifecycle "Stop watching directory: ${directory}"
		run = false
		watchService.close()
		thread.join()
	}

	void run() {
		processEvents()
	}

	private void processEvents() {

		while (run) {

			WatchKey key
			try {
				log.lifecycle "Watching ..."
				key = watchService.take()
			} catch (Exception e) {
				continue
			}

			// Poll all the events queued for the key.
			for (WatchEvent<?> event : key.pollEvents()){
				WatchEvent.Kind kind = event.kind()
				switch (kind.name()){
					case "ENTRY_CREATE":
						log.lifecycle "Created"
						watch(event)
						break
					case "ENTRY_MODIFY":
						log.lifecycle "Modified: " + event.context()
						break
					case "ENTRY_DELETE":
						log.lifecycle "Delete: " + event.context()
						break
				}
				closure.call(event.context)
			}

			// Reset is invoked to put the key back to ready state
			boolean valid = key.reset()

			// If the key is invalid, just exit.
			if (!valid) {
				keys.remove(key)
				if (keys.isEmpty()) {
					log.lifecycle "No more directories are accessible. Stopping Watcher."
					run = false
				}
			}
		}
	}

	private void watchRecursively(File dir) {
		dir.eachFileRecurse { File file ->
			if (file.isDirectory()) {
				watch(file)
			}
		}
		// Note: Recursive create directories!
	}

	private void watch(File dir) {
		WatchKey key = Paths.get(dir.toURI()).register(
			watchService,
			StandardWatchEventKinds.ENTRY_CREATE,
			StandardWatchEventKinds.ENTRY_DELETE,
			StandardWatchEventKinds.ENTRY_MODIFY)

		keys.put(key, dir)

		log.lifecycle "Watching directory: " + dir.name
	}

}
