import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

class Client {
	
	public String consumeServiceIPInfo() throws IOException, ParseException, UnirestException {
		
		
		URL urlGeoLocator= new URL("http://ipinfo.io/json");
		HttpURLConnection connection1= (HttpURLConnection) urlGeoLocator.openConnection();
		connection1.setRequestMethod("GET");
		connection1.connect();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(connection1.getInputStream()));
		String str;
		StringBuffer buffer=new StringBuffer();
		while((str= br.readLine())!=null) {
			buffer.append(str);
			buffer.append("\n");
		}
		str=buffer.toString();
		return str;
		
	}
	
	
	
	public static String consumeServiceInfobip(String city, String region, String lat, String lon) throws IOException, UnirestException {
		
		URL urlMessagingService=new URL("https://api.infobip.com/sms/1/text/single");
		HttpURLConnection connection2=(HttpURLConnection) urlMessagingService.openConnection();
		
		//adding header
		connection2.setDoOutput(true);
		connection2.setRequestMethod("POST");
		connection2.setRequestProperty("authorization", "Basic c21ydXRpOmpldGh3YW5p");
		connection2.setRequestProperty("content-type", "application/json");
		connection2.setRequestProperty("accept", "application/json");
		
		JSONObject obj = new JSONObject();
		
		JSONArray emergencyContactList = new JSONArray();
		
		//emergencyContactList.add("+15854858455");
		
		
		/////////////////////////////////////////////////////////////
		//Add the contact numbers here
		//
		//emergencyContactList.add("+1CONTINUE....AN EXAMPLE GIVEN ABOVE");
		//emergencyContactList.add("+1CONTINUE...");
		//.
		//.
		//.
		/////////////////////////////////////////////////////////////
		
		
		obj.put("to", emergencyContactList);
		String msgContent="I am Mohita. I am in distress. My location is- \nLatitude: "+lat+"\nLongitude: "+lon+"\nCity: "+city+"\nRegion: "+region;
		obj.put("text", msgContent);
		String urlParameters= obj.toJSONString();
		
		
		//send request
		DataOutputStream wr=new DataOutputStream(connection2.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		int responseCode=connection2.getResponseCode();
		System.out.println(responseCode);
		
		BufferedReader br=new BufferedReader(new InputStreamReader(connection2.getInputStream()));
		String str;
		StringBuffer buffer=new StringBuffer();
		while((str= br.readLine())!=null) {
			buffer.append(str);
			buffer.append("\n");
		}
		str=buffer.toString();
		return str;
	}
	
	public static String consumeServiceGooglePlaces(String lat, String lon, String type) throws IOException, ParseException {
		
		String toBePassedAsURL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&type="+type+"&opennow&radius=1800&key=AIzaSyD9-sWmBqEYSGRyvKIterHrVJf6b684UtE";
		URL urlGooglePlaces=new URL(toBePassedAsURL);
		HttpURLConnection connection3= (HttpURLConnection) urlGooglePlaces.openConnection();
		connection3.setRequestMethod("GET");
		connection3.connect();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(connection3.getInputStream()));
		String str;
		StringBuffer buffer=new StringBuffer();
		while((str= br.readLine())!=null) {
			buffer.append(str);
			buffer.append("\n");
		}
		str=buffer.toString();
		
		return str;	
		
	}
	
	
	
}
