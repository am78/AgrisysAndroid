package com.anteboth.agrisys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.anteboth.agrisys.client.model.AgrisysData;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.data.AgrisysDataManager;

public class AussaatDetailsView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aussaat_details_view);
		
		SharedPreferences pref = getSharedPreferences("Agrisys", Context.MODE_WORLD_WRITEABLE);
		String schlagName = pref.getString("schlagName", "");
		String aktivitaetName = "";

		long schlagErntejahrID = pref.getLong("schlagernteJahrID", -1);
		long aktivitaetId = pref.getLong("aktivitaetId", -1);

		AgrisysData ad = AgrisysDataManager.getInstance().getCachedData();
		Aktivitaet akt = ad.getAktivitaet(schlagErntejahrID, aktivitaetId);
		if (akt != null) {
			aktivitaetName = AgrisysHelper.formatDate(akt.getDatum());
		}
		//set the title
		setTitle(getString(R.string.app_name) + "/" + schlagName + "/" + aktivitaetName);

		
		List<Kultur> kl = ad.getStammdaten().getKulturList();
		List<String> kulturStrings = new ArrayList<String>();
		for (Kultur kultur : kl) {
			kulturStrings.add(kultur.getName());
		}
		
		

		DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
		AgrisysHelper.updateDate(datePicker, akt.getDatum());
		
		Spinner spinnerKultur = (Spinner) findViewById(R.id.spinnerKultur);
	    ArrayAdapter<String> adapterKultur = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, kulturStrings);
	    adapterKultur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinnerKultur.setAdapter(adapterKultur);
	    
	    //TODO listen for kultur selection changes and adjust sorte values
	    Spinner spinnerSorte = (Spinner) findViewById(R.id.spinnerSorte);
	    ArrayAdapter<String> adapterSorte = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item, new String[] {   
                  "Weizen A", "Weizen B", "Weizen C"});
	    adapterSorte.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinnerSorte.setAdapter(adapterSorte);
	    
	    //TODO set Kultur spinner selected value
	}
	
}
