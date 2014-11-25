package ch.epfl.scrumtool.gui.components;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.entity.Issue;
import ch.epfl.scrumtool.entity.Player;

/**
 * @author Cyriaque Brousse
 */
public final class IssueListAdapter extends DefaultAdapter<Issue> {
    private Activity activity;
    private LayoutInflater inflater;

    public IssueListAdapter(final Activity activity, final List<Issue> issueList) {
        super(issueList);
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_issue, parent, false);
        }

        TextView estimation = (TextView) convertView.findViewById(R.id.issue_row_quantity);
        TextView unit = (TextView) convertView.findViewById(R.id.issue_row_unit);
        TextView name = (TextView) convertView.findViewById(R.id.issue_row_name);
        TextView assignee = (TextView) convertView.findViewById(R.id.issue_row_assignee);
        View divider = (View) convertView.findViewById(R.id.issue_row_divider);
        
        Issue issue = getList().get(position);
        name.setText(issue.getName());

        Player optionAssignee = issue.getPlayer();
        assignee.setText(optionAssignee != null ? optionAssignee.getUser().fullname() : "<No one assigned>");
        
        estimation.setText(issue.getEstimatedTime() == 0 ? "-" : Float.toString(issue.getEstimatedTime()));
        unit.setText(activity.getResources().getString(R.string.project_default_unit));
        divider.setBackgroundColor(activity.getResources().getColor(issue.getStatus().getColorRef()));
        
        return convertView;
    }

}
