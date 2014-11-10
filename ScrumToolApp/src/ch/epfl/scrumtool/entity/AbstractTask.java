package ch.epfl.scrumtool.entity;

import java.io.Serializable;

/**
 * @author Vincent
 */
public abstract class AbstractTask implements Serializable {
    private static final long serialVersionUID = 1197442142563029748L;
    
    private String key;
    private String name;
    private String description;
    private Status status;

    /**
     * @param key
     * @param name
     * @param description
     */
    public AbstractTask(String key, String name, String description, Status status) {
        super();
        if (key == null || name == null || description == null || status == null) {
            throw new NullPointerException("AbstractTask.Constructor");
        }
        this.key = key;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    /**
     * 
     * @return
     */
    public abstract float getEstimatedTime();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the id
     */

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof AbstractTask)) {
            return false;
        }
        AbstractTask other = (AbstractTask) o;
        return other.key.equals(this.key);
    }
    
    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
