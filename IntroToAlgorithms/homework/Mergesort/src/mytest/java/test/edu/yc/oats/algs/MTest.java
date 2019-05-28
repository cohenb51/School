package test.edu.yc.oats.algs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.yc.oats.algs.*;



public class MTest {
	
	public static void random(Comparable[] a) {
		int i = 0;
		Random random = new Random();
		for(int iterator=0; i< a.length; i++) {
			int index = random.nextInt(a.length);
			Comparable temp = a[iterator];
			a[iterator] = a[index];
			a[index] = temp;
		}
			
		
	}

	public static void main(String[] args) {
		Mergesorter merge = MergeImplementations.MergesorterFactory(MergeImplementations.Merge);
		Mergesorter mergeX = MergeImplementations.MergesorterFactory(MergeImplementations.MergeX);
		Mergesorter parallel = MergeImplementations.MergesorterFactory(MergeImplementations.ParallelMerge);
		Mergesorter jdkMerge = MergeImplementations.MergesorterFactory(MergeImplementations.JDKParallelMerge);

		BufferedReader buffer;
		ArrayList<String> lineList = new ArrayList<>();
		String line;

		try {
			buffer = new BufferedReader(new FileReader("C:/Users/cohen/DataStructures/IntroToAlgorithms/homework/Mergesort/src/words.utf-8.txt"));
			while (true) {
				line = buffer.readLine();
				if (line == null) {
					break;
				} else {
					lineList.add(line);
				}
			}
			Comparable[] lineArr = lineList.toArray(new String[lineList.size()]);
			random(lineArr);

			//Comparable[] lineArr = {7,1,4,5,2,8,9};
			System.out.println(Runtime.getRuntime().availableProcessors());
			long startTime, endTime;
			ArrayList<Long> mTimes = new ArrayList<Long>();
			ArrayList<Long> mxTimes = new ArrayList<Long>();
			ArrayList<Long> JDKTimes = new ArrayList<Long>();
			ArrayList<Long> parallelTimes = new ArrayList<Long>();

			System.out.println("starting MergeTest");
			
			for (int i = 0; i < 50; i++) {
				Comparable[] testArr = lineArr.clone();

				//System.out.println(Arrays.toString(testArr));
				System.out.println(Merge.isSorted(testArr));
				startTime = System.currentTimeMillis();
				merge.sortIt(testArr);
				endTime = System.currentTimeMillis();
				//System.out.println(endTime - startTime);
				System.out.println(Merge.isSorted(testArr));
				if(i > 19) mTimes.add(endTime - startTime);
			}
			
			System.out.println("starting Mergex Test");

			for (int i = 0; i < 50; i++) {
				Comparable[] testArr = lineArr.clone();
				startTime = System.currentTimeMillis();
				System.out.println(Merge.isSorted(testArr));

				mergeX.sortIt(testArr);
				endTime = System.currentTimeMillis();
				//System.out.println(endTime - startTime);
				System.out.println(Merge.isSorted(testArr));

				if(i > 19) mxTimes.add(endTime - startTime);
			}
			
			System.out.println("starting JDKParallel Test");
			for (int i = 0; i < 50; i++) {
				Comparable[] testArr = lineArr.clone();
				startTime = System.currentTimeMillis();
				System.out.println(Merge.isSorted(testArr));

				jdkMerge.sortIt(testArr);
				endTime = System.currentTimeMillis();
				//System.out.println(endTime - startTime);
				System.out.println(Merge.isSorted(testArr));

				if(i > 19) JDKTimes.add(endTime - startTime);

			}
			
			System.out.println("Starting Parallel Merge Test");
			for (int i = 0; i < 50; i++) {
				Comparable[] testArr = lineArr.clone();
				System.out.println(Merge.isSorted(testArr));

				startTime = System.currentTimeMillis();
				parallel.sortIt(testArr);
				endTime = System.currentTimeMillis();
				//System.out.println(endTime - startTime);
				System.out.println(Merge.isSorted(testArr));

				if(i > 19) parallelTimes.add(endTime - startTime);

			}
		
		
			displayAVG(mTimes, mxTimes, JDKTimes, parallelTimes);
			

		} catch (FileNotFoundException e) {
			System.out.println("file not found");
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void displayAVG(ArrayList<Long> mTimes, ArrayList<Long> mxTimes, ArrayList<Long> jDKTimes, ArrayList<Long> ptTimes) {
		long sum = 0;
		System.out.println();
		for (int i = 0; i < mTimes.size(); i++) {
			sum += mTimes.get(i);
		}
		System.out.println("m times " + sum/mTimes.size());
		sum = 0;
		for (int i = 0; i < mxTimes.size(); i++) {
			sum += mxTimes.get(i);
		}
		System.out.println("mx Times" + sum/mxTimes.size());
		sum = 0;

		for (int i = 0; i < jDKTimes.size(); i++) {
			sum += jDKTimes.get(i);
		}
		System.out.println("JDKTimes" + sum/jDKTimes.size());
		sum = 0;
		if(ptTimes == null) {
			return;
		}
		for (int i = 0; i < ptTimes.size(); i++) {
			sum += ptTimes.get(i);
		}
		System.out.println("ptTimes" + sum/jDKTimes.size());
		
	}
	
	public static boolean isSorted(Comparable[] array) {
		for(int i = 1; i< array.length; i++) {
			if(array[i].compareTo(array[i-1]) >= 0) { // if the previous is greater than
				return false;
			}
		}
		return true;
		
		
	}

}