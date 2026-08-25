package hr.algebra.humanitarnaorganizacija.service;

import hr.algebra.humanitarnaorganizacija.task.TaskExportOrganisations;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceExportOrganisations extends Service<Integer> {
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    protected Task<Integer> createTask() {
        return new TaskExportOrganisations(path);
    }
}
