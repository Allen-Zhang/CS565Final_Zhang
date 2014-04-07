package cs565.finals;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Brokerage extends JFrame {
	
	private static double fee;
	
	private static final int WIDTH = 500;
    private static final int HEIGHT = 550;
	
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

    private JTextField textF_CUST_NAME, textF_CUST_ID, textF_INIT_DEPOSIT;
	
    private JButton button_ADD_CUST;

    private CardLayout mainCard = new CardLayout();
    private CardLayout custCard = new CardLayout();
    
    public Brokerage() {
    	super("MET Broker");
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
        mainCardPanel.setLayout(mainCard);  // Set mainPanel to be CardLayout
        
        /*----- 2 Main Page Menu Part -----*/
        
        loadFromDB = new JMenuItem("Load From Database");
        addCust = new JMenuItem("Add Customer");
        saveToDB = new JMenuItem("Save To Database");
        
        ListenForCardPanel lForCardPanel = new ListenForCardPanel();
        
        loadFromDB.addActionListener(lForCardPanel);
        addCust.addActionListener(lForCardPanel);
//        saveToDB.addActionListener(this);
        
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

        listCustPanel = new JPanel();   
        
        custCardPanel = new JPanel();
        custCardPanel.setLayout(custCard);  // Set custCardPanel to be CardLayout
        
        listCustPanel.setBackground(Color.CYAN);
        
        
        /*----- 1.1.1 Welcome Customer Panel Part -----*/
        
        welcomeCustPanel = new JPanel();
        welcomeCustPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 75));
        
        Font font_WELCOME = new Font("Serif", Font.BOLD, 30);
        JLabel label_WELCOME = new JLabel("Welcome to MET Broker!");
        label_WELCOME.setFont(font_WELCOME);
        label_WELCOME.setForeground(Color.BLUE);
        welcomeCustPanel.add(label_WELCOME);
        
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
    	
    	addComp(addCustPanel, label_CUST_NAME, 0, 0, 1, 1, new Insets(7,0,0,0),
    			GridBagConstraints.LINE_END, GridBagConstraints.NONE);
    	addComp(addCustPanel, textF_CUST_NAME, 1, 0, 2, 1, new Insets(7,10,0,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	addComp(addCustPanel, label_CUST_ID, 0, 1, 1, 1, new Insets(0,0,0,22),
    			GridBagConstraints.LINE_END, GridBagConstraints.NONE);
    	addComp(addCustPanel, textF_CUST_ID, 1, 1, 2, 1, new Insets(0,10,0,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	addComp(addCustPanel, label_INIT_DEPOSIT, 0, 2, 1, 1, new Insets(0,0,0,15),
    			GridBagConstraints.LINE_END, GridBagConstraints.NONE);
    	addComp(addCustPanel, textF_INIT_DEPOSIT, 1, 2, 2, 1, new Insets(0,10,0,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	addComp(addCustPanel, button_ADD_CUST, 1, 3, 2, 1, new Insets(0,62,7,0),
    			GridBagConstraints.LINE_START, GridBagConstraints.NONE);
    	
        /*----- 1.2 Customer Transaction Panel Part -----*/
                
        txPanel = new JPanel();
        
        
        
        custCardPanel.add("custCard01", welcomeCustPanel);
        custCardPanel.add("custCard02", addCustPanel);
        
        addPanel(custPanel, listCustPanel, 0, 0, 1, 1, 1, 1,
        		GridBagConstraints.CENTER, GridBagConstraints.BOTH);
        addPanel(custPanel, custCardPanel, 0, 1, 1, 1, 1, 0, 
        		GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL);
        
        mainCardPanel.add("mainCard01", custPanel);
        mainCardPanel.add("mainCard02", txPanel);
        
        add(toolBar, BorderLayout.NORTH);
        add(mainCardPanel, BorderLayout.CENTER);
        
    } 

    // Set the rules for a panel destined for 
 	// a GridBagLayout and then add it 
 	private void addPanel(JPanel thePanel, JPanel subPanel, int xPos, int yPos, 
 			int gridW, int gridH, double weightX, double weightY, int place, int stretch){
 		
 		GridBagConstraints gridConstraints = new GridBagConstraints();
 		
 		gridConstraints.gridx = xPos;
 		gridConstraints.gridy = yPos;
 		gridConstraints.gridwidth = gridW;
 		gridConstraints.gridheight = gridH;
 		gridConstraints.weightx = weightX;
 		gridConstraints.weighty = weightY;
 		gridConstraints.anchor = place;
 		gridConstraints.fill = stretch;
 		
 		thePanel.add(subPanel, gridConstraints);
 	}
 	
 	// Set the rules for a component destined for 
  	// a GridBagLayout and then add it 
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
    
 	// ActionListener for swap card panels
    private class ListenForCardPanel implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == loadFromDB) {
				mainCard.show(mainCardPanel, "mainCard01");
				custCard.show(custCardPanel, "custCard01");
			} 
			if (e.getSource() == addCust) {
				custCard.show(custCardPanel, "custCard02");
			} 
		}
    }
    
    
	public static void main(String[] args) {
		Brokerage brokerage = new Brokerage();
		brokerage.setVisible(true);

	}



}
