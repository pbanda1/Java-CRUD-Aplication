package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.AppException;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.Country;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountryRepo implements ICrud<Country, Integer> {
    //SINGLETON PATTERN
    private static final CountryRepo INSTANCE = new CountryRepo();

    private CountryRepo() {
    };

    public static CountryRepo getInstance() {
        return INSTANCE;
    }


    //SELECT STATEMENTS
    private static final String COUNTRY_FIND_ALL = """
            SELECT ID, StateName FROM Country 
            """;
    //INSERT STATEMENT
    private static final String COUNTRY_INSERT = """
            INSERT INTO Country(StateName) 
            VALUES (?)
            """;

    //UPDATE COUNTRY
    private static final String COUNTRY_UPDATE = """
             UPDATE Country 
             SET StateName = ? WHERE Country.ID = ?
            """;

    //DELETE BY ID
    private static final String COUNTRY_DELETE = """
               DELETE FROM Country
               WHERE  Country.ID = ?
            """;

    //FROM DB TO JAVA OBJECT - RESULT SET -> MAP ROW
    private Country mapRow(ResultSet rs) throws SQLException {
        return new Country(
                rs.getInt("ID"),
                rs.getString("StateName"));
    }

    //FIND ALL
    @Override
    public List<Country> findAll() {
        List<Country> countries = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(COUNTRY_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                countries.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RepoException("Error while fetching Countries", e);
        }
        return countries;
    }

    @Override
    public Optional<Country> findById(Integer id) {
        String sql = COUNTRY_FIND_ALL + " WHERE ID = ?";
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Error while finding Countries by ID", e);
        }
        return Optional.empty();
    }

    //save se odnosi na insert u bazu
    @Override
    public void save(Country entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(COUNTRY_INSERT)) {
            preparedStatement.setString(1, entity.getStateName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Error while Saving Coutry", e);
        }

    }

    @Override
    public void deleteById(Integer integer) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(COUNTRY_DELETE)) {
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Invalid ID provided for Country deletion", e);
        }
    }

    @Override
    public void update(Country entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(COUNTRY_UPDATE)) {
            preparedStatement.setString(1, entity.getStateName());
            preparedStatement.setInt(2, entity.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Can not update Country, Wrong ID?", e);
        }
    }
}
