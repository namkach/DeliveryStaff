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


CustomKeywords.'myPac.writeExcel.write'('ssss','aaaa','test4')