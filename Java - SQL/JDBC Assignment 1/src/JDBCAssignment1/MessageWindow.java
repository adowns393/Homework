//This class handles creating JOptionPane objects when needed.
//The JOptionPanes take in the message to be displayed and the title for the message window.

package JDBCAssignment1;

import javax.swing.JOptionPane;

public class MessageWindow {

	public MessageWindow(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
