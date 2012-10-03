package mensa.swfr;

import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.Toast;

public class SaveURL extends Activity{
	String stringkomplett;
	/** Called when the activity is first created. */
	@Override
 	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
	
	//ruft Methode getURL() in GetURL auf & speichert String
	public void parser(int mensa_item, Context context){
	    //ruft GetURL auf
	    GetURL get = new GetURL();
        try {
			stringkomplett = get.getURL(mensa_item, context);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        //fügt den Montag der aktuellen Woche als "Aktualisierungshilfe" hinzu
        Time monday = new Time();
        monday.setToNow();
        int x= monday.weekDay;
        if(monday.weekDay==0)x=7;
        monday.set(monday.monthDay - x+1, monday.month, monday.year);
        monday.normalize(true);
        
        stringkomplett = monday + stringkomplett;        
        //stringkomplett=stringkomplett.replaceAll("ß", "ss");    
        
        //speichert stringkomplett
        FileOutputStream fOut = null;   
        try{
         fOut = context.openFileOutput("settings.dat",MODE_PRIVATE);      
         fOut.write(stringkomplett.getBytes());
         Toast.makeText(context, "Speiseplan wurde gespeichert",Toast.LENGTH_SHORT).show();
         fOut.close();
       }
         catch (Exception e) {      
         e.printStackTrace();
         Toast.makeText(context, "Speiseplan konnte nicht gespeichert werden",Toast.LENGTH_SHORT).show();
         }
    }       
}
