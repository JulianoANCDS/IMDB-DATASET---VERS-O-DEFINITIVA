package ed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArvoreBinariaBusca {
    private class No {
        Filme filme;
        No esquerda, direita;

        No(Filme filme) {
            this.filme = filme;
        }
    }

    private No raiz;
    private int comparacoes;

    public void inserir(Filme filme) {
        raiz = inserir(raiz, filme);
    }

    private No inserir(No no, Filme filme) {
        if (no == null) return new No(filme);

        int cmp = filme.getTitulo().compareToIgnoreCase(no.filme.getTitulo());
        if (cmp < 0) {
            no.esquerda = inserir(no.esquerda, filme);
        } else {
            no.direita = inserir(no.direita, filme);
        }
        return no;
    }

    public List<Filme> buscar(String titulo) {
        comparacoes = 0;
        List<Filme> resultados = new ArrayList<>();
        buscar(raiz, titulo.toLowerCase().replaceAll("[^a-z0-9]", ""), resultados);
        ordenarPorRelevancia(resultados);
        return resultados;
    }

    private void buscar(No no, String tituloLower, List<Filme> resultados) {
        if (no == null) return;

        comparacoes++;
        String noTitulo = no.filme.getTitulo().toLowerCase().replaceAll("[^a-z0-9]", "");
        
        if (noTitulo.contains(tituloLower)) {
            resultados.add(no.filme);
            buscar(no.esquerda, tituloLower, resultados);
            buscar(no.direita, tituloLower, resultados);
        } else if (tituloLower.compareTo(noTitulo) < 0) {
            buscar(no.esquerda, tituloLower, resultados);
        } else {
            buscar(no.direita, tituloLower, resultados);
        }
    }

    private void ordenarPorRelevancia(List<Filme> filmes) {
        Collections.sort(filmes, (f1, f2) -> {
            int cmp = Integer.compare(f2.getVotos(), f1.getVotos());
            return cmp != 0 ? cmp : Double.compare(f2.getAvaliacao(), f1.getAvaliacao());
        });
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public int altura() {
        return altura(raiz);
    }

    private int altura(No no) {
        return no == null ? 0 : 1 + Math.max(altura(no.esquerda), altura(no.direita));
    }
}