package ch.epfl.scrumtool.database;

/**
 * @author zenhaeus
 *
 * @param <A> type of object that will be handed to callback function
 */
public interface Callback<A> {
    /**
     * @param object
     */
    void interactionDone(A object);
}