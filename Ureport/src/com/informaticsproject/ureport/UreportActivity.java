package com.informaticsproject.ureport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UreportActivity extends Activity {
	private String datetime, docurl, mediaurl, text, username, upvote, isvoted, ismarked; 
	String userid, reportid;
	static Ureport selected;	
	RelativeLayout RelativeLayoutupvote;
	MyTextView timestampTV, textTV, savedocTV, savemediaTV, usernameTV, docnameTV, upvotesTV;
	ImageView mediaIM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ureport);
		datetime = selected.getDatetime();
		docurl	= selected.getDocurl();
		mediaurl = selected.getMediaurl();
		text = selected.getText();
		username = selected.getUsername();
		upvote = selected.getUpvote();
		isvoted = selected.getIsvoted();
		ismarked = selected.getIsmarked();
		
		RelativeLayoutupvote = (RelativeLayout)findViewById(R.id.RelativeLayoutupvote);
		timestampTV = (MyTextView)findViewById(R.id.timestamp);
		textTV = (MyTextView)findViewById(R.id.text);
		savedocTV = (MyTextView)findViewById(R.id.savedoc);
		savemediaTV = (MyTextView)findViewById(R.id.savemedia);
		usernameTV = (MyTextView)findViewById(R.id.username);
		docnameTV = (MyTextView)findViewById(R.id.docname);
		upvotesTV = (MyTextView)findViewById(R.id.upvote);
		
		SessionManagement sm = new SessionManagement(getApplicationContext());
		userid = sm.getUserDetails().get(SessionManagement.KEY_USER_ID);
		reportid = selected.getId();
		if(Integer.parseInt(isvoted) == 0)
		{
			RelativeLayoutupvote.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					UserFunctions uf = new UserFunctions(getApplicationContext());
					boolean upvoted = uf.upvote(userid, reportid);
					if(upvoted)
						RelativeLayoutupvote.setClickable(false);
						upvotesTV.setText(R.id.upvote);
				}
			});
		}
		else
		{
			RelativeLayoutupvote.setClickable(false);
		}
		timestampTV.setText(datetime);
		textTV.setText(text);
		final String ext = docurl.substring(docurl.indexOf('.'));
		savedocTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				downloadFile df = new downloadFile();
				df.execute(docurl, ext);
			}
			});
		savemediaTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				downloadFile df = new downloadFile();
				df.execute(mediaurl, ".jpg");
			}
		});
		
		docnameTV.setText(username+ext);
		usernameTV.setText(username);
		upvotesTV.setText(upvote);
		ImageLoader il = new ImageLoader(UreportActivity.this);
		il.DisplayImage(mediaurl, R.id.useLogo, mediaIM);
	}

	class downloadFile extends AsyncTask<String, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			 try {
		            
		            String targetFileName = username + params[1];

		            URL u = new URL(params[0]);
		            HttpURLConnection c = (HttpURLConnection) u.openConnection();
		            c.setRequestMethod("GET");
		            c.setDoOutput(true);
		            c.connect();

		            String PATH_op = Environment.getExternalStorageDirectory()  + targetFileName;

		           

		            FileOutputStream f = new FileOutputStream(new File(PATH_op));

		            InputStream in = c.getInputStream();
		            byte[] buffer = new byte[1024];
		            int len1 = 0;
		            while ( (len1 = in.read(buffer)) > 0 ) {
		                f.write(buffer,0, len1);
		            }

		            f.close();

		            } catch (MalformedURLException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            } catch (ProtocolException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            } catch (FileNotFoundException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            } catch (IOException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		            Toast.makeText(UreportActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		        }

		        

			return null;
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
