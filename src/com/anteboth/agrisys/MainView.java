package com.anteboth.agrisys;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.anteboth.agrisys.client.model.AgrisysData;
import com.anteboth.agrisys.data.AgrisysDataManager;

/**
 * The main view.
 * 
 * @author michael
 */
public class MainView extends Activity {
	
	private Runnable dataLoader;
	private ProgressDialog progressDialog = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_view);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new MainViewGridAdapter(this));

	    setTitle(R.string.app_name);
	    
	    //when starting the app first load the agrisysData
	    //try to load the stored data which is stored in the file system
	    AgrisysDataManager adm = AgrisysDataManager.getInstance();
	    try {
			adm.loadAgrisysDataFromFileSystem(this);
		} catch (SystemException e) {
			Log.e("MainView", e.getMessage());
		}
	    
		if (adm.getCachedData() == null || adm.getCachedData().isEmpty()) {
			//if it's not present load the data from the server using the synchronize method
			performSynchronizeData();
		}
	}
	
	@Override
	protected void onDestroy() {
		//the main view will be destroyed
		//so it's time to save the cached data to the file system
		AgrisysDataManager adm = AgrisysDataManager.getInstance();
		try {
			adm.storeAgrisysDataToFileSystem(this);
		} catch (SystemException e) {
			Log.e("MainView", e.getMessage());
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//add reload menu item
		MenuItem item = menu.add(0, 1, 0, getString(R.string.sync_data));
		item.setIcon(R.drawable.ic_menu_refresh);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			switch (item.getItemId()) {
				//reload data if menu item pressed
				case 1:
					performSynchronizeData();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Starts the synchronization of the data.
	 */
	private void performSynchronizeData() {
		//create runnable to sync the data		
		dataLoader = new Runnable(){
			@Override
			public void run() {
				synchronizeData();
			}
		};
		
		//create and start the thread to sync the data
		Thread thread =  new Thread(null, dataLoader, "MagentoBackground");
		thread.start();

		//show a progress monitor while loading the data 
		progressDialog = ProgressDialog.show(
				MainView.this, getString(R.string.please_wait), 
				getString(R.string.retrieving_data), true);
	}
	
	/**
	 * Synchronizes the data.
	 */
	private void synchronizeData(){
		try{
			//perform the synchronize operation
			AgrisysDataManager.getInstance().synchronizeData(this);
		} catch (Exception e) { 
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		
		//after loading the data, display the entries
		runOnUiThread(dataRefresher);
	}
	
	/**
	 * Refresh the list data after the entries has been loaded.
	 */
	private Runnable dataRefresher = new Runnable() {
		@Override
		public void run() {
			//TODO
			//hide progress dialog
			progressDialog.dismiss();
		}
	};
}