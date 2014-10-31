package core.xml.BCC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import XMLUtil.XMLTemplateTime;
import dr.evolution.alignment.Alignment;
import dr.evolution.io.NexusImporter;

public class BEASTforBCC {

	private static int NOSEQ = 120;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String outDir = "/home/steven/temp/newXML/";
		String inDir = "/home/steven/temp/SiteFreq_BEAST_RESULT/";
		String tempfiles = "/home/steven/temp/newSet/CPo3Ti40S400_0.pau.xml";
	

		XMLTemplateTime template = new XMLTemplateTime(tempfiles);
//		template.getTemplateMultiFiles();	

		try {
//			int i=0;
			for (int i = 0; i < 100; i++) {
//			{
				String prefix = "CPo3Ti40S400_"+i;
				String aliFile = inDir+prefix+".pau";
				String xmlFile = outDir+prefix+".xml";
				
//				Alignment ali = new Importer(aliFile, NOSEQ ).importAlignment();		
				BufferedReader in = new BufferedReader(new FileReader(aliFile));
				NexusImporter importer = new NexusImporter(in);
				Alignment alignment = importer.importAlignment();

				template.parseSequenceAlignment_customizeTime(alignment);

				PrintWriter xmlOut = new PrintWriter(new BufferedWriter(new FileWriter(xmlFile)));

				String xmlString = template.generateXMLFile(prefix);

				xmlOut.println(xmlString);
				xmlOut.close();
				System.out.println(i+"\tconverted "+prefix+" to "+xmlFile);

				
//				 PrintWriter xout
//				   = new PrintWriter(new BufferedWriter(new FileWriter(xmlFile)));
//				
//				xout.println(template.getSection(0));
//				int time= 0;
//				int seqCount;
//				for (int j = 0; j < NOSEQ; j++) {
//					seqCount = j+1;
//					if(seqCount <41){
//						time = 0;
//					}
//					else if(seqCount > 40 & seqCount< 81){
//						time = 400;
//					}
//					else if(seqCount > 80){
//						time = 800;
//					}
//					xout.println("<sequence>\n<taxon idref=\"1."+seqCount+"."+time+"."+0+"\"/>");
//					xout.println(ali.getSequence(0).getSequenceString());
//					xout.println("</sequence>");
//
//				}
//				xout.print(template.getSection(1));
//				xout.print("CPo3Ti40S400_"+i+".log");
//				xout.print(template.getSection(2));
//				xout.print("CPo3Ti40S400_"+i+".trees");
//				xout.print(template.getSection(3));
//				xout.close();


			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}



}
