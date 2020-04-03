package myPac

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil
import com.sun.org.apache.xpath.internal.compiler.Keywords

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
	def FindOrder(String order_id) {
		checkOrder = FindOrderId(order_id)
		while(!checkOrder) {
			SwipeUp()
			checkOrder = FindOrderId(order_id)
			KeywordUtil.logInfo('checkOrder : ' + checkOrder)
		}
		return checkOrder
	}

	@Keyword
	def SwipeUp() {
		int x = Mobile.getDeviceWidth()/2
		int startY = Mobile.getDeviceHeight()*0.8
		int endY = Mobile.getDeviceHeight()*0.2
		Mobile.swipe(x, startY, x, endY)
	}

	@Keyword
	def FindOrderId(String order_id) {
		checkOrder = false
		List<MobileElement> orders = driver.findElementsById(riderId + 'txt_order_no')
		for (int j = orders.size() - 1; j >= 0; j--) {
			if (orders.get(j).getText() == order_id) {
				checkOrder = true
				orders.get(j).click()
				MobileElement orderNo = (MobileElement) driver.findElementById(riderId + 'main_toolbar_tv_order')
				assert orderNo.getText() == order_id
				break
			}
		}
		if (checkOrder) {
			return true
		} else {
			return false
		}
	}

	@Keyword
	def ConfirmBtn(String order_id, Integer status_id) {
		checkOrder = false
		KeywordUtil.logInfo(order_id)
		MobileElement ConfirmOrder = (MobileElement) driver.findElementById(riderId + 'order_detail_bt_confirm')
		if (status_id == 3) {
			assert ConfirmOrder.getText() == 'รับรายการสั่งซื้อ'
		} else if (status_id == 4) {
			assert ConfirmOrder.getText() == 'ชำระเงิน'
		}
		ConfirmOrder.click()
		
		if (status_id == 4) {
			ConfirmPayment()
		}
		checkOrder = true
		KeywordUtil.markPassed('ConfirmBtn : Pass')
		status_id += 1
		return [checkOrder, status_id]
	}
	
	@Keyword
	def ConfirmPayment() {
		MobileElement totalPrice = (MobileElement) driver.findElementById(riderId + 'txtCashPrice')
		MobileElement payPrice = (MobileElement) driver.findElementById(riderId + 'txtCashMoney')
		payPrice.sendKeys(totalPrice.getText())
		MobileElement confirmPayment = (MobileElement) driver.findElementById(riderId + 'btnConfirm')
		confirmPayment.click()
		MobileElement btnSkip = (MobileElement) driver.findElementById(riderId + 'btnSkip')
		btnSkip.click()

		MobileElement walk = (MobileElement) driver.findElementById(riderId + 'delivery_confirm_rd_walk')
		walk.click()
		MobileElement confirmDelivery = (MobileElement) driver.findElementById(riderId + 'delivery_confirm_bt_confirm')
		confirmDelivery.click()

		SwipeUp()
		MobileElement signBtn = (MobileElement) driver.findElementById(riderId + 'main_toolbar_tv_menu_right2')
		signBtn.click()

		MobileElement confirmSignBtn = (MobileElement) driver.findElementById(riderId + 'dialog_confirm_yes')
		confirmSignBtn.click()
	}

	@Keyword
	def checkStatusId(Integer status_id) {
		MobileElement statusElement = (MobileElement) driver.findElementById(riderId + 'order_detail_time_count_down')
		switch (status_id) {
			case 5 :
				assert statusElement.getText() == 'เสร็จสมบูรณ์'
				KeywordUtil.logInfo('เสร็จสมบูรณ์')
				return true
				break
			case 6 :
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
	def checkTotalProduct(Integer total_product) {
		List<MobileElement> prods = driver.findElementsById(riderId + 'row_order_detail_tv_name')
		println ('Product size : ' + prods.size().toString())
		println ('Total product : ' + total_product.toString())
		if (prods.size().toString() == total_product.toString()) {
			return true
		} else {
			return false
		}
	}
	
	@Keyword
	def checkEachProduct (String name, Integer qty, Double unitPrice, Integer countQty, Double countTotalPrice, Integer statusProduct) {
		List<MobileElement> prods = driver.findElementsById(riderId + 'row_order_detail_tv_name')
		List<MobileElement> qtys = driver.findElementsById(riderId + 'row_order_detail_tv_amount')
		List<MobileElement> prices = driver.findElementsById(riderId + 'row_order_detail_tv_price')
		for (int k = 0; k <= prods.size(); k++) {
			println ('product size : ' + prods.size())
			println ('element product : ' + prods.get(k).getText())
			println ('have to found : ' + name)
			println prods.get(k).getText().equals(name)
			println 'Round : ' + k
			if (prods.get(k).getText().equals(name)) {
				println (prods.get(k).getText() + ' : Found')
				println ('k is : ' + k)
				
				switch (statusProduct) {
					case -1 : 
						//check each QTY
						KeywordUtil.logInfo('qtys : ' + qtys.get(k - 1).getText())
						int numQty = extractInt(qtys.get(k - 1).getText())
						KeywordUtil.logInfo('qty : ' + qty)
						KeywordUtil.logInfo('numQty : ' + numQty)
						assert numQty == qty
	
						//check each total price
						double totalPrice = Double.parseDouble(prices.get(k - 1).getText())
//						double checkUnitPrice = Integer.parseInt(unitPrice)
						assert totalPrice == (qty * unitPrice)
						totalPrice = (qty * unitPrice)
	
						countQty += qty
						countTotalPrice += totalPrice
						KeywordUtil.logInfo('countQty : ' + countQty)
						KeywordUtil.logInfo('countTotalPrice : ' + countTotalPrice)
						return [countQty, countTotalPrice]
						break
					case 1 :
					case -2 : 
						//check each QTY
						KeywordUtil.logInfo('qtys : ' + qtys.get(k).getText())
						int numQty = extractInt(qtys.get(k).getText())
						KeywordUtil.logInfo('qty : ' + qty)
						KeywordUtil.logInfo('numQty : ' + numQty)
						println '-----------'
						assert numQty == qty
	
						double totalPrice = 0.00
						//check each total price
						if(statusProduct == 1) {
							totalPrice = Double.parseDouble(prices.get(k).getText())
						} else if (statusProduct == -2) {
							totalPrice = Double.parseDouble(prices.get(k-1).getText())
						}
						println '-----------'
						assert totalPrice == (qty * unitPrice)
						totalPrice = (qty * unitPrice)
						
	
						countQty += qty
						countTotalPrice += totalPrice
						KeywordUtil.logInfo('countQty : ' + countQty)
						KeywordUtil.logInfo('countTotalPrice : ' + countTotalPrice)
						return [countQty, countTotalPrice]
						break
				}
			}
		}
	}
	
	@Keyword
	def extractInt(String input) {
		return Integer.parseInt(input.replaceAll("[^0-9]", ""))
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

		assert numAllTotalPrice == countTotalPrice
		assert numAllTotalPrice == totalPrice
		assert extractInt(allQty.getText()) == countQty
		KeywordUtil.markPassed('Check all products : Pass')
	}
}
