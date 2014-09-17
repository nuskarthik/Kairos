import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

/**************
 * 
 * @author A0090551L
 *
 */
public class TaskListTesting {
//Unit testing for TaskList
	@SuppressWarnings("deprecation")
	@Test
	public void test() {

		Task newTask1 = new Task("meeting", new Date(112, 10, 1, 11, 30, 0), new Date(112, 10, 1, 12, 30, 0), "NUS", "John", Task.Priority.HIGH);
		Task newTask2 = new Task("meeting", null, new Date(112, 10, 1, 0, 0, 0), "YIH", "Max", Task.Priority.LOW);
		Task newTask3 = new Task("meeting", new Date(112, 10, 1, 10, 0, 0) , new Date(112, 10, 1, 11, 0, 0), "Central Lib", "Hari", Task.Priority.LOW);
		Task newTask4 = new Task("borrow books", null, new Date(112, 10, 1, 11, 0, 0), "Central Lib", "Max" , null);
		Task newTask5 = new Task("attend conference", new Date(112, 10, 1, 8, 0, 0), new Date(112, 10, 1, 9, 0, 0), "Suntec city", "Boss", Task.Priority.HIGH);
		Task newTask6 = new Task("have lunch", new Date(112, 10, 1, 11, 30, 0), new Date(112, 10, 1, 12, 30, 0), "Orchart", "Mom", Task.Priority.HIGH);
		
		assertFalse(newTask1.isMatchedDate(null));
		assertTrue(newTask2.isMatchedDate(new Date(112, 10, 1, 0, 0, 0)));
		assertTrue(newTask3.isMatchedDate(new Date(112, 10, 1, 11, 0, 0)));
		
		assertFalse(newTask1.isMatchedKeyword(""));
		assertFalse(newTask1.isMatchedKeyword("Max"));
		assertTrue(newTask1.isMatchedKeyword("John"));
		assertTrue(newTask1.isMatchedKeyword("meeting"));

		assertFalse(newTask1.isSameTask(newTask4));
		assertTrue(newTask2.isSameTask(newTask4));
		assertTrue(newTask3.isSameTask(newTask4));
		assertTrue(newTask4.isSameTask(newTask3));
		
		TaskList listOfTasks = new TaskList();
		assertTrue(listOfTasks.addTask(newTask1));
		assertTrue(listOfTasks.addTask(newTask2));
		assertTrue(listOfTasks.addTask(newTask3));
		assertTrue(listOfTasks.addTask(newTask4));
		assertTrue(listOfTasks.addTask(newTask5));
		assertTrue(listOfTasks.addTask(newTask6));
		
		assertFalse(listOfTasks.isTaskListEmpty());
		
		ArrayList<Task> searchResult;
		searchResult =  listOfTasks.searchTask("John");
		assertTrue(searchResult.get(0).isSameTask(newTask1));
		
		assertTrue(listOfTasks.deleteTask(0));
		searchResult =  listOfTasks.searchTask("John");
		assertFalse(searchResult.isEmpty());
		searchResult =  listOfTasks.searchTask("johN");
		assertFalse(searchResult.isEmpty());
		
		Task tempTask = new Task(null, null, new Date(112, 10, 1, 0, 0, 0), "Central Lib", null , null);
		searchResult =  listOfTasks.searchTask(tempTask);
		assertFalse(searchResult.isEmpty());
		
		assertTrue(listOfTasks.deleteTask(newTask4));
		assertTrue(listOfTasks.undo());
		System.out.println(listOfTasks.toString());
		assertTrue(listOfTasks.undo());
		System.out.println(listOfTasks.toString());
		assertTrue(listOfTasks.undo());
		System.out.println(listOfTasks.toString());
		assertTrue(listOfTasks.undo());
		System.out.println(listOfTasks.toString());
		assertTrue(listOfTasks.undo());
		System.out.println(listOfTasks.toString());
		
		assertTrue(listOfTasks.redo());
		System.out.println(listOfTasks.toString());
		assertTrue(listOfTasks.redo());
		System.out.println(listOfTasks.toString());
		assertTrue(listOfTasks.redo());
		System.out.println(listOfTasks.toString());
		
		
		Task testingTask1 = new Task("go shopping", new Date(112, 10, 1, 10, 30, 0), new Date(112, 10, 1, 13, 0, 0), "Bugis", "Mary", Task.Priority.HIGH);
		Task testingTask2 = new Task("go shopping", new Date(112, 10, 1, 11, 00, 0), new Date(112, 10, 1, 11, 45, 0), "Bugis", "Mary", Task.Priority.HIGH);
		Task testingTask3 = new Task("go shopping", new Date(112, 10, 1, 8, 30, 0), new Date(112, 10, 1, 9, 30, 0), "Bugis", "Mary", Task.Priority.HIGH);
		Task testingTask4 = new Task("go shopping", new Date(112, 10, 1, 9, 30, 0), new Date(112, 10, 1, 9, 45, 0), "Bugis", "Mary", Task.Priority.HIGH);
		
		assertTrue(listOfTasks.isClashing(testingTask1));
		assertTrue(listOfTasks.isClashing(testingTask2));
		assertTrue(listOfTasks.isClashing(testingTask3));
		assertFalse(listOfTasks.isClashing(testingTask4));
		
		System.out.println(listOfTasks.searchDoneTasks(null).toString());
		System.out.println(listOfTasks.searchUndoneTasks(null).toString());
		
		
	}

}
