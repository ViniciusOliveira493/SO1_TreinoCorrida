package model;

public class Carro {
	private int id;
	private double melhorTempo = 8;
	private String escuderia;
	private int companheiro;
	
	public Carro() {
		super();
	}
	
	public Carro(int id,int melhorTempo,String escuderia,int companheiro) {
		super();
		this.id = id;
		this.melhorTempo = melhorTempo;
		this.escuderia = escuderia;
		this.companheiro = companheiro;
	}
	
	public int getCompanheiro() {
		return companheiro;
	}
	
	public String getEscuderia() {
		return escuderia;
	}
	
	public int getId() {
		return id;
	}
	
	public double getMelhorTempo() {
		return melhorTempo;
	}
	
	public void setMelhorTempo(double melhorTempo) {
		this.melhorTempo = melhorTempo;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setEscuderia(String escuderia) {
		this.escuderia = escuderia;
	}

	public void setCompanheiro(int companheiro) {
		this.companheiro = companheiro;
	}
}
