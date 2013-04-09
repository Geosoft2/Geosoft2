package de.wwu.geosoftii.grp5;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Main class that controls the outlier detection workflow.
 * @author sven
 *
 */
public class OutlierDetection {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// do the left sided outlier detection
		//leftSidedWindowOutlierDetection();
		
		// do the two sided window outlier detection
		twoSidedWindowOutlierDetection();
		
		//System.out.println(outlierDetectionTest.executeTest());

		
	
		//Testing different functions
		/*
		System.out.println(dbCon.getAllFeatures());
		
		
		ArrayList<ValueSet> val = dbCon.getValuesInWindow(30, aqeSensors.getTemperaturePhenomenon(), "75842", true);
		int count = 0;
		Iterator valIter = val.iterator();
		while(valIter.hasNext()){
			count++;
			ValueSet temp = (ValueSet) valIter.next();
			System.out.println(count+" "+temp.getDate()+" "+temp.getQuality_id()+": "+temp.getValue()+" "+temp.getQuality_value());
		}
		
		System.out.println(rm.isOutlier(dbCon.getOldestUncheckedValue(30, aqeSensors.getTemperaturePhenomenon(), "75842"), dbCon.getValuesInWindow(30, aqeSensors.getTemperaturePhenomenon(), "75842", true)));
		
		System.out.println(dbCon.getNewestQualityTimestamp());
		
		System.out.println(dbCon.getPhenomenaOfFeature("75842"));
		
		*/
		
		/*
		ValueSet temp = dbCon.getOldestUncheckedValue(30, aqeSensors.getTemperaturePhenomenon(), "75842");
		System.out.println(temp.getDate()+"  "+temp.getQuality_id()+": "+temp.getValue()+" "+temp.getQuality_value());
		*/	
		
	}
	
	static void leftSidedWindowOutlierDetection(){
		//logging stuff
				Logger logger = Logger.getRootLogger();
				PropertyConfigurator.configure("log4j.properties");
				
				//create properties
				Properties properties = new Properties();
				
				//create running median object
				RunningMedian rm = new RunningMedian();
				
				try {
					//create new database connection
					databaseCon dbCon = new databaseCon();
					logger.info("Database connection established");
					
					// invalid query -> for testing purposes
					//ValueSet testPoint = dbCon.getOldestUncheckedValue(30, "urn:ogc:object:feature:Sensor:AQE:temperature-sensor", "75842");
					
					//load the properties file
					properties.load(new FileInputStream("config.properties"));

					boolean reset = Boolean.valueOf(properties.getProperty("reset_outlier_information"));
					
					//resets the outlier marking before starting the program -> for testing purposes
					if (reset) dbCon.resetOutlierMarking();
					
					//Get newest timestamp from quality table as reference
					//Date refDate = dbCon.getNewestQualityTimestamp();
					
					// set window width from properties file
					int winWidth = Integer.valueOf(properties.getProperty("window_width"));
					
					// set border multiplicator
					double borderMultiplicator = Double.valueOf(properties.getProperty("border_multiplicator"));
					rm.setBorderMultiplicator(borderMultiplicator);
					
					logger.info("Collect the features ids");
					// get all features and their phenomenons
					// first get the feaures ids
					ArrayList<String> featureIds = dbCon.getAllFeatures();
					// empty collection to be filled with features
					ArrayList<Feature> features = new ArrayList<Feature>();
					Iterator<String> idIter = featureIds.iterator();
					// create all the features
					// iterate over the feature ids
					while(idIter.hasNext()){
						String id = idIter.next();
						//create new feature
						Feature tempFeature = new Feature(id);
						//save the phenomenon of the feature
						tempFeature.setPhenomena(dbCon.getPhenomenaOfFeature(id));
						//add feature to feature collection
						features.add(tempFeature);
					}
					logger.info("Ids collected");
					logger.info("Iterate over features");
					//iterate over the features
					Iterator<Feature> featIter = features.iterator();
					while(featIter.hasNext()){
						// temporary feature
						Feature tempFeature = featIter.next();
						// temporary featureId
						String featureId = tempFeature.getId();
						logger.info("Current feature: " + featureId);
						logger.info("Iterate over phenomena");
						// iterate over the features phenomena
						ArrayList<String> phenomena = tempFeature.getPhenomena();
						Iterator<String> phenIter = phenomena.iterator();
						while(phenIter.hasNext()){
							//current phenomenon
							String tempPhenomenon = phenIter.next();
							logger.info("Current phenomenon: " + tempPhenomenon);
							Date refDate = dbCon.getNewestQualityTimestamp(featureId, tempPhenomenon);
							// search for outliers
							ValueSet checkPoint = dbCon.getOldestUncheckedValue(winWidth, tempPhenomenon, featureId);
							//check every value that is younger than the reference date
							while( checkPoint!=null && ( refDate.after(checkPoint.getDate()) || refDate.equals(checkPoint.getDate()) ) ){
								//logger.info("checkPoint found - " + checkPoint.getDate() + "  qualityId: " + checkPoint.getQuality_id() );
							//check for outliers
								//get the ValueSets used to test the current value, sorted by value
								ArrayList<ValueSet> valuesInWindowList = dbCon.getValuesInWindow(checkPoint, winWidth, tempPhenomenon, featureId, true);
								//check if there are enough values for outlier detection in database
								//in the beginning there may be not enough values in the database
								if (valuesInWindowList.size()==winWidth){
									logger.info("Check if current value is outlier");
									//check if the current value is an outlier
									boolean isOutlier = rm.isOutlier(checkPoint, valuesInWindowList);
									String outlierTag = "not_tested";
									if (isOutlier) outlierTag = "yes";
									else if (!isOutlier) outlierTag = "no";
									//update the information in the table
									dbCon.setOutlierInformation(outlierTag, checkPoint.getQuality_id());
									logger.info(checkPoint.getDate()+"  "+checkPoint.getQuality_id()+": "+checkPoint.getValue()+" "+checkPoint.getQuality_value()+" "+outlierTag);
									//save the next value
									checkPoint = dbCon.getOldestUncheckedValue(winWidth, tempPhenomenon, featureId);
								} else {
									// exit condition -> Not enough values in window
									checkPoint = null;
								}
								
							}
						}
					}
					
					// close the database connection
					dbCon.disconnect();
					logger.info("Outlier detection complete");
					
				} catch (FileNotFoundException e) {
					logger.warn("outlier_config.properties could not be found");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}
	
	static void twoSidedWindowOutlierDetection(){
		
		//logging stuff
		Logger logger = Logger.getRootLogger();
		PropertyConfigurator.configure("log4j.properties");
		
		//create properties
		Properties properties = new Properties();
		
		//create running median object
		RunningMedian rm = new RunningMedian();
		
		try {
			//create new database connection
			databaseCon dbCon = new databaseCon();
			logger.info("Database connection established");
			
			// invalid query -> for testing purposes
			//ValueSet testPoint = dbCon.getOldestUncheckedValue(30, "urn:ogc:object:feature:Sensor:AQE:temperature-sensor", "75842");
			
			//load the properties file
			properties.load(new FileInputStream("config.properties"));

			boolean reset = Boolean.valueOf(properties.getProperty("reset_outlier_information"));
			
			//resets the outlier marking before starting the program -> for testing purposes
			if (reset) dbCon.resetOutlierMarking();
			
			//Get newest timestamp from quality table as reference
			//Date refDate = dbCon.getNewestQualityTimestamp();
			
			// set window width for air quality eggs from properties file
			int aqeWinWidth = Integer.valueOf(properties.getProperty("aqe_window_width"));
			
			// set window width for air quality eggs from properties file
			int lanuvWinWidth = Integer.valueOf(properties.getProperty("lanuv_window_width"));
			
			// filter even numbers
			if (aqeWinWidth%2==0) aqeWinWidth++;
			if (lanuvWinWidth%2==0) lanuvWinWidth++;
			
			// set border multiplicator
			double borderMultiplicator = Double.valueOf(properties.getProperty("border_multiplicator"));
			rm.setBorderMultiplicator(borderMultiplicator);
			
			logger.info("Collect the features ids");
			// get all features and their phenomenons
			// first get the feaures ids
			ArrayList<String> featureIds = dbCon.getAllFeatures();
			// empty collection to be filled with features
			ArrayList<Feature> features = new ArrayList<Feature>();
			Iterator<String> idIter = featureIds.iterator();
			// create all the features
			// iterate over the feature ids
			while(idIter.hasNext()){
				String id = idIter.next();
				//create new feature
				Feature tempFeature = new Feature(id);
				//save the phenomenon of the feature
				tempFeature.setPhenomena(dbCon.getPhenomenaOfFeature(id));
				//add feature to feature collection
				features.add(tempFeature);
			}
			logger.info("Ids collected");
			logger.info("Iterate over features");
			//iterate over the features
			Iterator<Feature> featIter = features.iterator();
			while(featIter.hasNext()){
				// standard win width
				int winWidth = 21;
				// temporary feature
				Feature tempFeature = featIter.next();
				// temporary featureId
				String featureId = tempFeature.getId();
				if(featureId.equals("Geist")) {
					winWidth = lanuvWinWidth;
				} else if (featureId.equals("Weseler")) {
					winWidth = lanuvWinWidth;
				} else {
					winWidth = aqeWinWidth;
				}
				logger.info("Current feature: " + featureId);
				logger.info("Iterate over phenomena");
				logger.info("win width: "+winWidth);
				// iterate over the features phenomena
				ArrayList<String> phenomena = tempFeature.getPhenomena();
				Iterator<String> phenIter = phenomena.iterator();
				while(phenIter.hasNext()){
					//current phenomenon
					String tempPhenomenon = phenIter.next();
					logger.info("Current phenomenon: " + tempPhenomenon);
					// get newest value left of the half of the given window size
					//Example for window size 7: ...*******[***+***]| -> the plus is the wanted value
					Date refDate = dbCon.getNewestOuterRightQualityTimestamp(featureId, tempPhenomenon, (int)(winWidth/2.0));
					// search for outliers
					//middle of window, to be checked
					ValueSet checkPoint = dbCon.getOldestUncheckedValue((int)Math.ceil(winWidth/2.0), tempPhenomenon, featureId);
					//right outer border of window
					ValueSet rightBorder = dbCon.getOldestUncheckedValue(winWidth, tempPhenomenon, featureId);
					//check every value that is younger than the reference date
					while( checkPoint!=null && ( refDate.after(checkPoint.getDate()) || refDate.equals(checkPoint.getDate()) ) ){
						//logger.info("checkPoint found - " + checkPoint.getDate() + "  qualityId: " + checkPoint.getQuality_id() );
					//check for outliers
					
						//get the ValueSets used to test the current value, sorted by value
						ArrayList<ValueSet> valuesInWindowList = dbCon.getValuesInWindow(rightBorder, winWidth, tempPhenomenon, featureId, true);
						
						//check if there are enough values for outlier detection in database
						//in the beginning there may be not enough values in the database
						if (valuesInWindowList.size()==winWidth){
							//logger.info("Check if current value is outlier");
							//check if the current value is an outlier
							boolean isOutlier = rm.isOutlier(checkPoint, valuesInWindowList);
							String outlierTag = "not_tested";
							if (isOutlier) outlierTag = "yes";
							else if (!isOutlier) outlierTag = "no";
							//update the information in the table
							dbCon.setOutlierInformation(outlierTag, checkPoint.getQuality_id());
							//logger.info(checkPoint.getDate()+"  "+checkPoint.getQuality_id()+": "+checkPoint.getValue()+" "+checkPoint.getQuality_value()+" "+outlierTag);
							//save the next value
							checkPoint = dbCon.getOldestUncheckedValue((int) Math.ceil(winWidth/2.0), tempPhenomenon, featureId);
							//right outer border of window
							rightBorder = dbCon.getOldestUncheckedValue(winWidth, tempPhenomenon, featureId);
						} else {
							// exit condition -> Not enough values in window
							checkPoint = null;
						}
						
					}
					
				}
				
			}
			
			// close the database connection
			dbCon.disconnect();
			logger.info("Outlier detection complete");
			
		} catch (FileNotFoundException e) {
			logger.warn("outlier_config.properties could not be found");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Changes
	 * ValueSet-, SensorSet-, FeatureSet- and databaseConnection-classes added.
	 * Program is now possible to validate the data stored in the database with a left sided moving window filter (running median)
	 */
	
	

}
