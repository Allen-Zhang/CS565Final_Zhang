package cs565.finals;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


public class Brokerage extends JFrame {
	
	private static double fee;
	private static final int WIDTH = 520;
    private static final int HEIGHT = 580;
    
	private MySqlConnection dbConn;
	private Account accountTableModel;

    private JPanel mainCardPanel;
	
    private JPanel custPanel;
    private JPanel listCustPanel;  // Panel for listing customers
    private JPanel custCardPanel;
    private JPanel welcomeCustPanel;  // Panel for welcoming customers
    private JPanel addCustPanel;  // Panel for adding a new customer

    private JPanel txPanel;
    private JPanel custInfoPanel;  // Panel for displaying customer information
    private JPanel txCardPanel;
    private JPanel newTxPanel;  // Panel for making a new transaction 
    private JPanel viewTxPanel;  // Panel for viewing transaction history

    private JMenuItem loadFromDB, addCust, saveToDB;
    private JTable custListTable;
    private JTextField textF_CUST_NAME, textF_CUST_ID, textF_INIT_DEPOSIT;
    private JButton button_ADD_CUST;

    private CardLayout mainCard = new CardLayout();
    private CardLayout custCard = new CardLayout();
    
    public Brokerage(MySqlConnection dbConnArg) throws SQLException {
    	super("MET Broker");
    	
    	this.dbConn = dbConnArg;
    	
    	setSize(WIDTH, HEIGHT);
    	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    	int x = (size.width - WIDTH) / 2;
		int y = (size.height - HEIGHT - 100) / 2;
		setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
        setLayout(new BorderLayout());
        
        /*----- 1 Main Page Panel Part -----*/
        
        mainCardPanel = new JPanel();
        // Set CardLayout for mainPanel 
        mainCardPanel.setLayout(mainCard);  
        
        /*----- 2 Main Page Menu Part -----*/
        
        loadFromDB = new JMenuItem("Load From Database");
        addCust = new JMenuItem("Add Customer");
        saveToDB = new JMenuItem("Save To Database");
        
        // Add listeners for menu items
        ListenForCardPanel lForCardPanel = new ListenForCardPanel();
        loadFromDB.addActionListener(lForCardPanel);
        addCust.addActionListener(lForCardPanel);
    	ListenForButton lForButton = new ListenForButton();
        saveToDB.addActionListener(lForButton);
        
        JMenu menu = new JMenu("File");
        Font font_MENU = new Font("SansSerif", Font.BOLD, 13);
        menu.setFont(font_MENU);
        menu.add(loadFromDB);
        menu.add(addCust);
        menu.add(saveToDB);
        JMenuBar toolBar = new JMenuBar();
        toolBar.add(menu);
        
        /*----- 1.1 Customer List Panel Part -----*/
        
        custPanel = new JPanel();
        custPanel.setLayout(new GridBagLayout());
           
        CachedRowSet crsOfAccountTable = getContentsOfAccountTable();
        accountTableModel = new Account(crsOfAccountTable);
        accountTableModel.addEventHandlersToRowSet(new ListenForAccountRowSet());
        
        custListTable = new JTable();
        custListTable.setModel(accountTableModel);
        custListTable.addMouseListener(new ListenForMouse());
        custListTable.setFillsViewportHeight(true);
        custListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        listCustPanel = new JPanel();
        listCustPanel.setLayout(new BorderLayout());
        listCustPanel.add(new JScrollPane(custListTable));
        
        custCardPanel = new JPanel();
        // Set CardLayout for custCardPanel
        custCardPanel.setLayout(custCard);    
        
        /*----- 1.1.1 Welcome Customer Panel Part -----*/
        
        welcomeCustPanel = new JPanel();
        welcomeCustPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 75));
        
        Font font_WELCOME = new Font("Serif", Font.BOLD, 30);
        JLabel label_WELCOME = new JLabel("Welcome to MET Broker!");
        label_WELCOME.setFont(font_WELCOME);
        label_WELCOME.setForeground(Color.BLUE);
        welcomeCustPanel.add(label_WELCOME, BorderLayout.CENTER);
        
        /*----- 1.1.2 Add Customer Panel Part -----*/
        
        addCustPanel = new JPanel();
        addCustPanel.setLayout(new GridBagLayout());
        addCustPanel.setBorder(BorderFactory.createTitledBorder("Add a New Customer"));
        
        JLabel label_CUST_NAME = new JLabel("Customer Name: ");
        JLabel label_CUST_ID = new JLabel("Customer ID: ");
        JLabel label_INIT_DEPOSIT = new JLabel("Initial Deposit: ");
        
        textF_CUST_NAME = new JTextField(15); 
        textF_CUST_ID = new JTextField(15); 
        textF_INIT_DEPOSIT = new JTextField(15);
    	button_ADD_CUST = new JButton("Add Customer");
    	// Add listener for add customer button
    	button_ADD_CUST.addActionListener(lForButton);
    	
    	addComp(addCustPanel, label_CUST_NAME, 0, 0, 1, 1, new Insets(7,0,0,5),
    			GridBagConstraints.LINE_END, GridBagConstraints.NONE);
    	addComp(addCustPanel, textF_CUST_NAME, 1, 0, 2, 1, new Insets(7,10,0,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	addComp(addCustPanel, label_CUST_ID, 0, 1, 1, 1, new Insets(0,0,0,27),
    			GridBagConstraints.LINE_END, GridBagConstraints.NONE);
    	addComp(addCustPanel, textF_CUST_ID, 1, 1, 2, 1, new Insets(0,10,0,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	addComp(addCustPanel, label_INIT_DEPOSIT, 0, 2, 1, 1, new Insets(0,0,0,20),
    			GridBagConstraints.LINE_END, GridBagConstraints.NONE);
    	addComp(addCustPanel, textF_INIT_DEPOSIT, 1, 2, 2, 1, new Insets(0,10,0,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	addComp(addCustPanel, button_ADD_CUST, 1, 3, 2, 1, new Insets(0,62,7,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	
        custCardPanel.add("custCard01", welcomeCustPanel);
        custCardPanel.add("custCard02", addCustPanel);
        
        addPanel(custPanel, listCustPanel, 0, 0, 1, 1, 1, 1, new Insets(0,0,10,0),
        		GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addPanel(custPanel, custCardPanel, 0, 1, 1, 1, 1, 0, new Insets(0,0,0,0),
        		GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL);
    	
        /*----- 1.2 Customer Transaction Panel Part -----*/
                
        txPanel = new JPanel();
        txPanel.setBackground(Color.orange);

        
        
        mainCardPanel.add("mainCard01", custPanel);
        mainCardPanel.add("mainCard02", txPanel);
        
        add(toolBar, BorderLayout.NORTH);
        add(mainCardPanel, BorderLayout.CENTER);
        
    } 

    // Set rules for a panel destined for a GridBagLayout and then add it 
 	private void addPanel(JPanel thePanel, JPanel subPanel, int xPos, int yPos, int gridW, 
 			int gridH, double weightX, double weightY, Insets inset, int place, int stretch){
 		
 		GridBagConstraints gridConstraints = new GridBagConstraints();
 		
 		gridConstraints.gridx = xPos;
 		gridConstraints.gridy = yPos;
 		gridConstraints.gridwidth = gridW;
 		gridConstraints.gridheight = gridH;
 		gridConstraints.weightx = weightX;
 		gridConstraints.weighty = weightY;
 		gridConstraints.insets = inset;
 		gridConstraints.anchor = place;
 		gridConstraints.fill = stretch;
 		
 		thePanel.add(subPanel, gridConstraints);
 	}
 	
 	// Set rules for a component destined for a GridBagLayout and then add it 
  	private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, 
  			int gridW, int gridH, Insets inset, int place, int stretch){
  		
  		GridBagConstraints gridConstraints = new GridBagConstraints();
  		
  		gridConstraints.gridx = xPos;
  		gridConstraints.gridy = yPos;
  		gridConstraints.gridwidth = gridW;
  		gridConstraints.gridheight = gridH;
  		gridConstraints.weightx = 1;
  		gridConstraints.weighty = 1;
  		gridConstraints.insets = inset;
  		gridConstraints.anchor = place;
  		gridConstraints.fill = stretch;
  		
  		thePanel.add(comp, gridConstraints);
  	}
  	
  	// Display the SQLException in a dialog box
  	private void saveToDBFailedDialog(SQLException e) {
		JOptionPane.showMessageDialog(Brokerage.this, 
				new String[] {
					"Save to database failed!",
					"Error message: ",
					"    \"" + e.getClass().getName() + ": ", 
					"     " + e.getMessage() + "\"" 
				});
	}
  	
  	// Display the SQLException in a dialog box
  	private void displaySQLExceptionDialog(SQLException e) {
		JOptionPane.showMessageDialog(Brokerage.this, 
				new String[] {e.getClass().getName() + ": ", e.getMessage()});
	}
  	
  	// Reset all textfields
  	private void resetTextField() {
  		textF_CUST_NAME.setText("");
		textF_CUST_ID.setText("");
		textF_INIT_DEPOSIT.setText("");
  	}
  	
  	public CachedRowSet getContentsOfAccountTable() throws SQLException {
  		CachedRowSet crs = null;
  		try {
  			RowSetFactory rowSetFactory = RowSetProvider.newFactory(
  					"com.sun.rowset.RowSetFactoryImpl", null);
  			crs = rowSetFactory.createCachedRowSet();
  			
  			crs.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
			crs.setConcurrency(ResultSet.CONCUR_UPDATABLE);
			crs.setUsername(dbConn.getDB_USERNAME());
			crs.setPassword(dbConn.getDB_PASSWORD());
			crs.setUrl(dbConn.getDB_URL() + "?relaxAutoCommit=true");
			crs.setCommand("SELECT CustomerName, CustomerId, OpeningDate, "
					+ "OpeningBalance FROM zhang_accounts");
			crs.execute();
			
  		} catch (SQLException e) {
			displaySQLExceptionDialog(e);
		}
		return crs;
  	}
  	
  	private void createNewAccountTableModel() throws SQLException {
		accountTableModel = new Account(getContentsOfAccountTable());
		accountTableModel.addEventHandlersToRowSet(new ListenForAccountRowSet());
		custListTable.setModel(accountTableModel);
	}
  	
 	// ActionListener for swapping card panels
    private class ListenForCardPanel implements ActionListener{

    	@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loadFromDB) {
				mainCard.show(mainCardPanel, "mainCard01");
				custCard.show(custCardPanel, "custCard01");
				try {
					// Refresh data of customer list
					createNewAccountTableModel();
				} catch (SQLException sqle) {
					displaySQLExceptionDialog(sqle);
				}
				// Reset all textfields
				resetTextField();
			} 
			if (e.getSource() == addCust) {
				custCard.show(custCardPanel, "custCard02");
			} 
		}
    }
    
    // ActionListener for buttons
    private class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == saveToDB) {
				try {
					// Save changes into database
					accountTableModel.getAccountRowSet().acceptChanges();
					JOptionPane.showMessageDialog(Brokerage.this,
							"Save to database successfully!");
				} catch (SQLException sqle1) {
					saveToDBFailedDialog(sqle1);
					try {
						// Revert back changes
						createNewAccountTableModel();
					} catch (SQLException sqle2) {
						saveToDBFailedDialog(sqle2);
					}
				}
			}
			if (e.getSource() == button_ADD_CUST) {
				// Not empty validation
				if (textF_CUST_NAME.getText().trim().length() != 0 && 
						textF_CUST_ID.getText().trim().length() != 0 && 
						textF_INIT_DEPOSIT.getText().trim().length() != 0) {
					JOptionPane.showMessageDialog(Brokerage.this, 
							new String[] {
								"New customer information:",
								"Customer Name:  " + textF_CUST_NAME.getText(),
								"Customer ID:  " + textF_CUST_ID.getText(),
								"Initial Deposit:  $" + textF_INIT_DEPOSIT.getText()
							});
					try {
						accountTableModel.addCustomer(
								textF_CUST_NAME.getText(),
								textF_CUST_ID.getText(),
								Double.parseDouble(textF_INIT_DEPOSIT.getText().trim()));
					} catch (NumberFormatException nfe) {
						nfe.printStackTrace();
						JOptionPane.showMessageDialog(Brokerage.this,
								"Invalid initial deposit, please try again!");
					}
					// Reset textfields after inserting a row
					resetTextField();
				} else {
					JOptionPane.showMessageDialog(Brokerage.this, 
							"Please enter all required value!");
				}
			}
		}
    }
    
    private class ListenForAccountRowSet implements RowSetListener {

		@Override
		public void rowChanged(RowSetEvent event) {
			CachedRowSet currentRowSet = accountTableModel.getAccountRowSet();
			try {
				currentRowSet.moveToCurrentRow();
				accountTableModel = new Account(accountTableModel.getAccountRowSet());
				custListTable.setModel(accountTableModel);
			} catch (SQLException sqle) {
				displaySQLExceptionDialog(sqle);
			}
		}
		
		@Override
		public void rowSetChanged(RowSetEvent event) {}
		
		@Override
		public void cursorMoved(RowSetEvent event) {}
    	
    }
    
    private class ListenForMouse implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// Go to transaction page with customer's data 
			// when double click a record of customer list
			if (e.getClickCount() == 2) {
				int row = custListTable.getSelectedRow();
				int colCount = custListTable.getColumnCount();
				String[] custInfo = new String[colCount];
				for (int i = 0; i < colCount; i++) {
					custInfo[i] = (String)custListTable.getModel().getValueAt(row, i);
				}
				if (!custInfo[0].equals("java.sql.SQLException: absolute : Invalid cursor position")) {
					mainCard.show(mainCardPanel, "mainCard02");
				} 
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
    	
    }
    
	public static void main(String[] args) throws Exception {
		
		try {
			MySqlConnection mySqlConnection = new MySqlConnection();
			Brokerage brokerage = new Brokerage(mySqlConnection);
			brokerage.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
