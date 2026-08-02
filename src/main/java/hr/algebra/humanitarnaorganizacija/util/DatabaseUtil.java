package hr.algebra.humanitarnaorganizacija.util;


import java.sql.DriverManager; //otvara vezu prema bazi
import java.sql.Connection; //veza ili cijev prema bazi
import java.sql.Statement;


//EAGER SINGLETON
public final class DatabaseUtil {

    private static final String URL = "jdbc:h2:./HumanitarianDB;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE"; //standard za baze u Javi, H2 baza, ./ Datoteka u rootu
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    private static final Connection INSTANCE; //1. ovo je jedina veza koju će koristiti cijela aplikacija

    //STATIC BLOK SE PRVI UČITAVA   2. - primjer  EAGER SINGLETONA
    static {
        try {
            INSTANCE = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //singleton Pattern 3.
    private DatabaseUtil() {
    }

    //getter za instancu  4.
    public static Connection getConnection() {
        return INSTANCE;  // sadrzi metapodatke za bazu url , username i possword
    }

    public static void execSQL(Connection conn) throws Exception {
        String ddl = new String(DatabaseUtil.class.getResourceAsStream("/hr/algebra/humanitarnaorganizacija/sql/initDB.sql").readAllBytes());
        //preko imena klase pogledaj resurse kao stream i pretvori u niz bajtova
        try (Statement stmt = conn.createStatement()) { //napravi statement
            for (String sql_ : ddl.split(";")) { // razreze cijeli tekst na niz naredbi na svakom ";"
                String trimmedSQL = sql_.trim();
                if (!trimmedSQL.isEmpty()) {
                    stmt.execute(trimmedSQL); //saljem naredbe bazi CREATE, INSERT ITD
                }
            }
        }
    }

    public static void initSchema(Connection conn) throws Exception {
        execSQL(conn);
        System.out.println("Databnase initialized");
        ;
    }


}

