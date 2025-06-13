package tbb.db.Driver;

// database table objects
import tbb.db.Schema.Channel;
import tbb.utils.Logger.LogLevel;
import tbb.utils.Logger.Logger;

/*
 * Third party libraries
 */
import org.sqlite.JDBC;
import org.sqlite.SQLiteConnection;


public class Sqlite {
	private static Logger log;
	private static final String loc = "database.db";
	public boolean isConnected = false;
	public SQLiteConnection db = null;
	
	
	public Sqlite(Logger log) {
		this.log = log;
		// create db file if it doesnt exist
		
		// initialize database
		db_connect();
		// create tables
		
	}
	private void db_connect() {
		try {
			SQLiteConnection sqlite = JDBC.createConnection(loc, null);
			this.db = sqlite;
			this.isConnected = true;
		} catch (Exception e) {
			log.Write(LogLevel.ERROR, "Could not connect to database!");
			this.db = null;
			this.isConnected = false;
		}
	}
	
	public void connect() {
		if (this.isConnected) {
			return;
		}
		db_connect();
	}
}
