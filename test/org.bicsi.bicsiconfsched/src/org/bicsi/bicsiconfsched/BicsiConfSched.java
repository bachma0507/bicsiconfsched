package org.bicsi.bicsiconfsched;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

public class BicsiConfSched extends Activity {

 /*private CountriesDbAdapter dbHelper;*/
 private SQLiteDB sqlite_obj;
 List<String> list1, list2, list3, list4;
 private SimpleCursorAdapter dataAdapter;

 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_bicsi_conf_sched);

  /*dbHelper = new CountriesDbAdapter(this);
  dbHelper.open();*/
  
  sqlite_obj = new SQLiteDB(BicsiConfSched.this);
  
  
  GetURL();
  

  
  }
  
  private class LongOperation  extends AsyncTask<String, Void, Void> {
		
  		private final HttpClient Client = new DefaultHttpClient();
          private String Content;
          private String Error = null;
          private ProgressDialog Dialog = new ProgressDialog(BicsiConfSched.this);
          String data =""; 
        
          int sizeData = 0;  
        
         
         
          protected void onPreExecute() {
              // NOTE: You can call UI Element here.
              
              //Start Progress Dialog (Message)
            
              Dialog.setMessage("Please wait..");
              Dialog.show();
             
              try{
                  // Set Request parameter
                  data +="&" + URLEncoder.encode("data", "UTF-8");
                     
              } catch (UnsupportedEncodingException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } 
             
          }
  
          // Call after onPreExecute method
          protected Void doInBackground(String... urls) {
             
              /************ Make Post Call To Web Server ***********/
              BufferedReader reader=null;
    
                   // Send data 
                  try
                  { 
                   
                     // Defined URL  where to send data
                     URL url = new URL(urls[0]);
                      
                    // Send POST data request
        
                    URLConnection conn = url.openConnection(); 
                    conn.setDoOutput(true); 
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
                    wr.write( data ); 
                    wr.flush(); 
               
                    // Get the server response 
                    
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                 
                      // Read Server Response
                      while((line = reader.readLine()) != null)
                          {
                                 // Append server response in string
                                 sb.append(line + " ");
                          }
                     
                      // Append Server Response To Content String 
                     Content = sb.toString();
                  }
                  catch(Exception ex)
                  {
                      Error = ex.getMessage();
                  }
                  finally
                  {
                      try
                      {
          
                          reader.close();
                      }
        
                      catch(Exception ex) {}
                  }
             
              /*****************************************************/
              return null;
          }
          
          protected void onPostExecute(Void unused) {
              // NOTE: You can call UI Element here.
              
              // Close progress dialog
              Dialog.dismiss();
              
              if (Error != null) {
                  
                  System.out.println("Output : "+Error);
                  
              } else {
               
                  // Show Response Json On Screen (activity)
              	System.out.println( Content );
                 
               /****************** Start Parse Response JSON Data *************/
                 
                  String OutputData = "";
                  /*JSONObject jsonResponse;*/
                       
                  try {
                       
                	
                	
                       /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                       /*jsonResponse = new JSONObject(Content);*/
                       
                       /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                       /*******  Returns null otherwise.  *******/
                       /*JSONArray jsonMainNode = jsonResponse.optJSONArray("");*/
                	
                  	JSONArray jsonMainNode = new JSONArray(Content);
                       
                       /*********** Process each JSON Node ************/
   
                       int lengthJsonArr = jsonMainNode.length();  
                     
                     list1 = new ArrayList<String>();
           			 list2 = new ArrayList<String>();
           			 list3 = new ArrayList<String>();
           			 list4 = new ArrayList<String>();
   
                       for(int i=0; i < lengthJsonArr; i++) 
                       {
                           /****** Get Object for each JSON node.***********/
                           JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                           
                           /******* Fetch node values **********/
                           String id       = jsonChildNode.optString("id").toString();
                           String scheduleDate     = jsonChildNode.optString("scheduleDate").toString();
                           String sessionName = jsonChildNode.optString("sessionName").toString();
                           String sessionTime = jsonChildNode.optString("sessionTime").toString();
                         
                           list1.add(jsonChildNode.getString("id"));
                           list2.add(jsonChildNode.getString("scheduleDate"));
                           list3.add(jsonChildNode.getString("sessionName"));
                           list4.add(jsonChildNode.getString("sessionTime"));
                           
                         
                           OutputData += "ID: "+ id +" "
                                       + "ScheduleDate: "+ scheduleDate +" "
                                       + "SessionName: "+ sessionName +" "
                                       + "SessionTime: "+ sessionTime +" "
                                       +"\n";
                         
                           sqlite_obj.open();
                     	
                       	sqlite_obj.deleteAll();
                     	
                       	for(int j=0; j<list1.size(); j++) {
                     		
                       		sqlite_obj.insert(list1.get(j).toString(), list2.get(j).toString(), list3.get(j).toString(), list4.get(j).toString());
                       		
                       		
                       	}
                       	
                       	sqlite_obj.close();
                          
                      }
                   /****************** End Parse Response JSON Data *************/    
                      
                       //Show Parsed Output on screen (activity)
                       /*jsonParsed.setText( OutputData );*/
                       System.out.println(OutputData);
                       
                     //Generate ListView from SQLite Database
                       displayListView();
                      
                       
                       
                      
                       
                   } catch (JSONException e) {
           
                       e.printStackTrace();
                   }
   
                  
               }
            
          }

  //Clean all data
  /*dbHelper.deleteAllCountries();*/
  //Add some data
  /*dbHelper.insertSomeCountries();*/

  

 }
 
 public void GetURL(){
 		// WebServer Request URL
         String serverURL = "http://speedyreference.com/ehscheduleF14.php";
         
         // Use AsyncTask execute Method To Prevent ANR Problem
         new LongOperation().execute(serverURL);
 	}
 
 private void ListData(){
	 
	 sqlite_obj.open();
     
     Cursor cursor = sqlite_obj.fetchAllSchedules();

     // The desired columns to be bound
     String[] columns = new String[] {
       SQLiteDB.KEY_SCHEDULEDATE,
       SQLiteDB.KEY_SESSIONNAME,
       SQLiteDB.KEY_SESSIONTIME
       /*CountriesDbAdapter.KEY_CONTINENT,
       CountriesDbAdapter.KEY_REGION*/
     };

     // the XML defined views which the data will be bound to
     int[] to = new int[] { 
       R.id.textViewScheduleDate,
       R.id.textViewSessionName,
       R.id.textViewSessionTime,
       /*R.id.continent,
       R.id.region,*/
     };

     // create the adapter using the cursor pointing to the desired data 
     //as well as the layout information
     dataAdapter = new SimpleCursorAdapter(
       this, R.layout.schedule_info, 
       cursor, 
       columns, 
       to,
       0);

     ListView listView = (ListView) findViewById(R.id.listView1);
     // Assign adapter to ListView
     listView.setAdapter(dataAdapter);
	 
	 
 }

 private void displayListView() {

sqlite_obj.open();
     
     Cursor cursor = sqlite_obj.fetchAllSchedules();

     // The desired columns to be bound
     String[] columns = new String[] {
       SQLiteDB.KEY_SCHEDULEDATE,
       SQLiteDB.KEY_SESSIONNAME,
       SQLiteDB.KEY_SESSIONTIME
       /*CountriesDbAdapter.KEY_CONTINENT,
       CountriesDbAdapter.KEY_REGION*/
     };

     // the XML defined views which the data will be bound to
     int[] to = new int[] { 
       R.id.textViewScheduleDate,
       R.id.textViewSessionName,
       R.id.textViewSessionTime,
       /*R.id.continent,
       R.id.region,*/
     };

     // create the adapter using the cursor pointing to the desired data 
     //as well as the layout information
     dataAdapter = new SimpleCursorAdapter(
       this, R.layout.schedule_info, 
       cursor, 
       columns, 
       to,
       0);

     ListView listView = (ListView) findViewById(R.id.listView1);
     // Assign adapter to ListView
     listView.setAdapter(dataAdapter);


  listView.setOnItemClickListener(new OnItemClickListener() {
   @Override
   public void onItemClick(AdapterView<?> listView, View view, 
     int position, long id) {
   // Get the cursor, positioned to the corresponding row in the result set
   Cursor cursor = (Cursor) listView.getItemAtPosition(position);

   // Get the state's capital from this row in the database.
   String exHallScheduleDate = 
    cursor.getString(cursor.getColumnIndexOrThrow("scheduleDate"));
   Toast.makeText(getApplicationContext(),
		   exHallScheduleDate, Toast.LENGTH_SHORT).show();

   }
  });

  EditText myFilter = (EditText) findViewById(R.id.myFilter);
  
  myFilter.addTextChangedListener(new TextWatcher() {

   public void afterTextChanged(Editable s) {
   }

   public void beforeTextChanged(CharSequence s, int start, 
     int count, int after) {
   }

   public void onTextChanged(CharSequence s, int start, 
     int before, int count) {
    dataAdapter.getFilter().filter(s.toString());
   }
  });
  
  dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
         public Cursor runQuery(CharSequence constraint) {
             return sqlite_obj.fetchScheduleByDate(constraint.toString());
         }
     });

 }

}

