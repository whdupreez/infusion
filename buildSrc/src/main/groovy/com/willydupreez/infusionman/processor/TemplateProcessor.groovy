package com.willydupreez.infusionman.processor

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

class TemplateProcessor {

	SimpleTemplateEngine engine = new SimpleTemplateEngine()

	def process(File templateFile, File content, File out) {
		Template template = engine.createTemplate(templateFile)
		template.make([
			"content": content.text
		]).writeTo(new FileWriter(out));
	}

}
