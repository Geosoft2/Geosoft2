package de.wwu.geosoftii.grp5;

import java.util.ArrayList;

public class outlierDetectionTest {
	
	public static boolean executeTest(){
		
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

		// point to be tested
		ValueSet checkPoint = new ValueSet(null, "dummyID", "30", "not_tested");
		
		return rm.isOutlier(checkPoint, list);



	}

}
