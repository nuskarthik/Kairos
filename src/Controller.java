import java.util.Date;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author A0091545A
 * authored the complete class
 */
public class Controller {
	
	private static Logic logicObject;
	private TaskList taskList;
	private static KairosDictionary dictionaryObject;

	private static String commandEntered;
	private static KairosDictionary.CommandType commandType;
	private static Task dummyTaskAfterParsing;
	private static ArrayList<Task> searchResults;
	private static Vector<String> autocompleteResults;
	private static int autocompleteSearchId;
	private static Logger logger;

	private static final String logArrayOutOfBounds = "SearchId out of array bounds";
	private static final String logOperationSuccessful = "Operation performed successfully";
	private static final String feedbackTaskDateCrossed = "Task Date has already crossed. Cannot add task.";
	private static final String feedbackNoTitleDescription = "No task description found, operation is unsuccessful";
	private static final String feedbackOperationUnsuccessful = " is unsuccessful";
	private static final String feedbackOperationSuccessful = " is successful";
	private static final String feedbackTaskClashingWarning = " operation successful. \nTask is Clashing with another existing task";
	private static final String feedbackKairos = " task(s) have been marked DONE";
	private static final String feedbackInvalidCommand = "Command type entered in invalid";

	private static final char nextLineCharacter = '\n';
	private static final String whiteSpace = " ";

	private static final int lengthEmptyString = 0;
	private static final int leastPositiveInteger = 0;
	private static final int startingIndex0 = 0;
	private static final int errorInOperation = -1;
	private static final int startingElementString = 0;

	public Controller() {
		taskList = new TaskList();
		dictionaryObject = new KairosDictionary();
		logicObject = new Logic(dictionaryObject);
		logger = Logging.getInstance();
	}

	/**
	 * Return the command type of the entered command
	 * 
	 * @param commandEntered
	 * 
	 * @return CommandType from the list of types in Logic
	 */
	@SuppressWarnings("static-access")
	public KairosDictionary.CommandType getCommandType(String commandEntered) {
		return logicObject.determineCommandType(commandEntered);
	}

	public boolean isAutocompleteable(String command) {
		return KairosDictionary.isAutocompleteable(command);
	}

	public boolean isEdit(String command) {
		return KairosDictionary.isEdit(command);
	}

	public boolean isAdd(String command) {
		return KairosDictionary.isAdd(command);
	}

	public boolean isDelete(String command) {
		return KairosDictionary.isDelete(command);
	}

	public boolean isView(String command) {
		return KairosDictionary.isView(command);
	}

	public boolean isUndoOrRedo(String command) {
		return KairosDictionary.isUndoOrRedo(command);
	}

	public boolean isDone(String command) {
		return KairosDictionary.isDone(command);
	}

	public boolean isUndone(String command) {
		return KairosDictionary.isUndone(command);
	}

	public boolean isKairos(String command) {
		return KairosDictionary.isKairos(command);
	}

	public boolean isExit(String command) {
		return KairosDictionary.isExit(command);
	}

	/**
	 * 
	 * Function to return whether the entered user command is of view type
	 * searching for done results. This is required by the GUI to know whether
	 * to color all the tasks in display or not
	 * 
	 * @param User Command String
	 * 
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public boolean isViewDone(String command) {
		try {
			if (!(isView(command)))
				return false;
			Task dummyTaskAfterParsing = logicObject.commandParse(command);
			if (dummyTaskAfterParsing.getTaskStatus() == Task.Status.COMPLETE)
				return true;
			if (command.contains("done") && !(command.contains("\"done\"")) && !(command.contains("undone")))
					return true;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Function to search for a given string in database and format the search
	 * results to be displayed for AutoComplete feature in GUI
	 * 
	 * @param String
	 * 
	 * @return boolean for if search exists or not
	 */
	public boolean getAutocompleteSearchResults(String editStatement) {
		assert editStatement != null;
		commandType = getCommandType(editStatement);

		editStatement = removeExtraNextLineCharacter(editStatement);
		dummyTaskAfterParsing = logicObject.commandParse(editStatement);

		if (taskList.isTaskListEmpty())
			return false;

		autocompleteResults = new Vector<String>();
		searchResults = new ArrayList<Task>();

		searchTasks();

		if (searchResults == null | searchResults.isEmpty()) {
			return false;
		} else {
			searchResults = sortByPriority(searchResults);
			for (int taskIndex = startingIndex0; taskIndex < searchResults
					.size(); taskIndex++) {
				autocompleteResults.add(searchResults.get(taskIndex).toString());
			}
			return true;
		}
	}

	private void searchTasks() {
		if (commandType == KairosDictionary.CommandType.UNDONE) {
			getDoneOrUndoneTasksAutocomplete(Task.Status.COMPLETE);
		} 
		if (commandType == KairosDictionary.CommandType.DONE) {
			getDoneOrUndoneTasksAutocomplete(Task.Status.INCOMPLETE);
		} 
		if(commandType != KairosDictionary.CommandType.DONE && commandType != KairosDictionary.CommandType.UNDONE){
			searchResults = taskList.searchTask(dummyTaskAfterParsing);
		}
	}

	/**
	 * Function to remove extra next line character if any from the end of the
	 * string passed by user in cases like DELETE
	 * 
	 * @param String
	 * 
	 * @return String
	 */
	private String removeExtraNextLineCharacter(String editStatement) {
		int sizeStatement = editStatement.length();

		if (sizeStatement > leastPositiveInteger
				&& editStatement.charAt(sizeStatement - 1) == nextLineCharacter)
			editStatement = editStatement.substring(startingElementString,
					sizeStatement - 1);
		return editStatement;
	}

	/**
	 * To search for DONE or UNDONE tasks matching a certain description
	 * 
	 * @param Task.Status
	 */
	private void getDoneOrUndoneTasksAutocomplete(Task.Status statusType) {
		if (statusType == Task.Status.COMPLETE)
			searchResults = taskList.searchDoneTasks(dummyTaskAfterParsing);
		else {
			searchResults = taskList.searchUndoneTasks(dummyTaskAfterParsing);
		}
	}

	/**
	 * Function to the ids of all the tasks that have been done/high/low in the
	 * autocomplete search for the GUI to show those tasks as appropriately
	 * colored
	 * 
	 * @return vector of integers of index of done tasks
	 */
	public Vector<Integer> getAutocompleteSearchDoneID() {
		Vector<Integer> doneId = new Vector<Integer>();
		if (!(searchResults.isEmpty())) {
			for (int searchResultIterator = startingIndex0; searchResultIterator < searchResults
					.size(); searchResultIterator++) {
				if (searchResults.get(searchResultIterator).getTaskStatus() == Task.Status.COMPLETE)
					doneId.add(searchResultIterator);
			}
		}
		return doneId;
	}

	public Vector<Integer> getAutocompleteSearchHighPriorityID() {
		Vector<Integer> doneId = new Vector<Integer>();
		if (!(searchResults.isEmpty())) {
			for (int searchResultIterator = startingIndex0; searchResultIterator < searchResults
					.size(); searchResultIterator++) {
				if (searchResults.get(searchResultIterator).getTaskStatus() == Task.Status.COMPLETE)
					continue;
				if (searchResults.get(searchResultIterator).getTaskPriority() == Task.Priority.HIGH)
					doneId.add(searchResultIterator);
			}
		}
		return doneId;
	}

	public Vector<Integer> getAutocompleteSearchLowPriorityID() {
		Vector<Integer> doneId = new Vector<Integer>();
		if (!(searchResults.isEmpty())) {
			for (int searchResultIterator = startingIndex0; searchResultIterator < searchResults
					.size(); searchResultIterator++) {
				if (searchResults.get(searchResultIterator).getTaskStatus() == Task.Status.COMPLETE)
					continue;
				if (searchResults.get(searchResultIterator).getTaskPriority() == Task.Priority.LOW)
					doneId.add(searchResultIterator);
			}
		}
		return doneId;
	}

	/**
	 * Function to return search results for autocomplete
	 * 
	 * @return vector of search result strings
	 */
	public Vector<String> getAutocompleteSearchString() {
		return autocompleteResults;
	}

	/**
	 * Function to set the string number which user has chosen to edit
	 * 
	 * @param Integer
	 */
	public void setAutocompleteTaskId(int id) {
		assert id >= leastPositiveInteger;
		autocompleteSearchId = id;
	}

	/**
	 * To perform the operation requested by the user and return the success
	 * nature of the operation
	 * 
	 * @return Status of the user operation
	 */
	private boolean startExecution() {
		try {
			commandType = getCommandType(commandEntered);
			if (commandType != KairosDictionary.CommandType.KAIROS)
				// Pre-processing for command types other than Kairos
				dummyTaskAfterParsing = logicObject
						.commandParse(commandEntered);
			else {
				// Pre-processing for commandType Kairos
				Date currentDate = new Date();
				dummyTaskAfterParsing = new Task();
				dummyTaskAfterParsing.setEndTime(currentDate);
			}

			boolean operationStatus = false;

			/*
			 * Pre-processing involves checking for clashing tasks and not
			 * adding tasks to the past
			 */
			operationStatus = preProcessingTimedTask();
			if (operationStatus == false)
				return operationStatus;

			/*
			 * Performing operations on TaskList depending upon the type of
			 * command entered by the user
			 */
			switch (commandType) {
			case ADD:
				int clashFlag = 0;
				clashFlag = addOperation(clashFlag);
				operationStatus = taskList.addTask(dummyTaskAfterParsing);
				if (clashFlag == 1)
					return true;
				break;
			case DELETE:
				operationStatus = deleteTask();
				break;
			case EDIT:
				int clashingFlag = 0;
				clashingFlag = addOperation(clashingFlag);
				operationStatus = editTask();
				if (clashingFlag == 1)
					return true;
				break;
			case VIEW:
				Vector<Vector<String>> tasksView = viewTask(commandEntered);
				KairosGUI.setViewList(tasksView);
				operationStatus = true;
				break;
			case UNDO:
				operationStatus = taskList.undo();
				break;
			case REDO:
				operationStatus = taskList.redo();
				break;
			case DONE:
				operationStatus = doneTask();
				break;
			case UNDONE:
				operationStatus = undoneTask();
				break;
			case KAIROS:
				operationStatus = kairosTask();
				break;
			case INVALID:
				operationStatus = false;
				break;
			default:
				operationStatus = false;
				break;
			}

			/**
			 * Check the status of the operation to print relevant feedback
			 * message
			 */
			displayFeedbackMessage(operationStatus);
			return operationStatus;
		} catch (Exception e) {
			if (commandType == null)
				return false;
			checkForPossibleReasonsOfFailure();
			KairosGUI.displayText(commandEntered
					+ feedbackOperationUnsuccessful);
			return false;
		}
	}

	/**
	 * Function to be performed for command with Kairos as keyword
	 * @return boolean
	 */
	private boolean kairosTask() {
		boolean operationStatus;
		int numOfTasksMarked = taskList.kairos(dummyTaskAfterParsing);
		if (numOfTasksMarked == errorInOperation)
			operationStatus = false;
		else {
			operationStatus = true;
			KairosGUI.displayText(numOfTasksMarked + feedbackKairos);
		}
		return operationStatus;
	}

	/**
	 * Function to perform the necessary add operations
	 * @param Integer
	 * @return Integer
	 */
	private int addOperation(int clashFlag) {
		// Display warnings if new task clashes with existing ones
		if (taskList.isClashing(dummyTaskAfterParsing)) {
			KairosGUI.displayText(commandEntered
					+ feedbackTaskClashingWarning);
			clashFlag = 1;
		}
		return clashFlag;
	}

	
	/**
	 * Check the status of the operation to print relevant feedback message
	 * @param boolean operationStatus
	 */
	private void displayFeedbackMessage(boolean operationStatus) {
		if (operationStatus == true) {
			commandType = getCommandType(commandEntered);

			if (commandType != KairosDictionary.CommandType.VIEW
					&& commandType != KairosDictionary.CommandType.KAIROS) {
				logger.log(Level.FINE, logOperationSuccessful);
				if (commandType != KairosDictionary.CommandType.VIEW)
					taskList.writeToDataBase();
				KairosGUI.displayText(commandEntered
						+ feedbackOperationSuccessful);
			}
		} else {
			KairosGUI.displayText(commandEntered
					+ feedbackOperationUnsuccessful);
		}
	}

	private void checkForPossibleReasonsOfFailure() {
		switch (commandType) {
		case EDIT:
			if (autocompleteSearchId >= searchResults.size()) {
				logger.log(Level.INFO, logArrayOutOfBounds);
			}
			break;
		case DELETE:
			if (autocompleteSearchId >= searchResults.size()) {
				logger.log(Level.INFO, logArrayOutOfBounds);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * To prevent tasks from being added in the past. And to check for clashing
	 * of tasks
	 * 
	 * @return Result of whether out-dated task being added
	 */
	private boolean preProcessingTimedTask() {
		if (isAdd(commandEntered) || isEdit(commandEntered)) {
			if (dummyTaskAfterParsing.getEndTime() != null)
				// Tasks are not added to the past
				if (isPastEvent(dummyTaskAfterParsing)) {
					KairosGUI.displayText(feedbackTaskDateCrossed);
					return false;
				}

			if (dummyTaskAfterParsing.getTaskTitle().equals(null)
					|| dummyTaskAfterParsing.getTaskTitle().length() == lengthEmptyString) {
				KairosGUI.displayText(feedbackNoTitleDescription);
				return false;
			}
		}
		return true;
	}

	/**
	 * To mark a particular task as DONE
	 * 
	 * @return status of operation
	 */
	private boolean doneTask() {
		boolean operationStatus = false;
		int oldTaskId = searchResults.get(autocompleteSearchId).getTaskId();
		dummyTaskAfterParsing.setTaskId(oldTaskId);
		dummyTaskAfterParsing.setStatus(Task.Status.COMPLETE);
		try {
			operationStatus = taskList.editTask(dummyTaskAfterParsing);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return operationStatus;
	}

	/**
	 * To mark a particular done task as INCOMPLETE
	 * 
	 * @return status of operation
	 */
	private boolean undoneTask() {
		boolean operationStatus = false;
		int oldTaskId = searchResults.get(autocompleteSearchId).getTaskId();
		dummyTaskAfterParsing.setTaskId(oldTaskId);
		dummyTaskAfterParsing.setStatus(Task.Status.INCOMPLETE);
		try {
			operationStatus = taskList.editTask(dummyTaskAfterParsing);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return operationStatus;
	}

	/**
	 * To edit a pre-existing task.
	 * 
	 * @return status of operation
	 */
	private boolean editTask() {
		boolean operationStatus = false;

		assert autocompleteSearchId < searchResults.size();
		assert autocompleteSearchId >= leastPositiveInteger;
		int oldTaskId = searchResults.get(autocompleteSearchId).getTaskId();

		assert oldTaskId >= leastPositiveInteger;
		dummyTaskAfterParsing.setTaskId(oldTaskId);
		try {
			operationStatus = taskList.editTask(dummyTaskAfterParsing);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return operationStatus;
	}

	/**
	 * To delete the particular task chosen by the user
	 * 
	 * @return status of operation
	 */
	private boolean deleteTask() {
		boolean operationStatus = false;
		try {
			assert autocompleteSearchId < searchResults.size();
			assert autocompleteSearchId >= leastPositiveInteger;
			operationStatus = taskList.deleteTask(searchResults.get(
					autocompleteSearchId).getTaskId());
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return operationStatus;
	}

	/**
	 * To create a vector of Vector of Strings that is to be displayed for view
	 * command
	 * 
	 * @return Vector of Vector of Strings
	 */
	private Vector<Vector<String>> viewTask(String userCommand) {
		Vector<Vector<String>> stringsDisplay = new Vector<Vector<String>>();
		try {
			/*if (userCommand.contains("done")){
				String[] words = userCommand.split(whiteSpace);
				userCommand = new String();
				int flag = leastPositiveInteger;
				for(int iterator = startingIndex0; iterator<words.length; iterator++)
					if(!(words[iterator].equalsIgnoreCase("done")))
						userCommand += words[iterator];
					else
						flag ++;
				dummyTaskAfterParsing = logicObject.commandParse(userCommand);
				if(flag == leastPositiveInteger+1)
					dummyTaskAfterParsing.setStatus(Task.Status.COMPLETE);
			}*/
			// in case user wants to view done tasks
			if (isViewDone(userCommand)) {
				removeKeywordDoneFromDummyTask();

				Vector<String> taskToStrings = new Vector<String>();
				for (int i = startingIndex0; i < searchResults.size(); i++) {
					taskToStrings.add(searchResults.get(i).toString());
				}
				stringsDisplay.add(taskToStrings);

			} else if (dummyTaskAfterParsing.getTaskTitle().equals(whiteSpace)) {
				// in case user wants to view all tasks)
				ArrayList<Task> searchResults = taskList
						.searchUndoneTasks(null);
				Vector<Vector<Task>> tasksDisplayUndone = new Vector<Vector<Task>>();

				getViewSpecificTasks(tasksDisplayUndone, searchResults,
						dummyTaskAfterParsing.getTaskStatus());

				for (int i = startingIndex0; i < tasksDisplayUndone.size(); i++) {
					Vector<String> taskToStrings = new Vector<String>();
					for (int j = startingIndex0; j < tasksDisplayUndone.get(i).size(); j++)
						taskToStrings.add(tasksDisplayUndone.get(i).get(j).toString());
					stringsDisplay.add(taskToStrings);
				}

			} else {
				// in case user wants to view specific tasks
				getViewTasks(stringsDisplay,
						dummyTaskAfterParsing.getTaskStatus());
			}
		} catch (Exception e) {
			try {
				if (dummyTaskAfterParsing.getTaskTitle().equals(null)) {
					getViewTasks(stringsDisplay,
							dummyTaskAfterParsing.getTaskStatus());
				}
			} catch (Exception exception) {
				// This is reached only for null pointer exception
				getViewTasks(stringsDisplay,
						dummyTaskAfterParsing.getTaskStatus());
			}
		}
		return stringsDisplay;
	}

	/**
	 * Function that calls the relevant functions to search for view results
	 * 
	 * @param Vector<Vector<String>>
	 */
	private void getViewTasks(Vector<Vector<String>> stringsDisplay,
			Task.Status status) {
		Vector<Vector<Task>> tasksDisplay = new Vector<Vector<Task>>();
		ArrayList<Task> searchResults = new ArrayList<>();
		if (status == Task.Status.COMPLETE)
			searchResults = taskList.searchDoneTasks(dummyTaskAfterParsing);
		else {
			searchResults = taskList.searchUndoneTasks(dummyTaskAfterParsing);
		}
		getViewSpecificTasks(tasksDisplay, searchResults,
				dummyTaskAfterParsing.getTaskStatus());
		for (int i = startingIndex0; i < tasksDisplay.size(); i++) {
			Vector<String> taskToStrings = new Vector<String>();
			for (int j = startingIndex0; j < tasksDisplay.get(i).size(); j++)
				taskToStrings.add(tasksDisplay.get(i).get(j).toString());
			stringsDisplay.add(taskToStrings);
		}
	}

	/**
	 * Function to remove the keyword done from the task title when searching
	 * for tasks containing done in their title
	 */
	private void removeKeywordDoneFromDummyTask() {
		try{
			if (dummyTaskAfterParsing.toString().equals("done")
					&& dummyTaskAfterParsing.getTaskStatus() == Task.Status.COMPLETE)
				searchResults = taskList.searchDoneTasks(null);
			else {
				if (dummyTaskAfterParsing.getTaskStatus() == Task.Status.COMPLETE)
					searchResults = taskList.searchDoneTasks(dummyTaskAfterParsing);
				else {
					searchResults = taskList
							.searchUndoneTasks(dummyTaskAfterParsing);
				}
			}
		}catch(Exception e){
			try{
				if (dummyTaskAfterParsing.getTaskTitle().equals("done"))
					if(dummyTaskAfterParsing.getTaskStatus() == Task.Status.COMPLETE)
						searchResults = taskList.searchDoneTasks(null);
				}catch(Exception exception){
					//dummyTask title is null
					if(dummyTaskAfterParsing.getTaskStatus() == Task.Status.COMPLETE){
						searchResults = taskList.searchDoneTasks(null);
					}
				}
		}
	}

	
	/**
	 * Function to sort the list of tasks based on their priority and status
	 * @param ArrayList
	 * @return ArrayList
	 */
	private ArrayList<Task> sortByPriority(ArrayList<Task> taskArrayList) {
		ArrayList<Task> highPriority = new ArrayList<Task>();
		ArrayList<Task> lowPriority = new ArrayList<Task>();
		ArrayList<Task> nullPriority = new ArrayList<Task>();
		ArrayList<Task> doneTasks = new ArrayList<Task>();
		
		splitBasedOnPriority(taskArrayList, highPriority, lowPriority,
				nullPriority, doneTasks);
		
		highPriority = sortByTitle(highPriority);
		lowPriority = sortByTitle(lowPriority);
		nullPriority = sortByTitle(nullPriority);
		doneTasks = sortByTitle(doneTasks);
		
		taskArrayList = new ArrayList<Task>();
		taskArrayList.addAll(highPriority);
		taskArrayList.addAll(lowPriority);
		taskArrayList.addAll(nullPriority);
		taskArrayList.addAll(doneTasks);
		return taskArrayList;
	}
	
	
	/**
	 * Function that separates the ArrayList into sublists based on priority
	 * @param ArrayList original
	 * @param ArrayList highpriority
	 * @param ArrayList lowpriority
	 * @param ArrayList nullpriority
	 * @param ArrayList donetasks
	 */
	private void splitBasedOnPriority(ArrayList<Task> taskArrayList,
			ArrayList<Task> highPriority, ArrayList<Task> lowPriority,
			ArrayList<Task> nullPriority, ArrayList<Task> doneTasks) {
		for(int i =0 ; i<taskArrayList.size(); i++){
			if(taskArrayList.get(i).getTaskStatus() == Task.Status.COMPLETE)
				doneTasks.add(taskArrayList.get(i));
			else {
				if(taskArrayList.get(i).getTaskPriority() == Task.Priority.HIGH)
					highPriority.add(taskArrayList.get(i));
				else {
					if(taskArrayList.get(i).getTaskPriority() == Task.Priority.LOW)
						lowPriority.add(taskArrayList.get(i));
					else
						nullPriority.add(taskArrayList.get(i));
				}
			}
		}
	}
	
	
	/**
	 * Function to sort an ArrayList based on the task description
	 * @param ArrayList
	 */
	private ArrayList<Task> sortByTitle(ArrayList<Task> taskArrayList) {
		for (int i = 0; i < taskArrayList.size() - 1; i++) {
			for (int j = 1; j < taskArrayList.size() - i; j++) {
				Task task1 = taskArrayList.get(j);
				Task task2 = taskArrayList.get(j-1);
				if(task1.toString().compareToIgnoreCase(task2.toString()) < 0)
					swapTasksList(taskArrayList, j-1, j);
			}

		}
		return taskArrayList;
	}

	
	/**
	 * Function to swap two tasks at given positions
	 * @param ArrayList
	 * @param integer index
	 * @param integer index
	 */
	private void swapTasksList(ArrayList<Task> taskArrayList, int i, int j) {
		assert i<taskArrayList.size();
		assert j<taskArrayList.size();
		
		Task dummyTask = taskArrayList.get(i);
		taskArrayList.set(i, taskArrayList.get(j));
		taskArrayList.set(j, dummyTask);
	}

	/**
	 * Function to search tasks for view and sort them according to priority
	 * 
	 * @param Vector<Vector<Task>>
	 * 
	 * @param ArrayList<Task> of searchResults
	 */
	private void getViewSpecificTasks(Vector<Vector<Task>> tasksDisplay,
			ArrayList<Task> searchResults, Task.Status status) {
		Vector<Task> highPriorityTask = new Vector<Task>();
		Vector<Task> lowPriorityTask = new Vector<Task>();
		Vector<Task> nullPriorityTask = new Vector<Task>();

		for (int resultTaskIndex = startingIndex0; resultTaskIndex < searchResults
				.size(); resultTaskIndex++) {
			if (searchResults.get(resultTaskIndex).getTaskStatus() == status) {
				if (searchResults.get(resultTaskIndex).getTaskPriority() == Task.Priority.HIGH) {
					highPriorityTask.add(searchResults.get(resultTaskIndex));
				} else if (searchResults.get(resultTaskIndex).getTaskPriority() == Task.Priority.LOW) {
					lowPriorityTask.add(searchResults.get(resultTaskIndex));
				} else {
					nullPriorityTask.add(searchResults.get(resultTaskIndex));
				}
			}
		}

		tasksDisplay.add(highPriorityTask);
		tasksDisplay.add(lowPriorityTask);
		tasksDisplay.add(nullPriorityTask);
	}

	/**
	 * Function to check if an event/task date has already passed
	 * 
	 * @param Task To Be Added
	 */
	@SuppressWarnings("deprecation")
	private boolean isPastEvent(Task taskToBeAdded) {
		Date currentDate = new Date();
		if (taskToBeAdded.getEndTime().getSeconds() == 1) {
			if (isSameDateIgnoreTime(taskToBeAdded.getEndTime(), currentDate))
				return false;
		}
		if (currentDate.before(taskToBeAdded.getEndTime()))
			return false;
		return true;
	}

	/**
	 * Function to check if two dates are exactly the same ignoring the time
	 * 
	 * @param Date
	 * 
	 * @param Date
	 * 
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	private boolean isSameDateIgnoreTime(Date eventDate, Date currentDate) {
		if (eventDate.getDate() != currentDate.getDate())
			return false;
		if (eventDate.getMonth() != currentDate.getMonth())
			return false;
		if (eventDate.getYear() != currentDate.getYear())
			return false;
		return true;
	}

	/**
	 * Function that takes in the user command from the GUI and starts
	 * processing it appropriately
	 * 
	 * @param String
	 * 
	 * @return Boolean
	 * 
	 * @throws Exception in case any process encounters illegal situation
	 */
	public boolean setUserCommand(String userCommand) throws Exception {
		assert userCommand != null;
		commandEntered = userCommand;

		// checking the command type and whether a valid command or not
		commandType = getCommandType(commandEntered);
		if (commandType == null)
			return false;

		assert commandType != null;
		if (commandType == KairosDictionary.CommandType.INVALID) {
			KairosGUI.displayText(feedbackInvalidCommand);
			return false;
		}

		if (commandType == KairosDictionary.CommandType.EXIT) {
			exitSystem();
		}

		// performing the operations as per the commands given
		return startExecution();
	}

	/**
	 * Function to exit the system when the exit command is enter by the user
	 */
	private void exitSystem() {
		System.exit(leastPositiveInteger);
	}

	public static void main(String[] args) {
		try {
			KairosGUI.invokeGUI();
		} catch (Exception e) {

		}
	}

}
