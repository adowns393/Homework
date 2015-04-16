//Class used to create message windows for user error and success notifications.
package JDBCAssignment2;

import javax.swing.JOptionPane;

public class MessageWindow {

	public MessageWindow(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}