package com.willydupreez.infusionman

import java.nio.file.Files
import java.nio.file.Paths

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectories;
import org.gradle.api.tasks.OutputDirectory;

import com.willydupreez.infusionman.markdown.MarkdownProcessor

class InfusionSiteTask extends DefaultTask {

	@InputDirectory
	File srcDir = project.file("src/site")

	@OutputDirectory
	File siteDir = project.file("build/site")

	@OutputDirectory
	File tmpDir = project.file("build/site-tmp")

	def site(InfusionPluginExtension infusion) {

//		srcDir.mkdirs()
		siteDir.mkdirs()
		tmpDir.mkdirs()

		new File(project.buildDir, "site").mkdirs()
		new File(project.buildDir, "site-tmp/md2html").mkdirs()

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

		processMarkdown()

	}

	def processMarkdown() {

		MarkdownProcessor mdProcessor = new MarkdownProcessor()

		// Copy Markdown into site-tmp.
		project.copy {
			from 'src/site/markdown'
			into 'build/site-tmp/md2html'
			include '**/*'
		}

		// Convert Markdown into HTML.
		project.fileTree("build/site-tmp/md2html") {
			include "**/*.md"
		}.each { File markdown ->
			mdProcessor.process(markdown, htmlOut(markdown))
		}

		project.copy {
			from 'build/site-tmp/md2html'
			into 'build/site'
			include '**/*.html'
		}

//		// Convert Markdown into HTML.
//		project.copy {
//			from 'build/site-tmp/markdown'
//			into 'build/site-tmp/md2html'
//			include '**/*'
//		}
//
//		// Copy generated HTML into site.
//		project.copy {
//			from 'build/site-tmp/md2html'
//			into 'build/site'
//			include '**/*'
//		}

	}

	File htmlOut(File markdown) {
		return new File(markdown.parentFile, setExtension(markdown.name,  "html"))
	}

	private String setExtension(String filename, String extension) {
		int idx = filename.lastIndexOf('.')
		String stripped = idx > 0 ? filename.substring(0, idx) : filename
		return stripped + "." + extension
	}

}
