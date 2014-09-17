import java.util.Date;
/**************
 * 
 * @author A0090551L
 *
 */
public class Task {
	private int taskId;
	private String taskTitle;
	private Date startTime;
	private Date endTime;
	private String taskLocation;
	private Status taskStatus;
	private Priority taskPriority;
	private String taskPersonAccompany;

	private static final String ERROR_MESSAGE_NON_NEGATIVE_ID = "ID must be non-negative";
	private static final String ERROR_MESSAGE_NULL_STRING = "Input string is null";
	private static final String ERROR_MESSAGE_NULL_TASK = "Input task is null";

	private static final String STRING_SPACE = " ";
	private static final String STRING_EMPTY = "";
	private static final String STRING_AT = " at ";
	private static final String STRING_WITH = " with ";
	private static final String STRING_FROM = " from ";
	private static final String STRING_TO = " to ";
	private static final String STRING_ON = " on ";
	private static final String STRING_ZERO = "0";
	private static final String STRING_COLON = ":";
	
	private static final String STRING_MONTH_JAN = "Jan";
	private static final String STRING_MONTH_FEB = "Feb";
	private static final String STRING_MONTH_MAR = "Mar";
	private static final String STRING_MONTH_APR = "Apr";
	private static final String STRING_MONTH_MAY = "May";
	private static final String STRING_MONTH_JUN = "Jun";
	private static final String STRING_MONTH_JUL = "Jul";
	private static final String STRING_MONTH_AUG = "Aug";
	private static final String STRING_MONTH_SEP = "Sep";
	private static final String STRING_MONTH_OCT = "Oct";
	private static final String STRING_MONTH_NOV = "Nov";
	private static final String STRING_MONTH_DEC = "Dec";
	private static final String STRING_STAR = "*";
	private static final String STRING_DOT = ".";
	
	private static final int INT_YEAR_OFFSET = 1900;
	private static final int INT_DEFAULT_ID = -1;

	public enum Status {
		INCOMPLETE, COMPLETE
	}

	public enum Priority {
		HIGH, LOW, NULL
	}

	public Task() {
	}

	public Task(String title, Date start, Date end, String location,
			String withSomeone, Priority priority) {
		taskTitle = title;
		startTime = start;
		endTime = end;
		taskLocation = location;
		taskPersonAccompany = withSomeone;
		taskPriority = priority;
		taskId = INT_DEFAULT_ID;
		taskStatus = Status.INCOMPLETE;
	}

	// copy Constructor
	public Task(Task newTask) {
		this.setTaskId(newTask.getTaskId());
		if (!isNull(newTask.getTaskTitle())) {
			this.setTaskTitle(new String(newTask.getTaskTitle()));
		}
		if (!isNull(newTask.getTaskLocation())) {
			this.setLocation(new String(newTask.getTaskLocation()));
		}
		if (!isNull(newTask.getStartTime())) {
			this.setStartTime(new Date(newTask.getStartTime().getTime()));
		}
		if (!isNull(newTask.getEndTime())) {
			this.setEndTime(new Date(newTask.getEndTime().getTime()));
		}
		if (!isNull(newTask.getPersonAccompany())){
			this.setPersonAccompany(new String(newTask.getPersonAccompany()));
		}
		if (!isNull(newTask.getTaskPriority())) {
			this.setPriority(newTask.getTaskPriority());
		}
		if (!isNull(newTask.getTaskStatus())) {
			this.setStatus(newTask.getTaskStatus());
		}
	}

	// this function will set an id for this task. This ID must be non-negative.
	public void setTaskId(int id) {
		assert isNonNegative(id) : ERROR_MESSAGE_NON_NEGATIVE_ID;
		taskId = id;
	}

	// this function will set a title for this task. This title must not be null
	// and must not be empty.
	public void setTaskTitle(String title) {
		taskTitle = title;
	}

	public void setStartTime(Date start) {
		startTime = start;
	}

	public void setEndTime(Date end) {
		endTime = end;
	}

	public void setLocation(String location) {
		taskLocation = location;
	}

	public void setStatus(Status status) {
		taskStatus = status;
	}

	public void setPersonAccompany(String personName) {
		taskPersonAccompany = personName;
	}

	public void setPriority(Priority priority) {
		taskPriority = priority;
	}

	public int getTaskId() {
		return taskId;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public String getTaskLocation() {
		return taskLocation;
	}

	public Status getTaskStatus() {
		return taskStatus;
	}

	public Priority getTaskPriority() {
		return taskPriority;
	}

	public String getPersonAccompany() {
		return taskPersonAccompany;
	}

	// this function will check if this task contains the input keyword in any
	// of its components
	public boolean isMatchedKeyword(String keyword) {
		assert !isNull(keyword) : ERROR_MESSAGE_NULL_STRING;
		if (!isInfoValid(keyword)) {
			return false;
		}
		String keywordLowerCase = keyword.toLowerCase();
		if (isToShort(keywordLowerCase)) {
			return isMatchedKeywordShort(keywordLowerCase);
		} else {
			return isMatchedKeywordLong(keywordLowerCase);
		}
	}

	// this function will perform isMatchedKeyword if the input keyword is long
	// enough (at least 3 characters)
	private boolean isMatchedKeywordLong(String keywordLowerCase) {
		if (!isInfoValid(keywordLowerCase)) {
			return false;
		}
		String[] spiltedKeyword = keywordLowerCase.split(STRING_SPACE);
		boolean isMatched = true;
		for (int counter = 0; counter < spiltedKeyword.length; counter++) {
			if (!isMatchedKeywordAssist(spiltedKeyword[counter])) {
				isMatched = false;
			}
		}
		return isMatched;
	}

	// this function will assist the checking for keyword
	private boolean isMatchedKeywordAssist(String str) {
		try {
			if (isTitleMatched(str) || isLocationMatched(str)
					|| isPersonAccompanyMatched(str)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// this function will check if the string str is part of the personAccompany
	// field
	private boolean isPersonAccompanyMatched(String str) {
		return isInfoValid(this.getPersonAccompany())
				&& getPersonAccompany().toLowerCase().contains(str);
	}

	// this function will check if the string str is part of the Location field
	private boolean isLocationMatched(String str) {
		return isInfoValid(this.getTaskLocation())
				&& getTaskLocation().toLowerCase().contains(str);
	}

	// this function will check if the string str is part of the Title field
	private boolean isTitleMatched(String str) {
		return isInfoValid(this.getTaskTitle())
				&& getTaskTitle().toLowerCase().contains(str);
	}

	// this function will perform isMatchedKeyword() if the keyword is short
	// (less than 3 characters)
	private boolean isMatchedKeywordShort(String keywordLowerCase) {
		if (isNull(keywordLowerCase)) {
			return false;
		}
		String[] taskDetails = toCombinedString().toLowerCase().split(
				STRING_SPACE);
		try {
			for (int counter = 0; counter < taskDetails.length; counter++) {
				if (taskDetails[counter].startsWith(keywordLowerCase)) {
					return true;
				}
			}
			this.toCombinedString();
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// this function will combine title, location and personAccompany field
	// together to 1 single String if they are valid
	private String toCombinedString() {
		String combinedString = STRING_EMPTY;
		if (isInfoValid(this.getTaskTitle())) {
			combinedString = combinedString.concat(this.getTaskTitle()
					+ STRING_SPACE);
		}
		if (isInfoValid(this.getTaskLocation())) {
			combinedString = combinedString.concat(this.getTaskLocation()
					+ STRING_SPACE);
		}
		if (isInfoValid(this.getPersonAccompany())) {
			combinedString = combinedString.concat(this.getPersonAccompany());
		}
		return combinedString;
	}

	// this function will check if the string str is not null and is not empty
	private boolean isInfoValid(String str) {
		if (!isNull(str) && str.length() != 0) {
			return true;
		}
		return false;
	}

	// this function will check if a string is shorter than 3
	private boolean isToShort(String str) {
		if (str.length() < 3) {
			return true;
		}
		return false;
	}
	//this function will check if the input date matches any timing component of the task
	@SuppressWarnings("deprecation")
	public boolean isMatchedDate(Date date) {
		if (isNull(date)) {
			return false;
		}
		try {
			if (isTheSameDay(startTime, date)) {
				if (date.getSeconds() != 0) {
					return true;
				}
				if (isTheSameTime(startTime, date)) {
					return true;
				}
			}

			if (isTheSameDay(endTime, date)) {
				if (date.getSeconds() != 0) {
					return true;
				}
				if (isTheSameTime(endTime, date)) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	//this function will collate all the info of this task and put them into a string
	public String toString() {
		String taskDetails = STRING_EMPTY;
		try {
			if (!isNull(taskTitle)) {
				taskDetails = taskTitle;
			}
			if (!isNull(taskLocation)) {
				taskDetails = taskDetails.concat(STRING_AT + taskLocation);
			}
			if (!isNull(taskPersonAccompany)) {
				taskDetails = taskDetails
						.concat(STRING_WITH + taskPersonAccompany);
			}
			if (!isNull(startTime) && !isNull(endTime)) {
				taskDetails = taskDetails.concat(STRING_FROM
							+ timeString(startTime) + STRING_TO
							+ timeString(endTime));
				
			}
			if (isNull(startTime) && !isNull(endTime)) {
				taskDetails = taskDetails.concat(STRING_ON + timeString(endTime));
			}
			/*
			 * if (taskPriority != Priority.NULL && taskPriority != null) {
			 * taskDetails = taskDetails.concat(" - Priority: " +
			 * taskPriority.toString()); }
			 */

			// use this only fo debugging purpose
			// taskDetails = taskDetails.concat(" ID: " +
			// Integer.toString(taskId));
			if(taskPriority == Priority.HIGH){
				taskDetails = taskDetails.concat(STRING_STAR);
			}else if(taskPriority == Priority.LOW){
				taskDetails = taskDetails.concat(STRING_DOT);
			}
		} catch (Exception e) {
			taskDetails = STRING_EMPTY;
		}
		return taskDetails;
	}
	//this function will check if 2 dates are the same
	@SuppressWarnings("deprecation")
	private boolean isTheSameDay(Date dateToCheck1, Date dateToCheck2) {
		if (isNull(dateToCheck1) || isNull(dateToCheck2)) {
			return false;
		}
		try {
			if (dateToCheck1.getYear() == dateToCheck2.getYear()
					&& dateToCheck1.getMonth() == dateToCheck2.getMonth()
					&& dateToCheck1.getDate() == dateToCheck2.getDate())
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	//this function will check if 2 time are the same
	@SuppressWarnings("deprecation")
	private boolean isTheSameTime(Date dateToCheck1, Date dateToCheck2) {
		if (isNull(dateToCheck1) || isNull(dateToCheck2)) {
			return false;
		}
		try {
			if (dateToCheck1.getHours() == dateToCheck2.getHours()
					&& dateToCheck1.getMinutes() == dateToCheck2.getMinutes())
				return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	// this function will check if the input task has any same component as this task
	public boolean isSameTask(Task targetTask) {
		assert !isNull(targetTask) : ERROR_MESSAGE_NULL_TASK;

		if (isInfoValid(targetTask.getTaskTitle())
				&& isMatchedKeyword(targetTask.getTaskTitle())
				|| (isInfoValid(targetTask.getTaskLocation()) && isMatchedKeyword(targetTask
						.getTaskLocation()))
				|| (isInfoValid(targetTask.getPersonAccompany()) && isMatchedKeyword(targetTask
						.getPersonAccompany()))) {
			return true;
		}
		if ((!isNull(targetTask.getStartTime()) && isMatchedDate(targetTask
				.getStartTime()))
				|| (!isNull(targetTask.getEndTime()) && isMatchedDate(targetTask
						.getEndTime()))) {
			return true;
		}
		return false;
	}
	//this function will check if the taskToCompare is exactly same as the task
	public boolean exactComparison(Task taskToCompare) {
		if (isNull(taskToCompare)) {
			return false;
		}
		if (toString().equals(taskToCompare.toString()))
			return true;
		return false;
	}
	//this function will collate all the timing component to a single string
	@SuppressWarnings("deprecation")
	private String timeString(Date time) {
		if (time == null) {
			return STRING_EMPTY;
		}
		int hour = time.getHours();
		String hourString = STRING_EMPTY;
		if (hour < 10) {
			hourString = hourString.concat(STRING_ZERO + hour);
		} else {
			hourString = Integer.toString(hour);
		}
		int min = time.getMinutes();
		String minString = STRING_EMPTY;
		if (min < 10) {
			minString = minString.concat(STRING_ZERO + min);
		} else {
			minString = Integer.toString(min);
		}

		String stringOfTime = STRING_EMPTY;

		/*
		 * stringOfTime = stringOfTime.concat(hourString + ":" + minString + " "
		 * + convertDay(time.getDay()) + " " + time.getDate() + " " +
		 * convertMonth(time.getMonth()) + " " + convertYear(time.getYear()));
		 */

		if (time.getSeconds() == 1) {
			stringOfTime = stringOfTime.concat(time.getDate() + STRING_SPACE
					+ convertMonth(time.getMonth()) + STRING_SPACE
					+ convertYear(time.getYear()));
		} else {
			stringOfTime = stringOfTime.concat(hourString + STRING_COLON + minString
					+ STRING_SPACE + time.getDate() + STRING_SPACE
					+ convertMonth(time.getMonth()) + STRING_SPACE
					+ convertYear(time.getYear()));
		}
		return stringOfTime;
	}
	//this function will convert the year to the correct form
	private String convertYear(int year) {
		return Integer.toString(year + INT_YEAR_OFFSET);
	}

	
	//this function will convert month to readable from
	private String convertMonth(int month) {
		switch (month) {
		case 0:
			return STRING_MONTH_JAN;
		case 1:
			return STRING_MONTH_FEB;
		case 2:
			return STRING_MONTH_MAR;
		case 3:
			return STRING_MONTH_APR;
		case 4:
			return STRING_MONTH_MAY;
		case 5:
			return STRING_MONTH_JUN;
		case 6:
			return STRING_MONTH_JUL;
		case 7:
			return STRING_MONTH_AUG;
		case 8:
			return STRING_MONTH_SEP;
		case 9:
			return STRING_MONTH_OCT;
		case 10:
			return STRING_MONTH_NOV;
		case 11:
			return STRING_MONTH_DEC;
		default:
			return STRING_EMPTY;
		}
	}
	//check if num is non-negative
	private boolean isNonNegative(int num) {
		if (num >= 0) {
			return true;
		}
		return false;
	}
	//check if object is null
	private boolean isNull(Object obj){
		if(obj == null){
			return true;
		}
		return false;
	}
}
