package lab13.qifan.group2.a2;

import lab13.qifan.group2.a2.ui.LoginRegistrationUI;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class AppTest {

    @Test
    public void testRunAppMethod() {
        // Mock LoginRegistrationUI
        LoginRegistrationUI mockLoginUI = Mockito.mock(LoginRegistrationUI.class);

        App app = new App(mockLoginUI);
        
        app.runApp();

        // Verify that displayLoginScreen was called once
        verify(mockLoginUI, times(1)).displayLoginScreen();
    }
}
