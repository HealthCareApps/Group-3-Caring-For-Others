package edu.fau.ngamarra2014.sync_care;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.fau.ngamarra2014.sync_care.R;

public class Information extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthcare_activity);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.healthcare_fragment, container, false);
            NestedScrollView info = (NestedScrollView) rootView.findViewById(R.id.infotab);
            NestedScrollView motor = (NestedScrollView) rootView.findViewById(R.id.motortab);
            NestedScrollView exercise = (NestedScrollView) rootView.findViewById(R.id.exercisetab);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                motor.setVisibility(View.INVISIBLE);
                exercise.setVisibility(View.INVISIBLE);
                info.setVisibility(View.VISIBLE);
               /* TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                motor.setVisibility(View.VISIBLE);
                exercise.setVisibility(View.INVISIBLE);
                info.setVisibility(View.INVISIBLE);
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                motor.setVisibility(View.INVISIBLE);
                exercise.setVisibility(View.VISIBLE);
                info.setVisibility(View.INVISIBLE);
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                motor.setVisibility(View.INVISIBLE);
                exercise.setVisibility(View.VISIBLE);
                info.setVisibility(View.INVISIBLE);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Balance Checklist";
                case 1:
                    return "Motor Symptoms";
                case 2:
                    return "Exercise List";
                case 3:
                    return "Target Heart Rate";
            }
            return null;
        }
    }
}
