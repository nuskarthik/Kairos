import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

public class Logic {
	
/**
 * 
 * @author A0091523L
 *
 */
	private static final String fromKeyword = "from";
	private static final String toKeyword = "to";
	private static final String atKeyword = "at";
	private static final String onKeyword = "on";
	private static final String inKeyword = "in";
	private static final String setKeyword = "set";
	private static final String withKeyword = "with";
	private static final String shortWithKeyword = "w/";
	private static final String priorityKeyword = "priority";
	private static final String byKeyword = "by";
	private static final String beforeKeyword = "before";
	private static String[] timePhrases = { "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec","Mon","Tue","Wed","Thu","Fri","Sat","Sun" };
	
	private static List<Date> datesExtracted;
	
	KairosDictionary dictionary;
	
	Task taskDetails;
	String[] wordsToBeParsed ;
	String wordsToBeExcluded ;
	
	
	
	public Logic(KairosDictionary dictionaryObject) {
		//Dictionary contains the list of all the commands to be used
		dictionary = dictionaryObject;
	}
	
	public static KairosDictionary.CommandType determineCommandType(String command){
		if(command == null)
		{
			return KairosDictionary.CommandType.INVALID;
		}
		
		String[] spaceCheck = command.split(" ");
		try{
			switch(KairosDictionary.getWord(spaceCheck[0].toLowerCase())){
			case ADD:
				return KairosDictionary.CommandType.ADD;	
			case EDIT:
				return KairosDictionary.CommandType.EDIT;
			case DELETE:
				return KairosDictionary.CommandType.DELETE;
			case DONE:
				return KairosDictionary.CommandType.DONE;	
			case UNDONE:
				return KairosDictionary.CommandType.UNDONE;
			case VIEW:
				return KairosDictionary.CommandType.VIEW;
			case UNDO:
				return KairosDictionary.CommandType.UNDO;	
			case REDO:
				return KairosDictionary.CommandType.REDO;
			case KAIROS:
				return KairosDictionary.CommandType.KAIROS;
			case EXIT:
				return KairosDictionary.CommandType.EXIT;
			default:
			return KairosDictionary.CommandType.INVALID;
			}
		}catch(NullPointerException e){
			return KairosDictionary.CommandType.INVALID;
		}
	}
	
	
	
	
	public Task commandParse(String command) {
	String[] commandKeywords = command.split(" ",2);
	KairosDictionary.CommandType commandType = determineCommandType(commandKeywords[0].toLowerCase());
	Task taskDetails = new Task();
	
	switch(commandType){
	case ADD:
		taskDetails = interpretCommandDetails(commandKeywords[1]);
		break;
	case EDIT:
		taskDetails = interpretCommandDetails(commandKeywords[1]);
		break;
	case DELETE:
		taskDetails = interpretCommandDetails(commandKeywords[1]);
		break;
	case VIEW:
		taskDetails = interpretCommandDetails(commandKeywords[1]);
		interpretViewDetails(taskDetails,commandKeywords[1]);
		break;
	case DONE:
		taskDetails = interpretCommandDetails(commandKeywords[1]);
		break;
	case UNDONE:
		taskDetails = interpretCommandDetails(commandKeywords[1]);
		break;
	default:
		break;
	}
	return taskDetails;
	}
	
	private boolean isResembleDate(int i){
		return (wordsToBeParsed[i+1].matches(".*\\d.*") || isWrongParsing(wordsToBeParsed[i+1]));
	}
	
	private boolean isFromKeyword(String checkWord){
		return checkWord.equalsIgnoreCase(fromKeyword);
	}
	
	private boolean isToKeyword(String checkWord){
		return checkWord.equalsIgnoreCase(toKeyword);
	}
	
	private boolean isAtKeyword(String checkWord){
		return checkWord.equalsIgnoreCase(atKeyword)||checkWord.equalsIgnoreCase(inKeyword);
	}
	
	private boolean isWithKeyword(String checkWord){
		return checkWord.equalsIgnoreCase(withKeyword)||checkWord.equalsIgnoreCase(shortWithKeyword);
	}
	
	private boolean isSetKeyword(String checkWord){
		return checkWord.equalsIgnoreCase(setKeyword)||checkWord.equalsIgnoreCase(priorityKeyword);
	}
	
	private boolean isWordToAvoidAdding(String checkWord){
		return checkWord.matches(".*\\d.*")|checkWord.equalsIgnoreCase("am")|checkWord.equalsIgnoreCase("pm")|checkWord.equalsIgnoreCase("next")|checkWord.equalsIgnoreCase("last")|isWrongParsing(checkWord)|checkWord.contains("today")|checkWord.equalsIgnoreCase("from")|checkWord.equalsIgnoreCase("to")|checkWord.equalsIgnoreCase("on");
	}
	
	private boolean isKeyword(String checkWord){
		return checkWord.equalsIgnoreCase(atKeyword)|checkWord.equalsIgnoreCase(priorityKeyword) |checkWord.equalsIgnoreCase(inKeyword)|checkWord.equalsIgnoreCase(fromKeyword)|checkWord.equalsIgnoreCase(toKeyword)|checkWord.equalsIgnoreCase(onKeyword)|checkWord.equalsIgnoreCase(byKeyword) |checkWord.equalsIgnoreCase(setKeyword) | checkWord.equalsIgnoreCase(withKeyword) | checkWord.equalsIgnoreCase(byKeyword) | checkWord.equalsIgnoreCase(beforeKeyword) | checkWord.equalsIgnoreCase(withKeyword) |checkWord.equalsIgnoreCase(shortWithKeyword);
	}
	
	private Task interpretCommandDetails(String commandKeywords){
		assert commandKeywords!=null;
		
		taskDetails = new Task(null,null,null,null, null,Task.Priority.NULL);
		wordsToBeParsed = commandKeywords.split(" ");
		wordsToBeExcluded = "";
		String taskTitle = "";
		int positionOfFrom =0, positionOfTo=1;
		
		commandKeywords = swapDate(wordsToBeParsed);
		
		for(int i=0; i< wordsToBeParsed.length;i++){
			
			taskTitle = processQuotedWords(i, taskTitle);
			
			if(isFromKeyword(wordsToBeParsed[i])){
				try{
					if(!isResembleDate(i) && i!=wordsToBeParsed.length-1){
						taskTitle+=wordsToBeParsed[i]+" ";
					}
				}catch(ArrayIndexOutOfBoundsException e){
				}
				positionOfFrom = i;
			}
			
			
			if(isToKeyword(wordsToBeParsed[i])){
				try{
					if(!isResembleDate(i) && i!=wordsToBeParsed.length-1){
						taskTitle+=wordsToBeParsed[i]+" ";
					}
				}catch(ArrayIndexOutOfBoundsException e){
				}
				positionOfTo = i;
			}
			
			
			if(isAtKeyword(wordsToBeParsed[i])){
				parseAtKeyword(i);
			}
			
			else if(isWithKeyword(wordsToBeParsed[i])){
				parseWithKeyword(i);
			}
			
			else if(isSetKeyword(wordsToBeParsed[i])){
				i = setPriority(i);
				continue;
			}
			
			taskTitle = addToTitle(i, taskTitle);
				
		}
		if(wordsToBeExcluded.isEmpty())
		{
			setTime(commandKeywords, positionOfFrom, positionOfTo);
		}
		else
		{
			String[] excludedWords = wordsToBeExcluded.split(" ");
			String[] wordsInCommand = wordsToBeParsed;
			String toBeParsed = ""; 
			
			for(int i=0;i<wordsInCommand.length;i++){
				int flag=0;
				for(int j=0;j<excludedWords.length;j++){
					if(wordsInCommand[i].equals(excludedWords[j])){
						flag=1;
					}
				}
				if(flag==0){
					if(!(taskTitle.contains(wordsInCommand[i]) | taskDetails.toString().contains(wordsInCommand[i]))){
						toBeParsed+=wordsInCommand[i]+" ";
					}
				}
			}
			
			String[] checkQuotedWords = wordsToBeExcluded.split(" ");
			for( int i=0;i< checkQuotedWords.length;i++){
				if(!taskDetails.toString().contains(checkQuotedWords[i]) && !taskTitle.contains(checkQuotedWords[i])){
					taskTitle+=checkQuotedWords[i]+" ";
				}
			}
			
			setTime(toBeParsed, positionOfFrom, positionOfTo);
		}
		
		
		if(!taskTitle.isEmpty()){
		taskTitle = taskTitle.substring(0,taskTitle.length()-1);
		taskDetails.setTaskTitle(taskTitle);
		}
		return taskDetails;
	}
	
	//Used to adjust the Date format for parsing from Western format to Eastern format 
	private String swapDate(String[] wordsToBeParsed){
		String dateFixedCommandKeywords="";
		for(int i=0;i< wordsToBeParsed.length ;i++){
			if(wordsToBeParsed[i].contains("/")){
				String[] dateSplit = wordsToBeParsed[i].split("/");
				if(dateSplit.length>=2){
					wordsToBeParsed[i]=dateSplit[1]+"/"+dateSplit[0];
					if(dateSplit.length==3){
						wordsToBeParsed[i]+="/"+dateSplit[2];
					}
				}
			}
			if(wordsToBeParsed[i].contains("\\")){
					String[] dateSplit = wordsToBeParsed[i].split("\\\\");
					if(dateSplit.length>=2){
						wordsToBeParsed[i]=dateSplit[1]+"\\"+dateSplit[0];
						if(dateSplit.length==3){
							wordsToBeParsed[i]+="\\"+dateSplit[2];
						}
					}
			}
			if(wordsToBeParsed[i].contains("-")){
				String[] dateSplit = wordsToBeParsed[i].split("-");
				if(dateSplit.length>=2){
					wordsToBeParsed[i]=dateSplit[1]+"-"+dateSplit[0];
					if(dateSplit.length==3){
						wordsToBeParsed[i]+="-"+dateSplit[2];
					}
				}
			}
			dateFixedCommandKeywords+=wordsToBeParsed[i]+" ";
		}
		return dateFixedCommandKeywords;
	}
	
	
	private void parseAtKeyword(int i){
		String location = "";
		for(int j =i+1; j< wordsToBeParsed.length ;j++){
			
			if(wordsToBeParsed[j].startsWith("\"")){
				int k=0;
				location+=wordsToBeParsed[j]+" ";
				wordsToBeExcluded+=wordsToBeParsed[j]+" ";
				if(wordsToBeParsed[j].endsWith("\"") && !wordsToBeParsed[j].equals("\"")){
				}
				else{
					for(k=j+1; k< wordsToBeParsed.length;k++){
						if(!wordsToBeParsed[k].isEmpty()){
							if(wordsToBeParsed[k].endsWith("\"") && !wordsToBeParsed[k].equals("\"")){
								location+=wordsToBeParsed[k]+" ";
								wordsToBeExcluded+=wordsToBeParsed[k]+" ";
								break;
							}
							else{
								location+=wordsToBeParsed[k]+" ";
								wordsToBeExcluded+=wordsToBeParsed[k]+" ";
							}
						}
					}
				}
				if(k!=0)
					j=k;
				continue;
			}
			
			else{
				if(!(isWordToAvoidAdding(wordsToBeParsed[j]))){
					if(!isKeyword(wordsToBeParsed[j])){
						location += wordsToBeParsed[j]+" ";
					}
					else {
						i=j-1;
						break;
					}
				}
			}
			if(j==wordsToBeParsed.length-1){
				i=j;
			}
		}
		if(!location.isEmpty()){
			String[] checkSpace = location.split(" ");
			String locationWithoutQuotes = "";
			if(checkSpace.length!=0){
				for(int j=0;j<checkSpace.length;j++){
					if(!(wordsToBeExcluded.contains(checkSpace[j]))){
						locationWithoutQuotes+=checkSpace[j]+" ";
					}
				}
			}
			extractDatesFromCommand(locationWithoutQuotes);
			try{
				datesExtracted.size();
			}catch(NullPointerException e){
				
				if(checkSpace.length!=0){
					location = location.substring(0,location.length()-1);
					taskDetails.setLocation(location);
				}
			}
		}
	}	
	
	
	private void parseWithKeyword(int i){
		String name = "";
		for(int j =i+1; j< wordsToBeParsed.length ;j++){
			
			if(wordsToBeParsed[j].startsWith("\"")){
				int k=0;
				name+=wordsToBeParsed[j]+" ";
				wordsToBeExcluded+=wordsToBeParsed[j]+" ";
				if(wordsToBeParsed[j].endsWith("\"") && !wordsToBeParsed[j].equals("\"")){
				}
				else{
					for(k=j+1; k< wordsToBeParsed.length;k++){
						if(!wordsToBeParsed[k].isEmpty()){
							if(wordsToBeParsed[k].endsWith("\"") && !wordsToBeParsed[k].equals("\"")){
								name+=wordsToBeParsed[k]+" ";
								wordsToBeExcluded+=wordsToBeParsed[k]+" ";
								break;
							}
							else{
								name+=wordsToBeParsed[k]+" ";
								wordsToBeExcluded+=wordsToBeParsed[k]+" ";
							}
						}
					}
				}
				if(k!=0)
					j=k;
				continue;
			}

			else{
				if(!(isWordToAvoidAdding(wordsToBeParsed[j]))){
					if(!isKeyword(wordsToBeParsed[j])){
						name += wordsToBeParsed[j]+" ";
					}
					else {
						i=j-1;
						break;
					}
				}
			}
			if(j==wordsToBeParsed.length-1){
				i=j;
			}
		}
		
		if(!name.isEmpty()){
			String[] checkSpace = name.split(" ");
			String nameWithoutQuotes = "";
			if(checkSpace.length!=0){
				for(int j=0;j<checkSpace.length;j++){
					if(!(wordsToBeExcluded.contains(checkSpace[j]))){
						nameWithoutQuotes+=checkSpace[j]+" ";
					}
				}
			}
			extractDatesFromCommand(nameWithoutQuotes);
			try{
				datesExtracted.size();
			}catch(NullPointerException e){
				if(checkSpace.length!=0){
					name = name.substring(0,name.length()-1);
					taskDetails.setPersonAccompany(name);
				}
			}
		}
	}
	
	private String addToTitle(int i, String taskTitle){
		extractDatesFromCommand(wordsToBeParsed[i]);
		try{
			datesExtracted.get(0);
			for(String s: timePhrases){
				if(wordsToBeParsed[i].toLowerCase().contains(s.toLowerCase())){
					if(!isWrongParsing(wordsToBeParsed[i]) ){	
						String[] checkSpace = taskTitle.split(" ");
						if(checkSpace.length!=0){
							taskTitle+=wordsToBeParsed[i]+" ";
						}
						break;
					}
				}
			}
		}catch(NullPointerException e){
			if(!(isWordToAvoidAdding(wordsToBeParsed[i])| isKeyword(wordsToBeParsed[i]))){
				if(!wordsToBeParsed[i].matches(".*\\d.*")){
					if(!( (taskDetails.toString().contains(wordsToBeParsed[i]) || taskTitle.contains(wordsToBeParsed[i])) ) ){
						String[] checkSpace = taskTitle.split(" ");
						if(checkSpace.length!=0){
							taskTitle+=wordsToBeParsed[i]+" ";
						}
					}
				}
			}
		}
		return taskTitle;
	}
	
	private String processQuotedWords(int i,String taskTitle){
		if(wordsToBeParsed[i].startsWith("\"")){
			if(wordsToBeExcluded.contains(wordsToBeParsed[i])){
				return taskTitle;
			}
			int j=0;
			taskTitle+=wordsToBeParsed[i]+" ";
			wordsToBeExcluded+=wordsToBeParsed[i]+" ";
			if(wordsToBeParsed[i].endsWith("\"") && !wordsToBeParsed[i].equals("\"")){
				j=i+1;
			}
			else{
				for(j=i+1; j< wordsToBeParsed.length;j++){
					if(!wordsToBeParsed[j].isEmpty()){
						if(wordsToBeParsed[j].endsWith("\"") && !wordsToBeParsed[j].equals("\"")){
							taskTitle+=wordsToBeParsed[j]+" ";
							wordsToBeExcluded+=wordsToBeParsed[j]+" ";
							break;
						}
						else{
							taskTitle+=wordsToBeParsed[j]+" ";
							wordsToBeExcluded+=wordsToBeParsed[j]+" ";
						}
					}
				}
			}
			if(j!=0)
				i=j;
		}
		return taskTitle;
	}

	private void setTime(String commandKeywords, int positionOfFrom,
			int positionOfTo) {
		extractDatesFromCommand(commandKeywords);
		try{
		if(datesExtracted.size()==1){
			taskDetails.setEndTime(datesExtracted.get(0));
		}
		else if(datesExtracted.size()==2){
			if(positionOfFrom<positionOfTo){
			taskDetails.setEndTime(datesExtracted.get(1));
			taskDetails.setStartTime(datesExtracted.get(0));
			}
			else{
				taskDetails.setEndTime(datesExtracted.get(0));
				taskDetails.setStartTime(datesExtracted.get(1));
			}
		}
		}
		catch(NullPointerException e){	
		}
	}

	private int setPriority(int i) {
		String priority = "";
		if(i!=wordsToBeParsed.length-1){
			priority= wordsToBeParsed[i+1];
			i+=2;
		}
			switch(priority.toLowerCase()){
			case "critical":
			case "urgent":
			case "important":
			case "high":
				taskDetails.setPriority(Task.Priority.HIGH);
				break;
			case "moderate":
			case "medium":
			case "low":
				taskDetails.setPriority(Task.Priority.LOW);
				break;
			default:
				break;
			}
		return i;
	}
	
	private static boolean isMonthOrDay(String check, String s){
		switch(check.toLowerCase()){
		case "jan":
			if(s.equals("January")||s.equals("Jan")||s.equals("january")||s.equals("jan")){
				return true;
			}
			break;
		case "feb":
			if(s.equals("February")||s.equals("Feb")||s.equals("february")||s.equals("feb")){
				return true;
			}
			break;
		case "mar":
			if(s.equals("March")||s.equals("Mar")||s.equals("march")||s.equals("mar")){
				return true;
			}
			break;
		case "apr":
			if(s.equals("April")||s.equals("Apr")||s.equals("april")||s.equals("apr")){
				return true;
			}
			break;
		case "may":
			if(s.equals("May")||s.equals("May")||s.equals("may")||s.equals("may")){
				return true;
			}
			break;
		case "jun":
			if(s.equals("June")||s.equals("Jun")||s.equals("june")||s.equals("jun")){
				return true;
			}
			break;
		case "jul":
			if(s.equals("July")||s.equals("Jul")||s.equals("july")||s.equals("jul")){
				return true;
			}
			break;
		case "aug":
			if(s.equals("August")||s.equals("Aug")||s.equals("august")||s.equals("aug")){
				return true;
			}
			break;
		case "sep":
			if(s.equals("September")||s.equals("Sept")||s.equals("september")||s.equals("sept")||s.equals("Sep")||s.equals("sep")){
				return true;
			}
			break;
		case "oct":
			if(s.equals("October")||s.equals("Oct")||s.equals("october")||s.equals("oct")){
				return true;
			}
			break;
		case "nov":
			if(s.equals("November")||s.equals("Nov")||s.equals("november")||s.equals("nov")){
				return true;
			}
			break;
		case "dec":
			if(s.equals("December")||s.equals("Dec")||s.equals("december")||s.equals("dec")){
				return true;
			}
			break;
		case "mon":
			if(s.equals("Monday")||s.equals("Mon")||s.equals("monday")||s.equals("mon")){
				return true;
			}
			break;
		case "tue":
			if(s.equals("Tuesday")||s.equals("tuesday")||s.equals("Tue")||s.equals("tue")||s.equals("Tues")||s.equals("tues")){
				return true;
			}
			break;
		case "wed":
			if(s.equals("Wednesday")||s.equals("Wed")||s.equals("wednesday")||s.equals("wed")){
				return true;
			}
			break;
		case "thu":
			if(s.equals("Thursday")||s.equals("Thurs")||s.equals("thursday")||s.equals("thurs")||s.equals("Thur")||s.equals("thur")){
				return true;
			}
			break;
		case "fri":
			if(s.equals("Friday")||s.equals("Fri")||s.equals("friday")||s.equals("fri")){
				return true;
			}
			break;
		case "sat":
			if(s.equals("Saturday")||s.equals("Sat")||s.equals("saturday")||s.equals("sat")){
				return true;
			}
			break;
		case "sun":
			if(s.equals("Sunday")||s.equals("Sun")||s.equals("sunday")||s.equals("sun")){
				return true;
			}
			break;
		}
		return false;
	}
	private static boolean isWrongParsing(String s){
		boolean isWrong = false;
		if(s.length()<3)
			return false; 
		String prefix = s.substring(0, 3);
		for(String check : timePhrases){
			if(prefix.toLowerCase().equalsIgnoreCase(check)){
				isWrong = isMonthOrDay(check.toLowerCase(),s);
			}
		}
		return isWrong;
	}

	@SuppressWarnings("deprecation")
	private static void extractDatesFromCommand(String command){
		Parser parser = new Parser();
		List<DateGroup> dateGroupsExtracted = parser.parse(command);
		if(dateGroupsExtracted.size()!=0)
		{
			for(DateGroup group:dateGroupsExtracted) {
				datesExtracted = group.getDates();
			}
			Date referenceDate = new Date();
			for(Date extractedDate:datesExtracted){
				if(extractedDate.getHours()==referenceDate.getHours() || extractedDate.getHours()==referenceDate.getHours()+1){
					if(extractedDate.getMinutes()==referenceDate.getMinutes()||extractedDate.getMinutes()==referenceDate.getMinutes()+1||extractedDate.getMinutes()==referenceDate.getMinutes()-1){
						extractedDate.setHours(0);
						extractedDate.setMinutes(0);
						extractedDate.setSeconds(1);
					}
				}
			}
		}
		else
			datesExtracted=null;
 	}
	
	/**
	 * Function used to interpret special view details
	 * @author A0091545A and A0091523L
	 */
	@SuppressWarnings("deprecation")
	private void interpretViewDetails(Task taskDetails,String commandKeywords){
		try{
			taskDetails.getStartTime();
		}catch(NullPointerException e){
			Date currentDate = new Date();
			currentDate.setHours(0);
			currentDate.setMinutes(0);
			currentDate.setSeconds(1);
			taskDetails.setStartTime(currentDate);
		}
		if(commandKeywords.toLowerCase().contains("all")){
			taskDetails.setTaskTitle(" ");
		}
		if(commandKeywords.toLowerCase().equals("undone")){
			taskDetails.setTaskTitle(" ");
		}
		if(commandKeywords.toLowerCase().equals("done")){
			taskDetails.setStatus(Task.Status.COMPLETE);
		}
		if(commandKeywords.toLowerCase().contains("undone") && !commandKeywords.toLowerCase().equals("undone")){
			taskDetails.setStatus(Task.Status.INCOMPLETE);
			String words[] = commandKeywords.toLowerCase().split(" ");
			String title = null;
			for(int i=0; i<words.length; i++){
				if (!words[i].equals("undone")) {
					// if there is a word "done"
					if (words[i].equals("\"undone\"")) {
						words[i] = "undone";
					}

					if (title == null)
						title = words[i];
					else {
						title = title + " " + words[i];
					}
				}
			}
			taskDetails.setTaskTitle(title);
		}
		if (commandKeywords.toLowerCase().contains("done") && !commandKeywords.toLowerCase().equals("done") && !commandKeywords.toLowerCase().equals("undone")) {
			String[] words = taskDetails.getTaskTitle().split(" ");
			String title = new String();
			title = null;
			for (int iterator = 0; iterator < words.length; iterator++)
				if (!words[iterator].equals("done")) {
					// if there is a word "done"
					if (words[iterator].equals("\"done\"")) {
						words[iterator] = "done";
					}

					if (title == null)
						title = words[iterator];
					else {
						title = title + " " + words[iterator];
					}
				} else {
					taskDetails.setStatus(Task.Status.COMPLETE);
				}
			taskDetails.setTaskTitle(title);
			
		}
		if(commandKeywords.toLowerCase().contains("this week")){
			Date date = extractDateThisWeek(taskDetails);
			taskDetails.setEndTime(date);
		}
		if(commandKeywords.toLowerCase().contains("next week")){
			Date date = extractDateNextWeek(taskDetails);
			taskDetails.setEndTime(date);
		}
		if(commandKeywords.toLowerCase().contains("this month")){
			Date date = extractDateThisMonth(taskDetails);
			taskDetails.setEndTime(date);
		}
		if(commandKeywords.toLowerCase().contains("next month")){
			Date date = extractDateNextMonth(taskDetails);
			taskDetails.setEndTime(date);
		}
		if(commandKeywords.toLowerCase().contains("this year")){
			Date date = extractDateThisYear(taskDetails);
			taskDetails.setEndTime(date);
		}
		if(commandKeywords.toLowerCase().contains("next year")){
			Date date = extractDateNextYear(taskDetails);
			taskDetails.setEndTime(date);
		}
	}

	@SuppressWarnings("deprecation")
	private Date extractDateThisWeek(Task taskDetails) {
		Calendar c = Calendar.getInstance();
		Date date;
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		taskDetails.setStartTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		return date;
	}

	@SuppressWarnings("deprecation")
	private Date extractDateNextWeek(Task taskDetails) {
		Calendar c = Calendar.getInstance();
		Date date;
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		c.add(Calendar.DAY_OF_YEAR, 7);
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		taskDetails.setStartTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		c.add(Calendar.DAY_OF_YEAR, 7	 );
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		return date;
	}

	@SuppressWarnings("deprecation")
	private Date extractDateThisMonth(Task taskDetails) {
		Calendar c = Calendar.getInstance();
		Date date;
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		taskDetails.setStartTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));	
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		return date;
	}

	@SuppressWarnings("deprecation")
	private Date extractDateNextMonth(Task taskDetails) {
		Calendar c = Calendar.getInstance();
		Date date;
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.add(Calendar.MONTH, c.get(Calendar.MONTH)+1);
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		taskDetails.setStartTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		return date;
	}

	@SuppressWarnings("deprecation")
	private Date extractDateThisYear(Task taskDetails) {
		Calendar c = Calendar.getInstance();
		Date date;
		c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		taskDetails.setStartTime(date);
		c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));	
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		return date;
	}

	@SuppressWarnings("deprecation")
	private Date extractDateNextYear(Task taskDetails) {
		Calendar c = Calendar.getInstance();
		Date date;
		c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
		c.add(Calendar.YEAR, 1);
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		taskDetails.setStartTime(date);
		c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
		date = c.getTime();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(1);
		return date;
	}
}
