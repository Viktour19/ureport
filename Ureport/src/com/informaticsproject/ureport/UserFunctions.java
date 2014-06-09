package com.informaticsproject.ureport;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class UserFunctions {

	private JSONParser jsonParser;
	private Context context;

	public static String URL = "http://192.168.43.7/ureport/";

	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String getprofile_tag = "get_profile";
	private static String updateprofile_tag = "update_profile";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	public UserFunctions(Context ct) {
		jsonParser = new JSONParser();
		context = ct;
	}

	public JSONObject getPass(String username) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "pass_rec"));
		params.add(new BasicNameValuePair("username", username));

		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public JSONObject loginUser(String username, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		// return json
		// Log.e("JSON", json.toString());
		return json;
	}

	public JSONObject registerUser(String username, String password,
			String email, String usertype) {
		// Building Parameters

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("usertype", usertype));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		// return json
		return json;
	}

	public JSONObject get_profile(String username) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", getprofile_tag));
		params.add(new BasicNameValuePair("username", username));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		// return json
		return json;
	}

	public JSONObject updateProfile(String username, String email,
			String password) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", updateprofile_tag));
		params.add(new BasicNameValuePair("usernmae", username));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		return json;
	}

	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getUserRowCount();
		if (count > 0) {
			// user logged in
			return true;
		}
		return false;
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {
		DatabaseHandler db = new DatabaseHandler(context);
		SessionManagement session = new SessionManagement(context);
		session.logoutUser();
		db.resetTables();
		return true;
	}

	public Boolean addreport(String userid, String text, String mediaurl,
			String docurl) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "addreport"));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("text", text));
		params.add(new BasicNameValuePair("mediaurl", mediaurl));
		params.add(new BasicNameValuePair("docurl", docurl));

		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		if (json != null) {

			try {
				if (Integer.parseInt(json.getString("success")) == 1) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		return false;
	}
	public Boolean upvote(String userid, 
			String repid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "upvote"));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("id", repid));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		if (json != null) {

			try {
				if (Integer.parseInt(json.getString("success")) == 1) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		return false;
	}
	public Boolean duplicate(String userid, 
			String repid) {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", "duplicate"));
		params.add(new BasicNameValuePair("userid", userid));
		params.add(new BasicNameValuePair("id", repid));
		JSONObject json = jsonParser.getJSONFromUrl(URL, params);
		if (json != null) {

			try {
				if (Integer.parseInt(json.getString("success")) == 1) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		return false;
	}

	public boolean retrievereport(String lastid, String userid) {
		ConnectionDetector cd = new ConnectionDetector(context);
		if (cd.isConnectingToInternet()) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "retrievereports"));
			params.add(new BasicNameValuePair("lastid", lastid));

			JSONObject json = jsonParser.getJSONFromUrl(URL, params);
			if (json != null) {

				try {
					if (Integer.parseInt(json.getString("success")) == 1) {
						Ureport.retrievedreports = new ArrayList<Ureport>();
						JSONArray reparry = json
								.getJSONArray("retrieved_reports");
						for (int i = 0; i < reparry.length(); i++) {
							if (reparry.get(i) != null) {
								JSONObject jb = (JSONObject) reparry.get(i);
								String username = jb.getString("username");
								String mediaurl = jb.getString("mediaurl");
								String docurl = jb.getString("docurl");
								String upvotes = jb.getString("upvotes");
								String duplicates = jb.getString("duplicates");
								String timestamp = jb.getString("timestamp");
								String id = jb.getString("id");
								String upvoted = jb.getString("upvoted");
								String marked = jb.getString("marked");
								String isvoted = jb.getString("isvoted");
								String ismarked = jb.getString("ismarked");
								
								Ureport rep = new Ureport();
								rep.setUsername(username);
								rep.setMediaurl(mediaurl);
								rep.setDocurl(docurl);
								rep.setUpvote(upvotes);
								rep.setDuplicate(duplicates);
								rep.setDatetime(timestamp);
								rep.setId(id);								
								rep.isvoted = isvoted;
								rep.ismarked = ismarked;
								Ureport.retrievedreports.add(rep);
							}
						}
						return true;
					}

					else {
						return false;

					}

				} catch (Exception e) {
					// TODO: handle exception
					return false;
				}
			}

		}
		return false;
	}

	public boolean retrievemostupvotedreports(String lastid, String userid) {
		ConnectionDetector cd = new ConnectionDetector(context);
		if (cd.isConnectingToInternet()) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag", "retrievemostvotedreports"));
			params.add(new BasicNameValuePair("lastid", lastid));

			JSONObject json = jsonParser.getJSONFromUrl(URL, params);
			if (json != null) {

				try {
					if (Integer.parseInt(json.getString("success")) == 1) {

						Ureport.retrievedreports = new ArrayList<Ureport>();
						JSONArray reparry = json
								.getJSONArray("retrieved_reports");
						for (int i = 0; i < reparry.length(); i++) {
							if (reparry.get(i) != null) {
								JSONObject jb = (JSONObject) reparry.get(i);
								String username = jb.getString("username");
								String mediaurl = jb.getString("mediaurl");
								String docurl = jb.getString("docurl");
								String upvotes = jb.getString("upvotes");
								String duplicates = jb.getString("duplicates");
								String timestamp = jb.getString("timestamp");
								String id = jb.getString("id");
								String upvoted = jb.getString("upvoted");
								String marked = jb.getString("marked");

								Ureport rep = new Ureport();
								rep.setUsername(username);
								rep.setMediaurl(mediaurl);
								rep.setDocurl(docurl);
								rep.setUpvote(upvotes);
								rep.setDuplicate(duplicates);
								rep.setDatetime(timestamp);
								rep.setId(id);
								rep.setIsvoted(upvoted);
								rep.setIsmarked((marked));

								Ureport.retrievedreports.add(rep);
							}

						}
						return true;
					} else {
						return false;
					}
				} catch (Exception e) {
					// TODO: handle exception
					return false;
				}
			}

		}
		return false;
	}

}
