import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk'
Mobile.startApplication(path, true)
Mobile.setText(findTestObject('Rider/RiderUsername'), 'nam123', 0)
Mobile.setText(findTestObject('Rider/RiderPassword'), '1234', 0)
Mobile.tap(findTestObject('Rider/LoginBtn'), 0)

int status_id = 3
def riderId = 'th.co.gosoft.storemobile.sevendelivery.rider:id/'
def remark = ''
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
	
	checkOrder = CustomKeywords.'myPac.RiderKeywords.checkTotalProduct'(total_product)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to check total product at status_id ' + status_id
		return remark
	}
	
	int qty = 0
	int countQty = 0
	double countTotalPrice = 0.00
	int statusProduct = 1
	
	String[] productName = [product_name1, product_name2, product_name3]
	Integer[] productQty = [qty1, qty2, qty3]
	Double[] productUnitPrice = [unit_price1, unit_price2, unit_price3]
	
	int size = 0
	size = total_product
	
	if (flow_type == 2) {
		size -= 1
	}
	
	for(int i = 0; i < size; i++) {
		qty = productQty[i]
		if (flow_type == 1 && productName[i].equals(edit_product)) {
				qty += edit_qty
		} else if (statusProduct == 0) {
			statusProduct = -1	
		} else if (flow_type == 3 && productName[i] == edit_product) {
			println ('skip deleted product : ' + edit_product)
			statusProduct = -2
			continue
		}
		KeywordUtil.logInfo ('2. qty of product ' +  i + ' is : ' + qty)
		(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(productName[i], qty, productUnitPrice[i], countQty, countTotalPrice, statusProduct)
	}
	
	if (flow_type == 2) {
		qty = edit_qty
		(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct)
	}
	println ('countQty : ' + countQty)
	println ('countTotalPrice : ' + countTotalPrice)
	
	switch (flow_type) {
		case 0 :
			CustomKeywords.'myPac.RiderKeywords.checkAllProducts'(countTotalPrice, total_price, countQty)
			break
		case 1..3 :
			CustomKeywords.'myPac.RiderKeywords.checkAllProducts'(countTotalPrice, edit_total_price, countQty)
			break
	}
	
//	(checkOrder,status_id) = CustomKeywords.'myPac.RiderKeywords.ConfirmBtn'(order_id, status_id)
//	KeywordUtil.logInfo('status_id : ' + status_id)
//	
//	if (!checkOrder) {
//		status = 'Fail'
//		remark = 'Fail to confirm ' + order_id + ' at status_id ' + status_id
//		return remark
//	}
//				
//	KeywordUtil.logInfo('------------- processing -----------')
//	Mobile.tap(findTestObject('Rider/ProcessingTab'), 30)
//	
//	checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id)
//	if (!(checkOrder)) {
//		status = 'Fail'
//		remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
//		return remark
//	}
//	
//	if (flow_type == '0') {
//		status_id = CustomKeywords.'myPac.RiderKeywords.ConfirmBtn'(order_id, status_id)
//		KeywordUtil.logInfo('status_id : ' + status_id)
//	}
//	
//	KeywordUtil.logInfo('------------- processed -----------')
//	Mobile.tap(findTestObject('Rider/ProcessedTab'), 30)
//	
//	checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id)
//	if (!(checkOrder)) {
//		status = 'Fail'
//		remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
//		return remark
//	}
//	
//	if (flow_type == '0') {
//		checkOrder = CustomKeywords.'myPac.RiderKeywords.checkStatusId'(status_id)
//	}
//	if (!(checkOrder)) {
//		status = 'Fail'
//		remark = 'Fail to check order ' + order_id + ' at status_id ' + status_id
//		return remark
//	}
}
catch (Exception e) {
    KeywordUtil.markFailed('Crashed... ' + e)
} 

KeywordUtil.logInfo('remark : ' + remark)
