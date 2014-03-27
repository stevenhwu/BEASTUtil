package core.xml.shank.multiTime;

import java.util.ArrayList;
import java.util.HashMap;

public class BEASTforShankP9 {

	static ArrayList<String> taxon = new ArrayList<String>();
	static ArrayList<Integer>[] timeGroup;// = new ArrayList[12];
	// static String[] test = new String[12];

	// static ArrayList<int[]> timeIndex = new ArrayList<int[]>();

	// static ArrayList<StringBuilder> template = new
	// ArrayList<StringBuilder>();

	static HashMap<String, String> seq = new HashMap<String, String>();
	static HashMap<String, String> time = new HashMap<String, String>();

	String[] t9 = new String[] { "9V03", // 1
			"9V04", "9V051", "9V09", "9V13", // 5
			"9V17", "9V20", "9V21", "9V22", "9V23", // 10
			"9V24", "9V25" };

	/**
	 * reorganise the seq to T1T2, T1T3, T1T4...etc create time point creat
	 * master file
	 * 
	 * paste("1V", formatC(data[ data[,1]=="p1",2], width=2, flag=0) ,sep="",
	 * collapse=", ")
	 */
	public static void main(String[] args) {

		String outDir = "/home/sw167/PostdocLarge/BEASTRun/ShankMultiTime/";
		PatientInfo.getTemplate(outDir);

		String[] t1 = new String[] { "1V02", "1V04", "1V05", "1V07", "1V09", // 5
				"1V10", "1V11", "1V12", "1V13", "1V14",  // 10
				"1V15", "1V16", "1V17",	"1V18", "1V19"  // 15

		};
		
		String[] t2 = new String[] { "2V02", "2V04", "2V05", "2V07", "2V08", // 5
				"2V10", "2V12", "2V13", "2V14", "2V15", // 10
				"2V16", "2V17", "2V19", "2V23", "2V25", // 15
				"2V26", "2V28", "2V29"
		};
		
		String[] t3 = new String[] { "3V06", "3V07", "3V10", "3V11", "3V13", // 5
				"3V15", "3V17", "3V18", "3V19", "3V20", // 10
				"3V21", "3V22"
		};

		String[] t5 = new String[] { "5V02", "5V03", "5V04", "5V41", "5V05", // 5
				"5V51", "5V06", "5V07", "5V08", "5V81", // 10
				"5V09", "5V10", "5V11", "5V12", "5V13", // 15
				"5V15"
		};
		
		String[] t6 = new String[] { "6V11", "6V12", "6V13", "6V14", "6V15", // 5
				"6V16", "6V17", "6V18", "6V19", "6V22" // 10
		};
		
		String[] t7 = new String[] { "7V05", "7V09", "7V10", "7V12", "7V13", // 5
				"7V14", "7V15", "7V17", "7V18", "7V19", // 10
				"7V20", "7V21", "7V22"
		};
		
		String[] t8 = new String[] { "8V09", "8V10", "8V11", "8V12", "8V13", // 5
				"8V14", "8V15", "8V16", "8V17", "8V18", // 10
				"8V19", "8V20", "8V22", "8V24", "8V25", // 15
				"8V26", "8V27"
		};

		String[] t9 = new String[] { "9V03", "9V04", "9V51", "9V09", "9V13", // 5
				"9V17", "9V20", "9V21", "9V22", "9V23", // 10
				"9V24", "9V25" 
		};

		String[] t11 = new String[] { "11V06", "11V10", "11V12", "11V15", // 4
				"11V17", "11V22" 
		};

		createPatient(outDir, 1, t1);
		createPatient(outDir, 2, t2);
		createPatient(outDir, 3, t3);
		createPatient(outDir, 5, t5);
		createPatient(outDir, 6, t6);
		createPatient(outDir, 7, t7);
		createPatient(outDir, 8, t8);
//		createPatient(outDir, 9, t9);
		createPatient(outDir, 11, t11);

	}

	public static void createPatient(String outDir, int pNo,
			String[] timeGroupArray) {

		PatientInfo pInfo = new PatientInfo(outDir, pNo, timeGroupArray);
		pInfo.getSeqInfo();
		pInfo.generateXML();
	}

}
