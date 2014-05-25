package com.willydupreez.infusionman

import org.gradle.api.PathValidation;
import org.gradle.api.Plugin
import org.gradle.api.Project

class InfusionPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {

		def defaultSiteSrc = project.file(
			new File(project.projectDir, "src/site"),
			PathValidation.DIRECTORY)

		def defaultSiteDist = project.file(
			new File(project.buildDir, "site"),
			PathValidation.DIRECTORY)

		project.extensions.create("infusion", InfusionPluginExtension, defaultSiteSrc, defaultSiteDist)

		project.task("serve", group: "infusion",  type: InfusionServeTask) << {
			serve project.infusion
		}
	}

}

