package com.willydupreez.infusionman

import org.gradle.api.DefaultTask;

class InfusionSiteTask extends DefaultTask {

	def site(InfusionPluginExtension infusion) {

		// Copy HTML.
		project.copy {
			from 'src/site/html'
			into 'build/site'
			include '**/*'
		}

		// Copy resources.
		project.copy {
			from 'src/site/resources'
			into 'build/site'
			include '**/*'
		}

	}

}
