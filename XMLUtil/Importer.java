package XMLUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import dr.evolution.alignment.Alignment;
import dr.evolution.alignment.SimpleAlignment;
import dr.evolution.sequence.Sequence;


public class Importer {

	File aliFile;
	private int noSeq;
	
	public Importer(String f, int noS) {
		aliFile = new File(f);
		noSeq = noS;
	}
	
	

	public Alignment importAlignment() {//throws Exception{

		SimpleAlignment sa = new SimpleAlignment();
		try {
			String line;
			BufferedReader br = 
				new BufferedReader (new FileReader(aliFile));

			while ( (line = br.readLine().trim()) != null) {	
				if(line.equals("matrix")){break;}

			}
			while ((line = br.readLine()) != null) {

				if(line.contains(";")){break;}
				Sequence s = new Sequence(br.readLine());
				sa.addSequence(s);
				if(sa.getSequenceCount()==noSeq){
					break;
				}

			}
			br.close();

			if(sa.getSequenceCount() != noSeq){
				System.out.println(sa.getSequenceCount() +"\t"+ noSeq+" return null");
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sa;		
	}
}