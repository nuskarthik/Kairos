import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author @A0091545A
 */
public class Storage {

	private static final String fileName = "KairosTasks.xml";
	private static final String archiveFileName = "Archive.xml";
	private static Logger logger;

	private static final int daysIn31DayMonth = 31;
	private static final int daysIn30DayMonth = 30;
	private static final int daysIn28DayMonth = 28;

	private static final int decMonth = 11;
	private static final int janMonth = 0;
	private static final int nextDate = 1;
	private static final int nextYear = 1;
	private static final int nextMonth = 1;
	private static final int noDifference = 0;
	private static final int numOfDaysWeek = 7;
	private static final int leastPositiveInteger = 0;
	private static final int startingIndex0 = 0;

	private static final String logWriteOntoFileSuccessful = "Written on XML File successfully";
	private static final String logWriteOntoFileUnsuccessful = "Could not write on XML File";
	private static final String logWriteOntoArchive = "Writing onto the Archive";
	private static final String logWriteOntoArchiveSuccessful = "Written on Archive successfully";
	private static final String logWriteOntoArchiveUnsuccessful = "Could not write on Archive";

	private static final String logReadFromFile = "Reading from file";
	private static final String logReadFromFileSuccessful = "XML File read successfully";
	private static final String logReadFromFileUnsuccessful = "XML File not read";
	private static final String logReadFromArchive = "Reading from Archive";
	private static final String logReadFromArchiveSuccessful = "Archive File read successfully";
	private static final String logReadFromArchiveUnsuccessful = "Archive File not read";

	private static final String logNoFileName = "No XML filename given";
	private static final String logNoArchiveFileName = "No archive filename given";

	public Storage() {
		logger = Logging.getInstance();
	}

	/**
	 * Function to write the list of Tasks from taskList onto the XML file
	 * 
	 * @param ArrayList<Task>
	 */
	public void writeToFile(ArrayList<Task> listOfTasks) {
		try {
			assert fileName != null;
			if (fileName == null) {
				logger.log(Level.WARNING, logNoFileName);
				return;
			}

			String xmlFileLocation = createFile();
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(xmlFileLocation));
			XMLEncoder fileWriter = new XMLEncoder(outputStream);

			ArrayList<Task> listofTasksToWriteToXML = new ArrayList<>();
			ArrayList<Task> listofTasksToWriteToArchive = new ArrayList<>();
			generateSeparateListOfTasksForArchiveAndFile(listOfTasks,
					listofTasksToWriteToXML, listofTasksToWriteToArchive);

			writeListOfTasksToFile(fileWriter, listofTasksToWriteToXML);
			writeToArchive(listofTasksToWriteToArchive);
			fileWriter.close();
			outputStream.close();
			logger.log(Level.INFO, logWriteOntoFileSuccessful);
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, logWriteOntoFileUnsuccessful);
		}
	}

	
	/**
	 * Function to create the XML file
	 * @return String
	 */
	private String createFile() throws FileNotFoundException {
		String xmlFileLocation = fileName;
		PrintWriter fileXML = new PrintWriter(xmlFileLocation);
		fileXML.close();
		return xmlFileLocation;
	}

	/**
	 * Function that generate separate lists for task to be archived and for to
	 * be written onto the normal file.
	 * 
	 * @param ArrayList<Task>
	 * 
	 * @param ArrayList<Task>
	 * 
	 * @param ArrayList<Task>
	 */
	private void generateSeparateListOfTasksForArchiveAndFile(
			ArrayList<Task> listOfTasks,
			ArrayList<Task> listofTasksToWriteToXML,
			ArrayList<Task> listofTasksToWriteToArchive) {
		Task taskToBeWritten;
		for (int iterator = startingIndex0; iterator < listOfTasks.size(); iterator++) {
			taskToBeWritten = listOfTasks.get(iterator);

			if (!(isMoreThanOneWeekOld(taskToBeWritten))) {
				listofTasksToWriteToXML.add(taskToBeWritten);
			} else {
				listofTasksToWriteToArchive.add(taskToBeWritten);
			}
		}
	}

	/**
	 * Function that writes the list of tasks passed using the given output stream
	 * @param XMLEncoder
	 * @param ArrayList
	 */
	private void writeListOfTasksToFile(XMLEncoder fileWriter,
			ArrayList<Task> listofTasksToWriteToXML) {
		Task taskToBeWritten;
		fileWriter.writeObject(listofTasksToWriteToXML.size());
		for (int iterator = startingIndex0; iterator < listofTasksToWriteToXML
				.size(); iterator++) {
			taskToBeWritten = listofTasksToWriteToXML.get(iterator);
			writeTaskObject(taskToBeWritten, fileWriter);
		}
	}

	
	/**
	 * Function to write the list of tasks passed onto the archive file
	 * @param ArrayList
	 */
	public void writeToArchive(ArrayList<Task> listOfArchiveTasks) {
		try {
			logger = Logger.getLogger(logWriteOntoArchive);
			assert fileName != null;
			if (fileName == null) {
				logger.log(Level.WARNING, logNoArchiveFileName);
				return;
			}

			String xmlFileLocation = creatingArchiveFile();
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(xmlFileLocation));
			XMLEncoder fileWriter = new XMLEncoder(outputStream);

			writeListOfTasksToArchiveFile(listOfArchiveTasks, fileWriter);

			fileWriter.close();
			outputStream.close();
			logger.log(Level.INFO, logWriteOntoArchiveSuccessful);
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, logWriteOntoArchiveUnsuccessful);
		}
	}

	
	/**
	 * Function to create the Archive File
	 * @return String
	 * @throws FileNotFoundException
	 */
	private String creatingArchiveFile() throws FileNotFoundException {
		String xmlFileLocation = archiveFileName;
		PrintWriter fileXML = new PrintWriter(xmlFileLocation);
		fileXML.close();
		return xmlFileLocation;
	}

	
	/**
	 * Function to write list of tasks onto the Archive File
	 * @param ArrayList
	 * @param XMLEncoder
	 */
	private void writeListOfTasksToArchiveFile(
			ArrayList<Task> listOfArchiveTasks, XMLEncoder fileWriter) {
		Task taskToBeWritten;
		fileWriter.writeObject(listOfArchiveTasks.size());
		for (int iterator = startingIndex0; iterator < listOfArchiveTasks
				.size(); iterator++) {
			taskToBeWritten = listOfArchiveTasks.get(iterator);

			if (!(isMoreThanOneMonthOld(taskToBeWritten)))
				writeTaskObject(taskToBeWritten, fileWriter);
			else
				continue;
		}
	}

	
	/**
	 * Function to write a task object onto a XML file in the desired format
	 * @param Task
	 * @param XMLEncoder
	 */
	private void writeTaskObject(Task taskToBeWritten, XMLEncoder fileWriter) {
		fileWriter.writeObject(taskToBeWritten.getTaskId());
		fileWriter.writeObject(taskToBeWritten.getTaskTitle());
		fileWriter.writeObject(taskToBeWritten.getTaskLocation());
		fileWriter.writeObject(taskToBeWritten.getStartTime());
		fileWriter.writeObject(taskToBeWritten.getEndTime());
		fileWriter.writeObject(taskToBeWritten.getPersonAccompany());
		fileWriter.writeObject(taskToBeWritten.getTaskStatus());
		fileWriter.writeObject(taskToBeWritten.getTaskPriority());
	}

	
	/**
	 * Function to read from XML file and return the tasks read onto a ArrayList
	 * @return ArrayList
	 */
	public ArrayList<Task> readFromFile() {
		try {
			logger = Logger.getLogger(logReadFromFile);

			assert fileName != null;
			if (fileName == null) {
				logger.log(Level.WARNING, logNoFileName);
				return null;
			}

			ArrayList<Task> listOfTasks = new ArrayList<>();

			File fileValidation = new File(fileName);
			if (fileValidation.exists()) {

				boolean emty = isFileEmpty(fileName);
				if (emty)
					return listOfTasks;

				XMLDecoder fileReader = new XMLDecoder(new FileInputStream(
						fileName));
				ArrayList<Task> listofTasksToWriteToArchive = new ArrayList<>();
				readFileContentOntoArrayList(listOfTasks, fileReader,
						listofTasksToWriteToArchive);

				fileReader.close();
			}
			logger.log(Level.INFO, logReadFromFileSuccessful);
			return listOfTasks;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, logReadFromFileUnsuccessful);
		}
		return null;
	}

	
	/**
	 * Function to read tasks from the file and separate them into tasklist or
	 * archive tasklist
	 * @param ArrayList
	 * @param XMLDecoder
	 * @param ArrayList
	 */
	private void readFileContentOntoArrayList(ArrayList<Task> listOfTasks,
			XMLDecoder fileReader, ArrayList<Task> listofTasksToWriteToArchive) {
		int numberOfLines = (int) fileReader.readObject();
		for (int iterator = startingIndex0; iterator < numberOfLines; iterator++) {

			Task taskRead = new Task();
			readTaskObject(fileReader, taskRead);

			if (!(isMoreThanOneWeekOld(taskRead))) {
				listOfTasks.add(taskRead);
			} else {
				listofTasksToWriteToArchive.add(taskRead);
			}
		}
	}
	
	
	/**
	 * Function to check whether the file reading from, is empty or not
	 * @param String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private boolean isFileEmpty(String fileName) throws FileNotFoundException, IOException {
		// Check for empty file
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(new File(fileName));
		int emty = fis.available();
		if (emty == 0)
			return true;
		return false;
	}

	
	/**
	 * Function to return an ArrayList of all the tasks read from the archive
	 * @return ArrayList
	 */
	public ArrayList<Task> readFromArchive() {
		try {
			logger = Logger.getLogger(logReadFromArchive);

			assert fileName != null;
			if (fileName == null) {
				logger.log(Level.WARNING, logNoArchiveFileName);
				return null;
			}
			ArrayList<Task> listOfArchiveTasks = new ArrayList<>();
			File fileValidation = new File(archiveFileName);
			if (fileValidation.exists()) {

				boolean emty = isFileEmpty(archiveFileName);
				if (emty)
					return listOfArchiveTasks;

				XMLDecoder fileReader = new XMLDecoder(new FileInputStream(
						archiveFileName));
				readListFromArchive(listOfArchiveTasks, fileReader);
				fileReader.close();
			}
			logger.log(Level.INFO, logReadFromArchiveSuccessful);
			return listOfArchiveTasks;
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, logReadFromArchiveUnsuccessful);
		}
		return null;
	}

	
	/**
	 * Function to read task objects from file and store on an ArrayList
	 * @param ArrayList
	 * @param XMLDecoder
	 */
	private void readListFromArchive(ArrayList<Task> listOfArchiveTasks,
			XMLDecoder fileReader) {
		int numberOfLines = (int) fileReader.readObject();
		for (int iterator = startingIndex0; iterator < numberOfLines; iterator++) {
			Task taskRead = new Task();
			readTaskObject(fileReader, taskRead);

			if (!(isMoreThanOneMonthOld(taskRead))) {
				listOfArchiveTasks.add(taskRead);
			} else {
				continue;
			}
		}
	}

	
	/**
	 * Function to read the particular task object from the input stream
	 * @param XMLDecoder
	 * @param Task
	 */
	private void readTaskObject(XMLDecoder fileReader, Task taskRead) {
		taskRead.setTaskId((int) fileReader.readObject());
		taskRead.setTaskTitle((String) fileReader.readObject());
		taskRead.setLocation((String) fileReader.readObject());
		taskRead.setStartTime((Date) fileReader.readObject());
		taskRead.setEndTime((Date) fileReader.readObject());
		taskRead.setPersonAccompany((String) fileReader.readObject());
		taskRead.setStatus((Task.Status) fileReader.readObject());
		taskRead.setPriority((Task.Priority) fileReader.readObject());
	}

	
	/**
	 * Function to check if a task is more than one week old
	 * @param Task
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	private boolean isMoreThanOneWeekOld(Task taskRead) {
		Date currentDate = new Date();
		if (taskRead.getEndTime() == null)
			return false;

		Date taskDate = taskRead.getEndTime();
		if (currentDate.compareTo(taskDate) < leastPositiveInteger)
			return false;

		if (currentDate.getYear() == taskDate.getYear()) {
			if (currentDate.getMonth() == taskDate.getMonth()) {
				if (currentDate.getDate() - taskDate.getDate() >= numOfDaysWeek)
					return true;
				else
					return false;
			} else {// months are not same
				if (currentDate.getMonth() > taskDate.getMonth()) {
					if (currentDate.getMonth() - taskDate.getMonth() == nextMonth) {
						return is7DaysDifferenceIfTaskPreviousMonth(
								currentDate, taskDate);
					} else {// current date is more than 2months after
						return true;
					}
				} else {// current date is before task date
					return false;
				}

			}
		} else {
			if (currentDate.getYear() < taskDate.getYear()) {
				return false;
			} else {
				if (currentDate.getYear() - taskDate.getYear() > nextYear) {
					return true;
				} else if (currentDate.getYear() - taskDate.getYear() == nextYear) {
					if (currentDate.getMonth() == janMonth
							&& taskDate.getMonth() == decMonth) {
						int differenceInDays = currentDate.getDate();
						differenceInDays += daysIn31DayMonth
								- taskDate.getDate() + nextDate;
						if (differenceInDays >= numOfDaysWeek)
							return true;
						else
							return false;
					} else {
						return true;
					}
				}
			}
		}

		return false;
	}

	
	/**
	 * Function to check if the task which ends in the previous month is more
	 * than 7 days old or not.
	 * @param Date
	 * @param Date
	 * @return boolean
	 */
	@SuppressWarnings("deprecation")
	private boolean is7DaysDifferenceIfTaskPreviousMonth(Date currentDate,
			Date taskDate) {
		int differenceInDays = noDifference;
		switch (currentDate.getMonth()) {
		case 1:// jan-feb
			differenceInDays = differenceInDaysForTaskIn31DayMonth(
					currentDate, taskDate);
			break;
		case 3:// mar-apr
			differenceInDays = differenceInDaysForTaskIn31DayMonth(
					currentDate, taskDate);
			break;
		case 5:// may-jun
			differenceInDays = differenceInDaysForTaskIn31DayMonth(
					currentDate, taskDate);
			break;
		case 7:// jul-aug
			differenceInDays = differenceInDaysForTaskIn31DayMonth(
					currentDate, taskDate);
			break;
		case 8:// aug-sept
			differenceInDays = differenceInDaysForTaskIn31DayMonth(
					currentDate, taskDate);
			break;
		case 10:// oct-nov
			differenceInDays = differenceInDaysForTaskIn31DayMonth(
					currentDate, taskDate);
			break;
		case 2:// feb-mar
			differenceInDays = differenceInDaysForTaskIn28DayMonth(
					currentDate, taskDate);
			break;
		case 4:// apr-may
			differenceInDays = differenceInDaysForTaskIn30DayMonth(
					currentDate, taskDate);
			break;
		case 6:// jun-jul
			differenceInDays = differenceInDaysForTaskIn30DayMonth(
					currentDate, taskDate);
			break;
		case 9:// sept-oct
			differenceInDays = differenceInDaysForTaskIn30DayMonth(
					currentDate, taskDate);
			break;
		case 11:// nov-dec
			differenceInDays = differenceInDaysForTaskIn30DayMonth(
					currentDate, taskDate);
			break;
		}

		if (differenceInDays >= numOfDaysWeek)
			return true;
		else
			return false;
	}

	
	/**
	 * Function to check if the task is more than one month old to remove
	 * from archive
	 * @param Task
	 * @return Integer
	 */
	@SuppressWarnings("deprecation")
	private boolean isMoreThanOneMonthOld(Task taskRead) {
		Date currentDate = new Date();
		if (taskRead.getEndTime() == null)
			return false;

		Date taskDate = taskRead.getEndTime();
		if (currentDate.compareTo(taskDate) < noDifference)
			return false;

		if (currentDate.getYear() == taskDate.getYear()) {
			if (currentDate.getMonth() <= taskDate.getMonth())
				return false;
			else {
				if (currentDate.getMonth() - taskDate.getMonth() > nextMonth)
					return true;
				else {// currentDate.getMonth() - taskDate.getMonth() = 1
					if (currentDate.getDate() >= taskDate.getDate())
						return true;
					else {
						return false;
					}
				}
			}
		} else {
			if (currentDate.getYear() - taskDate.getYear() > nextYear)
				return true;
			else {
				if (currentDate.getYear() - taskDate.getYear() == nextYear) {
					if (currentDate.getMonth() == janMonth
							&& taskDate.getMonth() == decMonth) {
						if (currentDate.getDate() < taskDate.getDate())
							return false;
						else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					return false;
				}
			}
		}
	}

	/**
	 * Function to return the difference in the number of days between the
	 * endtime of a task in a 28day month and today
	 * @param Date
	 * @param Date
	 * @return Integer
	 */
	@SuppressWarnings("deprecation")
	private int differenceInDaysForTaskIn28DayMonth(Date currentDate,
			Date taskDate) {
		int differenceInDays;
		differenceInDays = currentDate.getDate();
		differenceInDays += daysIn28DayMonth - taskDate.getDate() + nextDate;
		return differenceInDays;
	}

	
	/**
	 * Function to return the difference in the number of days between the
	 * endtime of a task in a 30day month and today
	 * @param Date
	 * @param Date
	 * @return Integer
	 */
	@SuppressWarnings("deprecation")
	private int differenceInDaysForTaskIn30DayMonth(Date currentDate,
			Date taskDate) {
		int differenceInDays;
		differenceInDays = currentDate.getDate();
		differenceInDays += daysIn30DayMonth - taskDate.getDate() + nextDate;
		return differenceInDays;
	}

	
	/**
	 * Function to return the difference in the number of days between the
	 * endtime of a task in a 31day month and today
	 * @param Date
	 * @param Date
	 * @return Integer
	 */
	@SuppressWarnings("deprecation")
	private int differenceInDaysForTaskIn31DayMonth(Date currentDate,
			Date taskDate) {
		int differenceInDays;
		differenceInDays = currentDate.getDate();
		differenceInDays += daysIn31DayMonth - taskDate.getDate() + nextDate;
		return differenceInDays;
	}
}
