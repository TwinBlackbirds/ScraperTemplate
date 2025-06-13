// Name: Michael Amyotte
// Date: 6/13/25
// Purpose: Example database object for SQLite database

package tbb.db.Schema;

import java.time.Duration;
import java.time.LocalDateTime;

public class Channel {
	public String ID; // youtube.com/c/?
	public String Name;
	
	public int SubscriberCount;
	public int LastSubscriberCount;
	
	public LocalDateTime Checked;
	public LocalDateTime LastChecked;
	
	public Duration TimeSinceLastFound;
	public int SubscriberCountDifference; // calculated column ?
}
