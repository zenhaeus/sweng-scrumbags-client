package ch.epfl.scrumtool.gui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.entity.User;
import ch.epfl.scrumtool.exception.NotAuthenticatedException;
import ch.epfl.scrumtool.gui.components.DatePickerFragment;
import ch.epfl.scrumtool.gui.components.DefaultGUICallback;
import ch.epfl.scrumtool.network.Session;
import ch.epfl.scrumtool.util.gui.Validator;

/**
 * @author ketsio
 */
public class ProfileEditActivity extends ScrumToolActivity {

    
    // Date of birth
    private Calendar calendar = Calendar.getInstance();
    private long dateOfBirthChosen = calendar.getTimeInMillis();

    // Views
    private TextView dobDateDisplay;
    private EditText firstNameView;
    private EditText lastNameView;
    private EditText jobTitleView;
    private EditText companyNameView;
    private EditText genderView;

    private User connectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        try {
            connectedUser = Session.getCurrentSession().getUser();
        } catch (NotAuthenticatedException e) {
            // TODO Redirection to login page
            e.printStackTrace();
            this.finish();
        }

        initViews();
        
        if (connectedUser.getDateOfBirth() > 0) {
            dateOfBirthChosen = connectedUser.getDateOfBirth();
            updateDateOfBirth();
        }
    }

    private void initViews() {
        firstNameView = (EditText) findViewById(R.id.profile_edit_firstname);
        lastNameView = (EditText) findViewById(R.id.profile_edit_lastname);
        jobTitleView = (EditText) findViewById(R.id.profile_edit_jobtitle);
        companyNameView = (EditText) findViewById(R.id.profile_edit_company);
        genderView = (EditText) findViewById(R.id.profile_edit_gender);
        dobDateDisplay = (TextView) findViewById(R.id.profile_edit_dateofbirth);

        if (connectedUser.getName().length() > 0) {
            firstNameView.setText(connectedUser.getName());
        }
        if (connectedUser.getLastName().length() > 0) {
            lastNameView.setText(connectedUser.getLastName());
        }
        if (connectedUser.getJobTitle().length() > 0) {
            jobTitleView.setText(connectedUser.getJobTitle());
        }
        if (connectedUser.getCompanyName().length() > 0) {
            companyNameView.setText(connectedUser.getCompanyName());
        }
    }
    

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                dateOfBirthChosen = calendar.getTimeInMillis();
                updateDateOfBirth();
            }
        };
        Bundle args = new Bundle();
        args.putLong("long", dateOfBirthChosen);
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void saveUserChanges(View view) {

        Validator.checkNullableMinAndMax(firstNameView, Validator.SHORT_TEXT);
        Validator.checkNullableMinAndMax(lastNameView, Validator.SHORT_TEXT);
        Validator.checkNullableMinAndMax(jobTitleView, Validator.SHORT_TEXT);
        Validator.checkNullableMinAndMax(companyNameView, Validator.SHORT_TEXT);
        // TODO : gender not a member of user yet
        
        if (firstNameView.getError() == null 
                && lastNameView.getError() == null
                && jobTitleView.getError() == null
                && companyNameView.getError() == null) {
            
            findViewById(R.id.profile_edit_submit_button).setEnabled(false);
            
            User.Builder userBuilder = new User.Builder(connectedUser);
            userBuilder.setName(firstNameView.getText().toString());
            userBuilder.setLastName(lastNameView.getText().toString());
            userBuilder.setJobTitle(jobTitleView.getText().toString());
            userBuilder.setCompanyName(companyNameView.getText().toString());
            userBuilder.setDateOfBirth(dateOfBirthChosen);
            
            final User userToUpdate = userBuilder.build();
            userToUpdate.update(new DefaultGUICallback<Boolean>(this) {
                @Override
                public void interactionDone(Boolean success) {
                    if (success.booleanValue()) {
                        try {
                            Session.getCurrentSession().setUser(userToUpdate);
                            ProfileEditActivity.this.finish();
                        } catch (NotAuthenticatedException e) {
                            // TODO Redirection vers login
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ProfileEditActivity.this, "Could not edit profile", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    

    private void updateDateOfBirth() {
        SimpleDateFormat sdf = new SimpleDateFormat(getResources()
                .getString(R.string.format_date), Locale.ENGLISH);
        dobDateDisplay.setText(sdf.format(dateOfBirthChosen));
    }
}
