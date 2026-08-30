package hr.algebra.humanitarnaorganizacija.service;

import hr.algebra.humanitarnaorganizacija.task.TaskBackupDatabase;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ServiceBackupDatabase extends Service<Integer> {
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    protected Task<Integer> createTask() {
        return new TaskBackupDatabase(path);
    }
}
