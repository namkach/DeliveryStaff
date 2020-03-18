package myPac

import com.kms.katalon.core.annotation.Keyword

import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableCell
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

public class writeExcel {
	@Keyword
	def write(currentColumn, currentRow, value) {
		
		Workbook existingWorkbook = Workbook.getWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\output.xls'))
		WritableWorkbook workbookCopy = Workbook.createWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\output.xls'), existingWorkbook)
		WritableSheet sheetToEdit = workbookCopy.getSheet(0)
		WritableCell cell
		Label l = new Label(currentColumn, currentRow, value)
		cell = (WritableCell) l
		sheetToEdit.addCell(cell)
		workbookCopy.write()
		workbookCopy.close()
		existingWorkbook.close()
	}
}
