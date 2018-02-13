import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static javax.transaction.xa.XAResource.TMNOFLAGS;
import static javax.transaction.xa.XAResource.TMSUCCESS;

/**
 * Created by pyshankov on 2/10/18.
 */
public class TwoPhaseCommitMain {


    public static void main(String[] args) {

        XAConnection flyConnXA = null;
        Connection flyConn = null;
        XAResource flyXAResource = null;
        Statement flyStatement = null;
        XAConnection hotelConnXA = null;
        Connection hotelConn = null;
        XAResource hotelXAResource = null;
        Statement hotelStatement = null;
        try {
            flyConnXA = ConnectionManager.getFlyDb().getXAConnection();
            flyConn = flyConnXA.getConnection();
            flyXAResource = flyConnXA.getXAResource();
            flyStatement = flyConn.createStatement();

            hotelConnXA = ConnectionManager.getHotelDb().getXAConnection();
            hotelConn = hotelConnXA.getConnection();
            hotelXAResource = hotelConnXA.getXAResource();
            hotelStatement = hotelConn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        //may be generate xid's by JTA?
        Xid xidFly = new MyXid(100, new byte[]{0x01}, new byte[]{0x02});
        Xid xidHotel = new MyXid(100, new byte[]{0x01}, new byte[]{0x22});

        try {
            flyXAResource.start(xidFly, TMNOFLAGS);
            hotelXAResource.start(xidHotel, TMNOFLAGS);

            flyStatement.execute("INSERT INTO flight_booking VALUES (9,'1','1','1','1',CURRENT_TIMESTAMP)");
            hotelStatement.execute("INSERT INTO hotel_booking VALUES (9,'1','1',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)");

            flyXAResource.end(xidFly, TMSUCCESS);
            hotelXAResource.end(xidHotel, TMSUCCESS);

            int ret1 = flyXAResource.prepare(xidFly);
            int ret2 = hotelXAResource.prepare(xidHotel);
            if (ret1 == XAResource.XA_OK & ret2 == XAResource.XA_OK) {
                flyXAResource.commit(xidFly, false);
                hotelXAResource.commit(xidHotel, false);
            } else {
                flyXAResource.rollback(xidFly);
                hotelXAResource.rollback(xidHotel);
            }
        } catch (SQLException | XAException exception) {
                exception.printStackTrace();
            //handle by hand
            //and resolve it immidiatelly

        } finally {
            try {
                flyStatement.close();
                flyConn.close();
                flyConnXA.close();

                hotelStatement.close();
                hotelConn.close();
                hotelConnXA.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
