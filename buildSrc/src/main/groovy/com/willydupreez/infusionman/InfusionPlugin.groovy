package com.willydupreez.infusionman

import org.gradle.api.PathValidation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.Delete

import com.willydupreez.infusionman.watch.FilePatternWatcher
import com.willydupreez.infusionman.watch.TaskRunner

class InfusionPlugin implements Plugin<Project> {

	private final Logger log = Logging.getLogger(InfusionPlugin.class)

	@Override
	public void apply(Project project) {

		createInfusionPluginExtension(project)

		project.task("cleanInfusion", group: "infusion",  type: Delete) {
			description = "Cleans the site build directories"
			delete project.infusion.siteTmp, project.infusion.siteDist
		}

		project.task("infusionSite", group: "infusion",  type: InfusionSiteTask) {
			description = "Builds the site"
			siteSrc = project.infusion.siteSrc
			siteDist = project.infusion.siteDist
			siteTmp = project.infusion.siteTmp
		}

		project.task("infusionServe", group: "infusion",  type: InfusionServeTask, dependsOn: "infusionSite") {
			description = "Serves the site"
			waitForKeypress = true
			port = project.infusion.port
			siteDist = project.infusion.siteDist
		}

		project.task("infusionWatch", group: "infusion",  type: InfusionServeTask, dependsOn: "infusionSite") {
			description = "Serves the site"
			waitForKeypress = false
			port = project.infusion.port
			siteDist = project.infusion.siteDist

			doLast {
				FilePatternWatcher watcher = new FilePatternWatcher(project.infusion.siteSrc, { path ->
					new TaskRunner(project.rootDir, project.getGradle().getGradleHomeDir()).run()
				})
				watcher.start()
//				InfusionSiteTaskTest task = new InfusionSiteTaskTest(project)
//				task.siteSrc = project.infusion.siteSrc
//				task.siteDist = project.infusion.siteDist
//				task.siteTmp = project.infusion.siteTmp
//				Watcher watcher = new Watcher(project.infusion.siteSrc, task)
//				watcher.start();
				log.lifecycle "Watcher started. Press any key to stop ..."
				if (System.console() == null) {
					new BufferedReader(new InputStreamReader(System.in)).readLine()
				} else {
					System.console().readLine()
				}
				watcher.stop()
			}
		}

	}

	private void createInfusionPluginExtension(Project project) {

		def defaultSiteSrc = project.file(new File(project.projectDir, "src/site"),PathValidation.DIRECTORY)
		def defaultSiteDist = new File(project.buildDir, "site")
		def defaultSiteTmp = new File(project.buildDir, "site-tmp")

		defaultSiteDist.mkdirs()
		defaultSiteTmp.mkdirs()

		project.extensions.create("infusion", InfusionPluginExtension,
			defaultSiteSrc, defaultSiteTmp, defaultSiteDist)
	}

}

