package myPac

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

public class StaffKeywords {
	AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
	def staffId = 'th.co.gosoft.storemobile.sevendelivery.staff:id/'
	int sid = 0
	boolean checkOrder = false
	def status = ''
	def remark = ''

	@Keyword
	def findOrder(String order_id, Integer status_id) {
		checkOrder = findOrderId(order_id)
		while(!checkOrder) {
			swipeUp()
			checkOrder = findOrderId(order_id)
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
	def findOrderId(String order_id) {
		List<MobileElement> orders = driver.findElementsById(staffId + 'txt_order_no')
		for (int j = orders.size() - 1; j >= 0; j--) {
			if (orders.get(j).getText().equals(order_id)) {
				KeywordUtil.markPassed ('*** order found ***')
				orders.get(j).click()
				MobileElement orderNo = (MobileElement) driver.findElementById(staffId + 'main_toolbar_tv_order')
				assert orderNo.getText().equals(order_id)
				return true
			}
		}
		return false
	}

	@Keyword
	def swipeUp() {
		int x = Mobile.getDeviceWidth()/2
		int startY = Mobile.getDeviceHeight()*0.7
		int endY = Mobile.getDeviceHeight()*0.4
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
	def ConfirmBtn(Integer status_id, String payment_type, String apkType, String delivery_type) {
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
				countItem()
				break
			case 3..4 :
				println ('payment : ' + payment_type)
				if (apkType == 'rider') {
					btnName = 'ยืนยันการส่งสินค้า'
				} else if (apkType == 'cod') {
					if (payment_type == '1') {
						btnName = 'ชำระเงิน'
					} else if (payment_type == '2') {
						btnName = 'ยืนยันการส่งสินค้า'
					}
				}
				break
		}
		println ('------ btn : ' + btnName)

		MobileElement confirmOrder = (MobileElement) driver.findElementById(staffId + 'order_detail_bt_confirm')
		println confirmOrder.getText()
		assert confirmOrder.getText().equals(btnName)
		confirmOrder.click()
		(status_id, checkOrder) = confirmPayment(payment_type, status_id, apkType, delivery_type)
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
	def confirmPayment(String payment_type, Integer status_id, String apkType, String delivery_type) {
		checkOrder = false
		switch (status_id) {
			case 1 :
				status_id = 2
				println ('sid : ' + status_id)
				checkOrder = true
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
			//confirm LP
				MobileElement confirmLp = (MobileElement) driver.findElementById(staffId + 'dialog_lp_buy_button')
				confirmLp.click()
				println ('deliveryType : ' + delivery_type)
				println ('sid : ' + status_id)
				if (delivery_type == '1') {
					status_id = 3
					checkOrder = true
				} else if (delivery_type == '2') {
					status_id = 4
					checkOrder = true
				}
				break

			//check payment
			case 3..4 :
				if (apkType == 'rider') {
					MobileElement confirmYes = (MobileElement) driver.findElementById(staffId + 'dialog_confirm_yes')
					confirmYes.click()
					status_id = 5
					checkOrder = true
				} else if (apkType == 'cod') {
					KeywordUtil.logInfo ('status id : ' + status_id)
					checkOrder = CheckPaymentType(payment_type)
					if (checkOrder) {
						status_id = 5
					}
				}
				if (checkOrder) {
					status_id = 5
				}
				switch (delivery_type) {
					case '1' : 
						status_id = 5
						break
					case '2' :
						MobileElement walk = (MobileElement) driver.findElementById(staffId + 'delivery_confirm_rd_walk')
						walk.click()
						MobileElement confirmOrder = (MobileElement) driver.findElementById(staffId + 'delivery_confirm_bt_confirm')
						confirmOrder.click()
						MobileElement confirmSign = (MobileElement) driver.findElementById(staffId + 'main_toolbar_tv_menu_right2')
						if (confirmSign.isDisplayed()) {
							swipeUp()
							confirmSign.click()
							MobileElement confirmSignYes = (MobileElement) driver.findElementById(staffId + 'dialog_confirm_yes')
							confirmSignYes.click()
							status_id = 5
						}
						break
				}
				break
		}
		return [status_id, checkOrder]
	}
	
	@Keyword
	def CancelBtn(String flow_type, Integer status_id) {
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
	def countItem() {
		MobileElement totalAmountElement = (MobileElement) driver.findElementById(staffId + 'order_detail_tv_total_list')
		println totalAmountElement.getText()
		List<MobileElement> eachItemAmount = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
		println ('this element has : ' + eachItemAmount.size() + ' item(s)')

		RiderKeywords kw = new RiderKeywords()
		int totalamount = extractInt(totalAmountElement.getText())

		int countItem = 0
		List<MobileElement> checkbox = driver.findElementsById(staffId + 'row_order_detail_cb_is_pick')
		println ('checkbox size : ' + checkbox.size())
		while (totalamount != countItem) {
			for (int l = 0; l < checkbox.size(); l++) {
				println checkbox.size()

				boolean a=checkbox.get(l).getAttribute('checked')
				println('Check box is ' + a)

				if (a == true) {
					println ('checkbox' + l + ' is not selected')
					countItem += Integer.parseInt(eachItemAmount.get(l).getText())
					println ('All are : ' + totalamount + '   //    But now have : ' + countItem)
					checkbox.get(l).click()
				} else if (a == false) {
					println ('checkbox' + l + ' is selected')
					println ('All are : ' + totalamount + '   //    But now have : ' + countItem)
				} else {
					KeywordUtil.markFailed('error : ' + a)
				}
			}
			swipeUp()
		}
	}

	//	@Keyword
	//	def swipeUp() {
	//		int device_Height = Mobile.getDeviceHeight()
	//		int device_Width = Mobile.getDeviceWidth()
	//		int x = device_Width / 2
	//		int startY = device_Height * 0.80
	//		int endY = device_Height * 0.20
	//		println("Screen width = " + device_Width)
	//		println("Screen height = " + device_Height)
	//		Mobile.swipe(x, startY, x, endY)
	//	}

	@Keyword
	def CheckPaymentType(String payment_type) {
		switch (payment_type) {
			case '1' :
				MobileElement cashTab = (MobileElement) driver.findElementById(staffId + 'rdoCash')
				assert cashTab.getAttribute("checked")
				break
			case '4' :
				MobileElement tmwTab = (MobileElement) driver.findElementById(staffId + 'rdoTMW')
				assert tmwTab.getAttribute("checked")
				MobileElement cashTab = (MobileElement) driver.findElementById(staffId + 'rdoCash')
				cashTab.click()
				break
		}
		switch (payment_type) {
			case '1' :
			case '4' :
				println ('paymentType is : ' + payment_type)
				MobileElement totalPrice = (MobileElement) driver.findElementById(staffId + 'txtCashPrice')
				MobileElement payPrice = (MobileElement) driver.findElementById(staffId + 'txtCashMoney')
				payPrice.sendKeys(totalPrice.getText())
				MobileElement confirmPayment = (MobileElement) driver.findElementById(staffId + 'btnConfirm')
				confirmPayment.click()
				MobileElement btnSkip = (MobileElement) driver.findElementById(staffId + 'btnSkip')
				btnSkip.click()
				return true
			case '2' :
				println ('paymentType is : ' + payment_type)
				MobileElement confirmYes = (MobileElement) driver.findElementById(staffId + 'dialog_confirm_yes')
				confirmYes.click()
				return true
		}
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
	def editQty(String editProduct, Integer editQty, Integer qty) {
		List<MobileElement> products = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		println products.size()
		for (int i = 0; i <= products.size(); i++) {
			if (products.get(i).getText().equals(editProduct)) {
				println products.get(i).getText()
				if (editQty > 0) {
					println 'plus'
					List<MobileElement> plus = driver.findElementsById(staffId + 'row_order_detail_iv_plus')
					for (int j = 1; j <= editQty; j++) {
						plus.get(i).click()
						println j
					}
				} else if (editQty < 0) {
					println 'minus'
					List<MobileElement> minus = driver.findElementsById(staffId + 'row_order_detail_iv_minus')
					for (int k = -1; k >= editQty; k--) {
						minus.get(i).click()
						println k
					}
				}
				List<MobileElement> amount = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
				KeywordUtil.logInfo('amount : ' + amount.get(i).getText() )
				assert Integer.parseInt(amount.get(i).getText()) == qty + editQty
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
	def checkProduct (String name, Integer qty, Double unitPrice, Integer countQty, Double countTotalPrice, Integer statusProduct, Integer status_id) {
		List<MobileElement> prods = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		List<MobileElement> qtys = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
		List<MobileElement> prices = driver.findElementsById(staffId + 'row_order_detail_tv_price')
		println ('unitPrice : ' + unitPrice)
		double numPrice = 0.0
		int numQty = 0
		for (int k = 0; k <= prods.size(); k++) {
			println ('product size : ' + prods.size())
			println ('element product : ' + prods.get(k).getText())
			println ('productName : ' + name)
			println prods.get(k).getText().equals(name)
			if (prods.get(k).getText().equals(name)) {
				println (prods.get(k).getText() + ' : Found')
				println ('k is : ' + k)
				KeywordUtil.logInfo('statusProduct : ' + statusProduct)
				switch (statusProduct) {
					case 0 :
						numQty = extractInt(qtys.get(k).getText())
						numPrice = Double.parseDouble(prices.get(k).getText())
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
						numPrice = Double.parseDouble(prices.get(k - 1).getText())
						break
				}
				KeywordUtil.logInfo('qty : ' + qty)
				KeywordUtil.logInfo('numQty : ' + numQty)
				assert numQty == qty
				
				KeywordUtil.logInfo('numPrice : ' + numPrice)
				KeywordUtil.logInfo('qty / unitPrice : ' + qty + '   /   ' + unitPrice)
				if (numPrice.equals(qty * unitPrice)) {
					countQty += qty
					countTotalPrice += numPrice
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
	def checkProductAssert(Double countTotalPrice, Double totalPrice, Integer countQty, Integer status_id) {
		KeywordUtil.logInfo ('countTotalPrice : ' + countTotalPrice)
		KeywordUtil.logInfo ('totalPrice : ' + totalPrice)
		KeywordUtil.logInfo ('countQty : ' + countQty)

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
	def testDeleteProduct (String editProduct) {
		List<MobileElement> products = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		List<MobileElement> amounts = driver.findElementsById(staffId + 'row_order_detail_tv_amount')
		List<MobileElement> minus = driver.findElementsById(staffId + 'row_order_detail_iv_minus')
		println ('Product size : ' + products.size())
		println ('minus btn have : ' + minus.size())
		println ('editProduct : ' + editProduct)
		println ('amounts : ' + amounts.size())
		for (int i = 0; i < products.size(); i++) {
			println ('Product : ' + products.get(i).getText())
			println (editProduct.equals(products.get(i).getText()))
			println ('i is : ' + i)
			println ('amounts : ' + amounts.get(i).getText())
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
		int statusProduct = 0
		int size = total_product
		double price = 0.00

		switch (statusCheck) {
			case 1 :
				switch (flow_type) {
					case '6' :
					size = total_product + 1
					break
					default :
					size = total_product
					break
				}
				break
		}
		return [qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price]
	}
}