import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil

import io.appium.java_client.AppiumDriver as AppiumDriver
import io.appium.java_client.MobileElement as MobileElement

def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.6m_COD_05032020.apk'
Mobile.startApplication(path, true)
Mobile.setText(findTestObject('Staff/Store Id'), '00882', 0)
Mobile.setText(findTestObject('Staff/Staff Id'), '1010101', 0)
Mobile.tap(findTestObject('Staff/Login Btn'), 0)

try {
    AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()
	def status_id = '0'
	def paymentType = '0'
	int oldQty = 0
	def deliveryType = '0'
	
    println(order_id.length())
    while (order_id.length() < 4) {
        order_id = ('0' + order_id)

        println(order_id.length())
		KeywordUtil.logInfo(order_id)
    }
	checkOrder = CustomKeywords.'myPac.Success.FindOrder'(order_id)
	
	List<MobileElement> prods = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
	println ('Product size : ' + prods.size().toString())
	println ('Total product : ' + total_product.toString())
	assert prods.size().toString() == total_product.toString()
	String[] products = new String[Integer.parseInt(total_product)]
	println ('product size : ' + products.size())
	
	def name = ''
	int qty = 0
	def unitPrice = ''
	int countQty = 0
	int countTotalPrice = 0
	int statusProduct = 1
	
	for(int i = 1; i <= products.size(); i++) {
		switch (i) {
			case 1 :
				name = product_name1
				qty = Integer.parseInt(qty1)
				unitPrice = unit_price1
				break
			case 2 :
				name = product_name2
				qty = Integer.parseInt(qty2)
				unitPrice = unit_price2
				break
			case 3 :
				name = product_name3
				qty = Integer.parseInt(qty3)
				unitPrice = unit_price3
				break
		}
		println ('qty : ' + qty)
		(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(name, qty, unitPrice, countQty, countTotalPrice, statusProduct)
		println ('countQty : ' + countQty)
		println ('countTotalPrice : ' + countTotalPrice)
	}
	CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, total_price, countQty)
	KeywordUtil.markPassed('Check products : Pass')

    status_id = '1'
    println('Check outside found order :' + checkOrder)
	KeywordUtil.logInfo('flow_type :' + flow_type)
	switch (flow_type) {
		//new order
		case '1' :
			statusOrder = CustomKeywords.'myPac.Success.CancelBtn'(flow_type)
			status_id = '6'
			KeywordUtil.markPassed('Cancel : Pass')
			break
			
		//new order
		case '0'..'7' :
			(statusOrder, status_id, paymentType, deliveryType) = CustomKeywords.'myPac.Success.ConfirmBtn'(status_id, paymentType)
			println ('Check statusOrder :' + statusOrder)
			println ('Check status_id :' + status_id)
			println ('Check paymentType :' + paymentType)
			println ('Check deliveryType :' + deliveryType)
			assert deliveryType.toString() == delivery_type.toString()
			KeywordUtil.markPassed('Confirm : Pass')
			break
		default :
			println 'Error at New Order'
	}
	
//	status_id = '2'
	//------------------------------ processing ----------------------------//
	KeywordUtil.logInfo('------------- processing -----------')
	if (flow_type != '1') {
		Mobile.tap(findTestObject('Staff/ProcessingTab'), 30)
		checkOrder2 = CustomKeywords.'myPac.Success.FindOrder'(order_id)
		println ('Check outside found order :' + checkOrder2)
		println ('status_id : ' + status_id)
		
		List<MobileElement> prods2 = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
		println ('Product size : ' + prods2.size().toString())
		println ('Total product : ' + total_product.toString())
		assert prods2.size().toString() == total_product.toString()
		String[] products2 = new String[Integer.parseInt(total_product)]
		println ('product size : ' + products2.size())
		
		name = ''
		qty = 0
		unitPrice = ''
		countQty = 0
		countTotalPrice = 0
		
		for(int i = 1; i <= products2.size(); i++) {
			switch (i) {
				case 1 :
					name = product_name1
					qty = Integer.parseInt(qty1)
					unitPrice = unit_price1
					break
				case 2 :
					name = product_name2
					qty = Integer.parseInt(qty2)
					unitPrice = unit_price2
					break
				case 3 :
					name = product_name3
					qty = Integer.parseInt(qty3)
					unitPrice = unit_price3
					break
			}
			(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(name, qty, unitPrice, countQty, countTotalPrice, statusProduct)
			println ('countQty : ' + countQty)
			println ('countTotalPrice : ' + countTotalPrice)
		}
		CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, total_price, countQty)
		KeywordUtil.markPassed('Check products : Pass')
		
		if (flow_type == '2') {
			statusOrder2 = CustomKeywords.'myPac.Success.CancelBtn'(flow_type)
			status_id = '6'
			KeywordUtil.markPassed('Cancel : Pass')
		} else {
			if (flow_type == '5') {
				println ('Edit QTY')
				println ('QTY : ' + edit_qty)
				println edit_product
				switch (edit_product) {
					case product_name1 :
						oldQty = Integer.parseInt(qty1)
						break
					case product_name2 :
						oldQty = Integer.parseInt(qty2)
						break
					case product_name3 :
						oldQty = Integer.parseInt(qty3)
						break
				}
				CustomKeywords.'myPac.Success.editQty'(edit_product, edit_qty, oldQty)
			} else if (flow_type == '6') {
				println ('Add product')
				CustomKeywords.'myPac.Success.addProduct'(edit_product, edit_qty)
			} else if (flow_type == '7') {
				println ('Delete product')
				CustomKeywords.'myPac.Success.deleteProduct'(edit_product)
				println 'out-------'
			}
						
			//---------------------- check after edit product ----------------------//
			KeywordUtil.logInfo('------------- check after edit product -----------')
			if (flow_type == '5' || flow_type == '6' || flow_type == '7') {
				List<MobileElement> prods22 = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
				println ('Product size : ' + prods22.size().toString())
				int tmp = 0
				if (flow_type == '6') {
					tmp = Integer.parseInt(total_product) + 1
					total_product = tmp.toString()
					println total_product
				} else if (flow_type == '7') {
					total_product -= 1
				}
				
				println ('Total product : ' + total_product.toString())
				assert prods22.size().toString() == total_product.toString()
				String[] products22 = new String[Integer.parseInt(total_product)]
				println ('product size : ' + products22.size())
				
				name = ''
				qty = 0
				unitPrice = ''
				countQty = 0
				countTotalPrice = 0
				
				int size = 0
				size = products22.size()
				
				if (flow_type == '6') {
					size -= 1
				}
				
				for(int i = 1; i <= size; i++) {
					println 'i is : ' + i
					switch (i) {
						case 1 :
							name = product_name1
							qty = Integer.parseInt(qty1)
							unitPrice = unit_price1
							break
						case 2 :
							name = product_name2
							qty = Integer.parseInt(qty2)
							unitPrice = unit_price2
							break
						case 3 :
							name = product_name3
							qty = Integer.parseInt(qty3)
							unitPrice = unit_price3
							break
					}
					
					println ('name : ' + name)
					println ('qty : ' + qty)
					println ('unitPrice : ' + unitPrice)
					
					if (flow_type == '5' && name == edit_product) {
						qty += oldQty
					} else if (statusProduct == 0) {
						statusProduct = -1	
					} else if (flow_type == '7' && name == edit_product) {
						statusProduct = 0
						continue
					}
					(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(name, qty, unitPrice, countQty, countTotalPrice, statusProduct)
				}
				if (flow_type == '6') {
					qty = Integer.parseInt(edit_qty)
					(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct)
				}
				println ('countQty : ' + countQty)
				println ('countTotalPrice : ' + countTotalPrice)
				
				if (flow_type == '5' || flow_type == '6' || flow_type == '7') {
					CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, edit_total_price, countQty)
				} else {
					CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, total_price, countQty)
				}
				KeywordUtil.markPassed('Check products : Pass')
			}
			
			(statusOrder2, status_id, paymentType, deliveryType) = CustomKeywords.'myPac.Success.ConfirmBtn'(status_id, paymentType)
			println ('Check statusOrder :' + statusOrder2)
			println ('Check status_id :' + status_id)
			println ('Check paymentType :' + paymentType)
			println ('Check deliveryType :' + deliveryType)
			assert deliveryType.toString() == delivery_type.toString()
			assert paymentType.toString() == payment_type.toString()
			KeywordUtil.markPassed('Confirm : Pass')
		}
	}
	
	//------------------------ processed -----------------------------//
	KeywordUtil.logInfo('------------- processed -----------')
	Mobile.tap(findTestObject('Staff/Processed'), 30)
	if (flow_type != '1' && flow_type != '2') {
		checkOrder3 = CustomKeywords.'myPac.Success.FindOrder'(order_id)
		println ('Check outside found order :' + checkOrder3)
		
		List<MobileElement> prods3 = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
		println ('Product size : ' + prods3.size().toString())
		println ('Total product : ' + total_product.toString())
		assert prods3.size().toString() == total_product.toString()
		String[] products3 = new String[Double.parseDouble(total_product)]
		println ('product size : ' + products3.size())
		
		name = ''
		qty = 0
		unitPrice = ''
		countQty = 0
		countTotalPrice = 0
		statusProduct = 1
		
		int size = 0
		size = products3.size()
		
		if (flow_type == '6') {
			size -= 1
		}
		
		for(int i = 1; i <= size; i++) {
			switch (i) {
				case 1 :
					name = product_name1
					qty = Integer.parseInt(qty1)
					unitPrice = unit_price1
					break
				case 2 :
					name = product_name2
					qty = Integer.parseInt(qty2)
					unitPrice = unit_price2
					break
				case 3 :
					name = product_name3
					qty = Integer.parseInt(qty3)
					unitPrice = unit_price3
					break
			}
			
			println ('name : ' + name)
			println ('qty : ' + qty)
			println ('unitPrice : ' + unitPrice)
			
			if (flow_type == '5' && name == edit_product) {
				qty += oldQty
			} else if (statusProduct == 0) {
				statusProduct = -1	
			} else if (flow_type == '7' && name == edit_product) {
				println ('skip deleted product : ' + edit_product)
				statusProduct = -2
				continue
			}
			(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(name, qty, unitPrice, countQty, countTotalPrice, statusProduct)
			
		}
		if (flow_type == '6') {
			qty = Integer.parseInt(edit_qty)
			(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct)
		}
		println ('countQty : ' + countQty)
		println ('countTotalPrice : ' + countTotalPrice)
		
		if (flow_type == '5' || flow_type == '6' || flow_type == '7') {
			CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, edit_total_price, countQty)
		} else {
			CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, total_price, countQty)
		}
		KeywordUtil.markPassed('Check products : Pass') 
	
		if (flow_type == '3' || flow_type == '4') {
			statusOrder3 = CustomKeywords.'myPac.Success.CancelBtn'(flow_type)
			status_id = '6'
			KeywordUtil.markPassed('Cancel : Pass')
//			Mobile.pressBack()
		} else {
			(statusOrder3, status_id, paymentType, deliveryType) = CustomKeywords.'myPac.Success.ConfirmBtn'(status_id, paymentType)
			println ('Check statusOrder :' + statusOrder3)
			println ('Check status_id :' + status_id)
			println ('Check paymentType :' + paymentType)
			println ('Check deliveryType :' + deliveryType)
			assert deliveryType.toString() == delivery_type.toString()
			KeywordUtil.markPassed('Confirm : Pass')
			KeywordUtil.logInfo('delivery_type : ' + delivery_type)
			if (delivery_type == 2) {
				MobileElement walk = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/delivery_confirm_rd_walk')
				walk.click()
				MobileElement confirmOrder = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/delivery_confirm_bt_confirm')
				confirmOrder.click()
				MobileElement confirmSign = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/main_toolbar_tv_menu_right2')
				if (confirmSign.isDisplayed()) {
					CustomKeywords.'myPac.Success.swipeUp'()
					confirmSign.click()
					MobileElement confirmSignYes = (MobileElement) driver.findElementById('th.co.gosoft.storemobile.sevendelivery.staff:id/dialog_confirm_yes')
					confirmSignYes.click()
				}
			}
		}
	}
	
	KeywordUtil.logInfo('------------- Check final -----------')
	println order_id
	checkOrder4 = CustomKeywords.'myPac.Success.FindOrder'(order_id)
	println ('checkOrder4 : ' + checkOrder4)
	
	List<MobileElement> prods4 = driver.findElementsById('th.co.gosoft.storemobile.sevendelivery.staff:id/row_order_detail_tv_name')
	println ('Product size : ' + prods4.size().toString())
	println ('Total product : ' + total_product.toString())
	assert prods4.size().toString() == total_product.toString()
	String[] products4 = new String[Double.parseDouble(total_product)]
	println ('product size : ' + products4.size())

	name = ''
	qty = 0
	unitPrice = ''
	countQty = 0
	countTotalPrice = 0
	statusProduct = 1
	
	int size = 0
	size = products4.size()
	
	if (flow_type == '6') {
		size -= 1
	}
	
	for(int i = 1; i <= size; i++) {
		switch (i) {
			case 1 :
				name = product_name1
				qty = Integer.parseInt(qty1)
				unitPrice = unit_price1
				break
			case 2 :
				name = product_name2
				qty = Integer.parseInt(qty2)
				unitPrice = unit_price2
				break
			case 3 :
				name = product_name3
				qty = Integer.parseInt(qty3)
				unitPrice = unit_price3
				break
		}
		if (flow_type == '5' && name == edit_product) {
			qty += oldQty
		} else if (statusProduct == 0) {
			statusProduct = -1	
		} else if (flow_type == '7' && name == edit_product) {
			continue
		}
		(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(name, qty, unitPrice, countQty, countTotalPrice, statusProduct)
	}
	if (flow_type == '6') {
		qty = Integer.parseInt(edit_qty)
		(countQty, countTotalPrice) = CustomKeywords.'myPac.Success.checkProduct'(edit_product, qty, edit_unit_price, countQty, countTotalPrice, statusProduct)
	}
	println ('countQty : ' + countQty)
	println ('countTotalPrice : ' + countTotalPrice)
	
	if (flow_type == '5' || flow_type == '6' || flow_type == '7') {
		CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, edit_total_price, countQty)
	} else {
		CustomKeywords.'myPac.Success.checkProductAssert'(countTotalPrice, total_price, countQty)
	}
	KeywordUtil.markPassed('Check products : Pass')
	
	boolean isSuccess = false
	isSuccess = CustomKeywords.'myPac.Success.checkStatusId'(status_id)
	
	if (isSuccess) {
		if (status_id == '5' || status_id == '6') {
			statusOrder4 = 'Pass'
		} else {
			statusOrder4 = 'Fail at status : ' + status_id + ' isSuccess'
		}
	} else {
		statusOrder4 = 'Fail at status : ' + status_id
	}
			
	/////
//		}
//	}
	////
    String order = order_id.toString()
    KeywordUtil.logInfo('order :  ---- ' + order)
    KeywordUtil.logInfo('checkOrder : ' + checkOrder)
    KeywordUtil.logInfo('statusOrder : ' + statusOrder)
    KeywordUtil.logInfo('checkOrder2 : ' + checkOrder2)
    KeywordUtil.logInfo('statusOrder2 : ' + statusOrder2)
    KeywordUtil.logInfo('checkOrder3 : ' + checkOrder3)
    KeywordUtil.logInfo('statusOrder3 : ' + statusOrder3)
    KeywordUtil.logInfo('checkOrder4 : ' + checkOrder4)
    KeywordUtil.logInfo('statusOrder4 : ' + statusOrder4)
	
	//	CustomKeywords.'myPac.Excel.writeExcel'(order_id, checkOrder, statusOrder, checkOrder2, statusOrder2, checkOrder3, statusOrder3, checkOrder4, statusOrder4)
}
catch (Exception e) {
    KeywordUtil.markFailed('crashed... ' + e)
}
Mobile.closeApplication()

