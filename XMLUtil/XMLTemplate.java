package XMLUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class XMLTemplate {
	
	String filePre;
	ArrayList<StringBuilder> sb;
	
	public XMLTemplate(String s){
		filePre = s;
	}
	
	public void getTemplate(){
		sb = new ArrayList<StringBuilder>();
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(filePre));
			String line;
			StringBuilder sbi = new StringBuilder();
			
			
			while ( (line = in.readLine()) != null ) {
				
				if(line.equals("<!--TemplateSection-->")){
					sbi.delete(sbi.length()-1, sbi.length());
					sb.add(sbi);
					System.out.println(sbi.toString());
					System.out.println("========="+sb.size());
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
	public String getSection(int i){
		return sb.get(i).toString();
	}

	public String parseOutFileNames(String prefix) {
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

	@Deprecated
	public void getTemplateMultiFiles(){
		try {
			sb = new ArrayList<StringBuilder>();
			
			
			for (int i = 0; i < 4; i++) {
				String s = filePre+"_"+i+".xml";
			
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
}
