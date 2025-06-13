package tbb.apps.JScraper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Configurator {
	// private
	private final String filepathStr = "config.json";
	private final Path filepath = Paths.get(filepathStr);
	private Logger log = null;
	private ConfigPayload data = null;
	
	
	
	public Configurator(Logger log) throws IOException {
		this.log = log;
		
		
		ObjectMapper om = new ObjectMapper();
		if (!Files.exists(filepath)) {
			makeDefaultFile(om);
		}
		try {
			String fStr = Files.readString(filepath);
			this.data = om.readValue(fStr, ConfigPayload.class); // read as json 
		} catch (Exception e) {
			log.Write(LogLevel.ERROR, "Could not read configuration file!");
		}
	}
	
	void makeDefaultFile(ObjectMapper om) {
		log.Write(LogLevel.INFO, "Falling back to default configuration file");
		ConfigPayload defaultPayload = new ConfigPayload();
		try {
			Files.createFile(filepath);
			String jsonStr = om.writeValueAsString(defaultPayload);
			Files.writeString(filepath, jsonStr);
		} catch (Exception e) {
			this.log.Write(LogLevel.ERROR, "Could not make default config.json file!");
		}
	}

	public ConfigPayload getData() {
		return data;
	}
}

class ConfigPayload extends JsonFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String[] hosts = new String[0];
	
	public ConfigPayload() {
		super();
	}
	public ConfigPayload(ArrayList<String> hosts) {
		super();
		this.hosts = hosts.toArray(new String[0]);
	}
}
