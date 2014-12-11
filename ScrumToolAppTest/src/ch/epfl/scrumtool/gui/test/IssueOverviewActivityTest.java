package ch.epfl.scrumtool.gui.test;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onData;
import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.clearText;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.closeSoftKeyboard;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.pressBack;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isClickable;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.robotium.solo.Solo;

import android.content.Intent;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Menu;
import static ch.epfl.scrumtool.gui.utils.test.CustomMatchers.withError;
import static ch.epfl.scrumtool.gui.utils.test.CustomMatchers.withPlayer;
import static ch.epfl.scrumtool.gui.utils.test.CustomMatchers.withPriority;
import static ch.epfl.scrumtool.gui.utils.test.CustomMatchers.withSprint;
import static ch.epfl.scrumtool.gui.utils.test.CustomMatchers.withStatusValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import ch.epfl.scrumtool.R;
import ch.epfl.scrumtool.database.Callback;
import ch.epfl.scrumtool.entity.Issue;
import ch.epfl.scrumtool.entity.MainTask;
import ch.epfl.scrumtool.entity.Player;
import ch.epfl.scrumtool.entity.Project;
import ch.epfl.scrumtool.entity.Sprint;
import ch.epfl.scrumtool.gui.IssueOverviewActivity;
import ch.epfl.scrumtool.gui.utils.test.MockData;
import ch.epfl.scrumtool.network.Client;
import ch.epfl.scrumtool.network.DatabaseScrumClient;

/**
 * 
 * @author sylb
 *
 */
public class IssueOverviewActivityTest extends ActivityInstrumentationTestCase2<IssueOverviewActivity> {

    private static final MainTask TASK = MockData.TASK1;
    private static final Project PROJECT = MockData.MURCS;
    private static final Issue ISSUE1 = MockData.ISSUE1;
    private static final Sprint SPRINT1 = MockData.SPRINT1;
    private static final Sprint SPRINT2 = MockData.SPRINT2;
    private static final Player PLAYER1 = MockData.VINCENT_ADMIN;
    private static final Player PLAYER2 = MockData.JOEY_DEV;
    
    private List<Sprint> sprintList = new ArrayList<Sprint>();
    private List<Player> playerList = new ArrayList<Player>();
    
    private static final String TEST_TEXT = MockData.TEST_TEXT;
    private static final String VERY_LONG_TEXT = MockData.VERY_LONG_TEXT;
    private static final String ERROR_MESSAGE = MockData.ERROR_MESSAGE;
    private static final Float ESTIMATION = MockData.ESTIMATION;
    private static final Float LARGE_ESTIMATION = MockData.LARGE_ESTIMATION;
    private static final long THREADSLEEPTIME = MockData.THREADSLEEPTIME;
    
    private Solo solo = null;
    private DatabaseScrumClient mockClient = Mockito.mock(DatabaseScrumClient.class);
    
    public IssueOverviewActivityTest() {
        super(IssueOverviewActivity.class);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        Client.setScrumClient(mockClient);
        
        Intent mockIntent = new Intent();
        mockIntent.putExtra(Project.SERIALIZABLE_NAME, PROJECT);
        mockIntent.putExtra(MainTask.SERIALIZABLE_NAME, TASK);
        mockIntent.putExtra(Issue.SERIALIZABLE_NAME, ISSUE1);
        setActivityIntent(mockIntent);
        getActivity();
    }

    @SuppressWarnings("unchecked")
    private void setSuccessFulLoadOperationsBoth() {
        playerList = MockData.generatePlayerLists();
        sprintList = MockData.generateSprintLists();

        Answer<Void> loadPlayersAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Player>>) invocation.getArguments()[1]).interactionDone(playerList);
                return null;
            }
        };

        Answer<Void> loadSprintsAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Sprint>>) invocation.getArguments()[1]).interactionDone(sprintList);
                return null;
            }
        };

        Mockito.doAnswer(loadPlayersAnswer).when(mockClient).loadPlayers(Mockito.any(Project.class),
                Mockito.any(Callback.class));
        Mockito.doAnswer(loadSprintsAnswer).when(mockClient).loadSprints(Mockito.any(Project.class),
                Mockito.any(Callback.class));
    }

    @SuppressWarnings("unchecked")
    private void setFailedLoadOperationPlayerList() {
        solo = new Solo(getInstrumentation());
        playerList = MockData.generatePlayerLists();
        sprintList = MockData.generateSprintLists();

        Answer<Void> loadPlayersAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Player>>) invocation.getArguments()[1]).failure(ERROR_MESSAGE);
                return null;
            }
        };

        Answer<Void> loadSprintsAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Sprint>>) invocation.getArguments()[1]).interactionDone(sprintList);
                return null;
            }
        };

        Mockito.doAnswer(loadPlayersAnswer).when(mockClient).loadPlayers(Mockito.any(Project.class),
                Mockito.any(Callback.class));
        Mockito.doAnswer(loadSprintsAnswer).when(mockClient).loadSprints(Mockito.any(Project.class),
                Mockito.any(Callback.class));
    }

    @SuppressWarnings("unchecked")
    private void setFailedLoadOperationSprintList() {
        solo = new Solo(getInstrumentation());
        playerList = MockData.generatePlayerLists();
        sprintList = MockData.generateSprintLists();

        Answer<Void> loadPlayersAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Player>>) invocation.getArguments()[1]).interactionDone(playerList);
                return null;
            }
        };

        Answer<Void> loadSprintsAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Sprint>>) invocation.getArguments()[1]).failure(ERROR_MESSAGE);
                return null;
            }
        };

        Mockito.doAnswer(loadPlayersAnswer).when(mockClient).loadPlayers(Mockito.any(Project.class),
                Mockito.any(Callback.class));
        Mockito.doAnswer(loadSprintsAnswer).when(mockClient).loadSprints(Mockito.any(Project.class),
                Mockito.any(Callback.class));
    }

    @SuppressWarnings("unchecked")
    private void setFailedLoadOperationBoth() {
        solo = new Solo(getInstrumentation());
        playerList = MockData.generatePlayerLists();
        sprintList = MockData.generateSprintLists();

        Answer<Void> loadPlayersAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Player>>) invocation.getArguments()[1]).failure(ERROR_MESSAGE+"Player");
                return null;
            }
        };

        Answer<Void> loadSprintsAnswer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<List<Sprint>>) invocation.getArguments()[1]).failure(ERROR_MESSAGE+"Sprint");
                return null;
            }
        };

        Mockito.doAnswer(loadPlayersAnswer).when(mockClient).loadPlayers(Mockito.any(Project.class),
                Mockito.any(Callback.class));
        Mockito.doAnswer(loadSprintsAnswer).when(mockClient).loadSprints(Mockito.any(Project.class),
                Mockito.any(Callback.class));
    }
    
    @LargeTest
    public void testTaskOverviewAllFieldsAreDisplayed() {
        setSuccessFulLoadOperationsBoth();
        // check that all fields are displayed
        onView(withId(R.id.issue_name)).check(matches(isDisplayed()));
        onView(withId(R.id.issue_estimation_stamp)).check(matches(isDisplayed()));
        onView(withId(R.id.issue_status)).check(matches(isDisplayed()));
        onView(withId(R.id.issue_sprint)).check(matches(isDisplayed()));
        onView(withId(R.id.issue_assignee_label)).check(matches(isDisplayed()));
        onView(withId(R.id.issue_assignee_name)).check(matches(isDisplayed()));
        onView(withId(R.id.issue_desc_label)).check(matches(isDisplayed()));
        onView(withId(R.id.issue_desc)).check(matches(isDisplayed()));
        onView(withId(Menu.FIRST)).check(matches(isDisplayed()));
        onView(withId(R.id.action_overflow)).check(matches(isDisplayed()));
    }
    
    @LargeTest
    public void testTaskOverviewCheckClickableFields() {
        setSuccessFulLoadOperationsBoth();
        // check that some fields are clickable
        onView(withId(R.id.issue_name)).check(matches(isClickable()));
        onView(withId(R.id.issue_estimation_stamp)).check(matches(isClickable()));
        onView(withId(R.id.issue_sprint)).check(matches(isClickable()));
        onView(withId(R.id.issue_assignee_label)).check(matches(isClickable()));
        onView(withId(R.id.issue_assignee_name)).check(matches(isClickable()));
        onView(withId(R.id.issue_desc)).check(matches(isClickable()));
        onView(withId(Menu.FIRST)).check(matches(isClickable()));
        onView(withId(R.id.action_overflow)).check(matches(isClickable()));
    }
    
    @SuppressWarnings("unchecked")
    @LargeTest
    public void testOverviewModifyIssue() throws InterruptedException {
        setSuccessFulLoadOperationsBoth();
        // check if the fields are displayed correctly
        onView(withId(R.id.issue_name)).check(matches(withText(ISSUE1.getName())));
//        onView(withId(R.id.issue_estimation_stamp)).check(matches(withText(ISSUE1.getEstimatedTime()));
        onView(withId(R.id.issue_status)).check(matches(withText(ISSUE1.getStatus().toString())));
        onView(withId(R.id.issue_sprint)).check(matches(withText(ISSUE1.getSprint().getTitle())));
        onView(withId(R.id.issue_assignee_name)).check(matches(withText(ISSUE1.getPlayer().getUser().fullname())));
        onView(withId(R.id.issue_desc)).check(matches(withText(ISSUE1.getDescription())));
        
        // fill the modifiable fields with new values
        // set the name
        onView(withId(R.id.issue_name)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());
        onView(withId(R.id.popup_user_input)).perform(typeText(TEST_TEXT));
        onView(withText(android.R.string.ok)).perform(click());
        
        // set the description
        onView(withId(R.id.issue_desc)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());
        onView(withId(R.id.popup_user_input)).perform(typeText(TEST_TEXT));
        onView(withText(android.R.string.ok)).perform(click());
        
        // set the sprint
        onView(withId(R.id.issue_sprint)).perform(click());
        onData(allOf(is(instanceOf(Sprint.class)))).atPosition(1).perform(click());
        
        // set the player
        onView(withId(R.id.issue_assignee_label)).perform(click());
        onData(allOf(is(instanceOf(Player.class)))).atPosition(0).perform(click());
        
     // set the estimation
        onView(withId(R.id.issue_estimation_stamp)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());
        onView(withId(R.id.popup_user_input)).perform(typeText(ESTIMATION.toString()));
        onView(withText(android.R.string.ok)).perform(click());
        
        // check if the values have been changed
        onView(withId(R.id.issue_name)).check(matches(withText(TEST_TEXT)));
        onView(withId(R.id.issue_desc)).check(matches(withText(TEST_TEXT)));
        onView(withId(R.id.issue_sprint)).check(matches(withText(SPRINT2.getTitle())));
//        onView(withId(R.id.issue_assignee_name)).check(matches(withText(PLAYER1.getUser().fullname())));
    }
    
    @LargeTest
    public void testEditIssueBadUserInputs() throws InterruptedException {

        setSuccessFulLoadOperationsBoth();
        Resources res = getInstrumentation().getTargetContext().getResources();

        nameIsEmpty(res);
        descriptionIsEmpty(res);
        estimationIsEmpty(res);
        largeInputForTheName(res);
        largeInputForTheEstimation(res);
    }
    
    private void nameIsEmpty(Resources res) {
        // empty the issue name
        onView(withId(R.id.issue_name)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());

        // check the value in the field is empty
        onView(withId(R.id.popup_user_input)).check(matches(withText("")));

        // click on save button and check the error on the name
        onView(withText(android.R.string.ok)).perform(click());
        onView(withId(R.id.popup_user_input)).check(matches(withError(res.getString(R.string.error_field_required))));
        onView(withId(R.id.popup_user_input)).perform(pressBack());
    }
    
    private void descriptionIsEmpty(Resources res) {
        // empty the issue description
        onView(withId(R.id.issue_desc)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());

        // check the value in the field is empty
        onView(withId(R.id.popup_user_input)).check(matches(withText("")));

        // click on save button and check the error on the description
        onView(withText(android.R.string.ok)).perform(click());
        onView(withId(R.id.popup_user_input)).check(matches(withError(res.getString(R.string.error_field_required))));
        onView(withId(R.id.popup_user_input)).perform(pressBack());
    }
    
    private void estimationIsEmpty(Resources res) {
        // empty the issue estimation
        onView(withId(R.id.issue_estimation_stamp)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());

        // check the value in the field is empty
        onView(withId(R.id.popup_user_input)).check(matches(withText("")));

        // click on save button and check the error on the description
        onView(withText(android.R.string.ok)).perform(click());
        onView(withId(R.id.popup_user_input)).check(matches(withError(res.getString(R.string.error_field_required))));
        onView(withId(R.id.popup_user_input)).perform(pressBack());
    }
    
    private void largeInputForTheName(Resources res) {
        // fill the issue name with a large input
        onView(withId(R.id.issue_name)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());
        onView(withId(R.id.popup_user_input)).perform(typeText(VERY_LONG_TEXT));

        // check the value in the field is correct
        onView(withId(R.id.popup_user_input)).check(matches(withText(VERY_LONG_TEXT)));

        // click on save button and check the error on the name
        onView(withText(android.R.string.ok)).perform(click());
        onView(withId(R.id.popup_user_input)).check(matches(withError(res.getString(R.string.error_name_too_long))));
        onView(withId(R.id.popup_user_input)).perform(pressBack());
        onView(withId(R.id.popup_user_input)).perform(pressBack());
    }
    
    private void largeInputForTheEstimation(Resources res) {
        // fill the issue estimation with a large input
        onView(withId(R.id.issue_estimation_stamp)).perform(click());
        onView(withId(R.id.popup_user_input)).perform(clearText());
        onView(withId(R.id.popup_user_input)).perform(typeText(LARGE_ESTIMATION.toString()));

        // check the value in the field is correct
        onView(withId(R.id.popup_user_input)).check(matches(withText(LARGE_ESTIMATION.toString())));

        // click on save button and check the error on the estimation
        onView(withText(android.R.string.ok)).perform(click());
        onView(withId(R.id.popup_user_input)).check(matches(withError(res.getString(R.string.error_estimation_too_big))));
        onView(withId(R.id.popup_user_input)).perform(pressBack());
        onView(withId(R.id.popup_user_input)).perform(pressBack());
    }
    
//    FIXME : Tests do not terminate
//    @LargeTest
//    public void testloadPlayerFailed() throws Exception {
//        setFailedLoadOperationPlayerList();
//        onView(withId(R.id.issue_assignee_label)).perform(click());
//        assertTrue(solo.waitForText(ERROR_MESSAGE));
//    }
    
//    @LargeTest
//    public void testloadSprintFailed() throws Exception {
//        setFailedLoadOperationSprintList();
//        onView(withId(R.id.issue_sprint)).perform(click());
//        assertTrue(solo.waitForText(ERROR_MESSAGE));
//    }
    
//    @LargeTest
//    public void testloadBothFailed() throws Exception {
//        setFailedLoadOperationBoth();
//        onView(withId(R.id.issue_assignee_label)).perform(click());
//        assertTrue(solo.waitForText(ERROR_MESSAGE+"Player"));
//        Thread.sleep(THREADSLEEPTIME);
//        onView(withId(R.id.issue_sprint)).perform(click());
//        assertTrue(solo.waitForText(ERROR_MESSAGE+"Sprint"));
//    }
    

}
