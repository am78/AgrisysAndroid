package com.anteboth.agrisys;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * The grid view adpater for the main view.
 * Gets the ImageButtons.
 */
public class MainViewGridAdapter extends BaseAdapter {
    
	private static final int SAAT_IDX = 0;
	private static final int BODEN_IDX = 1;
	private static final int DUENGUNG_IDX = 2;
	private static final int PS_IDX = 3;
	private static final int ERNTE_IDX = 4;
	private static final int SCHLAG_LIST_IDX = 5;
	
	/** Reference to the context. */
	private Context ctx;
	
	/** references to our images */
    private Integer[] mThumbIds = null;

    /**
     * @param c
     */
    public MainViewGridAdapter(Context c) {
        ctx = c;
        //init image array
        mThumbIds = new Integer[6];
        mThumbIds[SAAT_IDX] = R.drawable.aussaat;
        mThumbIds[BODEN_IDX] = R.drawable.boden;
        mThumbIds[DUENGUNG_IDX] = R.drawable.duengung;
        mThumbIds[PS_IDX] = R.drawable.pflanzenschutz;
        mThumbIds[ERNTE_IDX] = R.drawable.ernte;
        mThumbIds[SCHLAG_LIST_IDX] = R.drawable.list_icon;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    public int getCount() {
        return mThumbIds.length;
    }
    
    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    public Object getItem(int position) {
        return null;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    public long getItemId(int position) {
        return 0;
    }

    /** 
     * create a new ImageView for each item referenced by the Adapter
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageButton b;
    	if (convertView == null) {
    		b = new ImageButton(ctx);
    		b.setLayoutParams(new GridView.LayoutParams(200, 200));
    		b.setScaleType(ImageView.ScaleType.FIT_CENTER);
    		b.setPadding(12, 12, 12, 12);
    		b.setId(position);
    		b.setOnClickListener(new OnClickListener() {
    		    public void onClick(View v) {
    		        onButtonClicked((ImageButton) v);
    		    }
    		});
    	} else {
    		b = (ImageButton) convertView;
    	}
    	b.setImageResource(mThumbIds[position]);
    	return b;
    }

    /**
     * Perform the button click action.
     * @param v the clicked button.
     */
    protected void onButtonClicked(ImageButton v) {
    	Log.i("MainViewGridAdapter", v.getId() + " clicked.");
    	Intent intent = null;

    	switch (v.getId()) {
    		case SCHLAG_LIST_IDX:
    			intent = new Intent(ctx, FlurstueckListView.class);
    			break;
    		case SAAT_IDX:
    			intent = new Intent(ctx, AussaatView.class);
    			break;
    		case BODEN_IDX:
    			break;
    		case DUENGUNG_IDX:
    			break;
    		case ERNTE_IDX:
    			break;
    		case PS_IDX:
    			break;
    	}
    	
    	if (intent != null) {
    		ctx.startActivity(intent);
    	}
	}
}