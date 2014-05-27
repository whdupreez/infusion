package com.willydupreez.infusionman

import org.gradle.api.PathValidation;
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.InputDirectory;

class InfusionPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {

		def defaultSiteSrc = project.file(
			new File(project.projectDir, "src/site"),
			PathValidation.DIRECTORY)

		def defaultSiteDist = new File(project.buildDir, "site")

		project.extensions.create("infusion", InfusionPluginExtension, defaultSiteSrc, defaultSiteDist)

		project.task("site", group: "infusion",  type: InfusionSiteTask) << {
			description = "Builds the site"
			site project.infusion
		}

		project.task("serve", group: "infusion",  type: InfusionServeTask, dependsOn: "site") << {
			description = "Serves the site"
			serve project.infusion
		}
	}

}

