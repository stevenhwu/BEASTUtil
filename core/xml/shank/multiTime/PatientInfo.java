package core.xml.shank.multiTime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

public class PatientInfo {
	
	static ArrayList<StringBuilder> template;

	
	String[] timeGroupArray;
	String outDir;
	int pNo;

	ArrayList<String> taxon;
	HashMap<String, String> seq;// = new HashMap<String, String>();
	HashMap<String, String> time;// = new HashMap<String, String>();
	ArrayList<ArrayList<Integer>> timeGroup;// = new ArrayList[12];
	
	public PatientInfo(String pDir, int pNo, String[] timeGroupArray2) {

		this.pNo = pNo;
		this.outDir = pDir += "P"+pNo+"/";
		this.timeGroupArray = timeGroupArray2;
		

		taxon = new ArrayList<String>();
		seq = new HashMap<String, String>();
		time = new HashMap<String, String>();
		timeGroup = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i < timeGroupArray.length; i++) {
			timeGroup.add(new ArrayList<Integer>());
		}

	}


	public void getSeqInfo() {

		
		try {
			String line, name;
			int start, end;
			int nameLength = (""+pNo).length()+3;
			String file = outDir+"ShankarappaP"+pNo+"Master.xml";
			BufferedReader in = new BufferedReader(new FileReader(file));

			while ((line = in.readLine()) != null) {
				if (line.contains("<taxa id=\"taxa\">")) {
					break;
				}
			}
			while ((line = in.readLine()) != null) {

				if (line.contains("<taxon id=")) {
					start = line.indexOf("\"") + 1;
					end = line.indexOf("\"", start);
					name = line.substring(start, end);
					taxon.add(name);

					line = in.readLine();
					start = line.indexOf("\"") + 1;
					end = line.indexOf("\"", start);
					time.put(name, line.substring(start, end));
				}
				if (line.contains("<alignment id=\"alignment\" dataType=\"nucleotide\">")) {
					break;
				}
			}
			while ((line = in.readLine()) != null) {

				if (line.contains("<taxon idref=")) {
					start = line.indexOf("\"") + 1;
					end = line.indexOf("\"", start);
					name = line.substring(start, end);

					line = in.readLine();
					seq.put(name, line);
				}
				if (line.contains("</alignment>")) {
					break;
				}
			}
			in.close();
			for (int i = 0; i < taxon.size(); i++) {
				name = taxon.get(i);
				int ind = ArrayUtils.indexOf(timeGroupArray,
						name.substring(0, nameLength) );
				if (ind == -1) {
					System.out.println(pNo + "\t" + i + "\t" + name + "\t"
							+ time.get(name) + "\t" + ind);
				}
				timeGroup.get(ind).add(i);
				

			}
			int q = 0;
			for (int i = 0; i < timeGroup.size(); i++) {
//				System.out.println(timeGroup.get(i).toString());
//				for (int j : timeGroup.get(i)) {
//					name = taxon.get(j);
//					System.out.println(q + "\t" + name + "\t" + time.get(name)
//							+ "\t");
//					q++;
//
//				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public void generateXML() {
	
		String name;
		StringBuilder t1Taxon = new StringBuilder();
		StringBuilder t1Seq = new StringBuilder();
		
		for (int j : timeGroup.get(0)) {
			name = taxon.get(j);
			t1Taxon.append("<taxon id=\"" + name + "\">\n");
			t1Taxon.append("\t<date value=\"" + time.get(name)
					+ "\" direction=\"forwards\" units=\"months\"/>\n");
			t1Taxon.append("</taxon>\n");
	
			t1Seq.append("<sequence>\n");
			t1Seq.append("\t<taxon idref=\"" + name + "\"/>\n");
			t1Seq.append("\t" + seq.get(name) + "\n");
			t1Seq.append("</sequence>\n");
		}
	
		for (int i = 1; i < timeGroup.size(); i++) {
			try {
				String code = "ShankP"+pNo+"T1T" + (i + 1);
				String xmlFile = outDir + code + ".xml";
				System.out.println(xmlFile);
				PrintWriter xout = new PrintWriter(new BufferedWriter(
						new FileWriter(xmlFile)));
	
				// xout.println(template.getSection(0));
	
				xout.println("<?xml version=\"1.0\" standalone=\"yes\"?>\n");
				xout.println("<beast>\n");
	
				xout.println("<taxa id=\"taxa\">");
				xout.println(t1Taxon.toString());
				for (int j : timeGroup.get(i)) {
					name = taxon.get(j);
					xout.println("<taxon id=\"" + name + "\">");
					xout.println("\t<date value=\"" + time.get(name)
							+ "\" direction=\"forwards\" units=\"months\"/>");
					xout.println("</taxon>");
				}
				xout.println("</taxa>");
	
				xout.println("<alignment id=\"alignment\" dataType=\"nucleotide\">");
				xout.println(t1Seq.toString());
				for (int j : timeGroup.get(i)) {
					name = taxon.get(j);
					xout.println("<sequence>");
					xout.println("\t<taxon idref=\"" + name + "\"/>");
					xout.println(seq.get(name));
					xout.println("</sequence>");
				}
				xout.println("</alignment>");
	
				// <sequence>
				// <taxon idref="9V03X01"/>
				// GAAGAAGAGGTAGTAATTAGATCTGAAAATTTCACGGACAATGCTAAAACCATAATAGTACAGCTGAATGAATCgGTAGTAATTAATTGTACAAGACCCAACAACAATACAAGAAAAAGTATA---------CAtATAGGACCgGGGAGAGCATggTATACAACAGGAGAAATAATAGGA---GATATAAGACAAGCACATTGTAACCTTAGTgaAGCAAAtTGGAATAAagCTTTAGAACAGATAGTTAAAAAATTAAaAGAACAATTTGGGaat---AAAACA---------------------------ATAGTCTTTAATCAATCCTCAGGAGGGGACCCAGAAATTGTAATGCACAGTTTTAATTGTGGAGGGGAATTTTTCTACTGTgATaCAACAaAGCTGTTT------------------AATAGTACTTGGaATgtTAcTAgTacttgg----------------------------aatgcAAataac---a--------------------------------atagaAcTATCAC---ACTCCCATGCAGAATAAAACAAATTATAAACAGGTGGCAGGAAGTAGGgAAAGCAATGTATGCCCCTCCCATCAGAGGACAAATTAGATGT-TCATCAAATATTACAGGGCTGCTATTAACAAGAGATGGTGGTActaacagga--------------------gC-ga---gccc---------------GAggT-CTTCAGACCTGGA---
				// </sequence>
	
				xout.println(template.get(0));
				xout.println("<log id=\"fileLog\" logEvery=\"1000\" fileName=\""
						+ code + ".log\" overwrite=\"false\">");
				xout.println(template.get(1));
				xout.println("<logTree id=\"treeFileLog\" logEvery=\"1000\" nexusFormat=\"true\" fileName=\""
						+ code + ".trees\" sortTranslationTable=\"true\">");
				xout.println(template.get(2));
	
				xout.close();
	
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		}
	
	}


	public static void getTemplate(String outDir) {
		
		template = new ArrayList<StringBuilder>();
		try {

			for (int i = 0; i < 3; i++) {
				String s = outDir + "ShankarappaTemplate_" + i + ".xml";

				String line;
				StringBuilder sbi = new StringBuilder();
				BufferedReader in = new BufferedReader(new FileReader(s));

				while ((line = in.readLine()) != null) {
					sbi.append(line).append("\n");
				}
				sbi.delete(sbi.length() - 1, sbi.length());
				template.add(sbi);
				// System.out.println(sbi.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
