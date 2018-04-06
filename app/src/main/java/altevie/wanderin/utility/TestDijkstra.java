package altevie.wanderin.utility;

import com.estimote.indoorsdk_module.cloud.LocationPosition;

import java.util.ArrayList;

import altevie.wanderin.utility.graphDec.AdjacencyListGraph;
import altevie.wanderin.utility.graphDec.AdjacencyListGraph.DecEdge;
import altevie.wanderin.utility.graphDec.AdjacencyListGraph.DecVertex;
import altevie.wanderin.utility.graphDec.Vertex;
import altevie.wanderin.utility.iterator.Iterator;
import altevie.wanderin.utility.set.SortedListSet;

public class TestDijkstra 
{	
	public static void main(String[] args) {
		TestDijkstra test = new TestDijkstra();
//		test.testDijkstra();
		test.testDijkstra2();
	}

	private void testDijkstra() 
	{
		this.riempiGrafo();
		D = new Dijkstra(graph, sorgente);
		D.execute();
		stampaAlbero();
	}


	private void testDijkstra2()
	{
		this.riempiGrafo2();
		D = new Dijkstra(graph, sorgente, destinazione);
		D.execute();
		stampaAlbero();
		g = D.percorsoMinimo();
		this.stampaPercorsoMinimo();
	}

	private void stampaAlbero()
	{
		SortedListSet set = new SortedListSet();
		Iterator it = graph.vertices();
		while(it.hasNext()){
			DecVertex v = (DecVertex) it.next();
			String vStr = (String) v.element();
			String uStr = (String) graph.getDecoration(v, "PREDECESSORE");
			
			Iterator it2 = v.incidentEdges();
			String w = "not found";
			while(it2.hasNext())
			{
				DecEdge e = (DecEdge) it2.next();
				if(graph.opposite(v, e).element().equals(uStr))
					w = e.element().toString();
			}
			
			set.insertElement("[("+uStr+","+vStr+") w:"+w+"]");
		}
		
		System.out.println(set.toString());
	}

	private void stampaPercorsoMinimo()
	{
		SortedListSet s = new SortedListSet();
		Iterator it = g.vertices();
		while(it.hasNext()){
			DecVertex v = (DecVertex) it.next();
			String vStr = (String) v.element();
			String uStr = (String) g.getDecoration(v, "PREDECESSORE");

			Iterator it2 = v.incidentEdges();
			String w = "not found";
			while(it2.hasNext())
			{
				DecEdge e = (DecEdge) it2.next();
				if(g.opposite(v, e).element().equals(uStr))
					w = e.element().toString();
			}

			s.insertElement("[("+uStr+","+vStr+") w:"+w+"]");
		}

		System.out.println(s.toString());
	}
	private void riempiGrafo()
	{
		graph = new AdjacencyListGraph();
		// vertici
		sorgente = graph.insertVertex("a");
		Vertex b = graph.insertVertex("b");
		Vertex c = graph.insertVertex("c");
		Vertex d = graph.insertVertex("d");
		Vertex e = graph.insertVertex("e");
		Vertex f = graph.insertVertex("f");
		Vertex g = graph.insertVertex("g");
		Vertex h = graph.insertVertex("h");
		graph.insertEdge(sorgente,b, 4);
		graph.insertEdge(sorgente,h, 8);
		graph.insertEdge(b,c, 8);
		graph.insertEdge(b,h, 11);
		graph.insertEdge(c,d, 7);
		graph.insertEdge(c,f, 4);
		graph.insertEdge(c,h, 6);
		graph.insertEdge(d,e, 9);
		graph.insertEdge(d,f, 14);
		graph.insertEdge(e,f, 10);
		graph.insertEdge(f,g, 2);
		graph.insertEdge(g,h, 1);
	}
	private void riempiGrafo2()
	{
		graph = new AdjacencyListGraph();
		// vertici
		sorgente = graph.insertVertex("a");
		Vertex b = graph.insertVertex("b");
		Vertex c = graph.insertVertex("c");
		Vertex d = graph.insertVertex("d");
		destinazione = graph.insertVertex("e");
		Vertex f = graph.insertVertex("f");
		Vertex g = graph.insertVertex("g");
		Vertex h = graph.insertVertex("h");
		graph.insertEdge(sorgente,b, 4);
		graph.insertEdge(sorgente,h, 8);
		graph.insertEdge(b,c, 8);
		graph.insertEdge(b,h, 11);
		graph.insertEdge(c,d, 7);
		graph.insertEdge(c,f, 4);
		graph.insertEdge(c,h, 6);
		graph.insertEdge(d,destinazione, 9);
		graph.insertEdge(d,f, 14);
		graph.insertEdge(destinazione,f, 10);
		graph.insertEdge(f,g, 2);
		graph.insertEdge(g,h, 1);
	}
	private Vertex sorgente ;
	private Vertex destinazione ;
	private Dijkstra D;
	private AdjacencyListGraph graph;
	private AdjacencyListGraph g;
}
