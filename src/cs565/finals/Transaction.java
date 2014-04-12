package cs565.finals;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class Transaction implements TableModel{

	private CachedRowSet stockRowSet;
	private ResultSetMetaData metadata;
	private int numcols, numrows;
	
	public Transaction(CachedRowSet rowSetArg) throws SQLException {
		
		this.stockRowSet = rowSetArg;
		this.metadata = this.stockRowSet.getMetaData();
		// Ignore id column
		numcols = metadata.getColumnCount() - 1;
		
		// Retrieve the number of rows
		this.stockRowSet.beforeFirst();
		this.numrows = 0;
		while (this.stockRowSet.next()) {
			this.numrows ++;
		}
		this.stockRowSet.beforeFirst();
	}
	
	public CachedRowSet getstockRowSet() {
		return stockRowSet;
	}
	
	public void addEventHandlersToRowSet(RowSetListener listener) {
		this.stockRowSet.addRowSetListener(listener);
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
		String[] colName = {"Date", "Type", "Symbol", "Quantity", "Price"};
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
			this.stockRowSet.absolute(rowIndex + 1);
			// Ignore id column, so start from the the second column
			Object o = this.stockRowSet.getObject(columnIndex + 2);
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
