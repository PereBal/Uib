package com.example.vullfugir;

import java.util.LinkedList;

public class Node implements Comparable<Node> {
	int x;
	int y;

	float dinstanciaDeLinici;
	float heuristica_DistanciaObjectiu;
	Node nodePare;

	Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.dinstanciaDeLinici = Integer.MAX_VALUE;
		heuristica_DistanciaObjectiu = 0;
	}

	Node(int x, int y, Node nodePare) {
		this.x = x;
		this.y = y;
		this.nodePare = nodePare;
		this.dinstanciaDeLinici = this.nodePare.dinstanciaDeLinici + 1;
		heuristica_DistanciaObjectiu = 0;
	}

	Node(int x, int y, int distanciaDeLinici) {
		this.x = x;
		this.y = y;
		this.dinstanciaDeLinici = distanciaDeLinici;
		heuristica_DistanciaObjectiu = 0;
	}

	@Override
	public boolean equals(Object node) // Per comparar dos nodes, mira les
										// coordenades x i y
	{
		if (node == null) {
			return false;
		}
		return ((Node) node).x == this.x && ((Node) node).y == this.y;
	}

	public Node getNodePare() {
		return nodePare;
	}

	public void setNodePrevi(Node nodePare) {
		this.nodePare = nodePare;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	// retorna -1, 0, o 1 depenent de si és el node1 és menor que el node2,
	// igual o major
	public int compareTo(Node node2) {
		final float d2 = node2.dinstanciaDeLinici
				+ node2.heuristica_DistanciaObjectiu;
		final float d1 = this.dinstanciaDeLinici;
		if (d1 > d2) {
			return 1;
		} else if (d1 < d2) {
			return -1;
		}
		return 0;
	}

	// Se podria mejorar, por ahora la dejo asi
	private void assignaHeuristica(boolean virtuals, GraellaJoc graella) {
		Punt p;

		p = (new Punt(this.x, this.y));
		if (graella.obstaclesVirtuals.contains(p)) {
			if (virtuals) {
				this.heuristica_DistanciaObjectiu -= 1;

			} else {
				this.heuristica_DistanciaObjectiu += 2;
			}
		}
		if (graella.obstacles.contains(p)) {
			this.heuristica_DistanciaObjectiu -= 1;
		}
		this.heuristica_DistanciaObjectiu += this.dinstanciaDeLinici * 0.8;

		this.heuristica_DistanciaObjectiu += Math.min(Math.min(this.x, this.y),
				Math.min((graella.CASELLES_PER_FILA - this.x),
						(graella.CASELLES_PER_COLUMNA - this.y))) * 1.5;
	}

	public Node[] veinats(GraellaJoc graella, boolean virtuals) {
		Node[] veinats = { null, null, null, null, null, null };
		Node n;
		int i = 0;

		for (Direccio d : Direccio.values()) {
			if (d != Direccio.C) // ignora la meva posició
			{
				Punt p = d.coordenadesVeinat(new Punt(x, y));
				if (!graella.obstacles.contains(p)
						&& (!graella.obstaclesVirtuals.contains(p) || !virtuals)) {

					(n = new Node(p.x, p.y, this)).assignaHeuristica(virtuals,
							graella);
					veinats[i++] = n;
				}
			}
		}
		return veinats;
	}

	// inserció ordenada sense repetir estats
	public static void addicioOrdenada(LinkedList<Node> pnd,
			LinkedList<Node> exam, Node[] fills) {
		boolean hasNext;
		java.util.ListIterator<Node> it = pnd.listIterator();
		Node e;

		for (Node f : fills) {
			if (f == null) {
				return;
			}
			do {
				if (!(hasNext = it.hasNext())) {
					// aquesta senyora comprovació que elimina estats repetits
					// sembla accelerar-ho tot d'una manera bestial
					if (!exam.contains(f)) {
						it.add(f);
					}
				} else {
					e = it.next();
					if (e.compareTo(f) > 0) {
						it.previous();
						// idem al cas anterior
						if (!e.equals(f) && !exam.contains(f)) {
							it.add(f);
						}
					}
				}
			} while (hasNext);
		}
	}

	public double distancia(Node p) {
		return Util.distancia(new Punt(p.getX(), p.getY()), new Punt(this.x,
				this.y));
	}

	public double distancia(Punt p) {
		return Util.distancia(p, new Punt(this.x, this.y));
	}

	@Override
	public String toString() {
		return "Node{x: " + x + ",y: " + y + ",dist: " + dinstanciaDeLinici
				+ '}';
	}
}