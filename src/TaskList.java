import java.util.ArrayList;
import java.util.Stack;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**************
 * 
 * @author A0090551L
 *
 */
public class TaskList {
	class Log {
		Task previousTask;
		Task updatedTask;
		KairosDictionary.CommandType commandType; // this is to be changed to
													// commandType
	}

	private static final String ERROR_MESSAGE_NULL_TASK = "Task is null";
	private static final String ERROR_MESSAGE_NON_NEGATIVE_ID = "ID must be non-negative";
	private static final int COUNT_ZERO = 0;;

	private ArrayList<Task> taskArrayList;
	private Stack<Log> undoLog;
	private Stack<Log> redoLog;
	private Storage dataBase;
	private int taskIdCount = 0;

	private static Logger logger;

	public TaskList() {
		taskArrayList = new ArrayList<Task>();
		undoLog = new Stack<Log>();
		redoLog = new Stack<Log>();
		dataBase = new Storage();
		readFromDataBase();
		taskIdCount = getInitialIdCount();
		logger = Logging.getInstance();
	}

	// this function add a new task into taskArrayList
	public boolean addTask(Task task) {
		assert !isNull(task) : ERROR_MESSAGE_NULL_TASK;
		if (isDuplicated(task)) {
			logger.log(Level.SEVERE, "Fail adding task: duplicated task");
			return false;
		}
		try {
			task.setTaskId(taskIdCount++);
			taskArrayList.add(task);
			pushUndoLog(KairosDictionary.CommandType.ADD, null, task);
			clearRedoLog();
			logger.log(Level.INFO, "adding task successfully");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "fail adding task");
			return false;
		}
		return true;
	}

	// this function will check if the input task is duplicated with any task in
	// taskArrayList
	private boolean isDuplicated(Task task) {
		if (isNull(task)) {
			return false;
		}
		for (Task elementTask : taskArrayList) {
			if (task.exactComparison(elementTask)) {
				return true;
			}
		}
		return false;
	}

	// this function will delete an existing task in taskArrayList
	public boolean deleteTask(Task task) {
		if (taskArrayList.isEmpty()) {
			return false;
		}
		if (!taskArrayList.contains(task)) {
			return false;
		}
		try {
			taskArrayList.remove(task);
			pushUndoLog(KairosDictionary.CommandType.DELETE, task, null);
			clearRedoLog();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// this function will delete a task with a task ID from taskArrayList
	public boolean deleteTask(int taskId) {
		if (taskId < 0) {
			logger.log(Level.SEVERE, "Fail deleting task: invalid ID");
			return false;
		}
		if (taskArrayList.isEmpty()) {
			logger.log(Level.WARNING, "Error deleting: list is empty");
			return false;
		}

		try {
			Task deletedTask = searchTask(taskId);
			if (deletedTask == null) {
				logger.log(Level.SEVERE, "Fail deleting task: unfound task");
				return false;
			}
			taskArrayList.remove(deletedTask);
			pushUndoLog(KairosDictionary.CommandType.DELETE, deletedTask, null);
			clearRedoLog();
			logger.log(Level.INFO, "Deleting task successfully");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Fail deleting task");
			return false;
		}
		return true;
	}

	// this function will search a task by its ID
	private Task searchTask(int taskId) {
		assert taskId >= 0 : ERROR_MESSAGE_NON_NEGATIVE_ID;
		if (taskArrayList.isEmpty()) {
			return null;
		}

		Task taskOfTaskList;
		for (int counter = 0; counter < taskArrayList.size(); counter++) {
			taskOfTaskList = taskArrayList.get(counter);
			if (taskOfTaskList.getTaskId() == taskId) {
				return taskOfTaskList;
			}
		}
		return null;
	}

	// this function will search many tasks by a keyword
	public ArrayList<Task> searchTask(String keyword) {
		if (taskArrayList.isEmpty())
			return null;

		ArrayList<Task> searchResultTaskList = new ArrayList<Task>();

		for (Task task : taskArrayList) {
			if (task.isMatchedKeyword(keyword)) {
				searchResultTaskList.add(task);
			}
		}

		return searchResultTaskList;
	}

	// this function will search all done tasks by some requirement from
	// inputTask
	public ArrayList<Task> searchDoneTasks(Task inputTask) {
		if (taskArrayList.isEmpty())
			return null;

		ArrayList<Task> searchResultTaskList = new ArrayList<Task>();
		if (isNull(inputTask)) {
			return searchTasksWithStatus(taskArrayList, Task.Status.COMPLETE);
		} else {
			searchResultTaskList = searchTask(inputTask);
			return searchTasksWithStatus(searchResultTaskList,
					Task.Status.COMPLETE);
		}
	}

	// this function will search all the task from listOfTasks by their status
	private ArrayList<Task> searchTasksWithStatus(ArrayList<Task> listOfTasks,
			Task.Status status) {
		ArrayList<Task> searchResults = new ArrayList<Task>();
		for (Task task : listOfTasks) {
			if (task.getTaskStatus() == status) {
				searchResults.add(task);
			}
		}
		return searchResults;
	}

	// this function will search all undone tasks by some requirement from
	// inputTask
	public ArrayList<Task> searchUndoneTasks(Task inputTask) {
		if (taskArrayList.isEmpty())
			return null;

		ArrayList<Task> searchResultTaskList = new ArrayList<Task>();
		if (isNull(inputTask)) {
			return searchTasksWithStatus(taskArrayList, Task.Status.INCOMPLETE);
		} else {
			searchResultTaskList = searchTask(inputTask);
			return searchTasksWithStatus(searchResultTaskList,
					Task.Status.INCOMPLETE);
		}
	}

	// this function searches all the tasks by some requirement from targetTask
	public ArrayList<Task> searchTask(Task targetTask) {
		if (taskArrayList.isEmpty())
			return null;

		ArrayList<Task> searchResultTaskList = new ArrayList<Task>();
		try {
			for (Task task : taskArrayList) {
				if (task.isSameTask(targetTask)
						|| isClashingSingleTask(targetTask, task)) {
					searchResultTaskList.add(task);
				}
			}
		} catch (Exception e) {
			return null;
		}

		return searchResultTaskList;
	}

	// this function will check if there is any task whose ID is same as taskID
	private boolean isTaskFound(int taskId) {
		if (searchTask(taskId) == null)
			return false;
		return true;
	}

	// this function will edit an existing task in taskArrayList and update it
	// with an updated task
	public boolean editTask(Task updatedTask) {
		if (updatedTask == null) {
			return false;
		}
		int taskId = updatedTask.getTaskId();
		return editTask(taskId, updatedTask);
	}

	// this function will edit an existing task by its ID in taskArrayList and
	// update it with an updated task
	private boolean editTask(int taskId, Task updatedTask) {
		if (updatedTask == null) {
			logger.log(Level.WARNING, "Fail editing task: new task is NULL");
			return false;
		}

		if (isTaskFound(taskId)) {
			Task oldTask = searchTask(taskId);
			if (isDuplicated(updatedTask) && (updatedTask.getTaskStatus() == oldTask.getTaskStatus())) {
				logger.log(Level.SEVERE, "Fail adding task: duplicated task");
				return false;
			}
			taskArrayList.remove(oldTask);
			// updatedTask.setTaskId(taskId);
			taskArrayList.add(updatedTask);
			pushUndoLog(KairosDictionary.CommandType.EDIT, oldTask, updatedTask);
			clearRedoLog();
			logger.log(Level.INFO, "Editing task successfully");
			return true;
		}
		logger.log(Level.WARNING, "Fail editing task: no task found");
		return false;
	}

	// this functions will set all the tasks of the current date to be done
	public int kairos(Task task) {
		pushUndoLog(KairosDictionary.CommandType.KAIROS, null, null);
		int numberOfTaskChanged = 0;
		try {
			int sizeOfTaskList = taskArrayList.size();
			for (int counter = 0; counter < sizeOfTaskList; counter++) {
				Task oldTask = taskArrayList.get(counter);
				Task newTask;
				if (oldTask.isMatchedDate(task.getEndTime())
						&& oldTask.getTaskStatus() == Task.Status.INCOMPLETE) {
					newTask = new Task(oldTask);
					taskArrayList.remove(oldTask);
					newTask.setStatus(Task.Status.COMPLETE);
					taskArrayList.add(newTask);
					counter--;
					sizeOfTaskList--;
					pushUndoLog(KairosDictionary.CommandType.KAIROS, oldTask,
							newTask);
					clearRedoLog();
					numberOfTaskChanged++;
				}
			}
			pushUndoLog(KairosDictionary.CommandType.KAIROS, null, null);
		} catch (Exception e) {
			pushUndoLog(KairosDictionary.CommandType.KAIROS, null, null);
			return -1;
		}
		return numberOfTaskChanged;
	}

	// this function pushes the current log action to the undoLog stack
	private boolean pushUndoLog(KairosDictionary.CommandType command,
			Task previousTask, Task updatedTask) {
		try {
			Log newAction = new Log();
			newAction.commandType = command;
			newAction.previousTask = previousTask;
			newAction.updatedTask = updatedTask;
			undoLog.push(newAction);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this function will clear the content of redoLog
	private boolean clearRedoLog() {
		if (redoLog.isEmpty()) {
			return true;
		}
		redoLog.clear();
		return true;
	}

	// this function will undo the previous action
	public boolean undo() {
		if (undoLog.isEmpty()) {
			return false;
		}
		Log currentLog = undoLog.pop();
		boolean undoResult = false;
		switch (currentLog.commandType) {
		case ADD:
			undoResult = undoAddCommand(currentLog);
			break;
		case DELETE:
			undoResult = undoDeleteCommand(currentLog);
			break;
		case EDIT:
			undoResult = undoEditCommand(currentLog);
			break;
		case KAIROS:
			undoResult = undoKairosCommand(currentLog);
			break;
		default:
			undoResult = false;
		}
		redoLog.push(currentLog);
		return undoResult;
	}

	// this function will undo the kairos command
	private boolean undoKairosCommand(Log currentLog) {
		try {
			redoLog.push(currentLog);
			currentLog = undoLog.pop();
			while (currentLog.commandType == KairosDictionary.CommandType.KAIROS
					&& currentLog.updatedTask != null) {
				taskArrayList.remove(currentLog.updatedTask);
				taskArrayList.add(currentLog.previousTask);
				redoLog.push(currentLog);
				if (undoLog.size() == 0) {
					break;
				}
				currentLog = undoLog.pop();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// this function will undo the edit command
	private boolean undoEditCommand(Log currentLog) {
		if (!taskArrayList.contains(currentLog.updatedTask)) {
			return false;
		}
		try {
			taskArrayList.remove(currentLog.updatedTask);
			taskArrayList.add(currentLog.previousTask);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this function will undo the delete command
	private boolean undoDeleteCommand(Log currentLog) {
		try {
			taskArrayList.add(currentLog.previousTask);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this function will undo the add command
	private boolean undoAddCommand(Log currentLog) {
		if (taskArrayList.isEmpty()) {
			return false;
		}
		if (!taskArrayList.contains(currentLog.updatedTask)) {
			return false;
		}
		try {
			taskArrayList.remove(currentLog.updatedTask);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this function will redo the current action
	public boolean redo() {
		if (redoLog.isEmpty()) {
			return false;
		}
		Log currentLog = redoLog.pop();
		boolean redoResult = false;
		switch (currentLog.commandType) {
		case ADD:
			redoResult = redoAddCommand(currentLog);
			break;
		case DELETE:
			redoResult = redoDeleteCommand(currentLog);
			break;
		case EDIT:
			redoResult = redoEditCommand(currentLog);
			break;
		case KAIROS:
			redoResult = redoKairosCommand(currentLog);
			break;
		default:
			redoResult = false;
		}
		undoLog.push(currentLog);
		return redoResult;
	}

	// this function will redo the kairos command
	private boolean redoKairosCommand(Log currentLog) {
		try {
			undoLog.push(currentLog);
			currentLog = redoLog.pop();
			while (currentLog.commandType == KairosDictionary.CommandType.KAIROS
					&& currentLog.updatedTask != null) {
				taskArrayList.remove(currentLog.previousTask);
				taskArrayList.add(currentLog.updatedTask);
				undoLog.push(currentLog);
				if (redoLog.size() == 0) {
					break;
				}
				currentLog = redoLog.pop();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// this function will redo the edit command
	private boolean redoEditCommand(Log currentLog) {
		if (!taskArrayList.contains(currentLog.previousTask)) {
			return false;
		}
		try {
			taskArrayList.remove(currentLog.previousTask);
			taskArrayList.add(currentLog.updatedTask);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this function will redo the delete command
	private boolean redoDeleteCommand(Log currentLog) {
		if (taskArrayList.isEmpty()) {
			return false;
		}
		if (!taskArrayList.contains(currentLog.previousTask)) {
			return false;
		}
		try {
			taskArrayList.remove(currentLog.previousTask);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this function will redo the add command
	private boolean redoAddCommand(Log currentLog) {
		try {
			taskArrayList.add(currentLog.updatedTask);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// this function will check if the taskArrayList is empty
	public boolean isTaskListEmpty() {
		return taskArrayList.isEmpty();
	}


	// this function will collate all the tasks info and combine to a single
	// string.
	public String toString() {
		if (taskArrayList.isEmpty()) {
			return "";
		}
		String message = "";
		for (Task task : taskArrayList) {
			message += task.toString() + "\n";
		}
		return message;
	}

	// this function will check if the dummyTask is clashing with any task in
	// the taskArrayList
	public boolean isClashing(Task dummyTask) {
		if (dummyTask == null) {
			return false;
		}
		if (dummyTask.getEndTime() == null && dummyTask.getStartTime() == null) {
			return false;
		}
		Date dummyStartTime = dummyTask.getStartTime();
		Date dummyEndTime = dummyTask.getEndTime();

		for (Task task : taskArrayList) {
			if (isClashingSingleTask(dummyStartTime, dummyEndTime, task)) {
				return true;
			}

		}
		return false;
	}

	// this function will check if the dummyTask is clashing with a task
	private boolean isClashingSingleTask(Task dummyTask, Task task) {
		return isClashingSingleTask(dummyTask.getStartTime(),
				dummyTask.getEndTime(), task);
	}

	// this function will check if the task is clashing with time range from
	// dummyStartTime to dummyEndTime
	private boolean isClashingSingleTask(Date dummyStartTime, Date dummyEndTime,
			Task task) {
		if (isDateWithinRange(dummyStartTime, task.getStartTime(),
				task.getEndTime())) {
			return true;
		}
		if (isDateWithinRange(dummyEndTime, task.getStartTime(),
				task.getEndTime())) {
			return true;
		}
		if (isDateWithinRange(task.getStartTime(), dummyStartTime, dummyEndTime)) {
			return true;
		}
		if (isDateWithinRange(task.getEndTime(), dummyStartTime, dummyEndTime)) {
			return true;
		}
		return false;
	}

	// this function will check if the targetTime is within the range from
	// startTime to endTime
	private boolean isDateWithinRange(Date targetTime, Date startTime,
			Date endTime) {
		if (targetTime == null || startTime == null || endTime == null) {
			return false;
		}
		try {
			if (targetTime.after(startTime) && targetTime.before(endTime)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// this function will read from database via Storage object
	private boolean readFromDataBase() {
		ArrayList<Task> tempTaskList = dataBase.readFromFile();
		if (tempTaskList != null) {
			taskArrayList = tempTaskList;
			return true;
		}
		return false;
	}
	
	private int getInitialIdCount() {
		if(taskArrayList == null || taskArrayList.size() == 0){
			return COUNT_ZERO;
		}
		try{
			int maxId = taskArrayList.get(0).getTaskId(); 
			for(Task task : taskArrayList){
				if(task.getTaskId() >= maxId){
					maxId = task.getTaskId();
				}
			}
			return maxId + 1;
		}catch(Exception e){
			return COUNT_ZERO;
		}
	}
	// this function will write to database via Storage object
	public boolean writeToDataBase() {
		dataBase.writeToFile(taskArrayList);
		return false;
	}

	// this function checks if an object is null
	private boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		}
		return false;
	}
}
