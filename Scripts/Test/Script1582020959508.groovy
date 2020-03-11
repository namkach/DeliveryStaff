import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.6m_COD_05032020.apk', true)

Mobile.setText(findTestObject('Staff/Store Id'), '00882', 0)
Mobile.setText(findTestObject('Staff/Staff Id'), '1010101', 0)
Mobile.tap(findTestObject('Staff/Login Btn'), 0)

//Mobile.tap(findTestObject('Staff/ProcessingTab'), 30)
//def order_id = '3364'
//def checkOrder2 = CustomKeywords.'myPac.Success.FindOrder'(order_id)
//def editProduct = 'ไข่ไก่ต้มสุกตราซีพี'
//CustomKeywords.'myPac.Success.deleteProduct'(editProduct)


def text = ''
println text.length()





