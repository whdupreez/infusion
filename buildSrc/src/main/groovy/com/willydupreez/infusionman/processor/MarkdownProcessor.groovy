package com.willydupreez.infusionman.processor

import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.pegdown.Extensions
import org.pegdown.PegDownProcessor

class MarkdownProcessor {

	private PegDownProcessor processor

	MarkdownProcessor() {
		processor = new PegDownProcessor(Extensions.ALL)
	}

	def process(File markdownFile, File htmlFile) {
		String markdown = new String(Files.readAllBytes(Paths.get(markdownFile.toURI())))
		String html = processor.markdownToHtml(markdown)
		Files.write(
			Paths.get(htmlFile.toURI()),
			html.getBytes(),
			StandardOpenOption.CREATE,
			StandardOpenOption.TRUNCATE_EXISTING,
			StandardOpenOption.WRITE);
	}

}
