import org.postgresql.xa.PGXADataSource;

import javax.sql.XADataSource;

/**
 * Created by pyshankov on 2/10/18.
 */
public class ConnectionManager {

    private static PGXADataSource xaDataSourceFly;

    private static PGXADataSource xaDataSourceHotel;


    public static XADataSource getFlyDb() {
        if (xaDataSourceFly == null) {
            xaDataSourceFly = new PGXADataSource();
            xaDataSourceFly.setDatabaseName("fly-db");
            xaDataSourceFly.setUser("postgres");
            xaDataSourceFly.setPassword("postgres");
            // log level DEBUG = 2, INFO = 1
            xaDataSourceFly.setLogLevel(2);
        }
        return xaDataSourceFly;
    }

    public static XADataSource getHotelDb() {
        if (xaDataSourceHotel == null) {
            xaDataSourceHotel = new PGXADataSource();
            xaDataSourceHotel.setDatabaseName("hotel-db");
            xaDataSourceHotel.setUser("postgres");
            xaDataSourceHotel.setPassword("postgres");
            // log level DEBUG = 2, INFO = 1
            xaDataSourceHotel.setLogLevel(2);
        }
        return xaDataSourceHotel;
    }


}
