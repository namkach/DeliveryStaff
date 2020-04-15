import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil

Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk', true)
//Mobile.setText(findTestObject('Staff/StoreId'), '00882', 0)
//Mobile.setText(findTestObject('Staff/StaffId'), '1010101', 0)
//Mobile.tap(findTestObject('Staff/LoginBtn'), 0)

//Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.6m_COD_05032020.apk', true)

boolean isTrue = false
def x = 'a'

try {
	if (isTrue) {
		KeywordUtil.logInfo('trueeeeeee')
	} else {
		KeywordUtil.logInfo('faillll')
		x = 'aaaaaaa'
		return x
	} 
	
	x = 'b'
} catch (Exception e) {
	println (e)
}

KeywordUtil.logInfo(x)



