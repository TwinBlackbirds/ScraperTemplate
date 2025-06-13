package tbb.db.test.Driver;

import tbb.db.Driver.Sqlite;
import tbb.utils.Logger.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class DriverTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DriverTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DriverTest.class );
    }

    
    public void testApp()
    {
    	Logger log = new Logger();
    	Sqlite sql = new Sqlite(log);
        assertTrue( sql.isConnected );
    }
}
