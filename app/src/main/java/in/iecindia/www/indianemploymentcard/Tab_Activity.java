package in.iecindia.www.indianemploymentcard;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class Tab_Activity extends AppCompatActivity implements android.support.v7.app.ActionBar.TabListener {

    ViewPager pager;
    android.support.v7.app.ActionBar actionBar;
    FragmentPagerAdapter fragmentpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_);

        pager = (ViewPager) findViewById(R.id.pager);

        fragmentpager = new FragmentPagerAdapterTest(getSupportFragmentManager());
        //make page listener
        pager.setAdapter(fragmentpager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("myerror", "onPageScrolled:at position " + position + "from" + positionOffset + "with num of pixels" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                //attch page movement to tab
//                actionBar.setSelectedNavigationItem(position);
                Log.d("myerror", "onPageSelected: " + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    Log.d("myerror", "onPageScrolledStateIdle: " + state);
                }

                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    Log.d("myerror", "onPageScrolledStateDragging: " + state);

                }
                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    Log.d("myerror", "onPageScrolledStateIdle: " + state);

                }
            }
        });


        /*try {
            actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab().setText("Android").setTabListener(this));
            actionBar.addTab(actionBar.newTab().setText("Ios").setTabListener(this));
            actionBar.addTab(actionBar.newTab().setText("windows").setTabListener(this));

        } catch (Exception e) {
            Log.d("myerr", "onCreate: " + e);

        }*/

    }
    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        //attach tab to fragment
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        //  Log.d("myerror", "onTabUnSelected:at position "+tab.getPosition()+"name:"+tab.getText());

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        // Log.d("myerror", "onTabRe Selected:at position "+tab.getPosition()+"name:"+tab.getText());

    }
}
