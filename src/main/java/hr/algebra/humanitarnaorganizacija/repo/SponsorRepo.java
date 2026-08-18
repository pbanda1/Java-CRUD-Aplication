package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.Sponsor;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SponsorRepo implements ICrud<Sponsor, Integer> {


    /// LOGGING /////
    private static final Logger log = LoggerFactory.getLogger(SponsorRepo.class);

    /// SINGLETON ////////////
    private static final SponsorRepo INSTANCE = new SponsorRepo();

    private SponsorRepo() {
    }

    public static SponsorRepo getInstance() {

        return INSTANCE;
    }

    /// SQL STATEMENTS ////

    public static final String SPONSOR_SAVE_TO_DB = """
            INSERT INTO Sponsor (Name, Surname, DonatorType) 
            VALUES (?,?,?);  
            """;
    public static final String SPONSOR_FIND_ALL = """
            SELECT ID, Name, Surname, DonatorType FROM  Sponsor 
            """;
    public static final String SPONSOR_DELETE_BY_ID = """
            DELETE FROM Sponsor 
            WHERE Sponsor.ID = ?; 
            """;
    public static final String SPONSOR_UPDATE = """
            UPDATE Sponsor SET 
            Name = ?,
            Surname = ?, 
            DonatorType = ? 
            WHERE ID = ?
            """;

    @Override
    public List<Sponsor> findAll() {
        List<Sponsor> sponsors = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(SPONSOR_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                sponsors.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            String msg = "Error while fetching Sponsors";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
        return sponsors;
    }

    private Sponsor mapRow(ResultSet resultSet) throws SQLException {
        return new Sponsor(
                resultSet.getInt("ID"),
                resultSet.getString("Name"),
                resultSet.getString("Surname"),
                Sponsor.DonatorType.valueOf(resultSet.getString("DonatorType"))
        );
    }

    @Override
    public Optional<Sponsor> findById(Integer id) {
        String sql = SPONSOR_FIND_ALL + " WHERE ID = ?";
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            String msg = "Can not find Sponsors by ID";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Sponsor entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(SPONSOR_SAVE_TO_DB)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurName());
            preparedStatement.setString(3, entity.getDonatorType().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not save Sponsor";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }

    @Override
    public void deleteById(Integer integer) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(SPONSOR_DELETE_BY_ID)) {
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not delete Sponsor";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }

    @Override
    public void update(Sponsor entity) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(SPONSOR_UPDATE)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getSurName());
            preparedStatement.setString(3, entity.getDonatorType().name());
            preparedStatement.setInt(4, entity.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = "Can not update Sponsor";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }
}
