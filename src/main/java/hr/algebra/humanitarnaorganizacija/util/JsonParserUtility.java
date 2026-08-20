package hr.algebra.humanitarnaorganizacija.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.humanitarnaorganizacija.exception.ApiException;
import hr.algebra.humanitarnaorganizacija.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.classfile.instruction.NewMultiArrayInstruction;
import java.util.ArrayList;
import java.util.List;

public class JsonParserUtility {

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
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Error whilst parsing Json";
            log.error(msg, e);
            throw new ApiException(msg, e);
        }
        return countries;
    }
}
