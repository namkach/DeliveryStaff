package myPac

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import jxl.CellType
import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableCell
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

public class RiderKeywords {
	AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()

	def riderId = 'th.co.gosoft.storemobile.sevendelivery.rider:id/'
	boolean checkOrder
	def status = ''
	def remark = ''

	@Keyword
	def filterStoreId(String store_id, Integer status_id) {
		MobileElement filterTab = (MobileElement) driver.findElementById(riderId + 'layout_main_spinner_filter_store_tv_result')
		KeywordUtil.logInfo (filterTab.getText())
		filterTab.click()
		List<MobileElement> filterTabs = driver.findElementsById(riderId + 'layout_main_spinner_filter_store_tv')
		for (int i = 0; i < filterTabs.size(); i++) {
			if (filterTabs.get(i).getText().contains(store_id)) {
				KeywordUtil.logInfo('filter store id : ' + filterTabs.get(i).getText())
				filterTabs.get(i).click()
				status = ''
				remark = ''
				return [status, remark]
			}
		}
		status = 'Fail'
		remark = 'Fail to filter order at status_id ' + status_id
		KeywordUtil.markFailed(remark)
		return [status, remark]
	}

	@Keyword
	def findOrder(String order_id, String store_id, String payment_type, Integer status_id) {
		checkOrder = findOrderId(order_id, store_id, payment_type, status_id)
		while(!checkOrder) {
			swipeUp()
			checkOrder = findOrderId(order_id, store_id, payment_type, status_id)
			KeywordUtil.logInfo('checkOrder : ' + checkOrder)
		}
		if (checkOrder) {
			status = ''
			remark = ''
		} else {
			status = 'Fail'
			remark = 'Fail to find order ' + order_id + ' at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark]
	}

	@Keyword
	def findOrderId(String order_id, String store_id, String payment_type, Integer status_id) {
		checkOrder = false
		KeywordUtil.markPassed ('order : ' + order_id)
		List<MobileElement> orders = driver.findElementsById(riderId + 'txt_order_no')
		List<MobileElement> storeId = driver.findElementsById(riderId + 'txt_store_id')
		for (int j = orders.size() - 1; j >= 0; j--) {
			if (orders.get(j).getText().equals(order_id)) {
				KeywordUtil.markPassed ('*** order found ***')
				switch (payment_type) {
					case '1' :
						checkOrder = true
						break
					case '2' :
						MobileElement paymentIcon = (MobileElement) driver.findElementById(riderId + 'img_payment_type')
						if (paymentIcon.isDisplayed()) {
							checkOrder = true
						}
						break
					case '4' :
						if (status_id == 5 || status_id == 6) {
							checkOrder = true
						} else {
							MobileElement paymentIcon = (MobileElement) driver.findElementById(riderId + 'img_payment_type')
							if (paymentIcon.isDisplayed()) {
								checkOrder = true
							}
						}
						break
				}
				assert storeId.get(j).getText() == store_id
				orders.get(j).click()
				MobileElement orderNo = (MobileElement) driver.findElementById(riderId + 'main_toolbar_tv_order')
				assert orderNo.getText().equals(order_id)
				break
			}
		}
		return checkOrder
	}

	@Keyword
	def swipeUp() {
		int x = Mobile.getDeviceWidth()/2
		int startY = Mobile.getDeviceHeight()*0.7
		int endY = Mobile.getDeviceHeight()*0.4
		Mobile.swipe(x, startY, x, endY)
	}

	@Keyword
	def confirmBtn(String order_id, Integer status_id, String payment_type, Double total_price) {
		checkOrder = false
		KeywordUtil.logInfo(order_id)
		MobileElement ConfirmOrder = (MobileElement) driver.findElementById(riderId + 'order_detail_bt_confirm')
		switch (status_id) {
			case 3 :
				assert ConfirmOrder.getText().equals('รับรายการสั่งซื้อ')
				ConfirmOrder.click()
				checkOrder = true
				break
			case 4 :
				switch (payment_type) {
					case '1' :
					assert ConfirmOrder.getText().equals('ชำระเงิน')
					break
					case '2' :
					assert ConfirmOrder.getText().equals('ยืนยันการส่งสินค้า')
					break
				}
				ConfirmOrder.click()
				printType(total_price)
				checkOrder = confirmPayment(payment_type, total_price)
				break
		}
		if (checkOrder) {
			status_id += 1
			status = ''
			remark = ''
		} else {
			status = 'Fail'
			remark = 'Fail to confirm order ' + order_id + ' at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark, status_id]
	}

	@Keyword
	def confirmPayment(String payment_type, Double total_price) {
		switch (payment_type) {
			case '1' :
				MobileElement cashTab = (MobileElement) driver.findElementById(riderId + 'rdoCash')
				assert cashTab.getAttribute("checked")
				break
			case '4' :
				MobileElement tmwTab = (MobileElement) driver.findElementById(riderId + 'rdoTMW')
				assert tmwTab.getAttribute("checked")
				MobileElement cashTab = (MobileElement) driver.findElementById(riderId + 'rdoCash')
				cashTab.click()
				break
		}
		switch (payment_type) {
			case '1' :
			case '4' :
				printType(total_price)
				MobileElement totalPrice = (MobileElement) driver.findElementById(riderId + 'txtCashPrice')
				MobileElement payPrice = (MobileElement) driver.findElementById(riderId + 'txtCashMoney')

			//--- check delivery fee
			//				if (total_price >= 100) {
			//					assert Double.parseDouble(totalPrice.getText()).equals(total_price)
			//				} else {
			//					assert Double.parseDouble(totalPrice.getText()).equals((total_price + 20))
			//				}

			//--- don't check delivery fee
				assert Double.parseDouble(totalPrice.getText()).equals(total_price)

				payPrice.sendKeys(totalPrice.getText())
				MobileElement confirmPayment = (MobileElement) driver.findElementById(riderId + 'btnConfirm')
				confirmPayment.click()
				MobileElement btnSkip = (MobileElement) driver.findElementById(riderId + 'btnSkip')
				btnSkip.click()
				break
		}

		MobileElement walk = (MobileElement) driver.findElementById(riderId + 'delivery_confirm_rd_walk')
		walk.click()
		MobileElement confirmDelivery = (MobileElement) driver.findElementById(riderId + 'delivery_confirm_bt_confirm')
		confirmDelivery.click()

		Mobile.delay(2)
		swipeUp()
		MobileElement signBtn = (MobileElement) driver.findElementById(riderId + 'main_toolbar_tv_menu_right2')
		signBtn.click()

		MobileElement confirmSignBtn = (MobileElement) driver.findElementById(riderId + 'dialog_confirm_yes')
		confirmSignBtn.click()
		return true
	}

	@Keyword
	def checkTotalProducts(String flow_type, Integer total_product, Integer status_id) {
		List<MobileElement> prods = driver.findElementsById(riderId + 'row_order_detail_tv_name')
		println ('Product size : ' + prods.size())
		println ('Total product : ' + total_product)
		if (flow_type.equals('2')) {
			total_product += 1
		}
		KeywordUtil.logInfo (printType(prods.size()))
		KeywordUtil.logInfo (printType(total_product))
		if (prods.size().equals(total_product)) {
			KeywordUtil.logInfo ('true')
			status = ''
			remark = ''
		} else {
			KeywordUtil.logInfo ('false')
			status = 'Fail'
			remark = 'Fail to check total products at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark]
	}

	@Keyword
	def checkEachProduct(String name, Integer qty, Double unitPrice, Integer countQty, Double countTotalPrice, Integer statusProduct, Integer status_id) {
		List<MobileElement> prods = driver.findElementsById(riderId + 'row_order_detail_tv_name')
		List<MobileElement> qtys = driver.findElementsById(riderId + 'row_order_detail_tv_amount')
		List<MobileElement> prices = driver.findElementsById(riderId + 'row_order_detail_tv_price')

		double totalPrice
		int numQty
		for (int k = 0; k < prods.size(); k++) {
			if (prods.get(k).getText().equals(name)) {
				KeywordUtil.logInfo ('check qty before ' + name + ' : ' + countQty)
				numQty = extractInt(qtys.get(k).getText())
				assert numQty.equals(qty)
				switch (statusProduct) {
					case 1:
						totalPrice = Double.parseDouble(prices.get(k).getText())
						break
					case 2 :
						totalPrice = Double.parseDouble(prices.get(k - 1).getText())
						break
				}

				if (totalPrice.equals((qty * unitPrice))) {
					countQty += qty
					countTotalPrice += totalPrice
					KeywordUtil.logInfo('countQty : ' + countQty)
					KeywordUtil.logInfo('countTotalPrice : ' + countTotalPrice)
					status = ''
					remark = ''
				} else {
					status = 'Fail'
					remark = 'Fail to check each product in order at status_id ' + status_id
					KeywordUtil.markFailed(remark)
				}
				return [status, remark, countQty, countTotalPrice]
			}
		}
	}

	@Keyword
	def checkAllProducts(Double countTotalPrice, Double totalPrice, Integer countQty, Integer status_id) {
		MobileElement allTotalPrice = (MobileElement) driver.findElementById(riderId + 'order_detail_tv_total_price')
		MobileElement allQty = (MobileElement) driver.findElementById(riderId + 'order_detail_tv_total_list')
		double numAllTotalPrice = 0.00
		if (allTotalPrice.getText().contains('บาท')) {
			numAllTotalPrice = Double.parseDouble(allTotalPrice.getText().replace(' บาท',''))
		} else {
			numAllTotalPrice = Double.parseDouble(allTotalPrice.getText())
		}
		printType(numAllTotalPrice)
		KeywordUtil.logInfo ('total price : ' + numAllTotalPrice)
		KeywordUtil.logInfo ('All QTY : ' + extractInt(allQty.getText()))
		if (numAllTotalPrice.equals(countTotalPrice)) {
			assert numAllTotalPrice.equals(totalPrice)
			assert extractInt(allQty.getText()).equals(countQty)
			status = ''
			remark = ''
		} else {
			status = 'Fail'
			remark = 'Fail to Check All Products order at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark]
	}

	@Keyword
	def extractInt(String text) {
		return Integer.parseInt(text.replaceAll('[^0-9]', ''))
	}

	def printType(Integer x) {
		KeywordUtil.logInfo (x + " is an int");
	}
	def printType(String x) {
		KeywordUtil.logInfo (x + " is a string");
	}
	def printType(Double x) {
		KeywordUtil.logInfo (x + " is a double");
	}

	@Keyword
	def setDefault(Double total_product) {
		int qty = 0
		double unitPrice = 0.00
		int countQty = 0
		double countTotalPrice = 0.00
		int statusProduct = 1
		int size = total_product
		double price = 0.00
		return [qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price]
	}

	@Keyword
	def checkStatusId(Integer status_id) {
		checkOrder = false
		MobileElement statusElement = (MobileElement) driver.findElementById(riderId + 'order_detail_time_count_down')
		KeywordUtil.logInfo('status text : ' + statusElement.getText())
		KeywordUtil.logInfo('status_id : ' + status_id)
		//		def text = statusElement.getText()
		switch (status_id) {
			case 5 :
				if (statusElement.getText().contains('เสร็จสมบูรณ์')) {
					checkOrder = true
				}
				break
			case 6 :
				if (statusElement.getText().contains('ยกเลิกออเดอร์')) {
					checkOrder = true
				}
				break
		}
		if (checkOrder) {
			status = 'Pass'
			remark = '-'
			KeywordUtil.markPassed('check Status : Pass')
		} else {
			status = 'Fail'
			remark = 'Fail to check Status order at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark]
	}

	@Keyword
	def writeRider(String order_id, String flow_type, String payment_type, String status, String remark) {
		def path = 'D:\\Users\\sunitakac\\Desktop\\AutoTest\\resultRider.xls'
		Workbook existingWorkbook = Workbook.getWorkbook(new File(path))
		WritableWorkbook workbookCopy = Workbook.createWorkbook(new File(path), existingWorkbook)
		WritableSheet sheetToEdit = workbookCopy.getSheet(0)

		String[] header = ['Order', 'Flow Type', 'Payment Type', 'Result', 'Remark']
		String[] text = [order_id, flow_type, payment_type, status, remark]

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
		KeywordUtil.markPassed('Stamp Pass')
	}
}
