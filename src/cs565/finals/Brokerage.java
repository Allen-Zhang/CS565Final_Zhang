package cs565.finals;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;


public class Brokerage extends JFrame {
	
	private static double fee = 5;  // Charge $5 for any buy and sell transactions
	private String name, id;
	private double balance;
	private static final int WIDTH = 900;
    private static final int HEIGHT = 620;
    
	private MySqlConnection dbConn;
	private Account accountTableModel;
	private Stock stockQuotesTableModel; 
	private Portfolio poHistoryTableModel; 
	private Portfolio poCurrentTableModel;
	private Transaction txTableModel;
	
    private JPanel mainCardPanel;  // The biggest panel
	
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
    private JLabel label_INFO_NAME, label_INFO_ID, label_INFO_OPDATE, label_INFO_BALANCE;
    private JTable custListTable, stockQuotesTable, poCurrentTable, poHistoryTable,
		txHistoryTable;
    private JTextField textF_CUST_NAME, textF_CUST_ID, textF_INIT_DEPOSIT, 
    	textF_TX_SYMBOL, textF_TX_PRICE, textF_TX_QTY, textF_TX_DEPOSIT;
    private JButton button_ADD_CUST, button_NEW_TX, button_TX_HISTORY,
    	button_TX_BUY, button_TX_SELL, button_TX_SELL_ALL, button_TX_DEPOSIT,
    	button_PO_SEARCH, button_TX_TYPE_S, button_TX_SINGLE_S, button_TX_FROM_TO_S;
    private DatePicker poHistoryPicker, txSinglePicker, txFromPicker, txToPicker;
    private JComboBox comBoBox_txType;
    
    private CardLayout mainCard = new CardLayout();
    private CardLayout custCard = new CardLayout();
    private CardLayout txCard = new CardLayout();
    
    public Brokerage(MySqlConnection dbConnArg) throws SQLException {
    	super("MET Broker");
    	this.dbConn = dbConnArg;
    	
    	setSize(WIDTH, HEIGHT);
    	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    	int x = (size.width - WIDTH) / 2;
		int y = (size.height - HEIGHT - 80) / 2;
		setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
        setLayout(new BorderLayout());
        
        /*----- 1 Main Page Panel -----*/
        
        mainCardPanel = new JPanel();
        mainCardPanel.setLayout(mainCard);  // Set CardLayout for mainPanel 
        
        /*----- 2 Main Page Menu -----*/
        
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
        
        /*----- 1.1 Main Page Customer Panel -----*/
        
        custPanel = new JPanel();
        custPanel.setLayout(new GridBagLayout());
        
        /*----- 1.1.1 Customer List Panel -----*/
      	
        custListTable = new JTable();
//        createAccountTableModel();  // Load from database when the app is launched
        custListTable.addMouseListener(new ListenForMouse());
        custListTable.setFillsViewportHeight(true);
        custListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        listCustPanel = new JPanel();
        listCustPanel.setLayout(new BorderLayout());
        listCustPanel.add(new JScrollPane(custListTable));
        
        custCardPanel = new JPanel();
        custCardPanel.setLayout(custCard);   // Set CardLayout for custCardPanel
        
        /*----- 1.1.2.1 Welcome Customer Panel -----*/
        
        welcomeCustPanel = new JPanel();
        welcomeCustPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 75));
        
        Font font_WELCOME = new Font("Serif", Font.BOLD, 30);
        JLabel label_WELCOME = new JLabel("Welcome to MET Broker!");
        label_WELCOME.setFont(font_WELCOME);
        label_WELCOME.setForeground(Color.BLUE);
        welcomeCustPanel.add(label_WELCOME, BorderLayout.CENTER);
        
        /*----- 1.1.2.2 Add Customer Panel -----*/
        
        addCustPanel = new JPanel();
        addCustPanel.setLayout(new GridBagLayout());
        addCustPanel.setBorder(BorderFactory.createTitledBorder("Add New Customers"));
        JPanel addCustMidPanel = new JPanel();
        addCustMidPanel.setLayout(new GridBagLayout());
        
        JLabel label_CUST_NAME = new JLabel("Customer Name: ");
        JLabel label_CUST_ID = new JLabel("Customer ID: ");
        JLabel label_INIT_DEPOSIT = new JLabel("Initial Deposit: ");
        
        textF_CUST_NAME = new JTextField(17); 
        textF_CUST_ID = new JTextField(17); 
        textF_INIT_DEPOSIT = new JTextField(17);
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
    	addComp(addCustPanel, button_ADD_CUST, 1, 3, 2, 1, new Insets(0,84,7,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	
    	addPanel(addCustMidPanel, addCustPanel, 0, 0, 1, 1, 1, 1, new Insets(
    			0,10,10,10), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        custCardPanel.add("custCard01", welcomeCustPanel);
        custCardPanel.add("custCard02", addCustMidPanel);
        
        addPanel(custPanel, listCustPanel, 0, 0, 1, 1, 1, 1, new Insets(0,0,10,0),
        		GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addPanel(custPanel, custCardPanel, 0, 1, 1, 1, 1, 0, new Insets(0,0,0,0),
        		GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL);
    	
        /*----- 1.2 Main Page Transaction Panel -----*/
                
        txPanel = new JPanel();
        txPanel.setLayout(new GridBagLayout());
        
        /*----- 1.2.1 Customer Information Panel -----*/
        
        custInfoPanel = new JPanel();
        custInfoPanel.setLayout(new GridBagLayout());
        
        label_INFO_NAME = new JLabel();
        label_INFO_ID = new JLabel(); 
        label_INFO_OPDATE = new JLabel(); 
        label_INFO_BALANCE = new JLabel();
        
        Font font_CUST_INFO = new Font("SansSerif", Font.BOLD, 15);
        label_INFO_NAME.setFont(font_CUST_INFO);
        label_INFO_ID.setFont(font_CUST_INFO);
        label_INFO_OPDATE.setFont(font_CUST_INFO);
        label_INFO_BALANCE.setFont(font_CUST_INFO);
        
        button_NEW_TX = new JButton("Make New Transactions");
        button_TX_HISTORY = new JButton("View Transaction History");
        // Add listeners for new_tx and tx_history buttons
        button_NEW_TX.addActionListener(lForCardPanel);
        button_TX_HISTORY.addActionListener(lForCardPanel);
        
        addComp(custInfoPanel, label_INFO_NAME, 0, 0, 1, 1, new Insets(10,10,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, label_INFO_ID, 1, 0, 1, 1, new Insets(10,10,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, label_INFO_OPDATE, 2, 0, 1, 1, new Insets(10,40,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, label_INFO_BALANCE, 3, 0, 1, 1, new Insets(10,10,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, button_NEW_TX, 0, 1, 2, 1, new Insets(10,10,5,-30),
    			GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL);
        addComp(custInfoPanel, button_TX_HISTORY, 2, 1, 2, 1, new Insets(10,40,5,10),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL);
        
        txCardPanel = new JPanel();
        txCardPanel.setLayout(txCard);   // Set CardLayout for txCardPanel
        
        /*----- 1.2.2.1 New Transaction Panel -----*/
        
        newTxPanel = new JPanel();
        newTxPanel.setLayout(new GridBagLayout());
        
        JPanel newTxPortfolioPanel = new JPanel();
        newTxPortfolioPanel.setLayout(new GridBagLayout());
        
        JPanel poCurrentPanel = new JPanel();
        JPanel poHistoryPanel = new JPanel();
        JPanel stockQuotesPanel = new JPanel();
        
        poCurrentPanel.setLayout(new BorderLayout());
        poHistoryPanel.setLayout(new BorderLayout());
        stockQuotesPanel.setLayout(new BorderLayout());
        
        JLabel label_PO_CURRENT = new JLabel("Current Portfolio");
        JLabel label_PO_HISTORY = new JLabel("Portfolio History");
        JLabel label_STOCK_QUOTES = new JLabel("Stock Quotes");
        
        label_PO_CURRENT.setHorizontalAlignment(SwingConstants.CENTER);
        label_PO_HISTORY.setHorizontalAlignment(SwingConstants.CENTER);
        label_STOCK_QUOTES.setHorizontalAlignment(SwingConstants.CENTER);
        
        Font font_PO = new Font("SansSerif", Font.BOLD, 15);
        label_PO_CURRENT.setFont(font_PO);
        label_PO_HISTORY.setFont(font_PO);
        label_STOCK_QUOTES.setFont(font_PO);
        
        // "Current Portfolio" table part
        poCurrentTable = new JTable();
        createCurrentPoTableModel("");  // Initiate poCurrentTable with dummy data
        poCurrentTable.setFillsViewportHeight(true);
        
        // "Portfolio History" table part
        poHistoryTable = new JTable();
        createPoHistoryTableModel("",20140401);  // Initiate poHistoryTable with dummy data
        poHistoryTable.setFillsViewportHeight(true);
        
        button_PO_SEARCH = new JButton(" Search ");
        // Add listener for stock history search button
        button_PO_SEARCH.addActionListener(lForButton);
        poHistoryPicker = new DatePicker(poHistoryPanel, 45);
        
        // "Stock Quotes" table part
        stockQuotesTable = new JTable();
        createStockQuotesTableModel();
        stockQuotesTable.setFillsViewportHeight(true);
        stockQuotesTable.addMouseListener(new ListenForMouse());
        stockQuotesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JPanel poHistorySearchPanel = new JPanel();
        poHistorySearchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));
        poHistorySearchPanel.add(poHistoryPicker);
        poHistorySearchPanel.add(button_PO_SEARCH);
        
        stockQuotesPanel.add(label_STOCK_QUOTES, BorderLayout.NORTH);
        stockQuotesPanel.add(new JScrollPane(stockQuotesTable), BorderLayout.CENTER);
        poCurrentPanel.add(label_PO_CURRENT, BorderLayout.NORTH);
        poCurrentPanel.add(new JScrollPane(poCurrentTable), BorderLayout.CENTER);
        poHistoryPanel.add(label_PO_HISTORY, BorderLayout.NORTH);
        poHistoryPanel.add(new JScrollPane(poHistoryTable), BorderLayout.CENTER);
        poHistoryPanel.add(poHistorySearchPanel, BorderLayout.SOUTH);
        
        addPanel(newTxPortfolioPanel, poCurrentPanel, 0, 0, 1, 1, 0.9, 1, new Insets(
        		0,0,0,10), GridBagConstraints.WEST, GridBagConstraints.BOTH);
        addPanel(newTxPortfolioPanel, poHistoryPanel, 1, 0, 1, 1, 1.2, 1, new Insets(
        		0,0,0,0), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addPanel(newTxPortfolioPanel, stockQuotesPanel, 2, 0, 1, 1, 0.8, 1, new Insets(
        		0,10,0,0), GridBagConstraints.EAST, GridBagConstraints.BOTH);
        
        JLabel label_TX_SYMBOl = new JLabel("Symbol: ");
        JLabel label_TX_PRICE = new JLabel("Price: ");
        JLabel label_TX_QTY = new JLabel("Quantity: ");
        JLabel label_TX_DEPOSIT = new JLabel("Deposit Amount: ");
        
        textF_TX_SYMBOL = new JTextField(10);
        textF_TX_SYMBOL.setEditable(false);
        textF_TX_SYMBOL.setBackground(Color.white);
        textF_TX_PRICE = new JTextField(10);
        textF_TX_PRICE.setEditable(false);
        textF_TX_PRICE.setBackground(Color.white);
        textF_TX_QTY = new JTextField(10);
        textF_TX_DEPOSIT = new JTextField(15);
        
        button_TX_BUY = new JButton("  Buy  "); 
        button_TX_SELL = new JButton("  Sell  ");
        button_TX_DEPOSIT = new JButton(" Deposit ");
        button_TX_SELL_ALL = new JButton(" Sell All ");
        
        // Add listeners for add customer buttons
        button_TX_BUY.addActionListener(lForButton);
        button_TX_SELL.addActionListener(lForButton);
        button_TX_DEPOSIT.addActionListener(lForButton);
        button_TX_SELL_ALL.addActionListener(lForButton);
        
        JPanel newTxBuyAndSellPanel = new JPanel();
        newTxBuyAndSellPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15,10));
        newTxBuyAndSellPanel.setBorder(BorderFactory.createTitledBorder(
        		"Buy and Sell Transaction"));
        JPanel newTxDepositPanel = new JPanel();
        newTxDepositPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15,8));
        newTxDepositPanel.setBorder(BorderFactory.createTitledBorder(
        		"Deposit Transaction"));
        
        newTxBuyAndSellPanel.add(label_TX_SYMBOl);
        newTxBuyAndSellPanel.add(textF_TX_SYMBOL);
        newTxBuyAndSellPanel.add(label_TX_PRICE);
        newTxBuyAndSellPanel.add(textF_TX_PRICE);
        newTxBuyAndSellPanel.add(label_TX_QTY);
        newTxBuyAndSellPanel.add(textF_TX_QTY);
        newTxBuyAndSellPanel.add(button_TX_BUY);
        newTxBuyAndSellPanel.add(button_TX_SELL);
        newTxBuyAndSellPanel.add(button_TX_SELL_ALL);
        
        newTxDepositPanel.add(label_TX_DEPOSIT);
        newTxDepositPanel.add(textF_TX_DEPOSIT);
        newTxDepositPanel.add(button_TX_DEPOSIT);
       
        addPanel(newTxPanel, newTxPortfolioPanel, 0, 0, 1, 1, 1, 1, new Insets(0,10,10,10),
        		GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addPanel(newTxPanel, newTxBuyAndSellPanel, 0, 1, 1, 1, 1, 0, new Insets(0,10,5,10),
        		GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL);
        addPanel(newTxPanel, newTxDepositPanel, 0, 2, 1, 1, 1, 0, new Insets(0,10,10,10),
        		GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL);
        
        /*----- 1.2.2.2 Transaction History Panel -----*/
        
        viewTxPanel = new JPanel();
        viewTxPanel.setLayout(new GridBagLayout());
        
        JPanel viewTxTypePanel = new JPanel();
        JPanel viewTxSinglePanel = new JPanel();
        JPanel viewTxTSPanel = new JPanel();
        JPanel viewTxFromToPanel = new JPanel();
        JPanel viewTxTitlelPanel = new JPanel();
        JPanel viewTxHistoryPanel = new JPanel();
        
        viewTxTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT,15,0));
        viewTxSinglePanel.setLayout(new FlowLayout(FlowLayout.LEFT,15,0));
        viewTxTSPanel.setLayout(new GridBagLayout());
        viewTxFromToPanel.setLayout(new FlowLayout(FlowLayout.LEFT,15,0));
        viewTxTitlelPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
        viewTxHistoryPanel.setLayout(new BorderLayout());
        
        JLabel label_TX_TYPE = new JLabel("Transaction Type:");
        JLabel label_TX_SINGLE = new JLabel("Single Date:");
        JLabel label_TX_FROM_TO = new JLabel("From Date To Date:    From");
        JLabel label_TX_TO = new JLabel("To");
        JLabel label_TX_TITLE = new JLabel("Transaction History Table");
        label_TX_TITLE.setFont(font_CUST_INFO);
        
        button_TX_TYPE_S = new JButton(" Search ");
        button_TX_SINGLE_S = new JButton(" Search ");
        button_TX_FROM_TO_S = new JButton(" Search ");
        
        // Add listeners for searching transaction history buttons
        button_TX_TYPE_S.addActionListener(lForButton);
        button_TX_SINGLE_S.addActionListener(lForButton);
        button_TX_FROM_TO_S.addActionListener(lForButton);
        
        txSinglePicker = new DatePicker(viewTxSinglePanel, 57);
        txFromPicker = new DatePicker(viewTxFromToPanel, 57);
        txToPicker = new DatePicker(viewTxFromToPanel, 57);
        
        String[] types = {"All", "Buy", "Sell", "Deposit"};
        comBoBox_txType = new JComboBox(types);
        comBoBox_txType.setMaximumRowCount(4);
        comBoBox_txType.setPreferredSize(new Dimension(100,28));
        
        viewTxTypePanel.add(label_TX_TYPE);
        viewTxTypePanel.add(comBoBox_txType);
        viewTxTypePanel.add(button_TX_TYPE_S);
        
        viewTxSinglePanel.add(label_TX_SINGLE);
        viewTxSinglePanel.add(txSinglePicker);
        viewTxSinglePanel.add(button_TX_SINGLE_S);
        
        viewTxFromToPanel.add(label_TX_FROM_TO);
        viewTxFromToPanel.add(txFromPicker);
        viewTxFromToPanel.add(label_TX_TO);
        viewTxFromToPanel.add(txToPicker);
        viewTxFromToPanel.add(button_TX_FROM_TO_S);
        
        viewTxTitlelPanel.add(label_TX_TITLE);
        
        txHistoryTable = new JTable();
        createTxTableModel("", "");
        txHistoryTable.setFillsViewportHeight(true);
        
        viewTxHistoryPanel.setLayout(new BorderLayout());
        viewTxHistoryPanel.add(new JScrollPane(txHistoryTable));
        
        addPanel(viewTxTSPanel, viewTxTypePanel, 0, 0, 1, 1, 0, 0, new Insets(0,0,0,0),
        		GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addPanel(viewTxTSPanel, viewTxSinglePanel, 1, 0, 1, 1, 1, 0, new Insets(0,12,0,0),
        		GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addPanel(viewTxPanel, viewTxTSPanel, 0, 0, 1, 1, 1, 0, new Insets(5,0,0,0),
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
        addPanel(viewTxPanel, viewTxFromToPanel, 0, 1, 1, 1, 1, 0, new Insets(10,0,0,0),
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
        addPanel(viewTxPanel, viewTxTitlelPanel, 0, 2, 1, 1, 1, 0, new Insets(10,0,0,0),
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
        addPanel(viewTxPanel, viewTxHistoryPanel, 0, 3, 1, 1, 1, 1, new Insets(0,10,10,10),
        		GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        
        txCardPanel.add("txCard01", newTxPanel);
        txCardPanel.add("txCard02", viewTxPanel);
        
        addPanel(txPanel, custInfoPanel, 0, 0, 1, 1, 1, 0, new Insets(0,0,10,0),
        		GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addPanel(txPanel, txCardPanel, 0, 1, 1, 1, 1, 1, new Insets(0,0,0,0),
        		GridBagConstraints.SOUTH, GridBagConstraints.BOTH);
        
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
  	
  	// Number format validation
  	private boolean isNumeric(String str){   
  	    Pattern pattern = Pattern.compile("[0-9]*");   
  	    return pattern.matcher(str).matches();      
  	} 
  	
  	// Display the SQLException in a dialog box
  	private void saveToDBFailedDialog(SQLException e) {
		JOptionPane.showMessageDialog(Brokerage.this, 
				new String[] {
					"Save to database failed!",
					"Error message: ",
					"    \"" + e.getClass().getName() + ": ", 
					"     " + e.getMessage() + "\""});
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
    	textF_TX_SYMBOL.setText("");
    	textF_TX_PRICE.setText("");
    	textF_TX_QTY.setText("");
    	textF_TX_DEPOSIT.setText("");
  	}
  	
  	// Get data in JTable that user selected
  	private String[] getSelectedRowValue(JTable table) {
  		int row = table.getSelectedRow();
		int colCount = table.getColumnCount();
		String[] value = new String[colCount];
		for (int i = 0; i < colCount; i++) {
			value[i] = (String)table.getModel().getValueAt(row, i);
		}
  		return value;
  	}
  	
  	private CachedRowSet getContentsOfTable(String sql) throws SQLException {
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
  			crs.setCommand(sql);
  			crs.execute();
			
  		} catch (SQLException e) {
			displaySQLExceptionDialog(e);
		}
		return crs;
  	}
  	
  	private void createAccountTableModel() throws SQLException {
  		String sql = "SELECT CustomerName, CustomerId, OpeningDate, "
				+ "OpeningBalance FROM zhang_accounts";
		accountTableModel = new Account(getContentsOfTable(sql));
		accountTableModel.addEventHandlersToRowSet(new ListenForAccountRowSet());
		custListTable.setModel(accountTableModel);
	}
  	
    private void createStockQuotesTableModel() throws SQLException {
  		String sql = "SELECT StockQuotesId, StockSymbol, StockPrice "
        		+ "FROM zhang_stock_quotes";
  		stockQuotesTableModel = new Stock(getContentsOfTable(sql));
  		stockQuotesTable.setModel(stockQuotesTableModel);
  	}

    private void createCurrentPoTableModel(String custID) throws SQLException {
    	String sql = "SELECT t.StockSymbol, "
    			+ "SUM(CASE WHEN t.TransactionType = 'Buy' THEN t.Quantity ELSE 0 END) - "
    			+ "SUM(CASE WHEN t.TransactionType = 'Sell' THEN t.Quantity ELSE 0 END) AS Qty, "
    			+ "ROUND((SUM(CASE WHEN t.TransactionType = 'Buy' THEN t.Quantity ELSE 0 END) - "
    			+ "SUM(CASE WHEN t.TransactionType = 'Sell' THEN t.Quantity ELSE 0 END)) "
    			+ "* s.StockPrice, 2) AS Value "
    			+ "FROM zhang_transactions t, zhang_stock_quotes s "
    			+ "WHERE CustomerId = \"" + custID + "\" "
    			+ "AND t.StockSymbol = s.StockSymbol "
    			+ "AND t.TransactionType != 'Deposit' "
    			+ "GROUP BY t.StockSymbol";
  		poCurrentTableModel = new Portfolio(getContentsOfTable(sql));
  		poCurrentTable.setModel(poCurrentTableModel);
  	}
  	
  	private void createPoHistoryTableModel(String custID, int date) throws SQLException {
  		String sql = "SELECT s.SDate, t.StockSymbol, "
  				+ "SUM(CASE WHEN t.TransactionType = 'Buy' THEN t.Quantity ELSE 0 END) - "
  				+ "SUM(CASE WHEN t.TransactionType = 'Sell' THEN t.Quantity ELSE 0 END) "
  				+ "AS Qty, s.StockPrice, "
  				+ "ROUND((SUM(CASE WHEN t.TransactionType = 'Buy' THEN t.Quantity ELSE 0 END) - "
  				+ "SUM(CASE WHEN t.TransactionType = 'Sell' THEN t.Quantity ELSE 0 END)) "
  				+ "* s.StockPrice, 2) AS Value "
  				+ "FROM zhang_transactions t, zhang_stock_history s "
  				+ "WHERE CustomerId = \"" + custID + "\" "
  				+ "AND t.StockSymbol = s.StockSymbol "
  				+ "AND t.TransactionType != 'Deposit' "
  				+ "AND s.SDate = \"" + date + "\" "
  				+ "GROUP BY t.StockSymbol";
  		poHistoryTableModel = new Portfolio(getContentsOfTable(sql));
  		poHistoryTable.setModel(poHistoryTableModel);
  	}
  	
  	private void createTxTableModel(String custID, String constraint) throws SQLException {
  		String sql = "SELECT CustomerId, TransactionId, TransactionDate, TransactionType, "
        		+ "StockSymbol, Quantity, Price FROM zhang_transactions"
        		+ " WHERE CustomerId = \"" + custID + "\" " + constraint
        		+ " ORDER BY TransactionId";
  		txTableModel = new Transaction(getContentsOfTable(sql));
  		txTableModel.addEventHandlersToRowSet(new ListenForAccountRowSet());
  		txHistoryTable.setModel(txTableModel);
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
					createAccountTableModel();
				} catch (SQLException sqle) {
					displaySQLExceptionDialog(sqle);
				}
				// Reset all textfields
				resetTextField();
			} 
			if (e.getSource() == addCust) {
				mainCard.show(mainCardPanel, "mainCard01");
				custCard.show(custCardPanel, "custCard02");
			} 
			if (e.getSource() == button_NEW_TX) {
				txCard.show(txCardPanel, "txCard01");
				// Reset all textfields
				resetTextField();
			} 
			if (e.getSource() == button_TX_HISTORY) {
				txCard.show(txCardPanel, "txCard02");
				// Refresh data of transaction history
				try {
					createTxTableModel(id, "");
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} 
		}
    }
    
    // ActionListener for buttons
    private class ListenForButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// For "Save To Database" menu item
			if (e.getSource() == saveToDB) {
				saveToDB();
			}
			// For "Add Customer" button
			if (e.getSource() == button_ADD_CUST) {
				addCustomer();
			}
			// For "Buy" transaction button
			if (e.getSource() == button_TX_BUY) {
				buy();
			}
			// For "Sell" transaction button
			if (e.getSource() == button_TX_SELL) {
				sell();
			}
			// For "Sell All" transaction button
			if (e.getSource() == button_TX_SELL_ALL) {
				sellAll();
			}
			// For "Deposit" transaction button
			if (e.getSource() == button_TX_DEPOSIT) {
				deposit();
			}
			// For "Search" stock history button
			if (e.getSource() == button_PO_SEARCH) {
				int queryDate = poHistoryPicker.getIntDate();
				try {
					// Display stock history table after searching
					createPoHistoryTableModel(id, queryDate);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			// For "Search" transaction history by type button
			if (e.getSource() == button_TX_TYPE_S) {
				String type = (String)comBoBox_txType.getSelectedItem();
				String constraint = "";
				// Add constraint for the sql statement
				if (!type.equals("All")) {
					constraint = "AND TransactionType = \"" + type + "\"";
				}
				try {
					// Display tx history table after searching
					createTxTableModel(id, constraint);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			// For "Search" transaction history by single date button
			if (e.getSource() == button_TX_SINGLE_S) {
				int queryDate = txSinglePicker.getIntDate();
				// Add constraint for the sql statement
				String constraint = "AND TransactionDate = " + queryDate;
				try {
					// Display tx history table after searching
					createTxTableModel(id, constraint);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			// For "Search" transaction history by interval date button
			if (e.getSource() == button_TX_FROM_TO_S) {
				int startDate = txFromPicker.getIntDate();
				int endDate = txToPicker.getIntDate();
				// Add constraint for the sql statement
				String constraint 
					= "AND TransactionDate BETWEEN " + startDate + " AND " + endDate;
				try {
					// Display tx history table after searching
					createTxTableModel(id, constraint);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		private void saveToDB() {
			try {
				// Save changes into database
				accountTableModel.getAccountRowSet().acceptChanges();
				JOptionPane.showMessageDialog(Brokerage.this,
						"Save to database successfully!");
			} catch (SQLException sqle1) {
				saveToDBFailedDialog(sqle1);
				try {
					// Revert back changes
					createAccountTableModel();
				} catch (SQLException sqle2) {
					saveToDBFailedDialog(sqle2);
				}
			}
		}
		
		private void addCustomer() {
			// Not empty validation
			if (textF_CUST_NAME.getText().trim().length() != 0 && 
					textF_CUST_ID.getText().trim().length() != 0 && 
					textF_INIT_DEPOSIT.getText().trim().length() != 0) {
				// Number format validation
				if (isNumeric(textF_INIT_DEPOSIT.getText())) {
					JOptionPane.showMessageDialog(Brokerage.this, 
							new String[] {
								"New customer information:",
								"Customer Name:  " + textF_CUST_NAME.getText().trim(),
								"Customer ID:  " + textF_CUST_ID.getText().trim(),
								"Initial Deposit:  $" + textF_INIT_DEPOSIT.getText().trim()
							});
					accountTableModel.addCustomer(
							textF_CUST_NAME.getText().trim(),
							textF_CUST_ID.getText().trim(),
							Double.parseDouble(textF_INIT_DEPOSIT.getText().trim()));
					// Reset textfields after a transaction
					resetTextField();
				} else {
					JOptionPane.showMessageDialog(Brokerage.this,
							"Invalid deposit amount, please try again!");
					textF_INIT_DEPOSIT.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(Brokerage.this, 
						"Please enter all required value!");
			}
		}
		
		private void buy() {
			// Not empty validation
			if (textF_TX_SYMBOL.getText().trim().length() != 0 &&
					textF_TX_PRICE.getText().trim().length() != 0 &&
					textF_TX_QTY.getText().trim().length() != 0) {
				// Number format validation
				if (isNumeric(textF_TX_QTY.getText().trim()) && 
						Double.parseDouble(textF_TX_QTY.getText().trim()) > 0) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");		
					String today = format.format(new Date());
					int n = JOptionPane.showConfirmDialog(Brokerage.this,
							new String[] {
								"Customer Name:  " + name,
								"Customer ID:  " + id,
								"Transaction Date:  " + today,
								"Transaction fee:  $" + fee,
								"Stock Symbol:  " + textF_TX_SYMBOL.getText(),
								"Current Price:  $" + textF_TX_PRICE.getText(),
								"Purchase Quantity:  " + 
								Double.parseDouble(textF_TX_QTY.getText().trim())
							},
							"Transaction Confirmation", 
							JOptionPane.OK_CANCEL_OPTION);
					// Execute buy transaction if customer confirms
					if (n == 0) {
						// Add a new transaction into Transaction table
						double newBalance = txTableModel.addBuyTx(balance, fee, id, 
								Integer.parseInt(today), textF_TX_SYMBOL.getText(),
								Double.parseDouble(textF_TX_QTY.getText().trim()),
								Double.parseDouble(textF_TX_PRICE.getText()));
						// Validation for balance
						if (newBalance != balance) {
							Brokerage.this.balance = newBalance;
							// Update balance in Account table
							accountTableModel.updateBalance(id, balance);						
							JOptionPane.showMessageDialog(Brokerage.this,
									"Purchase " + Double.parseDouble(textF_TX_QTY.getText().trim())
									+ " shares of " + textF_TX_SYMBOL.getText() 
									+ " successfully!");
							// Refresh the balance label
							label_INFO_BALANCE.setText("Balance: $" + balance);
							// Reset textfields after a transaction
							resetTextField();
							try {
								// Refresh customer stock table
								createCurrentPoTableModel(id);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							JOptionPane.showMessageDialog(Brokerage.this,
									"Insufficient balance, please try again!");
							textF_TX_QTY.setText("");
						}
					}
				} else {
					JOptionPane.showMessageDialog(Brokerage.this,
							"Invalid quantity, please try again!");
					textF_TX_QTY.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(Brokerage.this, 
						"Please enter all required value!");
			}
		}
		
		private void sell() {
			// Not empty validation
			if (textF_TX_SYMBOL.getText().trim().length() != 0 &&
					textF_TX_PRICE.getText().trim().length() != 0 && 
					textF_TX_QTY.getText().trim().length() != 0) {
				// Number format validation
				if (isNumeric(textF_TX_QTY.getText().trim()) && 
						Double.parseDouble(textF_TX_QTY.getText().trim()) > 0) {
					// Check remaining stock
					double remainingQty = txTableModel.queryRemainingStock(
							id, textF_TX_SYMBOL.getText().trim());
					if (remainingQty > 0) {
						// Check sell quantity
						if (remainingQty >= 
								Double.parseDouble(textF_TX_QTY.getText().trim())) {
							SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");		
							String today = format.format(new Date());
							int n = JOptionPane.showConfirmDialog(Brokerage.this,
									new String[] {
										"Customer Name:  " + name,
										"Customer ID:  " + id,
										"Transaction Date:  " + today,
										"Transaction fee:  $" + fee,
										"Stock Symbol:  " + textF_TX_SYMBOL.getText(),
										"Current Price:  $" + textF_TX_PRICE.getText(),
										"Sell Quantity:  " + 
										Double.parseDouble(textF_TX_QTY.getText().trim())
									},
									"Transaction Confirmation", 
									JOptionPane.OK_CANCEL_OPTION);
							// Execute buy transaction if customer confirms
							if (n == 0) {
								double newBalance = txTableModel.addSellTx(balance, fee, id, 
										Integer.parseInt(today), textF_TX_SYMBOL.getText(),
										Double.parseDouble(textF_TX_QTY.getText().trim()),
										Double.parseDouble(textF_TX_PRICE.getText()));
								// Validation for balance
								if (newBalance != balance) {
									Brokerage.this.balance = newBalance;
									// Update balance in Account table
									accountTableModel.updateBalance(id, balance);
									JOptionPane.showMessageDialog(Brokerage.this,
											"Sell " + Double.parseDouble(textF_TX_QTY.getText().trim())
											+ " shares of " + textF_TX_SYMBOL.getText() 
											+ " successfully!");
									// Refresh the balance label
									label_INFO_BALANCE.setText("Balance: $" + balance);
									// Reset textfields after a transaction
									resetTextField();
									try {
										// Refresh customer stock table
										createCurrentPoTableModel(id);
									} catch (SQLException e) {
										e.printStackTrace();
									}
								} else {
									JOptionPane.showMessageDialog(Brokerage.this,
											"Sorry, you can't afford the transaction fee!");
									resetTextField();
								}
							}
						} else {
							JOptionPane.showMessageDialog(Brokerage.this,
									"Insufficient remaining quantity, please try again!");
							textF_TX_QTY.setText("");
						}
					} else {
						JOptionPane.showMessageDialog(Brokerage.this, 
								"Sorry, you don't have this stock!");
						resetTextField();
					}
				} else {
					JOptionPane.showMessageDialog(Brokerage.this,
							"Invalid quantity, please try again!");
					textF_TX_QTY.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(Brokerage.this, 
						"Please enter all required value!");
			}
		}
		
		private void sellAll() {
			// Not empty validation
			if (textF_TX_SYMBOL.getText().trim().length() != 0 &&
					textF_TX_PRICE.getText().trim().length() != 0) {
				// Check remaining stock
				double remainingQty = txTableModel.queryRemainingStock(
						id, textF_TX_SYMBOL.getText().trim());
				if (remainingQty > 0) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");		
					String today = format.format(new Date());
					int n = JOptionPane.showConfirmDialog(Brokerage.this,
							new String[] {
								"Customer Name:  " + name,
								"Customer ID:  " + id,
								"Transaction Date:  " + today,
								"Transaction fee:  $" + fee,
								"Stock Symbol:  " + textF_TX_SYMBOL.getText(),
								"Current Price:  $" + textF_TX_PRICE.getText(),
								"Remaining Quantity:  " + remainingQty,
								"Are you sure about selling all of your " 
										+ textF_TX_SYMBOL.getText() + " ?"
							},
							"Transaction Confirmation", 
							JOptionPane.OK_CANCEL_OPTION);
					// Execute buy transaction if customer confirms
					if (n == 0) {
						double newBalance = txTableModel.addSellTx(balance, fee, id, 
								Integer.parseInt(today), textF_TX_SYMBOL.getText(),
								remainingQty, Double.parseDouble(textF_TX_PRICE.getText()));
						// Validation for balance
						if (newBalance != balance) {
							Brokerage.this.balance = newBalance;
							// Update balance in Account table
							accountTableModel.updateBalance(id, balance);
							JOptionPane.showMessageDialog(Brokerage.this,
									"Sell all " + textF_TX_SYMBOL.getText() + " successfully!");
							// Refresh the balance label
							label_INFO_BALANCE.setText("Balance: $" + balance);
							// Reset textfields after a transaction
							resetTextField();
							try {
								// Refresh customer stock table
								createCurrentPoTableModel(id);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						} else {
							JOptionPane.showMessageDialog(Brokerage.this,
									"Sorry, you can't afford the transaction fee!");
							resetTextField();
						}
					}
				} else {
					JOptionPane.showMessageDialog(Brokerage.this, 
							"Sorry, you don't have this stock!");
				}
			} else {
				JOptionPane.showMessageDialog(Brokerage.this, 
						"Please enter all required value!");
			}
		}
		
		private void deposit() {
			// Not empty validation
			if (textF_TX_DEPOSIT.getText().trim().length() != 0) {
				// Number format validation
				if (isNumeric(textF_TX_DEPOSIT.getText().trim()) && 
						Double.parseDouble(textF_TX_DEPOSIT.getText().trim()) > 0) {
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");		
					String today = format.format(new Date());
					int n = JOptionPane.showConfirmDialog(Brokerage.this,
							new String[] {
								"Customer Name:  " + name,
								"Customer ID:  " + id,
								"Transaction Date:  " + today,
								"Deposit Amount:  $" + textF_TX_DEPOSIT.getText().trim()
							},
							"Transaction Confirmation", 
							JOptionPane.OK_CANCEL_OPTION);
					// Execute deposit transaction if customer confirms
					if (n == 0) {
						// Add a new transaction into Transaction table
						Brokerage.this.balance = txTableModel.addDepositTx(
								balance, id, Integer.parseInt(today),
								Double.parseDouble(textF_TX_DEPOSIT.getText().trim()));
						// Update balance in Account table
						accountTableModel.updateBalance(id, balance);
						JOptionPane.showMessageDialog(Brokerage.this,
								"Deposit successfully!");
						// Refresh the balance label
						label_INFO_BALANCE.setText("Balance: $" + balance);
						// Reset textfields after inserting a row
						resetTextField();
					}
				} else {
					JOptionPane.showMessageDialog(Brokerage.this,
							"Invalid deposit amount, please try again!");
					textF_TX_DEPOSIT.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(Brokerage.this, 
						"Please enter your deposit amount!");
			}
		}

    }
    
    private class ListenForAccountRowSet implements RowSetListener {

		@Override
		public void rowChanged(RowSetEvent event) {
			// For "Customer List" JTable
			if (event.getSource() == accountTableModel.getAccountRowSet()) {
				CachedRowSet currentRowSet = accountTableModel.getAccountRowSet();
				try {
					currentRowSet.moveToCurrentRow();
					accountTableModel = new Account(accountTableModel.getAccountRowSet());
					custListTable.setModel(accountTableModel);
				} catch (SQLException sqle) {
					displaySQLExceptionDialog(sqle);
				}
			}
			// For "Transaction History" JTable
			if (event.getSource() == txTableModel.getTxRowSet()) {
				CachedRowSet currentRowSet = txTableModel.getTxRowSet();
				try {
					currentRowSet.moveToCurrentRow();
					txTableModel = new Transaction(txTableModel.getTxRowSet());
					txHistoryTable.setModel(txTableModel);
				} catch (SQLException sqle) {
					displaySQLExceptionDialog(sqle);
				}
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
			if (e.getSource() == custListTable) {
				// Double click a record in customer list table then
				// go to transaction page with this customer's data 
				if (e.getClickCount() == 2) {
					// Get value of the selected row
					String[] custInfo = getSelectedRowValue(custListTable);
					if (!custInfo[0].equals(
							"java.sql.SQLException: absolute : Invalid cursor position")) {
						// Get information of current customer
						Brokerage.this.name = custInfo[0];
						Brokerage.this.id = custInfo[1];
						Brokerage.this.balance = Double.parseDouble(custInfo[3]);
						// Display selected customer information on transaction page
						label_INFO_NAME.setText("Name: " + name);
						label_INFO_ID.setText("ID: " + id);
						label_INFO_OPDATE.setText("Opening Date: " + custInfo[2]);
						label_INFO_BALANCE.setText("Balance: $" + balance);
						int today = poHistoryPicker.getIntDate();
						try {
							createCurrentPoTableModel(id);  // Reconfigure poCurrentTable
					        createPoHistoryTableModel(id, today);  // Reconfigure poHistoryTable
					        createTxTableModel(id, "");  // Reconfigure txHistoryTable
						} catch (SQLException sqle) {
							displaySQLExceptionDialog(sqle);
						}
						// Reset all textfields 
						resetTextField();
						mainCard.show(mainCardPanel, "mainCard02");
						txCard.show(txCardPanel, "txCard01");
					} 
				}
			}
			if (e.getSource() == stockQuotesTable) {
				if (e.getClickCount() == 1) {
					// Get value of the selected row
					String[] stockInfo = getSelectedRowValue(stockQuotesTable);
					if (!stockInfo[0].equals(
							"java.sql.SQLException: absolute : Invalid cursor position")) {
						// Set these value to corresponding textfields
				        textF_TX_SYMBOL.setText(stockInfo[0]);
				        textF_TX_PRICE.setText(stockInfo[1]);
					}
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
			
			// Database initialization
			mySqlConnection.createTables();  // Create all tables
			mySqlConnection.populateTables();  // Insert data into tables
			
			Brokerage brokerage = new Brokerage(mySqlConnection);
			brokerage.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
