import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk'
Mobile.startApplication(path, true)
Mobile.setText(findTestObject('Rider/RiderUsername'), 'nam123', 0)
Mobile.setText(findTestObject('Rider/RiderPassword'), '1234', 0)
Mobile.tap(findTestObject('Rider/LoginBtn'), 0)

int status_id = 3
def riderId = 'th.co.gosoft.storemobile.sevendelivery.rider:id/'
try {
    AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()

    order_id = CustomKeywords.'myPac.RiderKeywords.checkOrderId'(order_id)

    KeywordUtil.logInfo('------------- new order -----------')
    checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id)
	
    if (!(checkOrder)) {
        status = 'Fail'
        remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
        return remark
    }
	
	if (flow_type == '0') {
		status_id = CustomKeywords.'myPac.RiderKeywords.ConfirmBtn'(order_id, status_id)
		KeywordUtil.logInfo('status_id : ' + status_id)
	}
	
	KeywordUtil.logInfo('------------- processing -----------')
	Mobile.tap(findTestObject('Rider/ProcessingTab'), 30)
	
	checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
		return remark
	}
	
	if (flow_type == '0') {
		status_id = CustomKeywords.'myPac.RiderKeywords.ConfirmBtn'(order_id, status_id)
		KeywordUtil.logInfo('status_id : ' + status_id)
		
		
	}
	
	KeywordUtil.logInfo('------------- processed -----------')
	Mobile.tap(findTestObject('Rider/ProcessedTab'), 30)
	
	checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
		return remark
	}
	
	if (flow_type == '0') {
		checkOrder = CustomKeywords.'myPac.RiderKeywords.checkStatusId'(status_id)
	}
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to check order ' + order_id + ' at status_id ' + status_id
		return remark
	}
	
}
catch (Exception e) {
    KeywordUtil.markFailed('Crashed... ' + e)
} 

