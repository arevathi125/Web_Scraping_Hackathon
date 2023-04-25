package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

//import CrossBrowser.Driverfactory;


public class Excel_Reader_Writer {
	// public static WebDriver driver = Driverfactory.getdriver();
	 
	 String path;
		public FileInputStream fi;
		public XSSFWorkbook workbook;
		public XSSFSheet sheet;
		public XSSFRow row;
		public XSSFCell cell;
		public FileOutputStream fo;
		public XSSFCellStyle cellStyle;
		
		Excel_Reader_Writer(String path) {
			this.path = path;
		}
		
		// read sheetname
		public int getRowCount(String sheetName) throws IOException {
			fi = new FileInputStream(path);
			workbook = new XSSFWorkbook(fi);
			sheet = workbook.getSheet(sheetName);
			int rowCount = sheet.getLastRowNum();
			workbook.close();
			fi.close();
			return rowCount;
		}
		
		//read sheetname with rownum
		public int getCellCount(String sheetName,int rowNum) throws IOException {
			fi = new FileInputStream(path);
			workbook = new XSSFWorkbook(fi);
			sheet = workbook.getSheet(sheetName);
			row = sheet.getRow(rowNum);
			int cellCount = row.getLastCellNum();
			workbook.close();
			fi.close();		
			return cellCount;
		}
		
		// read sheetname, rownum and colnum
		public String getCellData(String sheetName,int rowNum,int colNum) throws IOException {
			fi = new FileInputStream(path);
			workbook = new XSSFWorkbook(fi);
			sheet = workbook.getSheet(sheetName);
			
			row = sheet.getRow(rowNum);
			cell = row.getCell(colNum);
			
			DataFormatter formatter = new DataFormatter();
			String data;
			
			try {
				data = formatter.formatCellValue(cell);  //Returns the formatted cell value as a string regardless of the 
			}
			catch(Exception e) { 
				data = "";
			}
			workbook.close();
			fi.close();
			return data;
	  	}
		
		// read all columns
		
		
		// To Write data in the XLSheet
		public void setCellData(String sheetName,int rowNum,int colNum,
				String data,boolean allergeFound) throws IOException {
			File xlFile = new File(path);
			// if file not exists then create a new file
			if (!xlFile.exists()) {         
			workbook = new XSSFWorkbook();
			fo = new FileOutputStream(path);
			workbook.write(fo);
			}
			fi = new FileInputStream(path);
			workbook = new XSSFWorkbook(fi);
			// if Sheet not exists then create a new sheet
			if (workbook.getSheetIndex(sheetName) == -1) {
				workbook.createSheet(sheetName);
			}
			sheet = workbook.getSheet(sheetName);
			// if row not exists then create a new row
			if (sheet.getRow(rowNum) == null) {
				sheet.createRow(rowNum);
			}
			row = sheet.getRow(rowNum);
		    cell = row.createCell(colNum);
			cell.setCellValue(data);
			fo = new FileOutputStream(path);
			
			if(allergeFound) {
			cellStyle = workbook.createCellStyle();
			cellStyle.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			cell.setCellStyle(cellStyle);
			}
			workbook.write(fo);
			workbook.close();
			fi.close();
			fo.close();
			}
		
		
		//Method for Eliminated & Added list
		public  List<String> readColumnValueList(String sheet_name,int col_Index) throws IOException
		{
			fi = new FileInputStream(path);
			workbook = new XSSFWorkbook(fi);
			sheet = workbook.getSheet(sheet_name);
			List<String> columndata = new ArrayList<>();
			Iterator<Row> row = sheet.rowIterator();
		    while(row.hasNext()){
				Row currentRow = row.next();
				Iterator<Cell> cell = currentRow.cellIterator();
				while(cell.hasNext()){
					Cell currentCell = cell.next();
					
					if(currentCell.getColumnIndex() == col_Index) {
					columndata.add(currentCell.getStringCellValue());
					}
				}
			}
			workbook.close();
			fi.close();
			columndata.remove(0);
			//System.out.println("Elimination List"+columndata);
			
			return columndata;
		}
		
          // Method for find duplicates		
		public  List<String> uniqueAndDuplicateElements(List<String> a, List<String> b) {
		    Set<String> containsAll = new HashSet<String>();
		    containsAll.addAll(a);
		    containsAll.addAll(b);
		  
		   /* List<String> uniquevalues = containsAll.stream()
		                                .filter(str -> a.contains(str) ^ b.contains(str))
		                                .collect(Collectors.toList());  */
		        
		    List<String> duplicatevalues = containsAll.stream()
		                                   .filter(str -> a.contains(str) && b.contains(str))
		                                   .collect(Collectors.toList());

		   // System.out.println("Unique elements from both lists: " + uniquevalues);
		    System.out.println("Elements present in both lists: " + duplicatevalues);
		    return duplicatevalues;
		}
		
		public Map<String,ExcelPojo> receipyMap(){
			Map<String,ExcelPojo> maps  = new LinkedHashMap<String,ExcelPojo>();
			maps.put("Vegan Diabetes",new ExcelPojo("Diabetes", 0 , 5));
			maps.put("Vegan Hypothyroidism",new ExcelPojo("Hypothyroidism", 1 , 6));
			maps.put("Vegan Hypertension",new ExcelPojo("Hypertension", 2 , 7));
			maps.put("Vegan PCOS",new ExcelPojo("PCOS", 3 , 8));
			return maps;
		}
}
