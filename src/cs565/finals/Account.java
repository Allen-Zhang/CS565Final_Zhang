package cs565.finals;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class Account implements TableModel{

	private CachedRowSet accountRowSet;
	private ResultSetMetaData metadata;
	private int numcols, numrows;
	
	public Account(CachedRowSet rowSetArg) throws SQLException {
		
		this.accountRowSet = rowSetArg;
		this.metadata = this.accountRowSet.getMetaData();
		numcols = metadata.getColumnCount();
		
		// Retrieve the number of rows
		this.accountRowSet.beforeFirst();
		this.numrows = 0;
		while (this.accountRowSet.next()) {
			this.numrows ++;
		}
		this.accountRowSet.beforeFirst();
	}
	
	public CachedRowSet getAccountRowSet() {
		return accountRowSet;
	}
	
	public void addCustomer(String custName, String custID, double initDeposit) {
		
		SimpleDateFormat todayFormat = new SimpleDateFormat("yyyyMMdd");		
		int openingDate = Integer.parseInt(todayFormat.format(new Date()));
		
		try {
			this.accountRowSet.moveToInsertRow();
			this.accountRowSet.updateString("CustomerId", custID);
			this.accountRowSet.updateString("CustomerName", custName);
			this.accountRowSet.updateInt("OpeningDate", openingDate);
			this.accountRowSet.updateDouble("OpeningBalance", initDeposit);
			this.accountRowSet.insertRow();
			this.accountRowSet.moveToCurrentRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateBalance(String custID, double newBalance) {
		try {
			this.accountRowSet.beforeFirst();
			while (this.accountRowSet.next()) {
				if (this.accountRowSet.getString("CustomerId").equals(custID)) {
					this.accountRowSet.updateDouble("OpeningBalance", newBalance);
					this.accountRowSet.updateRow();
					// Synchronize changes back to the DB
					this.accountRowSet.acceptChanges();
					break;
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addEventHandlersToRowSet(RowSetListener listener) {
		this.accountRowSet.addRowSetListener(listener);
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
		// SQL starts numbering its rows and columns at 1,
		// but the TableModel interface starts at 0
//		try {
//			return this.metadata.getColumnLabel(columnIndex + 1);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		String[] colName = {"Customer Name", "Customer ID", "Opening Date", "Current Balance"};
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
			this.accountRowSet.absolute(rowIndex + 1);
			Object o = this.accountRowSet.getObject(columnIndex + 1);
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
