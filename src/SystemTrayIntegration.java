import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.melloware.jintellitype.JIntellitypeException;

/**
 * 
 * @author A0091523L
 * 
 */

public class SystemTrayIntegration {

	private static boolean isOpen;
	private static TrayIcon trayIcon;
	private static SystemTray tray;
	private static JFrame kairosFrame;

	SystemTrayIntegration(JFrame frameForKairos) {
		kairosFrame = frameForKairos;
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

	public void createSystemTray() {

		isOpen = true;

		try {
			JIntellitype.getInstance();
		} catch (JIntellitypeException e) {
			JOptionPane.showMessageDialog(kairosFrame,
					"Kairos is already running.");
			System.exit(1);
		}

		HotkeyListener hotKey = new HotkeyListener() {

			public void onHotKey(int Identifier) {
				if (Identifier == 1) {
					if (isOpen == true) {
						kairosFrame.setVisible(false);
						trayIcon.displayMessage("Minimized",
								"Kairos is running in the background",
								TrayIcon.MessageType.INFO);
						isOpen = false;
					} else {
						kairosFrame.setVisible(true);
						isOpen = true;
					}
				}

			}
		};

		createShortcut(hotKey);
		ImageIcon imageIcon = createImageIcon("/images/k.gif", "Kairos Logo");
		Image image = imageIcon.getImage();
		PopupMenu popup = createMenu();
		instantiateTrayAction(image, popup);
	}

	private void instantiateTrayAction(Image image, PopupMenu popup) {
		trayIcon = new TrayIcon(image, "Kairos", popup);

		if (SystemTray.isSupported()) {
			tray = SystemTray.getSystemTray();

			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// System.out.println("In here");
					trayIcon.displayMessage("Kairos", "Kairos is running!",
							TrayIcon.MessageType.INFO);
				}
			});

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added.");
			}
		}
	}

	private PopupMenu createMenu() {
		ActionListener exitListener = exitMenu();
		ActionListener hideListener = hideMenu();
		PopupMenu popup = new PopupMenu();
		MenuItem defaultItem = new MenuItem("Exit");
		defaultItem.addActionListener(exitListener);
		popup.add(defaultItem);
		defaultItem = new MenuItem("Open");
		addMenu(defaultItem);
		popup.add(defaultItem);
		defaultItem = new MenuItem("Hide");
		defaultItem.addActionListener(hideListener);
		popup.add(defaultItem);
		return popup;
	}

	private void addMenu(MenuItem defaultItem) {
		defaultItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kairosFrame.setVisible(true);
				kairosFrame.setExtendedState(JFrame.NORMAL);
			}
		});
	}

	private ActionListener hideMenu() {
		ActionListener hideListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kairosFrame.setVisible(false);
			}
		};
		return hideListener;
	}

	private ActionListener exitMenu() {
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane pane = new JOptionPane(
						"Are you sure you want to exit Kairos?\n");
				Object[] options = new String[] { "Yes", "No" };
				pane.setOptions(options);
				JDialog dialog = pane.createDialog(new JFrame(),
						"Leaving Kairos");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
				Object obj = pane.getValue();
				if (options[0].equals(obj)) {
					JIntellitype.getInstance().cleanUp();
					tray.remove(trayIcon);
					System.exit(0);
				} else
					dialog.setVisible(false);
			}
		};
		return exitListener;
	}

	private void createShortcut(HotkeyListener hotKey) {
		JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_WIN,
				(int) 'A');
		JIntellitype.getInstance().addHotKeyListener(hotKey);
	}

}
