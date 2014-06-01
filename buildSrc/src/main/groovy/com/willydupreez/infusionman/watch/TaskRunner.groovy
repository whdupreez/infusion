package com.willydupreez.infusionman.watch

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection

class TaskRunner {

	File projectDir
	File gradleHomeDir

	TaskRunner(File projectDir, File gradleHomerDir) {
		this.projectDir = projectDir
		this.gradleHomeDir = gradleHomerDir
	}

	public void run() {
		ProjectConnection connection = GradleConnector.newConnector()
				.useInstallation(gradleHomeDir)
				.forProjectDirectory(projectDir)
				.connect()

		connection.newBuild()
				.forTasks("infusionSite")
				.run()
	}

}
