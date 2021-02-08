package com.example.abhisehkgupta.orderkiya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhisehkgupta.orderkiya.fragment.HomeFragment;
import com.example.abhisehkgupta.orderkiya.fragment.OrderHistory;

public class HomeActivity extends AppCompatActivity
{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
   // private ImageView  imgProfile;
    private TextView txtName;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;
    // tags used to attach the fragments
    private static final String TAG_HOME = "customer";
    private static final String ORDER_HISTORY = "order_history";
    //private static final String USER_LIST="user_list";
    public static String CURRENT_TAG = TAG_HOME;
    private String[] activityTitles = {"CUSTOMER","ORDER HISTORY"};
    SharedPreferences sharedpreferences;
    public static String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer =  findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nav_view);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);

        //textview name is set in navigation header
        txtName = navHeader.findViewById(R.id.name);
        //imgProfile =  navHeader.findViewById(R.id.img_profile);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();
        if (savedInstanceState == null)
        {

        }
        navItemIndex = 0;
        CURRENT_TAG = TAG_HOME;
        loadFragment();

        if(sharedpreferences.getInt("userType",0) == 0)
        {
            activityTitles[0] = "SELLER";
            navigationView.getMenu().getItem(0).setTitle("SELLER");
        }
        else
        {
            activityTitles[0] = "CUSTOMER";
            navigationView.getMenu().getItem(0).setTitle("CUSTOMER");
        }

    }

    private void loadNavHeader()
    {
        // name, website
        txtName.setText("Abhishek");

        // loading header background image
        // Glide.with(this).load(urlNavHeaderBg)
        //.crossFade()
        //.diskCacheStrategy(DiskCacheStrategy.ALL)
        //.into(imgNavHeaderBg);

        // Loading profile image
        //Glide.with(this).load(urlProfileImg)
        //.crossFade()
        //.thumbnail(0.5f)
        //.bitmapTransform(new CircleTransform(this))
        //.diskCacheStrategy(DiskCacheStrategy.ALL)
        //.into(imgProfile);

        // showing dot next to notifications label
       // navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void setUpNavigationView()
    {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId())
                {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.order_history:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        loadFragment();
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = ORDER_HISTORY;
                        loadFragment();
                        break;

                    case R.id.logout:
                        logout();
                        break;

                    default:
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked())
                {
                    menuItem.setChecked(false);
                }

                else
                {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                //loadHomeFragment();
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                                                                                R.string.openDrawer, R.string.closeDrawer)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadFragment()
    {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();



        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null)
        {
            drawer.closeDrawers();

            return;
        }


        //setting up the fragment in the activity
        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commit();


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment()
    {
        switch (navItemIndex)
        {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                OrderHistory orderHistory = new OrderHistory();
                return orderHistory;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle()
    {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu()
    {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
//        if (navItemIndex == 0)
//        {
//            getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }
    public void logout()
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();

        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
