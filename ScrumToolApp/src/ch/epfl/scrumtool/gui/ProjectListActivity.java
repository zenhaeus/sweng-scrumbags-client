package ch.epfl.scrumtool.gui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.entity.Project;
import ch.epfl.scrumtool.gui.components.DefaultGUICallback;
import ch.epfl.scrumtool.gui.components.ProjectListAdapter;
import ch.epfl.scrumtool.network.Client;

/**
 * @author Cyriaque Brousse
 */
public class ProjectListActivity extends BaseListMenuActivity<Project> {

    private ListView listView;
    private ProjectListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        
        this.setTitle("Projects");
        
        final View progressBar = findViewById(R.id.waiting_project_list);
        listView = (ListView) findViewById(R.id.project_list);
        listView.setEmptyView(progressBar);
        
        Client.getScrumClient().loadProjects(new DefaultGUICallback<List<Project>>(this) {
            private final Activity that = ProjectListActivity.this;
            @Override
            public void interactionDone(final List<Project> projectList) {
                if (projectList.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    View emptyList = findViewById(R.id.empty_project_list);
                    listView.setEmptyView(emptyList);
                } else {
                    listView.addFooterView(new View(that));
                    listView.addHeaderView(new View(that));
                }

                adapter = new ProjectListAdapter(ProjectListActivity.this, projectList);
                registerForContextMenu(listView);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent openProjectOverviewIntent = new Intent(
                                view.getContext(),
                                ProjectOverviewActivity.class);
                        // Reduce postion by one to take empty HeaderView into account
                        Project project = projectList.get(position-1);
                        openProjectOverviewIntent.putExtra(Project.SERIALIZABLE_NAME, project);
                        startActivity(openProjectOverviewIntent);
                    }
                });

                adapter.notifyDataSetChanged();
            }
        });
    }
    
    @Override
    protected void onRestart() {
        super.onRestart();
        onCreate(null); // TODO right way to do it? (cyriaque)
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_entitylist_context, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_entity_edit:
                openEditElementActivity(adapter.getItem(info.position));
                return true;
            case R.id.action_entity_delete:
                deleteProject(adapter.getItem(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    
    @Override
    void openEditElementActivity(Project project) {
        Intent openProjectEditIntent = new Intent(this, ProjectEditActivity.class);
        openProjectEditIntent.putExtra(Project.SERIALIZABLE_NAME, project);
        startActivity(openProjectEditIntent);
    }

    /**
     * @param project
     *            the project to delete
     */
    private void deleteProject(final Project project) {
        project.remove(new DefaultGUICallback<Boolean>(this) {
            @Override
            public void interactionDone(Boolean success) {
                adapter.remove(project);
            }
        });
    }
}