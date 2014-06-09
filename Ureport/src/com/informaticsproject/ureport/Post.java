package com.informaticsproject.ureport;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Post extends Activity {

	private static final int PICTURE_ID = 1;
	private static final int DOUMENT_ID = 2;
	private int serverResponseCode;
	private PostReport pr = null;
	private String mediaurl, docurl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		findViewById(R.id.addmedia).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				

				new Thread(new Runnable() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								//messageText.setText("uploading started.....");
							}
						});

						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
					
						startActivityForResult(
								Intent.createChooser(intent, getResources()
										.getText(R.string.chooser_image)),
								PICTURE_ID);
						

					}
				}).start();
			}
		});
	
		findViewById(R.id.adddoc).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.addCategory(Intent.CATEGORY_OPENABLE);
		        intent.setType("text/csv");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        startActivityForResult(Intent.createChooser(intent, getText(R.string.chooser_file)),DOUMENT_ID);
			}
		});
		
		findViewById(R.id.btnpost).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(pr!= null)
				{
					return;
				}
				TextView postTV = (TextView)findViewById(R.id.txtpost);
				String post = postTV.getText().toString();
				if(TextUtils.isEmpty(post))
				{
					postTV.setError(getString(R.string.error_field_required));
				}
				else
				{
					pr = new PostReport();
					pr.execute(post);
				}
			}
		});
	}
		
	class PostReport extends AsyncTask<String, Void, Boolean>
	{
		
		private ProgressDialog PDialog;

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			PDialog.dismiss();
			if(result)
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
			else
				Toast.makeText(getApplicationContext(), "Unable to add ureport", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			PDialog = new ProgressDialog(Post.this);
			PDialog.setMessage("Adding Ureport..");
			PDialog.setCancelable(false);
			PDialog.setIndeterminate(false);
			PDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserFunctions uf = new UserFunctions(getApplicationContext());
			String post = params[0];
			SessionManagement sm = new SessionManagement(getApplicationContext());
			String uid = sm.getUserDetails().get(SessionManagement.KEY_USER_ID);
			Boolean st = uf.addreport(uid , post, mediaurl, docurl);
			return st;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) // if there was no error
		{
			
			Uri selectedUri = data.getData();

			
			if (requestCode == PICTURE_ID) {
				
				String[] fp = {MediaColumns.DATA};
				Cursor cursor = getContentResolver().query(selectedUri, fp, null, null, null);
				cursor.moveToFirst();
				int coli = cursor.getColumnIndex(fp[0]);					
				String uploadFullPath = cursor.getString(coli);
				cursor.close();
				upload up  = new upload();
				up.execute(uploadFullPath);
				ImageLoader loader = new ImageLoader(getApplicationContext());
				loader.clearCache();
			}
			else if(requestCode == DOUMENT_ID)
			{
				Uri currFileURI = data.getData();
				String path = currFileURI.getPath();
				upload up  = new upload();
				up.execute(path);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class upload extends AsyncTask<String, Void, Boolean> 
	{

		private ProgressDialog PDialog;

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stuba
			String fullpath = arg0[0];
			uploadFile(fullpath, null);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			PDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			PDialog = new ProgressDialog(Post.this);
			PDialog.setMessage("Uploading..");
			PDialog.setCancelable(false);
			PDialog.setIndeterminate(false);
			PDialog.show();
			super.onPreExecute();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_feeds:	
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
			break;
		case R.id.menu_profile:
			break;
		case R.id.menu_logout:
			UserFunctions uf = new UserFunctions(getApplicationContext());
			uf.logoutUser(getApplicationContext());
			startActivity(new Intent(getApplicationContext(), Splash.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public int uploadFile(String sourceFileUri, String ext) {
		
		String uploadFullPath = sourceFileUri;
		SessionIdentifierGenerator ssi =new SessionIdentifierGenerator();
		String fileName = null;
		if(ext == null)
		{
			fileName = ssi.nextSessionId()+".jpg";
			mediaurl = "therebels.com.ng/ureport/uploads/"+fileName;
		}
		else
		{
			fileName = ssi.nextSessionId()+ ext;
			docurl = "therebels.com.ng/ureport/uploads/"+fileName;
		}
			
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);
		
		if (!sourceFile.isFile()) {
			
			Log.e("uploadFile", "Source File not exist :" + uploadFullPath);

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),"sourcefile does not exist" , Toast.LENGTH_SHORT).show();
				}
			});

			return 0;

		} else {
			try {
				String upLoadServerUri = "http://therebels.com.ng/ureport/UploadToServer.php";

				// open a URL connection to the Servlet
				
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
						+ fileName + "" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							String msg = "File Upload Completed.";
							
							Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							
							//ImageView pp = (ImageView) findViewById(R.id.editProfilePicture);
						//	pp.setImageBitmap(BitmapFactory.decodeFile(uploadFullPath));
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();
			} 
			catch (MalformedURLException ex) {

			
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						/*messageText
								.setText("MalformedURLException Exception : check script url.");*/
						Toast.makeText(Post.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						/*messageText.setText("Got Exception : see logcat ");*/
						Toast.makeText(Post.this,
								"Unable to upload, please check internet connection ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			
			return serverResponseCode;

		} // End else block
	}

	
}
