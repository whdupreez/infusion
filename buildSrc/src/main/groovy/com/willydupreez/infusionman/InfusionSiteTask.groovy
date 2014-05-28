package com.willydupreez.infusionman

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory

import com.willydupreez.infusionman.processor.MarkdownProcessor;
import com.willydupreez.infusionman.processor.TemplateProcessor;

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
		TemplateProcessor tProcessor = new TemplateProcessor()
		File template = new File(srcDir, "templates/article.tpl.html")

		// Copy Markdown into site-tmp.
		project.copy {
			from 'src/site/markdown'
			into 'build/site-tmp/md2html'
			include '**/*'
		}

		// Convert Markdown into HTML partials.
		project.fileTree("build/site-tmp/md2html") {
			include "**/*.md"
		}.each { File markdown ->
			mdProcessor.process(markdown, md2htmlOut(markdown))
		}

		// Apply templates to the HTML partials.
		project.fileTree("build/site-tmp/md2html") {
			include "**/*.md2html"
		}.each { File md2html ->
			tProcessor.process(template, md2html, htmlOut(md2html))
		}

		project.copy {
			from 'build/site-tmp/md2html'
			into 'build/site'
			include '**/*.html'
		}

	}

	File md2htmlOut(File markdown) {
		return new File(markdown.parentFile, setExtension(markdown.name,  "md2html"))
	}

	File htmlOut(File md2html) {
		return new File(md2html.parentFile, setExtension(md2html.name,  "html"))
	}

	private String setExtension(String filename, String extension) {
		int idx = filename.lastIndexOf('.')
		String stripped = idx > 0 ? filename.substring(0, idx) : filename
		return stripped + "." + extension
	}

}
