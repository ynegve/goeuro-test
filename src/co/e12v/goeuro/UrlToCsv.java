package co.e12v.goeuro;
import java.io.File;
import java.io.IOException;


public class UrlToCsv {

	public static void main(String[] args) throws IOException, Exception {
		
		if(args.length > 0 && args[0]!= null && args[0].length() > 0){
			String url = "http://api.goeuro.com/api/v2/position/suggest/en/"+args[0];
			String filePath = new File("output.csv").getAbsolutePath();
			JSONReader jr = new JSONReader(url);
			CSVWriter cr = new CSVWriter(filePath);
			cr.writeCsv(jr);
			System.out.println("CSV file for provided city written to: " + filePath);
			
		}else{
			System.out.println("Provide a city name as first argument. ");
		}
	}
}
