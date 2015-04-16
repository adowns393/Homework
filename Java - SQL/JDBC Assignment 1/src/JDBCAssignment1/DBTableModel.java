//This class handles the processing of ResultSet objects so the results can be displayed
//in a JTable.
package JDBCAssignment1;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class DBTableModel extends AbstractTableModel{
		private Vector <String> columnNames;
	    private Vector <Object> data;
	    private static final long serialVersionUID = 1L;
	 
	    public DBTableModel(ResultSet rs)
	    {
	        columnNames = new Vector <String>(); //names of columns
	        data = new Vector <Object>();  //rows of data
	        setResultSet(rs);
	    }
	    public void setResultSet(ResultSet rs)
	    {
	        try
	        {
	            ResultSetMetaData rsMetaData = rs.getMetaData();
	            int columns = rsMetaData.getColumnCount();
	            for (int i = 1; i <= columns; i++)
	            {
	                columnNames.addElement(rsMetaData.getColumnName(i));
	            }
	            while (rs.next())
	            {
	                Vector<Object> row = new Vector<Object>(columns);
	                for (int i = 1; i <= columns; i++)
	                {
	                    row.addElement(rs.getObject(i));
	                }
	                data.addElement(row);
	            }
	            
	        }
	        catch ( SQLException e )
	        {
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	            System.exit(-1);
	        }
	    }          
	    @Override
	    public int getRowCount()
	    {
	        if (data == null)
	            return 0;
	        else
	            return data.size();
	    }
	 
	    @Override
	    public int getColumnCount()
	    {
	        if (columnNames == null)
	            return 0;
	        else
	            return columnNames.size();
	    }
	 
	    @Override
	    public Object getValueAt(int rowIndex, int columnIndex)
	    {
	        Vector<Object> temp = (Vector<Object>) data.elementAt(rowIndex);
	        return temp.elementAt(columnIndex);
	    }
	     
	    @Override
	    public String getColumnName(int column)
	    {
	        if (column < 0 || column > columnNames.size())
	            return "No name";
	        else
	        if (columnNames.elementAt(column) == null)
	            return "No name";
	        else
	            return columnNames.elementAt(column);
	    }
}
