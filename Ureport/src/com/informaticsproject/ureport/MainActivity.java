package com.informaticsproject.ureport;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	ArrayList<Ureport> ureports;
	ArrayList<Ureport> mostvotedreports;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(android.view.Window.FEATURE_INDETERMINATE_PROGRESS);

		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_main);
		
		ureports = Ureport.retrievedreports;
		mostvotedreports = Ureport.mostupvotedureports;
		if (ureports == null) {
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			ureports = db.getUReports();
		}

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		if (ureports == null) {
			UserFunctions uf = new UserFunctions(getApplicationContext());
			SessionManagement sm = new SessionManagement(
					getApplicationContext());
			if (!uf.retrievereport("0",
					sm.getUserDetails().get(SessionManagement.KEY_USER_ID))) {
				Ureport temp = new Ureport();
				temp.setUsername("admin");
				temp.setUpvote("*");
				temp.setText("Unable to retrieve report at the moment ;(");
				temp.setDatetime("Today, 00:00:00");
				ureports.add(temp);
			}
		} else {

			mSectionsPagerAdapter = new SectionsPagerAdapter(
					getSupportFragmentManager());

			mSectionsPagerAdapter.SectionsPagerAdaptervars(MainActivity.this,
					getResources(), ureports);
			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);

			// When swiping between different sections, select the corresponding
			// tab. We can also use ActionBar.Tab#select() to do this if we have
			// a reference to the Tab.
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						

						@Override
						public void onPageSelected(int position) {
							findViewById(R.id.list);
							View footerview = findViewById(R.id.showmore);
							ListView list = (ListView) findViewById(R.id.list);
					//		list.addFooterView(list, null, false);
							footerview
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {

											onShowmoreClick();

										}
									});
							Ureport temp = new Ureport();
							temp.setUsername("admin");
							temp.setUpvote("*");
							temp.setText("would be nice to be the first init? :)");
							temp.setDatetime("Today, 00:00:00");
							ArrayList<Ureport> reports = new ArrayList<Ureport>(ureports);
							reports.add(temp);

							
							switch (position) {
							case 0:
								list.setAdapter(new FeedAdapter(MainActivity.this, ureports, getResources()));
								break;

							case 1:
								list.setAdapter(new MostUpvotedFeedAdapter(MainActivity.this, mostvotedreports, getResources()));
								break;

							default:
								list.setAdapter(new FeedAdapter(MainActivity.this, ureports, getResources()));
								break;
							}
							actionBar.setSelectedNavigationItem(position);

						}
					});

			// For each of the sections in the app, add a tab to the action bar.
			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
				// Create a tab with text corresponding to the page title
				// defined by
				// the adapter. Also specify this Activity object, which
				// implements
				// the TabListener interface, as the callback (listener) for
				// when
				// this tab is selected.
				actionBar.addTab(actionBar.newTab()
						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setTabListener(this));

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_post:
			startActivity(new Intent(MainActivity.this, Post.class));
			break;
		case R.id.menu_logout:
			UserFunctions uf = new UserFunctions(getApplicationContext());
			uf.logoutUser(getApplicationContext());
			startActivity(new Intent(MainActivity.this, Splash.class));
			break;

		case R.id.action_refresh:
			RetrieveReports ir = new RetrieveReports();
			ir.execute();
			break;

		default:
			break;
		}
		;

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Activity a;
		Resources res;
		private ArrayList<Ureport> ureport;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public void SectionsPagerAdaptervars(Activity a, Resources res,
				ArrayList<Ureport> ureport) {

			this.a = a;
			this.res = res;
			this.ureport = ureport;

		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			DummySectionFragment.a = a;
			DummySectionFragment.reports = ureport;
			DummySectionFragment.res = res;

			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "recent".toUpperCase(l);
			case 1:
				return "most upvoted".toUpperCase(l);

			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */

	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		public static Activity a = null;
		public static Resources res = null;
		public static ArrayList<Ureport> reports = null;
		private ListView ListView;

		public DummySectionFragment() {
			super();

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int section_no = getArguments().getInt(ARG_SECTION_NUMBER);
			View rootView = null;

			rootView = inflater.inflate(
					R.layout.fragment_main_retrievedureports, container, false);
			ListView = (ListView) rootView.findViewById(R.id.list);
			ListView.setAdapter(new FeedAdapter(a, reports, res));

			return rootView;
		}
	}

	public void onShowmoreClick() {
		// TODO Auto-generated method stub
		String index = ureports.get(Ureport.retrievedreports.size() - 1).id;
		UserFunctions uf = new UserFunctions(getApplicationContext());
		SessionManagement sm = new SessionManagement(getApplicationContext());
		uf.retrievereport(index,
				sm.getUserDetails().get(SessionManagement.KEY_USER_ID));

	}

	protected void onShowmoreClickMOstVoted() {
		// TODO Auto-generated method stub

	}

	public void onItemClick(int mPosition) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), UreportActivity.class);
		i.putExtra("pos", mPosition);
	
		startActivity(i);
	}

	class RetrieveReports extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			findViewById(R.id.action_refresh).setVisibility(View.VISIBLE);
			setProgressBarIndeterminate(false);
			setProgressBarIndeterminateVisibility(false);
			if (!result)

				super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			findViewById(R.id.action_refresh).setVisibility(View.INVISIBLE);
			setProgressBarIndeterminate(true);
			setProgressBarIndeterminateVisibility(true);
			
			UserFunctions uf = new UserFunctions(getApplicationContext());
			String lastid = null, userid = null;
			if (ureports != null) {
				lastid = ureports.get(ureports.size() - 1).getId();
				SessionManagement sm = new SessionManagement(
						getApplicationContext());
				userid = sm.getUserDetails().get(SessionManagement.KEY_USER_ID);
			}
			if (lastid != null && userid != null) {
				boolean done = uf.retrievereport(lastid, userid);
				if (done)
					return true;
			}

			return false;

		}

	}

	public void onItemClick2(int mPosition) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getApplicationContext(), UreportActivity.class);
		i.putExtra("pos", mPosition);
		
		
		UreportActivity.selected = mostvotedreports.get(mPosition);
		startActivity(i);
	}
}
