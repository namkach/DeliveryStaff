import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import jxl.Workbook
import jxl.write.Label
import jxl.write.WritableCell
import jxl.write.WritableSheet
import jxl.write.WritableWorkbook

//Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk', true)
//Mobile.setText(findTestObject('Staff/StoreId'), '00882', 0)
//Mobile.setText(findTestObject('Staff/StaffId'), '1010101', 0)
//Mobile.tap(findTestObject('Staff/LoginBtn'), 0)

Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.6m_COD_05032020.apk', true)





CustomKeywords.'myPac.writeExcel.write'('ssss','aaaa','test4')