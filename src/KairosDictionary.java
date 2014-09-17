import java.util.HashMap;
import java.util.Map;

public class KairosDictionary {
	
	public enum CommandType {
		ADD, DELETE, EDIT ,VIEW ,UNDO, REDO, DONE, UNDONE, EXIT, INVALID, KAIROS
	}

	static Map<String,CommandType> dictionary;
	
	private static CommandType commandType;

	/**
	 * Creating a dictionary for Kairos
	 * 
	 * @author A0091523L
	 */
	public KairosDictionary() {
			dictionary = new HashMap<String, CommandType>();
			dictionary.put("add", CommandType.ADD);
			dictionary.put("create", CommandType.ADD);
			dictionary.put("new", CommandType.ADD);
			dictionary.put("+", CommandType.ADD);
			
			dictionary.put("delete", CommandType.DELETE);
			dictionary.put("remove", CommandType.DELETE);
			dictionary.put("-", CommandType.DELETE);
			
			dictionary.put("edit", CommandType.EDIT);
			dictionary.put("update", CommandType.EDIT);
			dictionary.put("change", CommandType.EDIT);
			dictionary.put("fix", CommandType.EDIT);
			dictionary.put("postpone", CommandType.EDIT);
			
			dictionary.put("search", CommandType.VIEW);
			dictionary.put("view", CommandType.VIEW);
			dictionary.put("find", CommandType.VIEW);
			dictionary.put("display", CommandType.VIEW);
			dictionary.put("show", CommandType.VIEW);
			dictionary.put("see", CommandType.VIEW);
			
			dictionary.put("undo", CommandType.UNDO);
			
			dictionary.put("redo", CommandType.REDO);
			
			dictionary.put("exit", CommandType.EXIT);
			dictionary.put("leave", CommandType.EXIT);
			dictionary.put("quit", CommandType.EXIT);
		
			dictionary.put("done", CommandType.DONE);
			dictionary.put("finished", CommandType.DONE);
			dictionary.put("completed", CommandType.DONE);
			
			dictionary.put("undone", CommandType.UNDONE);
			dictionary.put("incomplete", CommandType.UNDONE);
			dictionary.put("unfinished", CommandType.UNDONE);
			
			dictionary.put("kairos", CommandType.KAIROS);
	}
	
	/**
	 * Function to get the dictionary equivalent word for input string
	 * @param String
	 * @return CommandType
	 * 
	 * @author A0091523L
	 */
	public static CommandType getWord(String word){
		return dictionary.get(word);
	}
	
	
	/**
	 * Function to return whether the entered user command requires the use
	 * of autocomplete i.e., is either edit, delete, done or undone type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isAutocompleteable(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case EDIT:
		case DELETE:
		case DONE:
		case UNDONE:
			return true;
		default:
			break;
		}
		return false;
	}

	
	/**
	 * Function to return whether the entered user command is of Edit type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isEdit(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case EDIT:
			return true;
		default:
			break;
		}
		return false;
	}

	
	/**
	 * Function to return whether the entered user command is of Add type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isAdd(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case ADD:
			return true;
		default:
			break;
		}
		return false;
	}

	
	/**
	 * Function to return whether the entered user command is of Delete type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isDelete(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case DELETE:
			return true;
		default:
			break;
		}
		return false;
	}

	
	/**
	 * Function to return whether the entered user command is of View type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isView(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case VIEW:
			return true;
		default:
			break;
		}
		return false;
	}
	
	
	/**
	 * Function to return whether the entered user command is of Undo or Redo type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isUndoOrRedo(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case UNDO:
			return true;
		case REDO:
			return true;
		default:
			break;
		}
		return false;
	}
	
	
	/**
	 * Function to return whether the entered user command is of Done type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isDone(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case DONE:
			return true;
		default:
			break;
		}
		return false;
	}

	
	/**
	 * Function to return whether the entered user command is of Undone type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isUndone(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case UNDONE:
			return true;
		default:
			break;
		}
		return false;
	}
	
	
	/**
	 * Function to return whether the entered user command is of Kairos type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isKairos(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case KAIROS:
			return true;
		default:
			break;
		}
		return false;
	}
	
	/**
	 * Function to return whether the entered user command is of Kairos type
	 * @param User Command String
	 * @return Boolean
	 * 
	 * @author A0091545A
	 */
	public static boolean isExit(String command) {
		assert command != null;
		commandType = Logic.determineCommandType(command);
		switch (commandType) {
		case EXIT:
			return true;
		default:
			break;
		}
		return false;
	}

}
