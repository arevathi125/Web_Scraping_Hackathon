package Utilities;

public class ExcelPojo {
  private String sheetName;
  private int eliminationColumnIndex;
  private int addColumnIndex;
  
  public ExcelPojo (String sheetName, int eliminationColumnIndex, int addColumnIndex ) {
	  this.sheetName = sheetName;
	  this.eliminationColumnIndex = eliminationColumnIndex;
	  this.addColumnIndex = addColumnIndex;
  }
  
  
public String getSheetName() {
	return sheetName;
}
public void setSheetName(String sheetName) {
	this.sheetName = sheetName;
}
public int getEliminationColumnIndex() {
	return eliminationColumnIndex;
}
public void setEliminationColumnIndex(int eliminationColumnIndex) {
	this.eliminationColumnIndex = eliminationColumnIndex;
}
public int getAddColumnIndex() {
	return addColumnIndex;
}
public void setAddColumnIndex(int addColumnIndex) {
	this.addColumnIndex = addColumnIndex;
}
  
  
}
