package br.ufrn.dimap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.stream.LongStream;

public class GeradorCoordenadas {
	
	//digite aqui o caminho de onde quer esse dataset
	public static final String CAMINHO = "C:/Users/Joao/Documents/UFRN/PC/inputOutput/";
	
	public static void main(String[] args) {
		gerarCoordenadas(30_000_000L, CAMINHO+"entrada.txt");
    }

    public static void gerarCoordenadas(long numeroDeCoordenadas, String nomeArquivo) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo))) {
            Random random = new Random();
            LongStream.range(0, numeroDeCoordenadas)
                .forEach(i -> {
                    double x = random.nextDouble() * 180 - 90;
                    double y = random.nextDouble() * 360 - 180;
                    try {
                    	escritor.write(x + " " + y + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            System.out.println("Coordenadas geradas.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
