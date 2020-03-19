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
	def write(String order_id, String flow_type, String delivery_type, String payment_type, String status, String remark) {

		Workbook existingWorkbook = Workbook.getWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\result.xls'))
		WritableWorkbook workbookCopy = Workbook.createWorkbook(new File('D:\\Users\\sunitakac\\Desktop\\result.xls'), existingWorkbook)
		WritableSheet sheetToEdit = workbookCopy.getSheet(0)

		String[] header = ['Order','Flow Type', 'Delivery Type', 'Payment Type', 'Result', 'Remark']
		String[] text = [order_id, flow_type, delivery_type, payment_type, status, remark]

		for (int i = 0; i < header.size(); i++) {
			WritableCell cellHeader
			Label l = new Label(i, 0, header[i])
			cellHeader = (WritableCell) l
			sheetToEdit.addCell(cellHeader)
		}

		WritableCell cellText
		def textCell
		int row = sheetToEdit.getRows()
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
		}
		workbookCopy.write()
		workbookCopy.close()
		existingWorkbook.close()
	}
}
