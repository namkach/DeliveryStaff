package myPac

import java.util.concurrent.TimeUnit

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

public class RiderKeywords {
	AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()

	def riderId = 'th.co.gosoft.storemobile.sevendelivery.rider:id/'
	boolean checkOrder

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
	def FindOrder(String order_id, String payment_type) {
		checkOrder = FindOrderId(order_id, payment_type)
		//		Mobile.delay(2)
		//		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)
		while(!checkOrder) {
			SwipeUp()
			checkOrder = FindOrderId(order_id, payment_type)
			KeywordUtil.logInfo('checkOrder : ' + checkOrder)
		}
		return checkOrder
	}

	@Keyword
	def SwipeUp() {
		int x = Mobile.getDeviceWidth()/2
		int startY = Mobile.getDeviceHeight()*0.7
		int endY = Mobile.getDeviceHeight()*0.4
		Mobile.swipe(x, startY, x, endY)
	}

	@Keyword
	def FindOrderId(String order_id, String payment_type) {
		checkOrder = false
		List<MobileElement> orders = driver.findElementsById(riderId + 'txt_order_no')
		for (int j = orders.size() - 1; j >= 0; j--) {
			if (orders.get(j).getText().equals(order_id)) {
				KeywordUtil.markPassed ('*** order found ***')
				switch (payment_type) {
					case '1' :
						checkOrder = true
						break
					case '2' :
					case '4' :
						MobileElement paymentIcon = (MobileElement) driver.findElementById(riderId + 'img_payment_type')
						if (paymentIcon.isDisplayed()) {
							checkOrder = true
						}
						break
				}
				orders.get(j).click()
				MobileElement orderNo = (MobileElement) driver.findElementById(riderId + 'main_toolbar_tv_order')
				assert orderNo.getText().equals(order_id)
				break
			}
		}
		return checkOrder
	}

	@Keyword
	def ConfirmBtn(String order_id, Integer status_id, String payment_type, Double total_price) {
		checkOrder = false
		KeywordUtil.logInfo(order_id)
		MobileElement ConfirmOrder = (MobileElement) driver.findElementById(riderId + 'order_detail_bt_confirm')
		switch (status_id) {
			case 3 :
				assert ConfirmOrder.getText().equals('รับรายการสั่งซื้อ')
				ConfirmOrder.click()
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
				ConfirmPayment(payment_type, total_price)
				break
		}
		checkOrder = true
		KeywordUtil.markPassed('ConfirmBtn : Pass')
		status_id += 1
		return [checkOrder, status_id]
	}

	@Keyword
	def ConfirmPayment(String payment_type, Double total_price) {
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

			//check delivery fee
			//				if (total_price >= 100) {
			//					assert Double.parseDouble(totalPrice.getText()).equals(total_price)
			//				} else {
			//					assert Double.parseDouble(totalPrice.getText()).equals((total_price + 20))
			//				}

			//dont check delivery fee
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
		SwipeUp()
		MobileElement signBtn = (MobileElement) driver.findElementById(riderId + 'main_toolbar_tv_menu_right2')
		signBtn.click()

		MobileElement confirmSignBtn = (MobileElement) driver.findElementById(riderId + 'dialog_confirm_yes')
		confirmSignBtn.click()
	}

	@Keyword
	def checkStatusId(Integer status_id) {
		checkOrder = false
		MobileElement statusElement = (MobileElement) driver.findElementById(riderId + 'order_detail_time_count_down')
		KeywordUtil.logInfo('status text : ' + statusElement.getText())
		KeywordUtil.logInfo('status_id : ' + status_id)
		def text = statusElement.getText()
		switch (status_id) {
			case 5 :
				if (text.contains('เสร็จสมบูรณ์')) {
					checkOrder = true
				}
				break
			case 6 :
				if (text.contains('ยกเลิกออเดอร์')) {
					checkOrder = true
				}
				break
		}
		return checkOrder
	}

	@Keyword
	def checkTotalProducts(String flow_type, Integer total_product) {
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
			return true
		} else {
			KeywordUtil.logInfo ('false')
			return false
		}
	}

	@Keyword
	def checkEachProduct(String name, Integer qty, Double unitPrice, Integer countQty, Double countTotalPrice, Integer statusProduct) {
		KeywordUtil.logInfo ('Get countQty : ' + countQty)
		KeywordUtil.logInfo ('Get countTotalPrice : ' + countTotalPrice)
		KeywordUtil.logInfo ('Get statusProduct : ' + statusProduct)

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
				assert totalPrice.equals((qty * unitPrice))
				totalPrice = (qty * unitPrice)
				countQty += qty
				countTotalPrice += totalPrice
				KeywordUtil.logInfo('countQty : ' + countQty)
				KeywordUtil.logInfo('countTotalPrice : ' + countTotalPrice)
				return [countQty, countTotalPrice]
			}
		}
	}

	@Keyword
	def extractInt(String input) {
		return Integer.parseInt(input.replaceAll('[^0-9]', ''))
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
	def checkAllProducts(Double countTotalPrice, Double totalPrice, Integer countQty) {
		KeywordUtil.logInfo ('countTotalPrice : ' + countTotalPrice)
		KeywordUtil.logInfo ('totalPrice : ' + totalPrice)
		KeywordUtil.logInfo ('countQty : ' + countQty)

		MobileElement allTotalPrice = (MobileElement) driver.findElementById(riderId + 'order_detail_tv_total_price')
		MobileElement allQty = (MobileElement) driver.findElementById(riderId + 'order_detail_tv_total_list')
		double numAllTotalPrice = 0.00
		if (allTotalPrice.getText().contains('บาท')) {
			numAllTotalPrice = Double.parseDouble(allTotalPrice.getText().replace(' บาท',''))
		}
		printType(numAllTotalPrice)
		KeywordUtil.logInfo ('total price : ' + numAllTotalPrice)
		KeywordUtil.logInfo ('All QTY : ' + extractInt(allQty.getText()))

		assert numAllTotalPrice.equals(countTotalPrice)
		assert numAllTotalPrice.equals(totalPrice)
		assert extractInt(allQty.getText()).equals(countQty)
		KeywordUtil.markPassed('Check all products : Pass')
	}

	@Keyword
	def setDefault(Integer total_product) {
		int qty = 0
		double unitPrice = 0.00
		int countQty = 0
		double countTotalPrice = 0.00
		int statusProduct = 1
		int size = total_product
		double price = 0.00
		return [qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price]
	}
}
