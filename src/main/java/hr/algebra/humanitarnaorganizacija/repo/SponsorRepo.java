package hr.algebra.humanitarnaorganizacija.repo;
import hr.algebra.humanitarnaorganizacija.exception.AppException;
import hr.algebra.humanitarnaorganizacija.exception.RepoException;
import hr.algebra.humanitarnaorganizacija.model.Sponsor;
import hr.algebra.humanitarnaorganizacija.util.DatabaseUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SponsorRepo implements ICrud<Sponsor, Integer> {

    /// SINGLETON ////////////
    private static final SponsorRepo INSTANCE = new SponsorRepo();

    private SponsorRepo() {
    }

    public static SponsorRepo getInstance() {

        return INSTANCE;
    }

    /// SIKVEEEL ////
    public static final String SPONSOR_FIND_ALL = """
            SELECT ID, Name, Surname, DonatorType FROM  Sponsor 
            """;

    @Override
    public List<Sponsor> findAll() {
        List<Sponsor> sponsors = new ArrayList<>();
            try(PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(SPONSOR_FIND_ALL);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sponsors.add(mapRow(resultSet));
                }
            } catch (SQLException e) {
                throw new RepoException("Error while fetching Sponsors",e);
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
    };

    @Override
    public Optional<Sponsor> findById(Integer id) {
      String sql =  SPONSOR_FIND_ALL + " WHERE ID = ?";
      try (PreparedStatement preparedStatement = DatabaseUtil.getConnection().prepareStatement(sql)) {
          preparedStatement.setInt(1, id);
          try (ResultSet resultSet = preparedStatement.executeQuery()) {
              if(resultSet.next()) {
                  return Optional.of(mapRow(resultSet));
              }
          }
      } catch (SQLException e) {
          throw new RepoException("Can not find Sponsors by ID", e);
      }
      return Optional.empty();
    };

    @Override
    public void save(Sponsor entity) throws AppException {

    }

    @Override
    public void deleteById(Integer integer) throws AppException {

    }

    @Override
    public void update(Sponsor entity) throws RepoException {

    }
}
