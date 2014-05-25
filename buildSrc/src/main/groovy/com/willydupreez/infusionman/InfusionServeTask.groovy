package com.willydupreez.infusionman

import static io.undertow.Handlers.resource;
import io.undertow.Undertow
import io.undertow.server.handlers.resource.FileResourceManager

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class InfusionServeTask extends DefaultTask {

	def serve(InfusionPluginExtension infusion) {
		startServing(infusion.siteDist, infusion.port)
	}

	private void startServing(File site, int port) {
		logger.println "Starting to serve site: ${site}"

		def siteHandler = resource(new FileResourceManager(site, 100))
				.setDirectoryListingEnabled(true)

		Undertow server = Undertow.builder()
				.addHttpListener(port, "localhost")
				.setHandler(siteHandler)
				.build()
		server.start()

		logger.println "Server started: http://localhost:/${port}"
		logger.println ""
		logger.println "Press any key to continue ..."

		waitForKeyPress()
	}

	private void waitForKeyPress() {
		if (System.console() == null) {
			new BufferedReader(new InputStreamReader(System.in)).readLine()
		} else {
			System.console().readLine()
		}
	}

}
