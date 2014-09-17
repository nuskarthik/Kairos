import java.util.Date;
import java.util.ArrayList;
import org.junit.Test;


public class StorageTest {

	@Test
	public void testWriteToFile() throws NullPointerException {
		Storage database = new Storage();
		ArrayList<Task> list = new ArrayList<>();

		list.add(new Task("Meeting John", null, null, null, null,
				Task.Priority.HIGH));
		list.add(new Task("Meeting John", null, null, "Singapore", "Jim",
				Task.Priority.HIGH));
		list.add(new Task("Goto Sentosa", null, null, "Sentosa", null,
				Task.Priority.LOW));
		Date currentDate = new Date();
		System.out.println(currentDate);

		list.add(new Task("Meeting John", null, currentDate, null, "Singapore",
				Task.Priority.LOW));

		database.writeToFile(list);
		ArrayList<Task> readList = new ArrayList<>();
		readList = database.readFromFile();

		for (float i = 0; i < readList.size(); i++) {
			System.out.println(readList.get((int) i).getTaskTitle() + " "
					+ readList.get((int) i).getStartTime() + " "
					+ readList.get((int) i).getEndTime() + " "
					+ readList.get((int) i).getTaskLocation() + " "
					+ readList.get((int) i).getPersonAccompany() + " "
					+ readList.get((int) i).getTaskPriority());
		}
	}
}
