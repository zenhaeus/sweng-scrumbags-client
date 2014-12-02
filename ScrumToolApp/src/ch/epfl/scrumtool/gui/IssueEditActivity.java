package ch.epfl.scrumtool.gui;

import static ch.epfl.scrumtool.util.Preconditions.throwIfNull;
import static ch.epfl.scrumtool.util.gui.InputVerifiers.entityNameIsValid;
import static ch.epfl.scrumtool.util.gui.InputVerifiers.textEditNonNullNotEmpty;
import static ch.epfl.scrumtool.util.gui.InputVerifiers.updateTextViewAfterValidityCheck;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.entity.Issue;
import ch.epfl.scrumtool.entity.MainTask;
import ch.epfl.scrumtool.entity.Player;
import ch.epfl.scrumtool.entity.Priority;
import ch.epfl.scrumtool.entity.Project;
import ch.epfl.scrumtool.entity.Sprint;
import ch.epfl.scrumtool.entity.Status;
import ch.epfl.scrumtool.gui.components.DefaultGUICallback;
import ch.epfl.scrumtool.gui.components.PlayerListAdapter;
import ch.epfl.scrumtool.gui.components.SprintListAdapter;

/**
 * @author Cyriaque Brousse
 * @author sylb
 */
public class IssueEditActivity extends BaseMenuActivity {

    private EditText issueNameView;
    private EditText issueDescriptionView;
    private EditText issueEstimationView;
    
    private PlayerListAdapter playerAdapter;
    private SprintListAdapter sprintAdapter;
    private Spinner issueAssigneeSpinner;
    private Spinner sprintSpinner;


    /** If {@code null} then we are in create mode, otherwise in edit mode*/
    private Issue original;
    private MainTask parentTask;
    private Issue.Builder issueBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_edit);
        
        initOriginalAndParentTask();
        initViews();

        Project project = (Project) getIntent().getSerializableExtra(Project.SERIALIZABLE_NAME);
        project.loadPlayers(new DefaultGUICallback<List<Player>>(this) {
            @Override
            public void interactionDone(List<Player> playerList) {
                playerList.add(0, null);
                playerAdapter = new PlayerListAdapter(IssueEditActivity.this, playerList);
                issueAssigneeSpinner.setAdapter(playerAdapter);
                
                if (issueBuilder.getPlayer() == null) {
                    issueAssigneeSpinner.setSelection(0);
                } else {
                    issueAssigneeSpinner.setSelection(playerAdapter.getList().indexOf(issueBuilder.getPlayer()));
                }
            }
        });
        
        project.loadSprints(new DefaultGUICallback<List<Sprint>>(this) {
            @Override
            public void interactionDone(List<Sprint> sprintList) {
                sprintList.add(0, null);
                sprintAdapter = new SprintListAdapter(IssueEditActivity.this, sprintList);
                sprintSpinner.setAdapter(sprintAdapter);
                
                if (issueBuilder.getSprint() == null) {
                    sprintSpinner.setSelection(0);
                } else {
                    sprintSpinner.setSelection(sprintAdapter.getList().indexOf(issueBuilder.getSprint()));
                }
            }
        });
    }

    private void initOriginalAndParentTask() {
        original = (Issue) getIntent().getSerializableExtra(Issue.SERIALIZABLE_NAME);
        if (original == null) {
            issueBuilder = new Issue.Builder();
            setTitle(R.string.title_activity_issue_edit_new);
        } else {
            issueBuilder = new Issue.Builder(original);
        }

        parentTask = (MainTask) getIntent().getSerializableExtra(MainTask.SERIALIZABLE_NAME);
        throwIfNull("Parent task cannot be null", parentTask);
    }

    private void initViews() {
        issueNameView = (EditText) findViewById(R.id.issue_name_edit);
        issueDescriptionView = (EditText) findViewById(R.id.issue_description_edit);
        issueEstimationView = (EditText) findViewById(R.id.issue_estimation_edit);
        issueAssigneeSpinner = (Spinner) findViewById(R.id.issue_assignee_spinner);
        sprintSpinner = (Spinner) findViewById(R.id.sprint_spinner);

        issueNameView.setText(issueBuilder.getName());
        issueDescriptionView.setText(issueBuilder.getDescription());
        issueEstimationView.setText(Float.toString(issueBuilder.getEstimatedTime()));
        
    }

    public void saveIssueChanges(View view) {
        boolean nameIsValid = entityNameIsValid(issueNameView);
        boolean descriptionIsValid = textEditNonNullNotEmpty(issueDescriptionView);
        
        updateTextViewAfterValidityCheck(issueNameView, nameIsValid, getResources());
        updateTextViewAfterValidityCheck(issueDescriptionView, descriptionIsValid, getResources());

        if (nameIsValid && descriptionIsValid) {
            findViewById(R.id.issue_edit_button_next).setEnabled(false);
            String newName = issueNameView.getText().toString();
            String newDescription = issueDescriptionView.getText().toString();
            float newEstimation = Float.parseFloat(issueEstimationView.getText().toString());

            issueBuilder.setName(newName);
            issueBuilder.setDescription(newDescription);
            issueBuilder.setEstimatedTime(newEstimation);
            setStatusAccordingToEstimation(newEstimation);
            issueBuilder.setPriority(Priority.NORMAL); // TODO for now issue priorities are useless
            issueBuilder.setPlayer((Player) issueAssigneeSpinner.getSelectedItem());
            issueBuilder.setSprint((Sprint) sprintSpinner.getSelectedItem());
            if (sprintSpinner.getSelectedItem() != null) {
                issueBuilder.setStatus(Status.IN_SPRINT);
            } else {
                issueBuilder.setStatus(Status.READY_FOR_SPRINT);
            }

            if (original == null) {
                insertIssue();
            } else {
                updateIssue();
            }
        }
    }

    private void setStatusAccordingToEstimation(float estimation) {
        if (estimation > 0) {
            issueBuilder.setStatus(Status.READY_FOR_ESTIMATION);
        } else {
            issueBuilder.setStatus(Status.READY_FOR_SPRINT);
        }
    }

    private void insertIssue() {
        Issue issue = issueBuilder.build();
        issue.insert(parentTask, new DefaultGUICallback<Issue>(this) {
            @Override
            public void interactionDone(Issue issue) {
                IssueEditActivity.this.finish();
            }
        });
    }

    private void updateIssue() {
        final Issue issue = issueBuilder.build();
        issue.update(null, new DefaultGUICallback<Boolean>(this) {
            @Override
            public void interactionDone(Boolean success) {
                if (success.booleanValue()) {
                    passResult(issue);
                    IssueEditActivity.this.finish();
                } else {
                    Toast.makeText(IssueEditActivity.this, "Could not update issue", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.issue_edit_button_next).setEnabled(true);
                }
            }
        });
    }
    
    private void passResult(Issue issue) {
        Intent intent = new Intent();
        intent.putExtra(Issue.SERIALIZABLE_NAME, issue);
        setResult(1, intent);
    }
}
