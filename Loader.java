package org.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

class Loader {
	
	
	String loadShader(String path) throws IOException , URISyntaxException {
		
		var url = Loader.class.getResource(path);
		
		if(url == null) throw new IOException("cant find shader at : " + path);
		
		return Files.readString(Paths.get(url.toURI()));
	}
	
}