import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mashape.unirest.http.exceptions.UnirestException;


public class Application {
	
	static JFrame mainFrame= new JFrame("Save My Ship");
	static JPanel mainPanel=new JPanel(new GridLayout(2,1));
	static JPanel upperPanel=new JPanel(new GridLayout(2,1));
	static JPanel lowerPanel=new JPanel(new GridLayout(1,2));
	static JTextArea msgTextArea=new JTextArea("Click the above button when in distress");
	static JButton distressButton=new JButton("I am in distress");
	static JTextArea listHospital=new JTextArea();
	static JTextArea listPharmacy=new JTextArea();
	
	public static void main(String arg[]) {
		
		showGUI();
	}
	
	public static void showGUI() {
		
		
		mainFrame.setPreferredSize(new Dimension(500,500));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		distressButton.setPreferredSize(new Dimension(50,20));
		distressButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				//call to first api
				Client c=new Client();
				try {
					String locationString=c.consumeServiceIPInfo();
					
					//parse result of calling first api
					parseLocationString(locationString);
					
				} catch (IOException | ParseException | UnirestException e1) {
					
					e1.printStackTrace();
				}
			}
		});
		upperPanel.add(distressButton);
		upperPanel.add(msgTextArea);
		
		
		listHospital.setLineWrap(true);
		listHospital.setWrapStyleWord(true);
		lowerPanel.add(new JScrollPane(listHospital));
		
		
		listPharmacy.setLineWrap(true);
		listPharmacy.setWrapStyleWord(true);
		lowerPanel.add(new JScrollPane(listPharmacy));
		
		mainPanel.add(upperPanel);
		mainPanel.add(lowerPanel);
		mainFrame.add(mainPanel);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
	
	public static void parseLocationString(String locationString) throws ParseException, IOException, UnirestException {
		
		//Create JSON Parser
		Client c1=new Client();
		JSONParser parser=new JSONParser();
		Object obj=parser.parse(locationString);
		JSONObject a=(JSONObject)obj;
		String city=(String)a.get("city");
		String region=(String)a.get("region");
		String coOrdinates=(String)a.get("loc");
		String arr[]=coOrdinates.split(",");
		System.out.println("City: "+city);
		System.out.println("Region: "+region);
		System.out.println("Latitude: "+arr[0]+" Longitude: "+arr[1]);
		
		//call to second api
		String responseFromSMSAPI=c1.consumeServiceInfobip(city,region,arr[0],arr[1]);
		System.out.println(responseFromSMSAPI);
		parseResponseFromSMSAPI(responseFromSMSAPI);
		
		//call to third api
		String hospitalListJSON=c1.consumeServiceGooglePlaces(arr[0],arr[1],"hospital");
		parseToDisplayNearByPlaces(hospitalListJSON,"hospital");
		String pharmacyListJSON=c1.consumeServiceGooglePlaces(arr[0],arr[1],"pharmacy");
		parseToDisplayNearByPlaces(pharmacyListJSON,"pharmacy");
			
	}
	
	public static void parseResponseFromSMSAPI(String responseFromSMSAPI) throws ParseException {
		
		String messageStatus="Sending location to:\n";
		JSONParser parser= new JSONParser();
		Object obj=parser.parse(responseFromSMSAPI);
		JSONObject a=(JSONObject)obj;
		JSONArray allMessages=(JSONArray)a.get("messages");
		for(int i=0; i<allMessages.size(); i++) {
			JSONObject message=(JSONObject)allMessages.get(i);
			String destNumber=(String)message.get("to");
			JSONObject status=(JSONObject)message.get("status");
			String name=(String)status.get("name");
			messageStatus+=destNumber+" Status: "+name+"\n";
		}
		
		msgTextArea.setText(messageStatus);
	}
	
	public static void parseToDisplayNearByPlaces(String str, String type) throws ParseException, IOException {
		
		String hospitalDisplay="", pharmacyDisplay="",hospitalFinalString="",pharmacyFinalString="";
		JSONParser parser= new JSONParser();
		Object obj=parser.parse(str);
		JSONObject a=(JSONObject)obj;
		JSONArray results=(JSONArray) a.get("results");
		if(type.equals("hospital")) {
			
			hospitalFinalString+="List of nearby hospitals:\n";
		}
		
		else if(type.equals("pharmacy")) {
			
			pharmacyFinalString+="List of nearby pharmacies:\n";
		}
		
		
		for(int i=0; i<results.size(); i++) {
			System.out.println();
			System.out.println();
			JSONObject object=(JSONObject)results.get(i);
			String placeName=(String)object.get("name");
			String placeID=(String)object.get("place_id");
			
			// call to get details of the place
			String urlToGetContactNumber="https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeID+"&key=AIzaSyD9-sWmBqEYSGRyvKIterHrVJf6b684UtE";
			URL urlGooglePlaces1=new URL(urlToGetContactNumber);
			HttpURLConnection connection4= (HttpURLConnection) urlGooglePlaces1.openConnection();
			connection4.setRequestMethod("GET");
			connection4.connect();
			
			BufferedReader br=new BufferedReader(new InputStreamReader(connection4.getInputStream()));
			String str1;
			StringBuffer stringBuffer=new StringBuffer();
			while((str1= br.readLine())!=null) {
				stringBuffer.append(str1);
				stringBuffer.append("\n");
			}
			str1=stringBuffer.toString();
			
			
			JSONParser parser1= new JSONParser();
			Object obj1=parser1.parse(str1);
			JSONObject a1=(JSONObject)obj1;
			JSONObject result=(JSONObject)a1.get("result");
			String formatted_address=(String)result.get("formatted_address");
			String formatted_phone_number=(String)result.get("formatted_phone_number");
			if(formatted_phone_number == null)
				formatted_phone_number="Unavailable";
			
			if(type.equals("hospital")) {
				hospitalDisplay="\n\nName: "+placeName+"\nAddress: "+formatted_address+"\nPhone No.: "+formatted_phone_number;
				hospitalFinalString+=hospitalDisplay;
			}
			
			else if(type.equals("pharmacy")){
				pharmacyDisplay="\n\nName: "+placeName+"\nAddress: "+formatted_address+"\nPhone No.: "+formatted_phone_number;
				pharmacyFinalString+=pharmacyDisplay;

			}
			
		}
		
		listHospital.append(hospitalFinalString);
		listPharmacy.append(pharmacyFinalString);
		
	}
}
