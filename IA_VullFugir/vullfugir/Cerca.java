package com.example.vullfugir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Cerca {

	/*
	 * S'empra una llista/cua doblement enllaçada enlloc d'un ArrayList per
	 * motius d'eficiencia. 1. No te el problema de les còpies quan s'està
	 * acabant l'espai reservat 2. Les insercions ordenades són O(n) enlloc de
	 * O(n+n) (n per trobar la posició on afegir l'element i n per desplaçar
	 * tots els elements posteriors i deixar lloc al nou elem) 3. En cap cas es
	 * necessita un accés O(1) a un element (excepte al primer i al darrer, cosa
	 * que la llista també garanteix), per tant, l'avantatge més important dels
	 * ArrayList no és tal.
	 */
	private LinkedList<Node> exam; // llista de nodes examinats
	private GraellaJoc graella;
	double minDist;
	Punt minPath;
	Node minNd;

	Cerca(GraellaJoc graella) {
		this.graella = graella;
		exam = new LinkedList<Node>();
	}

	public Node calculaCasella(Punt inici) {
		graella.obstaclesVirtuals = Collections
				.synchronizedList(new ArrayList<Punt>(150));
		Node nextNode;
		Node n;
		LinkedList<Node> pnd; // llista de pendents
		minDist = Double.MAX_VALUE;
		minPath = null;
		minNd = null;

		if (graella.noPucMoure(inici)) {
			return null;
		}

		obstaclesVirtuals(graella.obstacles, inici);
		pnd = new LinkedList<Node>();
		nextNode = new Node(inici.x, inici.y, 0);
		pnd.add(nextNode);

		do {

			n = pnd.removeFirst();
			if (graella.isASolution(n.x, n.y)) {
				break;
			}
			if (minPath != null && n.x == minPath.x && n.y == minPath.y) {
				minNd = n;
			}
			exam.add(n);
			Node.addicioOrdenada(pnd, exam, n.veinats(graella, true));

		} while (!pnd.isEmpty());

		// Si no hemos llegado hasta el final de la cuadricula, tenemos que
		// buscar el camino mas corto sin tener
		// en cuenta las piedras virtuales
		if (!graella.isASolution(n.x, n.y)) {
			n = shortestPath(inici);
		}

		// Si despues de hacer el camino con y sin las piedras virtuales, no
		// hemos llegado hasta el final, el bitxo esta encerrado
		if (!graella.isASolution(n.x, n.y)) {
			return null;
		}

		while (n.nodePare != null) {
			graella.afegirNodeSolucio(new Punt(n.x, n.y));
			nextNode = n;
			n = n.nodePare;
		}
		if (nextNode.getX() == inici.x && nextNode.getY() == inici.y)
			return null;

		return nextNode;
	}

	private void obstaclesVirtuals(List<Punt> obstacles, Punt bitxo) {
		double distBitxoP1, distBitxoP2, distPunts;
		for (int i = 0; i < obstacles.size(); i++) {
			distBitxoP1 = bitxo.distancia(obstacles.get(i));
			for (int j = 0; j < obstacles.size(); j++) {
				distBitxoP2 = bitxo.distancia(obstacles.get(j));
				distPunts = obstacles.get(i).distancia(obstacles.get(j));

				if (distPunts < Math.max(distBitxoP1, distBitxoP2)
						&& obstacles.get(i) != obstacles.get(j)) {
					addVirtuals(obstacles.get(i), obstacles.get(j), bitxo);
				}
			}
		}

	}

	private void addVirtuals(Punt p1, Punt p2, Punt bitxo) {
		int x1, x2, y1, y2, dx;

		// El incremento de las coordenadas Y sigue un patron diferente cuando
		// y1>y2 y cuando y2>y1
		// Por eso nos basamos en las coordenadas y.
		if (p1.y > p2.y) {
			x1 = p2.x;
			y1 = p2.y;
			x2 = p1.x;
			y2 = p1.y;
		} else {
			x1 = p1.x;
			y1 = p1.y;
			x2 = p2.x;
			y2 = p2.y;
		}
		if (x1 < x2) {
			dx = 1;
		} else {
			dx = -1;
		}
		while (x1 != x2 && y1 != y2) {

			if (x1 % 2 == 0) {
				y1++;
			}
			x1 += dx;
			if (!graella.obstaclesVirtuals.contains(new Punt(x1, y1))
					&& !graella.obstacles.contains(new Punt(x1, y1))
					&& !bitxo.equals(new Punt(x1, y1))) {
				setDistMinPath(p1, p2, new Punt(x1, y1), bitxo);
				graella.obstaclesVirtuals.add(new Punt(x1, y1));
			}

		}
		while (x1 != x2) {
			x1 += dx;
			if (!graella.obstaclesVirtuals.contains(new Punt(x1, y1))
					&& !graella.obstacles.contains(new Punt(x1, y1))
					&& !bitxo.equals(new Punt(x1, y1))) {

				setDistMinPath(p1, p2, new Punt(x1, y1), bitxo);
				graella.obstaclesVirtuals.add(new Punt(x1, y1));
			}
		}
		while (y1 != y2) {
			y1++;
			if (!graella.obstaclesVirtuals.contains(new Punt(x1, y1))
					&& !graella.obstacles.contains(new Punt(x1, y1))
					&& !bitxo.equals(new Punt(x1, y1))) {
				setDistMinPath(p1, p2, new Punt(x1, y1), bitxo);
				graella.obstaclesVirtuals.add(new Punt(x1, y1));
			
			}

		}
		

	}

	private Node shortestPath(Punt p) {
		Node nextNode;
		Node n;
		LinkedList<Node> pnd; // llista de pendents
		LinkedList<Node> examinats = new LinkedList<Node>();
		if (minNd == null) {
			nextNode = new Node(p.x, p.y, 0);
			// n = nextNode;
		} else {
			nextNode = minNd;
		}
		pnd = new LinkedList<Node>();
		pnd.add(nextNode);
		do {
			n = pnd.removeFirst();
			if (graella.isASolution(n.x, n.y)) {
				break;
			}
			examinats.add(n);
			Node.addicioOrdenada(pnd, examinats, n.veinats(graella, false));
		} while (!pnd.isEmpty());

		// }

		return n;
	}

	private void setDistMinPath(Punt obst1, Punt obst2, Punt p, Punt bitxo) {
		final double x = p.x, y = p.y;
		double dist = p.distancia(bitxo);
		double disto2 = obst2.distancia(p), disto1 = obst1.distancia(p);
		double prop = disto2 / (disto2 + disto1);
		dist = Math.min(Math.min(x, y), Math.min(
				(graella.CASELLES_PER_FILA - x),
				(graella.CASELLES_PER_COLUMNA - y))); // Distancia minima hasta
														// las fronteras

		if (dist < minDist && disto1 > 0 && disto2 > 0
				&& (disto1 + disto1) * 0.75 < dist && prop > 0.45
				&& prop < 0.55) {
			System.out.println("MINDIST ::> " + minDist + "   " + dist);
			minDist = dist;
			minPath = p;
		}
	}
}