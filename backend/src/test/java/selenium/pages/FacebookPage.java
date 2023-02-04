package selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class FacebookPage {

    private WebDriver driver;
    public static final String PAGE_URL = "https://www.facebook.com/login.php?skip_api_login=1&api_key=430844285930561&kid_directed_site=0&app_id=430844285930561&signed_next=1&next=https%3A%2F%2Fwww.facebook.com%2Fv10.0%2Fdialog%2Foauth%3Fapp_id%3D430844285930561%26cbt%3D1675528584952%26channel_url%3Dhttps%253A%252F%252Fstaticxx.facebook.com%252Fx%252Fconnect%252Fxd_arbiter%252F%253Fversion%253D46%2523cb%253Df33cb6f71c2ea1%2526domain%253Dlocalhost%2526is_canvas%253Dfalse%2526origin%253Dhttps%25253A%25252F%25252Flocalhost%25253A4200%25252Ff1a940c4441f528%2526relation%253Dopener%26client_id%3D430844285930561%26display%3Dpopup%26domain%3Dlocalhost%26e2e%3D%257B%257D%26fallback_redirect_uri%3Dhttps%253A%252F%252Flocalhost%253A4200%252Fanon%252Flogin%26fields%3Dname%252Cemail%252Cpicture%252Cfirst_name%252Clast_name%26locale%3Den_US%26logger_id%3Df1f2ec83af8c4d%26origin%3D1%26redirect_uri%3Dhttps%253A%252F%252Fstaticxx.facebook.com%252Fx%252Fconnect%252Fxd_arbiter%252F%253Fversion%253D46%2523cb%253Df360053cbcb435c%2526domain%253Dlocalhost%2526is_canvas%253Dfalse%2526origin%253Dhttps%25253A%25252F%25252Flocalhost%25253A4200%25252Ff1a940c4441f528%2526relation%253Dopener%2526frame%253Dfe9998d31346e4%26response_type%3Dtoken%252Csigned_request%252Cgraph_domain%26scope%3Demail%252Cpublic_profile%26sdk%3Djoey%26version%3Dv10.0%26ret%3Dlogin%26fbapp_pres%3D0%26tp%3Dunspecified&cancel_url=https%3A%2F%2Fstaticxx.facebook.com%2Fx%2Fconnect%2Fxd_arbiter%2F%3Fversion%3D46%23cb%3Df360053cbcb435c%26domain%3Dlocalhost%26is_canvas%3Dfalse%26origin%3Dhttps%253A%252F%252Flocalhost%253A4200%252Ff1a940c4441f528%26relation%3Dopener%26frame%3Dfe9998d31346e4%26error%3Daccess_denied%26error_code%3D200%26error_description%3DPermissions%2Berror%26error_reason%3Duser_denied&display=popup&locale=en_US&pl_dbl=0";

    public FacebookPage(WebDriver driver) {
        this.driver = driver;
        driver.get(PAGE_URL);
        PageFactory.initElements(driver, this);
    }

    public boolean isOpened(){
        return true;
    }

}
