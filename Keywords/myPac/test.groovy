package myPac

import com.kms.katalon.core.annotation.Keyword

import jxl.CellType
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableCell
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

public class test {
	@Keyword
	def void writeExcel(String order_id, String checkOrder, String status_id) {
		//		Workbook wb = Workbook.getWorkbook(new File("D:\\Users\\sunitakac\\Desktop\\report.csv"));
		//		WritableWorkbook wbcopy = Workbook.createWorkbook(new File("D:\\Users\\sunitakac\\Desktop\\report.csv"), wb);
		//
		//		WritableSheet ws = wbcopy.getSheet(0)
		//		Label data1 = new Label(0,1,order_id)
		//		ws.addCell(data1)
		//		wbcopy.write()
		//		wbcopy.close()
		//		wb.close()

		def path = 'D:\\Users\\sunitakac\\Desktop\\result.csv'
		Workbook workbook = Workbook.getWorkbook(new File(path))
		WritableWorkbook copy = Workbook.createWorkbook(new File(path), workbook)

		WritableSheet sheet2 = copy.getSheet(1)
		WritableCell cell = sheet2.getWritableCell(1, 1)

		if (cell.getType() == CellType.LABEL)
		{
			Label l = (Label) cell
			l.setString(order_id)
		}
		copy.write()
		copy.close()
		workbook.close()
	}
}
