package com.anteboth.agrisys;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.data.AgrisysDataManager;

/**
 * Displays the flurstueck list entries.
 * 
 * @author michael
 */
public class FlurstueckListView extends ListActivity{

	/** The progress dialog.*/
	private ProgressDialog progressDialog = null; 
	/** Holds the list data. */
	private ArrayList<SchlagErntejahr> listData = null;
	/** The list data adapter. */
	private ListDataAdapter listAdapter;
	/** The data loading runnable. */
	private Runnable dataLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_list_view);
		listData = new ArrayList<SchlagErntejahr>();
		this.listAdapter = new ListDataAdapter(
				this, R.layout.main_item_two_line_row, listData);
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
			getString(R.string.please_wait), getString(R.string.loading_data), true);
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		SchlagErntejahr f = this.listData.get(position);
		if (f != null) {
			SharedPreferences pref = getSharedPreferences(
					"Agrisys", Context.MODE_WORLD_WRITEABLE);
			Editor editor = pref.edit();
			editor.putLong("schlagernteJahrID", f.getId());
			editor.putString("schlagName", f.getName());
			editor.commit();
		}
		
		Context ctx = v.getContext();
		Intent intent = new Intent(ctx, AktivitaetListView.class);
		ctx.startActivity(intent);
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
			List<SchlagErntejahr> data = 
				AgrisysDataManager.getInstance().getCachedData().getFlurstueckList();
			listData = new ArrayList<SchlagErntejahr>(data);
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
	private class ListDataAdapter extends ArrayAdapter<SchlagErntejahr> {
		private ArrayList<SchlagErntejahr> items;

		public ListDataAdapter(Context context, int textViewResourceId, ArrayList<SchlagErntejahr> items) {
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
			
			SchlagErntejahr o = items.get(position);
			if (o != null) {
				TextView tvName = (TextView) v.findViewById(R.id.toptext);
				TextView tvFlaeche = (TextView) v.findViewById(R.id.bottomtext);
				
				if (tvName != null) {
					tvName.setText(o.getName());                            }
				if(tvFlaeche != null){
					tvFlaeche.setText(o.getFlaeche() + " ha");
				}
			}
			return v;
		}
	}

}