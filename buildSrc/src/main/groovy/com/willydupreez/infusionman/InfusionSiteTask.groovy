package com.willydupreez.infusionman

import java.nio.file.Files
import java.nio.file.Paths

import org.gradle.api.DefaultTask

import com.willydupreez.infusionman.markdown.MarkdownProcessor

class InfusionSiteTask extends DefaultTask {

	private File md2html

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

		processMarkdown()

	}

	def processMarkdown() {

		MarkdownProcessor mdProcessor = new MarkdownProcessor()

		// Copy Markdown into site-tmp.
		project.copy {
			from 'src/site/markdown'
			into 'build/site-tmp/markdown'
			include '**/*'
		}

		// Convert Markdown into HTML.
		md2html = project.file("build/site-tmp/md2html")
		Files.createDirectories(Paths.get(md2html.toURI()))

		project.fileTree("build/site-tmp/markdown") {
			include "**/*.md"
		}.each { File markdown ->
			mdProcessor.process(markdown, htmlOut(markdown))
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
		return new File(md2html, setExtension(markdown.name,  "html"))
	}

	private String setExtension(String filename, String extension) {
		int idx = filename.lastIndexOf('.')
		String stripped = idx > 0 ? filename.substring(0, idx) : filename
		return stripped + "." + extension
	}

}
