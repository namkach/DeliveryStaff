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

public class StaffKeywords_Rider {
	AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
	def staffId = 'th.co.gosoft.storemobile.sevendelivery.staff:id/'
	int sid = 0
	boolean checkOrder = false
	def status = ''
	def remark = ''

	@Keyword
	def findOrder(String order_id, Integer status_id) {
		checkOrder = findOrderId(order_id, status_id)
		while(!checkOrder) {
			swipeUp()
			checkOrder = findOrderId(order_id, status_id)
			KeywordUtil.logInfo('checkOrder : ' + checkOrder)
		}
		if (checkOrder) {
			status = ''
			remark = ''
		} else {
			status = 'Fail'
			remark = 'Fail to find order at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark]
	}

	@Keyword
	def findOrderId(String order_id, Integer status_id) {
		List<MobileElement> orders = driver.findElementsById(staffId + 'txt_order_no')
		for (int j = orders.size() - 1; j >= 0; j--) {
			if (orders.get(j).getText().equals(order_id)) {
				List<MobileElement> statusText = driver.findElementsById(staffId + 'txt_order_time')
				//				MobileElement statusText = (MobileElement) driver.findElementById(staffId + 'txt_order_time')
				switch (status_id) {
					case 1 :
						assert statusText.get(j).getText().contains('รอรับออเดอร์')
						break
					case 2 :
						assert statusText.get(j).getText().contains('กำลังจัดของ')
						break
					case 3 :
						assert statusText.get(j).getText().contains('จัดของเสร็จแล้ว')
						break
					case 4 :
						assert statusText.get(j).getText().contains('กำลังจัดส่ง')
						break
//					case 5 :
//						assert statusText.get(j).getText().contains('เสร็จสมบูรณ์')
//						break
//					case 6 :
//						assert statusText.get(j).getText().contains('ยกเลิกออเดอร์')
//						break
				}
				orders.get(j).click()
				MobileElement orderNo = (MobileElement) driver.findElementById(staffId + 'main_toolbar_tv_order')
				assert orderNo.getText().equals(order_id)
				Mobile.delay(2)
				return true
			}
		}
		return false
	}

	@Keyword
	def swipeUp() {
		int x = Mobile.getDeviceWidth()/2
		int startY = Mobile.getDeviceHeight()*0.7
		int endY = Mobile.getDeviceHeight()*0.35
		Mobile.swipe(x, startY, x, endY)
	}

	@Keyword
	def swipeUp(Double sy, Double ey) {
		int x = Mobile.getDeviceWidth()/2
		int startY = Mobile.getDeviceHeight()*sy
		int endY = Mobile.getDeviceHeight()*ey
		Mobile.swipe(x, startY, x, endY)
	}


	@Keyword
	def checkTotalProducts(String flow_type, Integer total_product, Integer status_id, Integer statusCheck) {
		List<MobileElement> prods = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		println ('Product size : ' + prods.size())
		println ('Total product : ' + total_product)
		for (int i = 0; i < prods.size(); i++) {
			KeywordUtil.logInfo(prods.get(i).getText())
		}
		if (prods.size().equals(total_product)) {
			if (statusCheck == 1 && flow_type == '6') {
				total_product -= 1
			}
			status = ''
			remark = ''
		} else {
			status = 'Fail'
			remark = 'Fail to check total products order at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark, total_product]
	}

	@Keyword
	def checkEachProduct(String name, Integer qty, Double unitPrice, Integer countQty, Double countTotalPrice, Integer statusProduct, Integer status_id) {
		List<MobileElement> prods = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		List<MobileElement> qtys = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
		List<MobileElement> prices = driver.findElementsById(staffId + 'row_order_detail_tv_price')
		double totalPrice = 0.0
		int numQty = 0
		for (int k = 0; k <= prods.size(); k++) {
			if (prods.get(k).getText().equals(name)) {
				println (prods.get(k).getText() + ' : Found')
				println ('k is : ' + k)
				KeywordUtil.logInfo('statusProduct : ' + statusProduct)
				switch (statusProduct) {
					case 0 :
						numQty = extractInt(qtys.get(k).getText())
						totalPrice = Double.parseDouble(prices.get(k).getText())
						break
					case 1 :
						switch (status_id) {
							case 2 :
							numQty = extractInt(qtys.get(k - 1).getText())
							break
							case 3..5 :
							numQty = extractInt(qtys.get(k).getText())
							break
						}
						totalPrice = Double.parseDouble(prices.get(k - 1).getText())
						break
				}

				KeywordUtil.logInfo('qty : ' + qty)
				KeywordUtil.logInfo('numQty : ' + numQty)
				assert numQty == qty

				KeywordUtil.logInfo('numPrice : ' + totalPrice)
				KeywordUtil.logInfo('qty / unitPrice : ' + qty + '   /   ' + unitPrice)
				if (totalPrice.equals(qty * unitPrice)) {
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
		MobileElement allTotalPrice = (MobileElement) driver.findElementById(staffId + 'order_detail_tv_total_price')
		MobileElement allQty = (MobileElement) driver.findElementById(staffId + 'order_detail_tv_total_list')
		double numAllTotalPrice = 0.00
		if (allTotalPrice.getText().contains('บาท')) {
			numAllTotalPrice = Double.parseDouble(allTotalPrice.getText().replace(' บาท',''))
		} else {
			numAllTotalPrice = Double.parseDouble(allTotalPrice.getText())
		}
		KeywordUtil.logInfo ('total price : ' + numAllTotalPrice)
		KeywordUtil.logInfo ('All QTY : ' + extractInt(allQty.getText()))

		if (numAllTotalPrice.equals(countTotalPrice)) {
			assert numAllTotalPrice.equals(totalPrice)
			assert extractInt(allQty.getText()).equals(countQty)
			status = ''
			remark = ''
		} else {
			KeywordUtil.logInfo ('false')
			status = 'Fail'
			remark = 'Fail to Check All Products at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark]
	}

	@Keyword
	def confirmBtn(Integer status_id, String payment_type, String delivery_type, String rider_name) {
		//	def confirmBtn(Integer status_id, String payment_type, String apkType, String delivery_type, String rider_name) {
		def btnName = ''
		//get delivery type
		MobileElement deliveryTypeElement = (MobileElement) driver.findElementById(staffId + 'order_detail_tv_pickup_method')
		println ('deliveryTypeElement : ' + deliveryTypeElement.getText())
		switch (deliveryTypeElement.getText()) {
			case 'รับที่ร้าน' :
				assert delivery_type == '1'
				break
			case 'จัดส่งถึงที่' :
				assert delivery_type == '2'
				break
		}
		switch (status_id) {
			case 1 :
				btnName = 'รับรายการสั่งซื้อ'
				break
			case 2 :
				btnName = 'ยืนยันการจัดสินค้า'
				MobileElement totalQtyElement = (MobileElement) driver.findElementById(staffId + 'order_detail_tv_total_list')
				int totalQty = extractInt(totalQtyElement.getText())
				KeywordUtil.logInfo('totalQty : ' + totalQty)
				int countItem = 0
				countItem = countItems(countItem)
				KeywordUtil.logInfo('countItem : ' + countItem)
				while(countItem != totalQty) {
					swipeUp()
					countItem = countItems(countItem)
					KeywordUtil.logInfo('countItem : ' + countItem)
				}
				break
			//			case 3..4 :
			case 3 :
				println ('payment : ' + payment_type)
			//				switch (delivery_type) {
			//					case '1' :
				btnName = 'ยืนยันการส่งสินค้า'
			//						break
			//					case '1' :
			//						switch (payment_type) {
			//							case '1' :
			//							case '4' :
			//							btnName = 'ชำระเงิน'
			//							break
			//							case '2' :
			//							btnName = 'ยืนยันการส่งสินค้า'
			//							break
			//						}
			//						break
			//				}

			//				println ('payment : ' + payment_type)
			//				switch (apkType) {
			//					case 'rider' :
			//						btnName = 'ยืนยันการส่งสินค้า'
			//						break
			//					case 'cod' :
			//						switch (payment_type) {
			//							case '1' :
			//							case '4' :
			//							btnName = 'ชำระเงิน'
			//							break
			//							case '2' :
			//							btnName = 'ยืนยันการส่งสินค้า'
			//							break
			//						}
			//						break
			//				}
				break
		}
		println ('------ btn : ' + btnName)

		MobileElement confirmOrder = (MobileElement) driver.findElementById(staffId + 'order_detail_bt_confirm')
		KeywordUtil.logInfo('confirmOrder btn : ' + confirmOrder.getText())
		assert confirmOrder.getText().equals(btnName)
		confirmOrder.click()
		Mobile.delay(2)

		//		(status_id, checkOrder) = confirmPayment(payment_type, status_id, apkType, delivery_type, rider_name)
		(status_id, checkOrder) = confirmPayment(payment_type, status_id, delivery_type, rider_name)
		if (checkOrder) {
			status = ''
			remark = ''
		} else {
			status = 'Fail'
			remark = 'Fail to confirm order at status_id ' + status_id
			KeywordUtil.markFailed(remark)
		}
		return [status, remark, status_id]
	}

	@Keyword
	def confirmPayment(String payment_type, Integer status_id, String delivery_type, String rider_name) {
		//	def confirmPayment(String payment_type, Integer status_id, String apkType, String delivery_type, String rider_name) {
		KeywordUtil.logInfo('-- confirmPayment --')
		checkOrder = false
		KeywordUtil.logInfo ('status_id : ' + status_id)
		switch (status_id) {
			case 1 :
				status_id = 2
				println ('sid : ' + status_id)
				checkOrder = true
				KeywordUtil.logInfo('checkOrder : ' + checkOrder)
				break

			//confirm LP
			case 2 :
			//get payment type
				println ('sid : ' + status_id)
				MobileElement payment = (MobileElement) driver.findElementById(staffId + 'dialog_lp_payment_type')
				switch (payment.getText()) {
					case 'ชำระเงินสด' :
						assert payment_type == '1'
						break
					case 'True Money' :
						assert payment_type == '2'
						break
					case 'ทรูมันนี่ปลายทาง' :
						assert payment_type == '4'
						break
				}
				println ('payment type : ' + payment_type)
			//confirm LP with select rider
				MobileElement confirmLp = (MobileElement) driver.findElementById(staffId + 'dialog_lp_buy_button')
				confirmLp.click()
				println ('deliveryType : ' + delivery_type)
				println ('sid : ' + status_id)
				switch (delivery_type) {
					case '1' :
						checkOrder = true
						status_id = 3
						break
					case '2' :
					/////
						checkOrder = selectRider(rider_name)
						while(!checkOrder) {
							swipeUp(0.55, 0.35)
							checkOrder = selectRider(rider_name)
							KeywordUtil.logInfo('checkOrder : ' + checkOrder)
						}
						if (!checkOrder) {
							return [status_id, checkOrder]
						}
					////
						MobileElement confirmRider = (MobileElement) driver.findElementById(staffId + 'dialog_select_buy_button')
						confirmRider.click()
						status_id = 3
						break
				}
				KeywordUtil.logInfo('checkOrder : ' + checkOrder)
				break

			//check payment
			case 3 :
				MobileElement confirmYes = (MobileElement) driver.findElementById(staffId + 'dialog_confirm_yes')
				confirmYes.click()
				status_id = 5
				checkOrder = true
				KeywordUtil.logInfo('checkOrder : ' + checkOrder)
				break
		}
		return [status_id, checkOrder]
	}

	//	@Keyword
	//	def checkPaymentType(String payment_type) {
	//		switch (payment_type) {
	//			case '1' :
	//				MobileElement cashTab = (MobileElement) driver.findElementById(staffId + 'rdoCash')
	//				assert cashTab.getAttribute("checked")
	//				break
	//			case '4' :
	//				MobileElement tmwTab = (MobileElement) driver.findElementById(staffId + 'rdoTMW')
	//				assert tmwTab.getAttribute("checked")
	//				MobileElement cashTab = (MobileElement) driver.findElementById(staffId + 'rdoCash')
	//				cashTab.click()
	//				break
	//		}
	//
	//		println ('paymentType is : ' + payment_type)
	//		MobileElement totalPrice = (MobileElement) driver.findElementById(staffId + 'txtCashPrice')
	//		MobileElement payPrice = (MobileElement) driver.findElementById(staffId + 'txtCashMoney')
	//		payPrice.sendKeys(totalPrice.getText())
	//		MobileElement confirmPayment = (MobileElement) driver.findElementById(staffId + 'btnConfirm')
	//		confirmPayment.click()
	//		MobileElement btnSkip = (MobileElement) driver.findElementById(staffId + 'btnSkip')
	//		btnSkip.click()
	//
	//	}

	@Keyword
	def selectRider(String rider_name) {
		List<MobileElement> names = driver.findElementsById(staffId + 'dialog_select_item_tv_rider_display')
		for (int j = names.size() - 1; j >= 0; j--) {
			KeywordUtil.logInfo('rider name : ' + names.get(j).getText())
			if (names.get(j).getText().equals(rider_name)) {
				List<MobileElement> radioBtns = driver.findElementsById(staffId + 'dialog_select_item_rbtn')
				// bug -- have to double click to select rider's name
				//				for (int i = 0; i < 2; i++) {
				radioBtns.get(j).click()
				//				}
				return true
			}
		}
		return false
	}

	@Keyword
	def cancelBtn(String flow_type, Integer status_id) {
		MobileElement cancelBtn = (MobileElement) driver.findElementById(staffId + 'order_detail_bt_cancel')
		cancelBtn.click()
		List<MobileElement> cancelOption = driver.findElementsByClassName('android.widget.RadioButton')
		println cancelOption.size()
		for (int i = 0; i < cancelOption.size(); i++) {
			if (cancelOption.get(i).getText() == 'รายการทดสอบ') {
				println cancelOption.get(i).getText()
				cancelOption.get(i).click()

				MobileElement cancelConfirm = (MobileElement) driver.findElementById(staffId + 'dialog_order_cancel_confirm_bt_yes')
				assert cancelConfirm.getText() == 'ยืนยัน'
				cancelConfirm.click()
				KeywordUtil.logInfo('PASS CANCEL Status : ' + flow_type)
				status = ''
				remark = ''
				return [status, remark]
			}
		}
		status = 'Fail'
		remark = 'Fail to cancel order at status id ' + status_id
		return [status, remark]
	}

	@Keyword
	def countItems(Integer countItem) {
		List<MobileElement> eachItemQty = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
		List<MobileElement> checkbox = driver.findElementsById(staffId + 'row_order_detail_cb_is_pick')
		boolean checkboxItem
		for (int i = 0; i < checkbox.size(); i++) {
			checkboxItem = checkbox.get(i).getAttribute('checked')
			if (checkboxItem) {
				countItem += Integer.parseInt(eachItemQty.get(i).getText())
				checkbox.get(i).click()
			}
			KeywordUtil.logInfo('countItem : ' + countItem)
		}
		return countItem
	}

	@Keyword
	def editQty(String editProduct, Integer qty, Integer oldQty) {
		List<MobileElement> products = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		println products.size()
		for (int i = 0; i <= products.size(); i++) {
			if (products.get(i).getText().equals(editProduct)) {
				println products.get(i).getText()
				if (qty > 0) {
					println 'plus'
					List<MobileElement> plus = driver.findElementsById(staffId + 'row_order_detail_iv_plus')
					for (int j = 1; j <= qty; j++) {
						plus.get(i).click()
						println j
					}
					//					List<MobileElement> amount = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
					//					KeywordUtil.logInfo('amount : ' + amount.get(i).getText() )
					//					assert Integer.parseInt(amount.get(i).getText()) == oldQty + qty
				} else if (qty < 0) {
					println 'minus'
					List<MobileElement> minus = driver.findElementsById(staffId + 'row_order_detail_iv_minus')
					for (int k = -1; k >= qty; k--) {
						minus.get(i).click()
						println k
					}
					//					List<MobileElement> amount = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
					//					KeywordUtil.logInfo('amount : ' + amount.get(i).getText())
					//					assert Integer.parseInt(amount.get(i).getText()) == oldQty + qty
				}
				List<MobileElement> amount = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
				KeywordUtil.logInfo('amount : ' + amount.get(i).getText())
				assert Integer.parseInt(amount.get(i).getText()) == oldQty + qty
				break
			}
		}
	}

	@Keyword
	def addProduct(String editProduct, Integer qty) {
		MobileElement cart = (MobileElement) driver.findElementById(staffId + 'order_detail_cv_add')
		cart.click()
		MobileElement search = (MobileElement) driver.findElementById(staffId + 'main_et_search')
		search.sendKeys(editProduct + '\\n')
		MobileElement products = (MobileElement) driver.findElementById(staffId + 'layout_product_name_tv_label')
		println products.getText()
		products.click()
		if (qty > 1) {
			MobileElement plus = (MobileElement) driver.findElementById(staffId + 'product_detail_iv_plus')
			for (int j = 2; j <= qty; j++) {
				plus.click()
				println ('qty : ' + qty + '    //   j : ' + j)
			}
		}
		MobileElement addCart = (MobileElement) driver.findElementById(staffId + 'product_detail_bt_add')
		addCart.click()
	}

	@Keyword
	def deleteProduct (String editProduct) {
		List<MobileElement> products = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		List<MobileElement> amounts = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
		List<MobileElement> minus = driver.findElementsById(staffId + 'row_order_detail_iv_minus')
		println ('Product size : ' + products.size())
		println ('minus btn have : ' + minus.size())
		println ('editProduct : ' + editProduct)
		for (int i = 0; i < products.size(); i++) {
			println ('Product : ' + products.get(i).getText())
			println (editProduct.equals(products.get(i).getText()))
			println ('i is : ' + i)
			if (!editProduct.equals(products.get(i).getText())) {
				continue
			} else {
				KeywordUtil.logInfo('Product to delete : ' + products.get(i).getText())
				println ('amounts : ' + amounts.get(i).getText())
				def amount = Integer.parseInt(amounts.get(i).getText())
				println ('amount : ' + amount)
				for (int j = amount; j > 0; j--) {
					minus.get(i).click()
					println j
				}
				println 'success-------------'
				break
			}
		}
	}

	@Keyword
	def extractInt(String input) {
		return Integer.parseInt(input.replaceAll("[^0-9]", ""))
	}

	@Keyword
	def setDefault(Double total_product, String flow_type, Integer statusCheck) {
		int qty = 0
		double unitPrice = 0.00
		int countQty = 0
		double countTotalPrice = 0.00
		int size = total_product
		double price = 0.00
		int statusProduct = 0

		switch (statusCheck) {
			case 1 :
				switch (flow_type) {
					case '6' :
					size = total_product + 1
					break
				}
				break
		}
		return [qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price]
	}

	@Keyword
	def checkStatusId(Integer status_id) {
		checkOrder = false
		MobileElement statusElement = (MobileElement) driver.findElementById(staffId + 'order_detail_time_count_down')
		KeywordUtil.logInfo('Status Element : ' + statusElement.getText())
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
	def checkRider(String rider_name) {
		MobileElement riderText = (MobileElement) driver.findElementById(staffId + 'order_detail_tv_rider_name')
		KeywordUtil.logInfo('rider_name : ' + rider_name)
		KeywordUtil.logInfo('riderText : ' + riderText.getText())
		if (riderText.getText().equals(rider_name)) {
			status = ''
			remark = ''
		} else {
			status = 'Fail'
			remark = 'Fail to check rider name'
			KeywordUtil.markFailed(remark)
		}
		return [status, remark]}

	@Keyword
	def writeStaff(String order_id, String flow_type, String delivery_type, String payment_type, String status, String remark) {
		def path = 'D:\\Users\\sunitakac\\Desktop\\AutoTest\\resultStaff.xls'
		Workbook existingWorkbook = Workbook.getWorkbook(new File(path))
		WritableWorkbook workbookCopy = Workbook.createWorkbook(new File(path), existingWorkbook)
		WritableSheet sheetToEdit = workbookCopy.getSheet(0)

		String[] header = ['Order', 'Flow Type', 'Delivery Type', 'Payment Type', 'Result', 'Remark']
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