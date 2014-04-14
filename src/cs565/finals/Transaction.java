package cs565.finals;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class Transaction implements TableModel{

	private CachedRowSet txRowSet;
	private ResultSetMetaData metadata;
	private int numcols, numrows;
	
	public Transaction(CachedRowSet rowSetArg) throws SQLException {
		
		this.txRowSet = rowSetArg;
		this.metadata = this.txRowSet.getMetaData();
		// Ignore CustomerID and TransactionID columns
		numcols = metadata.getColumnCount() - 2;
		
		// Retrieve the number of rows
		this.txRowSet.beforeFirst();
		this.numrows = 0;
		while (this.txRowSet.next()) {
			this.numrows ++;
		}
		this.txRowSet.beforeFirst();
	}
	
	public CachedRowSet getTxRowSet() {
		return txRowSet;
	}
	
	public String generateTxId(String custID) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");		
		String prefix = format.format(new Date());
		String txID = prefix + custID;
		return txID;
	}
	
	public double addDepositTx(double balance, String custID, 
			int date, double price) {
		// Generate transaction id
		String txID = generateTxId(custID);
		try {
			this.txRowSet.moveToInsertRow();
			this.txRowSet.updateString("TransactionId", txID);
			this.txRowSet.updateString("CustomerId", custID);
			this.txRowSet.updateInt("TransactionDate", date);
			this.txRowSet.updateString("TransactionType", "Deposit");
			this.txRowSet.updateNull("StockSymbol");
			this.txRowSet.updateNull("Quantity");
			this.txRowSet.updateDouble("Price", price);
			this.txRowSet.insertRow();
			this.txRowSet.moveToCurrentRow();
			// Synchronize changes back to the DB
			this.txRowSet.acceptChanges();  
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return balance + price;  // Return new balance
	}
	
	public double addBuyTx(double balance, double fee, String custID, 
			int date, String symbol, double qty, double price) {
		// Validation for balance
		if (balance >= qty * price + fee) {
			// Generate transaction id
			String txID = generateTxId(custID);
			try {
				this.txRowSet.moveToInsertRow();
				this.txRowSet.updateString("TransactionId", txID);
				this.txRowSet.updateString("CustomerId", custID);
				this.txRowSet.updateInt("TransactionDate", date);
				this.txRowSet.updateString("TransactionType", "Buy");
				this.txRowSet.updateString("StockSymbol", symbol);
				this.txRowSet.updateDouble("Quantity", qty);
				this.txRowSet.updateDouble("Price", price);
				this.txRowSet.insertRow();
				this.txRowSet.moveToCurrentRow();
				// Synchronize changes back to the DB
				this.txRowSet.acceptChanges(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return balance - qty * price - fee;  // Return new balance
		} else {
			return balance;  // Return old balance
		}
	}
	
	public double queryRemainingStock(String custID, String stockSymbol) {
		double buyQty = 0;
		double sellQty = 0;
		try {
			this.txRowSet.beforeFirst();
			while (this.txRowSet.next()) {
				// Customer identification
				if (this.txRowSet.getString("CustomerId").equals(custID)) {
					// Calculate the quantity of this stock that customer brought
					if (this.txRowSet.getString("TransactionType").equals("Buy")
							&& this.txRowSet.getString("StockSymbol").equals(stockSymbol)) {
						buyQty += this.txRowSet.getDouble("Quantity");
					}
					// Calculate the quantity of this stock that customer sold
					if (this.txRowSet.getString("TransactionType").equals("Sell")
							&& this.txRowSet.getString("StockSymbol").equals(stockSymbol)) {
						sellQty += this.txRowSet.getDouble("Quantity");	
					}
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return buyQty - sellQty;  // Return remaining quantity
	}
	
	public double addSellTx(double balance, double fee, String custID, 
			int date, String symbol, double qty, double price) {
		// Validation for balance
		if (balance + qty * price > fee) {
			// Generate transaction id
			String txID = generateTxId(custID);
			try {
				this.txRowSet.moveToInsertRow();
				this.txRowSet.updateString("TransactionId", txID);
				this.txRowSet.updateString("CustomerId", custID);
				this.txRowSet.updateInt("TransactionDate", date);
				this.txRowSet.updateString("TransactionType", "Sell");
				this.txRowSet.updateString("StockSymbol", symbol);
				this.txRowSet.updateDouble("Quantity", qty);
				this.txRowSet.updateDouble("Price", price);
				this.txRowSet.insertRow();
				this.txRowSet.moveToCurrentRow();
				// Synchronize changes back to the DB
				this.txRowSet.acceptChanges(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return balance + qty * price - fee;  // Return new balance
		} else {
			return balance;  // Return old balance
		}
	}
	
	public void addEventHandlersToRowSet(RowSetListener listener) {
		this.txRowSet.addRowSetListener(listener);
	}

	@Override
	public int getRowCount() {
		return numrows;
	}

	@Override
	public int getColumnCount() {
		return numcols;
	}

	@Override
	public String getColumnName(int columnIndex) {
		String[] colName = new String[columnIndex];
		// For transaction history table
		if (numcols == 5) {
			colName = new String[]{"Date", "Type", "Symbol", "Quantity", "Price ($)"};
		} 
		// For "your stock" table
		else if (numcols == 2) {
			colName = new String[]{"Symbol", "Quantity"};
		}
		return colName[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			// SQL starts numbering its rows and columns at 1,
			// but the TableModel interface starts at 0
			this.txRowSet.absolute(rowIndex + 1);
			// Ignore CustomerID and TransactionID columns, so start from the the third column
			Object o = this.txRowSet.getObject(columnIndex + 3);
			if (o == null)
				return null;
			else
				return o.toString();
		} catch (SQLException e) {
			return e.toString();
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		System.out.println("Calling setValueAt row " + rowIndex + 
				", column " + columnIndex);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {}

	@Override
	public void removeTableModelListener(TableModelListener l) {}

}
