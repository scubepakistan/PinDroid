package com.pindroid.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.pindroid.activity.AddBookmark;
import com.pindroid.activity.BrowseBookmarks;
import com.pindroid.activity.BrowseNotes;
import com.pindroid.activity.BrowseTags;
import com.pindroid.activity.ViewBookmark;
import com.pindroid.Constants;
import com.pindroid.Constants.BookmarkViewType;
import com.pindroid.providers.BookmarkContent.Bookmark;
import com.pindroid.providers.BookmarkContentProvider;
import com.pindroid.providers.NoteContent.Note;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentHelper {

	public static Intent OpenInBrowser(String url){
    	Uri link = Uri.parse(url);
		return new Intent(Intent.ACTION_VIEW, link);
	}
	
	public static Intent ReadBookmark(String url){
    	String readUrl = "";
		try {
			readUrl = Constants.TEXT_EXTRACTOR_URL + URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	Uri readLink = Uri.parse(readUrl);
		return new Intent(Intent.ACTION_VIEW, readLink);
	}
	
	public static Intent SendBookmark(String url, String title) {
    	Intent sendIntent = new Intent(Intent.ACTION_SEND);
    	sendIntent.setType("text/plain");
    	sendIntent.putExtra(Intent.EXTRA_TEXT, url);
    	sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
    	sendIntent.putExtra(Intent.EXTRA_TITLE, title);
    	
    	return sendIntent;
	}
	
	public static Intent AddBookmark(String url, String account, Context context) {
		Intent addBookmark = new Intent(context, AddBookmark.class);
		addBookmark.setAction(Intent.ACTION_SEND);
		if(url != null)
			addBookmark.putExtra(Intent.EXTRA_TEXT, url);
		
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("bookmarks");
		addBookmark.setData(data.build());
		
		return addBookmark;
	}
	
	public static Intent ViewBookmark(Bookmark b, BookmarkViewType type, String account, Context context) {
		Intent viewBookmark = new Intent(context, ViewBookmark.class);
		viewBookmark.setAction(Intent.ACTION_VIEW);
		viewBookmark.addCategory(Intent.CATEGORY_DEFAULT);
		viewBookmark.putExtra("com.pindroid.BookmarkViewType", type);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("bookmarks");
		
		if(b.getId() != 0) {
			data.appendEncodedPath(Integer.toString(b.getId()));
		} else {
			data.appendEncodedPath(Integer.toString(0));
			data.appendQueryParameter("url", b.getUrl());
			data.appendQueryParameter("title", b.getDescription());
			data.appendQueryParameter("notes", b.getNotes());
			data.appendQueryParameter("tags", b.getTagString());
			data.appendQueryParameter("time", Long.toString(b.getTime()));
			data.appendQueryParameter("account", b.getAccount());
		}
		viewBookmark.setData(data.build());
		
		return viewBookmark;
	}
	
	public static Intent ViewNote(Note n, String account, Context context) {
		Intent viewBookmark = new Intent(context, com.pindroid.activity.ViewNote.class);
		viewBookmark.setAction(Intent.ACTION_VIEW);
		viewBookmark.addCategory(Intent.CATEGORY_DEFAULT);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("notes");
		
		data.appendEncodedPath(Integer.toString(n.getId()));

		viewBookmark.setData(data.build());
		
		return viewBookmark;
	}
	
	public static Intent EditBookmark(Bookmark b, String account, Context context) {
		Intent editBookmark = new Intent(context, AddBookmark.class);
		editBookmark.setAction(Intent.ACTION_EDIT);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("bookmarks");
		data.appendEncodedPath(Integer.toString(b.getId()));
		editBookmark.setData(data.build());
		
		return editBookmark;
	}
	
	public static Intent ViewBookmarks(String tag, String account, Context context) {
		Intent i = new Intent(context, BrowseBookmarks.class);
		i.setAction(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("bookmarks");
		
		if(tag != null && !tag.equals(""))
			data.appendQueryParameter("tagname", tag);
		
		i.setData(data.build());
		
		return i;
	}
	
	public static Intent ViewNotes(String tag, String account, Context context) {
		Intent i = new Intent(context, BrowseNotes.class);
		i.setAction(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("notes");
		
		i.setData(data.build());
		
		return i;
	}
	
	public static Intent ViewUnread(String account, Context context) {
		Intent i = new Intent(context, BrowseBookmarks.class);
		i.setAction(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("bookmarks");
		data.appendQueryParameter("unread", "1");
		i.setData(data.build());
		
		return i;
	}
	
	public static Intent ViewTags(String account, Context context) {
		Intent i = new Intent(context, BrowseTags.class);
		i.setAction(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("tags");
		i.setData(data.build());
		
		return i;
	}
	
	public static Intent ViewTabletTags(String account, Context context) {
		Intent i = new Intent(context, BrowseBookmarks.class);
		i.setAction(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority(account + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("tags");
		i.setData(data.build());
		
		return i;
	}
	
	public static Intent SearchBookmarks(String query, String account, Context context) {
		Intent i = new Intent(context, BrowseBookmarks.class);
		i.setAction(Intent.ACTION_SEARCH);
		i.putExtra(SearchManager.QUERY, query);
		i.putExtra("MainSearchResults", "1");
		return i;
	}
	
	public static Intent SearchTags(String query, String account, Context context) {
		Intent i = new Intent(context, BrowseTags.class);
		i.setAction(Intent.ACTION_SEARCH);
		i.putExtra(SearchManager.QUERY, query);
		i.putExtra("MainSearchResults", "1");
		return i;
	}
	
	public static Intent SearchNotes(String query, String account, Context context) {
		Intent i = new Intent(context, BrowseNotes.class);
		i.setAction(Intent.ACTION_SEARCH);
		i.putExtra(SearchManager.QUERY, query);
		i.putExtra("MainSearchResults", "1");
		return i;
	}
	
	public static Intent SearchGlobalTags(String query, String account, Context context) {
		Intent i = new Intent(context, BrowseBookmarks.class);
		i.setAction(Intent.ACTION_SEARCH);
		i.putExtra(SearchManager.QUERY, query);
		i.putExtra("MainSearchResults", "1");
		
		Uri.Builder data = new Uri.Builder();
		data.scheme(Constants.CONTENT_SCHEME);
		data.encodedAuthority("global" + "@" + BookmarkContentProvider.AUTHORITY);
		data.appendEncodedPath("bookmarks");
		i.setData(data.build());
		
		return i;
	}
}
