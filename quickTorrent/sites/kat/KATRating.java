package sites.kat;

import connect.GetGzippedHTTP;
import globals.Variables;
import java.util.ArrayList;

public class KATRating extends KATBuildCache {
	GetGzippedHTTP conn = new GetGzippedHTTP();
	/*
	 * Finds the best Piratebay torrent based on data gleaned using the PirateGrep Class
	 */
	public float[] sizeArray;
	public int[] seedArray;
	public int[] leechArray;
	public String[] linkArray;
	private String[] torrentPages;
	private boolean qc = true;
	
	public KATRating(String query, String mediaType, boolean qualityCheck){
		/*
		 * Constructor, two arguments the search query and a boolean, true == scan comments, false == do not scan comments
		 */
		String currentSearch = createParsedURI(query, mediaType); //returns the parsed URI query
		String searchResultHTML = conn.getWebPageGzipHTTP(currentSearch); //pulls down the html from the URI given
		torrentPages = super.grepDetailsURI(searchResultHTML); //finds each torrent page link
		qc = qualityCheck;
	}
	
	public ArrayList <String> generateQueryResults(){ 
		/*
		 * Uses methods from PageGrep and PageFunc to generate a list of candidate torrents
		 * This method must be called after the constructor
		 */
		boolean qualityCheck = this.qc;
		super.buildDataCache(this.torrentPages);
		if (qualityCheck) return super.qualityFilter(super.getDataCache()); //run it through a quality check (takes a bit longer)
		else return super.getDataCache(); //skip quality check
	}
	
	public void convertToArrays(ArrayList <String> queryResults){
		/*
		 * Converts the ArrayList <String> into the separate Arrays of the correct datatype
		 * The easiest way to implement this method is passing the GenerateQueryResults() directly to it.
		 */
		sizeArray = super.sizeToFloat(queryResults);
		seedArray = super.seedToInt(queryResults);
		leechArray = super.leechToInt(queryResults);
		linkArray = super.dataCacheToArray(queryResults, "link");
	}
	
	public String getBestLink(int[] seedArray, int[] leechArray, String mediaType){
		/*
		 * Determines the best link to pull the torrent from.
		 */
		float sizeMinimum;
		float sizeMaximum;
		if (mediaType.toLowerCase().equals("movie") || mediaType.toLowerCase().equals("movies")){
			sizeMinimum = Variables.movieSizeMin;
			sizeMaximum = Variables.movieSizeMax;
		}
		
		else if (mediaType.toLowerCase().equals("album") || mediaType.toLowerCase().equals("albums")){
			sizeMinimum = Variables.albumSizeMin;
			sizeMaximum = Variables.albumSizeMax;
		}
		else{
			sizeMinimum = Variables.musicSizeMin;
			sizeMaximum = Variables.musicSizeMax;
		}
			
		try{
		String bestChoice = null;
		int greatestDifference = 0;
			for (int i = 0; i < seedArray.length; i++){
					if (sizeArray[i] > sizeMinimum && sizeArray[i] < sizeMaximum){
						if (seedArray[i] - leechArray[i] > greatestDifference){
							greatestDifference = seedArray[i] - leechArray[i];
							bestChoice = linkArray[i];
					}
					
				}
			}
			return bestChoice;
		}catch (Exception e){
			return null;
		}
	}
}
	

