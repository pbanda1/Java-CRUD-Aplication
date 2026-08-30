package hr.algebra.humanitarnaorganizacija.task;

import hr.algebra.humanitarnaorganizacija.repo.*;
import hr.algebra.humanitarnaorganizacija.util.XmlWriterExportUtility;
import javafx.concurrent.Task;

public class TaskBackupDatabase extends Task<Integer> {

    private final String path;

    public TaskBackupDatabase(String path) {
        this.path = path;
    }

    @Override
    protected Integer call() throws Exception {
        updateMessage("Reading data from database...");
        var countries = CountryRepo.getInstance().findAll();
        var missions = MissionRepo.getInstance().findAll();
        var volunteers = VolunteerRepo.getInstance().findAll();
        var sponsors = SponsorRepo.getInstance().findAll();
        var campaigns = CampaignRepo.getInstance().findAll();
        var organisations = OrganisationRepo.getInstance().findAll();

        updateMessage("Writing backup XML file...");
        XmlWriterExportUtility.export_Backup(countries, missions, volunteers, sponsors, campaigns, organisations, path);

        int total = countries.size() + missions.size() + volunteers.size() + sponsors.size() + campaigns.size() + organisations.size();
        updateMessage("Backup finished");
        return total;
    }


}
