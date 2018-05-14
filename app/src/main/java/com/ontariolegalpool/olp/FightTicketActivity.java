package com.ontariolegalpool.olp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FightTicketActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;

    @BindView(R.id.btn_fight_ticket) ImageButton _fightTicket;
    @BindView(R.id.btn_pay_ticket) ImageButton _payTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight_ticket);
        ButterKnife.bind(this);


//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Log.d("FB_USER", "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    Log.d("FB_USER", "SIGNED_OUT");
//                    Intent intent = new Intent(FightTicketActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.logout:
                logout();
        }
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        Log.d("LOGOUT", "logging out Firebase");
        FirebaseAuth.getInstance().signOut();
        Log.d("LOGOUT", "logging out Facebook");
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}