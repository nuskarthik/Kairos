import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.Vector;

/**
 * 
 * @author Hari Krishna Vetharenian A0088635R
 * 
 */
public class KairosGUI extends JPanel {
	private static final long serialVersionUID = 1L;
	// Define GUI Variables
	private static JComboBox<String> patternList;
	private static JFrame kairosFrame;
	private static JLabel commandLabel;
	private static JList<String> viewList;
	private static DefaultListModel<String> viewListModel;
	private static JLabel viewLabel;
	private static JScrollPane listScrollPane;
	// Define Variables
	private static String[] autoCompleteString;
	private static Controller controllerObject;
	private static String userStringOrig;
	private static Vector<String> autoCompleteResults;
	private static int noOfEnters;
	private static Vector<Integer> doneTaskId;
	private static Vector<Integer> highPriorityTaskId;
	private static Vector<Integer> lowPriorityTaskId;
	private static int viewHighPriorityID;
	private static int viewLowPriorityID;
	private static boolean isViewDoneTasks;
	// Here I'll define some constants
	private static final int ZERO_ENTERS_ENTERED = 0;
	private static final int ONE_ENTER_ENTERED = 1;
	private static final int HIGH_PRIORITY_TASK_VECTOR_INDEX = 0;
	private static final int LOW_PRIORITY_TASK_VECTOR_INDEX = 1;
	private static final int EMPTY_STRING_LENGTH = 0;
	private static final int BACKSPACE_KEY_CODE = 8;
	private static final int USER_COMMAND_INDEX = 0;
	private static final int USER_COMMAND_DESCRIPTION_INDEX = 1;
	private static final int EMPTY_VECTOR_LENGTH = 0;
	private static final String EMPTY_STRING = "";
	private static final int FIRST_ARRAY_INDEX = 0;
	private static final int NO_SELECTION_INDEX = -1;
	private static final String SPACE_STRING = " ";
	private static final int NO_OF_SPLITS = 2;
	private static final int ADD_COMMAND_MESSAGE = 0;
	private static final int EDIT_COMMAND_MESSAGE = 1;
	private static final int DELETE_COMMAND_MESSAGE = 2;
	private static final int DONEUNDONE_COMMAND_MESSAGE = 3;
	private static final int KAIROS_COMMAND_MESSAGE = 4;
	private static final int VIEW_COMMAND_MESSAGE = 5;
	private static final int UNDOREDOEXIT_COMMAND_MESSAGE = 6;
	private static final int DEFAULT_COMMAND_MESSAGE = 7;
	private static final String[] commandSyntaxes = new String[] {
			"<html>%s attend ABC conference on 29 Oct 2012 10am</html>",
			"<html><ol> %s <li> Type what you to edit </li> <li> Choose from one of the suggestions by pressing ENTER</li> <li> Edit task and press ENTER </li></ol></html>",
			"<html><ol> %s <li> Type what you to delete</li> <li> Choose what you want to delete from one of the suggestions by pressing ENTER</li></ol></html>",
			"<html><ol> %s <li> Type what you to mark done/undone</li> <li> Choose what you want to mark done/undone from one of the suggestions by pressing ENTER</li></ol></html>",
			"<html>%s Marks all tasks today as done</html>",
			"<html>%s [done]/[date]/[all]/[search string] </html>",
			"<html>%s</html>",
			"<html><p>Commands: ADD EDIT DELETE VIEW <center>KAIROS DONE UNDO REDO EXIT</p></html>" };

	public KairosGUI() {
		super(new GridBagLayout());
		// Initialize variables
		isViewDoneTasks = false;
		controllerObject = new Controller();
		userStringOrig = "";
		Font textFont = new Font("Verdana", Font.PLAIN, 15);
		Font displayFont = new Font("Verdana", Font.BOLD, 12);
		noOfEnters = ZERO_ENTERS_ENTERED;
		// Initialize viewLabel
		viewLabel = new JLabel();
		viewLabel.setPreferredSize(new Dimension(600, 30));
		viewLabel.setFont(displayFont);
		viewLabel.setHorizontalAlignment(JLabel.CENTER);
		viewLabel.setVerticalAlignment(JLabel.CENTER);
		viewLabel.setText("Kairos up and running!");
		// Set Layout style for viewLabel
		GridBagConstraints gbc_viewLabel = new GridBagConstraints();
		gbc_viewLabel.fill = GridBagConstraints.BOTH;
		gbc_viewLabel.gridx = 0;
		gbc_viewLabel.anchor = GridBagConstraints.CENTER;
		add(viewLabel, gbc_viewLabel);
		// Initialize and set properties of PatternList
		patternList = new JComboBox<String>();
		patternList.setEditable(true);
		patternList.setFocusable(true);
		patternList.getEditor().getEditorComponent()
				.addKeyListener(new AutoCompleteKeyListener());
		patternList.setFont(textFont);
		ComboBoxRenderer plRenderer = new ComboBoxRenderer();
		patternList.setRenderer(plRenderer);
		removeCommboBoxButtons(patternList);
		patternList.setPreferredSize(new Dimension(600, 30));
		// Set Layout style for patternList
		GridBagConstraints gbc_patternList = new GridBagConstraints();
		gbc_patternList.insets = new Insets(0, 20, 0, 0);
		gbc_patternList.fill = GridBagConstraints.BOTH;
		gbc_patternList.gridy = 1;
		gbc_patternList.gridx = 0;
		add(patternList, gbc_patternList);
		// Initialize and set properties for CommandLabel
		commandLabel = new JLabel();
		commandLabel.setFont(displayFont);
		commandLabel.setHorizontalAlignment(JLabel.CENTER);
		commandLabel.setPreferredSize(new Dimension(600, 80));
		// Set Layout style for CommandLabel
		GridBagConstraints gbc_commandLabel = new GridBagConstraints();
		gbc_commandLabel.fill = GridBagConstraints.BOTH;
		gbc_commandLabel.gridy = 2;
		gbc_commandLabel.gridx = 0;
		commandLabel.setText(commandSyntaxes[DEFAULT_COMMAND_MESSAGE]);
		add(commandLabel, gbc_commandLabel);
		// initialize viewList
		viewListModel = new DefaultListModel<String>();
		viewList = new JList<String>(viewListModel);
		ListRenderer listRenderer = new ListRenderer();
		viewList.setCellRenderer(listRenderer);
		listScrollPane = new JScrollPane(viewList);
		// Set Layout style for viewList
		GridBagConstraints gbc_viewList = new GridBagConstraints();
		gbc_viewList.gridwidth = GridBagConstraints.REMAINDER;
		gbc_viewList.fill = GridBagConstraints.BOTH;
		gbc_viewList.weighty = 1.0;
		gbc_viewList.weightx = 1.0;
		gbc_viewList.gridy = 3;
		gbc_viewList.gridx = 0;
		add(listScrollPane, gbc_viewList);
		listScrollPane.setVisible(false);
		listScrollPane.revalidate();
		listScrollPane.repaint();
		// System Tray integration
		SystemTrayIntegration systemTray = new SystemTrayIntegration(
				kairosFrame);
		systemTray.createSystemTray();

		// assert GUI components
		assert kairosFrame != null;
		assert patternList != null;
		assert viewLabel != null;
		assert commandLabel != null;
		assert viewList != null;
	}

	class ComboBoxRenderer extends JLabel implements ListCellRenderer<String> {

		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(
				JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setFont(new Font("Verdana", Font.BOLD, 12));
			setText(value);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			try {
				setComboBoxForegroundColour(index);
			} catch (NullPointerException e) {
			}
			return this;
		}

		/**
		 * Color Codes ComboBox List Item fonts RED - High priority tasks ORANGE
		 * - Low priority tasks GREEN - Done Tasks BLACK - Other Tasks
		 * 
		 * @param int index of the item whose color is to be changed
		 * 
		 */
		private void setComboBoxForegroundColour(int index) {
			if (doneTaskId.contains(index)) {
				Color customGreen = new Color(0, 150, 0);
				setForeground(customGreen);
			}
			if (highPriorityTaskId.contains(index)) {
				setForeground(Color.RED);
			}
			if (lowPriorityTaskId.contains(index)) {
				Color customOrange = new Color(204, 102, 0);
				setForeground(customOrange);
			}
		}
	}

	class ListRenderer extends JLabel implements ListCellRenderer<String> {

		private static final long serialVersionUID = 1L;

		public ListRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(
				JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setFont(new Font("Verdana", Font.BOLD, 12));
			setText(value);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			try {
				setListCellBackgroundColour(index);
			} catch (NullPointerException e) {
				System.out.println("Null pointer exception!");
			}
			return this;
		}

		/**
		 * Colors codes List Cell item backgrounds RED - High priority tasks
		 * ORANGE - Low priority tasks GREEN - Done Tasks BLACK - Other Tasks
		 * 
		 * @param int index of the item whose color to be changed
		 */
		private void setListCellBackgroundColour(int index) {
			if (isViewDoneTasks) {
				setBackground(Color.GREEN);
			} else {
				Color customRed = new Color(255, 70, 70);
				setBackground(customRed);
				if (index >= viewHighPriorityID) {
					setBackground(Color.ORANGE);
				}
				if (index >= viewLowPriorityID) {
					setBackground(Color.WHITE);
				}
			}
		}
	}

	/*
	 * Class for handling the KeyEvents generated on the ComboBox
	 */

	private class AutoCompleteKeyListener implements KeyListener {

		public void keyPressed(KeyEvent evt) {
			handleKeyPressedEvent(evt);
		}

		public void keyReleased(KeyEvent evt) {

		}

		public void keyTyped(KeyEvent evt) {
			handleKeyTypedEvent(evt);

		}

	}

	/**
	 * Removes the button associated with the ComboBox for better look and feel
	 * 
	 * @param JComboBox
	 *            <?> ComboBox whose button is to be removed.
	 */

	private void removeCommboBoxButtons(JComboBox<?> cbList) {
		Component buttonToBeHidden = null;
		for (Component cbChild : cbList.getComponents()) {
			if (cbChild instanceof JButton) {
				buttonToBeHidden = cbChild;
			}
		}
		if (buttonToBeHidden != null) {
			buttonToBeHidden.setVisible(false);
		}
	}

	private static void handleKeyTypedEvent(KeyEvent evt) {
		JTextField evtSource = (JTextField) evt.getSource();
		assert evtSource != null;
		userStringOrig = evtSource.getText();
		if (userStringOrig.length() == EMPTY_STRING_LENGTH) {
			noOfEnters = ZERO_ENTERS_ENTERED;
		}
		String userString = userStringOrig;
		if ((int) evt.getKeyChar() != BACKSPACE_KEY_CODE) {
			userString = userStringOrig + evt.getKeyChar();
		}
		if (isUserStringEntered(userString)) {
			String userCommandEntered[] = userString.toLowerCase().split(
					SPACE_STRING, NO_OF_SPLITS);
			setCommandLabel(userCommandEntered[USER_COMMAND_INDEX]);
		}
		if (noOfEnters == ZERO_ENTERS_ENTERED) {
			if (!isMultipleSpaceEntered(userString)) {
				autoCompleteString = userString.toLowerCase().split(
						SPACE_STRING, 2);
				if (isReadyForAutoCompletion()) {
					handleAutoCompletion(evtSource, userString);
				}
			} else {
				patternList.removeAllItems();
				evtSource.setText(userStringOrig);
				patternList.hidePopup();
			}

		}

	}

	private static void handleKeyPressedEvent(KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
			handleEscape(evt);
		}
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			handleEnter(evt);
		} else {
			viewLabelHide();
			viewListHide();
			kairosFrame.revalidate();
			kairosFrame.repaint();
		}
	}

	/**
	 * Checks if user entered string is not empty.
	 * 
	 * @param String
	 *            string entered by user
	 * 
	 * @return boolean true if userString is not empty.
	 */
	private static boolean isUserStringEntered(String userString) {
		String[] checkMultipleSpacing = userString.toLowerCase().split(
				SPACE_STRING);
		if (checkMultipleSpacing.length > EMPTY_STRING_LENGTH)
			return true;
		return false;
	}

	/**
	 * Checks for multiple spaces entered by the user
	 * 
	 * @param string
	 *            entered by user
	 * 
	 * @return boolean true if user entered multiple spaces only.
	 */

	private static boolean isMultipleSpaceEntered(String userString) {
		String[] checkMultipleSpacing = userString.toLowerCase().split(
				SPACE_STRING);
		if (checkMultipleSpacing.length > 1)
			return false;
		return true;
	}

	/**
	 * Checks if user entered auto completable command
	 * 
	 * @return boolean true if usercommand is capable of autocompletion
	 */
	private static boolean isReadyForAutoCompletion() {
		boolean isAutocompleteable = controllerObject
				.isAutocompleteable(autoCompleteString[USER_COMMAND_INDEX]);
		boolean isNotEmptyString = (autoCompleteString[USER_COMMAND_DESCRIPTION_INDEX]
				.length() > EMPTY_STRING_LENGTH);
		return isAutocompleteable && isNotEmptyString;
	}

	/**
	 * Handles Auto completion on the given textfield
	 * 
	 * @param JTextField
	 *            , String
	 */

	private static void handleAutoCompletion(JTextField evtSource,
			String userString) {
		patternList.removeAllItems();
		// pass autoCompleteString[1] to controller.
		boolean isResultsReturned = controllerObject
				.getAutocompleteSearchResults(userString);
		if (isResultsReturned) {
			autoCompleteResults = controllerObject
					.getAutocompleteSearchString();
			doneTaskId = controllerObject.getAutocompleteSearchDoneID();
			highPriorityTaskId = controllerObject
					.getAutocompleteSearchHighPriorityID();
			lowPriorityTaskId = controllerObject
					.getAutocompleteSearchLowPriorityID();
			assert autoCompleteResults != null;
			int iterator = FIRST_ARRAY_INDEX;
			while (iterator < autoCompleteResults.size()) {
				patternList.addItem(autoCompleteString[USER_COMMAND_INDEX]
						+ " " + autoCompleteResults.get(iterator));
				iterator++;
			}
			evtSource.setText(userStringOrig);
			patternList.setMaximumRowCount(iterator);
			patternList.showPopup();
		} else {
			doneTaskId = new Vector<Integer>(EMPTY_VECTOR_LENGTH);
			highPriorityTaskId = new Vector<Integer>(EMPTY_VECTOR_LENGTH);
			lowPriorityTaskId = new Vector<Integer>(EMPTY_VECTOR_LENGTH);
			patternList.addItem("No Results found. Enter a different String!");
			patternList.setMaximumRowCount(1);
			patternList.showPopup();
			evtSource.setText(userStringOrig);
		}
	}

	private static void handleEnter(KeyEvent evt) {
		doneTaskId = new Vector<Integer>(EMPTY_VECTOR_LENGTH);
		patternList.hidePopup();
		JTextField evtSource = (JTextField) evt.getSource();
		JComboBox<?> cbSource = (JComboBox<?>) evtSource.getParent();
		assert evtSource != null;
		assert cbSource != null;
		String userCommand = evtSource.getText();
		assert userCommand != null;
		userStringOrig = EMPTY_STRING;
		String[] userCommandString = userCommand.toLowerCase().split(" ");
		if (controllerObject.isEdit(userCommandString[USER_COMMAND_INDEX])) {
			handleEditCommandEnter(evtSource, cbSource, userCommand);
		} else {
			handleOtherCommandEnter(cbSource, userCommand);
		}
	}

	private static void handleEscape(KeyEvent evt) {
		if (noOfEnters == ONE_ENTER_ENTERED) {
			JTextField evtSource = (JTextField) evt.getSource();
			evtSource.setText("");
			noOfEnters = ZERO_ENTERS_ENTERED;
		}
	}

	private static void handleEditCommandEnter(JTextField evtSource,
			JComboBox<?> cbSource, String userCommand) {
		if (noOfEnters == ZERO_ENTERS_ENTERED) {
			// pass cbSource.getSelectedIndex();
			int selectedIndex = cbSource.getSelectedIndex();
			assert selectedIndex != NO_SELECTION_INDEX;
			evtSource.setText((String) cbSource.getItemAt(selectedIndex));
			passUserSelectedIndex(selectedIndex);
			noOfEnters++;
		} else if (noOfEnters == ONE_ENTER_ENTERED) {
			// pass userCommand
			try {
				passUserCommand(userCommand);
			} catch (Exception e) {
				e.printStackTrace();
			}

			patternList.removeAllItems();
			noOfEnters = ZERO_ENTERS_ENTERED;
		}
	}

	private static void handleOtherCommandEnter(JComboBox<?> cbSource,
			String userCommand) {
		noOfEnters = ZERO_ENTERS_ENTERED;
		// pass to Controller
		isViewDoneTasks = controllerObject.isViewDone(userCommand);
		try {
			int selectedIndex = cbSource.getSelectedIndex();
			if (selectedIndex != NO_SELECTION_INDEX) {
				passUserSelectedIndex(selectedIndex);
				userCommand = (String) cbSource.getItemAt(selectedIndex);
			}
			passUserCommand(userCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		patternList.removeAllItems();
	}

	private static void passUserSelectedIndex(int selectedIndex) {
		controllerObject.setAutocompleteTaskId(selectedIndex);
	}

	private static void passUserCommand(String userCommand) throws Exception {
		boolean isOperationSuccessful = controllerObject
				.setUserCommand(userCommand);
		setViewLabelFontColor(isOperationSuccessful);
	}

	/**
	 * Sets the contents of the Help Label
	 * 
	 * @param String
	 *            the text to be set in the Label
	 */

	private static void setCommandLabel(String userCommand) {
		try {
			if (userCommand.length() == EMPTY_STRING_LENGTH) {
				commandLabel.setText(String.format(
						commandSyntaxes[DEFAULT_COMMAND_MESSAGE], userCommand));
			}
			if (controllerObject.isAdd(userCommand)) {
				commandLabel.setText(String.format(
						commandSyntaxes[ADD_COMMAND_MESSAGE], userCommand));
			}
			if (controllerObject.isEdit(userCommand)) {
				commandLabel.setText(String.format(
						commandSyntaxes[EDIT_COMMAND_MESSAGE], userCommand));
			}
			if (controllerObject.isDelete(userCommand)) {
				commandLabel.setText(String.format(
						commandSyntaxes[DELETE_COMMAND_MESSAGE], userCommand));
			}
			if (controllerObject.isDone(userCommand)
					|| controllerObject.isUndone(userCommand)) {
				commandLabel.setText(String.format(
						commandSyntaxes[DONEUNDONE_COMMAND_MESSAGE],
						userCommand));
			}
			if (controllerObject.isKairos(userCommand)) {
				commandLabel.setText(String.format(
						commandSyntaxes[KAIROS_COMMAND_MESSAGE], userCommand));
			}
			if (controllerObject.isView(userCommand)) {
				commandLabel.setText(String.format(
						commandSyntaxes[VIEW_COMMAND_MESSAGE], userCommand));
			}
			if (controllerObject.isUndoOrRedo(userCommand)
					|| controllerObject.isExit(userCommand)) {
				commandLabel.setText(String.format(
						commandSyntaxes[UNDOREDOEXIT_COMMAND_MESSAGE],
						userCommand));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets contents of the view List.
	 * 
	 * @param Vector
	 *            <Vector<String>> A 2D Vector containing tasks according to
	 *            priority where Index 0 - High Index 1 - Low Index 2 - Null
	 */

	public static void setViewList(Vector<Vector<String>> viewTasks)
			throws Exception {
		try {
			int iterator;
			setPriorityID(viewTasks);
			Vector<String> viewTaskListValues = new Vector<String>(
					EMPTY_VECTOR_LENGTH);
			for (iterator = FIRST_ARRAY_INDEX; iterator < viewTasks.size(); iterator++) {
				viewTaskListValues.addAll(viewTasks.get(iterator));
			}
			viewListModel.clear();
			if (viewTaskListValues.size() > EMPTY_VECTOR_LENGTH) {
				for (iterator = FIRST_ARRAY_INDEX; iterator < viewTaskListValues
						.size(); iterator++) {
					viewListModel.addElement(iterator + 1 + ". "
							+ viewTaskListValues.get(iterator));
				}
			} else {
				viewListModel
						.addElement("Sorry your search returned no tasks! Enter a different search string");
			}
			listScrollPane.setVisible(true);
			viewList.setVisible(true);
			kairosFrame.revalidate();
			kairosFrame.repaint();
		} catch (Exception e) {
			throw e;
		}
	}

	private static void setPriorityID(Vector<Vector<String>> viewTasks) {
		if (!isViewDoneTasks) {
			viewHighPriorityID = viewTasks.get(HIGH_PRIORITY_TASK_VECTOR_INDEX)
					.size();
			viewLowPriorityID = viewTasks.get(LOW_PRIORITY_TASK_VECTOR_INDEX)
					.size() + viewHighPriorityID;
		}
	}

	/**
	 * Sets the Font Color of the viewLabel based on status of task returned RED
	 * - Unsuccessful GREEN - Successful
	 * 
	 * @param boolean Status of the operation
	 */

	private static void setViewLabelFontColor(boolean isOperationSuccesful) {
		if (isOperationSuccesful) {
			Color customGreen = new Color(0, 150, 0);
			viewLabel.setForeground(customGreen);
		} else {
			viewLabel.setForeground(Color.RED);
		}
	}

	/**
	 * Function used to set value of the viewLabel
	 * 
	 * @param String
	 *            Text to be set in the label
	 */

	public static void displayText(String text) {
		viewLabel.setVisible(true);
		viewLabel.setText(text);
	}

	private static void viewLabelHide() {
		viewLabel.setVisible(false);
	}

	private static void viewListHide() {
		listScrollPane.setVisible(false);
		viewList.setVisible(false);
		kairosFrame.revalidate();
		kairosFrame.repaint();
	}

	/**
	 * Exits the application in safe way.
	 */

	public static void closeGUI() throws Exception {
		try {
			kairosFrame.setVisible(false);
			kairosFrame.dispose();
		} catch (Exception e) {
			throw new Exception("GUI could not be closed!");
		}
	}

	private static ImageIcon createImageIcon(String path, String description) {
		URL imgURL = path.getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Initializes the GUI Frame
	 */

	private static void showGUI() {
		kairosFrame = new JFrame("Kairos");
		assert kairosFrame != null;
		kairosFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		kairosFrame.setAlwaysOnTop(false);
		ImageIcon frameIcon = createImageIcon("/images/k.gif", "Kairos");
		kairosFrame.setIconImage(frameIcon.getImage());
		kairosFrame.setResizable(false);
		kairosFrame.getContentPane().add(new KairosGUI());
		kairosFrame.pack();
		kairosFrame.setVisible(true);
		kairosFrame.setLocationRelativeTo(null);
		patternList.getEditor().getEditorComponent().requestFocus();
	}

	/**
	 * Creates the GUI Thread
	 */

	public static void invokeGUI() throws Exception {
		try {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					showGUI();
				}
			});
		} catch (Exception e) {
			throw new Exception("GUI cannot be instantiated!");
		}
	}
}