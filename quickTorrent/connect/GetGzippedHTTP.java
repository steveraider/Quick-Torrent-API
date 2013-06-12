package connect;
import java.io.*;
import java.net.*;
import java.util.zip.GZIPInputStream;



public class GetGzippedHTTP { 
	/*
	 * Given a URI this method will grab the html from that page using http protocol 
	 * KAT.ph pages are gzipped. This method takes that into account
	 */
	public String GetWebPageGzipHTTP(String URI){ 
		String html = "";
		try {
		    URLConnection connect = new URL(URI).openConnection();                        
		    BufferedReader in = null;
		    connect.setReadTimeout(100000);
		    connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.3) Gecko/20100401");
		    if (connect.getHeaderField("Content-Encoding")!=null && connect.getHeaderField("Content-Encoding").equals("gzip")){
		        in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connect.getInputStream())));            
		    } else {
		        in = new BufferedReader(new InputStreamReader(connect.getInputStream()));            
		    }          
		    String inputLine;
		    while ((inputLine = in.readLine()) != null){
		    html+=inputLine;
		    }
		in.close();
			return html;
		} catch (Exception e) {
			return html;
		}
	}
}

