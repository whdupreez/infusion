package com.willydupreez.infusionman.file

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService

import com.willydupreez.infusionman.InfusionSiteTaskTest

class Watcher extends Thread {

	private File directory
	private InfusionSiteTaskTest watchAction

	private boolean watch

	Watcher(File directory, InfusionSiteTaskTest watchAction) {
		this.directory = directory
		this.watchAction = watchAction
	}

	public void stopWatching() {
		watch = false
	}

	@Override
	void run() {

		println "running"

		watch = true

		Path dirPath = Paths.get(directory.absolutePath, "markdown/articles/osgi")

		WatchService watchService = FileSystems.getDefault().newWatchService()

		dirPath.register(
				watchService,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_MODIFY,
				StandardWatchEventKinds.ENTRY_DELETE);

		while (watch) {

			println "taking"

			WatchKey key = watchService.take()

			println "took key: " + key

			//Poll all the events queued for the key
			for (WatchEvent<?> event: key.pollEvents()){
				WatchEvent.Kind kind = event.kind()
				switch (kind.name()){
					case "ENTRY_CREATE":
					case "ENTRY_MODIFY":
						println "Modified: " + event.context()
						watchAction.site()
						break
					case "ENTRY_DELETE":
						println "Delete: " + event.context()
						break
				}
			}

			// Reset is invoked to put the key back to ready state
			boolean valid = key.reset()

			// If the key is invalid, just exit.
			if (!valid) {
				println "Invalid key. Stopping watcher."
				watch = false
			}
		}

		println "Watcher stopped."
	}

}
