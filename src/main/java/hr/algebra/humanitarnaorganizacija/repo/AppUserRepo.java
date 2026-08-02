package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.AppException;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.AppUser;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AppUserRepo {

    /*singleton - static keyword - objekt se stvara samo jedanput u trenutku kad JVM ucita klasu = EagerSingleton*/
    /*instance je single object stvoren pri učitavanju klase*/
    /*final - jednom stvorena reference INSTANCe se više ne može mijenjati u drugi objekt*/
    private static final AppUserRepo INSTANCE = new AppUserRepo();


    private AppUserRepo() {
    }

    public static AppUserRepo getInstance() {
        return INSTANCE;
    }

    /*kako doci do objekta izvana*/

    /*SQL QUERY as constants*/
    private static final String AppUserInsert = """
            INSERT INTO AppUser(Name, SurName,UserName,PassWord, Role)
            VALUES (?,?,?,?,?)
            """;
    private static final String FindByUserName = """
             SELECT * FROM AppUser
             WHERE LOWER(TRIM(UserName)) = LOWER(TRIM(?));
            """;

    /*MAP ROW vraća redak iz baze kao Java objekt*/
    private AppUser mapRow(ResultSet rs) throws SQLException {
        return new AppUser(
                rs.getInt("ID"),
                rs.getString("Name"),
                rs.getString("SurName"),
                rs.getString("UserName"),
                rs.getString("PassWord"),
                rs.getString("Role")
        );
    }

    public Optional<AppUser> findByUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            return Optional.empty();
        }
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(FindByUserName)) {
            //1 oznacava redni broj [?] u sql upitu!
            statement.setString(1, userName.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                //ResultSet je privremeni spremnik ili tablica s rezultatitma nakon SQL upita
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                    //mapRow -> sluzi za mapiranje retka iz tablice u JavaObjekt
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while finding User", e);
        }
        return Optional.empty();
    }

    public void save(AppUser appUser) throws AppException {
        if (findByUserName(appUser.getUserName()).isPresent()) {
            throw new AppException("User" + appUser.getUserName() + " already exists");
        }
        try (PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(AppUserInsert)) {
            statement.setString(1, appUser.getName() == null ? "" : appUser.getName());
            statement.setString(2, appUser.getSurName() == null ? "" : appUser.getSurName());
            statement.setString(3, appUser.getUserName().trim());
            statement.setString(4, appUser.getPassWord());
            statement.setString(5, appUser.getRole().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Error whilst adding user", e);
        }
    }
}
