import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import io.appium.java_client.AppiumDriver as AppiumDriver
import io.appium.java_client.MobileElement as MobileElement
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import internal.GlobalVariable as GlobalVariable

//Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\staff_uat_1.2.6m_COD_05032020.apk', true)
def staffId = 'th.co.gosoft.storemobile.sevendelivery.staff:id/'

try {
    //	Mobile.startApplication(path, true)
    Mobile.startApplication('D:\\Users\\sunitakac\\Desktop\\apk\\rider_1.0.1b_uat_COD_17032020.apk', true)

    Mobile.tap(findTestObject('null'), 0)

    Mobile.tap(findTestObject('null'), 0)

    Mobile.tap(findTestObject('null'), 0)

    Mobile.closeApplication()

    AppiumDriver<MobileElement> driver = MobileDriverFactory.getDriver()

    Mobile.delay(5)

    MobileElement cpBtn = driver.findElementByClassName('android.widget.Button')

    KeywordUtil.logInfo('text : ' + cpBtn.getText())

    cpBtn.click()

    Mobile.setText(findTestObject('null'), 'sunitakac', 0)

    Mobile.setText(findTestObject('null'), 'nazoru504', 0)

    Mobile.tap(findTestObject('null'), 0)

    MobileElement pin1 = ((driver.findElementById(staffId + 'pin_1')) as MobileElement)

    for (int i = 1; i <= 6; i++) {
        pin1.click()
    }
    
    Mobile.setText(findTestObject('null'), store_id, 0)

    Mobile.setText(findTestObject('null'), '1010101', 0)

    Mobile.tap(findTestObject('null'), 0)
}
catch (Exception e) {
    KeywordUtil.markFailed('Crashed... ' + e)

    status = 'Fail'

    remark = 'Cannot start application' //	CustomKeywords.'myPac.StaffKeywords_COD.writeStaff'(order_id, flow_type, delivery_type, payment_type, status, remark)
} 

Mobile.setText(findTestObject('Staff/StoreId'), '00151', 0)

Mobile.tap(findTestObject('Staff/LoginBtn'), 0)

Mobile.tap(findTestObject('Staff/confirmLogin'), 0)

Mobile.closeApplication()

