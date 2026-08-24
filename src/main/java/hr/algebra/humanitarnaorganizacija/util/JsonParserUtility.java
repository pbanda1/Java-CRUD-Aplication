package hr.algebra.humanitarnaorganizacija.util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.humanitarnaorganizacija.exception.ApiException;
import hr.algebra.humanitarnaorganizacija.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public final class JsonParserUtility {

    /// LOGGER ////
    private static final Logger log = LoggerFactory.getLogger(JsonParserUtility.class);

    /// OBJECT MAPPER //
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonParserUtility() {
    }

    /// METHOD FOR COUNTRIES ///
    public static List<Country> parse_Countries(String json) {
        List<Country> countries = new ArrayList<>();
        try {
            JsonNode root = MAPPER.readTree(json);
            if(!root.has("error")) {
                String msg = "API hasn't returned expected JSON shape";
                log.error(msg);
                throw new ApiException(msg);
            }

            boolean error = root.path("error").asBoolean(false);

            if (error) {
                String msg = "Countries NOW API error: " +  root.path("msg").asText("Unknown ERROR");
                log.error(msg);
                throw new ApiException(msg);
            }
            JsonNode data = root.path("data");

            if (!data.isArray()) {
                String msg = "Countries NOW did not return data as ArrayList";
                log.error(msg);
                throw new ApiException(msg);
            }

            for(JsonNode node: data) {
                String stateName = node.path("country").asText("").trim();
                if(!stateName.isBlank()) {
                    countries.add(new Country(stateName));
                }
            }

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Error whilst parsing Json";
            log.error(msg, e);
            throw new ApiException(msg, e); //logiram grešku i omotam u ApiExc
        }
        return countries;
    }
}
