package myPac

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

public class StaffKeywords {
	def sid = '0'
	boolean checkOrder = false
	AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()

	@Keyword
	def checkOrderId(String order_id) {
		println(order_id.length())
		while (order_id.length() < 4) {
			order_id = ('0' + order_id)
			println(order_id.length())
			KeywordUtil.logInfo(order_id)
		}
		return order_id
	}

	@Keyword
	def FindOrder(String order_id) {
		Mobile.scrollToText(order_id)
		List<MobileElement> orders = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/txt_order_no')
		checkOrder = false
		for (int j = 0; j < orders.size(); j++) {
			println('Order in the screen is : ' + orders.get(j).getText())
			if (order_id == orders.get(j).getText()) {
				assert orders.get(j).getText().isEmpty() != true
				KeywordUtil.logInfo('scrollToText : Pass')
				orders.get(j).click()
				MobileElement orderNo = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/main_toolbar_tv_order')
				assert orderNo.getText() == order_id
				List<MobileElement> names = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')

				println ('order name size : ' + names.size())
				println 'Order Found'
				checkOrder = true
				return checkOrder
				break
			}
		}
	}

	@Keyword
	def ConfirmBtn(String status_id, String paymentType, String apkType) {
		def btnName = ''
		def deliveryType = '0'

		//get delivery type
		MobileElement deliveryTypeElement = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_tv_pickup_method')
		println ('deliveryTypeElement : ' + deliveryTypeElement.getText())
		switch (deliveryTypeElement.getText()) {
			case 'รับที่ร้าน' :
				deliveryType = '1'
				KeywordUtil.logInfo('check deliveryType : ' + deliveryType)
				break
			case 'จัดส่งถึงที่' :
				deliveryType = '2'
				KeywordUtil.logInfo('check deliveryType : ' + deliveryType)
				break
		}

		if (status_id == '1') {
			btnName = 'รับรายการสั่งซื้อ'
		} else if (status_id == '2') {
			btnName = 'ยืนยันการจัดสินค้า'
			sid = status_id
			println ('deliveryType : ' + deliveryType)
			println ('sid : ' + sid)

			if (deliveryType == '1') {
				sid = '3'
				KeywordUtil.logInfo('sid in deliveryType : ' + sid)
			} else if (deliveryType == '2') {
				sid = '4'
				KeywordUtil.logInfo('sid in deliveryType : ' + sid)
			} else {
				KeywordUtil.markFailed('error to find sid in delivery')
			}
			countItem()
		} else if (status_id == '3' || status_id == '4') {
			println ('payment : ' + paymentType)
			if (apkType == 'rider') {
				btnName = 'ยืนยันการส่งสินค้า'
			} else if (apkType == 'cod') {
				if (paymentType == '1') {
					btnName = 'ชำระเงิน'
				} else if (paymentType == '2') {
					btnName = 'ยืนยันการส่งสินค้า'
				}
			}
		} else {
			KeywordUtil.markFailed('error dont find status id : ' + status_id)
		}
		println ('btn : ' + btnName)

		MobileElement confirmOrder = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_bt_confirm')
		println confirmOrder.getText()
		assert confirmOrder.getText() == btnName
		if (confirmOrder.getText() == btnName) {
			checkOrder = true
		} else {
			checkOrder = false
		}
		confirmOrder.click()
		KeywordUtil.logInfo ('Order confirmed')
		KeywordUtil.logInfo ('status_id : ' + status_id)
		KeywordUtil.logInfo ('paymentType : ' + paymentType)
		switch (status_id) {
			case '1' :
				sid = '2'
				paymentType = '0'
				println ('sid : ' + sid)
				println ('paymentType : ' + paymentType)
				break

			//confirm LP
			case '2' :
			//get payment type
				println ('sid : ' + sid)
				println ('paymentType : ' + paymentType)
				MobileElement payment = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_lp_payment_type')
				switch (payment.getText()) {
					case 'ชำระเงินสด' :
						paymentType = '1'
						break
					case 'True Money' :
						paymentType = '2'
						break
					case 'ทรูมันนี่ปลายทาง' :
						paymentType = '3'
						break
					default :
						paymentType = '4'
						println 'error'
						KeywordUtil.markFailed('error to get payment type')
						break
				}
				println ('payment type : ' + paymentType)
			//confirm LP
				MobileElement confirmLp = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_lp_buy_button')
				confirmLp.click()
				break

			//check payment
			case '3'..'4' :
			if (apkType == 'rider') {
				MobileElement confirmYes = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_confirm_yes')
				confirmYes.click()
				sid = '5'
			} else if (apkType == 'cod') {
				KeywordUtil.logInfo ('status id : ' + status_id)
				boolean checkPayment = CheckPaymentType(paymentType)
				KeywordUtil.logInfo('checkPayment : ' + checkPayment)
				if (checkPayment) {
					sid = '5'
				} else {
					sid = '7'
					KeywordUtil.markFailed('error sid = 7 payment Function')
				}
			}
			
			// for Rider
			//				MobileElement confirmYes = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_confirm_yes')
			//				confirmYes.click()
			//				sid = '5'

			//for COD
//				KeywordUtil.logInfo ('status id : ' + status_id)
//				boolean checkPayment = CheckPaymentType(paymentType)
//				KeywordUtil.logInfo('checkPayment : ' + checkPayment)
//				if (checkPayment) {
//					sid = '5'
//				} else {
//					sid = '7'
//					KeywordUtil.markFailed('error sid = 7 payment Function')
//				}
				break
			default :
				KeywordUtil.logInfo ('error status_id : ' + status_id)
		}
		return [checkOrder, sid, paymentType, deliveryType]
	}

	@Keyword
	def CancelBtn(String flow_type) {
		checkOrder = false
		MobileElement cancelBtn = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_bt_cancel')
		cancelBtn.click()
		List<MobileElement> cancelOption = driver.findElementsByClassName('android.widget.RadioButton')
		println cancelOption.size()
		for (int i = 0; i < cancelOption.size(); i++) {
			if (cancelOption.get(i).getText() == 'รายการทดสอบ') {
				println cancelOption.get(i).getText()
				cancelOption.get(i).click()

				MobileElement cancelConfirm = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_order_cancel_confirm_bt_yes')
				assert cancelConfirm.getText() == 'ยืนยัน'
				cancelConfirm.click()
				KeywordUtil.logInfo('PASS CANCEL Status : ' + flow_type)
				checkOrder = true
				return checkOrder
				break
			}
		}
	}

	@Keyword
	def countItem() {
		MobileElement totalAmountElement = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_tv_total_list')
		println totalAmountElement.getText()
		List<MobileElement> eachItemAmount = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_amount')
		println ('this element has : ' + eachItemAmount.size() + ' item(s)')

		Keywords kw = new Keywords()
		int totalamount = extractInt(totalAmountElement.getText())

		int countItem = 0
		List<MobileElement> checkbox = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_cb_is_pick')
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

	@Keyword
	def swipeUp() {
		int device_Height = Mobile.getDeviceHeight()
		int device_Width = Mobile.getDeviceWidth()
		int x = device_Width / 2
		int startY = device_Height * 0.80
		int endY = device_Height * 0.20
		println("Screen width = " + device_Width)
		println("Screen height = " + device_Height)
		Mobile.swipe(x, startY, x, endY)
	}

	@Keyword
	def CheckPaymentType(String paymentType) {
		switch (paymentType) {
			case '1' :
				println ('paymentType is : ' + paymentType)
				MobileElement totalPrice = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/txtCashPrice')
				MobileElement payPrice = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/txtCashMoney')
				payPrice.sendKeys(totalPrice.getText())
				MobileElement confirmPayment = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/btnConfirm')
				confirmPayment.click()
				MobileElement btnSkip = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/btnSkip')
				btnSkip.click()
				KeywordUtil.markPassed('Payment success')
				return true
				break
			case '2' :
				println ('paymentType is : ' + paymentType)
				MobileElement confirmYes = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_confirm_yes')
				confirmYes.click()
				KeywordUtil.markPassed('Payment success')
				return true
				break
			case '3' :
				println 'Payment error'
				KeywordUtil.markFailed('Wrong payment type')
				return false
		}
	}

	@Keyword
	def checkStatusId(String status_id) {
		MobileElement statusElement = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_time_count_down')
		switch (status_id) {
			case '5' :
				assert statusElement.getText() == 'เสร็จสมบูรณ์'
				KeywordUtil.logInfo('เสร็จสมบูรณ์')
				return true
				break
			case '6' :
				assert statusElement.getText() == 'ยกเลิกออเดอร์'
				KeywordUtil.logInfo('ยกเลิกออเดอร์')
				return true
				break
			default :
				KeywordUtil.markFailed('Error to check status_id')
				return false
				break
		}
	}

	@Keyword
	def editQty(String editProduct, String quantity, Integer oldQty) {
		def qty = Integer.parseInt(quantity)
		println qty
		List<MobileElement> products = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
		println products.size()
		for (int i = 0; i <= products.size(); i++) {
			if (products.get(i).getText().equals(editProduct)) {
				println products.get(i).getText()
				if (qty > 0) {
					println 'plus'
					List<MobileElement> plus = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_iv_plus')
					for (int j = 1; j <= qty; j++) {
						plus.get(i).click()
						println j
					}
					List<MobileElement> amount = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_amount')
					KeywordUtil.logInfo('amount : ' + amount.get(i).getText() )
					assert Integer.parseInt(amount.get(i).getText()) == oldQty + qty
				} else if (qty < 0) {
					println 'minus'
					List<MobileElement> minus = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_iv_minus')
					for (int k = -1; k >= qty; k--) {
						minus.get(i).click()
						println k
					}
					List<MobileElement> amount = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_amount')
					KeywordUtil.logInfo('amount : ' + amount.get(i).getText())
					assert Integer.parseInt(amount.get(i).getText()) == oldQty - qty
				}
				break
			}
		}
	}

	@Keyword
	def addProduct(String editProduct, String quantity) {
		int qty = 0
		qty = Integer.parseInt(quantity)
		MobileElement cart = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_cv_add')
		cart.click()
		MobileElement search = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/main_et_search')
		search.sendKeys(editProduct + '\\n')
		MobileElement products = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/layout_product_name_tv_label')
		println products.getText()
		products.click()
		if (qty > 1) {
			MobileElement plus = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/product_detail_iv_plus')
			for (int j = 2; j <= qty; j++) {
				plus.click()
				println ('qty : ' + qty + '    //   j : ' + j)
			}
		}
		MobileElement addCart = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/product_detail_bt_add')
		addCart.click()
	}

	@Keyword
	def deleteProduct (String editProduct) {
		List<MobileElement> products = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
		List<MobileElement> amounts = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_amount')
		List<MobileElement> minus = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_iv_minus')
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
	def checkProduct (String name, Integer qty, String unitPrice, Integer countQty, Integer countTotalPrice, Integer statusProduct) {
		List<MobileElement> prods = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
		List<MobileElement> qtys = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_amount')
		List<MobileElement> prices = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_price')
		for (int k = 0; k <= prods.size(); k++) {
			println ('product size : ' + prods.size())
			println ('element product : ' + prods.get(k).getText())
			println ('productName : ' + name)
			println prods.get(k).getText().equals(name)
			println 'Round : ' + k
			if (prods.get(k).getText().equals(name)) {
				println (prods.get(k).getText() + ' : Found')
				println ('k is : ' + k)
				if (statusProduct == -1) {
					//check each QTY
					KeywordUtil.logInfo('qtys : ' + qtys.get(k - 1).getText())
					int numQty = extractInt(qtys.get(k - 1).getText())
					KeywordUtil.logInfo('qty : ' + qty)
					KeywordUtil.logInfo('numQty : ' + numQty)
					assert numQty == qty

					//check each total price
					double numPrice = Double.parseDouble(prices.get(k - 1).getText())
					double checkUnitPrice = Double.parseDouble(unitPrice)
					assert numPrice == (qty * checkUnitPrice)
					numPrice = (qty * checkUnitPrice)

					countQty += qty
					countTotalPrice += numPrice
					KeywordUtil.logInfo('countQty : ' + countQty)
					KeywordUtil.logInfo('countTotalPrice : ' + countTotalPrice)
					return [countQty, countTotalPrice]
					break
				} else if (statusProduct == 1 || statusProduct == -2) {
					//check each QTY
					KeywordUtil.logInfo('qtys : ' + qtys.get(k).getText())
					int numQty = extractInt(qtys.get(k).getText())
					KeywordUtil.logInfo('qty : ' + qty)
					KeywordUtil.logInfo('numQty : ' + numQty)
					println '-----------'
					assert numQty.toString() == qty.toString()

					double numPrice = 0.0
					//check each total price
					if(statusProduct == 1) {
						numPrice = Double.parseDouble(prices.get(k).getText())
					} else if (statusProduct == -2) {
						numPrice = Double.parseDouble(prices.get(k-1).getText())
					}
					double checkUnitPrice = Double.parseDouble(unitPrice)
					println '-----------'
					assert numPrice == (qty * checkUnitPrice)
					numPrice = (qty * checkUnitPrice)

					countQty += qty
					countTotalPrice += numPrice
					KeywordUtil.logInfo('countQty : ' + countQty)
					KeywordUtil.logInfo('countTotalPrice : ' + countTotalPrice)
					return [countQty, countTotalPrice]
					break
				}
			}
		}
	}

	@Keyword
	def checkProductAssert(Integer countTotalPrice, String totalPrice, Integer countQty) {
		KeywordUtil.logInfo ('countTotalPrice : ' + countTotalPrice)
		KeywordUtil.logInfo ('totalPrice : ' + totalPrice)
		KeywordUtil.logInfo ('countQty : ' + countQty)

		MobileElement allTotalPrice = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_tv_total_price')
		MobileElement allQty = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/order_detail_tv_total_list')
		String numAllTotalPrice = ''
		if (allTotalPrice.getText().contains('บาท')) {
			numAllTotalPrice = allTotalPrice.getText().replace('บาท','')
		}
		KeywordUtil.logInfo ('total price : ' + Double.parseDouble(numAllTotalPrice))
		KeywordUtil.logInfo ('All QTY : ' + extractInt(allQty.getText()))

		assert Double.parseDouble(numAllTotalPrice) == countTotalPrice
		assert Double.parseDouble(numAllTotalPrice) == Double.parseDouble(totalPrice)
		assert extractInt(allQty.getText()) == countQty
	}

	@Keyword
	def testDeleteProduct (String editProduct) {
		List<MobileElement> products = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
		List<MobileElement> amounts = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_amount')
		List<MobileElement> minus = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_iv_minus')
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
}





