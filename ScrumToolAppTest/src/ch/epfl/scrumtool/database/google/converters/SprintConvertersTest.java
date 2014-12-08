package ch.epfl.scrumtool.database.google.converters;

import junit.framework.TestCase;
import ch.epfl.scrumtool.database.google.containers.InsertResponse;
import ch.epfl.scrumtool.database.google.conversion.SprintConverters;
import ch.epfl.scrumtool.entity.Sprint;
import ch.epfl.scrumtool.server.scrumtool.model.KeyResponse;
import ch.epfl.scrumtool.server.scrumtool.model.ScrumSprint;
import ch.epfl.scrumtool.utils.TestConstants;

public class SprintConvertersTest extends TestCase {
    public void testToSprintNullKey() {
        try {
            ScrumSprint sprint = TestConstants.generateBasicScrumSprint();
            sprint.setKey(null);
            
            SprintConverters.SCRUMSPRINT_TO_SPRINT.convert(sprint);
            fail("NullPointerException for invalid ScrumSprint expected");
        } catch (NullPointerException e) {
            
        } catch (Exception e) {
            fail("NullPointerException expected");
        }
    }
    
    public void testToSprintNullTitle() {
        try {
            ScrumSprint sprint = TestConstants.generateBasicScrumSprint();
            sprint.setDate(null);
            
            SprintConverters.SCRUMSPRINT_TO_SPRINT.convert(sprint);
            fail("NullPointerException for invalid ScrumSprint expected");
        } catch (NullPointerException e) {
            
        } catch (Exception e) {
            fail("NullPointerException expected");
        }
        
    }
    
    public void testToSprintNullDate() {
        try {
            ScrumSprint sprint = TestConstants.generateBasicScrumSprint();
            sprint.setTitle(null);
            
            SprintConverters.SCRUMSPRINT_TO_SPRINT.convert(sprint);
            fail("NullPointerException for invalid ScrumSprint expected");
        } catch (NullPointerException e) {
            
        } catch (Exception e) {
            fail("NullPointerException expected");
        }
    }
    
    public void testToSprintValid() {
        ScrumSprint sprint = TestConstants.generateBasicScrumSprint();
        Sprint result = SprintConverters.SCRUMSPRINT_TO_SPRINT.convert(sprint);
        assertEquals(TestConstants.generateBasicSprint(), result);
    }
    
    public void testToScrumSprintEmptyKey() {
        Sprint sprint = TestConstants.generateBasicSprint();
        sprint = sprint.getBuilder()
                .setKey("")
                .build();
        ScrumSprint result = SprintConverters.SPRINT_TO_SCRUMSPRINT.convert(sprint);
        ScrumSprint mustResult = TestConstants.generateBasicScrumSprint();
        mustResult.setKey(null);
        
        assertEquals(mustResult.getKey(), result.getKey());
        assertEquals(mustResult.getTitle(), result.getTitle());
        assertEquals(mustResult.getDate(), result.getDate());
    }
    
    public void testToScrumSprint() {
        KeyResponse response = new KeyResponse();
        response.setKey(TestConstants.validKey);
        Sprint Sprint = TestConstants.generateBasicSprint();
        Sprint = Sprint.getBuilder()
                .setKey("")
                .build();
        InsertResponse<Sprint> insresp = new InsertResponse<Sprint>(Sprint, response);
        Sprint mustResult = TestConstants.generateBasicSprint();
        Sprint keySprint = SprintConverters.INSERTRESPONSE_TO_SPRINT.convert(insresp);
        
        assertEquals(mustResult, keySprint);
    }

}