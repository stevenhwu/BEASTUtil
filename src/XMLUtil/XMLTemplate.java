package XMLUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javafx.scene.control.Tab;

import org.apache.commons.lang3.text.StrBuilder;

import dr.evolution.alignment.Alignment;

public class XMLTemplate {
	
	private String templateFileName;
	private String prefix;
	
	private ArrayList<StringBuilder> sb;
	protected StringBuilder sbTaxaAlignment;
	
	@Deprecated
	private String templateFileNamePrefix;
	private boolean isOperatorAnalysis;
	
	public XMLTemplate(String templateFileName){
		this.templateFileName = templateFileName;
//		getTemplate();
		parseXML();
		
//		System.out.println(sb.get(0).toString());
//		System.out.println("\n===================\n");
//		System.out.println(sb.get(1).toString());
//		System.out.println("\n===================\n");
//		System.out.println(sb.get(2).toString());
//		System.out.println("\n===================\n");
//		System.out.println(sb.get(3).toString());
//		System.out.println("\n===================\n");
//		System.out.println(sb.get(4).toString());
//		System.out.println("\n===================\n");
		
		
	}
	
	private void parseXML(){
		sb = new ArrayList<StringBuilder>();
		
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(templateFileName));
			String line;
			StringBuilder sbi = new StringBuilder();
			
			
			while ( (line = in.readLine()) != null ) {
				
				if(line.startsWith("\t<taxa id=")){

					sbi.append(line);
					sb.add(sbi);
					sbi = new StringBuilder();
					do{
						line = in.readLine();
					}while(!line.contains("</alignment>"));
					sbi.append(line);

				}
				
//				<mcmc id="mcmc" chainLength="100000000" autoOptimize="true" operatorAnalysis="
				else if(line.startsWith("\t<mcmc id=")){
					String delimiter = "operatorAnalysis=\"";
					int index = line.indexOf(delimiter);
					if(index != -1){
						isOperatorAnalysis=true;
						index += + delimiter.length();
						sbi.append( line.subSequence(0, index) );
						sb.add(sbi);
						
						sbi = new StringBuilder();
						int index2 = line.indexOf("\"",index);
						sbi.append(line.subSequence(index2, line.length())).append("\n");
					}
					else{
						isOperatorAnalysis=false;
						sbi.append(line).append("\n");
						sb.add(sbi);
						sbi = new StringBuilder();
					}
				}
//				<log id="fileLog" logEvery="10000" fileName="
				else if(line.startsWith("\t\t<log id=\"fileLog\"")){
					String delimiter = "fileName=\"";
					int index = line.indexOf(delimiter) + delimiter.length();
					sbi.append( line.subSequence(0, index) );
					sb.add(sbi);
					
					sbi = new StringBuilder();
					int index2 = line.indexOf("\"",index);
					sbi.append(line.subSequence(index2, line.length())).append("\n");
				}
				else if(line.startsWith("\t\t<logTree id=")){
					String delimiter = "fileName=\"";
					int index = line.indexOf(delimiter) + delimiter.length();
					sbi.append( line.subSequence(0, index) );
					sb.add(sbi);
					
					sbi = new StringBuilder();
					int index2 = line.indexOf("\"",index);
					sbi.append(line.subSequence(index2, line.length())).append("\n");
				}
				else{
					sbi.append(line).append("\n");
				}

			}
			sbi.delete(sbi.length()-1, sbi.length());
			sb.add(sbi);
			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void parseSequenceAlignment(Alignment alignment) {
	
		sbTaxaAlignment = new StringBuilder();
		StringBuilder tempAlignment = new StringBuilder();

//		<taxa id="taxa">
//		<taxon id="99_148_1200"/>
//		<taxon id="154_148_1200"/>
//		<taxon id="152_148_1200"/>
//		<taxon id="101_148_1200"/>
//		<taxon id="103_148_1200"/>
//		<taxon id="106_148_1200"/>
//		<taxon id="126_148_1200"/>
//		<taxon id="127_148_1200"/>
//	</taxa>
		sbTaxaAlignment.append("\n");
		for (int j = 0; j < alignment.getTaxonCount(); j++) {
			sbTaxaAlignment.append("\t\t<taxon id=\"")
					.append(alignment.getTaxonId(j))
					.append("\"/>\n");
			tempAlignment.append("\t\t<sequence>\n\t\t\t<taxon idref=\"")
					.append(alignment.getTaxonId(j)).append("\"/>\n\t\t\t")
					.append(alignment.getSequence(j).getSequenceString())
					.append("\n\t\t</sequence>\n");
	
		}
		sbTaxaAlignment.append("\t</taxa>\n\n")
			.append("\t<alignment id=\"alignment\" dataType=\"nucleotide\">\n")
			.append(tempAlignment);
		
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		
	}

	public String generateXMLFile(String prefix) {
		if(sbTaxaAlignment == null){
			System.err.println("No sequences");
			System.exit(-1);
		}
		if(prefix == null){
			System.err.println("prefix for the results not set");
			System.exit(-1);
		}
		StringBuffer sb = new StringBuffer();
		sb.append(getSection(0));
		sb.append(sbTaxaAlignment);
		sb.append(getSection(1));
		if(isOperatorAnalysis){
			sb.append(prefix+".ops");
		}
		sb.append(getSection(2));
		sb.append(prefix+".log");
		sb.append(getSection(3));
		sb.append(prefix+".trees");
		sb.append(getSection(4));
		return sb.toString();
	}

	public String getSection(int i){
		return sb.get(i).toString();
	}

	@Deprecated
	private void getTemplate(){
		sb = new ArrayList<StringBuilder>();
		
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(templateFileName));
			String line;
			StringBuilder sbi = new StringBuilder();
			
			
			while ( (line = in.readLine()) != null ) {
				
				if(line.equals("<!--TemplateSection-->")){
					sbi.delete(sbi.length()-1, sbi.length());
					sb.add(sbi);
//					System.out.println(sbi.toString());
//					System.out.println("========="+sb.size());
					sbi = new StringBuilder();
				}
				else{
					sbi.append(line).append("\n");
				}

			}
			sbi.delete(sbi.length()-1, sbi.length());
			sb.add(sbi);
			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public void getTemplateMultiFiles(){
		try {
			sb = new ArrayList<StringBuilder>();
			
			
			for (int i = 0; i < 4; i++) {
				String s = templateFileNamePrefix+"_"+i+".xml";
			
				String line;
				StringBuilder sbi = new StringBuilder();
				BufferedReader in = new BufferedReader(new FileReader(s));
	
				while ( (line = in.readLine()) != null ) {
					sbi.append(line).append("\n");
				}
				sbi.delete(sbi.length()-1, sbi.length());
				sb.add(sbi);
			System.out.println(sbi.toString());
				in.close();
			}	
			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deprecated
	public String parseOutFileNames(String prefix) {
		if(sbTaxaAlignment == null){
			System.err.println("No sequences");
			System.exit(-1);
		}
		StringBuffer sb = new StringBuffer();
		
		sb.append(getSection(1));
		sb.append(prefix+".ops");
		sb.append(getSection(2));
		sb.append(prefix+".log");
		sb.append(getSection(3));
		sb.append(prefix+".trees");
		sb.append(getSection(4));
		return sb.toString();
	}

}
