package hr.algebra.humanitarnaorganizacija.task;

import hr.algebra.humanitarnaorganizacija.exception.ApiException;
import hr.algebra.humanitarnaorganizacija.model.Country;
import hr.algebra.humanitarnaorganizacija.repo.CountryRepo;
import hr.algebra.humanitarnaorganizacija.util.HttpUtility;
import hr.algebra.humanitarnaorganizacija.util.JsonParserUtility;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaskLoadStates extends Task<Integer> {

    /// API
    private static final String DRZAVE_API_URL = "https://countriesnow.space/api/v0.1/countries";
    /// LOGGER
    private static final Logger log = LoggerFactory.getLogger(TaskLoadStates.class);

    @Override
    protected Integer call() throws Exception {

        int numOfNewStates = 0;

        updateMessage("Collecting States from remote API..");

        CountryRepo countryRepo = CountryRepo.getInstance();

        //JSON fetch -> main dio taska -> stop screen freezing by fetching via task on background thread!
        String statesJSON = HttpUtility.fetchJson(DRZAVE_API_URL);
        updateMessage("Parsing JSON");

        List<Country> apiResults = JsonParserUtility.parse_Countries(statesJSON);

        if (apiResults.isEmpty()) {
            throw new ApiException("API returned a response, but not a single state was found");
        }

        //loop over api results
        for (int i = 0; i < apiResults.size(); i++) {
            if (isCancelled()) {
                break;
            }
            //def country
            Country jsonCountry = apiResults.get(i);

            if (countryRepo.saveIfNotExists(jsonCountry)) {
                numOfNewStates++;
                log.info("Saved new country ({}): {}", numOfNewStates, jsonCountry.getStateName());

            }
            //current / fullRangeStates
            updateProgress(i + 1, apiResults.size());
            updateMessage("Loaded " + (i + 1) + "/" + apiResults.size());
        }
        updateMessage("Loading finished!");
        return numOfNewStates;
    }
}
