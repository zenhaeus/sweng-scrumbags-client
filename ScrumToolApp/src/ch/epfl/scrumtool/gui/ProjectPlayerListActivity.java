package ch.epfl.scrumtool.gui;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.database.Callback;
import ch.epfl.scrumtool.entity.Player;
import ch.epfl.scrumtool.entity.Project;
import ch.epfl.scrumtool.entity.Role;
import ch.epfl.scrumtool.entity.User;
import ch.epfl.scrumtool.gui.components.PlayerListAdapter;
import ch.epfl.scrumtool.gui.util.InputVerifiers;
import ch.epfl.scrumtool.network.Client;

/**
 * @author Cyriaque Brousse
 */
public class ProjectPlayerListActivity extends Activity {

    private Project project;
    
    private ListView listView;
    private PlayerListAdapter adapter;
    
    private EditText newPlayerEmailView;
    private Player.Builder playerBuilder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_player_list);
        
        initProject();
        initAddPlayerBlock();
        
        Client.getScrumClient().loadPlayers(project, new Callback<List<Player>>() {
            @Override
            public void interactionDone(final List<Player> playerList) {
                adapter = new PlayerListAdapter(ProjectPlayerListActivity.this, playerList);
                listView = (ListView) findViewById(R.id.project_playerlist);
                listView.setAdapter(adapter);
                
                listView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(ProjectPlayerListActivity.this, "User clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                
                adapter.notifyDataSetChanged();
            }
        });
    }
    
    private void initProject() {
        Project project = (Project) getIntent().getSerializableExtra(Project.SERIALIZABLE_NAME);
        if (project != null) {
            this.project = project;
        } else {
            Log.e("ProjectPlayerList", "null project passed");
            this.finish();
        }
    }
    
    private void initAddPlayerBlock() {
        newPlayerEmailView = (EditText) findViewById(R.id.player_list_add_playeremail);
    }
    
    public void addPlayer(View view) {
        InputVerifiers.updateTextViewAfterValidityCheck(newPlayerEmailView, emailIsValid(), getResources());
        
        if (emailIsValid()) {
            String email = newPlayerEmailView.getText().toString();
            
            User.Builder userBuilder = new User.Builder();
            userBuilder.setEmail(email);
            userBuilder.setName(email.toUpperCase()); // TODO real name
            playerBuilder.setUser(userBuilder.build());
            playerBuilder.setId("random player id "+ new Random().nextInt());
            playerBuilder.setIsAdmin(false);
            playerBuilder.setRole(Role.DEVELOPER); // TODO real role
            
            insertPlayer();
        }
    }
    
    private void insertPlayer() {
        Player player = playerBuilder.build();
        Client.getScrumClient().addPlayer(player, project, new Callback<Player>() {
            @Override
            public void interactionDone(Player object) {
                adapter.notifyDataSetChanged();
            }
        });
    }
    
    private void deletePlayer(Player player) {
        // Client.getScrumClient().removePlayer(player, cB);
        // FIXME scrum client must remove player from project
        // adapter.notifyDataSetChanged();
    }
    
    private boolean emailIsValid() {
        String email = newPlayerEmailView.getText().toString();
        return email != null && email.length() > 4 && email.contains("@") && email.length() < 255;
    }
}