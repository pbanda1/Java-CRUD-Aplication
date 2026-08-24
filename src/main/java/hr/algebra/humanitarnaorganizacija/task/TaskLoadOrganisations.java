package hr.algebra.humanitarnaorganizacija.task;

import hr.algebra.humanitarnaorganizacija.model.*;
import hr.algebra.humanitarnaorganizacija.repo.*;
import hr.algebra.humanitarnaorganizacija.util.XmlParserUtility;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskLoadOrganisations extends Task<Integer> {
   /// XML
   private static final String XML_PATH = "/hr/algebra/humanitarnaorganizacija/data/organisations-import.xml";
    ///LOGGER
    private static final Logger log = LoggerFactory.getLogger(TaskLoadOrganisations.class);


    @Override
    protected Integer call() throws Exception {

        int numOfNewOrganisations  = 0;
        updateMessage("Collecting data(Organisations) from XML file");
        InputStream xml = TaskLoadOrganisations.class.getResourceAsStream(XML_PATH);

        List<Country> countries = CountryRepo.getInstance().findAll();
        List<Mission> missions = MissionRepo.getInstance().findAll();
        List<Volunteer> volunteers = VolunteerRepo.getInstance().findAll();
        List<Sponsor> sponsors = SponsorRepo.getInstance().findAll();
        List<Campaign> campaigns = CampaignRepo.getInstance().findAll();

        updateMessage("Parsing xml");

        //funkcija koja veže Util sa Taskom
        List<Organisation> xmlResults = XmlParserUtility.parse_Organisations(xml, countries, missions, volunteers, sponsors, campaigns);
        OrganisationRepo organisationRepo = OrganisationRepo.getInstance();
        Set<String> existingTitles = organisationRepo.findAll().
                stream().
                map(o -> o.getTitle().toLowerCase())
                .collect(Collectors.toSet());
        for(int i = 0; i<xmlResults.size(); i++) {
            if (isCancelled()) {
                break;
            }
            Organisation o = xmlResults.get(i);
            if (!existingTitles.contains(o.getTitle().toLowerCase())) {
                organisationRepo.save(o);
                existingTitles.add(o.getTitle().toLowerCase());
                numOfNewOrganisations++;
                log.info("Saved new Organisation ({}): {}", numOfNewOrganisations, o.getTitle());
            }
            updateProgress(i + 1, xmlResults.size());
            updateMessage("Loaded " + (i+1) + "/" + xmlResults.size());
        }


        return numOfNewOrganisations;
    }
}
