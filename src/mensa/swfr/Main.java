package mensa.swfr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnClickListener {
    int id = 0; //benötigt für Dialog
	TextView mensa_actual, time;
	String mensa, openings, position, positioncode;
	int item = 0;
	ReadURL read;
	int mensai;
	String[] str = new String[2];
	int mensa_alt=8;
	private ProgressDialog prog; 
	


       	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //ruft Methode auf, die in ReadURL schaut ob was da ist und mensa_id holt
        mensa = getData();
       
        //Wenn eine Mensa_id vorhanden, gib sie der mensaauswahl()
        //sonst öffne Metjode showDialog(Liste mit Mensen) --> onCreateDialog(int id)
        mensa_actual=(TextView) findViewById(R.id.mensa_actual);
        if(mensa == "" || mensa=="NaN") showDialog(id);
        else   {
        	mensai = Integer.parseInt(mensa);
        	mensaauswahl(mensai);
        	checkDate();
        }

        //zu Speiseplan (Diet) --> ruft onClick(View v) auf
        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        
        //Öffnungszeiten - pop-up
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        
        //Lage - pop-up
        final Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        
        //Über - pop-up
        final Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
               
        //beenden
        final Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);   
    }
    
    //überprüft Gültigkeit der Daten
    private void checkDate() {
    	if(mensa != "" || mensa !="NaN"){
	    	 int index1 = ReadURL.inhalt[2].indexOf("Montag");
	         String mo = ReadURL.inhalt[2].substring(index1+8, index1+18);
	         int index2 = ReadURL.inhalt[4].indexOf("Samstag, ");
	         String sa = ReadURL.inhalt[4].substring(index2+8, index2+19);
	         
	  		TextView time = (TextView) findViewById(R.id.time);
	 		time.setText("Gültig von "+mo+" bis "+sa);
	 		TextView update =(TextView) findViewById(R.id.refresh);
	 		if(ReadURL.r == 1 ){
	 			
	 			update.setText("Bitte aktualisieren Sie den Speiseplan.");
	 		}	
	 		else update.setVisibility(0);
	 	}
	}

	//liest mensa_id (= str[0]) und Woche (= str[1]) aus
    private String getData(){
		read = new ReadURL();
    	str=read.getData(getApplicationContext());
       	return str[0]; 
	}
   
    //durch setOnClickListener(this) aufgerufen
    //legt Funktionen der Buttons fest
	@Override
    public void onClick(View v){
		Intent submit;
		Dialog dialog = new Dialog(Main.this);		
		dialog.setContentView(R.layout.hours);	
		TextView text = (TextView) dialog.findViewById(R.id.text);
		
		switch(v.getId()){
			case R.id.button1:
				if(mensa == "" || mensa =="NaN"){}
				else{
					Time today = new Time();
					today.setToNow();
					//today.weekDay: 0 = so, 1 = mo...
					int x = today.weekDay-1;
					if(x==-1)x=0;
					//nachher: 0 = so, 0 = mo, 1 = di, 2 = mi, 3 = do, 4 = fr, 5 = sa
					submit = new Intent(this, Diet.class);
					submit.putExtra("Woche", ""+str[1]);
					submit.putExtra("Tag", ""+x);
					startActivity(submit);
					}
				break;								
			case R.id.button2:	
				dialog.setTitle("Öffnungszeiten");
				text.setText(openings);
				dialog.show();
				dialog.setCanceledOnTouchOutside(true);
				break;						
			case R.id.button3:	
				dialog.setContentView(R.layout.navigation);
                dialog.setTitle("Lage der Mensa");
                dialog.setCancelable(true);
                
                TextView text01 = (TextView) dialog.findViewById(R.id.textView01);
                text01.setText(position);              
 
                Button button = (Button) dialog.findViewById(R.id.button01);
                button.setOnClickListener(this);    
		
		       	dialog.show();
				dialog.setCanceledOnTouchOutside(true);
				break;								
			case R.id.button4:	
				finish();
				break;		
			case R.id.button5:	
				if(isOnline()) update(mensai);
            	else Toast.makeText(getApplicationContext(), "Kein Internetzugriff möglich.",Toast.LENGTH_SHORT).show();
				break;
			case R.id.button01:	
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + positioncode + "&mode=w"));
	            startActivity(intent);
	            break;
			default:
				break;
		}
		
	}
   
	//Bei Klick auf Einstellungs-Button: Menü einblenden
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    
	//Auswahlmöglichkeiten des eingeblendeten Menüs
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
            case R.id.contact:
        		Dialog dialog = new Dialog(Main.this);		
        		dialog.setContentView(R.layout.hours);	
        		TextView text = (TextView) dialog.findViewById(R.id.text);
        		dialog.setTitle("Über");
        		text.setText( Html.fromHtml(
    				"Version: 1.0<br><br>Vorschläge, Probleme oder Anregungen:<br><a href=\"mailto:android.gabimanz@gmail.com\">Android.GabiManz@gmail.com</a>"));
    			text.setMovementMethod(LinkMovementMethod.getInstance());
    			dialog.show();
    			dialog.setCanceledOnTouchOutside(true);	
            	break;
            case R.id.choose_mensa:	
            	showDialog(id);
				break;
        }
        return true;
    }
    //mit showDialog aufgerufen, zeigt Liste mit Mensen, switch ermöglicht späteres erweitern
    protected Dialog onCreateDialog(int id) {
    	
    	switch (id) {
	    	case 0:	CharSequence[] items = 	{"FR-Rempartstraße", "FR-Institutsviertel", "FR-Littenweiler",
	    					"FR-Flugplatz", "FR-Musikhochschule", "FR- Ausgabestelle EFH", "Furtwangen", "Schwenningen", "Offenburg (und Gengenbach)",
	    					"Kehl", "Trossingen"};
	  		    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
			        builder.setTitle("Wählen Sie Ihre Mensa");
			        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int it) {
			            	item = it;
			           }});
			        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int id) {
			                    	//übergibt mensa_id=item an update (speichert URL)
			                    	//und Mensaauswahl (zeigt Mensaname in Main)     
			                    	dialog.cancel();  
			                    	if(isOnline()) update(item);
			                    	else Toast.makeText(getApplicationContext(), "Kein Internetzugriff möglich.",Toast.LENGTH_SHORT).show();
			                    	
			                   }
			               });
			        builder.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int id) {
					            	dialog.cancel();                   	  
					            }	
			        });
			        
			        AlertDialog alert = builder.create();
					return alert;
    	}
		return null;
    }
    
    public boolean isOnline()
	{
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo netInfo = cm.getActiveNetworkInfo();
    	if (netInfo != null && netInfo.isConnectedOrConnecting())
    	{
    		return true;
    	}
    	return false;
	}
    
    //ruft Methode parser() in SaveURL auf (speichert String)
    protected void update(final int item2) {
    	
    	prog = ProgressDialog.show(Main.this, "", "Bitte warten.\nDie Daten werden abgerufen...", true, true);
    	final SaveURL url = new SaveURL();
   
    	new Thread(){
    		public void run(){
    			Looper.prepare();
    			url.parser(item2, getApplicationContext());	//Daten auslesen & speichern
    			prog.dismiss();
    			finish();
    			startActivity(getIntent());
    			overridePendingTransition(0, 0);
    	}}.start();  	
    }
    
	//zeigt Mensa_name, Lage und Öffnungszeiten in Main an
    public void mensaauswahl(int which){
    	switch(which){
    		case 0:	
    			mensa = "FR-Rempartstraße";
    			openings = "Mittagessen:\nMo - Fr 11:30 – 14:00 Uhr\nSa 11:30 - 13:30 Uhr\n\nAbendessen:\nMo - Fr 17:30 - 19:30 Uhr\n\nBiergarten (SS):\nMo - Sa 11:30 - 22:00 Uhr ";
    			position = "Mensa Rempartstraße\nRempartstraße 18\n79098 Freiburg";
    			positioncode = "Rempartstraße 18+79098 Freiburg";
    			break;
    		case 1:
    			mensa = "FR-Institutsviertel";
	    		openings = "Mittagessen: Mo - Fr 11:30 bis 14:00 Uhr";
	    		position = "Mensa Institutsviertel\nStefan-Meier-Str. 28\n79104 Freiburg";
	    		positioncode = "Stefan-Meier-Str. 28+79104 Freiburg";
	    		break;
    		case 2:
    			mensa = "FR-Littenweiler";
    			openings = "Mo - Do 9:00 - 15:30 Uhr\nFr 9:00 - 15:00 Uhr\n\nMittagessen:\nMo - Fr 11:30 - 14:00 Uhr\n\nPasta- und Salatbuffet bis 14:45 Uhr";
    			position = "Mensa Littenweiler\nKunzenweg 29\n79117 Freiburg";
    			positioncode = "Kunzenweg 29+79117 Freiburg";
    			break;
    		case 3:
    			mensa = "FR-Flugplatz";
    			openings = "Cafeteria:\nMo - Do 9:00 - 15:00 Uhr\nFr 9:00 - 14:30 Uhr\n\n Mittagessen: Mo - Fr 11:45 - 13:30 Uhr";
    			position = "Mensa Flugplatz\nGeorges-Köhler-Allee 82\nGebäude 082\n79110 Freiburg";
    			positioncode = "Georges-Köhler-Allee 82+79110 Freiburg";
    			break;
    		case 4:
    			mensa = "FR-Musikhochschule";
    			openings = "Mo - Fr 9:00 - 18:00 Uhr\n\n Mittagessen: Mo - Fr 11:30 - 13:30 Uhr";
    			position = "Mensa Flugplatz\nSchwarzwaldstrasse 141\n79102 Freiburg";
    			positioncode = "Schwarzwaldstrasse 141+79102 Freiburg";
    			break;
    		case 5:
    			mensa = "FR-Ausgabestelle EFH";
    			openings = "nicht bekannt";
    			position = "Ausgabestelle Evang. Fachhochschule\n\nBugginger Str. 38\n79114 Freiburg im Breisgau";
    			positioncode = "Bugginger Str. 38+79114 Freiburg";
    			break;	
    		case 6:
    			mensa = "Furtwangen";
    			openings = "Mensa: Mo - Fr 11:25 - 13:40 Uhr\n\nCafeteria:\nMo - Do: 8.00 - 18:00 Uhr\n(WS) Mo - Do 8:00 - 17:00 Uhr\nFr 8:00 - 16:00 Uhr";
    			position = "Mensa Furtwangen\nGerwigstrasse 15\n78120 Furtwangen";
    			positioncode = "Gerwigstrasse 15+78120 Furtwangen";
    			break;
    		case 7:
    			mensa = "Schwenningen";
    			openings = "Mensa: Mo - Fr 11:30 -14:00 Uhr\n\nCafeteria:\nMo - Do 8:00 - 19:30 Uhr\nFr 8:00 - 15:00 Uhr";
    			position = "Mensa Schwenningen\nKarlstr. 19\n78054 Schwenningen";
    			positioncode = "Karlstr. 19+78054 Schwenningen";
    			break;
    		case 8:
    			mensa = "Offenburg";
    			openings = "Mensa: Mo - Fr 11:00-13:45 Uhr\n\nCafeteria: Mo - Fr 7:30-15:30 Uhr ";
    			position = "Mensa Offenburg\nBadstrasse 24\n77652 Offenburg";
    			positioncode = "Badstrasse 24+77652 Offenburg";
    			break;
    		case 9:
    			mensa = "Kehl";
    			openings = "Mensa: Mo - Fr 11:30 - 13:30 Uhr\n\nCafeteria: Mo - Fr 7:30 - 14:00 Uhr. ";
    			position = "Mensa Kehl\nKinzigallee 1\n77694 Kehl";
    			positioncode = "Kinzigallee 1+77694 Kehl";
    			break;
    		case 10:
    			mensa = "Trossingen";
    			openings = "Mensa: Mo - Fr 11:30 - 13:30 Uhr\n\nCafeteria: Mo - Do 8:30 - 15:30 Uhr\nFr 8:30 - 15:00 Uhr. ";
    			position = "Mensa Trossingen\nSchultheiß-Koch-Platz 3\n78347 Trossingen";
    			positioncode = "Schultheiß-Koch-Platz 3+78347 Trossingen";
    			break;
    		default: mensa = "NaN";
    			break;
    	}
    	mensa_actual.setText(mensa);
    }
}