package ch.epfl.scrumtool.gui;

import android.app.Activity;
import android.os.Bundle;
import ch.epfl.scrumtool.R;

/**
 * @author cyriaquebrousse
 */
public class TaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }
}