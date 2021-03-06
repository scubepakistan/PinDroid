/*
 * PinDroid - http://code.google.com/p/PinDroid/
 *
 * Copyright (C) 2010 Matt Schmidt
 *
 * PinDroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * PinDroid is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PinDroid; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package com.pindroid.activity;

import com.pindroid.R;
import com.pindroid.widget.SmallWidgetProvider;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SmallWidgetConfigure extends ListActivity {
	
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
    private static final String PREFS_NAME = "com.pindroid.widget.SmallWidgetProvider";
    private static final String PREF_PREFIX_KEY = "button_";
	
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);

        // Set the view layout resource to use.
        setContentView(R.layout.small_widget_configure_activity);
        setTitle(R.string.small_widget_configuration_description);
        
		String[] MENU_ITEMS = new String[] {getString(R.string.small_widget_my_bookmarks),
				getString(R.string.small_widget_my_unread),
				getString(R.string.small_widget_my_tags),
				getString(R.string.small_widget_add_bookmark),
				getString(R.string.small_widget_search_bookmarks)};
		

		setListAdapter(new ArrayAdapter<String>(this, R.layout.widget_configure_view, MENU_ITEMS));
		
		ListView lv = getListView();
		
		lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	final Context context = SmallWidgetConfigure.this;
		    	
		    	if(position == 0){
		    		saveButtonPref(context, mAppWidgetId, "bookmark");
		    	} else if(position == 1){
		    		saveButtonPref(context, mAppWidgetId, "unread");
		    	} else if(position == 2){
		    		saveButtonPref(context, mAppWidgetId, "tags");
		    	} else if(position == 3){
		    		saveButtonPref(context, mAppWidgetId, "add");
		    	} else if(position == 4){
		    		saveButtonPref(context, mAppWidgetId, "search");
		    	}

	            // Push widget update to surface with newly set prefix
	            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	            SmallWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

	            // Make sure we pass back the original appWidgetId
	            Intent resultValue = new Intent();
	            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
	            setResult(RESULT_OK, resultValue);
	            finish();
		    }
		});


        // Find the widget id from the intent. 
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }
	
    // Write the prefix to the SharedPreferences object for this widget
    static void saveButtonPref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static String loadButtonPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String button = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (button != null) {
            return button;
        } else {
            return context.getString(R.string.small_widget_button_default);
        }
    }
    
    public static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }
}