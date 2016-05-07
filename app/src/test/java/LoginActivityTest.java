import android.test.suitebuilder.annotation.SmallTest;

import com.alex.bagofwords.LoginActivity;

import junit.framework.TestCase;

public class LoginActivityTest extends TestCase {


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void checker() {
        LoginActivity loginActivity = new LoginActivity();
        boolean result = loginActivity.checker("MOOO");
        assertEquals(false, result);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
