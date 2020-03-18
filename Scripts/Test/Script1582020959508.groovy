import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableCell
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.10a_RIDER_17032020.apk', true)

//Mobile.setText(findTestObject('Staff/Store Id'), '00882', 0)
//Mobile.setText(findTestObject('Staff/Staff Id'), '1010101', 0)
//Mobile.tap(findTestObject('Staff/Login Btn'), 0)

//Mobile.tap(findTestObject('Staff/ProcessingTab'), 30)
//def order_id = '3364'
//def checkOrder2 = CustomKeywords.'myPac.StaffKeyword.FindOrder'(order_id)
//def editProduct = 'ไข่ไก่ต้มสุกตราซีพี'
//CustomKeywords.'myPac.StaffKeyword.deleteProduct'(editProduct)

//
//def text = ''
//println text.length()

//CustomKeywords.'myPac.writeExcel.write'(1,1,'test3')
CustomKeywords.'myPac.writeExcel.write'('ssss','aaaa','test4')

//try {
//	println 'hihihihihihi'
//	boolean t = true
//	if (t) {
//		return '2'
//	}
//	
//	println 'nnononon'
//} catch (Exception e) {
//	println e
//}
 
 