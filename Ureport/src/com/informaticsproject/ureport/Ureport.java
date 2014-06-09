package com.informaticsproject.ureport;

import java.util.ArrayList;

public class Ureport {

	public static ArrayList<Ureport> retrievedreports = null, mostupvotedureports= null;
	String username;
	String text, docurl, mediaurl, id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	String datetime;
	String upvote = "0";
	String duplicate = "0";
	String isvoted = "0";
	String ismarked = "0";
	
	public static ArrayList<Ureport> getRetrievedreports() {
		return retrievedreports;
	}
	public static void setRetrievedreports(ArrayList<Ureport> retrievedreports) {
		Ureport.retrievedreports = retrievedreports;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDocurl() {
		return docurl;
	}
	public void setDocurl(String docurl) {
		this.docurl = docurl;
	}
	public String getMediaurl() {
		return mediaurl;
	}
	public void setMediaurl(String mediaurl) {
		this.mediaurl = mediaurl;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getUpvote() {
		return upvote;
	}
	public void setUpvote(String upvote) {
		this.upvote = upvote;
	}
	public String getDuplicate() {
		return duplicate;
	}
	public void setDuplicate(String duplicate) {
		this.duplicate = duplicate;
	}
	public static ArrayList<Ureport> getMostupvotedureports() {
		return mostupvotedureports;
	}
	public static void setMostupvotedureports(ArrayList<Ureport> mostupvotedureports) {
		Ureport.mostupvotedureports = mostupvotedureports;
	}
	public String getIsvoted() {
		return isvoted;
	}
	public void setIsvoted(String isvoted) {
		this.isvoted = isvoted;
	}
	public String getIsmarked() {
		return ismarked;
	}
	public void setIsmarked(String ismarked) {
		this.ismarked = ismarked;
	}
	
	
	

}
