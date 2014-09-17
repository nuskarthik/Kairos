import static org.junit.Assert.*;
import java.util.Vector;
import org.junit.Test;


public class ControllerTest {
	
	@Test
	public void testSetUserCommand() {
		Controller testObject = new Controller();
		try {
			String commandEntered = "add new task";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			commandEntered = "add new task";
			assertEquals(testObject.setUserCommand(commandEntered), false);
			commandEntered = "done new task";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			assertEquals(testObject.setUserCommand(commandEntered), false);
			commandEntered = "add demo project today";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			commandEntered = "kairos";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			commandEntered = "undone demo project";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			commandEntered = "undo";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			commandEntered = "redo";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			commandEntered = "delete new task";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			commandEntered = "delete demo project today";
			assertEquals(testObject.setUserCommand(commandEntered), true);
			
		} catch (Exception e) {	}
		
	}
	
	@Test
	public void testAllGetAutoCompleteFunctions() {
		Controller testObject = new Controller();
		try {
			KairosGUI.invokeGUI();
			String commandEntered = "add new task";
			assertTrue(testObject.isAdd(commandEntered) == true);
			testObject.setUserCommand(commandEntered);
			commandEntered = "done new task";
			Vector<Integer> idVector= new Vector<>();
			idVector.add(0);
			commandEntered = "edit new task";
			testObject.getAutocompleteSearchResults(commandEntered);
			Vector<Integer> checkVector = testObject.getAutocompleteSearchDoneID();
			assertEquals(checkVector, idVector);
			commandEntered = "add new task today";
			testObject.setUserCommand(commandEntered);
			//this will result in sorting of the database
			idVector.remove(0);
			idVector.add(1);
			commandEntered = "edit new task";
			testObject.getAutocompleteSearchResults(commandEntered);
			checkVector = new Vector<>();
			checkVector = testObject.getAutocompleteSearchDoneID();
			assertEquals(checkVector, idVector);
			commandEntered ="delete new task";
			testObject.setUserCommand(commandEntered);
			commandEntered ="delete new task today";
			testObject.setUserCommand(commandEntered);
		} catch (Exception e) {}
	}
	
	@Test
	public void testGetCommandType() {
		Controller testObject = new Controller();
		String commandEntered = "add meeting with John";
		assertEquals(KairosDictionary.CommandType.ADD, testObject.getCommandType(commandEntered));
		commandEntered = "Add meeting with John";
		assertEquals(KairosDictionary.CommandType.ADD, testObject.getCommandType(commandEntered));
		commandEntered = "ADD meeting with John";
		assertTrue(KairosDictionary.CommandType.ADD == testObject.getCommandType(commandEntered));
		commandEntered = "Ad meeting with John";
		assertFalse(KairosDictionary.CommandType.ADD == testObject.getCommandType(commandEntered));
		
		commandEntered = "edit meeting with John";
		assertTrue(KairosDictionary.CommandType.EDIT == testObject.getCommandType(commandEntered));
		commandEntered = "update meeting with John";
		assertTrue(KairosDictionary.CommandType.EDIT == testObject.getCommandType(commandEntered));
		commandEntered = "change meeting with John";
		assertTrue(KairosDictionary.CommandType.EDIT == testObject.getCommandType(commandEntered));
		commandEntered = "fix meeting with John";
		assertTrue(KairosDictionary.CommandType.EDIT == testObject.getCommandType(commandEntered));
		commandEntered = "postpone meeting with John";
		assertTrue(KairosDictionary.CommandType.EDIT == testObject.getCommandType(commandEntered));
		
		
		commandEntered = "delETE meeting with John";
		assertTrue(KairosDictionary.CommandType.DELETE == testObject.getCommandType(commandEntered));
		
		commandEntered = "done meeting with John";
		assertTrue(KairosDictionary.CommandType.DONE == testObject.getCommandType(commandEntered));
		commandEntered = "finished meeting with John";
		assertTrue(KairosDictionary.CommandType.DONE == testObject.getCommandType(commandEntered));
		commandEntered = "completed meeting with John";
		assertTrue(KairosDictionary.CommandType.DONE == testObject.getCommandType(commandEntered));
		commandEntered = "delETE meeting with John done";
		assertTrue(KairosDictionary.CommandType.DELETE == testObject.getCommandType(commandEntered));

		commandEntered = "undone meeting with John";
		assertTrue(KairosDictionary.CommandType.UNDONE == testObject.getCommandType(commandEntered));
		commandEntered = "incomplete meeting with John";
		assertTrue(KairosDictionary.CommandType.UNDONE == testObject.getCommandType(commandEntered));
		commandEntered = "unfinished meeting with John";
		assertTrue(KairosDictionary.CommandType.UNDONE == testObject.getCommandType(commandEntered));
		
		commandEntered = "kairos";
		assertTrue(KairosDictionary.CommandType.KAIROS == testObject.getCommandType(commandEntered));
	}
	
	@Test
	public void testIsAutocompleteable() {
		Controller testObject = new Controller();
		String commandEntered = "edit meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "update meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "change meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "fix meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "postpone meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		
		
		commandEntered = "delETE meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		
		commandEntered = "done meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "finished meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "completed meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "delETE meeting with John done";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);

		commandEntered = "undone meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "incomplete meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "unfinished meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		
	}
	
	@Test
	public void testIsAdd() {
		Controller testObject = new Controller();
		String commandEntered = "add meeting with John";
		assertTrue(testObject.isAdd(commandEntered) == true);
		commandEntered = "Add meeting with John";
		assertTrue(testObject.isAdd(commandEntered) == true);		
		commandEntered = "ADD meeting with John";
		assertTrue(testObject.isAdd(commandEntered) == true);
		commandEntered = "Ad meeting with John";
		assertFalse(testObject.isAdd(commandEntered) == true);
	}
	
	@Test
	public void testIsEdit() {
		Controller testObject = new Controller();
		String commandEntered = "edit meeting with John";
		assertTrue(testObject.isEdit(commandEntered) == true);
		commandEntered = "update meeting with John";
		assertTrue(testObject.isEdit(commandEntered) == true);
		commandEntered = "change meeting with John";
		assertTrue(testObject.isAdd(commandEntered) == false);
		commandEntered = "fix meeting with John";
		assertTrue(testObject.isAutocompleteable(commandEntered) == true);
		commandEntered = "postpone meeting with John";
		assertTrue(testObject.isDone(commandEntered) == false);
	}
	
	@Test
	public void testIsView() {
		Controller testObject = new Controller();
		String commandEntered = "view meetings";
		assertTrue(testObject.isView(commandEntered) == true);
		commandEntered = "view done today";
		assertTrue(testObject.isView(commandEntered) == true);
		commandEntered = " done view";
		assertTrue(testObject.isView(commandEntered) == false);
	}
	
	@Test
	public void testIsViewDone() {
		Controller testObject = new Controller();
		String commandEntered = "view meetings";
		assertTrue(testObject.isViewDone(commandEntered) == false);
		commandEntered = "view done today";
		assertTrue(testObject.isViewDone(commandEntered) == true);
		commandEntered = " done view";
		assertTrue(testObject.isViewDone(commandEntered) == false);
	}
}

