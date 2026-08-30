package hr.algebra.humanitarnaorganizacija.util;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import hr.algebra.humanitarnaorganizacija.exception.DatabaseException;
import hr.algebra.humanitarnaorganizacija.poco.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



//EAGER SINGLETON
public final class DatabaseUtil {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final String CONFIG_PATH = "/hr/algebra/humanitarnaorganizacija/data/app-config.xml";
    private static final AppConfig CONFIG = loadConfig();
    private static final String INIT_DB_PATH = "/hr/algebra/humanitarnaorganizacija/sql/initDB.sql";
    private static final String RESET_DB_PATH = "/hr/algebra/humanitarnaorganizacija/sql/resetDB.sql";

    private static AppConfig loadConfig() {
        InputStream xml = DatabaseUtil.class.getResourceAsStream(CONFIG_PATH);
        return ConfigParserUtility.parse_Config(xml);
    }

    private static final Connection INSTANCE; //1. ovo je jedina veza koju će koristiti cijela aplikacija

    //STATIC BLOK SE PRVI UČITAVA   2. - primjer  EAGER SINGLETONA
    static {
        try {
            INSTANCE = DriverManager.getConnection(CONFIG.getUrl(), CONFIG.getUsername(), CONFIG.getPassword());
            log.info("Instance found!");
        } catch (SQLException e) {
            String msg = "Unsuccessful connection to Database";
            log.error(msg, e);
            throw new DatabaseException(msg, e);
        }
    }

    //singleton Pattern 3.
    private DatabaseUtil() {
    }

    //getter za instancu  4.
    public static Connection getConnection() {
        return INSTANCE;  // sadrzi metapodatke za bazu url , username i possword
    }

    public static boolean schemaExists(Connection conn) {
        String sql = "SELECT COUNT(*) FROM Country";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void execSQL(Connection conn , String sqlPath) {
        try {
            String ddl = new String(DatabaseUtil.class.getResourceAsStream(sqlPath).readAllBytes());
            try (Statement stmt = conn.createStatement()) { //napravi statement
                for (String sql_ : ddl.split(";")) { // razreze cijeli tekst na niz naredbi na svakom ";"
                    String trimmedSQL = sql_.trim();
                    if (!trimmedSQL.isEmpty()) {
                        stmt.execute(trimmedSQL); //saljem naredbe bazi CREATE, INSERT ITD
                    }
                }
            }
        } catch (IOException | SQLException e) {
            String msg = "Error whilst executing DDL script";
            log.error(msg, e);
            throw new DatabaseException(msg, e);
        }
    }

    public static void initSchema(Connection conn)  {
        execSQL(conn, INIT_DB_PATH);
        log.info("Database initialised");

    }

    public static void resetDatabase(Connection conn) {
        execSQL(conn, RESET_DB_PATH);
        log.info("Database reset");
    }


}

