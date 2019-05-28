package edu.yc.oats.algs;

import java.util.ArrayList;
public class Digraph {
	
	
	private final int V; //vertices
	private int E;      // edges
	private ArrayList<Integer>[] adj; // order does not matter
	
	public Digraph(int V) {
		this.V = V;
		this.E = 0;
	    adj = (ArrayList<Integer>[])new ArrayList[V];		
	    for (int v = 0; v < V; v++) {
			adj[v] = new ArrayList<Integer>();
		}
		
	}
	public int V() {
		return V;
	}
	
	public int E() {
		return E;
	}
	
	public void addEdge(int v, int w) {
		adj[v].add(w);
		E++;
	}
	
	public Iterable<Integer> adj(int v){
		return adj[v];	
	}
	
	public Digraph reverse() {
		Digraph R = new Digraph(V);
		for(int v =0; v < V; v++ ) {
			for(int w:adj(v))
				R.addEdge(w, v);
			
		}
		return R;
		
	}
	
	
	
	public static void main(String[] args) {
		Digraph d = new Digraph(20);
		d.addEdge(4, 2);
		d.addEdge(2, 3);
		d.addEdge(3, 2);	
		d.addEdge(6, 0);
		d.addEdge(0, 1);
		d.addEdge(2, 0);
		d.addEdge(11, 12);
		d.addEdge(12, 9);
		d.addEdge(9, 10);	
		d.addEdge(7, 9);
		d.addEdge(10, 12);
		d.addEdge(11, 4);
		d.addEdge(4, 3);
		d.addEdge(3, 5);	
		d.addEdge(6, 8);
		d.addEdge(8, 6);
		d.addEdge(5, 4);
		d.addEdge(0, 5);
		d.addEdge(6, 4);
		d.addEdge(6, 9);	
		d.addEdge(9, 11);
		d.addEdge(7, 6);
		d = d.reverse();
		System.out.println(d.V());
		Iterable<Integer> line1 = d.adj(9);
		for(int x: line1) {
			System.out.println(x);
		}
		
		
	}

}
