package com.anteboth.agrisys;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anteboth.agrisys.data.AgrisysDataManager;
import com.anteboth.agrisys.data.Flurstueck;

/**
 * Displays the flurstueck list entries.
 * 
 * @author michael
 */
public class FlurstueckListView extends ListActivity{

	/** Reload menu item id. */
	private static final int RELOAD_DATA = 0;
	/** The progress dialog.*/
	private ProgressDialog progressDialog = null; 
	/** Holds the list data. */
	private ArrayList<ListItemData> listData = null;
	/** The list data adapter. */
	private ListDataAdapter listAdapter;
	/** The data loading runnable. */
	private Runnable dataLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schlag_list_view);
		listData = new ArrayList<ListItemData>();
		this.listAdapter = new ListDataAdapter(this, R.layout.main_item_two_line_row, listData);
		setListAdapter(this.listAdapter);

		loadData();
	}

	/**
	 * Loads the data entries.
	 */
	private void loadData() {
		//create runnable to load the data		
		dataLoader = new Runnable(){
			@Override
			public void run() {
				getData();
			}
		};
		
		//create and start the thread to load the data
		Thread thread =  new Thread(null, dataLoader, "MagentoBackground");
		thread.start();

		//show a progress monitor while loading the data 
		progressDialog = ProgressDialog.show(FlurstueckListView.this,
				"Bitte warten...", "Daten werden geladen...", true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//add reload menu item
		MenuItem item = menu.add(0, RELOAD_DATA, 0, "Neu laden");
		item.setIcon(R.drawable.ic_menu_refresh);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item != null) {
			switch (item.getItemId()) {
				//reload data if menu item pressed
				case RELOAD_DATA:
					loadData();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Refresh the list data after the entries has been loaded.
	 */
	private Runnable dataRefresher = new Runnable() {
		@Override
		public void run() {
			if(listData != null && listData.size() > 0){				
				listAdapter.clear(); //clear old items in adapter 
				//add loaded data items to the adapter
				for(int i=0;i<listData.size();i++) {
					listAdapter.add(listData.get(i));
				}
				//inform adapter about data changes
				listAdapter.notifyDataSetChanged();
			}
			//hide progress dialog
			progressDialog.dismiss();
		}
	};
	
	/**
	 * Loads the list data.
	 */
	private void getData(){
		try{
			//get the flurstueck entries
			List<Flurstueck> data = new AgrisysDataManager().loadFlurstueckList(getApplicationContext());
			listData = new ArrayList<ListItemData>();
			if (data != null) {
				for (Flurstueck f : data) {
					ListItemData o = new ListItemData();
					o.setItem0(f.getName());
					o.setItem1(f.getFlaeche() + " ha");
					listData.add(o);
				}
			}
		} catch (Exception e) { 
			Log.e("BACKGROUND_PROC", e.getMessage());
		}
		
		//after loading the data, display the entries
		runOnUiThread(dataRefresher);
	}
	
	/**
	 * Implements the list data adpater for the list view.
	 * @author michael
	 */
	private class ListDataAdapter extends ArrayAdapter<ListItemData> {
		private ArrayList<ListItemData> items;

		public ListDataAdapter(Context context, int textViewResourceId, ArrayList<ListItemData> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.main_item_two_line_row, null);
			}
			
			ListItemData o = items.get(position);
			if (o != null) {
				TextView tvName = (TextView) v.findViewById(R.id.toptext);
				TextView tvFlaeche = (TextView) v.findViewById(R.id.bottomtext);
				
				if (tvName != null) {
					tvName.setText(o.getItem0());                            }
				if(tvFlaeche != null){
					tvFlaeche.setText(o.getItem1());
				}
			}
			return v;
		}
	}
	
	/**
	 * Implements a two line list data item.
	 * @author michael
	 */
	class ListItemData {
	    private String item0;
	    private String item1;
	    
	    public String getItem0() {
			return item0;
		}
		public void setItem0(String item0) {
			this.item0 = item0;
		}
		public String getItem1() {
			return item1;
		}
		public void setItem1(String item1) {
			this.item1 = item1;
		}
	}
}