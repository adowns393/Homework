//Andrew Downs
//March 29, 2015

// This is the main class for the application and handles
// creating the application window, its contents, and the action handlers.

package JDBCAssignment1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.JLabel;

import oracle.jdbc.*;


public class SQLwindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	
	private Connection con = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private JTable tblResults = null;
	JTextArea txtResults = null;
	private boolean result;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 
		
		try
			        {
			            Class.forName("oracle.jdbc.OracleDriver");
			        }
			        catch (ClassNotFoundException e)
			        {
			            e.printStackTrace();
			            System.out.println("Couldn't register JDBC driver.");
			            System.out.println("Application Ending.");
			            System.exit(-1);
			        }
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SQLwindow frame = new SQLwindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SQLwindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Create user/pass text fields
		txtUsername = new JTextField();
		txtUsername.setToolTipText("");
		txtUsername.setBounds(81, 102, 86, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(81, 153, 86, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(81, 82, 63, 14);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(81, 133, 63, 14);
		contentPane.add(lblPassword);
		//End create user/pass text fields
		
		
		
		//Create Buttons
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setEnabled(false);
		btnDisconnect.setBounds(70, 263, 109, 23);
		contentPane.add(btnDisconnect);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(81, 210, 89, 23);
		contentPane.add(btnConnect);
		
		JButton btnExecute = new JButton("Execute");
		btnExecute.setBounds(281, 181, 89, 23);
		btnExecute.setEnabled(false);
		contentPane.add(btnExecute);
		
		JButton btnClearStatements = new JButton("Clear");
		btnClearStatements.setBounds(427, 181, 89, 23);
		btnClearStatements.setEnabled(false);
		contentPane.add(btnClearStatements);
		
		JButton btnClearResults = new JButton("Clear");
		btnClearResults.setBounds(355, 368, 89, 23);
		btnClearResults.setEnabled(false);
		contentPane.add(btnClearResults);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(78, 323, 89, 23);
		contentPane.add(btnExit);
		//End Create Buttons
		
		
		
		//Create SQL text areas
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(230, 11, 357, 154);
		contentPane.add(scrollPane);
		
		JTextArea txtrEnterSqlStatements = new JTextArea();
		txtrEnterSqlStatements.setText("Enter SQL statements here...");
		txtrEnterSqlStatements.setEditable(false);
		scrollPane.setViewportView(txtrEnterSqlStatements);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(230, 215, 351, 142);
		contentPane.add(scrollPane_1);
		
		
		//End Create SQL text areas
		
		
		
		//Button action handlers
		
		//The disconnect button will close the connection to the database.
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				    {
				        if (rs != null)
				            rs.close ();
				        if (stmt != null)
				            stmt.close ();
				        if (con != null)
				            con.close ();
				        
				        if(tblResults != null)
				        {
				        	tblResults = null;
				        	scrollPane_1.setViewport(null);
				        	scrollPane_1.setColumnHeader(null);
				        }
				        
				        if(txtResults != null)
				        {
				        	txtResults = null;
				        	scrollPane_1.setViewport(null);
				        	scrollPane_1.setColumnHeader(null);
				        }
				        
				        btnConnect.setEnabled(true);
				        btnDisconnect.setEnabled(false);
				        btnExecute.setEnabled(false);
				        btnClearStatements.setEnabled(false);
				        btnClearResults.setEnabled(false);
				        txtrEnterSqlStatements.setEditable(false);
				        txtrEnterSqlStatements.setText("Enter SQL statements here...");
				        txtUsername.setEditable(true);
				        txtPassword.setEditable(true);
				        
				        MessageWindow confirmation = new MessageWindow("Successesfully disconnected from the database.", "Connection Status");
				    }
				    catch (SQLException e1)
				    {
				        System.out.println("Exception closing JDBC resources.");
				        e1.printStackTrace();
				        System.exit(-1);
				    } 
			}
		});
		
		//The connect button will establish a connection to the database.
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String dbURL = "jdbc:oracle:thin:@localhost:1522:bikerace";
					      try
					      {
					          con = DriverManager.getConnection(dbURL, txtUsername.getText(), txtPassword.getText());
					          btnDisconnect.setEnabled(true);
					          btnConnect.setEnabled(false);
					          btnExecute.setEnabled(true);
					          btnClearStatements.setEnabled(true);
					          btnClearResults.setEnabled(true);
					          txtrEnterSqlStatements.setEditable(true);
					          
					          txtUsername.setText("");
					          txtPassword.setText("");
					          txtUsername.setEditable(false);
					          txtPassword.setEditable(false);
					          
					          MessageWindow confirmation = new MessageWindow("Successesfully connected to the database.", "Connection Status");
					      }
					      catch (SQLException e1)
					      {
					          MessageWindow failure = new MessageWindow("Could not connect to the database.", "Connection Status");
					          e1.printStackTrace();
					      }
			}
		});
		
		//The execute button will execute any sql statements typed in by the user.
		btnExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					stmt = con.createStatement();
					result = stmt.execute(txtrEnterSqlStatements.getText());
					
					if (result)
					{
						rs = stmt.getResultSet();
						DBTableModel dbTableModel = new DBTableModel(rs);
						tblResults = new JTable(dbTableModel);
						scrollPane_1.setViewportView(tblResults);
					}
					else
					{
						int rsUpdateCount = stmt.getUpdateCount();
						
						if(rsUpdateCount != -1)
						{
							txtResults = new JTextArea();
							scrollPane_1.setViewportView(txtResults);
							txtResults.append("Number of rows updated: " + rsUpdateCount);
						}
					}
					
				}
				catch(Exception ex)
				{
					MessageWindow failure = new MessageWindow("Could not process your request.", "Execution Error");
					ex.printStackTrace();
				}
			}
		});
		
		//Clears the txtarea where the user enters in sql statements
		btnClearStatements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtrEnterSqlStatements.setText("Enter SQL statements here...");
			}
		});
		
		//Clears any results within scrollPane_1
		btnClearResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tblResults != null)
		        {
		        	tblResults = null;
		        	scrollPane_1.setViewport(null);
		        	scrollPane_1.setColumnHeader(null);
		        }
		        
		        if(txtResults != null)
		        {
		        	txtResults = null;
		        	scrollPane_1.setViewport(null);
		        	scrollPane_1.setColumnHeader(null);
		        }
			}
		});
		
		//Exits the application
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		//End button action handlers
	}
}
