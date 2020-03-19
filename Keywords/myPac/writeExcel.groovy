package myPac

import com.kms.katalon.core.annotation.Keyword

import jxl.CellType
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableCell
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

public class writeExcel {
	@Keyword
	def write(String order_id, String status, String remark) {

		Workbook existingWorkbook = Workbook.getWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\1.xls'))
		WritableWorkbook workbookCopy = Workbook.createWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\1.xls'), existingWorkbook)
		WritableSheet sheetToEdit = workbookCopy.getSheet(0)

		String[] header = ['Order', 'Result', 'Remark']
		String[] text = [order_id, status, remark]

		for (int i = 0; i < header.size(); i++) {
			WritableCell cellHeader
			Label l = new Label(i, 0, header[i])
			cellHeader = (WritableCell) l
			sheetToEdit.addCell(cellHeader)
		}

//		for (int j = 1; j < text.size() + 1; j++) {
//			WritableCell cellText = sheetToEdit.getCell(0, j)
//			def textCell = sheetToEdit.getCell(0, j).getContents()
//			if (cellText == null || textCell == order_id) {
//				for (int k = 0; k < text.size(); k++) {
//					Label l = new Label(k, j, text[k])
//					cellText = (WritableCell) l
//					sheetToEdit.addCell(cellText)
//				}
//			}
//		}
		
		
		 
		

//		boolean isFilled = true
//		int j = 1 
//		while (isFilled) {
//			boolean isEmpty = cellText.getType().equals(CellType.EMPTY)
//			if (!isEmpty) {
//				
//			}
//			
//			
//			println ('text cell : ' + textCell)
//			if ((textCell != null && textCell == order_id) || (textCell == null)) {
//				for (int k = 0; k < text.size(); k++) {
//					Label l = new Label(k, j, text[k])
//					cellText = (WritableCell) l
//					sheetToEdit.addCell(cellText)
//				}
//				break
//			} else {
//				j++
//			}
//		}
		
		
		WritableCell cellText
		def textCell
		int row = sheetToEdit.getRows()
//		cellText = sheetToEdit.getCell(0, row)
		
		
		boolean isEmpty
		for (int j = 1; j <= row + 1; j++) {
			cellText = sheetToEdit.getCell(0, j)
			textCell = sheetToEdit.getCell(0, j).getContents()
			isEmpty = cellText.getType().equals(CellType.EMPTY)
			if (isEmpty || (!isEmpty && textCell == order_id)) {
				for (int k = 0; k < text.size(); k++) {
					Label l = new Label(k, j, text[k])
					cellText = (WritableCell) l
					sheetToEdit.addCell(cellText)
				}
				break
			}
			
//			if ((textCell != null && textCell == order_id) || (textCell == null)) {
//				for (int k = 0; k < text.size(); k++) {
//					Label l = new Label(k, j, text[k])
//					cellText = (WritableCell) l
//					sheetToEdit.addCell(cellText)
//				}
//				break
//			}
		}
		
		
		

		
		
		workbookCopy.write()
		workbookCopy.close()
		existingWorkbook.close()
	}
}
