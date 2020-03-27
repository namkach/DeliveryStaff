package myPac

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

public class Keywords {
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
}
