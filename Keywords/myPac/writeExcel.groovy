package myPac

import com.kms.katalon.core.annotation.Keyword

import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableCell
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

public class writeExcel {
	@Keyword
	def write(String order_id, String status, String remark) {
		
		Workbook existingWorkbook = Workbook.getWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\output.xls'))
		WritableWorkbook workbookCopy = Workbook.createWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\output.xls'), existingWorkbook)
		WritableSheet sheetToEdit = workbookCopy.getSheet(0)
		
		String[] header = ['Order', 'Result', 'Remark']
		String[] text = [order_id, status, remark]
		
		for (int i = 0; i < header.size(); i++) {
				WritableCell cell
				Label l = new Label(i, 0, header[i])
				cell = (WritableCell) l
				sheetToEdit.addCell(cell)
		}
		
		workbookCopy.write()
		workbookCopy.close()
		existingWorkbook.close()
	}
}
