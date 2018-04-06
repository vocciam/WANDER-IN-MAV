package altevie.wanderin.utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.estimote.indoorsdk_module.cloud.LocationPosition;

import java.util.ArrayList;

import altevie.wanderin.utility.graphDec.AdjacencyListGraph;
import altevie.wanderin.utility.graphDec.Vertex;

/**
 * Created by mvoccia on 30/03/2018.
 */

public class ClsGraph {

    public ClsGraph(){
        super();
    }

    public AdjacencyListGraph calcGraph(LocationPosition locationPosition, ArrayList<AdjacencyListGraph.DecVertex> listHashMapPon, AdjacencyListGraph graph){

//definire destinazione

        double distance_min = Double.POSITIVE_INFINITY;
        double distance = 0;
        double x_s = locationPosition.getX();
        double y_s = locationPosition.getY();

        AdjacencyListGraph g = new AdjacencyListGraph();
        AdjacencyListGraph.DecVertex v_min = g.new DecVertex("SORGENTE");
        //v_min.put("LAT", x_s);
        //v_min.put("LON",y_s);

        java.util.Iterator<AdjacencyListGraph.DecVertex> it = listHashMapPon.iterator();

        while(it.hasNext()){
            AdjacencyListGraph.DecVertex vertice = it.next();
            double x = Double.parseDouble((String)vertice.get("LAT"));
            double y = Double.parseDouble((String)vertice.get("LON"));
            distance = Math.hypot(x_s - x, y_s - y);

            if (distance < distance_min){
                v_min = vertice;
                distance_min = distance;
            }
        }

        Log.i("VERTICE:",v_min.toString());
        Log.i("DISTANZA DA SORGENTE:",""+distance_min);

        v_min.remove("LAT");
        v_min.remove("LON");

        AdjacencyListGraph graph2 = new AdjacencyListGraph();

        graph2 = graph;

        altevie.wanderin.utility.iterator.Iterator vv = graph2.vertices();
        String ss= "", tot = "";
        while(vv.hasNext()){
            ss = vv.next().toString();
            tot = tot + ss  ;
            ss = "";
        }
        altevie.wanderin.utility.iterator.Iterator ee = graph2.edges();
        String sse= "", tote = "";
        while(ee.hasNext()){
            sse = ee.next().toString();
            tote = tote + sse  ;
            sse = "";
        }

        Log.i("Graph_CLS-vertici:",tot);
        Log.i("Graph_CLS-archi:",tote);

        Vertex v = graph2.insertVertex("SORGENTE");
        sorgente = (AdjacencyListGraph.DecVertex) v;

        graph2.insertEdge(v,v_min, distance_min);

        vv = graph2.vertices();
        ss= ""; tot = "";
        while(vv.hasNext()){
            ss = vv.next().toString();
            tot = tot + ss  ;
            ss = "";
        }
        ee = graph2.edges();
        sse= ""; tote = "";
        while(ee.hasNext()){
            sse = ee.next().toString();
            tote = tote + sse  ;
            sse = "";
        }

        Log.i("Graph_CLS-vert_dopoIns:",tot);
        Log.i("Graph_CLS-arch_dopoIns:",tote);

        return graph2;
    }

    public AdjacencyListGraph calculateDijkstra(AdjacencyListGraph graph, Vertex sorgente, Vertex destinazione){
        D = new Dijkstra(graph, sorgente, destinazione);
        D.execute();
        Log.i("grafo_dopo_dec",graph.toString());

        AdjacencyListGraph g = D.percorsoMinimo();
        //AdjacencyListGraph g = new AdjacencyListGraph();
        return g;
    }

    public AdjacencyListGraph.DecVertex calculateDestination(AdjacencyListGraph graph2, ArrayList<AdjacencyListGraph.DecVertex> listHashMapPoi, int i){
        AdjacencyListGraph.DecVertex ver = listHashMapPoi.get(i);

        Log.i("DESTINAZIONE:",ver.toString());

        ver.remove("LON");
        ver.remove("LAT");
        ver.remove("NAME");

        Log.i("DESTINAZIONE:",ver.toString());

        altevie.wanderin.utility.iterator.Iterator ite = graph2.vertices();
        while (ite.hasNext()) {
            AdjacencyListGraph.DecVertex decV = (AdjacencyListGraph.DecVertex) ite.next();
            if(ver.toString().substring(0,16).equals(decV.toString().substring(0,16))){
                this.setDestinazione(decV);
            }
        }
        return this.destinazione;
    }

    public void printGraph(AdjacencyListGraph gr, Context context ){
        altevie.wanderin.utility.iterator.Iterator vv = gr.vertices();
        String ss= "", tot = "";
        while(vv.hasNext()){
            ss = vv.next().toString();
            tot = tot + ss  ;
            ss = "";
        }
        altevie.wanderin.utility.iterator.Iterator ee = gr.edges();
        String sse= "", tote = "";
        while(ee.hasNext()){
            sse = ee.next().toString();
            tote = tote + sse  ;
            sse = "";
        }
        //
        Toast.makeText(context, "Grafo caricato correttamente - size:" + gr.numVertices(), Toast.LENGTH_LONG).show();
        Log.i("GraphDj-vertici:",tot);
        Log.i("GraphDj-archi:",tote);

    }

    public AdjacencyListGraph.DecVertex getSorgente(){
        return this.sorgente;
    }

    public AdjacencyListGraph.DecVertex getDestinazione(){
        return this.destinazione;
    }

    public void setDestinazione(Vertex destinazione){
        this.destinazione = (AdjacencyListGraph.DecVertex)destinazione;
    }

    private Dijkstra D;
    private AdjacencyListGraph.DecVertex sorgente;
    private AdjacencyListGraph.DecVertex destinazione;
}
