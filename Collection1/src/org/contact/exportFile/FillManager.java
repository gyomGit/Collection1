package org.contact.exportFile;


import java.util.List;
 
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.contact.entity.Contact;
 
public class FillManager {
 
 /**
  * Fills the report with content
  * 
  * @param worksheet
  * @param startRowIndex starting row offset
  * @param startColIndex starting column offset
  * @param datasource the data source
  */
 public static void fillReport(HSSFSheet worksheet, int startRowIndex, int startColIndex, List<Contact> datasource) {
  // Row offset
  startRowIndex += 2;
   
  // Create cell style for the body
  HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
  bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
  bodyCellStyle.setWrapText(true);
   
  // Create body
  for (int i=startRowIndex; i+startRowIndex-2< datasource.size()+2; i++) {
   // Create a new row
   HSSFRow row = worksheet.createRow((short) i+1);
 
   // Retrieve the id value
   HSSFCell cell1 = row.createCell(startColIndex+0);
   cell1.setCellValue(datasource.get(i-2).getObjetId());
   cell1.setCellStyle(bodyCellStyle);
 
   // Retrieve the brand value
   HSSFCell cell2 = row.createCell(startColIndex+1);
   cell2.setCellValue(datasource.get(i-2).getIdentification());
   cell2.setCellStyle(bodyCellStyle);
 
   // Retrieve the model value
   HSSFCell cell3 = row.createCell(startColIndex+2);
   cell3.setCellValue(datasource.get(i-2).getPrefixeMusee());
   cell3.setCellStyle(bodyCellStyle);
 
   // Retrieve the maximum power value
   HSSFCell cell4 = row.createCell(startColIndex+3);
   cell4.setCellValue(datasource.get(i-2).getInventaire());
   cell4.setCellStyle(bodyCellStyle);
 
   // Retrieve the price value
   HSSFCell cell5 = row.createCell(startColIndex+4);
   cell5.setCellValue(datasource.get(i-2).getLocalisation());
   cell5.setCellStyle(bodyCellStyle);
   
   // Retrieve the efficiency value
//   HSSFCell cell6 = row.createCell(startColIndex+5);
//   cell6.setCellValue(datasource.get(i-2).getImage());
//   cell6.setCellStyle(bodyCellStyle);
  }
 }
}