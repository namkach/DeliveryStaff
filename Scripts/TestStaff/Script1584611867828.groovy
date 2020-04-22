import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver as AppiumDriver
import io.appium.java_client.MobileElement as MobileElement

//def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.10a_RIDER_17032020.apk'
def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.10h_COD_26032020.apk'
Mobile.startApplication(path, true)
Mobile.setText(findTestObject('Staff/StoreId'), store_id, 0)
Mobile.setText(findTestObject('Staff/StaffId'), '1010101', 0)
Mobile.tap(findTestObject('Staff/LoginBtn'), 0)

try {
    AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
//	def paymentType = '0'
	int oldQty = 0
//	def deliveryType = '0'
	def remark = '-'
	def status = ''
	def apkType = ''
	int status_id = 1
	int size = total_product
	def name = ''
	int qty = 0
	def unitPrice = ''
	int countQty = 0
	int countTotalPrice = 0
	int statusProduct = 1
	int statusCheck = 1
	
	if (path.contains('COD')) {
		apkType = 'cod'
	} else if (path.contains('RIDER')) {
		apkType = 'rider'
	}
	KeywordUtil.logInfo('apkType : ' + apkType)
	
//	order_id = CustomKeywords.'myPac.StaffKeywords.checkOrderId'(order_id)
	
	KeywordUtil.logInfo('------------- new order -----------')
	(status, remark) = CustomKeywords.'myPac.StaffKeywords.findOrder'(order_id, status_id)
	if (status.equals('Fail')) {
		return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
	}
	KeywordUtil.logInfo('size :' + size)
	(status, remark, size) = CustomKeywords.'myPac.StaffKeywords.checkTotalProducts'(flow_type, size, status_id, statusCheck)
	if (status.equals('Fail')) {
		return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
	}
	
	String[] productName = [product_name1, product_name2, product_name3]
	Integer[] productQty = [qty1, qty2, qty3]
	Double[] productUnitPrice = [unit_price1, unit_price2, unit_price3]
	
	for(int i = 0; i < size; i++) {
		println ('qty : ' + productQty[i])
		(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(productName[i], productQty[i], productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
		}
		println ('countQty : ' + countQty)
		println ('countTotalPrice : ' + countTotalPrice)
	}
	(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, total_price, countQty, status_id)
	if (status.equals('Fail')) {
		return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
	}
	
    println('Check outside found order :' + checkOrder)
	KeywordUtil.logInfo('flow_type :' + flow_type)
	switch (flow_type) {
		//new order
		case '1' :
			(status, remark) = CustomKeywords.'myPac.StaffKeywords.CancelBtn'(flow_type, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			status_id = 6
			break
		//new order
		case '0':
		case '2'..'7' :
			(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords.ConfirmBtn'(status_id, payment_type, apkType, delivery_type)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			break
	}
	
	KeywordUtil.logInfo('------------- processing -----------')
	if (flow_type != '1') {
		Mobile.tap(findTestObject('Staff/ProcessingTab'), 30)
		(status, remark) = CustomKeywords.'myPac.StaffKeywords.findOrder'(order_id, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords.setDefault'(total_product, flow_type, statusCheck)
		
		(status, remark, size) = CustomKeywords.'myPac.StaffKeywords.checkTotalProducts'(flow_type, size, status_id, statusCheck)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		for(int i = 0; i < size; i++) {
			println ('qty : ' + productQty[i])
			(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(productName[i], productQty[i], productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			println ('countQty : ' + countQty)
			println ('countTotalPrice : ' + countTotalPrice)
		}
		
		(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, total_price, countQty, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
		}
		
		switch (flow_type) {
			case '2' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords.CancelBtn'(flow_type, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				status_id = 6
				break
			case '0' :
			case '3'..'7' :
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
						CustomKeywords.'myPac.StaffKeywords.editQty'(edit_product, edit_qty, oldQty)
						break
					case '6' :
						println ('Add product')
						CustomKeywords.'myPac.StaffKeywords.addProduct'(edit_product, edit_qty)
						break
					case '7' :
						println ('Delete product')
						CustomKeywords.'myPac.StaffKeywords.deleteProduct'(edit_product)
						break
				}
				KeywordUtil.logInfo('------------- check after edit product -----------')
				statusCheck = 2
				switch (flow_type) {
					case '5'..'7' :
						(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords.setDefault'(total_product, flow_type, statusCheck)
						
						(status, remark, size) = CustomKeywords.'myPac.StaffKeywords.checkTotalProducts'(flow_type, size, status_id, statusCheck)
						if (status.equals('Fail')) {
							return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
						}
						
						for(int i = 0; i < size; i++) {
							qty = productQty[i]
							if (flow_type == '5' && productName[i] == edit_product) {
								qty += oldQty
							} else if (flow_type == '7' && productName[i] == edit_product) {
								statusProduct = 0
								continue
							}
							(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(productName[i], qty, productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
							if (status.equals('Fail')) {
								return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
							}
						}
						
						switch (flow_type) {
							case '6' :
								qty = edit_qty
								(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
								if (status.equals('Fail')) {
									return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
								}
								break
						}
						println ('countQty : ' + countQty)
						println ('countTotalPrice : ' + countTotalPrice)
						
//						switch (flow_type) {
//							case '5'..'7' :
//								CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, edit_total_price, countQty)
//								break
//							default :
//								CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, total_price, countQty)
//								break
//						}
						(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, edit_total_price, countQty, status_id)
						if (status.equals('Fail')) {
							return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
						}
						break
				}
				(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords.ConfirmBtn'(status_id, payment_type, apkType, delivery_type)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
		}
	}
	
//	KeywordUtil.logInfo('------------- processed -----------')
//	Mobile.tap(findTestObject('Staff/ProcessedTab'), 30)
//	if (flow_type != '1' && flow_type != '2') {
//		(status, remark) = CustomKeywords.'myPac.StaffKeywords.findOrder'(order_id, status_id)
//		if (status.equals('Fail')) {
//			return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
//		}
//		
//		(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords.setDefault'(total_product, flow_type, statusCheck)
//		
//		(status, remark, size) = CustomKeywords.'myPac.StaffKeywords.checkTotalProducts'(flow_type, size, status_id, statusCheck)
//		if (status.equals('Fail')) {
//			return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
//		}
//		
//		for(int i = 0; i < size; i++) {
//			qty = Integer.parseInt(productQty[i])
//			if (flow_type == '5' && productName[i] == edit_product) {
//				qty += oldQty
//			} else if (flow_type == '7' && productName[i] == edit_product) {
//				println ('skip deleted product : ' + edit_product)
//				statusProduct = 0
//				continue
//			}
//			(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(productName[i], qty, productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
//			if (status.equals('Fail')) {
//				return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
//			}
//		}
//		
//		if (flow_type == '6') {
//			qty = Integer.parseInt(edit_qty)
//			(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
//			if (status.equals('Fail')) {
//				return CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
//			}
//		}
//		println ('countQty : ' + countQty)
//		println ('countTotalPrice : ' + countTotalPrice)
//		
//		if (flow_type == '5' || flow_type == '6' || flow_type == '7') {
//			CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, edit_total_price, countQty)
//		} else {
//			CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, total_price, countQty)
//		}
//		KeywordUtil.markPassed('Check products : Pass') 
//	
//		if (flow_type == '3' || flow_type == '4') {
//			statusOrder3 = CustomKeywords.'myPac.StaffKeywords.CancelBtn'(flow_type)
//			status_id = 6
//			KeywordUtil.markPassed('Cancel : Pass')
//		} else {
//			(statusOrder3, status_id, paymentType, deliveryType) = CustomKeywords.'myPac.StaffKeywords.ConfirmBtn'(status_id, paymentType, apkType)
//			println ('Check statusOrder :' + statusOrder3)
//			println ('Check status_id :' + status_id)
//			println ('Check paymentType :' + paymentType)
//			println ('Check deliveryType :' + deliveryType)
//			assert deliveryType.toString() == delivery_type.toString()
//			KeywordUtil.markPassed('Confirm : Pass')
//			KeywordUtil.logInfo('delivery_type : ' + delivery_type)
//			if (delivery_type == 2) {
//				MobileElement walk = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/delivery_confirm_rd_walk')
//				walk.click()
//				MobileElement confirmOrder = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/delivery_confirm_bt_confirm')
//				confirmOrder.click()
//				MobileElement confirmSign = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/main_toolbar_tv_menu_right2')
//				if (confirmSign.isDisplayed()) {
//					CustomKeywords.'myPac.StaffKeywords.swipeUp'()
//					confirmSign.click()
//					MobileElement confirmSignYes = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_confirm_yes')
//					confirmSignYes.click()
//				}
//			}
//		}
//		
//		if (!statusOrder3) {
//			status = 'Fail'
//			remark = 'Fail to confirm/cancel order ' + order_id + ' at status_id ' + status_id
//			return remark
//		}
//	}
//	
//	KeywordUtil.logInfo('------------- Check final -----------')
//	println order_id
//	checkOrder4 = CustomKeywords.'myPac.StaffKeywords.findOrder'(order_id)
//	println ('checkOrder4 : ' + checkOrder4)
//	
//	if (!checkOrder4) {
//		status = 'Fail'
//		remark = 'Fail to find order ' + order_id + ' at status_id ' + status_id
//		return remark
//	}
//	
//	List<MobileElement> prods4 = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
//	println ('Product size : ' + prods4.size().toString())
//	println ('Total product : ' + total_product.toString())
//	assert prods4.size().toString() == total_product.toString()
//	String[] products4 = new String[Double.parseDouble(total_product)]
//	println ('product size : ' + products4.size())
//
//	name = ''
//	qty = 0
//	unitPrice = ''
//	countQty = 0
//	countTotalPrice = 0
//	statusProduct = 1
//	
//	int size = 0
//	size = products4.size()
//	
//	if (flow_type == '6') {
//		size -= 1
//	}
//	
//	for(int i = 0; i < size; i++) {
//		qty = Integer.parseInt(productQty[i])
//		if (flow_type == '5' && productName[i] == edit_product) {
//			qty += oldQty
//		} else if (statusProduct == 0) {
//			statusProduct = -1	
//		} else if (flow_type == '7' && productName[i] == edit_product) {
//			println ('skip deleted product : ' + edit_product)
//			statusProduct = -2
//			continue
//		}
//		(countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(productName[i], qty, productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
//	}
//	if (flow_type == '6') {
//		qty = Integer.parseInt(edit_qty)
//		(countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
//	}
//	println ('countQty : ' + countQty)
//	println ('countTotalPrice : ' + countTotalPrice)
//	
//	if (flow_type == '5' || flow_type == '6' || flow_type == '7') {
//		CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, edit_total_price, countQty)
//	} else {
//		CustomKeywords.'myPac.StaffKeywords.checkProductAssert'(countTotalPrice, total_price, countQty)
//	}
//	KeywordUtil.markPassed('Check products : Pass')
//	
//	boolean isSuccess = false
//	statusOrder4 = CustomKeywords.'myPac.StaffKeywords.checkStatusId'(status_id)
//	
//	if (!statusOrder4) {
//		status = 'Fail'
//		remark = 'Fail to find order ' + order_id + ' at status_id ' + status_id
//		return remark
//	} else {
//		status = 'Pass'
//	}
//	CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
}
catch (Exception e) {
    KeywordUtil.markFailed('crashed... ' + e)
}
Mobile.closeApplication()

