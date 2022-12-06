package cscb07.group4.androidproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cscb07.group4.androidproject.manager.AccountManager;


@RunWith(MockitoJUnitRunner.class)
public class LoginPageTest {

    @Mock
    LoginFragment view;

    @Mock
    AccountManager model;

    /**
     * Tests the text fields with an invalid email (no @).
     */
    @Test
    public void testProperLogin() {
        when(view.getEmail()).thenReturn("email@gmail.com");
        when(view.getPassword()).thenReturn("password");

        LoginPresenter presenter = new LoginPresenter(view, model);

        assertTrue(presenter.checkEmail());
        assertTrue(presenter.checkPassword());
    }

    /**
     * Tests the text fields with an invalid email (no @).
     */
    @Test
    public void testInvalidEmail() {
        when(view.getEmail()).thenReturn("email######gmail.com");
        when(view.getPassword()).thenReturn("password");

        LoginPresenter presenter = new LoginPresenter(view, model);

        assertFalse(presenter.checkEmail());
    }

    /**
     * Tests the text fields with an empty email.
     */
    @Test
    public void testEmptyEmail() {
        when(view.getEmail()).thenReturn("");
        when(view.getPassword()).thenReturn("password");

        LoginPresenter presenter = new LoginPresenter(view, model);

        assertFalse(presenter.checkEmail());
    }

    /**
     * Tests the text fields with an empty password.
     */
    @Test
    public void testEmptyPassword() {
        when(view.getEmail()).thenReturn("email@gmail.com");
        when(view.getPassword()).thenReturn("");

        LoginPresenter presenter = new LoginPresenter(view, model);

        assertFalse(presenter.checkPassword());
    }
}