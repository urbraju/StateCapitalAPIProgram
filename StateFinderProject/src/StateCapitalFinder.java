// State Capital and Largest City Finder Application
// Author : Bheem

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class StateCapitalFinder {

	
	public static void main(String[] args) {

	  try {

		// Rest Service url
		URL url = new URL("http://services.groupkt.com/state/get/USA/all");
		
		// Connect to Rest Service
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		// GET data from Rest Service
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		// Validate response code 
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		// BufferReader to get data
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        
        // Create JSON file for State Details
 		File file = new File("stateDetails.json");
 		
 		// Write Data in JSON File
 		FileWriter fileWriter = new FileWriter(file);
        while ((output = br.readLine()) != null) {
               
        	fileWriter.write(output);
         }
                        		
 		//flush the stream
 		fileWriter.flush();
 		
 		//Close the stream
 		fileWriter.close();   

 		// Disconnect from Rest Service
		conn.disconnect();
		
		// Create ArrayList for State
		ArrayList<StateDetails> stateList = new ArrayList<StateDetails>();
		stateList = convertJsontoObjectList();
		
		// Get User Input
		getUserInput(stateList);


	  } catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  }

	}
	
	// Method for getting user input
	public static void getUserInput(ArrayList<StateDetails> list) {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String stateName;  

		// Get user input as State Name or Abberivation
		System.out.println("Enter the State name or Abberivation : ");
		try {
			stateName = br.readLine();
			
			for(int i =0 ; i <list.size();i++) {
				
				if(list.get(i).getName().equalsIgnoreCase(stateName) ||list.get(i).getAbbr().equalsIgnoreCase(stateName) ) {
					
					System.out.println("The Capital is : " + list.get(i).getCapital() );
					System.out.println("The Largest City is : " + list.get(i).getLargest_city() );
				}
				
			}
			
			// Prompt user to wish to Continue or not
			System.out.println("Do you wish to continue ? Enter Yes or No");
			
			BufferedReader brA = new BufferedReader(new InputStreamReader(System.in));
			String yesorno;  
			
			yesorno = brA.readLine();
			if(yesorno.equalsIgnoreCase("YES")) {
				getUserInput(list);
			}
			else {
				System.out.println("Program will exist now !! Run the program again to continue");
				System.exit(0);
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Method to convertJsontoObjectList
	public static ArrayList<StateDetails> convertJsontoObjectList() {
		JSONParser parser = new JSONParser();
		Object object;
		ArrayList<StateDetails> finalList = new ArrayList<StateDetails>();
		try {
			object = parser.parse(new FileReader("stateDetails.json"));
			JSONObject jsonObject = (JSONObject)object;
			if(jsonObject.containsKey("RestResponse")) {
			//System.out.println("true");	
			JSONObject obj = (JSONObject)jsonObject.get("RestResponse");
			
			if(obj.containsKey("result")) {
				
				JSONArray statedetailsJsonArray = (JSONArray) obj.get("result");
				
				for (int i = 0; i<statedetailsJsonArray.size();i++) {
					
					ObjectMapper objectMapper = new ObjectMapper();
						StateDetails state = objectMapper.readValue(statedetailsJsonArray.get(i).toString(), StateDetails.class);
					  finalList.add(state);
				
				//System.out.println("final list size" + finalList.size());
	                
				
				
			}
			}
			}	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return 	finalList;
	}
		
}