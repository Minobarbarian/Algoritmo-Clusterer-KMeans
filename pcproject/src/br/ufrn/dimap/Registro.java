package br.ufrn.dimap;

import java.util.Comparator;
import java.util.List;

public class Registro {
	private int cluster;
	private double x;
	private double y;
	private Registro centroideAssociado;
	
	public Registro(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Registro(int c, double x, double y) {
		this.cluster = c;
		this.x = x;
		this.y = y;
	}
	
	public int getCluster() {
		return cluster;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Registro getCentroideAssociado() {
		return centroideAssociado;
	}
	
	public void associarCentroideMaisProximo(List<Registro> centroides) {
		centroideAssociado = centroides.stream()
			.min(Comparator.comparingDouble(centroide -> Math.sqrt(Math.pow(x - centroide.getX(), 2) + Math.pow(y - centroide.getY(), 2))))
			.orElse(null);
		this.cluster = centroideAssociado.cluster;
	}
	
	public static Registro transformaEmRegistro(String linha) {
		String[] pedacos = linha.split(" ");
		double x = Double.parseDouble(pedacos[0]);
		double y = Double.parseDouble(pedacos[1]);
		return new Registro(x,y);
	}
}
