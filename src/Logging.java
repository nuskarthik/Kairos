import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * 
 * @author A0091523L
 *
 */

public class Logging {

	static FileHandler fileTxt;
	static SimpleFormatter formatter;
	public static Logger self;
	
	public static Logger getInstance(){
		if (self == null){
			self = Logger.getLogger("Kairos");
			try {
				fileTxt = new FileHandler("KairosLog.txt",true);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			formatter = new SimpleFormatter();  
			self.setLevel(Level.ALL);
	        fileTxt.setFormatter(formatter);
			self.addHandler(fileTxt);
		}
		return self;
	}

}
