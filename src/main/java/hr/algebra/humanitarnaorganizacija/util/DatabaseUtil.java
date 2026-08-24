package hr.algebra.humanitarnaorganizacija.util;
import java.io.IOException;
import java.sql.DriverManager; //otvara vezu prema bazi
import java.sql.Connection; //veza ili cijev prema bazi
import java.sql.SQLException;
import java.sql.Statement;

import hr.algebra.humanitarnaorganizacija.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//EAGER SINGLETON
public final class DatabaseUtil {

    ///LOGGER////

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtil.class);

    private static final String URL = "jdbc:h2:./HumanitarianDB;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE"; //standard za baze u Javi, H2 baza, ./ Datoteka u rootu
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private static final Connection INSTANCE; //1. ovo je jedina veza koju će koristiti cijela aplikacija

    //STATIC BLOK SE PRVI UČITAVA   2. - primjer  EAGER SINGLETONA
    static {
        try {
            INSTANCE = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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

    public static void execSQL(Connection conn) {
        try {
            String ddl = new String(DatabaseUtil.class.getResourceAsStream("/hr/algebra/humanitarnaorganizacija/sql/initDB.sql").readAllBytes());
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
        execSQL(conn);
        log.info("Database initialised");

    }


}

