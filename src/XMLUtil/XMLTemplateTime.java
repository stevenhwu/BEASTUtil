package XMLUtil;

import dr.evolution.alignment.Alignment;

public class XMLTemplateTime extends XMLTemplate {

	public XMLTemplateTime(String templateFileName) {
		super(templateFileName);
		// TODO Auto-generated constructor stub
	}
	

	public void parseSequenceAlignment_customizeTime(Alignment alignment) {
		
		sbTaxaAlignment = new StringBuilder();
		StringBuilder tempAlignment = new StringBuilder();

		sbTaxaAlignment.append("\n");
		double time = 0;
		for (int j = 0; j < alignment.getTaxonCount(); j++) {
			
			if(j <40){
				time = 0;
			}
			else if(j >= 40 & j< 80){
				time = 400;
			}
			else if(j >= 80){
				time = 800;
			}

			
			sbTaxaAlignment.append("\t\t<taxon id=\"")
					.append(alignment.getTaxonId(j)).append("\">\n")
					.append("\t\t\t<date value=\"").append(time).append("\" direction=\"backwards\" units=\"days\"/>\n")
					.append("\t\t</taxon>\n");
					
			tempAlignment.append("\t\t<sequence>\n\t\t\t<taxon idref=\"")
					.append(alignment.getTaxonId(j)).append("\"/>\n\t\t\t")
					.append(alignment.getSequence(j).getSequenceString())
					.append("\n\t\t</sequence>\n");
	
		}
		sbTaxaAlignment.append("\t</taxa>\n\n")
			.append("\t<alignment id=\"alignment\" dataType=\"nucleotide\">\n")
			.append(tempAlignment);

		
	}

}
