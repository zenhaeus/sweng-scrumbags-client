package ch.epfl.scrumtool.gui;

import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.R.layout;
import android.app.Activity;
import android.os.Bundle;

/**
 * @author Cyriaque Brousse
 */
public class ProjectPlayerListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_player_list);
    }
}
