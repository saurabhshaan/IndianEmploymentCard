package in.iecindia.www.indianemploymentcard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by DHARMENDRA on 12-Oct-16.
 */
public class FragmentPagerAdapterTest extends FragmentPagerAdapter {
    public FragmentPagerAdapterTest(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position==0)
        {
            fragment=new Profile();
        }
        if (position==1)
        {
            fragment=new News();
        }
        if (position==2)
        {
            fragment=new IEC_card();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}