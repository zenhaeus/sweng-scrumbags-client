package ch.epfl.scrumtool.entity;

import java.io.Serializable;
import java.util.List;

import ch.epfl.scrumtool.database.Callback;
import ch.epfl.scrumtool.network.Client;

/**
 * @author Vincent, zenhaeus
 * 
 */

public final class MainTask extends AbstractTask implements Serializable, Comparable<MainTask> {

    public static final String SERIALIZABLE_NAME = "ch.epfl.scrumtool.TASK";
    private static final long serialVersionUID = 4279399766459657365L;

    /**
     * @param name
     * @param description
     * @param status
     * @param subtasks
     * @param priority
     */
    private MainTask(String id, String name, String description, Status status, Priority priority) {
        super(id, name, description, status, priority);
    }

    // FIXME not the right way to do it: create super method getIssuesFinishedCount(void)
    @Deprecated
    public int getIssuesFinishedCount(final List<Issue> issues) {
        int finishedCount = 0;
        for (Issue i : issues) {
            if (i.getStatus() == Status.FINISHED) {
                finishedCount++;
            }
        }
        return finishedCount;
    }
    
    @Override
    public float getEstimatedTime() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    // FIXME not the right way of proceeding
    public float estimatedTime(final List<Issue> issues) {
        float total = 0;
        for (Issue i: issues) {
            if (i.getStatus() != Status.FINISHED) {
                total += i.getEstimatedTime();
            }
        }
        
        return (total != 0) ? total : -1;
    }

    /**
     * Creates the maintask in the DS
     * 
     * @param project
     * @param callback
     */
    public void insert(final Project project, final Callback<MainTask> callback) {
        Client.getScrumClient().insertMainTask(this, project, callback);
    }

    /**
     * Updates the maintask in the DS
     * 
     * @param ref
     * @param project
     * @param callback
     */
    public void update(final MainTask ref, final Callback<Boolean> callback) {
        Client.getScrumClient().updateMainTask(this, ref, callback);
    }

    /**
     * Removes the maintask form the DS
     * 
     * @param callback
     */
    public void remove(final Callback<Boolean> callback) {
        Client.getScrumClient().deleteMainTask(this, callback);
    }

    /**
     * Loads the issues of this maintask from the DS
     * 
     * @param callback
     */
    public void loadIssues(final Callback<List<Issue>> callback) {
        Client.getScrumClient().loadIssues(this, callback);
    }
    
    /**
     * Get new instance of Builder
     * @return
     */
    public Builder getBuilder() {
        return new Builder();
    }


    /**
     * Builder class for the MainTask object
     * 
     * @author zenhaeus
     * 
     */
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private Status status;
        private Priority priority;

        public Builder() {
            this.id = "";
            this.name = "";
            this.description = "";
            this.status = Status.READY_FOR_ESTIMATION;
            this.priority = Priority.NORMAL;
        }

        public Builder(MainTask task) {
            this.id = task.getKey();
            this.name = task.getName();
            this.description = task.getDescription();
            this.status = task.getStatus();
            this.priority = task.getPriority();
        }

        /**
         * 
         * @return the id
         */
        public String getKey() {
            return id;
        }

        public MainTask.Builder setKey(String key) {
            if (key != null) {
                this.id = key;
            }
            return this;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public MainTask.Builder setName(String name) {
            if (name != null) {
                this.name = name;
            }
            return this;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description
         *            the description to set
         */
        public MainTask.Builder setDescription(String description) {
            if (description != null) {
                this.description = description;
            }
            return this;
        }

        public Status getStatus() {
            return this.status;
        }

        public MainTask.Builder setStatus(Status status) {
            if (status != null) {
                this.status = status;
            }
            return this;
        }

        public Priority getPriority() {
            return priority;
        }

        public MainTask.Builder setPriority(Priority priority) {
            if (priority != null) {
                this.priority = priority;
            }
            return this;
        }

        public MainTask build() {
            return new MainTask(this.id, this.name, this.description,
                    this.status, this.priority);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof MainTask && super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(MainTask that) {
        final int equal = 0;
        
        if (this == that) {
            return equal;
        }
        
        int comparison = this.getStatus().compareTo(that.getStatus());
        if (comparison != equal) {
            return comparison;
        }
        
        comparison = this.getPriority().compareTo(that.getPriority());
        if (comparison != equal) {
            return comparison;
        }
        
        comparison = this.getName().compareTo(that.getName());
        return comparison;
    }

 
    
}
