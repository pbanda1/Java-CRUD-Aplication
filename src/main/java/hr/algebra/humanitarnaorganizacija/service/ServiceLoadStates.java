package hr.algebra.humanitarnaorganizacija.service;

import hr.algebra.humanitarnaorganizacija.task.TaskLoadStates;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceLoadStates extends Service<Integer> {
    @Override
    protected Task<Integer> createTask() {
        return new TaskLoadStates();
    }
}
