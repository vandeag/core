package org.transitime.custom.kombus;

import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.transitime.avl.PollUrlAvlModule;
import org.transitime.db.structs.AvlReport;
import org.transitime.modules.Module;
import java.io.*;
import java.util.*;

public class KombusAvlModule extends PollUrlAvlModule {

//	private static String avlURL="http://www.rozklady.kiedybus.pl/kombus/gps.log";
	private static String avlURL="http://www.rozklady.kiedybus.pl/kombus/dane.json";

	public KombusAvlModule(String agencyId) {
		super(agencyId);
	}

	@Override
	protected String getUrl() {

		return avlURL;
	}

	@Override
	protected void processData(InputStream in) throws Exception {

/*
					 Scanner s = new Scanner(in);
					 ArrayList lines = new ArrayList();
					 while (s.hasNextLine()) {
							 String line = s.nextLine();
							 if (line.contains("<LocationRecord>") == true)
									 {
										 lines = new ArrayList();
										 do {
										 lines.add(line);
										 try {
										 line = s.nextLine();
										 }
										 catch ( Exception e ) {
											 break;
										 }
									 } while (!( line.equals("</LocationRecord>") ));
									 }

						//  System.out.println(lines);
							if ( lines.size() == 0 )
								continue;
							String message = "";
							Iterator it = lines.iterator();
							while ( it.hasNext() )
								{
										message += (String)it.next();
								}
						//  System.out.println(message);
							int v1 = message.indexOf("<VehicleId>");
							int v2 = message.indexOf("</VehicleId>");
							String id = message.substring(v1+11,v2);

							int x1 = message.indexOf("<Latitude>");
							int x2 = message.indexOf("</Latitude>");
							String slat = message.substring(x1+10,x2);

							int y1 = message.indexOf("<Longtitude>");
							int y2 = message.indexOf("</Longtitude>");
							String slon = message.substring(y1+12,y2);

							Double lat = Double.parseDouble(slat);
							Double lon = Double.parseDouble(slon);

							int t1 = message.indexOf("<TimeOfRecord>");
							int t2 = message.indexOf("</TimeOfRecord>");
							String time = message.substring(t1+14,t2);


							//2016-09-07 17:02:48
							SimpleDateFormat dateformater=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

							Date timestamp=dateformater.parse(time);

							float heading=Float.NaN;

							float speed=Float.NaN;

							AvlReport avlReport =
									new AvlReport(id, timestamp.getTime(), lat,
											lon, heading, speed, "Kombus");

							processAvlReport(avlReport);
					 }

					 s.close();
					 */

					 String json=this.getJsonString(in);

		 			JSONArray array = new JSONArray(json);

		 			for (int i=0; i<array.length(); ++i) {
		 				JSONObject entry = array.getJSONObject(i);
		 				String vehicleId=entry.getString("Name");
		 				Double latitude=entry.getDouble("Latitude");
		 				Double longitude=entry.getDouble("Longitude");

		 				//2016-09-07 17:02:48
		 				SimpleDateFormat dateformater=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		 				Date timestamp=dateformater.parse(entry.getString("Time"));

		 				float heading=Float.NaN;

		 				float speed=Float.NaN;

		 				AvlReport avlReport =
		 						new AvlReport(vehicleId, timestamp.getTime(), latitude,
		 								longitude, heading, speed, "Kombus");

		 				processAvlReport(avlReport);
		 			}
	}
	/**
	 * Just for debugging
	 */
	public static void main(String[] args) {
		// Create a WexfordCoachAvlModule for testing
		Module.start("org.transitime.custom.kombus.KombusAvlModule");
	}
}
