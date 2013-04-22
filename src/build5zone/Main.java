package build5zone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JFrame;

import build5zone.optim.YearlyResults;

import panel.MultipleTimeTrace;
import panel.MultipleXYTrace;
import tools.OpenTrnsysoutput;
import tools.RunTrnSys;
import tools.WriteListToFile;

public class Main {

	/**
	 * 
	 * @param nightTimeTEMP
	 * @param dayTimeTemp
	 * @param dayStartTime included
	 * @param dayEndTime included
	 * @return
	 */

	public static LinkedList<Double> generateOneDayTempOrder(
			double nightTimeTEMP ,
			double dayTimeTemp ,
			int dayStartTime,
			int dayEndTime){


		LinkedList<Double> list = new LinkedList<>();

		double tempOrder = nightTimeTEMP;

		// prepare temp order file
		for(int hourofday = 0 ; hourofday <24; hourofday++){
			//weekday
			if(dayStartTime<=hourofday && hourofday <= dayEndTime){
				//day
				tempOrder = dayTimeTemp;
			}else{
				//night
				tempOrder = nightTimeTEMP;
			}
			list.add(tempOrder);
		}

		return list;

	}





	public static LinkedList<Double> generateOneWeekTempOrder(
			double nightTimeTEMP ,
			double dayTimeTemp ,
			int dayStartTime,
			int dayEndTime
			){
		LinkedList<Double> list = new LinkedList<>();

		double tempOrder = nightTimeTEMP;

		// prepare temp order file
		for(int hourOfYear = 0 ; hourOfYear <168; hourOfYear++){
			//hour
			int dayofYear = hourOfYear/24;
			int hourofday = hourOfYear % 24;

			//week
			int weekID = dayofYear/7;
			int dayofWeek = dayofYear%7;


			//calc
			if(dayofWeek < 5){
				//weekday
				if(dayStartTime<=hourofday && hourofday <= dayEndTime){
					//day
					tempOrder = dayTimeTemp;
				}else{
					//night
					tempOrder = nightTimeTEMP;
				}
			}else{
				//weekend 
				tempOrder = nightTimeTEMP;
			}

			list.add(tempOrder);
		}

		return list;
	}
	public static LinkedList<Double> generateTempOrder(
			double nightTimeTEMP ,
			double dayTimeTemp ,
			int dayStartTime,
			int dayEndTime){

		LinkedList<Double> list = new LinkedList<>();

		double tempOrder = nightTimeTEMP;

		// prepare temp order file
		for(int hourOfYear = 0 ; hourOfYear <9000; hourOfYear++){
			//hour
			int dayofYear = hourOfYear/24;
			int hourofday = hourOfYear % 24;

			//week
			int weekID = dayofYear/7;
			int dayofWeek = dayofYear%7;


			//calc
			if(dayofWeek < 5){
				//weekday
				if(dayStartTime<=hourofday && hourofday <= dayEndTime){
					//day
					tempOrder = dayTimeTemp;
				}else{
					//night
					tempOrder = nightTimeTEMP;
				}
			}else{
				//weekend 
				tempOrder = nightTimeTEMP;
			}

			list.add(tempOrder);
		}

		return list;
	}

	public static void  dosimulation(String basePathwithending,
			double nightTimeTEMP ,
			double dayTimeTemp ){

		LinkedList<Double> list = generateTempOrder(nightTimeTEMP,dayTimeTemp,7,18);

		//System.out.println("list size "+list.size());
		try {
			//C:\Trnsys17\MyProjects\5ZonesModel_20130301\Profils
			WriteListToFile.writeToFile(basePathwithending+"Profils\\TempConsigne.txt","nightTempOrder", list);
		} catch (IOException e) {
			e.printStackTrace();
		}



		//run trnsys
		//
		String dckfilefullpath = basePathwithending+"5ZonesModel.dck";
		RunTrnSys.runTrnSys(dckfilefullpath,false);

	}


	/**
	 * Collect energy results by week
	 * 
	 * @param basePathwithending
	 * @return
	 */
	public static SetOfMap collectWeeklyEnergyResults(String basePathwithending){
		//open weekly results file
		String weekelyenergyFile = basePathwithending+"outputFile\\resultWeekly.txt";

		SetOfMap energyByWeek = new SetOfMap();
		for(int zoneID = 1 ; zoneID <= 5 ; zoneID++ ){
			TreeMap<Long, Double> weeklyRawData = OpenTrnsysoutput.collectData(weekelyenergyFile, zoneID);

			//long time = yearlyenergyData.lastEntry().getKey();
			for (long i = 1; i <= 52; i+=1) {
				Entry<Long, Double> entry = weeklyRawData.floorEntry(i*168);
				if(entry != null){

					energyByWeek.addData("weekelyEnergyZ"+zoneID, i,  entry.getValue());
				}else{
					System.out.println("null entry");
				}

				//mxyt.addValue(i*168, entry.getValue(), "weekZ"+zoneID, 0);
			}

			/*
					System.out.println("weeklyenergyData for z"+zoneID+" size "+weeklyenergyData.size());
					for(Entry<Long, Double> entry : weeklyenergyData.entrySet()){
						System.out.println("peakat:"+entry.getKey()+" value : "+entry.getValue());
					}
							TIME   
							EnergyWeekZ1 
							EnergyWeekZ2   
							EnergyWeekZ3       
							EnergyWeekZ4        
							EnergyWeekZ5       

							PowerZ1               
							PowerZ2    
							PowerZ3               
							PowerZ4          
							PowerZ5  

				            priceWeekZ1      
				            priceWeekZ2         
				            priceWeekZ3        
				            priceWeekZ4        
				            priceWeekZ5
			 */
		}

		return energyByWeek;

	}

	public static SetOfMap collectWeeklyPriceResults(String basePathwithending){
		//open weekly results file
		String resultWeeklyFile = basePathwithending+"outputFile\\resultWeekly.txt";


		SetOfMap priceByWeek = new SetOfMap();
		for(int zoneID = 1 ; zoneID <= 5 ; zoneID++ ){
			TreeMap<Long, Double> weeklyRawData = OpenTrnsysoutput.collectData(resultWeeklyFile, zoneID+10);

			//long time = yearlyenergyData.lastEntry().getKey();
			for (long i = 1; i <= 52; i+=1) {
				Entry<Long, Double> entry = weeklyRawData.floorEntry(i*168);
				if(entry != null){
					priceByWeek.addData("weekelyPriceZ"+zoneID, i,  entry.getValue());
				}else{
					System.out.println("null entry");
				}

				//mxyt.addValue(i*168, entry.getValue(), "weekZ"+zoneID, 0);
			}

		}

		return priceByWeek;
	}

	public static SetOfMap collectWeeklyInConfortResults(String basePathwithending){
		//open weekly results file
		String resultWeeklyFile = basePathwithending+"outputFile\\resultWeeklyConfort.txt";

		// TIME                    	
		//Z1Inconfort             Z1ColdInconf             	Z2Inconfort              	Z2ColdInconf             	Z3Inconfort              	Z3ColdInconf             	Z4Inconfort              	Z4ColdInconf             	Z5Inconfort              	Z5ColdInconf          
		//priceWeekZ1              	priceWeekZ2              	priceWeekZ3              	priceWeekZ4              	priceWeekZ5   


		SetOfMap inconfortByWeek = new SetOfMap();
		for(int zoneID = 1 ; zoneID <= 5 ; zoneID++ ){
			TreeMap<Long, Double> inconfortWeekrawData = OpenTrnsysoutput.collectData(resultWeeklyFile, 2*zoneID-1);

			//long time = yearlyenergyData.lastEntry().getKey();
			for (long i = 1; i <= 52; i+=1) {
				Entry<Long, Double> entry = inconfortWeekrawData.floorEntry(i*168);
				if(entry != null){
					inconfortByWeek.addData("weekelyInconfortZ"+zoneID, i,  entry.getValue());
				}else{
					System.out.println("null entry");
				}

				//mxyt.addValue(i*168, entry.getValue(), "weekZ"+zoneID, 0);
			}



		}

		return inconfortByWeek;
	}

	public static SetOfMap collectWeeklyColdInConfortResults(String basePathwithending){
		String resultWeeklyFile = basePathwithending+"outputFile\\resultWeeklyConfort.txt";
		SetOfMap coldinconfortByWeek = new SetOfMap();
		for(int zoneID = 1 ; zoneID <= 5 ; zoneID++ ){
			TreeMap<Long, Double> coldinconfortWeekrawData = OpenTrnsysoutput.collectData(resultWeeklyFile, 2*zoneID);
			for (long i = 1; i <= 52; i+=1) {
				Entry<Long, Double> entry = coldinconfortWeekrawData.floorEntry(i*168);
				if(entry != null){
					coldinconfortByWeek.addData("weekelyColdInconfortZ"+zoneID, i,  entry.getValue());
				}else{
					System.out.println("null entry");
				}

				//mxyt.addValue(i*168, entry.getValue(), "weekZ"+zoneID, 0);
			}

		}
		return coldinconfortByWeek;
	}

	public static TreeMap<Double, Double> collectWeeklyOutsideTemp(String basePathwithending){
		String resultWeeklyFile = basePathwithending+"outputFile\\result1.txt";

		TreeMap<Long, Double> rawData = OpenTrnsysoutput.collectData(resultWeeklyFile, 15);

		TreeMap<Double, Double> outsideByWeek = new TreeMap<>();
		for (long i = 1; i <= 52; i+=1) {
			Entry<Long, Double> entry = rawData.floorEntry(i*168);
			if(entry != null){
				outsideByWeek.put(new Double(i), entry.getValue());
			}else{
				System.out.println("null entry");
			}
		}

		return outsideByWeek;

	}


	/**
	 * zoneID is from 1 to 5;
	 * @param basePathwithending
	 * @return
	 */
	public static YearlyResults collectYearlyResults (String basePathwithending){
		YearlyResults yr = new YearlyResults();

		String outputFileName = basePathwithending+"outputFile\\resultAnnual.txt";
		for(int zoneID = 1 ; zoneID <= 5 ; zoneID++ ){

			TreeMap<Long, Double> yearlyenergyData = OpenTrnsysoutput.collectData(
					outputFileName,
					zoneID);

			TreeMap<Long, Double> yearlypriceData = OpenTrnsysoutput.collectData(
					outputFileName,
					5+zoneID);
			TreeMap<Long, Double> yearlyInconfData = OpenTrnsysoutput.collectData(
					outputFileName,
					10+(zoneID*2)-1);

			TreeMap<Long, Double> yearlyColdInconfData = OpenTrnsysoutput.collectData(
					outputFileName,
					10+(zoneID*2));

			// 10+(zoneID*2)-1; -> inconf
			// 10+(zoneID*2)-1; -> coldInconf

			double totalZonePrice = yearlypriceData.lastEntry().getValue();
			double totalenergyZone = yearlyenergyData.lastEntry().getValue();

			double totalZoneinconf = yearlyInconfData.lastEntry().getValue();
			double totalZoneColdInconf = yearlyColdInconfData.lastEntry().getValue();
			yr.setEnergy(zoneID, totalenergyZone);
			yr.setPrice(zoneID, totalZonePrice);
			yr.setInconfort(zoneID, totalZoneinconf);
			yr.setColdInconfort(zoneID, totalZoneColdInconf);
		}

		return yr;
	}

	public static void main(String[] args) throws IOException {
		MultipleXYTrace energyvsTempOrderGraph = new MultipleXYTrace();
		JFrame f = new JFrame("energy");
		f.add(energyvsTempOrderGraph);
		f.setVisible(true);

		MultipleXYTrace pricevsTempOrderGraph = new MultipleXYTrace();
		JFrame f2 = new JFrame("price");
		f2.add(pricevsTempOrderGraph);
		f2.setVisible(true);

		MultipleXYTrace coldInconfortvsTempOrderGraph = new MultipleXYTrace();
		JFrame f3 = new JFrame("coldInconf");
		f3.add(coldInconfortvsTempOrderGraph);
		f3.setVisible(true);


		String endline = System.getProperty("line.separator");

		String resultYearFileName = "Result_YEAR_5Zone.txt";
		FileWriter outYearly = new FileWriter(new File(resultYearFileName), false); //no append
		outYearly.write("NightTimeOrder EnergyZ1 EnergyZ2 EnergyZ3 EnergyZ4 EnergyZ5");
		outYearly.write(endline);
		outYearly.close();

		String resultWeekFileName = "Result_Week_Zone1.txt";
		FileWriter outWeekely = new FileWriter(new File(resultWeekFileName), false); //no append

		outWeekely.write("NightTimeOrder \t");
		for(int i = 0 ;i<52;i++){
			outWeekely.write("energyWeek"+i+"Z1 \t");
			outWeekely.write("priceWeek"+i+"Z1 \t");
		}

		outWeekely.write(endline);
		outWeekely.close();

		String basePathwithending = "C:\\Trnsys17\\MyProjects\\5ZonesModel_20130301\\";




		double nightTimeTEMP= 19;
		double dayTimeTemp = 20;

		//result by zone
		SetOfMap weeklyZ1DataVSTempOrder = new SetOfMap();


		for(nightTimeTEMP = 10 ; nightTimeTEMP<= 24 ; nightTimeTEMP+=0.5){

			dosimulation(basePathwithending,nightTimeTEMP, dayTimeTemp); 

			/***
			 * YEAR results
			 */
			YearlyResults yearlyresults = collectYearlyResults(basePathwithending);


			energyvsTempOrderGraph.addValue(nightTimeTEMP, yearlyresults.getEnergy(0), "energyYearZ1", 0);

			//print to file
			outYearly = new FileWriter(new File(resultYearFileName), true); // append
			String toPrint = String.format("%s %s %s %s %s %s", 
					nightTimeTEMP,
					yearlyresults.getEnergy(1),
					yearlyresults.getEnergy(2),
					yearlyresults.getEnergy(3),
					yearlyresults.getEnergy(4),
					yearlyresults.getEnergy(5)
					);
			outYearly.write(toPrint);
			outYearly.write(endline);
			outYearly.close();
			System.out.println(toPrint);

			/********
			 * WEEKELY RESULTS
			 */

			//Z1

			outWeekely = new FileWriter(new File(resultWeekFileName), true); // append

			SetOfMap energyByWeek = collectWeeklyEnergyResults(basePathwithending);
			SetOfMap priceByWeek = collectWeeklyPriceResults(basePathwithending);
			SetOfMap inconfortbyWeek = collectWeeklyInConfortResults(basePathwithending);
			SetOfMap coldinconfortByWeek = collectWeeklyColdInConfortResults(basePathwithending);


			TreeMap<Double, Double> energyByWeekZ1 = energyByWeek.getMap("weekelyEnergyZ1");
			TreeMap<Double, Double> priceByWeekZ1 = priceByWeek.getMap("weekelyPriceZ1");
			TreeMap<Double, Double> inconfortbyWeekZ1 = inconfortbyWeek.getMap("weekelyInconfortZ1");
			TreeMap<Double, Double> coldinconfortByWeekZ1 = coldinconfortByWeek.getMap("weekelyColdInconfortZ1");

			String toPrintweek = String.format("%s \t", nightTimeTEMP);

			for(Entry<Double, Double> e : energyByWeekZ1.entrySet()){
				Double weekID = e.getKey();
				Double energythisWeek = e.getValue();
				Double priceThisWeek = priceByWeekZ1.get(weekID);
				Double inconfortThisWeek = inconfortbyWeekZ1.get(weekID);
				Double coldInconfortThisWeek = coldinconfortByWeekZ1.get(weekID);


				//map with nightTimeTemp
				weeklyZ1DataVSTempOrder.addData("energyZ1W"+weekID, nightTimeTEMP, energythisWeek);
				weeklyZ1DataVSTempOrder.addData("priceZ1W"+weekID, nightTimeTEMP, priceThisWeek);
				weeklyZ1DataVSTempOrder.addData("coldInconfortZ1W"+weekID, nightTimeTEMP, coldInconfortThisWeek);
				toPrintweek+= String.format("%s \t",energythisWeek );
				toPrintweek+= String.format("%s \t",priceThisWeek );

				//graph
				energyvsTempOrderGraph.addValue(nightTimeTEMP, energythisWeek, "energyZ1W"+weekID, 0);
				pricevsTempOrderGraph.addValue(nightTimeTEMP, priceThisWeek, "priceZ1W"+weekID, 0);
				coldInconfortvsTempOrderGraph.addValue(nightTimeTEMP, coldInconfortThisWeek, "colfinconfZ1W"+weekID,0) ; 

			}
			outWeekely.write(toPrintweek);
			outWeekely.write(endline);
			outWeekely.close();



		}

		FileOutputStream fos = new FileOutputStream("dataBYtempOrder.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(weeklyZ1DataVSTempOrder);
		oos.flush();
		oos.close();

	}


}
