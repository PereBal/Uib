package com.example.vullfugir;

// Simplement per mirar si un número és parell o senar (comprobant el bit menys significatiu)
// operador "&" and bit a bit

public class Util {
	public static boolean isEven(int number) {
		return (number & 1) == 0;
	}

	public static boolean isOdd(int number) {
		return (number & 1) == 1;
	}

	// La distancia no sale del todo bien...
	public static double distancia(Punt p1, Punt p2) {
		//
		// final double x1 = p1.x, y1 = p1.y;
		// final double x2 = p2.x, y2 = p2.y;
		// double dx;
		// double dy;
		// if (x1 == x2) {
		// return (Math.abs(y1 - y2));
		// } else if (y1 == y2) {
		// return (Math.abs(x1 - x2));
		// } else {
		// dx = Math.abs(x1 - x2);
		// dy = Math.abs(y1 - y2);
		// if (x1 < x2) {
		// return dx + dy + Math.ceil(dx / 2);
		// } else {
		// return dx + dy + Math.floor(dx / 2);
		// }
		// }

		final double x1 = p1.x;
		final double x2 = p2.x;
		final double y1 = p1.y;
		final double y2 = p2.y;
		if (x1 == x2) {
			return (Math.abs(y1 - y2));
		} else if (y1 == y2) {
			return (Math.abs(x1 - x2));
		}
		double du = x2 - x1;
		double dv = (y2 + Math.floor(x2 / 2)) - (y1 + Math.floor(x1 / 2));
		return Math.max(Math.abs(du), Math.abs(dv));

	}

	public static double distanciaEuclidena(double x1, double y1, double x2,
			double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
}
