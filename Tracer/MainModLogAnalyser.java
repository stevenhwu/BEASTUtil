/*******************************************************************************
 * MainAnalysisResultHPD.java
 * 
 * This file is part of BIDE-2D
 * 
 * Copyright (C) 2012 Steven Wu
 * 
 * BIDE-2D is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BIDE-2D is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BIDE-2D.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package Tracer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;


import dr.inference.loggers.TabDelimitedFormatter;
import dr.inference.trace.MarginalLikelihoodAnalysis;
import dr.inference.trace.TraceAnalysis;
import dr.inference.trace.TraceCorrelation;
import dr.inference.trace.TraceDistribution;
import dr.inference.trace.TraceException;
import dr.inference.trace.TraceFactory.TraceType;
import dr.inference.trace.TraceList;

public class MainModLogAnalyser {
	
	public static void main(String[] args) throws IOException, TraceException {
	
		String cwd = System.getProperty("user.dir")+File.separator;
//		cwd = "/home/sw167/workspace/BEASTUtil/data/";
		cwd = "/home/sw167/PostdocLarge/BEASTRun/Project_OrigFungi/MultiRegion_R5";
		File fCwd = new File(cwd);
		int burnin = -1;
        boolean hpds = true;
        boolean ess = true;
        boolean stdErr = true;
        boolean verbose = false;
        String marginalLikelihood = null;
        String[] summaryVariable = new String[] {"posterior", "tmrca(RhidGloi)"};
        
        
//	    for (File f : files) {
		StringBuilder sb = new StringBuilder();
		analyze(fCwd, burnin, verbose, new boolean[] { true }, hpds, ess,
				stdErr, marginalLikelihood, summaryVariable, sb);
		// }
		
		String outfile = "/home/sw167/PostdocLarge/BEASTRun/Project_OrigFungi/summaryOutput";
		PrintWriter out
		   = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
		out.println(sb.toString());
		out.close();
		
		System.err.println(sb.toString());
    	System.err.println(count);
    }	
	static int count = 0;
	public static StringBuilder analyze(File file, int burnin, boolean verbose, boolean[] drawHeader,
	                     boolean hpds, boolean ess, boolean stdErr,
	                     String marginalLikelihood, String[] summaryVariable,
	                     StringBuilder sb) throws TraceException {
	
//		StringBuilder sb = new StringBuilder();
	    if (file.isFile()) {
	    	if (file.getName().endsWith(".log") ) {
		        try {
		            String name = file.getCanonicalPath();
//		            System.out.println(name);
		            count++;
		            if (verbose) {
		                report(name, burnin, marginalLikelihood);
		            } else {
						String out = shortReport(name, burnin, drawHeader[0], hpds, ess,
								stdErr, marginalLikelihood, summaryVariable);
//						System.err.println(out.length() +"\t"+out);
						if(out.length()!=0){
							sb.append(out);
							drawHeader[0] = false;
						}
						 
		            }
		        } catch (IOException e) {
	            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		        }
	    	}
	    } else {
	        File[] files = file.listFiles();
	        for (File f : files) {
	            if (f.isDirectory()) {
					analyze(f, burnin, verbose, drawHeader, hpds, ess, stdErr,
							marginalLikelihood, summaryVariable, sb);
				} else if (f.getName().endsWith(".log") || f.getName().endsWith(".p")) {
					analyze(f, burnin, verbose, drawHeader, hpds, ess, stdErr,
							marginalLikelihood, summaryVariable, sb);
				} else {
	                if (verbose) System.out.println("Ignoring file: " + f);
	            }
	        }
	    }
//	    System.err.println(sb.toString());
	    return sb;
	}




	public static String shortReport(String filename, final int burnin,
			boolean drawHeader, boolean hpds, boolean individualESSs,
			boolean stdErr, String likelihoodName, String[] summaryVariables) throws java.io.IOException,
			TraceException {

		StringBuilder sb = new StringBuilder();
		TraceList traces = analyzeLogFile(filename, burnin);
		if(traces != null){
			int maxState = traces.getMaxState();
			
			double minESS = Double.MAX_VALUE;
			if (drawHeader) {
				sb.append("file\t");
				System.out.print("file\t");
				for (int i = 0; i < traces.getTraceCount(); i++) {
					String traceName = traces.getTraceName(i);
					
					if (ArrayUtils.contains(summaryVariables, traceName) ){
						sb.append(traceName + "\t");
						System.out.print(traceName + "\t");
						if (stdErr)
							sb.append(traceName + " stdErr\t");
							System.out.print(traceName + " stdErr\t");
						if (hpds) {
							sb.append(traceName + " hpdLower\t");
							sb.append(traceName + " hpdUpper\t");
							System.out.print(traceName + " hpdLower\t");
							System.out.print(traceName + " hpdUpper\t");
						}
						if (individualESSs) {
							sb.append(traceName + " ESS\t");
							System.out.print(traceName + " ESS\t");
						}
					}
				}
				sb.append("minESS\t");
				System.out.print("minESS\t");
				if (likelihoodName != null) {
					System.out.print("marginal likelihood\t");
					System.out.print("stdErr\t");
				}
				sb.append("chainLength\n");
				System.out.println("chainLength");
			}
			sb.append(filename + "\t");
			System.out.print(filename + "\t");
			for (int i = 0; i < traces.getTraceCount(); i++) {
				// TraceDistribution distribution =
				// traces.getDistributionStatistics(i);
				String traceName = traces.getTraceName(i);
//				System.out.println(traceName +"\t"+ Arrays.toString(summaryVariables));
				if (ArrayUtils.contains(summaryVariables, traceName) ){
					TraceCorrelation distribution = traces.getCorrelationStatistics(i);
					sb.append(distribution.getMean() + "\t");
					System.out.print(distribution.getMean() + "\t");
					if (stdErr)
						sb.append(distribution.getStdErrorOfMean() + "\t");
						System.out.print(distribution.getStdErrorOfMean() + "\t");
					if (hpds) {
						sb.append(distribution.getLowerHPD() + "\t");
						sb.append(distribution.getUpperHPD() + "\t");
						System.out.print(distribution.getLowerHPD() + "\t");
						System.out.print(distribution.getUpperHPD() + "\t");
					}
					if (individualESSs) {
						sb.append(distribution.getESS() + "\t");
						System.out.print(distribution.getESS() + "\t");
					}
					double ess = distribution.getESS();
					if (ess < minESS) {
						minESS = ess;
					}
				}
			}
			sb.append(minESS + "\t");
			System.out.print(minESS + "\t");
	
			if (likelihoodName != null) {
				int traceIndex = -1;
				for (int i = 0; i < traces.getTraceCount(); i++) {
					String traceName = traces.getTraceName(i);
					if (traceName.equals(likelihoodName)) {
						traceIndex = i;
						break;
					}
				}
	
				if (traceIndex == -1) {
					throw new TraceException("Column '" + likelihoodName
							+ "' can not be found in file " + filename + ".");
				}
	
				String analysisType = "aicm";
				int bootstrapLength = 1000;
	
				List<Double> sample = traces.getValues(traceIndex);
	
				MarginalLikelihoodAnalysis analysis = new MarginalLikelihoodAnalysis(
						sample, traces.getTraceName(traceIndex), burnin,
						analysisType, bootstrapLength);
	
				System.out.print(analysis.getLogMarginalLikelihood() + "\t");
				System.out.print(analysis.getBootstrappedSE() + "\t");
			}
			sb.append(maxState).append("\n");
			System.out.println(maxState);
		}
		else{
//			System.err.println(filename);
		}
		
//		System.err.println(":"+ sb.toString()+":");
		return sb.toString();
	}
	
    private static TraceList analyzeLogFile(String fileName, int burnin) throws TraceException, IOException {

        File file = new File(fileName);
        LogFileTraces traces = new LogFileTraces(fileName, file);
        boolean pass = traces.loadTraces();
        System.err.println(fileName);
//        System.err.println(pass);
        if(pass){
	        traces.setBurnIn(burnin);
	        
	        for (int i = 0; i < traces.getTraceCount(); i++) {
	            traces.analyseTrace(i);
	        }
	        
	        return traces;
        }
        else{
        	return null;
        }
	}




	public static TraceList report(String fileName, int burnin, String likelihoodName) throws java.io.IOException, TraceException {
        return report(fileName, burnin, likelihoodName, true);
    }
    public static TraceList report(String fileName, int inBurnin, String likelihoodName, boolean withStdError)
            throws java.io.IOException, TraceException {

//        int fieldWidth = 14;
//        int firstField = 25;
//        NumberFormatter formatter = new NumberFormatter(4);
//        formatter.setPadding(true);
//        formatter.setFieldWidth(fieldWidth);

        File file = new File(fileName);

        LogFileTraces traces = new LogFileTraces(fileName, file);
//        if (traces == null) {
//            throw new TraceException("Trace file is empty.");
//        }
        traces.loadTraces();

//        traces.addTrace("R0", traces.getTraceIndex("bdss.psi"));

        int burnin = inBurnin;
        if (burnin == -1) {
            burnin = traces.getMaxState() / 10;
        }

        traces.setBurnIn(burnin);

//        System.out.println();
        System.out.println("burnIn   <= " + burnin + ",   maxState  = " + traces.getMaxState());
//        System.out.println();

        System.out.print("statistic");
        String[] names;

        if (!withStdError)
            names = new String[]{"mean", "hpdLower", "hpdUpper", "ESS"};
        else
            names = new String[]{"mean", "stdErr", "median", "hpdLower", "hpdUpper", "ESS", "50hpdLower", "50hpdUpper"};

        for (String name : names) {
            System.out.print("\t" + name);
        }
        System.out.println();

        int warning = 0;
        for (int i = 0; i < traces.getTraceCount(); i++) {
            traces.analyseTrace(i);
            TraceDistribution distribution = traces.getDistributionStatistics(i);

            double ess = distribution.getESS();
            System.out.print(traces.getTraceName(i));
            System.out.print("\t" + formattedNumber(distribution.getMean()));

            if (withStdError) {
                System.out.print("\t" + formattedNumber(distribution.getStdError()));
                System.out.print("\t" + formattedNumber(distribution.getMedian()));
            }

            System.out.print("\t" + formattedNumber(distribution.getLowerHPD()));
            System.out.print("\t" + formattedNumber(distribution.getUpperHPD()));

            System.out.print("\t" + formattedNumber(ess));
            
            if (withStdError) {
                System.out.print("\t" + formattedNumber(distribution.getHpdLowerCustom()));
                System.out.print("\t" + formattedNumber(distribution.getHpdUpperCustom()));
            }

            if (ess < 100) {
                warning += 1;
                System.out.println("\t" + "*");
            } else {
                System.out.println("\t");
            }
        }
        System.out.println();

        if (warning > 0) {
            System.out.println(" * WARNING: The results of this MCMC analysis may be invalid as ");
            System.out.println("            one or more statistics had very low effective sample sizes (ESS)");
        }

        if (likelihoodName != null) {
            System.out.println();
            int traceIndex = -1;
            for (int i = 0; i < traces.getTraceCount(); i++) {
                String traceName = traces.getTraceName(i);
                if (traceName.equals(likelihoodName)) {
                    traceIndex = i;
                    break;
                }
            }

            if (traceIndex == -1) {
                throw new TraceException("Column '" + likelihoodName +
                        "' can not be found for marginal likelihood analysis.");
            }

            String analysisType = "aicm";
            int bootstrapLength = 1000;

            List<Double> sample = traces.getValues(traceIndex);

            MarginalLikelihoodAnalysis analysis = new MarginalLikelihoodAnalysis(sample,
                    traces.getTraceName(traceIndex), burnin, analysisType, bootstrapLength);

            System.out.println(analysis.toString());
        }

        System.out.flush();
        return traces;
    }

    public static String formattedNumber(double value) {
        DecimalFormat formatter = new DecimalFormat("0.####E0");
        DecimalFormat formatter2 = new DecimalFormat("####0.####");

        if (value > 0 && (Math.abs(value) < 0.01 || Math.abs(value) >= 100000.0)) {
            return formatter.format(value);
        } else return formatter2.format(value);
    }
    
    @Deprecated
	private static void calHPD(String cwd, String[] filenameTamplate, int start, int end) {
		
		try {
			FileWriter fout = new FileWriter(cwd+"ABC_result.tab");
			fout.write("data\tmuLowel\tmuUpper\tmuTF\tpopLower\tpopUpper\tpopTF\tthetaLower\tthetaUpper\tthetaTF\n");
			
			int count[] = new int[NAMES.length];
			Arrays.fill(count, 0);
			for (int i = start; i <= end; i++) {
				fout.write(""+i);
				String filename = filenameTamplate[0]+i+filenameTamplate[1];
				count = readLogFile(cwd, filename, count, fout);
			}
			System.out.println(Arrays.toString(count));
			
			fout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Deprecated
	private static int[] readLogFile(String cwd, String logFileName, int[] count, FileWriter fout) {

		String logFile = cwd+logFileName;
		System.out.println("File:\t" + logFile);
		try {

			BufferedReader in = new BufferedReader(new FileReader(logFile));
//			FileWriter fout = new FileWriter(logFile+"_result.tab");
			String input = in.readLine();
			String name = in.readLine();//.split("\t");
			
			StringTokenizer token = new StringTokenizer(name);

			int noParamInLogFile = token.countTokens()-1;
			ArrayList<Double>[] dists = new ArrayList[NAMES.length];
			for (int i = 0; i < dists.length; i++) {
				dists[i] = new ArrayList<Double>();
			}
			
			while ((input = in.readLine()) != null) {
				token = new StringTokenizer(input);
				token.nextToken(); //skip noIte

				double theta = 1;
				for (int i = 0; i < noParamInLogFile; i++) {
					double temp = Double.parseDouble(token.nextToken());
					theta *= temp;
					dists[i].add(temp)	;
				}
				dists[noParamInLogFile].add(theta);  

			}
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < dists.length; i++) {
				double[] values = Doubles.toArray( dists[i]);
				
				double burnin = 0.1;
				int start = (int) (values.length * burnin);
				double[] newValues = Arrays.copyOfRange(values, start, values.length);
				
				TraceDistribution td = new TraceDistribution(Arrays.asList(newValues), TraceType.DOUBLE);
				double hpdlower = td.getLowerHPD();
				double hpdupper = td.getUpperHPD();
				double ess = td.getESS();
				
				boolean isGood = checkHPD(i, hpdlower, hpdupper); 
				if(isGood){
					count[i]++;
				}
				sb.append(ess).append("\t").append(hpdlower).append("\t").append(hpdupper).
					append("\t").append(isGood).append("\t");
			
			}
			
			sb.append("\n");
			fout.write(sb.toString());
//			fout.flush();
			
			in.close();
//			fout.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Deprecated
	private static boolean checkHPD(int index, double hpdlower, double hpdupper) {
//		index == 0 mu
//		index == 1 popsize
//		index == 2 theta

		boolean isGood = false;
		switch(index){
			case 0:
				isGood = hpdlower < 1e-5 & hpdupper > 1e-5; 
//				System.out.println(index +"\t"+ hpdlower +"\t"+ (hpdlower < 1e-5) +"\t"+  hpdupper +"\t"+  (hpdupper > 1e-5) +"\t"+ isGood);
				break;
			case 1:
				isGood = hpdlower < 3000 & hpdupper > 3000;
//				System.out.println(index +"\t"+ hpdlower +"\t"+ (hpdlower < 3000) +"\t"+  hpdupper +"\t"+  (hpdupper > 3000) +"\t"+ isGood);
				break;
			case 2:
				isGood = hpdlower < 0.03 & hpdupper > 0.03;
//				System.out.println(index +"\t"+ hpdlower +"\t"+ (hpdlower < 3e-2) +"\t"+  hpdupper +"\t"+  (hpdupper > 3e-2) +"\t"+ isGood);
				break;
		}

		return isGood;
				
		
	}

	@Deprecated
	public void oldTestingMethod() throws IOException, TraceException{
		String cwd = System.getProperty("user.dir")+File.separator;
		cwd = "/home/sw167/workspace/BEASTUtil/data/";

		dr.inference.trace.LogFileTraces analyzeLogFile = TraceAnalysis.analyzeLogFile(cwd+"testTracer_0.log", 1000);
		System.out.println(analyzeLogFile.getTraceCount());
		int traceIndex = analyzeLogFile.getTraceIndex("mu");
//		for (int i = 0; i < analyzeLogFile.getTraceCount(); i++) {
		TraceDistribution ds = analyzeLogFile.getDistributionStatistics(traceIndex);
		System.out.println(analyzeLogFile.getTraceName(traceIndex));
		System.out.println(ds.getLowerHPD()+"\t"+ ds.getUpperHPD());
//		}
//		System.out.println(analyzeLogFile.getCorrelationStatistics(0).getLowerCPD());
//		System.out.println(distributionStatistics.getLowerCPD());
//		String[] filenameTamplate = {"testTracer_",".log"};
//		calHPD(cwd, filenameTamplate, 0, 1);
		
	}
	@Deprecated
	static String[] NAMES = {"mu", "popsize", "theta"};

}
