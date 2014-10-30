package ch.epfl.scrumtool.database.google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.os.AsyncTask;
import ch.epfl.scrumtool.database.DatabaseCallback;
import ch.epfl.scrumtool.database.DatabaseHandler;
import ch.epfl.scrumtool.entity.Issue;
import ch.epfl.scrumtool.entity.Player;
import ch.epfl.scrumtool.entity.User;
import ch.epfl.scrumtool.server.scrumtool.Scrumtool;
import ch.epfl.scrumtool.server.scrumtool.model.ScrumIssue;
import ch.epfl.scrumtool.server.scrumtool.model.ScrumPlayer;
import ch.epfl.scrumtool.server.scrumtool.model.ScrumUser;

/**
 * @author sylb, aschneuw, zenhaeus
 *
 */
public class DSIssueHandler extends DatabaseHandler<Issue> {
	private ScrumIssue scrumIssue;


	@Override
	public void insert(Issue object) {
		scrumIssue = new ScrumIssue();
		scrumIssue.setName(object.getName());
		scrumIssue.setDescription(object.getDescription());
		scrumIssue.setEstimation(object.getEstimatedTime());
		scrumIssue.setAssignedPlayer(new ScrumPlayer());
		Date date = new Date();
		scrumIssue.setLastModDate(date.getTime());
		scrumIssue.setLastModUser(object.getPlayer().getUser().getName());
		InsertIssueTask ii = new InsertIssueTask();
		ii.execute(scrumIssue);
	}


	@Override
	public void load(String key, DatabaseCallback<Issue> dbC) {
		GetIssueTask task = new GetIssueTask(dbC);
		task.execute(key);	
	}


	@Override
	public void loadAll(DatabaseCallback<Issue> dbC) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Issue modified) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Issue object) {
		// TODO Auto-generated method stub

	}

	private class InsertIssueTask extends AsyncTask<ScrumIssue, Void, Void> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(ScrumIssue... params) {
			Scrumtool service = AppEngineUtils.getServiceObject();

			try {
				service.insertScrumIssue(params[0]).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	
    private class GetIssueTask extends AsyncTask<String, Void, ScrumIssue> {
        private DatabaseCallback<Issue> cB;

        public GetIssueTask(DatabaseCallback<Issue> cB) {
            this.cB = cB;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected ScrumIssue doInBackground(String... params) {
            Scrumtool service = AppEngineUtils.getServiceObject();
            ScrumIssue issue = null;
            try {
                issue = service.getScrumIssue(params[0]).execute();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return issue;
        }

        @Override
        protected void onPostExecute(ScrumIssue si) {
            Issue.Builder iB = new Issue.Builder();
            iB.setId(0000); // TODO change this value
            iB.setName(si.getName());
            iB.setDescription(si.getDescription());
            iB.setStatus(ch.epfl.scrumtool.entity.Status.READY_FOR_SPRINT); //TODO change this value
            iB.setEstimatedTime(si.getEstimation());
            Issue issue = iB.build(); // TODO need to add the player
            cB.interactionDone(issue);
        }
    }
}