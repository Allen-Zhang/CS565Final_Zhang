package cs565.finals;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.*;


public class Brokerage extends JFrame {
	
	private static double fee;
	private static final int WIDTH = 750;
    private static final int HEIGHT = 570;
    
	private MySqlConnection dbConn;
	private Account accountTableModel;
	private Stock stockQuotesTableModel;
	private Stock stockHistoryTableModel;
	private Transaction txTableModel;

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
    private JLabel label_INFO_NAME, label_INFO_ID, label_INFO_OPDATE, label_INFO_BALANCE;
    private JTable custListTable, poStockTable, poCurrentTable, poHistoryTable,
		txHistoryTable;
    private JTextField textF_CUST_NAME, textF_CUST_ID, textF_INIT_DEPOSIT, 
    	textF_TX_SYMBOL, textF_TX_PRICE, textF_TX_QTY, textF_TX_DEPOSIT;
    private JButton button_ADD_CUST, button_NEW_TX, button_TX_HISTORY,
    	button_TX_BUY, button_TX_SELL, button_TX_DEPOSIT,
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
        // Set CardLayout for mainPanel 
        mainCardPanel.setLayout(mainCard);  
        
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
        
        String sql = "SELECT CustomerName, CustomerId, OpeningDate, "
				+ "OpeningBalance FROM zhang_accounts";
        CachedRowSet crsOfAccountTable = getContentsOfTable(sql);
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
        label_INFO_BALANCE = new JLabel("Balance: $60000");
        
        Font font_CUST_INFO = new Font("SansSerif", Font.BOLD, 15);
        label_INFO_NAME.setFont(font_CUST_INFO);
        label_INFO_ID.setFont(font_CUST_INFO);
        label_INFO_OPDATE.setFont(font_CUST_INFO);
        label_INFO_BALANCE.setFont(font_CUST_INFO);
        
        button_NEW_TX = new JButton("  Make New Transactions ");
        button_TX_HISTORY = new JButton("View Transaction History");
        // Add listeners for new_tx and tx_history buttons
        button_NEW_TX.addActionListener(lForCardPanel);
        button_TX_HISTORY.addActionListener(lForCardPanel);
        
        addComp(custInfoPanel, label_INFO_NAME, 0, 0, 1, 1, new Insets(10,10,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, label_INFO_ID, 1, 0, 1, 1, new Insets(10,10,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, label_INFO_OPDATE, 2, 0, 1, 1, new Insets(10,10,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, label_INFO_BALANCE, 3, 0, 1, 1, new Insets(10,10,0,0),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE);
        addComp(custInfoPanel, button_NEW_TX, 0, 1, 2, 1, new Insets(10,10,5,0),
    			GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL);
        addComp(custInfoPanel, button_TX_HISTORY, 2, 1, 2, 1, new Insets(10,10,5,10),
    			GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL);
        
        txCardPanel = new JPanel();
        // Set CardLayout for txCardPanel
        txCardPanel.setLayout(txCard);
        
        /*----- 1.2.2.1 New Transaction Panel -----*/
        
        newTxPanel = new JPanel();
        newTxPanel.setLayout(new GridBagLayout());
        
        JPanel newTxPortfolioPanel = new JPanel();
        newTxPortfolioPanel.setLayout(new GridBagLayout());
        
        JPanel custStockPanel = new JPanel();
        JPanel currentPoPanel = new JPanel();
        JPanel poHistoryPanel = new JPanel();
        
        custStockPanel.setLayout(new BorderLayout());
        currentPoPanel.setLayout(new BorderLayout());
        poHistoryPanel.setLayout(new BorderLayout());
        
        JLabel label_PO_STOCK = new JLabel("    Your Stock    ");
        JLabel label_PO_CURRENT = new JLabel("Current Portfolio");
        JLabel label_PO_HISTORY = new JLabel("Portfolio History");
        
        label_PO_STOCK.setHorizontalAlignment(SwingConstants.CENTER);
        label_PO_CURRENT.setHorizontalAlignment(SwingConstants.CENTER);
        label_PO_HISTORY.setHorizontalAlignment(SwingConstants.CENTER);
        
        Font font_PO = new Font("SansSerif", Font.BOLD, 15);
        label_PO_STOCK.setFont(font_PO);
        label_PO_CURRENT.setFont(font_PO);
        label_PO_HISTORY.setFont(font_PO);
        
        // "Your Stock" table part
        poStockTable = new JTable();
//      poStockTable.setModel();
        poStockTable.setFillsViewportHeight(true);
        
        // "Current Portfolio" table part
        sql = "SELECT StockQuotesId, StockSymbol, StockPrice "
        		+ "FROM zhang_stock_quotes";
        CachedRowSet crsOfStockQuotesTable = getContentsOfTable(sql);
        stockQuotesTableModel = new Stock(crsOfStockQuotesTable);
//        stockQuotesTableModel.addEventHandlersToRowSet(new ListenForAccountRowSet());
        
        poCurrentTable = new JTable();
        poCurrentTable.setModel(stockQuotesTableModel);
        poCurrentTable.setFillsViewportHeight(true);
        poCurrentTable.addMouseListener(new ListenForMouse());
        poCurrentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // "Portfolio History" table part
        sql = "SELECT StockHistoryId, StockSymbol, StockPrice, SDate "
				+ "FROM zhang_stock_history";
        CachedRowSet crsOfStockHistoryTable = getContentsOfTable(sql);
        stockHistoryTableModel = new Stock(crsOfStockHistoryTable);
        
        poHistoryTable = new JTable();
        poHistoryTable.setModel(stockHistoryTableModel);
        poHistoryTable.setFillsViewportHeight(true);
        
        button_PO_SEARCH = new JButton("  Search  ");
        poHistoryPicker = new DatePicker(poHistoryPanel, 42);
        
        JPanel poHistorySearchPanel = new JPanel();
        poHistorySearchPanel.setLayout(new BorderLayout());
        poHistorySearchPanel.add(poHistoryPicker, BorderLayout.WEST);
        poHistorySearchPanel.add(button_PO_SEARCH, BorderLayout.EAST);
        
        custStockPanel.add(label_PO_STOCK, BorderLayout.NORTH);
        custStockPanel.add(new JScrollPane(poStockTable), BorderLayout.CENTER);
        currentPoPanel.add(label_PO_CURRENT, BorderLayout.NORTH);
        currentPoPanel.add(new JScrollPane(poCurrentTable), BorderLayout.CENTER);
        poHistoryPanel.add(label_PO_HISTORY, BorderLayout.NORTH);
        poHistoryPanel.add(new JScrollPane(poHistoryTable), BorderLayout.CENTER);
        poHistoryPanel.add(poHistorySearchPanel, BorderLayout.SOUTH);
        
        addPanel(newTxPortfolioPanel, custStockPanel, 0, 0, 1, 1, 1, 1, new Insets(
        		0,0,0,10), GridBagConstraints.WEST, GridBagConstraints.BOTH);
        addPanel(newTxPortfolioPanel, currentPoPanel, 1, 0, 1, 1, 1, 1, new Insets(
        		0,0,0,0), GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addPanel(newTxPortfolioPanel, poHistoryPanel, 2, 0, 1, 1, 0.65, 1, new Insets(
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
        
        button_TX_BUY = new JButton("Buy"); 
        button_TX_SELL = new JButton("Sell");
        button_TX_DEPOSIT = new JButton("Deposit");
        
        JPanel newTxBuyAndSellPanel = new JPanel();
        newTxBuyAndSellPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        newTxBuyAndSellPanel.setBorder(BorderFactory.createTitledBorder(
        		"Buy and Sell Transaction"));
        JPanel newTxDepositPanel = new JPanel();
        newTxDepositPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,8));
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
        
        viewTxTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT,12,0));
        viewTxSinglePanel.setLayout(new FlowLayout(FlowLayout.LEFT,12,0));
        viewTxTSPanel.setLayout(new GridBagLayout());
        viewTxFromToPanel.setLayout(new FlowLayout(FlowLayout.LEFT,12,0));
        viewTxTitlelPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
        viewTxHistoryPanel.setLayout(new BorderLayout());
        
        JLabel label_TX_TYPE = new JLabel("Transaction Type:");
        JLabel label_TX_SINGLE = new JLabel("Single Date:");
        JLabel label_TX_FROM_TO = new JLabel("From Date To Date:    From");
        JLabel label_TX_TO = new JLabel("To");
        JLabel label_TX_TITLE = new JLabel("Transaction History Table");
        label_TX_TITLE.setFont(font_CUST_INFO);
        
        button_TX_TYPE_S = new JButton("Search");
        button_TX_SINGLE_S = new JButton("Search");
        button_TX_FROM_TO_S = new JButton("Search");
        
        txSinglePicker = new DatePicker(viewTxSinglePanel, 50);
        txFromPicker = new DatePicker(viewTxFromToPanel, 50);
        txToPicker = new DatePicker(viewTxFromToPanel, 50);
        
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
        
        sql = "SELECT CustomerId, TransactionDate, TransactionType, "
        		+ "StockSymbol, Quantity, Price FROM zhang_transactions";
        CachedRowSet crsOfTxTable = getContentsOfTable(sql);
        txTableModel = new Transaction(crsOfTxTable);
//        stockQuotesTableModel.addEventHandlersToRowSet(new ListenForAccountRowSet());
        
        txHistoryTable = new JTable();
        txHistoryTable.setModel(txTableModel);
        txHistoryTable.setFillsViewportHeight(true);
        
        viewTxHistoryPanel.setLayout(new BorderLayout());
        viewTxHistoryPanel.add(new JScrollPane(txHistoryTable));
        
        addPanel(viewTxTSPanel, viewTxTypePanel, 0, 0, 1, 1, 0.25, 0, new Insets(0,0,0,0),
        		GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        addPanel(viewTxTSPanel, viewTxSinglePanel, 1, 0, 1, 1, 1, 0, new Insets(0,0,0,0),
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
    	textF_TX_SYMBOL.setText("");
    	textF_TX_PRICE.setText("");
    	textF_TX_QTY.setText("");
    	textF_TX_DEPOSIT.setText("");
  	}
  	
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
  	
  	private void createNewAccountTableModel() throws SQLException {
  		String sql = "SELECT CustomerName, CustomerId, OpeningDate, "
				+ "OpeningBalance FROM zhang_accounts";
		accountTableModel = new Account(getContentsOfTable(sql));
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
				mainCard.show(mainCardPanel, "mainCard01");
				custCard.show(custCardPanel, "custCard02");
			} 
			if (e.getSource() == button_NEW_TX) {
				txCard.show(txCardPanel, "txCard01");
			} 
			if (e.getSource() == button_TX_HISTORY) {
				txCard.show(txCardPanel, "txCard02");
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
			if (e.getSource() == custListTable) {
				// Double click a record in customer list table then
				// go to transaction page with this customer's data 
				if (e.getClickCount() == 2) {
					// Get value of the selected row
					String[] custInfo = getSelectedRowValue(custListTable);
					if (!custInfo[0].equals(
							"java.sql.SQLException: absolute : Invalid cursor position")) {
						// Display selected customer information on transaction page
						label_INFO_NAME.setText("Name: " + custInfo[0]);
						label_INFO_ID.setText("ID: " + custInfo[1]);
						label_INFO_OPDATE.setText("Opening Date: " + custInfo[2]);
						// Reset all textfields 
						resetTextField();
						mainCard.show(mainCardPanel, "mainCard02");
						txCard.show(txCardPanel, "txCard01");
					} 
				}
			}
			if (e.getSource() == poCurrentTable) {
				if (e.getClickCount() == 1) {
					// Get value of the selected row
					String[] stockInfo = getSelectedRowValue(poCurrentTable);
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
			Brokerage brokerage = new Brokerage(mySqlConnection);
			brokerage.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
