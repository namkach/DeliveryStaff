import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable

def path = 'D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk'
Mobile.startApplication(path, true)
Mobile.setText(findTestObject('Rider/RiderId'), 'supanans', 0)
Mobile.setText(findTestObject('Rider/RiderPassword'), '1234', 0)
Mobile.tap(findTestObject('Rider/LoginBtn'), 0)

try {
    AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()

    order_id = CustomKeywords.'myPac.StaffKeywords.checkOrderId'(order_id)

    KeywordUtil.logInfo('------------- new order -----------')
    checkOrder = CustomKeywords.'myPac.StaffKeywords.FindOrder'(order_id)
	
    if (!(checkOrder)) {
        status = 'Fail'
        remark = ((('Fail to find order' + order_id) + ' at status_id ') + status_id)
        return remark
    }
}
catch (Exception e) {
    KeywordUtil.markFailed('Crashed... ' + e)
} 

