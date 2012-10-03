package mensa.swfr;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Diet extends Activity implements OnClickListener  {
    String[] inhaltsstoffe={"0 \n","1 -mit Schwein\n","2 -mit Alkohol\n"
			,"3 -mit Geschmacksverstärker\n","4 -mit Farbstoff\n","5 -mit Antioxidationsmittel\n","6 -mit Konservierungsstoff\n"
			,"7 -geschwefelt\n","8 -mit Phosphat\n","9 -mit Süßungsmittel\n","10 -geschwärzt\n"
			,"11 -gewachst\n","12 -Formfleisch\n","13 \n"
			,"14 \n","15 -mit Rindfleisch\n","16 -Vorderschinken\n"
			,"17 \n","18 \n","MSC -aus nachhaltiger Fischerei\n"};
    public String[] header = new String[8];
    public String[] date = new String[6];
    public String[][] meal = new String[6][8];
    int week, i;    
    int curPos = 0;
	StringBuilder text = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.diet);
        
        try{
        //erfährt die angezeigte Woche
    	Intent intent = getIntent();
		String Woche = intent.getStringExtra("Woche");
		week = Integer.parseInt(Woche);
		String Wochentag = intent.getStringExtra("Tag");
		i = Integer.parseInt(Wochentag);
		
        text = new StringBuilder();
        text.append(ReadURL.inhalt[week+2]); }
        catch(Exception e){
        	Intent submit;
        	submit = new Intent(this, Main.class);
        	startActivity(submit);
        }

        //Woche x --> nicht benötigt
        nextTagContent("td");//Woche 29
        for(int i = 0; i<=7; i++){
        	//6 Spalten... Überschriften
        	header[i] = nextTagContent("td");
        } 
        for(int b = 0;b<=5;b++){
        	//6 Zeilen... Wochentage
        	date[b]=nextTagContent("td");
        	for(int z = 0; z<=7; z++){
        		//6 Zeilen mit je 6 Gerichten
        		meal[b][z]=nextTagContent("td");
        		meal[b][z]=meal[b][z].replaceAll("<br>", "\n");
        	}
        } 
        
        showContent(i, week);       
    }
   
    private void showContent(int i, int week) {
	    	final ImageButton button1 = (ImageButton) findViewById(R.id.zuruck);
	    	button1.setOnClickListener(this);
	    	if(week!=0)         button1.setVisibility(0);
	    	else  button1.setVisibility(4);
    	
            final ToggleButton button2 = (ToggleButton) findViewById(R.id.mo);
            button2.setOnClickListener(this);
            
            final ToggleButton button3 = (ToggleButton) findViewById(R.id.di);
            button3.setOnClickListener(this);
                   
            final ToggleButton button4 = (ToggleButton) findViewById(R.id.mi);
            button4.setOnClickListener(this);
            
            final ToggleButton button5 = (ToggleButton) findViewById(R.id.don);
            button5.setOnClickListener(this);
            
            final ToggleButton button6 = (ToggleButton) findViewById(R.id.fr);
            button6.setOnClickListener(this);
            
            final ToggleButton button7 = (ToggleButton) findViewById(R.id.sa);
            button7.setOnClickListener(this);
            
            button2.setChecked(false);
	       	button3.setChecked(false);
	       	button4.setChecked(false);
	       	button5.setChecked(false);
	       	button6.setChecked(false);
	       	button7.setChecked(false);
            
            switch(i){
            	case 0:  button2.setChecked(true);
		            	 break;     	
            	case 1:  button3.setChecked(true);
            			 break;
            	case 2:  button4.setChecked(true);
		    			 break;
            	case 3:  button5.setChecked(true);
		    			 break;
            	case 4:  button6.setChecked(true);
		    			 break;
            	case 5:  button7.setChecked(true);
		    			 break;
            }
            
            final ImageButton button8 = (ImageButton) findViewById(R.id.vor);
            button8.setOnClickListener(this);
            
            if(week!=2)         button8.setVisibility(0);
	    	else  button8.setVisibility(4);
                              
            TextView[] headerText = new TextView[8];
            TextView[] mealText = new TextView[8];          
           
            
   			TextView dateText = (TextView) findViewById(R.id.date);  
    		dateText.setText(date[i]);//leer		
    		headerText[0] = (TextView) findViewById(R.id.header0);  
    		mealText[0] = (TextView) findViewById(R.id.meal0);
    		headerText[1] = (TextView) findViewById(R.id.header1);
    		mealText[1] = (TextView) findViewById(R.id.meal1);
    		headerText[2] = (TextView) findViewById(R.id.header2);
    		mealText[2] = (TextView) findViewById(R.id.meal2);
    		headerText[3] = (TextView) findViewById(R.id.header3);
    		mealText[3] = (TextView) findViewById(R.id.meal3);    		
    		headerText[4] = (TextView) findViewById(R.id.header4);    		
    		mealText[4] = (TextView) findViewById(R.id.meal4);
    		headerText[5] = (TextView) findViewById(R.id.header5);    		
    		mealText[5]= (TextView) findViewById(R.id.meal5);
    		headerText[6] = (TextView) findViewById(R.id.header5);  //? 		
    		mealText[6]= (TextView) findViewById(R.id.meal5);		//? 
    		headerText[7] = (TextView) findViewById(R.id.header5);  //? 	
    		mealText[7]= (TextView) findViewById(R.id.meal5);		//? 
    		
    		for(int a = 0; a<=7; a++){
    			 if(header[a]!=""){
                	headerText[a].setText(header[a]);
                	mealText[a].setTypeface(null, Typeface.ITALIC);
                 	mealText[a].setText(meal[i][a]);
                 	Log.i("MensaMeal",meal[i][a]);
	              	headerText[a].setVisibility(0);
	             	mealText[a].setVisibility(0);
    			 }
             }
	}

	//Eigenschaften button. ruft  onOptionsItemSelected(MenuItem item) auf
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_diet, menu);
        return true;
    }
	
	@Override
	public void onBackPressed() {
		finish();
		return;
	}
    
	//Ruft info() auf
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           info();
           return true;
    }
    
    //zeigt Inhaltsstoffe an
    public void info()
	{
		StringBuilder info=new StringBuilder();
		for (int i = 1; i < 20; i++) 
		{
			if (inhaltsstoffe[i].length()>6)
				info.append(inhaltsstoffe[i]);
		}
		Toast.makeText(this, info.toString(), Toast.LENGTH_LONG).show();
	}
	
    //StringBuilder text aus ReadURL wird nach tag durchsucht
	public String nextTagContent(String tag)
	{
		int start = nextStartTag(tag, curPos)+tag.length()+2;
		curPos=start;
		int end = nextEndTag(tag, start);
		if (start>=0 && end>start)
			return text.substring(start, end);
		return "";
	}
	
	private int nextStartTag(String tag, int pos)
	{
		return text.indexOf("<"+tag+">", pos);
	}
	
	private int nextEndTag(String tag, int pos)
	{
		return text.indexOf("</"+tag+">", pos);
	}
	
	@Override
    public void onClick(View v){
		Intent submit = new Intent(this, Diet.class);
		switch(v.getId()){
			case R.id.zuruck:	
				week--;
				//showContent(5, week);
				submit.putExtra("Woche", ""+week);
				submit.putExtra("Tag", "5");
				finish();
				startActivity(submit);
				overridePendingTransition(0, 0);
				break;								
			case R.id.mo:
				showContent(0, week);
				break;						
			case R.id.di:	
				showContent(1, week);
				break;								
			case R.id.mi:	
				showContent(2, week);
				break;
			case R.id.don:	
				showContent(3, week);
				break;
			case R.id.fr:	
				showContent(4, week);
				break;
			case R.id.sa:	
				showContent(5, week);
				break;
			case R.id.vor:
				week++;
				//showContent(0, week);
				submit.putExtra("Woche", ""+week);
				submit.putExtra("Tag", "0");
				finish();
				startActivity(submit);
				overridePendingTransition(0, 0);
				break;	
		}
	}
}