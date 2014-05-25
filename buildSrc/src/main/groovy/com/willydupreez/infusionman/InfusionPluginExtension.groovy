package com.willydupreez.infusionman;

class InfusionPluginExtension {

	File siteSrc
	File siteDist

	int port = 9090

	InfusionPluginExtension(File siteSrc, File siteDist) {
		this.siteSrc = siteSrc
		this.siteDist = siteDist
	}
}