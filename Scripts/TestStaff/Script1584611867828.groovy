import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver as AppiumDriver
import io.appium.java_client.MobileElement as MobileElement

//def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.10a_RIDER_17032020.apk'
def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.10h_COD_26032020.apk'
try {
	Mobile.startApplication(path, true)
	Mobile.setText(findTestObject('Staff/StoreId'), store_id, 0)
	Mobile.setText(findTestObject('Staff/StaffId'), '1010101', 0)
	Mobile.tap(findTestObject('Staff/LoginBtn'), 0)
} catch (Exception e) {
	KeywordUtil.markFailed('Crashed... ' + e)
	status = 'Fail'
	remark = 'Cannot start application'
	CustomKeywords.'myPac.writeExcel.writeResult'(order_id, flow_type, payment_type, status, remark)
}

try {
    AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
	int oldQty = 0
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
	int statusProduct = 0
	int statusCheck = 0
	
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
		KeywordUtil.logInfo ('qty : ' + productQty[i])
		(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(productName[i], productQty[i], productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		println ('countQty : ' + countQty)
		println ('countTotalPrice : ' + countTotalPrice)
	}
	(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
	if (status.equals('Fail')) {
		return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
	}
	
	switch (flow_type) {
		//new order
		case '1' :
			(status, remark) = CustomKeywords.'myPac.StaffKeywords.cancelBtn'(flow_type, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			status_id = 6
			break
		//new order
		case '0':
		case '2'..'7' :
			(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords.confirmBtn'(status_id, payment_type, apkType, delivery_type)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			break
	}
	
	KeywordUtil.logInfo('------------- processing -----------')
	if (flow_type != '1') {
		Mobile.tap(findTestObject('Staff/ProcessingTab'), 80)
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
			KeywordUtil.logInfo ('statusProduct : ' + statusProduct)
			(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(productName[i], productQty[i], productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			println ('countQty : ' + countQty)
			println ('countTotalPrice : ' + countTotalPrice)
		}
		
		(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
		
		switch (flow_type) {
			case '2' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords.cancelBtn'(flow_type, status_id)
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
						qty = edit_qty
						CustomKeywords.'myPac.StaffKeywords.editQty'(edit_product, qty, oldQty)
						break
					case '6' :
						println ('Add product')
						qty = edit_qty
						CustomKeywords.'myPac.StaffKeywords.addProduct'(edit_product, qty)
						break
					case '7' :
						println ('Delete product')
						CustomKeywords.'myPac.StaffKeywords.deleteProduct'(edit_product)
						break
				}
				KeywordUtil.logInfo('------------- check after edit product -----------')
				statusCheck = 1
				KeywordUtil.logInfo ('statusCheck : ' + statusCheck)
				switch (flow_type) {
					case '5'..'7' :
						(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.StaffKeywords.setDefault'(total_product, flow_type, statusCheck)
						
						(status, remark, size) = CustomKeywords.'myPac.StaffKeywords.checkTotalProducts'(flow_type, size, status_id, statusCheck)
						if (status.equals('Fail')) {
							return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
						}
						
						for(int i = 0; i < size; i++) {
							qty = productQty[i]
							if (productName[i] == edit_product) {
								switch (flow_type) {
									case '5' :
										qty += edit_qty
										break
									case '7' :
										statusProduct = 1
										continue
								}
							}
							
//							if (flow_type == '5' && productName[i] == edit_product) {
//								qty += oldQty
//							} else if (flow_type == '7' && productName[i] == edit_product) {
//								statusProduct = 1
//								continue
//							}
							(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(productName[i], qty, productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
							if (status.equals('Fail')) {
								return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
							}
						}
						
						switch (flow_type) {
							case '6' :
								qty = edit_qty
								(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
								if (status.equals('Fail')) {
									return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
								}
								break
						}
						println ('countQty : ' + countQty)
						println ('countTotalPrice : ' + countTotalPrice)
						(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkAllProducts'(countTotalPrice, edit_total_price, countQty, status_id)
						if (status.equals('Fail')) {
							return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
						}
						break
				}
				(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords.confirmBtn'(status_id, payment_type, apkType, delivery_type)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
		}
	}
	
	KeywordUtil.logInfo('------------- processed -----------')
	Mobile.tap(findTestObject('Staff/ProcessedTab'), 80)
	if (flow_type != '1' && flow_type != '2') {
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
			qty = productQty[i]
			if (productName[i] == edit_product) {
				switch (flow_type) {
					case '5' :
						qty += edit_qty
						break
					case '7' :
						statusProduct = 1
						continue
				}
			}
			(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(productName[i], qty, productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
		}

		switch (flow_type) {
			case '6' :
				qty = edit_qty
				(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
		}
		println ('countQty : ' + countQty)
		println ('countTotalPrice : ' + countTotalPrice)
		
		switch (flow_type) {
			case '0' :
			case '3'..'4' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
			case '5'..'7' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkAllProducts'(countTotalPrice, edit_total_price, countQty, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
		}
	
		switch (flow_type) {
			case '3'..'4' :
				(status, remark) = CustomKeywords.'myPac.StaffKeywords.cancelBtn'(flow_type, status_id)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				status_id = 6
				break
			case '0' :
			case '5'..'7' :
				(status, remark, status_id) = CustomKeywords.'myPac.StaffKeywords.confirmBtn'(status_id, payment_type, apkType, delivery_type)
				if (status.equals('Fail')) {
					return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
				}
				break
		}
	}
	
	KeywordUtil.logInfo('------------- Check final -----------')
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
		qty = productQty[i]
		if (productName[i] == edit_product) {
			switch (flow_type) {
				case '5' :
					qty += edit_qty
					break
				case '7' :
					statusProduct = 1
					continue
			}
		}
		(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(productName[i], qty, productUnitPrice[i], countQty, countTotalPrice, statusProduct, status_id)
		if (status.equals('Fail')) {
			return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
		}
	}

	switch (flow_type) {
		case '6' :
			qty = edit_qty
			(status, remark, countQty, countTotalPrice) = CustomKeywords.'myPac.StaffKeywords.checkEachProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			break
	}
	println ('countQty : ' + countQty)
	println ('countTotalPrice : ' + countTotalPrice)
	
	switch (flow_type) {
		case '0'..'4' :
			(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkAllProducts'(countTotalPrice, total_price, countQty, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			break
		case '5'..'7' :
			(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkAllProducts'(countTotalPrice, edit_total_price, countQty, status_id)
			if (status.equals('Fail')) {
				return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
			}
			break
	}
	
	(status, remark) = CustomKeywords.'myPac.StaffKeywords.checkStatusId'(status_id)
	KeywordUtil.logInfo('status : ' + status)
	return CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
}
catch (Exception e) {
	KeywordUtil.markFailed('Crashed... ' + e)
	status = 'Fail'
	remark = e.toString()
	CustomKeywords.'myPac.writeExcel.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
}
Mobile.closeApplication()

