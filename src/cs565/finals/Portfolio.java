package cs565.finals;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class Portfolio implements TableModel{

	private CachedRowSet poRowSet;
	private ResultSetMetaData metadata;
	private int numcols, numrows;
	
	public Portfolio(CachedRowSet rowSetArg) throws SQLException {
		
		this.poRowSet = rowSetArg;
		this.metadata = this.poRowSet.getMetaData();
		numcols = metadata.getColumnCount();
		
		// Retrieve the number of rows
		this.poRowSet.beforeFirst();
		this.numrows = 0;
		while (this.poRowSet.next()) {
			this.numrows ++;
		}
		this.poRowSet.beforeFirst();
	}
	
	public CachedRowSet getpoRowSet() {
		return poRowSet;
	}
	
	public void addEventHandlersToRowSet(RowSetListener listener) {
		this.poRowSet.addRowSetListener(listener);
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
		String[] colName = null;
		if (numcols == 3) {
			colName = new String[]{"Symbol", "Quantity", "Value ($)"};
		}
		if (numcols == 5) {
			colName = new String[]{"Date", "Symbol", "Quantity", "Price ($)", "Value ($)"};
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
			this.poRowSet.absolute(rowIndex + 1);
			Object o = this.poRowSet.getObject(columnIndex + 1);
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
