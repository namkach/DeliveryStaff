import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil

//Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk', true)
//Mobile.setText(findTestObject('Staff/StoreId'), '00882', 0)
//Mobile.setText(findTestObject('Staff/StaffId'), '1010101', 0)
//Mobile.tap(findTestObject('Staff/LoginBtn'), 0)

Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.6m_COD_05032020.apk', true)

//long startTime
//long endTime
//def myList = [5, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40, 40, 6, 7, 8, 12, 15, 18, 30, 40, 40]
//int size = myList.size()
//KeywordUtil.logInfo('Size : ' + size)
//startTime = Calendar.getInstance().getTimeInMillis()
//for(int j = 0; j < size ; j++)
//{
//	println('number : ' + j )
//}
//endTime = Calendar.getInstance().getTimeInMillis()
//KeywordUtil.logInfo('Time : ' + (endTime - startTime))
//
//startTime = Calendar.getInstance().getTimeInMillis()
//for(int j = size; j > 0 ; j--)
//{
//	println('number : ' + j )
//}
//endTime = Calendar.getInstance().getTimeInMillis()
//KeywordUtil.logInfo('Time : ' + (endTime - startTime))

//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//double width = screenSize.getWidth();
//double height = screenSize.getHeight();
//
//println('screenHeight : ' + height )
//println('screenWidth : ' + width )

//GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
//
//
//
////println('screenHeight : ' + height )
//println('screenWidth : ' + ge.getMaximumWindowBounds().getWidth() )

//GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

//println('screenWidth : ' + Mobile.getDeviceWidth())
//def orders = [1,2,3]
//
//for (int j = orders.size() - 1; j >= 0; j--) {
//	if (orders.get(j) == 3) {
//		return true
//		break;
//	} else {
//		return false
//	}
//}



boolean check

check = CustomKeywords.'myPac.RiderKeywords.test'('3')

KeywordUtil.logInfo('check : ' + check)


