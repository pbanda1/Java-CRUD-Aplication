package hr.algebra.humanitarnaorganizacija.repo;

import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.interface_.ICrud;
import hr.algebra.humanitarnaorganizacija.model.Volunteer;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VolunteerRepo implements ICrud<Volunteer, Integer> {

    /// LOGGING MECHANISM ////
    private static final Logger log = LoggerFactory.getLogger(VolunteerRepo.class);
    /// SINGLETON //////////
    private static final VolunteerRepo INSTANCE = new VolunteerRepo();

    private VolunteerRepo() {
    }

    public static VolunteerRepo getInstance() {
        return INSTANCE;
    }

    /// SQL STATEMENTS ////////
    private final static String VOLUNTEER_FIND_ALL = """
            SELECT ID, Name, Surname, Specialisation, HoursNum, Status FROM Volunteer
            """;

    private static final String VOLUNTEER_SAVE_TO_DB = """
            INSERT INTO Volunteer (Name, Surname, Specialisation, HoursNum, Status) 
            VALUES (?,?,?,?,?); 
            """;
    private static final String VOLUNTEER_DELETE_BY_ID = """
            DELETE FROM Volunteer
            WHERE Volunteer.ID = ?
            """;
    private static final String VOLUNTEER_UPDATE = """
            UPDATE Volunteer 
            SET 
            Name = ?,
            Surname = ?,
            Specialisation = ?,
            HoursNum = ?,
            Status = ?
                WHERE ID = ? 
            """;
    @Override
    public List<Volunteer> findAll() {
        List<Volunteer> volunteers = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(VOLUNTEER_FIND_ALL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                volunteers.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            String msg = "Error while fetching Volunteers";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
        return volunteers;
    }

    private Volunteer mapRow(ResultSet resultSet) throws SQLException {
        return new Volunteer(
                resultSet.getInt("ID"),
                resultSet.getString("Name"),
                resultSet.getString("Surname"),
                resultSet.getString("Specialisation"),
                resultSet.getInt("HoursNum"),
                Volunteer.VolunteerStatus.valueOf(resultSet.getString("Status"))
        );
    };

    @Override
    public Optional<Volunteer> findById(Integer id) {
        String sql = VOLUNTEER_FIND_ALL + " WHERE ID = ?";
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            String msg = "Error while finding Volunteers by ID";
            log.error(msg, e);
            throw new RepoException(msg,e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Volunteer entity) throws RepoException {
      try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(VOLUNTEER_SAVE_TO_DB)) {
           preparedStatement.setString(1, entity.getName());
           preparedStatement.setString(2, entity.getSurName());
           preparedStatement.setString(3, entity.getSpecialisation());
           preparedStatement.setInt(4,entity.getHoursNum());
           preparedStatement.setString(5, entity.getVolunteerStatus().name());
           preparedStatement.executeUpdate();
      } catch (SQLException e) {
          String msg = "Can not save Volunteer";
          log.error(msg,e);
          throw new RepoException(msg, e);
      }
    }

    @Override
    public void deleteById(Integer integer) throws RepoException {
        try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(VOLUNTEER_DELETE_BY_ID)) {
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            String msg = "Invalid ID provided";
            log.error(msg, e);
            throw new RepoException(msg, e);
        }
    }

    @Override
    public void update(Volunteer entity) throws RepoException {
            try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(VOLUNTEER_UPDATE)) {
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getSurName());
                preparedStatement.setString(3, entity.getSpecialisation());
                preparedStatement.setInt(4,entity.getHoursNum());
                preparedStatement.setString(5, entity.getVolunteerStatus().name());
                //bitan parametar za id
                preparedStatement.setInt(6, entity.getID());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                String msg = "can not update Volunteers";
                log.error(msg, e);
                throw new RepoException(msg, e);
            }
    }
}
