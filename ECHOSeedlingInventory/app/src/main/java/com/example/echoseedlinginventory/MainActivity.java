package com.example.echoseedlinginventory;

import android.content.Intent;
import android.os.Bundle;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity
    implements BottomNavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private GoogleSignInClient mGoogleSignInClient;
   public NavigationView navigationView;
   public TextView navEmail;
   public GoogleSignInAccount myaccount;
   ExpandableRelativeLayout noNet;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        NetworkConnection.setMainActivity(this);
        setContentView(R.layout.activity_main);
        loadFragment(new InventoryFragment());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.getMenu().findItem(R.id.navigation_inventory).setChecked(true);
        navView.setOnNavigationItemSelectedListener(this);
       noNet = findViewById(R.id.banner_no_internet);
        noNet.collapse();
        drawerLayout = findViewById(R.id.logout_sheet);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close){
            public void onDrawerClosed(View drawerView){
                drawerLayout.setVisibility(View.INVISIBLE);
            }
        };
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_drawer);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                signOut();
                revokeAccess();
                return false;
            }
        });

        







        mGoogleSignInClient = LoginActivity.mGoogleSignInClient;


        myaccount = GoogleSignIn.getLastSignedInAccount(this);


        NetworkConnection.checkConnection(this);



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            drawerLayout.setVisibility(View.VISIBLE);
            navEmail =  navigationView.getHeaderView(0).findViewById(R.id.email_in_header);

            if (myaccount == null)
                Toast.makeText(this, "No Account Found", Toast.LENGTH_LONG).show();
            else
                navEmail.setText(myaccount.getEmail());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean loadFragment (Fragment toLoad){

        if(toLoad != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, toLoad)
                    .commit();
            return true;
        }

        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment frag = null;

        switch (menuItem.getItemId()){

            case R.id.navigation_inventory:
                frag = new InventoryFragment();

                break;
            case R.id.navigation_reports:
                frag = new ReportsFragment();

                break;
            case R.id.navigation_notifications:
                frag = new NotificationsFragment();

            default:
                    break;


        }
        return loadFragment(frag);
    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(myIntent);
                    }
                });
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


}
