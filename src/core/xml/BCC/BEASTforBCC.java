package core.xml.BCC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import XMLUtil.Importer;
import XMLUtil.XMLTemplate;

import dr.evolution.alignment.Alignment;

public class BEASTforBCC {

	private static int NOSEQ = 120;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String outDir = "/home/sw167/PostdocLarge/BEASTRun/CPo3Ti40S/";
		String tempfiles = outDir+"CPo3Ti";

		String inDir = "/home/sw167/Postdoc/Project_EPSF/simData/CPo3Ti40S400/";

		XMLTemplate template = new XMLTemplate(tempfiles);
		template.getTemplateMultiFiles();	

		try {
			
			for (int i = 0; i < 100; i++) {
				
				String aliFile = inDir+"CPo3Ti40S400_"+i+".pau";
				String xmlFile = outDir+"CPo3Ti40S400_"+i+".xml";
				
				Alignment ali = new Importer(aliFile, NOSEQ ).importAlignment();		

				 PrintWriter xout
				   = new PrintWriter(new BufferedWriter(new FileWriter(xmlFile)));
				
				xout.println(template.getSection(0));
				int time= 0;
				int seqCount;
				for (int j = 0; j < NOSEQ; j++) {
					seqCount = j+1;
					if(seqCount <41){
						time = 0;
					}
					else if(seqCount > 40 & seqCount< 81){
						time = 400;
					}
					else if(seqCount > 80){
						time = 800;
					}
					xout.println("<sequence>\n<taxon idref=\"1."+seqCount+"."+time+"."+0+"\"/>");
					xout.println(ali.getSequence(0).getSequenceString());
					xout.println("</sequence>");

				}
				xout.print(template.getSection(1));
				xout.print("CPo3Ti40S400_"+i+".log");
				xout.print(template.getSection(2));
				xout.print("CPo3Ti40S400_"+i+".trees");
				xout.print(template.getSection(3));
				xout.close();


			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}



}
