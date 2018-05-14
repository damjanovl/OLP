package com.ontariolegalpool.olp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private final int REQUEST_PHONE_PERMISSION = 4321;

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public AccessTokenTracker accessTokenTracker;
    public Bitmap bmp;
    String userID, userNumber, email, firstName, lastName, accessToken, imageFile;
    ByteArrayOutputStream bYtE;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.fbButton) Button fb;
    @BindView(R.id.btn_login_with_facebook) LoginButton _loginFacebookButton;
    @BindView(R.id.googleButton) Button google;
    @BindView(R.id.btn_login_with_google) SignInButton _loginGoogleButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ontariolegalpool.firebaseio.com/");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ontariolegalpool.olp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // pass
        } catch (NoSuchAlgorithmException e) {
            // pass
        }

        if (isLoggedIn()){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
//                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _loginFacebookButton.performClick();
            }
        });

        // Implement Facebook Login through Firebase
        _loginFacebookButton.setReadPermissions("email", "public_profile");
        _loginFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FB_LOGIN", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FB_LOGIN", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB_LOGIN", "facebook:onError:", error);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d("FB_USER", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    String email = user.getEmail();
                    String name = user.getDisplayName();
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("FB_USER", "SIGNED_OUT");
                }
            }
        };
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d("FB_LOGGED_IN:", "" + accessToken);
        return accessToken != null;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    progressDialog.dismiss();
                    onLoginSuccess();
                }
                else {
                    progressDialog.dismiss();
                    onLoginFailed();
                }
            }
        });
    }

    private void handleFacebookAccessToken(final AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            accessToken = token.getToken();
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID  = user.getUid();
                            userNumber = getMobileNumber();
                            GraphRequest request = GraphRequest.newMeRequest(
                                    token,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.d("FB_LOGIN", "GETTING USER EMAIL");
                                            Bundle facebookData = getFacebookData(object, userID, userNumber);
                                        }
                                    }
                            );
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,email,gender");
                            request.setParameters(parameters);
                            request.executeAsync();
                            onLoginSuccess();
                        }
                        else {
                            onLoginFailed();
                        }
                    }
                });
    }

    private Bundle getFacebookData(JSONObject object, String userID, String userNumber) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }


            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
            }
            if (object.has("last_name")) {
                bundle.putString("last_name", object.getString("last_name"));
            }
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
            }
            if (object.has("gender")) {
                bundle.putString("gender", object.getString("gender"));
            }
            Log.d("FB_OBJ", "object: " + bundle);

            UserInformation userInformation = new UserInformation(object.getString("email"),
                            object.getString("first_name"), object.getString("last_name"),
                            userNumber, "No Image", bundle.getString("profile_pic"));
//            myRef.child("users").child(userID).setValue(userInformation);
            new DownloadImagesTask().execute(userInformation);

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : "+e.toString());
        }

        return bundle;
    }

    public class DownloadImagesTask extends AsyncTask<UserInformation, Void, UserInformation> {

        UserInformation userInfo = null;

        @Override
        protected UserInformation doInBackground(UserInformation... userInformation) {
            return download_Image(userInformation[0]);
        }

        @Override
        protected void onPostExecute(UserInformation userInformation) {
            myRef.child("users").child(userID).setValue(userInformation);
        }


        private UserInformation download_Image(UserInformation userInformation) {
            Bitmap bmp =null;
            String url = userInformation.getTag();
            Log.d("DOWNLOAD", "URL: " + url);
            try{
                URL ulrn = new URL(url);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp) {
                    bYtE = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
                    bmp.recycle();
                    byte[] byteArray = bYtE.toByteArray();
                    imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("SETTING", "Image File: " + imageFile);
                    userInformation.setImage(imageFile);
                    return userInformation;
                }
            }
            catch (Exception e){
                Log.d("PROFILE PIC", "Failed download. Setting basic user information. Error: " + e);
            }
            return userInformation;
        }
    }

//    public Bitmap getBitmapFromURL(String src) {
//        try {
//            java.net.URL url = new java.net.URL(src);
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public String getMobileNumber(){

        TelephonyManager telephonyManager;
        if (!checkPhonePermission()) {
            Log.d("ASKING PHONE", "TEST");
            requestPhonePermission();
        }
        if (!checkPhonePermission()){
            return "No Number";
        }
        else {
            telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String strMobileNumber = telephonyManager.getLine1Number();
            return strMobileNumber;
        }
    }

    private boolean checkPhonePermission() {
        return ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPhonePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_PHONE_STATE},
                REQUEST_PHONE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PHONE", "permission granted.");
                return;
//                continue to get phone number;
            } else {
                Log.d("PHONE", "permission denied.");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TEST", "ReqCode: " + requestCode + "ResCode: "+ resultCode+ "Data: " + data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
