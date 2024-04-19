package br.ufrn.dimap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Map;

public class SerialMain {

	//digite aqui o caminho de onde quer acessar/escrever
	public static final String CAMINHO = "C:/Users/Joao/Documents/UFRN/PC/inputOutput/";
	
	public static void main(String[] args) throws IOException {
		
		int k = 3;
		int iteracoes = 4;
		
		List<Registro> centroides = inicializarCentroides(k);
		
		List<Registro> registros = leitorAutomatico("entrada.txt").map(Registro::transformaEmRegistro).collect(Collectors.toList());
			
		System.out.println("Leu e Alocou!");
		
		kMeans(iteracoes, registros, centroides);
			
		System.out.println("Associou!");
			
		gerarSaida(registros);
			
		System.out.println("Gravou!");
		
		gerarResultado();
		
	}
	
	private static List<Registro> inicializarCentroides(int k) {
		List<Registro> centroides = new ArrayList<>();
		Random rand = new Random();
		
		for(int i = 0; i < k; i++) {
			centroides.add(new Registro(i,rand.nextDouble(),rand.nextDouble()));
		}
		
		return centroides;
	}
	
	private static void kMeans(int it, List<Registro> registros, List<Registro> centroides) {
		
		IntStream.range(0, it).forEach(i -> {
			associarAosCentroides(registros,centroides);
			List<Registro> novosCentroides = calcularNovosCentroides(registros,centroides);
			centroides.clear();
			centroides.addAll(novosCentroides);
		});
		
	}
	
	private static void associarAosCentroides(List<Registro> registros, List<Registro> centroides) {
		registros.forEach(registro -> registro.associarCentroideMaisProximo(centroides));
	}
	
	private static List<Registro> calcularNovosCentroides(List<Registro> registros, List<Registro> centroides) {
		return centroides.stream()
				.map(centroide -> {
					List<Registro> registrosAssociados = registros.stream()
							.filter(r -> r.getCentroideAssociado() == centroide)
							.collect(Collectors.toList());
					
					double mediaX = registrosAssociados.stream()
							.mapToDouble(Registro::getX)
							.average()
							.orElse(0);
					
					double mediaY = registrosAssociados.stream()
							.mapToDouble(Registro::getY)
							.average()
							.orElse(0);
					
					return new Registro(centroide.getCluster(),mediaX,mediaY);
				}).collect(Collectors.toList());
	}
	
	private static void gerarSaida(List<Registro> registros) {
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CAMINHO+"saida.txt"))) {
			registros.forEach(registro ->{
				try {
					escritor.write(registro.getX() + " " + registro.getY() + " " + registro.getCluster()+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
            
            System.out.println("Registros associados gravados.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo 'saida.txt': " + e.getMessage());
        }
	}
	
	private static void gerarResultado() {
		Map<Integer, Integer> contadorMap = new HashMap<>();
		leitorAutomatico("saida.txt")
		.map(linha -> linha.split("\\s+"))
		.filter(pedacos -> pedacos.length >= 3)
		.forEach(pedacos ->{
			try {
				int numero = Integer.parseInt(pedacos[2].trim());
                contadorMap.put(numero, contadorMap.getOrDefault(numero, 0) + 1);
			} catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.err.println("Error parsing line: " + String.join(" ", pedacos));
			}
		});
		
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter(CAMINHO+"resultado.txt"))) {
			
			contadorMap.forEach((num, count) -> {
				try {
					escritor.write("Grupo ["+(num+1)+"º]: " + count + " ocorrências."+"\n");
				} catch(IOException e) {
					System.err.println("Erro ao contar no arquivo 'saida.txt': " + e.getMessage());
				}
			});
			
		} catch(IOException e) {
			System.err.println("Erro ao escrever no arquivo 'resultado.txt': " + e.getMessage());
		}
	}
	
	private static Stream<String> leitorAutomatico(String text) {
		try {
			return Files.lines(Paths.get(CAMINHO+text));
		} catch (IOException e) {
			e.printStackTrace();
			return Stream.empty();
		}
	}
}
