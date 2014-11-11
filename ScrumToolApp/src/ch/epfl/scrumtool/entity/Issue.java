package ch.epfl.scrumtool.entity;

import java.io.Serializable;

/**
 * @author Vincent, zenhaeus
 * 
 */
public final class Issue extends AbstractTask implements Serializable {

    public static final String SERIALIZABLE_NAME = "ch.epfl.scrumtool.ISSUE";
    private static final long serialVersionUID = -1590796103232831763L;

    private float estimatedTime;
    private Player player;

    /**
     * @param id
     * @param name
     * @param description
     * @param status
     * @param estimatedTime
     * @param player
     */
    private Issue(String id, String name, String description, Status status,
            float estimatedTime, Player player) {
        super(id, name, description, status);
//        if (player == null) {
//            throw new NullPointerException("Issue.Constructor");
//        }
        this.estimatedTime = estimatedTime;

        this.player = player;

    }

    /**
     * @return the mEstimatedTime
     */
    public float getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * @return the mProgramer
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @author 
     *
     */
    public static class Builder {
        private String id;
        private String name;
        private String description;
        private Status status;
        private float estimatedTime;
        private Player player;

        public Builder() {
            this.name = "";
            this.description = "";
            this.estimatedTime = 0f;
        }

        public Builder(Issue otherIssue) {
            this.id = otherIssue.getKey();
            this.name = otherIssue.getName();
            this.description = otherIssue.getDescription();
            this.status = otherIssue.getStatus();
            this.estimatedTime = otherIssue.estimatedTime;
            this.player = otherIssue.player;
        }

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return the status
         */
        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        /**
         * @return the estimated time
         */
        public float getEstimatedTime() {
            return estimatedTime;
        }

        public void setEstimatedTime(float estimatedTime) {
            this.estimatedTime = estimatedTime;
        }

        /**
         * @return the player
         */
        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Issue build() {
            return new Issue(this.id, this.name, this.description, this.status,
                    this.estimatedTime, this.player);
        }

    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Issue && super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
