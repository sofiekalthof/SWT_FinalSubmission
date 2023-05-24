package testing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import application.CAApplication;
import dbadapter.GroupCalendar;
import junit.framework.TestCase;


public class CAApplicationTest extends TestCase{
	 public CAApplicationTest() {
	        super();
	    }

	    @Test
	    public void testGetDeadline() {
	        GroupCalendar stub = mock(GroupCalendar.class);
	        GroupCalendar.setInstance(stub);

	        CAApplication.getInstance().getDeadline();

	        // verify that the methods from the getDeadline method can be called at least once and won't throw any exceptions
	        verify(stub, times(1)).markDates();
	        verify(stub, times(1)).removePreDates();
	        verify(stub, times(1)).setParticipants();
	    }
}
