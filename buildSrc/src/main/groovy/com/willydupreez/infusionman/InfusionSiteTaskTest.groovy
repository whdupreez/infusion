package com.willydupreez.infusionman

import org.gradle.api.Project

import com.willydupreez.infusionman.processor.MarkdownProcessor
import com.willydupreez.infusionman.processor.TemplateProcessor

class InfusionSiteTaskTest {

	File siteSrc
	File siteDist
	File siteTmp

	private File siteSrcHtml
	private File siteSrcMarkdown
	private File siteSrcResources
	private File siteSrcTemplates

	private File siteTmpMd2html

	private Project project

	public InfusionSiteTaskTest(Project project) {
		this.project = project
	}

	def site() {

		siteSrcHtml 		= new File(siteSrc, "html")
		siteSrcMarkdown 	= new File(siteSrc, "markdown")
		siteSrcResources 	= new File(siteSrc, "resources")
		siteSrcTemplates 	= new File(siteSrc, "templates")

		siteTmpMd2html		= new File(siteTmp, "md2html")

		siteTmp.mkdirs()
		siteTmpMd2html.mkdirs()

		siteDist.mkdirs()

		prepareSiteTmp()
		processMarkdown()
		copySite()

	}

	def prepareSiteTmp() {

		// Access to closure
		def srcMarkdown = siteSrcMarkdown
		def tmpMd2html = siteTmpMd2html

		// Copy Markdown into site-tmp.
		project.copy {
			from srcMarkdown
			into tmpMd2html
			include '**/*.md'
		}
	}

	def processMarkdown() {

		MarkdownProcessor mdProcessor = new MarkdownProcessor()
		TemplateProcessor tProcessor = new TemplateProcessor()
		File template = new File(siteSrcTemplates, "article.tpl.html")

		// Convert Markdown into HTML partials.
		project.fileTree(siteTmpMd2html) {
			include "**/*.md"
		}.each { File markdown ->
			mdProcessor.process(markdown, md2htmlOut(markdown))
		}

		// Apply templates to the HTML partials.
		project.fileTree(siteTmpMd2html) {
			include "**/*.md2html"
		}.each { File md2html ->
			tProcessor.process(template, md2html, htmlOut(md2html))
		}

	}

	def copySite() {

		// Access to closure.
		def srcHtml = siteSrcHtml
		def srcResources = siteSrcResources
		def tmpMd2html = siteTmpMd2html
		def dist = siteDist

		// Copy HTML.
		project.copy {
			from srcHtml
			into dist
			include '**/*.html'
		}

		// Copy resources.
		project.copy {
			from srcResources
			into dist
			include '**/*'
		}

		// Copy processed markdown.
		project.copy {
			from tmpMd2html
			into dist
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
