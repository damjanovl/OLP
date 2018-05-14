package com.ontariolegalpool.olp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;


public class MainActivity extends AppCompatActivity {
//
//    @BindView(R.id.txt_fight_ticket) TextView _fightticketText;
//    @BindView(R.id.btn_fight_ticket) ImageButton _fightticketButton;
//    @BindView(R.id.txt_pay_ticket) TextView _payticketText;
//    @BindView(R.id.btn_pay_ticket) ImageButton _payticketButton;

    final String FRAGMENT_TAG="fragment_tag";

    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
            Log.d("MAINACTIVITY", "onBackPressed");
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottom_navigation);
        setupBottomBar(bottomBar);
        Log.d("MAIN_ACTIVITY", bottomBar.toString());
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        android.support.v4.app.Fragment oldFrag = fragmentManager.findFragmentByTag(FRAGMENT_TAG);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (tabId == R.id.tab_home){
                    Log.d("FRAG", "HOME");
                    transaction.replace(R.id.main_frame, new HomeFragment())
                            .addToBackStack("home")
                            .commit();
                }
                else if (tabId == R.id.tab_alert) {
                    Log.d("FRAG", "ALERT");
                    transaction.replace(R.id.main_frame, new AlertFragment())
                            .addToBackStack("alert")
                            .commit();
                }
                else if (tabId == R.id.tab_camera) {
                    Log.d("FRAG", "CAMERA");
                    transaction.replace(R.id.main_frame, new CameraFragment())
                            .addToBackStack("camera")
                            .commit();
                }
                else if (tabId == R.id.tab_ticket) {
                    Log.d("FRAG", "TICKET");
                    transaction.replace(R.id.main_frame, new TicketFragment())
                            .addToBackStack("ticket")
                            .commit();
                }
                else if (tabId == R.id.tab_profile) {
                    Log.d("FRAG", "PROFILE, backstackentrycount= " + fragmentManager.getBackStackEntryCount());
                    transaction.replace(R.id.main_frame, new ProfileFragment())
                            .addToBackStack("profile")
                            .commit();
                }
            }
        });
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(int tabId) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (tabId == R.id.tab_home){
                    Log.d("FRAG", "HOME");
                    transaction.replace(R.id.main_frame, new HomeFragment())
                            .addToBackStack("home")
                            .commit();
                }
                else if (tabId == R.id.tab_alert) {
                    Log.d("FRAG", "ALERT");
                    transaction.replace(R.id.main_frame, new AlertFragment())
                            .addToBackStack("alert")
                            .commit();
                }
                else if (tabId == R.id.tab_camera) {
                    Log.d("FRAG", "CAMERA");
                    transaction.replace(R.id.main_frame, new CameraFragment())
                            .addToBackStack("camera")
                            .commit();
                }
                else if (tabId == R.id.tab_ticket) {
                    Log.d("FRAG", "TICKET");
                    transaction.replace(R.id.main_frame, new TicketFragment())
                            .addToBackStack("ticket")
                            .commit();
                }
                else if (tabId == R.id.tab_profile) {
                    Log.d("FRAG", "PROFILE, backstackentrycount= " + fragmentManager.getBackStackEntryCount());
                    transaction.replace(R.id.main_frame, new ProfileFragment())
                            .addToBackStack("profile")
                            .commit();
                }
            }
        });
    }

    public void doBack() {
        Log.d("MAINACTIVITY", "doBack");
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_frame, new HomeFragment()).commit();
    }
//    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setupBottomBar(BottomBar bottomBar) {
        for (int i = 0; i < bottomBar.getTabCount(); i++) {
            BottomBarTab tab = bottomBar.getTabAtPosition(i);
            tab.setGravity(Gravity.CENTER);

            View icon = tab.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
            // the paddingTop will be modified when select/deselect,
            // so, in order to make the icon always center in tab,
            // we need set the paddingBottom equals paddingTop
            icon.setPadding(0, icon.getPaddingTop(), 0, icon.getPaddingTop());

            View title = tab.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
            title.setVisibility(View.GONE);
        }
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

        return super.onOptionsItemSelected(item);
    }
}
