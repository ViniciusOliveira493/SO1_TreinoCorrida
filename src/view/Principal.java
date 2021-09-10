package view;

import java.util.concurrent.Semaphore;

import controller.ThreadCarro;

public class Principal {
	public static void main(String[] args) {
		int permissao = 5;
		int permissaoMutex = 1;
		Semaphore semaforo = new Semaphore(permissao);	
		Semaphore mutexPista = new Semaphore(permissaoMutex);
		Semaphore mutexTempo = new Semaphore(permissaoMutex);
		String escuderias[] = {"Mercedes","RBR-Honda","Mclaren-Mercedes","Ferrari","Alfa Romeo-Ferrari","Aston Martin-Mercedes","Alpine-Renault"};
		
		ThreadCarro carros[] = new ThreadCarro[escuderias.length*2];
		for (int i = 0; i < escuderias.length*2; i++) {
			ThreadCarro carro = new ThreadCarro(i, semaforo, escuderias,mutexPista,mutexTempo);
			carros[i]=carro;
		}
		
		for (int i = 0; i < escuderias.length*2; i++) {
			carros[i].start();
		}		
	}
}
