import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil

Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk', true)
//Mobile.setText(findTestObject('Staff/StoreId'), '00882', 0)
//Mobile.setText(findTestObject('Staff/StaffId'), '1010101', 0)
//Mobile.tap(findTestObject('Staff/LoginBtn'), 0)

//Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.6m_COD_05032020.apk', true)

//def test = '20'
//int t = 5
//KeywordUtil.logInfo('test : ' + t)
//t = CustomKeywords.'myPac.RiderKeywords.testtext'(t)
//KeywordUtil.logInfo('test : ' + t)

int qty = 2
double unitPrice = 40.00
int countQty = 30
double countTotalPrice = 10.00
int statusProduct = 4
def total_product = '5'
double price = 6.00
int size = 0


(qty, unitPrice, countQty, countTotalPrice, statusProduct, size, price) = CustomKeywords.'myPac.RiderKeywords.setDefault'(Integer.parseInt(total_product))
KeywordUtil.logInfo('qty : ' + qty)
KeywordUtil.logInfo('countQty : ' + countQty)
KeywordUtil.logInfo('countTotalPrice : ' + countTotalPrice)
KeywordUtil.logInfo('statusProduct : ' + statusProduct)
KeywordUtil.logInfo('size : ' + size)
KeywordUtil.logInfo('price : ' + price)
KeywordUtil.logInfo('unitPrice : ' + unitPrice)




