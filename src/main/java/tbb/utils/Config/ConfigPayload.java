// Name: Michael Amyotte
// Date: 6/13/25
// Purpose: Configurator 'payload' object to marshal in and out of JSON form

package tbb.utils.Config;

public class ConfigPayload {

	// do not remove (I don't know what it does but it is crucial)
	private static final long serialVersionUID = 1L;
	
	// your configuration parameters
	public boolean headless;
	
	
	public ConfigPayload() {
		this(false);
	}
	
	public ConfigPayload(boolean headless) {
		this.headless = headless;
	}
}
