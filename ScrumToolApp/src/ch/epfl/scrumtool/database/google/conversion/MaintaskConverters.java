package ch.epfl.scrumtool.database.google.conversion;

import ch.epfl.scrumtool.entity.MainTask;
import ch.epfl.scrumtool.entity.Priority;
import ch.epfl.scrumtool.entity.Status;
import ch.epfl.scrumtool.server.scrumtool.model.ScrumMainTask;

/**
 * Ensures conversiont between ScrumMaintask and Maintask
 * 
 * @author vincent
 * 
 */
public class MaintaskConverters {
    /**
     * Converts a ScrumMainTask into a MainTask
     */
    public static final EntityConverter<ScrumMainTask, MainTask> SCRUMMAINTASK_TO_MAINTASK =
            new EntityConverter<ScrumMainTask, MainTask>() {

        @Override
        public MainTask convert(ScrumMainTask dbMainTask) {
            assert dbMainTask != null;

            MainTask.Builder maintask = new MainTask.Builder();

            String key = dbMainTask.getKey();
            if (key != null) {
                maintask.setKey(key);
            }

            String name = dbMainTask.getName();
            if (name != null) {
                maintask.setName(name);
            }

            String description = dbMainTask.getDescription();
            if (description != null) {
                maintask.setDescription(description);
            }

            String priority = dbMainTask.getPriority();
            if (priority != null) {
                maintask.setPriority(Priority.valueOf(priority));
            }

            String status = dbMainTask.getStatus();
            if (status != null) {
                maintask.setStatus(Status.valueOf(status));
            }

            return maintask.build();
        }
    };

    /**
     * Converts a MainTask into a ScrumMainTask
     */
    public static final EntityConverter<MainTask, ScrumMainTask> MAINTASK_TO_SCRUMMAINTASK =
            new EntityConverter<MainTask, ScrumMainTask>() {

        @Override
        public ScrumMainTask convert(MainTask maintask) {
            assert maintask != null;

            ScrumMainTask dbMainTask = new ScrumMainTask();

            dbMainTask.setKey(maintask.getKey());
            dbMainTask.setName(maintask.getName());
            dbMainTask.setDescription(maintask.getDescription());
            dbMainTask.setPriority(maintask.getPriority().name());
            dbMainTask.setStatus(maintask.getStatus().name());
            // Currently we don't need LastModDate and LastModUser

            return dbMainTask;
        }
    };
}