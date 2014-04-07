package ru.guap.dao.dbf;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ru.guap.dao.DBManager;
import nl.knaw.dans.common.dbflib.CorruptedTableException;
//import nl.knaw.dans.common.dbflib.CorruptedTableException;
import nl.knaw.dans.common.dbflib.Field;
import nl.knaw.dans.common.dbflib.IfNonExistent;
import nl.knaw.dans.common.dbflib.Record;
import nl.knaw.dans.common.dbflib.Table;
import nl.knaw.dans.common.dbflib.Type;
import nl.knaw.dans.common.dbflib.ValueTooLargeException;

public class DBFConverter {
    
    /**
     * Формирует SQL-запросы на вставку данных из DBF-файла
     * @param dbf входной DBF-файл
     * @return результат операции
     */
    public static boolean insertDBFContentToDB(File dbf) {
	Table table = new Table(dbf); // Открываем DBF-таблицу

	try
	{
	    table.open(IfNonExistent.ERROR);

            final Iterator<Record> recordIterator = table.recordIterator();
            int count = 0;
            
    	    // Формируем начало SQL-запроса
            StringBuilder recordInsertSql = new StringBuilder();
            
            // Insert into loads table current load
            recordInsertSql.append(String.format("INSERT INTO `%s`.`%s`", DBManager.DB_NAME, DBManager.LOADS_TABLE_NAME));
            recordInsertSql.append(" (`date`) VALUES (NOW());");
            //System.out.println("Insert SQL: " + recordInsertSql.toString());
            
            int load_id = DBManager.getInstance().getLastInsertIdAfterSQL(recordInsertSql.toString(), DBManager.LOADS_TABLE_NAME);            
            
            // Обходим строки
            while(recordIterator.hasNext())
            {
                recordInsertSql = new StringBuilder();
                
                // Insert load data
                recordInsertSql.append(String.format("INSERT INTO `%s`.`%s`", DBManager.DB_NAME, DBManager.MAIN_TABLE_NAME));
                
                LinkedList<String> cols = new LinkedList<>();
                LinkedList<String> vals = new LinkedList<>();
                
                final Record record = recordIterator.next();

                // Обходим столбцы
                List<Field> fields = table.getFields();
                for(final Field field: fields)
                {
                    try
                    {
                        byte[] rawValue = record.getRawValue(field);
                        
                        // Преобразуем старые имена столбцов в новые
                        String colName = DBFColumnConvMap.map.get(field.getName());
                        
                        String value = "";
                        
                        // Декодируем значение и вырезаем лишние пробелы
                        if (field.getType() == Type.CHARACTER)
                            value = (rawValue == null ? "NULL" : new String(rawValue, "CP866").trim());
                        else	
                            value = (rawValue == null ? "NULL" : new String(rawValue).trim());
                        
                        if (field.getType() == Type.NUMBER && rawValue != null && new String(rawValue).trim().isEmpty()) {
                            value = "0";
                        }
                        
                        cols.add(colName);
                        vals.add(value);
                        
                        
                    }
                    catch(ValueTooLargeException vtle)
                    {
                    }
                }
                
                recordInsertSql.append(" (");
                for (String col : cols) {
                    recordInsertSql.append("`").append(col).append("`").append(", ");
                }
                
                // Add two columns: date and teacher_ID (null)
                recordInsertSql.append("`load_id`, `teachers_id`").append(") ");
                
                recordInsertSql.append("VALUES (");
                for (String val : vals) {
                    recordInsertSql.append("\"").append(val).append("\", ");
                }
                
                // Add values of last two columns: date and teachers_id
                recordInsertSql.append(load_id);
                recordInsertSql.append(", NULL);");
                
                boolean res = DBManager.getInstance().executeSql(recordInsertSql.toString());
                
                //System.out.println(recordInsertSql.toString());
                cols.clear();
                vals.clear();
            }	    
	    
	} catch (CorruptedTableException e) {
	    e.printStackTrace();
	    return false;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	}
	finally
	{
	    try {
		table.close();
		
		// Избавляемся от ненужного более файла
		dbf.delete();
	    } catch (IOException e) {
		e.printStackTrace();
		return false;
	    } 
	}
	
	return true;
    }
}
