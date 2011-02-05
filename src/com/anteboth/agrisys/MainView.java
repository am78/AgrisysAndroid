package com.anteboth.agrisys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

/**
 * The main view.
 * 
 * @author michael
 */
public class MainView extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_view);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new MainViewGridAdapter(this));
	    
	}
}