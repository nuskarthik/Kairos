import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * 
 * @author A0091523L
 *
 */

public class LogicTest {

	@SuppressWarnings({ "static-access", "deprecation" })
	@Test
	public void test() {
		KairosDictionary kairosDictionary = new KairosDictionary();
		Logic logic = new Logic(kairosDictionary);
		
		
		assertEquals(KairosDictionary.CommandType.ADD, logic.determineCommandType("AdD aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.EDIT, logic.determineCommandType("change aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.EDIT, logic.determineCommandType("update aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.DELETE, logic.determineCommandType("- aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.DONE, logic.determineCommandType("completed aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.UNDONE, logic.determineCommandType("undone aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.VIEW, logic.determineCommandType("find aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.EXIT, logic.determineCommandType("quit aajsdn asdfa;lsdnv a;sdkjf"));
		assertEquals(KairosDictionary.CommandType.INVALID, logic.determineCommandType("aajsdn asdfa;lsdnv a;sdkjf"));
		
		Date currentDate = new Date();
		
		Task newTask = logic.commandParse("add meeting John from 10:20 today to 11:20 next Tuesday at NUS");
		Date nextTuesday = new Date();
		nextTuesday = getDayRequired(2,1);
		assertEquals(newTask.toString(), "meeting John at NUS from 10:20 "+currentDate.getDate()+" "+getMonthPrefix(currentDate.getMonth()+1)+" "+ (1900+currentDate.getYear())+" to 11:20 "+nextTuesday.getDate()+" "+getMonthPrefix(nextTuesday.getMonth()+1)+" "+(1900+nextTuesday.getYear()));
		
		String taskTime10MinInTheFuture = addToTheFuture10Min(currentDate);
		Task newTask1 = logic.commandParse("add go from home to school at "+taskTime10MinInTheFuture+" pm");
		assertEquals(newTask1.toString(), "go from home to school on "+taskTime10MinInTheFuture+" 12 Nov 2012");
		
		Task newTask3 = logic.commandParse("+ submit report on 6:30 Friday 26/12/2012");
		assertEquals(newTask3.toString(), "submit report on 06:30 26 Dec 2012");
		
		Task newTask4 = logic.commandParse("view done today");
		assertEquals(newTask4.toString(), " on "+currentDate.getDate()+" "+getMonthPrefix(currentDate.getMonth()+1)+" "+ (1900+currentDate.getYear()));
		
		Task newTask2 = logic.commandParse("add meeting with boss at \"21 arab street\" set high");
		assertEquals(newTask2.toString(), "meeting at \"21 arab street\" with boss*");
	}
	private String getMonthPrefix(int i){
		switch(i){
		case 1:
			return "Jan";
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Apr";
		case 5:
			return "May";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Aug";
		case 9:
			return "Sept";
		case 10:
			return "Oct";
		case 11:
			return "Nov";
		case 12:
			return "Dec";
		}
		return "Jan";
	}
	@SuppressWarnings("deprecation")
	private String addToTheFuture10Min(Date currentDate){
		int timeHours =currentDate.getHours(); 
		int timeMinutes = currentDate.getMinutes()+10;
		if(timeMinutes+10>=60){
			int numberOfHoursPast = timeMinutes/60;
			timeMinutes = timeMinutes%60;
			timeHours+=numberOfHoursPast;
		}
		return timeHours+":"+timeMinutes;
	}
	private Date getDayRequired(int i,int thisOrNextWeek){
		Calendar c = Calendar.getInstance();
		switch(i){
		case 1:
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			break;
		case 2:
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
			break;
		case 3:
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
			break;
		case 4:
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
			break;
		case 5:
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			break;
		case 6:
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			break;
		case 7:
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			break;
		default:
				
		}
		if(thisOrNextWeek==1){
			c.add(Calendar.DAY_OF_YEAR, 7);	
		}
		return c.getTime();
	}
}
