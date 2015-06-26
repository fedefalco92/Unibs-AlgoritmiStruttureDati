package it.univr.esempio;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Arco a = new Arco();
	    Grafo g = new Grafo();
	    g.add("a","b",new Integer(1));
	    g.add("a","c",new Integer(1));
	    g.add("a","e",new Integer(3));
	    g.add("c","d",new Integer(4));
	    g.add("c","e",new Integer(2));
	    g.add("b","d",new Integer(3));

	    System.out.println("Il grafo G e':\n" + g);
	    System.out.println("L'insieme di archi e': " + g.getEdgeSet());
	}

}
