package myPac

import com.kms.katalon.core.annotation.Keyword

import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook



public class Excel {
	@Keyword
	def void write() {
		//
		try {


			def heads = ['Order_id', 'Order Found', 'New Order', 'Order Found2', 'Processing', 'Order Found3', 'Processed', 'Flow Success']

			//						def datas = [
			//							order_id,
			//							checkOrder,
			//							statusOrder,
			//							checkOrder2,
			//							statusOrder2,
			//							checkOrder3,
			//							statusOrder3,
			//							checkOrder4,
			//							statusOrder4
			//						]

			//			//			for (int i = 0; i < heads.size(); i++) {
			//			//				Row rowHead = sheet.getRow(0)
			//			//				Cell cellHead = rowHead.getCell(i)
			//			//				if (cellHead == null) {
			//			//					cellHead = rowHead.createCell(i)
			//			//				}
			//			//				cellHead.setCellType(CellType.STRING)
			//			//				cellHead.setCellValue(heads[i])
			//			//				cellHead = rowHead.createCell(1)
			//			//			}
			//
			//			//			Row rowHead = sheet.getRow(0)
			//			//			Cell cellHead = rowHead.getCell(0)
			//			//
			//			//			cellHead = rowHead.createCell(0)
			//			//
			//			//			cellHead.setCellType(CellType.STRING)
			//			//			cellHead.setCellValue(123)
			//
			//
			//			String excelFilePath = "D:\\Users\\sunitakac\\Desktop\\result.csv"
			//			String excelFilePath2 = "D:\\Users\\sunitakac\\Desktop\\result2.csv"
			//			Workbook workbook = WorkbookFactory.create(new File(excelFilePath))
			//			Sheet sheet = workbook.getSheetAt(0)
			//			println 'Found Excel'
			//
			//			//			Workbook workbook = WorkbookFactory.create(new File("existing-spreadsheet.xlsx"));
			//			//
			//			//				// Get Sheet at index 0
			//			//				Sheet sheet = workbook.getSheetAt(0);
			//
			//			// Get Row at index 1
			//			Row row = sheet.getRow(0);
			//
			//			// Get the Cell at index 2 from the above row
			//			Cell cell = row.getCell(1);
			//
			//			// Create the cell if it doesn't exist
			//			if (cell == null) {
			//				cell = row.createCell(1);
			//			}
			//
			//			// Update the cell's value
			//			cell.setCellType(CellType.STRING);
			//			cell.setCellValue('aassssa');
			//
			//
			//
			//			//			boolean isCreate = false
			//			//			int countRow = 1
			//			//			Row rowBody = sheet.getRow(countRow)
			//			//			Cell cellBody = rowBody.getCell(0)
			//			//			while(cellBody != null) {
			//			//				if(cellBody == order_id) {
			//			//					for (int i = 0; i < datas.size(); i++) {
			//			//						cellBody = rowBody.getCell(i)
			//			//						cellBody.setCellType(CellType.STRING)
			//			//						cellBody.setCellValue(datas[i])
			//			//						isCreate = true
			//			//					}
			//			//					break
			//			//				}
			//			//				countRow++
			//			//			}
			//			//
			//			//			if (cellBody == null && isCreate == false) {
			//			//				for (int i = 0; i < datas.size(); i++) {
			//			//					cellBody = rowBody.getCell(i)
			//			//					cellBody.setCellType(CellType.STRING)
			//			//					cellBody.setCellValue(datas[i])
			//			//					isCreate = true
			//			//				}
			//			//			}
			//
			//
			//			FileOutputStream fileOut = new FileOutputStream(excelFilePath2);
			//			workbook.write(fileOut);
			//			fileOut.close();
			//
			//			// Closing the workbook
			//			workbook.close();
			//
			//
			//
			//			//			FileOutputStream fileOut = new FileOutputStream(excelFilePath)
			//			//			workbook.write(fileOut)
			//			//			fileOut.close()
			//			//
			//			//			workbook.close()


			Workbook wb = Workbook.getWorkbook(new File("D:\\Users\\sunitakac\\Desktop\\result.csv"));
			WritableWorkbook wbcopy = Workbook.createWorkbook(new File("D:\\Users\\sunitakac\\Desktop\\result.csv"), wb);

			WritableSheet ws = wbcopy.getSheet(0);
			Label data1 = new Label(1,1,'aaaaa');
			ws.addCell(data1);
			wbcopy.write();
			wbcopy.close();
			wb.close();



		} catch (Exception e) {
			println '------ error at : ' + e.printStackTrace()
		}
	}
}