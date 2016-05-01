package edu.fau.ngamarra2014.sync_care;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Information extends NavigationActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.healthcare_activity, null, false);
        drawer.addView(contentView, 0);

        getSupportActionBar().setTitle("Parkinson's Disease Information");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
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
