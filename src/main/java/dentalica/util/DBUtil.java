package dentalica.util;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtil {

    private static final String SERVER_ADDRESS = "localhost";
    private static final Integer SERVER_PORT = 5432;
    private static final String DATABASE_NAME = "dentalica";
    private static final String DATABASE_USERNAME = "postgres";

    public static Connection connect() {
        var dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{SERVER_ADDRESS});
        dataSource.setPortNumbers(new int[]{SERVER_PORT});
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setUser(DATABASE_USERNAME);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
