package core.xml.Srp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;

import XMLUtil.Importer;
import XMLUtil.XMLTemplate;

import dr.evolution.alignment.Alignment;
import dr.evolution.datatype.Nucleotides;
import dr.evolution.io.FastaImporter;
import dr.inference.loggers.TabDelimitedFormatter;

public class generateXMLforSrp {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String HIndex = "H12_";
		String outDir = "/home/sw167/workspace/ABI/BEAST/Run0603_H12/";
		String tempaletfile = "/home/sw167/workspace/ABI/BEAST/"+HIndex+"Srp_Template.xml";

		String inDir = "/home/sw167/workspace/ABI/simData/"+HIndex+"Data/";
//		String inDir = "/home/sw167/workspace/ABI/simData/Run0603/";

		XMLTemplate template = new XMLTemplate(tempaletfile);
//		template.getTemplate();	

		try {
			
			for (int i = 0; i < 100; i++) {
				String subDir = HIndex+i+"/";
				String prefix = HIndex+i+"_Srp_fullHaplotype";
				String alignmentFile = inDir+subDir+prefix+".fasta";
				String xmlFile = outDir+prefix+".xml";
//				System.out.println(alignmentFile);
				System.out.println(xmlFile);
				
				BufferedReader in = new BufferedReader(new FileReader(alignmentFile));
				FastaImporter importer = new FastaImporter(in, Nucleotides.INSTANCE);
				Alignment alignment = importer.importAlignment();
				

				PrintWriter xmlOut
				   = new PrintWriter(new BufferedWriter(new FileWriter(xmlFile)));
				 
				
				
//				Alignment ali = new Importer(aliFile, NO_SEQ ).importAlignment();		
//
				
				xmlOut.println(template.getSection(0));

				for (int j = 0; j < alignment.getTaxonCount(); j++) {
//					System.out.println(j +"\t"+ alignment.getTaxonId(j) +"\t"+
//							alignment.getSequence(j).getSequenceString());

					xmlOut.println("<sequence>\n<taxon idref=\""+alignment.getTaxonId(j)+"\"/>");
					xmlOut.println(alignment.getSequence(j).getSequenceString());
					xmlOut.println("</sequence>");

				}
				String outString = template.parseOutFileNames(prefix);
				xmlOut.println(outString);
				xmlOut.close();


			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}



}
