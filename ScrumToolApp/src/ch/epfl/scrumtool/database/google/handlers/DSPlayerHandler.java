/**
 * 
 */
package ch.epfl.scrumtool.database.google.handlers;

import java.util.List;

import ch.epfl.scrumtool.database.Callback;
import ch.epfl.scrumtool.database.PlayerHandler;
import ch.epfl.scrumtool.database.google.containers.InsertPlayerArgs;
import ch.epfl.scrumtool.database.google.containers.InsertResponse;
import ch.epfl.scrumtool.database.google.conversion.CollectionResponseConverters;
import ch.epfl.scrumtool.database.google.conversion.OperationStatusConverters;
import ch.epfl.scrumtool.database.google.conversion.PlayerConverters;
import ch.epfl.scrumtool.database.google.operations.DSExecArgs;
import ch.epfl.scrumtool.database.google.operations.OperationExecutor;
import ch.epfl.scrumtool.database.google.operations.PlayerOperations;
import ch.epfl.scrumtool.database.google.operations.DSExecArgs.Factory;
import ch.epfl.scrumtool.database.google.operations.DSExecArgs.Factory.MODE;
import ch.epfl.scrumtool.entity.Player;
import ch.epfl.scrumtool.entity.Project;
import ch.epfl.scrumtool.entity.Role;
import ch.epfl.scrumtool.server.scrumtool.model.CollectionResponseScrumPlayer;
import ch.epfl.scrumtool.server.scrumtool.model.OperationStatus;

/**
 * @author aschneuw
 * 
 */
public class DSPlayerHandler implements PlayerHandler {

    @Override
    public void insert(final Player player, final Project project,
            final Callback<Player> callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void load(final String playerKey, final Callback<Player> callback) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void update(final Player modified, final Player ref,
            final Callback<Boolean> callback) {
        DSExecArgs.Factory<Player, OperationStatus, Boolean> builder = 
                new DSExecArgs.Factory<Player, OperationStatus, Boolean>(MODE.AUTHENTICATED);
        builder.setCallback(callback);
        builder.setConverter(OperationStatusConverters.OPSTAT_TO_BOOLEAN);
        builder.setOperation(PlayerOperations.UPDATE_PLAYER);
        OperationExecutor.execute(modified, builder.build());
    }

    @Override
    public void remove(final Player player, final Callback<Boolean> callback) {
        DSExecArgs.Factory<String, OperationStatus, Boolean> factory = 
                new DSExecArgs.Factory<String, OperationStatus, Boolean>(MODE.AUTHENTICATED);
        factory.setCallback(callback);
        factory.setConverter(OperationStatusConverters.OPSTAT_TO_BOOLEAN);
        factory.setOperation(PlayerOperations.DELETE_PLAYER);
        OperationExecutor.execute(player.getKey(), factory.build());
    }

    @Override
    public void loadPlayers(final Project project,
            final Callback<List<Player>> callback) {
        DSExecArgs.Factory<String, CollectionResponseScrumPlayer, List<Player>> factory = 
                new Factory<String, CollectionResponseScrumPlayer, List<Player>>(MODE.AUTHENTICATED);
        factory.setCallback(callback);
        factory.setConverter(CollectionResponseConverters.PLAYERS);
        factory.setOperation(PlayerOperations.LOAD_PLAYERS);
        OperationExecutor.execute(project.getKey(), factory.build());
    }

    @Override
    public void insert(Player player, Callback<Player> callback) {
        throw new UnsupportedOperationException();

    }

    /**
     * Add a new Player to a Project
     */
    @Override
    public void addPlayerToProject(final Project project,
            final String userEmail, final Role role,
            final Callback<Player> callback) {
        InsertPlayerArgs args = new InsertPlayerArgs(project, userEmail, role);
        DSExecArgs.Factory<InsertPlayerArgs, InsertResponse<Player>, Player> factory = 
                new DSExecArgs.Factory<InsertPlayerArgs, InsertResponse<Player>, Player>(MODE.AUTHENTICATED);
        factory.setCallback(callback);
        factory.setOperation(PlayerOperations.INSERT_PLAYER_TO_PROJECT);
        factory.setConverter(PlayerConverters.OPSTATPLAYER_TO_PLAYER);
        OperationExecutor.execute(args, factory.build());
    }
}