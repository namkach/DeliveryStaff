package myPac

import com.kms.katalon.core.annotation.Keyword

import jxl.Cell
import jxl.CellType
import jxl.CellView
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

public class Write {
	@Keyword
	def void writeExcel() {
		Workbook wb = Workbook.getWorkbook(new File("D:\\Users\\sunitakac\\Desktop\\result.csv"));
		WritableWorkbook wbcopy = Workbook.createWorkbook(new File("D:\\Users\\sunitakac\\Desktop\\result.csv"), wb);
		println 'Found Excel'

		WritableSheet ws = wbcopy.getSheet(0);

		def heads = [
			'Order_id',
			'Order Found',
			'New Order',
			'Order Found2',
			'Processing',
			'Order Found3',
			'Processed',
			'Flow Success'
		]
		for (int i = 0; i < heads.size(); i++) {
			Label header = new Label(i, 0, heads[i]);
			ws.addCell(heads[i]);
		}
		Cell cell;
		int row = ws.getRows()
		cell = ws.getCell(0, row)

		boolean isEmpty = cell.getType().equals(CellType.EMPTY)
		println ('Cell empty is : ' + isEmpty)
		println ('Number of row : ' + row)

		while (isEmpty == false) {
			// there is text in the column
			row++
			println 'Cell is not empty'
			println ('Move to next row : ' + row)
		}

		if(isEmpty) {
			println ('Cell empty is : ' + isEmpty)
			def datas = [order_id, checkOrder, statusOrder, checkOrder2, statusOrder2, checkOrder3, statusOrder3, statusText]
			for (int i = 0; i < datas.size(); i++) {
				Label data = new Label(i, 0, datas[i]);
				ws.addCell(datas[i]);
			}

			Label data1 = new Label(0, row, order_id);
			Label data2 = new Label(1, row, checkOrder);
			Label data3 = new Label(2, row, statusOrder);
			ws.addCell(data1);
			ws.addCell(data2);
			ws.addCell(data3);
		}

		CellView cellView = new CellView();
		cellView.setSize(12 * 350);
		int col
		ws.setColumnView(3, cellView);

		wbcopy.write();
		wbcopy.close();
		wb.close();
	}
}
