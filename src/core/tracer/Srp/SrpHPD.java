package core.tracer.Srp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import dr.inference.trace.LogFileTraces;
import dr.inference.trace.TraceAnalysis;
import dr.inference.trace.TraceDistribution;
import dr.inference.trace.TraceException;

public class SrpHPD {

	/**
	 * @param args
	 * @throws TraceException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, TraceException {
		
//		Run0606_H7/H7_99_Srp_fullHaplotype.log
		BEAST("/home/sw167/workspace/ABI/BEAST/Run0606_H12/","BEAST0606_H12.summary");
//		Srp0606H7();
	}

	private static void Srp0606H7() throws IOException, TraceException{

		String cwd = "/home/sw167/workspace/ABI/simData/Run0606_H7/allLog/";
		String resultFileName = cwd+"Srp_0606_H7.summary";
		String[] paramName = {"populationSize", "coalescent"};  
		String[] labels = {"_ESS", "_Mean", "_LHPD", "_UHPD"};
//		String[] allLabels = new String[paramName.length*labels.length];
		
		int[] burnin = {0, 150000000, 50000000, 50000000, 150000000, 50000000, 50000000, 0, 50000000, 250000000, 50000000, 150000000, 0, 50000000, 0, 150000000, 50000000, 400000000, 50000000, 350000000, 0, 300000000, 50000000, 50000000, 300000000, 50000000, 50000000, 60000000, 50000000, 50000000, 0, 100000000, 0, 50000000, 400000000, 350000000, 50000000, 50000000, 360000000, 0, 50000000, 50000000, 350000000, 50000000, 200000000, 50000000, 50000000, 0, 0, 50000000, 150000000, 50000000, 150000000, 50000000, 60000000, 50000000, 50000000, 300000000, 50000000, 50000000, 50000000, 50000000, 0, 50000000, 50000000, 50000000, 0, 50000000, 50000000, 0, 50000000, 50000000, 50000000, 300000000, 200000000, 200000000, 50000000, 50000000, 50000000, 60000000, 250000000, 0, 0, 50000000, 50000000, 50000000, 250000000, 0, 0, 150000000, 50000000, 0, 0, 0, 150000000, 50000000, 0, 50000000, 60000000, 50000000};
		
		StringBuffer sb = createStringBufferLabels("logIndex\tposterior_ESS\t", paramName, labels);
		
		for (int logIndex = 0; logIndex < 100; logIndex++) {
			String logFile = cwd+"FullTree_H7_"+logIndex+".log";
			
			sb = summariseLog(sb, logIndex, logFile, burnin[logIndex], paramName);
		}
//		System.out.println(sb.toString());

		PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter(resultFileName)));
		fout.println(sb.toString());
		fout.close();
		
	}

	private static StringBuffer summariseLog(StringBuffer sb, int logIndex,
			String logFile, int burnin, String[] paramName) throws IOException, TraceException {
		
		LogFileTraces analyzeLogFile = TraceAnalysis.analyzeLogFile(logFile, burnin);
//		System.out.println(analyzeLogFile.get);
		sb.append("log_").append(logIndex).append("\t");
		int traceIndex = analyzeLogFile.getTraceIndex("posterior");
		TraceDistribution ds = analyzeLogFile.getDistributionStatistics(traceIndex);
		sb.append(ds.getESS());
		for (int i = 0; i < paramName.length; i++) {
			
			traceIndex = analyzeLogFile.getTraceIndex(paramName[i]);
			ds = analyzeLogFile.getDistributionStatistics(traceIndex);
			sb.append("\t").append(ds.getESS()).append("\t").append(ds.getMean()).append("\t")
					.append(ds.getLowerHPD()).append("\t").append(ds.getUpperHPD());

		}
	
		sb.append("\n");
		return sb;
		
	}

	private static void Srp() throws IOException, TraceException{

		String cwd = "/home/sw167/workspace/ABI/simData/Run0603/";
		String[] paramName = {"TREE_HEIGHT", "populationSize", "kappa", "coalescent"};  
		
		String[] labels = {"_ESS", "_Mean", "_LHPD", "_UHPD"};
		String[] allLabels = new String[paramName.length*labels.length];
		StringBuffer sb = new StringBuffer("logIndex\tposterior_ESS\t");
		for (int i = 0; i < paramName.length; i++) {
			for (int j = 0; j < labels.length; j++) {
				sb.append(paramName[i]).append(labels[j]).append("\t");
			}
		}
		sb.append("\n");
		
		for (int l = 0; l < 100; l++) {
 
			LogFileTraces analyzeLogFile = TraceAnalysis.analyzeLogFile(cwd+"H7_"+l+"/FullTree_H7_"+l+".log", 25_000_000);
//			System.out.println(analyzeLogFile.get);
			sb.append("log_").append(l).append("\t");
			int traceIndex = analyzeLogFile.getTraceIndex("posterior");
			TraceDistribution ds = analyzeLogFile.getDistributionStatistics(traceIndex);
			sb.append(ds.getESS());
			for (int i = 0; i < paramName.length; i++) {
				
				traceIndex = analyzeLogFile.getTraceIndex(paramName[i]);
				ds = analyzeLogFile.getDistributionStatistics(traceIndex);
				sb.append("\t").append(ds.getESS()).append("\t").append(ds.getMean()).append("\t")
						.append(ds.getLowerHPD()).append("\t").append(ds.getUpperHPD());
	
//				System.out.println(ds.getESS() +"\t"+ ds.getMean() +"\t"+ ds.getLowerHPD()+"\t"+ ds.getUpperHPD());
			}
		
			sb.append("\n");
		}
		
		System.out.println(sb.toString());
		
		PrintWriter fout = new PrintWriter(new BufferedWriter(
				new FileWriter(cwd+"Srp_0603.summary")));
		fout.println(sb.toString());
		fout.close();
		
	}
	private static void BEAST(String cwd, String resultFileName) throws IOException, TraceException{
//		Run0606_H7/H7_9_Srp_fullHaplotype.log
		String[] paramName = {"constant.popSize", "coalescent"};
		String[] labels = {"_ESS", "_Mean", "_LHPD", "_UHPD"};
		String[] allLabels = new String[paramName.length*labels.length];
		StringBuffer sb = createStringBufferLabels("logIndex\tposterior_ESS\t", paramName, labels);
		int burnin = 100000;
		for (int logIndex = 0; logIndex < 100; logIndex++) {
			String logFile = cwd+"H12_"+logIndex+"_Srp_fullHaplotype.log";
			
			sb = summariseLog(sb, logIndex, logFile, burnin, paramName);
		}
		
		PrintWriter fout = new PrintWriter(new BufferedWriter(
				new FileWriter(cwd+resultFileName)));
		fout.println(sb.toString());
		fout.close();
		
	}

	private static StringBuffer createStringBufferLabels(String prefix,
			String[] paramName, String[] labels) {
		StringBuffer sb = new StringBuffer(prefix);
		for (int i = 0; i < paramName.length; i++) {
			for (int j = 0; j < labels.length; j++) {
				sb.append(paramName[i]).append(labels[j]).append("\t");
			}
		}
		sb.append("\n");
		
		return sb;
	}
	
}
