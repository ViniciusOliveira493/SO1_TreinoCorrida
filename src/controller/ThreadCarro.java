package controller;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;

import model.Carro;

public class ThreadCarro extends Thread{
	private static String escuderias[];
	private Carro carro = new Carro();
	
	private static int carrosNaPista[] = new int[5];	
	private static double tempos[];
	private static Carro carros[];
	private static int temposMarcados = 0;
	
	Semaphore semaforo;
	Semaphore mutexPista;
	Semaphore mutexTempo;
	
	public ThreadCarro(int id,Semaphore semaforo,String escuderias[],Semaphore mutexPista,Semaphore mutexTempo) {
		this.carro.setId(id);
		this.semaforo = semaforo;
		this.escuderias = escuderias;
		this.mutexPista = mutexPista;
		this.mutexTempo = mutexTempo;
		ThreadCarro.tempos = new double[escuderias.length*2];
		ThreadCarro.carros = new Carro[escuderias.length*2];
		
		if(id%2==0) {
			this.carro.setCompanheiro(id+1);
		}else {
			this.carro.setCompanheiro(id-1);
		}
		
		if(id<2) {
			this.carro.setEscuderia(escuderias[0]);
		}else if(id<4) {
			this.carro.setEscuderia(escuderias[1]);
		}else if(id<6) {
			this.carro.setEscuderia(escuderias[2]);
		}else if(id<8) {
			this.carro.setEscuderia(escuderias[3]);
		}else if(id<10) {
			this.carro.setEscuderia(escuderias[4]);
		}else if(id<12) {
			this.carro.setEscuderia(escuderias[5]);
		}else {
			this.carro.setEscuderia(escuderias[6]);
		}
		
		for (int i = 0; i < carrosNaPista.length; i++) {
			carrosNaPista[i]=-1;
		}
	}
	
	@Override
	public void run() {
		entrarNaPista();
		if (tempos.length==temposMarcados) {
			System.out.println("A CORRIDA ACABOU");
			String grid = criarGrid();
			System.out.println(grid);
			JOptionPane.showMessageDialog(null, grid);
			
		}
	}
	
	public double[] getTempos() {
		return ThreadCarro.tempos;
	}
	
	private void entrarNaPista() {
		while (this.carro.getMelhorTempo()==8) {
			if(!companheiroEstaNaPista()) {
				try {
					semaforo.acquire();
					adicionarCarroNaPista(this.carro.getId());
					for (int i = 0; i < 3; i++) {
						//tempo de volta 2-4 minutos
						double volta = (Math.random()*2)+2;
						this.sleep((int)volta*1000);
						System.out.println(this.carro.getEscuderia()+" Carro #"+(this.carro.getId()+1)+" Tempo da volta #"+(i+1)+" "+volta);
						if(volta < this.carro.getMelhorTempo()) {
							this.carro.setMelhorTempo(volta);
						}
					}
					marcarTempo();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					removerCarroDaPista(this.carro.getId());
					System.err.println("Carro #"+(this.carro.getId()+1)+" tempo:"+this.carro.getMelhorTempo());
					semaforo.release();
				}
			}
		}
	}
	
	private void marcarTempo() {
		try {
			mutexTempo.acquire();
			tempos[this.carro.getId()] = this.carro.getMelhorTempo();
			carros[this.carro.getId()] = this.carro;
			temposMarcados++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			mutexTempo.release();
		}
		
	}
	
	private boolean companheiroEstaNaPista() {
		boolean companheiroEstaNaPista = false;
		for (int i = 0; i < carrosNaPista.length; i++) {
			if (carrosNaPista[i] == this.carro.getCompanheiro()) {
				companheiroEstaNaPista = true;
			}
		}
		return companheiroEstaNaPista;
	}
	
	private void adicionarCarroNaPista(int id) {
		try {
			mutexPista.acquire();
			for (int j = 0; j < carrosNaPista.length; j++) {
				if(carrosNaPista[j]==-1) {
					carrosNaPista[j] = id;
					break;
				}
			}			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			mutexPista.release();
		}
		
	}
	
	private void removerCarroDaPista(int id) {
		try {
			mutexPista.acquire();
			for (int j = 0; j < carrosNaPista.length; j++) {
				if(carrosNaPista[j]==id) {
					carrosNaPista[j] = -1;
				}
			}			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			mutexPista.release();
		}
		
	}	
	
	private String getCarrosnaPista() {
		String r = "";
		for (int j = 0; j < carrosNaPista.length; j++) {
			r+= carrosNaPista[j]+" ";
		}	
		return r;
	}
	
	private String criarGrid() {
		double aux[] = tempos;
		Arrays.sort(aux);
		
		String grid = "---Grid de Largada---\n\n";		
		
		for (int i = 0; i < aux.length; i++) {
			for (int j = 0; j < carros.length; j++) {
				if(aux[i] == carros[j].getMelhorTempo()) {
					grid+=(i+1)+"º - Carro número: "+(carros[j].getId()+1)+" | Escuderia: "+carros[j].getEscuderia()+" "
							+ "| Tempo:"+carros[j].getMelhorTempo()+"\n";
					break;
				}
			}
		}
		
		return grid;
	}
}
