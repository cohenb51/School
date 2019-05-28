package edu.yc.oats.algs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TopologicalQ {

	private int[] indegree;
	private int[] outdegree;
	private Queue<Integer> sources;
	private ArrayList<Integer> sinks;
	private ArrayList<Integer> order;

	private boolean[] marked;
	private int[] edgeTo;
	private Stack<Integer> cycle;
	private boolean[] onStack;

	public TopologicalQ(Digraph G) {
		indegree = new int[G.V()];
		outdegree = new int[G.V()];
		sources = new LinkedList();
		sinks = new ArrayList<>();

		// cycle = new Stack<Integer>(); Check by seing if it's null (as opposed to
		// seing if its length is 0)
		marked = new boolean[G.V()];
		edgeTo = new int[G.V()];
		onStack = new boolean[G.V()];
		isOK(G);
		if (hasCycle()) {
			return;
		}
		order = new ArrayList<Integer>(); // declare here so don't need seperate line to return if null

		doTopologicalSort(G);

		int outdegreeCounter = 0;
		for (int v = 0; v < G.V(); v++) {
			int outDegreeCounter = 0;
			for (int g : G.adj(v)) {
				indegree[g]++;
				outDegreeCounter++;
			}
			outdegree[v] = outDegreeCounter;
		}
		for (int i = 0; i < indegree.length; i++) {
			if (indegree[i] == 0) {
				sources.add(i); // this is not reachable from any vertex since indegree = 0
			}
			if (outdegree[i] == 0) {
				sinks.add(i);
			}
		}
		doTopologicalSort(G);

	}

	private void doTopologicalSort(Digraph G) {
		while (sources.size() > 0) {
			Integer source = sources.remove();
			order.add(source);
			for (int g : G.adj(source)) {
				indegree[g]--;
				if (indegree[g] == 0) {
					sources.add(g);
				}

			}

		}
	}

	private void isOK(Digraph G) {

		for (int v = 0; v < G.V(); v++) {
			if (!marked[v])
				dfs(G, v);
			if (hasCycle()) {
				return; // we only care about 1 cycle
			}
		}

		return;
	}

	private void dfs(Digraph G, int v) {
		onStack[v] = true;
		marked[v] = true;
		for (int w : G.adj(v)) {
			if (this.hasCycle()) {
				return;
			} else if (!marked[w]) {
				edgeTo[w] = v;
				dfs(G, w);
			} else if (onStack[w]) {
				cycle = new Stack<Integer>();
				for (int x = v; x != w; x = edgeTo[x]) {
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(v);
			}

		}
		onStack[v] = false;

	}

	public boolean hasCycle() {
		return cycle != null;
	}

	public boolean hasOrder() {

		return !(hasCycle());
	}
	
	public boolean isDAG() {
		return hasOrder();
	}

	public Iterable<Integer> order() {
		return order;
	}

	public static void main(String[] args) {
		/*Digraph d = new Digraph(8);
		d.addEdge(0, 1); 
		d.addEdge(0, 7);
		d.addEdge(0, 4);
		d.addEdge(1, 7);
		d.addEdge(4, 7);
		d.addEdge(4, 5);
		d.addEdge(4, 6);
		d.addEdge(5, 2);
		d.addEdge(2, 3);
		d.addEdge(1, 3);
		d.addEdge(3, 6);
		d.addEdge(6, 3);

		TopologicalQ t = new TopologicalQ(d);
		Iterable<Integer> a = t.order();
		System.out.println(t.hasOrder());
		System.out.println(t.isDag());;

		for (int number : a) {
			System.out.print(number + " ");
		}
		*/

	}

}