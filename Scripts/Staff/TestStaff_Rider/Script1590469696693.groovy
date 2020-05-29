import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement

def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.6.0.d_RIDER_28052020.apk'
def staffId = 'th.co.gosoft.storemobile.sevendelivery.staff:id/'
try {
	Mobile.startApplication(path, true)
	
	AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
	Mobile.delay(8)
	
	MobileElement cpBtn = driver.findElementByClassName('android.widget.Button')
	KeywordUtil.logInfo('text : ' + cpBtn.getText())
	cpBtn.click()
	
	Mobile.setText(findTestObject('Staff/Username'), 'sunitakac', 0)
	Mobile.setText(findTestObject('Staff/Password'), 'nazoru504', 0)
	Mobile.tap(findTestObject('Staff/LogonBtn'), 0)
	
	MobileElement pin1 = (MobileElement) driver.findElementById(staffId + 'pin_1')
	for (int i = 0; i < 12; i++) {
		pin1.click()
	}
	
	Mobile.setText(findTestObject('Staff/StoreId'), store_id, 0)
	Mobile.tap(findTestObject('Staff/LoginBtn'), 0)
	Mobile.tap(findTestObject('Staff/confirmLogin'), 0)
	
} catch (Exception e) {
	KeywordUtil.markFailed('Crashed... ' + e)
	status = 'Fail'
	remark = 'Cannot start application'
	CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
}

try {
	AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
	int oldQty = 0
	def remark = '-'
	def status = ''
//	def apkType = ''
	int status_id = 1
	int size = total_product
	def name = ''
	int qty = 0
	def unitPrice = ''
	int countQty = 0
	int countTotalPrice = 0
	int statusCheck = 0
	int statusProduct = 0
	
	String[] productName = [product_name1, product_name2, product_name3]
	Integer[] productQty = [qty1, qty2, qty3]
	Double[] productUnitPrice = [unit_price1, unit_price2, unit_price3]
	
	KeywordUtil.logInfo('------------- new order -----------')
	(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.findOrder'(order_id, status_id)
	if (status.equals('Fail')) {
		return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
	}
	
	(status, remark, size) = CustomKeywords.'myPac.StaffKeywords_Rider.checkTotalProducts'(flow_type, size, status_id, statusCheck)
	if (status.equals('Fail')) {
		return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
	}
	
	for(int i = 0; i < size; i++) {
		KeywordUtil.logInfo ('qty : ' + productQty[i])
		(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(productName[i], productQty[i], productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		println ('countQty : ' + countQty)
		println ('countTotalPrice : ' + countTotalPrice)
	}
	(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
	if (status.equals('Fail')) {
		return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
	}
	
	switch (flow_type) {
		//new order
		case '1' :
			(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.cancelBtn'(flow_type, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			status_id = 6
			break
		//new order
		default:
			(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords_Rider.confirmBtn'(status_id, payment_type, delivery_type, rider_name)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			break
	}
	
//	status_id = 2
	
	KeywordUtil.logInfo('------------- process -----------')
	if (flow_type != '1') {
		MobileElement processTab = (MobileElement) driver.findElementById(staffId + 'navigation_in_process')
		processTab.click()
		
		(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.findOrder'(order_id, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords_Rider.setDefault'(total_product, flow_type, statusCheck)
		
		(status, remark, size) = CustomKeywords.'myPac.StaffKeywords_Rider.checkTotalProducts'(flow_type, size, status_id, statusCheck)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		for(int i = 0; i < size; i++) {
			println ('qty : ' + productQty[i])
//			KeywordUtil.logInfo ('statusProduct : ' + statusProduct)
			(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(productName[i], productQty[i], productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			println ('countQty : ' + countQty)
			println ('countTotalPrice : ' + countTotalPrice)
		}
		
		(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		switch (flow_type) {
			case '2' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.cancelBtn'(flow_type, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				status_id = 6
				break
			default :
				switch (flow_type) {
					case '5' :
						println ('Edit QTY')
						println ('QTY : ' + edit_qty)
						println edit_product
						switch (edit_product) {
							case product_name1 :
								oldQty = qty1
								break
							case product_name2 :
								oldQty = qty2
								break
							case product_name3 :
								oldQty = qty3
								break
						}
						qty = edit_qty
						CustomKeywords.'myPac.StaffKeywords_Rider.editQty'(edit_product, qty, oldQty)
						break
					case '6' :
						println ('Add product')
						qty = edit_qty
						CustomKeywords.'myPac.StaffKeywords_Rider.addProduct'(edit_product, qty)
						break
					case '7' :
						println ('Delete product')
						CustomKeywords.'myPac.StaffKeywords_Rider.deleteProduct'(edit_product)
						break
				}
				KeywordUtil.logInfo('------------- check after edit product -----------')
				statusCheck = 1
				KeywordUtil.logInfo ('statusCheck : ' + statusCheck)
				switch (flow_type) {
					case '5'..'7' :
						(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords_Rider.setDefault'(total_product, flow_type, statusCheck)
						
						(status, remark, size) = CustomKeywords.'myPac.StaffKeywords_Rider.checkTotalProducts'(flow_type, size, status_id, statusCheck)
						if (status.equals('Fail')) {
							return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
						}

						List<MobileElement> products = driver.findElementsById(staffId + 'row_order_detail_tv_name')
						for(int i = 0; i < products.size(); i++) {
							for (int j = 0; j < productName.size(); j++) {
								if (products.get(i).getText() == productName[j]) {
									qty = productQty[j]
									if (products.get(i).getText() == edit_product) {
										switch (flow_type) {
											case '5' :
												qty += edit_qty
												break
											case '7':
												KeywordUtil.logInfo ('skip deleted product : ' + edit_product)
												statusProduct = 1
												continue
										}
									}
									(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(productName[j], qty, productUnitPrice[j], countQty, countTotalPrice, statusProduct, status_id)
									if (status.equals('Fail')) {
										return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
									}
								}
							}
						}
						
						switch (flow_type) {
							case '6' :
								qty = edit_qty
								(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
								if (status.equals('Fail')) {
									return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
								}
								break
						}
						println ('countQty : ' + countQty)
						println ('countTotalPrice : ' + countTotalPrice)
						(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkAllProducts'(countTotalPrice, edit_total_price, countQty, status_id)
						if (status.equals('Fail')) {
							return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
						}
						break
				}
				
//				if (delivery_type == '1') {
					
				KeywordUtil.logInfo('status_id : ' + status_id)
				KeywordUtil.logInfo('payment_type : ' + payment_type)
				KeywordUtil.logInfo('delivery_type : ' + delivery_type)
				KeywordUtil.logInfo('rider_name : ' + rider_name)
					(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords_Rider.confirmBtn'(status_id, payment_type, delivery_type, rider_name)
					if (status.equals('Fail')) {
						return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
					}
//				}
				break
		}
	}
	
//	Mobile.delay(8)
//	status_id = 3

		KeywordUtil.logInfo('------------- shipping -----------')
		MobileElement shippingTab = (MobileElement) driver.findElementById(staffId + 'navigation_for_shipping')
		shippingTab.click()
		
		if (flow_type != '1' && flow_type != '2') {
			(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.findOrder'(order_id, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			
			(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords_Rider.setDefault'(total_product, flow_type, statusCheck)
			
			(status, remark, size) = CustomKeywords.'myPac.StaffKeywords_Rider.checkTotalProducts'(flow_type, size, status_id, statusCheck)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			
			List<MobileElement> products = driver.findElementsById(staffId + 'row_order_detail_tv_name')
			for(int i = 0; i < products.size(); i++) {
				for (int j = 0; j < productName.size(); j++) {
					if (products.get(i).getText() == productName[j]) {
						qty = productQty[j]
						
						KeywordUtil.logInfo('productName[j] : ' + productName[j])
						KeywordUtil.logInfo('productQty[j] : ' + productQty[j])
						
						if (products.get(i).getText() == edit_product) {
							switch (flow_type) {
								case '5' :
									qty += edit_qty
									break
								case '7':
									KeywordUtil.logInfo ('skip deleted product : ' + edit_product)
									statusProduct = 1
									continue
							}
						}
						(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(productName[j], qty, productUnitPrice[j], countQty, countTotalPrice, statusProduct, status_id)
						if (status.equals('Fail')) {
							return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
						}
					}
				}
			}
	
			switch (flow_type) {
				case '6' :
					qty = edit_qty
					(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
					if (status.equals('Fail')) {
						return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
					}
					break
			}
			println ('countQty : ' + countQty)
			println ('countTotalPrice : ' + countTotalPrice)
			
			switch (flow_type) {
				case '0' :
				case '3'..'4' :
					(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
					if (status.equals('Fail')) {
						return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
					}
					break
				case '5'..'7' :
					(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkAllProducts'(countTotalPrice, edit_total_price, countQty, status_id)
					if (status.equals('Fail')) {
						return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
					}
					break
			}
			KeywordUtil.logInfo('delivery_type : ' + delivery_type)
			switch (delivery_type) {
				case '1' : 
					switch (flow_type) {
						case '3'..'4' :
							(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.cancelBtn'(flow_type, status_id)
							if (status.equals('Fail')) {
								return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
							}
							status_id = 6
							break
						case '0' :
						case '5'..'7' :
							(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords_Rider.confirmBtn'(status_id, payment_type, delivery_type, rider_name)
							if (status.equals('Fail')) {
								return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
							}
							break
					}
					break
				case '2' :
					(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkRider'(rider_name)
					if (status.equals('Fail')) {
						return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
					}
					break
			}
//			if (delivery_type == '1') {
				
//			}
		}
	if (delivery_type == '1') {
		KeywordUtil.logInfo('------------- Check final -----------')
		switch (status_id) {
			case 5..6 :
				MobileElement deliveryTab = (MobileElement) driver.findElementById(staffId + 'navigation_for_delivery')
				deliveryTab.click()
				break
		}
		
		(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.findOrder'(order_id, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords_Rider.setDefault'(total_product, flow_type, statusCheck)
			
		(status, remark, size) = CustomKeywords.'myPac.StaffKeywords_Rider.checkTotalProducts'(flow_type, size, status_id, statusCheck)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		List<MobileElement> products = driver.findElementsById(staffId + 'row_order_detail_tv_name')
		for(int i = 0; i < products.size(); i++) {
			for (int j = 0; j < productName.size(); j++) {
				if (products.get(i).getText() == productName[j]) {
					qty = productQty[j]
					
					KeywordUtil.logInfo('productName[j] : ' + productName[j])
					KeywordUtil.logInfo('productQty[j] : ' + productQty[j])
					
					if (products.get(i).getText() == edit_product) {
						switch (flow_type) {
							case '5' :
								qty += edit_qty
								break
							case '7':
								KeywordUtil.logInfo ('skip deleted product : ' + edit_product)
								statusProduct = 1
								continue
						}
					}
					(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(productName[j], qty, productUnitPrice[j], countQty, countTotalPrice, statusProduct, status_id)
					if (status.equals('Fail')) {
						return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
					}
				}
			}
		}
		
		switch (flow_type) {
			case '6' :
				qty = edit_qty
				(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords_Rider.checkEachProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
		}
		println ('countQty : ' + countQty)
		println ('countTotalPrice : ' + countTotalPrice)
		
		switch (flow_type) {
			case '0'..'4' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
			case '5'..'7' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkAllProducts'(countTotalPrice, edit_total_price, countQty, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
		}
	}
	switch (status_id) {
		case 3 :
			status = 'Pass'
			remark = '-'
			KeywordUtil.markPassed('check Status : Pass')
			break
		case 5..6 :
			(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkStatusId'(status_id)
			break
	}
//	(status, remark) = CustomKeywords.'myPac.StaffKeywords_Rider.checkStatusId'(status_id)
	
	KeywordUtil.logInfo('status : ' + status)
	return CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
}
catch (Exception e) {
	KeywordUtil.markFailed('Crashed... ' + e)
	status = 'Fail'
	remark = e.toString()
	CustomKeywords.'myPac.StaffKeywords_Rider.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
}
//Mobile.closeApplication()