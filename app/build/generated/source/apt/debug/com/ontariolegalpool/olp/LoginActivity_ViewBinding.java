// Generated code from Butter Knife. Do not modify!
package com.ontariolegalpool.olp;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target._emailText = Utils.findRequiredViewAsType(source, R.id.input_email, "field '_emailText'", EditText.class);
    target._passwordText = Utils.findRequiredViewAsType(source, R.id.input_password, "field '_passwordText'", EditText.class);
    target._loginButton = Utils.findRequiredViewAsType(source, R.id.btn_login, "field '_loginButton'", Button.class);
    target._signupButton = Utils.findRequiredViewAsType(source, R.id.btn_signup, "field '_signupButton'", Button.class);
    target.fb = Utils.findRequiredViewAsType(source, R.id.fbButton, "field 'fb'", Button.class);
    target._loginFacebookButton = Utils.findRequiredViewAsType(source, R.id.btn_login_with_facebook, "field '_loginFacebookButton'", LoginButton.class);
    target.google = Utils.findRequiredViewAsType(source, R.id.googleButton, "field 'google'", Button.class);
    target._loginGoogleButton = Utils.findRequiredViewAsType(source, R.id.btn_login_with_google, "field '_loginGoogleButton'", SignInButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._emailText = null;
    target._passwordText = null;
    target._loginButton = null;
    target._signupButton = null;
    target.fb = null;
    target._loginFacebookButton = null;
    target.google = null;
    target._loginGoogleButton = null;
  }
}
