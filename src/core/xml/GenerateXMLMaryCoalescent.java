package core.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import XMLUtil.XMLTemplate;
import dr.evolution.alignment.Alignment;
import dr.evolution.datatype.Nucleotides;
import dr.evolution.io.FastaImporter;

public class GenerateXMLMaryCoalescent {

	public GenerateXMLMaryCoalescent() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		String pwd = "/home/sw167/Postdoc/Project_MaryDeBruijn/MaxLikelihoodSet/H7_forward/";
//		String tempaletfile = pwd+"zcoalesceTemplate.xml";
		String tempaletfile = pwd+"H7_50.60.back.xml";

//		String inDir = "/home/sw167/workspace/ABI/simData/" + HIndex + "Data/";
		// String inDir = "/home/sw167/workspace/ABI/simData/Run0603/";

		XMLTemplate template = new XMLTemplate(tempaletfile);
//		template.getTemplate();

		try {
			File fpwd = new File(pwd);
			FilenameFilter filter = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".fasta");

				}
			};
			
			String[] fileList = fpwd.list(filter);
			
			for (int i = 0; i < fileList.length; i++) {
				String prefix = fileList[i].replace(".fasta", "");
				String xmlFile = pwd+"xml"+File.separatorChar+prefix+File.separatorChar;
				
				Path xmlDir = Paths.get(xmlFile);
				if (!Files.exists(xmlDir)){
					Files.createDirectories(xmlDir);
				}
				xmlFile += 	prefix + ".xml";
				
				String alignmentFile = pwd+fileList[i];
				BufferedReader in = new BufferedReader(new FileReader(alignmentFile));
				FastaImporter importer = new FastaImporter(in,Nucleotides.INSTANCE);
				Alignment alignment = importer.importAlignment();
				in.close();
				
				template.parseSequenceAlignment(alignment);
				

				PrintWriter xmlOut = new PrintWriter(new BufferedWriter(new FileWriter(xmlFile)));

				String xmlString = template.generateXMLFile(prefix);
				xmlOut.println(xmlString);
				xmlOut.close();
				System.out.println(i+"\tconverted "+prefix+" to "+xmlFile);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
 
	}

}