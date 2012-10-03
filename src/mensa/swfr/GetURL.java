package mensa.swfr;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class GetURL extends Activity {
	/** Declare attributes. */
	private String string;
	int which;
	URL[] url = new URL[3];
	int id = 0;
	ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}

	//holt URL und liest sich über Parser() aus, return: string
	public String getURL(int w, Context context) throws IOException {
		which = w;
		try {
			switch(which) { 
			case 0:	id = 610;	//FR-Rempartstraße
				break;
			case 1: id = 620;	//FR-Institutsviertel
				break;
			case 2: id = 630;	//FR-Littenweiler
				break;
			case 3:id = 681;	//FR-FLugplatz
				break;
			case 4: id = 722;	//FR-Musikhochschule
				break;
			case 5:id = 685;	//FR-Ausgabestelle EFH
				break;
			case 6: id = 641;	//Furtwangen
				break;
			case 7:id = 671;	//Schwenningen
				break;
			case 8:id = 651;	//Offenburg
				break;
			case 9:id = 661;	//Kehl
				break;
			case 10:id = 672;	//Trossingen
				break;
			default:  //Fehler melden
				break;
			}
			
			url[0] = new URL("http://www.swfr.de/essen-trinken/speiseplaene/speiseplan-wochenansicht/?no_cache=1&Woche=0&Ort_ID="+id+"&print=1");
			url[1] = new URL("http://www.swfr.de/essen-trinken/speiseplaene/speiseplan-wochenansicht/?no_cache=1&Woche=1&Ort_ID="+id+"&print=1");
			url[2] = new URL("http://www.swfr.de/essen-trinken/speiseplaene/speiseplan-wochenansicht/?no_cache=1&Woche=2&Ort_ID="+id+"&print=1");

		}
		catch (Exception e) {
			System.err.println("IOException while downloading url.");
		}
		string = parser(which, url);
		return string;   
	}

	//liest URL aus --> d kommt aus anderer Version...
	public String parser(int which, URL[] url) throws IOException	{
		String[] text = new String[3];
		for(int i = 0; i<=2;i++){

			URLConnection conn = url[i].openConnection();

			DataInputStream in = new DataInputStream ( conn.getInputStream (  )  ) ;
			BufferedReader d = new BufferedReader(new InputStreamReader(in, "utf-8"));//?
			String inLine;
			while ((inLine = d.readLine()) != null)	{
				text[i] += inLine;
			}
		}
		String string = "!!!"+which+"!!!"+text[0]+"!!!"+text[1]+"!!!"+text[2];
		return string;    
	}
}