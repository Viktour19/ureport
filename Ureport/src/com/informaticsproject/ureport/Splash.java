package com.informaticsproject.ureport;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SessionManagement sm = new SessionManagement(getApplicationContext());
		if(sm.isLoggedIn())
		{
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
		}
		setContentView(R.layout.activity_splash);
		findViewById(R.id.btnsignup).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), SignUp.class));
			}
		});
		findViewById(R.id.txtlogin).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(), LoginActivity.class));
			}
		});
		
//		Handler h = new Handler();
//		h.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				startActivity(new Intent(Splash.this, MainActivity.class));
//			}
//		}, 3000);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
	
		return true;
	}

}
