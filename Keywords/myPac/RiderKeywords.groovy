package myPac

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
	def FindOrder(String order_id) {
		checkOrder = FindOrderId(order_id)
		while(!checkOrder) {
			SwipeUp()
			checkOrder = FindOrderId(order_id)
			KeywordUtil.logInfo('checkOrder : ' + checkOrder)
		}

		//		Mobile.scrollToText(order_id)
		//		List<MobileElement> orders = driver.findElementsById(riderId + 'txt_order_no')
		//		checkOrder = false
		//		for (int j = 0; j < orders.size(); j++) {
		//			println('Order in the screen is : ' + orders.get(j).getText())
		//			if (order_id == orders.get(j).getText()) {
		//				assert orders.get(j).getText().isEmpty() != true
		//				KeywordUtil.logInfo('scrollToText : Pass')
		//				orders.get(j).click()
		//				MobileElement orderNo = (MobileElement) driver.findElementById(riderId + 'main_toolbar_tv_order')
		//				assert orderNo.getText() == order_id
		//				List<MobileElement> names = driver.findElementsById(riderId + 'row_order_detail_tv_name')
		//
		//				println ('order name size : ' + names.size())
		//				println 'Order Found'
		//				checkOrder = true
		//				return checkOrder
		//				break
		//			}
		//		}

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
		MobileElement orderNo = (MobileElement) driver.findElementById(riderId + 'order_detail_bt_confirm')
		if (status_id == 3) {
			assert orderNo.getText() == 'รับรายการสั่งซื้อ'
		} else if (status_id == 4) {
			assert orderNo.getText() == 'ชำระเงิน'
		}
		orderNo.click()
		return status_id + 1
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
}
