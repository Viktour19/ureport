package com.informaticsproject.ureport;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

public class MostUpvotedFeedAdapter extends BaseAdapter implements OnClickListener {

	/*********** Declare Used Variables *********/
	private Activity activity;
	private ArrayList<Ureport> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	Ureport tempValues = null;
	int i = 0;

	public MostUpvotedFeedAdapter(Activity a, ArrayList<Ureport> d, Resources resLocal) {
		// TODO Auto-generated constructor stub
		/********** Take passed values **********/
		activity = a;
		data = d;
		res = resLocal;

		/*********** Layout inflator to call external xml layout () ***********/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/******** What is the size of Passed Arraylist Size ************/
	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {

		public TextView UreportText;
		public TextView dateTimeText;
		public TextView usernameText, tagtext;
		public TableRow download;
		public TextView downVoteText;
		public TextView upVoteText;
		public ImageView up, down, media;
		public CheckBox duplicate;

	}

	/****** Depends upon data size called for each row , Create each ListView row *****/
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		if (convertView == null) {

			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			tempValues = null;
			// tempValues = (Ureport) data.get(position);

			vi = inflater.inflate(R.layout.ureportitem, null);
			/******
			 * View Holder Object to contain tabitem.xml file elements
			 * 
			 * ******/

			holder = new ViewHolder();
			holder.UreportText = (TextView) vi.findViewById(R.id.txtUreport);
			holder.dateTimeText = (TextView) vi
					.findViewById(R.id.txtUreportDateTime);
			holder.upVoteText = (TextView) vi
					.findViewById(R.id.txtUreportUpvote);
			holder.usernameText = (TextView) vi
					.findViewById(R.id.txtUreportusername);
			holder.up = (ImageView) vi.findViewById(R.id.up);

			/************ Set holder with LayoutInflater ************/
			vi.setTag(holder);
		} else

			holder = (ViewHolder) vi.getTag();

		if (data.size() <= 0) {

			holder.UreportText.setText("No Report to display");
			holder.dateTimeText.setText("00:00 today");
			holder.upVoteText.setText("No Upvote");
			holder.usernameText.setText("@admin");
		} else {
			/***** Get each Model object from Arraylist ********/
			tempValues = null;
			tempValues = (Ureport) data.get(position);

			holder.UreportText.setText(tempValues.getText());
			holder.dateTimeText.setText(tempValues.getDatetime());
			holder.upVoteText.setText(tempValues.getUpvote() + " Up-Votes");
			holder.usernameText.setText("@" + tempValues.getUsername());

			vi.setOnClickListener(new OnItemClickListener(position));
		}
		return vi;
	}

	@Override
	public void onClick(View v) {
		Log.v("CustomAdapter", "=====Row button clicked=====");
	}

	/********* Called when Item click in ListView ************/
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			MainActivity sct = (MainActivity) activity;
			sct.onItemClick2(mPosition);
		}
	}
}