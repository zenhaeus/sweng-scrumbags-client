package ch.epfl.scrumtool.network;

import java.io.IOException;

import android.content.Intent;
import ch.epfl.scrumtool.database.Callback;
import ch.epfl.scrumtool.database.DBHandlers;
import ch.epfl.scrumtool.database.google.AppEngineUtils;
import ch.epfl.scrumtool.database.google.DSIssueHandler;
import ch.epfl.scrumtool.database.google.DSMainTaskHandler;
import ch.epfl.scrumtool.database.google.DSPlayerHandler;
import ch.epfl.scrumtool.database.google.DSProjectHandler;
import ch.epfl.scrumtool.database.google.DSSprintHandler;
import ch.epfl.scrumtool.database.google.DSUserHandler;
import ch.epfl.scrumtool.entity.User;
import ch.epfl.scrumtool.gui.LoginActivity;
import ch.epfl.scrumtool.server.scrumtool.Scrumtool;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;

/**
 * This class provides ways to create and access GoogleSessions, 
 * which are needed to access the Datastore on the Google App Engine
 * 
 * @author aschneuw
 * 
 */
public class GoogleSession extends Session {
    public static final String CLIENT_ID = "756445222019-7ll8hq36aorbfbno1unp49ikle7kt1nv.apps.googleusercontent.com";
    private static Scrumtool dbService = null;
    private Scrumtool authDBService = null;

    private final GoogleAccountCredential googleCredential;

    /**
     * Constructs a GoogleSession with a {@link User} and a {@link GoogleAccountCredential}
     * @param user
     * @param credential
     */
    public GoogleSession(User user, GoogleAccountCredential credential) {
        super(user);
        this.googleCredential = credential;
    }

    /**
     * @return Scrumtool
     * Returns a new Scrumtool service object with GoogleAccoundCredential 
     * for authenticated access to the Datastore
     */
    public Scrumtool getAuthServiceObject() {
        // TODO
        // A pool of service objects???
        if (authDBService == null) {
            authDBService = getServiceObject(googleCredential);
        }

        return authDBService;
    }

    /**
     * @return Scrumtool
     * Returns a new Scrumtool service for access to API methods that do not
     * require authentication.
     */
    public static Scrumtool getServiceObject() {
        if (dbService == null) {
            dbService = getServiceObject(null);
        }
        return dbService;
    }

    private static Scrumtool getServiceObject(GoogleAccountCredential credential) {
        Scrumtool.Builder builder = new Scrumtool.Builder(
                AndroidHttp.newCompatibleTransport(), new GsonFactory(),
                credential);
        builder.setRootUrl(AppEngineUtils.getServerURL());
        builder.setApplicationName(AppEngineUtils.APP_NAME);
        /*
         * The following line fixes an issue with sending UTF-8 characters to
         * the server which caused an IllegalArgumentException
         * 
         * Source
         * https://code.google.com/p/googleappengine/issues/detail?id=11057 post
         * #17
         */
        builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(
                    AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });
        Scrumtool service = builder.build();
        return service;

    }

    /**
     * This class provides functionalities to create a new GoogleSession
     * 
     * @author aschneuw
     *
     */
    public static class Builder {
        private GoogleAccountCredential googleCredential = null;
        
        public Builder(LoginActivity context) {
            googleCredential = GoogleAccountCredential.usingAudience(context,
                    "server:client_id:" + GoogleSession.CLIENT_ID);
        }

        public Intent getIntent() {
            return googleCredential.newChooseAccountIntent();
        }

        /**
         * Create a new GoogleSession
         * @param accName
         * @param authCallback
         */
        public void build(String accName, Callback<Boolean> authCallback) {
            DSUserHandler handler = new DSUserHandler();
            final Callback<Boolean> cB = authCallback;
            googleCredential.setSelectedAccountName(accName);
            /*
             * If the server successfully logs in the user (insert user in case of 
             * non-existence, otherwise return user that wants to login) then the
             * interactionDone function of the Callback is executed.
             */
            handler.loginUser(accName, new Callback<User>() {

                @Override
                public void interactionDone(User object) {
                    if (object != null) {
                        new GoogleSession(object, googleCredential);
                        
                        DBHandlers.Builder builder = new DBHandlers.Builder();
                        builder.setIssueHandler(new DSIssueHandler());
                        builder.setMaintaskHandler(new DSMainTaskHandler());
                        builder.setPlayerHandler(new DSPlayerHandler());
                        builder.setProjectHandler(new DSProjectHandler());
                        builder.setSprintHandler(new DSSprintHandler());
                        builder.setUserHandler(new DSUserHandler());
                        
                        Client.setScrumClient(new DBScrumClient(builder.build()));

                        cB.interactionDone(Boolean.TRUE);
                    } else {
                        cB.interactionDone(Boolean.FALSE);
                    }
                }
            });
        }
    }
}
