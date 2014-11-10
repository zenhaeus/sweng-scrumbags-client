package ch.epfl.scrumtool.gui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.database.Callback;
import ch.epfl.scrumtool.entity.MainTask;
import ch.epfl.scrumtool.entity.Project;
import ch.epfl.scrumtool.gui.components.TaskListAdapter;

/**
 * @author Cyriaque Brousse
 */
public class BacklogActivity extends Activity {
    
    private ListView listView;
    private Project project;
    
    private TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);
        
        project = (Project) getIntent().getSerializableExtra(Project.SERIALIZABLE_NAME);
        project.loadBacklog(new Callback<List<MainTask>>() {
            
            @Override
            public void interactionDone(final List<MainTask> taskList) {

                // Get list and initialize adapter
                adapter = new TaskListAdapter(BacklogActivity.this, taskList);
                listView = (ListView) findViewById(R.id.backlog_tasklist);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent openTaskIntent = new Intent(view.getContext(), TaskOverviewActivity.class);
                        MainTask task = taskList.get(position);
                        openTaskIntent.putExtra(MainTask.SERIALIZABLE_NAME, task);
                        startActivity(openTaskIntent);
                    }
                });
                
                adapter.notifyDataSetChanged();
            }
        });
    }
}
