package co.e12v.goeuro;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class JSONReader implements Iterable<List<Object>> {

	private String urlString;
	private JsonParser parser;
	
	public JSONReader(String urlString){
		this.urlString = urlString;
	}
	
	//what if row is null!!!
	private List<Object> parseNextObject(){
		List<Object> row = new ArrayList<Object>();

		while (parser.hasNext()) {	
			Event e = parser.next();
			if (e == Event.KEY_NAME) {
				switch (parser.getString()) {
				case "_id":
					parser.next();
					row.add(parser.getInt());
					break;
				case "name":
					parser.next();
					row.add(parser.getString());
					break;
				case "type":
					parser.next();
					row.add(parser.getString());
					break;
				case "latitude":
					parser.next();
					row.add(parser.getBigDecimal());
					break;
				case "longitude":
					parser.next();
					row.add(parser.getBigDecimal());
					return row;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator iterator() {
		URL url;
		try {
			url = new URL(urlString);
			InputStream is = url.openStream();
			parser = Json.createParser(is);
			return new JsonObjectIterator();
			
		} catch (IOException e) {
			System.out.println("Provide valid city name: " +e.getMessage());
			System.exit(0);
		}
		return null;
	}
	
	private class JsonObjectIterator implements Iterator<List<Object>>{

		List<Object> row;
		boolean rowRead;
		
		@Override
		public boolean hasNext() {
			row = parseNextObject();
			if(row != null){
				rowRead = true;
				return true;
			}
			return false;
		}

		@Override
		public List<Object> next() {
			if(rowRead){
				rowRead = false;
				return row;
			}
			else return parseNextObject();
		}

		@Override
		public void remove() {
		}
		
	}

}
