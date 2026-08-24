package hr.algebra.humanitarnaorganizacija.service;


import hr.algebra.humanitarnaorganizacija.task.TaskLoadOrganisations;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

///  wrapper for xml  Task ////
public class ServiceLoadOrganisations extends Service<Integer> {

    @Override
    protected Task<Integer> createTask() {
        return new TaskLoadOrganisations();
    }

}
