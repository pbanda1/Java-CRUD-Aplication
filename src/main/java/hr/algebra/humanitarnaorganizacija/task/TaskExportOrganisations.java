package hr.algebra.humanitarnaorganizacija.task;

import hr.algebra.humanitarnaorganizacija.model.Organisation;
import hr.algebra.humanitarnaorganizacija.repo.OrganisationRepo;
import hr.algebra.humanitarnaorganizacija.util.XmlWriterExportUtility;
import javafx.concurrent.Task;

import java.util.List;

public class TaskExportOrganisations extends Task<Integer> {

    //  client chooses path
    private final String path;

    public TaskExportOrganisations(String path) {
        this.path = path;
    }

    @Override
    protected Integer call() throws Exception {
        int numOfExportedOrganisations;
            updateMessage("Reading Organisations from database....");
        List<Organisation> organisations = OrganisationRepo.getInstance().findAll();
            updateMessage("writing XML file");
        XmlWriterExportUtility.export_Organisations(organisations, path);
            numOfExportedOrganisations = organisations.size();
            updateMessage("export finished");

        return numOfExportedOrganisations;
    }
}
