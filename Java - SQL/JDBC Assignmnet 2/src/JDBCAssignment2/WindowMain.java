/*
 * Andrew Downs
 * April 12, 2015
 * CEN 4333
 * Chapter 14 JDBC Assignment 2
 */
package JDBCAssignment2;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTabbedPane;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.NumberFormat;


public class WindowMain extends JFrame {

	private JPanel contentPane;
	private JTextField team_txtBikeName;
	private JTextField team_txtRegNation;
	private JTextField team_txtNumRiders;
	private JTextField team_txtManager;
	private JTextField rider_txtNumProWins;
	private JTextField rider_txtNationality;
	private JTextField rider_txtTeamName;
	private JTextField bike_txtCountryOrigin;
	private JTextField bike_txtBikeCost;
	
	private static Connection con = null;
	private ResultSet rs = null;
	private Statement stmt = null;
	private static WindowMain frame = null;
	
	private final static String dbURL = new String("jdbc:oracle:thin:@localhost:1522:bikerace");
	private final static String username = new String("SYSTEM");
	private final static String password = new String("password");

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
		
	      try
	      {
	          con = DriverManager.getConnection(dbURL, username, password);
	       
	      }
	      catch (SQLException e1)
	      {
	    	  MessageWindow failure = new MessageWindow("Could not connect to the database. Check the dbURL, Username, and Password", "Connection Error");
	          e1.printStackTrace();
	      }
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new WindowMain();
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
	public WindowMain() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 570, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		
		
		//Set up bike tab
		JPanel bikeTab = new JPanel();
		bikeTab.setToolTipText("");
		tabbedPane.addTab("Bike", null, bikeTab, null);
		bikeTab.setLayout(null);
		
		JLabel lblBikeName_1 = new JLabel("Bike Name");
		lblBikeName_1.setBounds(49, 41, 129, 14);
		bikeTab.add(lblBikeName_1);
		
		JLabel lblCountryOfOrigin = new JLabel("Country of Origin");
		lblCountryOfOrigin.setBounds(49, 92, 129, 14);
		bikeTab.add(lblCountryOfOrigin);
		
		JLabel lblCost = new JLabel("Cost");
		lblCost.setBounds(49, 146, 129, 14);
		bikeTab.add(lblCost);
		
		bike_txtCountryOrigin = new JTextField();
		bike_txtCountryOrigin.setBounds(188, 86, 86, 20);
		bikeTab.add(bike_txtCountryOrigin);
		bike_txtCountryOrigin.setColumns(10);
		
		bike_txtBikeCost = new JTextField();
		bike_txtBikeCost.setBounds(188, 140, 60, 20);
		bikeTab.add(bike_txtBikeCost);
		bike_txtBikeCost.setColumns(10);
		
		JComboBox<String> bike_cmboBikeName = new JComboBox<String>();
		bike_cmboBikeName.setEditable(true);
		bike_cmboBikeName.setBounds(188, 35, 180, 20);
		bikeTab.add(bike_cmboBikeName);
		populateComboBox(bike_cmboBikeName, "SELECT bikename FROM RACEBIKES ORDER BY bikename");
		
		JButton bike_btnAdd = new JButton("Add");
		bike_btnAdd.setBounds(29, 252, 80, 23);
		bikeTab.add(bike_btnAdd);
		
		JButton bike_btnChange = new JButton("Change");
		bike_btnChange.setBounds(161, 252, 78, 23);
		bikeTab.add(bike_btnChange);
		
		JButton bike_btnDelete = new JButton("Delete");
		bike_btnDelete.setBounds(301, 252, 78, 23);
		bikeTab.add(bike_btnDelete);
		
		JButton bike_btnExit = new JButton("Exit");
		bike_btnExit.setBounds(436, 252, 78, 23);
		bikeTab.add(bike_btnExit);
		
		
		
		
		//Set up rider tab
		JPanel riderTab = new JPanel();
		tabbedPane.addTab("Rider", null, riderTab, null);
		riderTab.setLayout(null);
		
		JLabel lblRiderName = new JLabel("Rider Name");
		lblRiderName.setBounds(51, 35, 110, 14);
		riderTab.add(lblRiderName);
		
		JLabel lblTeamName = new JLabel("Team Name");
		lblTeamName.setBounds(51, 86, 110, 14);
		riderTab.add(lblTeamName);
		
		JLabel lblNationality = new JLabel("Nationality");
		lblNationality.setBounds(51, 140, 110, 14);
		riderTab.add(lblNationality);
		
		JLabel lblNumberOfPro = new JLabel("Number of Pro Wins");
		lblNumberOfPro.setBounds(51, 189, 125, 14);
		riderTab.add(lblNumberOfPro);
		
		rider_txtNumProWins = new JTextField();
		rider_txtNumProWins.setBounds(186, 183, 55, 20);
		riderTab.add(rider_txtNumProWins);
		rider_txtNumProWins.setColumns(10);
		
		rider_txtNationality = new JTextField();
		rider_txtNationality.setBounds(186, 134, 86, 20);
		riderTab.add(rider_txtNationality);
		rider_txtNationality.setColumns(10);
		
		rider_txtTeamName = new JTextField();
		rider_txtTeamName.setBounds(186, 80, 110, 20);
		riderTab.add(rider_txtTeamName);
		rider_txtTeamName.setColumns(10);
		
		JComboBox<String> rider_cmboRiderName = new JComboBox<String>();
		rider_cmboRiderName.setEditable(true);
		rider_cmboRiderName.setBounds(186, 29, 206, 20);
		riderTab.add(rider_cmboRiderName);
		populateComboBox(rider_cmboRiderName, "SELECT ridername FROM RACERIDERS ORDER BY ridername");
		
		JButton rider_btnAdd = new JButton("Add");
		rider_btnAdd.setBounds(30, 250, 80, 23);
		riderTab.add(rider_btnAdd);
		
		JButton rider_btnChange = new JButton("Change");
		rider_btnChange.setBounds(162, 250, 78, 23);
		riderTab.add(rider_btnChange);
		
		JButton rider_btnDelete = new JButton("Delete");
		rider_btnDelete.setBounds(302, 250, 78, 23);
		riderTab.add(rider_btnDelete);
		
		JButton rider_btnExit = new JButton("Exit");
		rider_btnExit.setBounds(437, 250, 78, 23);
		riderTab.add(rider_btnExit);
		
		
		
		//Set up team tab
		JPanel teamTab = new JPanel();
		tabbedPane.addTab("Team", null, teamTab, null);
		teamTab.setLayout(null);
		
		JLabel lblName = new JLabel("Team Name");
		lblName.setBounds(49, 28, 119, 14);
		teamTab.add(lblName);
		
		JLabel lblBikeName = new JLabel("Bike Name");
		lblBikeName.setBounds(49, 71, 119, 14);
		teamTab.add(lblBikeName);
		
		JLabel lblRegisteredNation = new JLabel("Registered Nation");
		lblRegisteredNation.setBounds(49, 115, 119, 14);
		teamTab.add(lblRegisteredNation);
		
		JLabel lblNumberOfRiders = new JLabel("Number of Riders");
		lblNumberOfRiders.setBounds(49, 161, 119, 14);
		teamTab.add(lblNumberOfRiders);
		
		JLabel lblManager = new JLabel("Manager");
		lblManager.setBounds(49, 209, 119, 14);
		teamTab.add(lblManager);
		
		JComboBox<String> team_cmboTeamName = new JComboBox<String>();
		team_cmboTeamName.setEditable(true);
		team_cmboTeamName.setBounds(191, 22, 232, 20);
		teamTab.add(team_cmboTeamName);
		populateComboBox(team_cmboTeamName, "SELECT teamname FROM RACETEAMS ORDER BY teamname");
		
		team_txtBikeName = new JTextField();
		team_txtBikeName.setBounds(191, 65, 100, 20);
		teamTab.add(team_txtBikeName);
		team_txtBikeName.setColumns(10);
		
		team_txtRegNation = new JTextField();
		team_txtRegNation.setBounds(191, 109, 100, 20);
		teamTab.add(team_txtRegNation);
		team_txtRegNation.setColumns(10);
		
		team_txtNumRiders = new JTextField();
		team_txtNumRiders.setBounds(191, 155, 49, 20);
		teamTab.add(team_txtNumRiders);
		team_txtNumRiders.setColumns(10);
		
		team_txtManager = new JTextField();
		team_txtManager.setBounds(191, 203, 119, 20);
		teamTab.add(team_txtManager);
		team_txtManager.setColumns(10);
		
		JButton team_btnAdd = new JButton("Add");
		team_btnAdd.setBounds(27, 264, 80, 23);
		teamTab.add(team_btnAdd);
		
		JButton team_btnChange = new JButton("Change");
		team_btnChange.setBounds(159, 264, 78, 23);
		teamTab.add(team_btnChange);
		
		JButton team_btnDelete = new JButton("Delete");
		team_btnDelete.setBounds(299, 264, 78, 23);
		teamTab.add(team_btnDelete);
		
		JButton team_btnExit = new JButton("Exit");
		team_btnExit.setBounds(434, 264, 78, 23);
		teamTab.add(team_btnExit);
		
		
		
		//Bike tab action handlers
		bike_btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(validateBikeInput((String) bike_cmboBikeName.getSelectedItem(), bike_txtCountryOrigin.getText(), bike_txtBikeCost))
				{
					String pInsertStatement = new String("INSERT INTO RACEBIKES VALUES(?, ?, ?)");
					PreparedStatement preparedInsert = null;
					
					try
					{
						preparedInsert = con.prepareStatement(pInsertStatement);
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getErrorCode());
						return;
					}
					
					try
					{
						preparedInsert.setString(1, (String) bike_cmboBikeName.getSelectedItem());
						preparedInsert.setString(2, bike_txtCountryOrigin.getText());
						
						if(bike_txtBikeCost.getText().length() != 0)
							preparedInsert.setInt(3, Integer.parseInt(bike_txtBikeCost.getText()));
						else
							preparedInsert.setNull(3, Types.NULL);
						
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.executeUpdate();
					}
					catch (SQLException ex)
					{
						MessageWindow failure = new MessageWindow("A bike with that name is already in the database.", "Input Error");
						return;
					}
					bike_cmboBikeName.addItem((String) bike_cmboBikeName.getSelectedItem());
					MessageWindow success = new MessageWindow("Successfully created new entry.", "Success");
					
				}
				
				
			}
		});
		
		bike_btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(validateBikeInput((String) bike_cmboBikeName.getSelectedItem(), bike_txtCountryOrigin.getText(), bike_txtBikeCost))
				{
					String pInsertStatement = new String("UPDATE RACEBIKES SET country_of_origin = ?, cost = ? WHERE bikename = ?");
					PreparedStatement preparedInsert = null;
					
					try
					{
						preparedInsert = con.prepareStatement(pInsertStatement);
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						System.out.println(ex.getErrorCode());
						return;
					}
					
					try
					{
						preparedInsert.setString(3, (String) bike_cmboBikeName.getSelectedItem());
						preparedInsert.setString(1, bike_txtCountryOrigin.getText());
						
						if(bike_txtBikeCost.getText().length() != 0)
							preparedInsert.setInt(2, Integer.parseInt(bike_txtBikeCost.getText()));
						else
							preparedInsert.setNull(2, Types.NULL);
						
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.executeUpdate();
					}
					catch (SQLException ex)
					{
						MessageWindow failure = new MessageWindow("An error ocurred while processing your request.", "Input Error");
						return;
					}
					
					MessageWindow success = new MessageWindow("Successfully updated the entry.", "Success");
					
				}
			}
		});
		
		bike_btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if( JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record?") != JOptionPane.YES_OPTION)
					return;
				

				String pInsertStatement = new String("DELETE FROM RACEBIKES WHERE bikename = ?");
				PreparedStatement preparedInsert = null;
					
				if(!checkComboContents((String) bike_cmboBikeName.getSelectedItem(), bike_cmboBikeName))
				{
					MessageWindow failure = new MessageWindow("No record with that name was found", "Error");
					return;
				}
				if(containsResult("SELECT * FROM RACETEAMS WHERE bikename = " + "'" + (String) bike_cmboBikeName.getSelectedItem() + "'"))
				{
					MessageWindow failure = new MessageWindow("This bike cannot be deleted while it is still used by a team.", "Error");
					return;
				}
				
				try
				{
					preparedInsert = con.prepareStatement(pInsertStatement);
				}
					catch (SQLException ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getErrorCode());
					return;					
				}
				
				try
				{
					preparedInsert.setString(1, (String) bike_cmboBikeName.getSelectedItem());
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
					return;
				}
					
				try
				{
					preparedInsert.executeUpdate();
				}
				catch (SQLException ex)
				{
					MessageWindow failure = new MessageWindow("An error ocurred while processing your request.", "Input Error");
					return;
				}
				bike_cmboBikeName.removeItem(bike_cmboBikeName.getSelectedItem());
				MessageWindow success = new MessageWindow("Successfully deleted the entry.", "Success");
					
			}
		});
		
		bike_btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		
		bike_cmboBikeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String pQuerySQLStatement = new String("SELECT country_of_origin, cost FROM RACEBIKES WHERE bikename = ?");
				PreparedStatement preparedQuery = null;
				try
				{
					preparedQuery = con.prepareStatement(pQuerySQLStatement);
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
				}
				
				try 
				{
					preparedQuery.setString(1, (String) bike_cmboBikeName.getSelectedItem());
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				
				try
				{
					rs = preparedQuery.executeQuery();
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
				}
				
				try 
				{
					while (rs.next())
					{
						bike_txtCountryOrigin.setText(rs.getString(1));
						bike_txtBikeCost.setText(rs.getString(2));
					}
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
		
		
		
		//Rider tab action handlers
		rider_btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(rider_txtTeamName.getText().length() == 0)
				{
					MessageWindow failure = new MessageWindow("Team Name cannot be empty.", "Input error");
					return;
				}
				if(!checkComboContents(rider_txtTeamName.getText(), team_cmboTeamName))
				{
					MessageWindow failure = new MessageWindow("A team with that name must already exist.", "Input error");
					return;
				}
				
				if(validateRiderInput((String) rider_cmboRiderName.getSelectedItem(), rider_txtTeamName.getText(), rider_txtNationality.getText(), rider_txtNumProWins))
				{
					String pInsertStatement = new String("INSERT INTO RACERIDERS VALUES(?, ?, ?, ?)");
					PreparedStatement preparedInsert = null;
					
					try
					{
						preparedInsert = con.prepareStatement(pInsertStatement);
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.setString(1, (String) rider_cmboRiderName.getSelectedItem());
						preparedInsert.setString(2, rider_txtTeamName.getText());
						preparedInsert.setString(3, rider_txtNationality.getText());
						
						if(rider_txtNumProWins.getText().length() != 0)
							preparedInsert.setInt(4, Integer.parseInt(rider_txtNumProWins.getText()));
						else
							preparedInsert.setNull(4, Types.NULL);
						
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.executeUpdate();
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						MessageWindow failure = new MessageWindow("A rider with that name is already in the database.", "Input Error");
						return;
					}
					rider_cmboRiderName.addItem((String) rider_cmboRiderName.getSelectedItem());
					MessageWindow success = new MessageWindow("Successfully created new entry.", "Success");
					
				}
			}
		});
		
		rider_btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(rider_txtTeamName.getText().length() == 0)
				{
					MessageWindow failure = new MessageWindow("Team Name cannot be empty.", "Input error");
					return;
				}
				if(!checkComboContents(rider_txtTeamName.getText(), team_cmboTeamName))
				{
					MessageWindow failure = new MessageWindow("A team with that name must already exist.", "Input error");
					return;
				}
				
				if(validateRiderInput((String) rider_cmboRiderName.getSelectedItem(), rider_txtTeamName.getText(), rider_txtNationality.getText(), rider_txtNumProWins))
				{
					String pInsertStatement = new String("UPDATE RACERIDERS SET teamname = ?, nationality = ?, num_pro_wins = ? WHERE ridername = ?");
					PreparedStatement preparedInsert = null;
					
					try
					{
						preparedInsert = con.prepareStatement(pInsertStatement);
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.setString(4, (String) rider_cmboRiderName.getSelectedItem());
						preparedInsert.setString(1, rider_txtTeamName.getText());
						preparedInsert.setString(2, rider_txtNationality.getText());
						
						if(rider_txtNumProWins.getText().length() != 0)
							preparedInsert.setInt(3, Integer.parseInt(rider_txtNumProWins.getText()));
						else
							preparedInsert.setNull(3, Types.NULL);
						
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.executeUpdate();
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						MessageWindow failure = new MessageWindow("An error occurred while processing your request.", "Input Error");
						return;
					}

					MessageWindow success = new MessageWindow("Successfully updated the entry.", "Success");					
				}
			}
		});
		
		rider_btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if( JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record?") != JOptionPane.YES_OPTION)
					return;
				

				String pDeleteStatement = new String("DELETE FROM RACERIDERS WHERE ridername = ?");
				PreparedStatement preparedDelete = null;
					
				if(!checkComboContents((String) rider_cmboRiderName.getSelectedItem(), rider_cmboRiderName))
				{
					MessageWindow failure = new MessageWindow("No record with that name was found", "Error");
					return;
				}
				if(containsResult("SELECT * FROM RACEWINNERS WHERE ridername = " + "'" + (String) rider_cmboRiderName.getSelectedItem() + "'"))
				{
					MessageWindow failure = new MessageWindow("This rider cannot be deleted while they have a recorded win.", "Error");
					return;
				}
				
				try
				{
					preparedDelete = con.prepareStatement(pDeleteStatement);
				}
					catch (SQLException ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getErrorCode());
					return;					
				}
				
				try
				{
					preparedDelete.setString(1, (String) rider_cmboRiderName.getSelectedItem());
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
					return;
				}
					
				try
				{
					preparedDelete.executeUpdate();
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
					MessageWindow failure = new MessageWindow("An error occurred while processing your request.", "Input Error");
					return;
				}
				rider_cmboRiderName.removeItem(rider_cmboRiderName.getSelectedItem());
				MessageWindow success = new MessageWindow("Successfully deleted the entry.", "Success");
			}
		});
		
		rider_btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		
		rider_cmboRiderName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pQuerySQLStatement = new String("SELECT teamname, nationality, num_pro_wins FROM RACERIDERS WHERE ridername = ?");
				PreparedStatement preparedQuery = null;
				try
				{
					preparedQuery = con.prepareStatement(pQuerySQLStatement);
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
				}
				
				try 
				{
					preparedQuery.setString(1, (String) rider_cmboRiderName.getSelectedItem());
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				
				try
				{
					rs = preparedQuery.executeQuery();
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
				}
				
				try 
				{
					while (rs.next())
					{
						rider_txtTeamName.setText(rs.getString(1));
						rider_txtNationality.setText(rs.getString(2));
						rider_txtNumProWins.setText(rs.getString(3));
					}
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				
				
			}
		});
		
		
		
		
		//Team tab action handlers
		team_btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(team_txtBikeName.getText().length() == 0)
				{
					MessageWindow failure = new MessageWindow("Bike Name cannot be empty.", "Input error");
					return;
				}
				if(!checkComboContents(team_txtBikeName.getText(), bike_cmboBikeName))
				{
					MessageWindow failure = new MessageWindow("A bike with that name must already exist.", "Input error");
					return;
				}
				if(checkComboContents((String) team_cmboTeamName.getSelectedItem(), team_cmboTeamName))
				{
					MessageWindow failure = new MessageWindow("A team with that name is already in the database.", "Input Error");
					return;
				}
				
				if(validateTeamInput((String) team_cmboTeamName.getSelectedItem(), team_txtBikeName.getText(), team_txtRegNation.getText(), team_txtNumRiders, team_txtManager.getText()))
				{
					String pInsertStatement = new String("INSERT INTO RACETEAMS VALUES(?, ?, ?, ?, ?)");
					PreparedStatement preparedInsert = null;
					
					try
					{
						preparedInsert = con.prepareStatement(pInsertStatement);
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.setString(1, (String) team_cmboTeamName.getSelectedItem());
						preparedInsert.setString(2, team_txtBikeName.getText());
						preparedInsert.setString(3, team_txtRegNation.getText());
						
						if(team_txtNumRiders.getText().length() != 0)
							preparedInsert.setInt(4, Integer.parseInt(team_txtNumRiders.getText()));
						else
							preparedInsert.setNull(4, Types.NULL);
						
						preparedInsert.setString(5, team_txtManager.getText());
						
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.executeUpdate();
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						MessageWindow failure = new MessageWindow("An error occurred while processing your request.", "Input Error");
						return;
					}
					team_cmboTeamName.addItem((String) team_cmboTeamName.getSelectedItem());
					MessageWindow success = new MessageWindow("Successfully created new entry.", "Success");
					
				}
			}
		});
		
		team_btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(team_txtBikeName.getText().length() == 0)
				{
					MessageWindow failure = new MessageWindow("Bike Name cannot be empty.", "Input error");
					return;
				}
				if(!checkComboContents(team_txtBikeName.getText(), bike_cmboBikeName))
				{
					MessageWindow failure = new MessageWindow("A bike with that name must already exist.", "Input error");
					return;
				}
				
				if(validateTeamInput((String) team_cmboTeamName.getSelectedItem(), team_txtBikeName.getText(), team_txtRegNation.getText(), team_txtNumRiders, team_txtManager.getText()))
				{
					String pInsertStatement = new String("UPDATE RACETEAMS SET bikename = ?, registered_nation = ?, num_riders = ?, manager = ? WHERE teamname = ?");
					PreparedStatement preparedInsert = null;
					
					try
					{
						preparedInsert = con.prepareStatement(pInsertStatement);
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.setString(5, (String) team_cmboTeamName.getSelectedItem());
						preparedInsert.setString(1, team_txtBikeName.getText());
						preparedInsert.setString(2, team_txtRegNation.getText());
						
						if(team_txtNumRiders.getText().length() != 0)
							preparedInsert.setInt(3, Integer.parseInt(team_txtNumRiders.getText()));
						else
							preparedInsert.setNull(3, Types.NULL);
						
						preparedInsert.setString(4, team_txtManager.getText());
						
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						return;
					}
					
					try
					{
						preparedInsert.executeUpdate();
					}
					catch (SQLException ex)
					{
						ex.printStackTrace();
						MessageWindow failure = new MessageWindow("An error occurred while processing your request.", "Input Error");
						return;
					}
					
					MessageWindow success = new MessageWindow("Successfully updated the entry.", "Success");
				}
			}
		});
		
		team_btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if( JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record?") != JOptionPane.YES_OPTION)
					return;
				

				String pDeleteStatement = new String("DELETE FROM RACETEAMS WHERE teamname = ?");
				PreparedStatement preparedDelete = null;
					
				if(!checkComboContents((String) team_cmboTeamName.getSelectedItem(), team_cmboTeamName))
				{
					MessageWindow failure = new MessageWindow("No record with that name was found", "Error");
					return;
				}
				if(containsResult("SELECT * FROM RACERIDERS WHERE teamname = " + "'" + (String) team_cmboTeamName.getSelectedItem() + "'"))
				{
					MessageWindow failure = new MessageWindow("This team cannot be deleted while it still has members.", "Error");
					return;
				}
				try
				{
					preparedDelete = con.prepareStatement(pDeleteStatement);
				}
					catch (SQLException ex)
				{
					ex.printStackTrace();
					System.out.println(ex.getErrorCode());
					return;					
				}
				
				try
				{
					preparedDelete.setString(1, (String) team_cmboTeamName.getSelectedItem());
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
					return;
				}
					
				try
				{
					preparedDelete.executeUpdate();
				}
				catch (SQLException ex)
				{
					MessageWindow failure = new MessageWindow("An error occurred while processing your request.", "Input Error");
					return;
				}
				
				team_cmboTeamName.removeItem(team_cmboTeamName.getSelectedItem());
				MessageWindow success = new MessageWindow("Successfully deleted the entry.", "Success");
			}
		});
		
		team_btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		
		team_cmboTeamName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pQuerySQLStatement = new String("SELECT bikename, registered_nation, num_riders, manager FROM RACETEAMS WHERE teamname = ?");
				PreparedStatement preparedQuery = null;
				try
				{
					preparedQuery = con.prepareStatement(pQuerySQLStatement);
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
				}
				
				try 
				{
					preparedQuery.setString(1, (String) team_cmboTeamName.getSelectedItem());
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				
				try
				{
					rs = preparedQuery.executeQuery();
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
				}
				
				try 
				{
					while (rs.next())
					{
						team_txtBikeName.setText(rs.getString(1));
						team_txtRegNation.setText(rs.getString(2));
						team_txtNumRiders.setText(rs.getString(3));
						team_txtManager.setText(rs.getString(4));
					}
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
			}
		});
	}
	
	
	//Returns true if the passed query returns any results, false if not.
	public boolean containsResult(String query)
	{
		int count = 0;
		
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next())
				count++;
			
			if(count > 0)
				return true;
			else
				return false;
		}
		catch (SQLException ex)
		{
			return false;
		}
	}
	
	//Populates the passed combobox with items obtained from the passed query
	public void populateComboBox(JComboBox<String> box, String query)
	{
		box.removeAllItems();
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next())
			{
				box.addItem(rs.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	//Checks the passed combobox for a match with the passed string. Returns true if a match is found, false if not.
	public boolean checkComboContents(String item, JComboBox<String> box)
	{
		
		for(int i = 0; i < box.getItemCount(); i++)
			if(item.equals(box.getItemAt(i).toString()) )
				return true;
			
		return false;
	}
	
	//Validates the info typed into the bike tab before sending an INSERT or UPDATE request.
	public boolean validateBikeInput(String name, String origin, JTextField txtCost)
	{
		if(name.isEmpty())
		{
			MessageWindow error = new MessageWindow("Bike Name cannot be empty.", "Input Error");
			return false;
		}
			
		
		if(name.length() > 20)
		{
			MessageWindow error = new MessageWindow("Bike Name cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		if(origin.length() > 20)
		{
			MessageWindow error = new MessageWindow("Country of Origin cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		try
		{
			if(txtCost.getText().length() != 0)
			{
				if(Integer.parseInt(txtCost.getText()) < 0)
				{
					MessageWindow error = new MessageWindow("Bike Cost cannot be less than 0.", "Input Error");
					return false;
				}
			}
		}
		catch (Exception ex)
		{
			MessageWindow error = new MessageWindow("Bike Cost must be an integer.", "Input Error");
			return false;
		}
		
		
			
		
		return true;
	}
	
	//Validates the info typed into the rider tab before sending an INSERT or UPDATE request.
	public boolean validateRiderInput(String riderName, String teamName, String nationality, JTextField numProWins)
	{
		if(riderName.isEmpty())
		{
			MessageWindow error = new MessageWindow("Rider Name cannot be empty.", "Input Error");
			return false;
		}
		if(teamName.isEmpty())
		{
			MessageWindow error = new MessageWindow("Team Name cannot be empty.", "Input Error");
			return false;
		}
			
		
		if(riderName.length() > 20)
		{
			MessageWindow error = new MessageWindow("Rider Name cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		if(teamName.length() > 20)
		{
			MessageWindow error = new MessageWindow("Team Name cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		if(nationality.length() > 20)
		{
			MessageWindow error = new MessageWindow("Nationality cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		try
		{
			if(numProWins.getText().length() != 0)
			{
				if(Integer.parseInt(numProWins.getText()) < 0)
				{
					MessageWindow error = new MessageWindow("Number of Pro Wins cannot be less than 0.", "Input Error");
					return false;
				}
			}
		}
		catch (Exception ex)
		{
			MessageWindow error = new MessageWindow("Number of Pro Wins must be an integer.", "Input Error");
			return false;
		}
		
		
			
		
		return true;
	}
	
	//Validates the info typed into the team tab before sending an INSERT or UPDATE request.
	public boolean validateTeamInput(String teamName, String bikeName, String regNation, JTextField numRiders, String manager)
	{
		if(teamName.isEmpty())
		{
			MessageWindow error = new MessageWindow("Team Name cannot be empty.", "Input Error");
			return false;
		}
		if(bikeName.isEmpty())
		{
			MessageWindow error = new MessageWindow("Bike Name cannot be empty.", "Input Error");
			return false;
		}
			
		
		if(teamName.length() > 20)
		{
			MessageWindow error = new MessageWindow("Team Name cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		if(bikeName.length() > 20)
		{
			MessageWindow error = new MessageWindow("Bike Name cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		if(regNation.length() > 20)
		{
			MessageWindow error = new MessageWindow("Registerd Nation cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		if(manager.length() > 20)
		{
			MessageWindow error = new MessageWindow("Manager name cannot be longer than 20 characters.", "Input Error");
			return false;
		}
		
		try
		{
			if(numRiders.getText().length() != 0)
			{
				if(Integer.parseInt(numRiders.getText()) < 0)
				{
					MessageWindow error = new MessageWindow("Number of Riders cannot be less than 0.", "Input Error");
					return false;
				}
			}
		}
		catch (Exception ex)
		{
			MessageWindow error = new MessageWindow("Number of Riders must be an integer.", "Input Error");
			return false;
		}
		
		
			
		
		return true;
	}
	

}
