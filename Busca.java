package ed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Busca {
    private static int comparacoes;

    public static List<Filme> pesquisaSequencial(List<Filme> filmes, String titulo) {
        comparacoes = 0;
        List<Filme> resultados = new ArrayList<>();
        String tituloLower = titulo.toLowerCase().replaceAll("[^a-z0-9]", "");
        
        for (Filme filme : filmes) {
            comparacoes++;
            if (filme.getTitulo().toLowerCase().replaceAll("[^a-z0-9]", "").contains(tituloLower)) {
                resultados.add(filme);
            }
        }
        
        ordenarPorRelevancia(resultados);
        return resultados;
    }

    public static List<Filme> pesquisaBinaria(List<Filme> filmes, String titulo) {
        comparacoes = 0;
        List<Filme> resultados = new ArrayList<>();
        String tituloLower = titulo.toLowerCase().replaceAll("[^a-z0-9]", "");
        
        int esquerda = 0, direita = filmes.size() - 1;
        while (esquerda <= direita) {
            comparacoes++;
            int meio = esquerda + (direita - esquerda) / 2;
            String meioTitulo = filmes.get(meio).getTitulo().toLowerCase().replaceAll("[^a-z0-9]", "");
            
            if (meioTitulo.contains(tituloLower)) {
                resultados.add(filmes.get(meio));
                for (int i = meio - 1; i >= 0 && filmes.get(i).getTitulo().toLowerCase().replaceAll("[^a-z0-9]", "").contains(tituloLower); i--) {
                    resultados.add(filmes.get(i));
                }
                for (int i = meio + 1; i < filmes.size() && filmes.get(i).getTitulo().toLowerCase().replaceAll("[^a-z0-9]", "").contains(tituloLower); i++) {
                    resultados.add(filmes.get(i));
                }
                break;
            } else if (meioTitulo.compareTo(tituloLower) < 0) {
                esquerda = meio + 1;
            } else {
                direita = meio - 1;
            }
        }
        
        ordenarPorRelevancia(resultados);
        return resultados;
    }

    private static void ordenarPorRelevancia(List<Filme> filmes) {
        Collections.sort(filmes, (f1, f2) -> {
            int cmp = Integer.compare(f2.getVotos(), f1.getVotos());
            return cmp != 0 ? cmp : Double.compare(f2.getAvaliacao(), f1.getAvaliacao());
        });
    }

    public static int getComparacoes() {
        return comparacoes;
    }
}