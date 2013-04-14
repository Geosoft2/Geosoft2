package de.wwu.geosoftii.grp5;

import java.util.ArrayList;

/**
 * Test class for different methods used in the outlier detection.
 * @author sven
 *
 */
public class outlierDetectionTest {
	
	/**
	 * Method that executes the test
	 * ArrayList of ValueSets (similar to the objects returned by the database request)
	 * @return true if value is an outlier
	 */
	public static boolean executeTest(int value){
		
		// point to be tested
		ValueSet checkPoint = new ValueSet(null, "dummyID", ""+value, "not_tested");
		
		//list with test values
		ArrayList<ValueSet> list = new ArrayList<ValueSet>();
		
		//running median object
		RunningMedian rm = new RunningMedian();
		rm.setBorderMultiplicator(1.5);
		
		// add the values
		list.add(new ValueSet(null, "dummyID", "28", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "28", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "28", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "28", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "28", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "29", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "30", "not_tested"));
		list.add(new ValueSet(null, "dummyID", "31", "not_tested"));

		
		return rm.isOutlier(checkPoint, list);



	}

}
