import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk'
def riderId = 'th.co.gosoft.storemobile.sevendelivery.rider:id/'
def status = ''
def remark = '-'

int status_id = 3
int qty = 0
double unitPrice = 0.00
int countQty = 0
double countTotalPrice = 0.00
int statusProduct = 1
int size = Integer.parseInt(total_product)
double price = 0.00

try {
	Mobile.startApplication(path, true)
	Mobile.setText(findTestObject('Rider/RiderUsername'), 'nam123', 0)
	Mobile.setText(findTestObject('Rider/RiderPassword'), '1234', 0)
	Mobile.tap(findTestObject('Rider/LoginBtn'), 0)
} catch (Exception e) {
    KeywordUtil.markFailed('Crashed... ' + e)
	status = 'Fail'
	remark = 'Cannot start application'
} 

try {
    AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
	
    order_id = CustomKeywords.'myPac.RiderKeywords.checkOrderId'(order_id)
	KeywordUtil.logInfo ('size : ' + size)
	KeywordUtil.logInfo ('flow_type : ' + flow_type)
	
    KeywordUtil.logInfo('------------- new order -----------')
	checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id, payment_type)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
		KeywordUtil.markFailed(remark)
		return remark
	}
	CustomKeywords.'myPac.RiderKeywords.printType'(size)
	checkOrder = CustomKeywords.'myPac.RiderKeywords.checkTotalProducts'(flow_type, size)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to check total product at status_id ' + status_id
		KeywordUtil.markFailed(remark)
		return remark
	}
	
	String[] productName = [product_name1, product_name2, product_name3]
	Integer[] productQty = [Integer.parseInt(qty1), Integer.parseInt(qty2), Integer.parseInt(qty3)]
	Double[] productUnitPrice = [Double.parseDouble(unit_price1), Double.parseDouble(unit_price2), Double.parseDouble(unit_price3)]
	
	List<MobileElement> products = driver.findElementsById(riderId + 'row_order_detail_tv_name')
	for(int i = 0; i < products.size(); i++) {
		for (int j = 0; j < productName.size(); j++) {
			if ((products.get(i).getText() == edit_product) && (products.get(i).getText() == productName[j])) {
				switch (flow_type) {
					case '1' : 
						qty = productQty[j] + Integer.parseInt(edit_qty)
						(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(edit_product, qty, productUnitPrice[j], countQty, countTotalPrice, statusProduct)
						break
					case '3': 
						KeywordUtil.logInfo ('skip deleted product : ' + edit_product)
						statusProduct = 2
						break
				}
			} else if (products.get(i).getText() == productName[j]) {
				(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(productName[j], productQty[j], productUnitPrice[j], countQty, countTotalPrice, statusProduct)
			}
		}
	}
	if (flow_type == '2') {
		qty = Integer.parseInt(edit_qty)
		unitPrice = Double.parseDouble(edit_unit_price)
		(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(edit_product, qty, unitPrice, countQty, countTotalPrice, statusProduct)
	}
	KeywordUtil.logInfo  ('countQty : ' + countQty)
	KeywordUtil.logInfo  ('countTotalPrice : ' + countTotalPrice)
	
	switch (flow_type) {
		case '0' :
			price = Double.parseDouble(total_price)
			break
		case '1'..'3' :
			price = Double.parseDouble(edit_total_price)
			break
	}
	CustomKeywords.'myPac.RiderKeywords.checkAllProducts'(countTotalPrice, price, countQty)
	
	(checkOrder,status_id) = CustomKeywords.'myPac.RiderKeywords.ConfirmBtn'(order_id, status_id, payment_type, price)
	KeywordUtil.logInfo('status_id : ' + status_id)
	
	if (!checkOrder) {
		status = 'Fail'
		remark = 'Fail to confirm ' + order_id + ' at status_id 1'
		KeywordUtil.markFailed(remark)
		return remark
	}
				
	KeywordUtil.logInfo('------------- processing -----------')
	Mobile.tap(findTestObject('Rider/ProcessingTab'), 50)

	checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id, payment_type)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
		KeywordUtil.markFailed(remark)
		return remark
	}
	
	KeywordUtil.logInfo('total_product : ' + Integer.parseInt(total_product) + 
		'\n qty : ' + qty + 
		'\n countQty : ' + countQty + 
		'\n countTotalPrice : ' + countTotalPrice + 
		'\n statusProduct : ' + statusProduct + 
		'\n size : ' + size + 
		'\n price : ' + price + 
		'\n unitPrice : ' + unitPrice)
	(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.RiderKeywords.setDefault'(Integer.parseInt(total_product))
	KeywordUtil.logInfo('total_product : ' + Integer.parseInt(total_product) + 
		'\n qty : ' + qty + 
		'\n countQty : ' + countQty + 
		'\n countTotalPrice : ' + countTotalPrice + 
		'\n statusProduct : ' + statusProduct + 
		'\n size : ' + size + 
		'\n price : ' + price + 
		'\n unitPrice : ' + unitPrice)
	
	CustomKeywords.'myPac.RiderKeywords.printType'(size)
	checkOrder = CustomKeywords.'myPac.RiderKeywords.checkTotalProducts'(flow_type, size)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to check total product at status_id ' + status_id
		KeywordUtil.markFailed(remark)
		return remark
	}
	
	products = driver.findElementsById(riderId + 'row_order_detail_tv_name')
	for(int i = 0; i < products.size(); i++) {
		for (int j = 0; j < productName.size(); j++) {
			if ((products.get(i).getText() == edit_product) && (products.get(i).getText() == productName[j])) {
				switch (flow_type) {
					case '1' : 
						qty = productQty[j] + Integer.parseInt(edit_qty)
						(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(edit_product, qty, productUnitPrice[j], countQty, countTotalPrice, statusProduct)
						break
					case '3': 
						KeywordUtil.logInfo ('skip deleted product : ' + edit_product)
						statusProduct = 2
						break
				}
			} else if (products.get(i).getText() == productName[j]) {
				(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(productName[j], productQty[j], productUnitPrice[j], countQty, countTotalPrice, statusProduct)
			}
		}
	}
	if (flow_type == '2') {
		qty = Integer.parseInt(edit_qty)
		unitPrice = Double.parseDouble(edit_unit_price)
		(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(edit_product, qty, unitPrice, countQty, countTotalPrice, statusProduct)
	}
	println ('countQty : ' + countQty)
	println ('countTotalPrice : ' + countTotalPrice)
	
	switch (flow_type) {
		case '0' :
			price = Double.parseDouble(total_price)
			break
		case '1'..'3' :
			price = Double.parseDouble(edit_total_price)
			break
	}
	CustomKeywords.'myPac.RiderKeywords.checkAllProducts'(countTotalPrice, price, countQty)
	
	(checkOrder,status_id) = CustomKeywords.'myPac.RiderKeywords.ConfirmBtn'(order_id, status_id, payment_type, price)
	KeywordUtil.logInfo('status_id : ' + status_id)
	
	if (!checkOrder) {
		status = 'Fail'
		remark = 'Fail to confirm ' + order_id + ' at status_id 1'
		KeywordUtil.markFailed(remark)
		return remark
	}
	
	KeywordUtil.logInfo('------------- processed -----------')
	Mobile.tap(findTestObject('Rider/ProcessedTab'), 50)
	
	checkOrder = CustomKeywords.'myPac.RiderKeywords.FindOrder'(order_id, payment_type)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to find order' + order_id + ' at status_id ' + status_id
		return remark
	}
	
	KeywordUtil.logInfo('total_product : ' + Integer.parseInt(total_product) + 
		'\n qty : ' + qty + 
		'\n countQty : ' + countQty + 
		'\n countTotalPrice : ' + countTotalPrice + 
		'\n statusProduct : ' + statusProduct + 
		'\n size : ' + size + 
		'\n price : ' + price + 
		'\n unitPrice : ' + unitPrice)
	(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.RiderKeywords.setDefault'(Integer.parseInt(total_product))
	KeywordUtil.logInfo('total_product : ' + Integer.parseInt(total_product) + 
		'\n qty : ' + qty + 
		'\n countQty : ' + countQty + 
		'\n countTotalPrice : ' + countTotalPrice + 
		'\n statusProduct : ' + statusProduct + 
		'\n size : ' + size + 
		'\n price : ' + price + 
		'\n unitPrice : ' + unitPrice)
	
	checkOrder = CustomKeywords.'myPac.RiderKeywords.checkTotalProducts'(flow_type, size)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to check total product at status_id ' + status_id
		KeywordUtil.markFailed(remark)
		return remark
	}
	
	products = driver.findElementsById(riderId + 'row_order_detail_tv_name')
	for(int i = 0; i < products.size(); i++) {
		for (int j = 0; j < productName.size(); j++) {
			if ((products.get(i).getText() == edit_product) && (products.get(i).getText() == productName[j])) {
				switch (flow_type) {
					case '1' : 
						qty = productQty[j] + Integer.parseInt(edit_qty)
						(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(edit_product, qty, productUnitPrice[j], countQty, countTotalPrice, statusProduct)
						break
					case '3': 
						KeywordUtil.logInfo ('skip deleted product : ' + edit_product)
						statusProduct = 2
						break
				}
			} else if (products.get(i).getText() == productName[j]) {
				(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(productName[j], productQty[j], productUnitPrice[j], countQty, countTotalPrice, statusProduct)
			}
		}
	}
	
	if (flow_type == '2') {
		qty = Integer.parseInt(edit_qty)
		unitPrice = Double.parseDouble(edit_unit_price)
		(countQty, countTotalPrice) = CustomKeywords.'myPac.RiderKeywords.checkEachProduct'(edit_product, qty, unitPrice, countQty, countTotalPrice, statusProduct)
	}
	println ('countQty : ' + countQty)
	println ('countTotalPrice : ' + countTotalPrice)
	
	switch (flow_type) {
		case '0' :
			price = Double.parseDouble(total_price)
			break
		case '1'..'3' :
			price = Double.parseDouble(edit_total_price)
			break
	}
	CustomKeywords.'myPac.RiderKeywords.checkAllProducts'(countTotalPrice, price, countQty)
	
	checkOrder = CustomKeywords.'myPac.RiderKeywords.checkStatusId'(status_id)
	if (!(checkOrder)) {
		status = 'Fail'
		remark = 'Fail to check order ' + order_id + ' at status_id ' + status_id
		return remark
	} else {
		status = 'Pass'
	}
}
catch (Exception e) {
    KeywordUtil.markFailed('Crashed... ' + e)
	status = 'Fail'
	remark = e.toString()
} 

CustomKeywords.'myPac.writeExcel.writeRider'(order_id, flow_type, payment_type, status, remark)
KeywordUtil.logInfo('status : ' + status)