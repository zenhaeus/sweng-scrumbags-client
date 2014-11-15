/**
 * 
 */
package ch.epfl.scrumtool.database;

import java.util.List;

import ch.epfl.scrumtool.entity.Player;
import ch.epfl.scrumtool.entity.Project;
import ch.epfl.scrumtool.entity.Role;

/**
 * @author Arno
 * 
 */
public interface PlayerHandler extends DatabaseHandler<Player> {
    /**
     * Insert a Player in a Project
     * 
     * @param player
     * @param project
     * @param cB
     */
    void insert(final Player player, final Project project,
            final Callback<Player> cB);

    /**
     * Load the Players of a Project
     * 
     * @param projectKey
     * @param cB
     */
    void loadPlayers(final String projectKey, final Callback<List<Player>> cB);

    /**
     * Add a User to a Project (via a Player)
     * 
     * @param projectKey
     * @param userEmail
     * @param role
     * @param callback
     */
    void addPlayerToProject(final String projectKey, final String userEmail,
            final Role role, final Callback<Player> callback);

}
