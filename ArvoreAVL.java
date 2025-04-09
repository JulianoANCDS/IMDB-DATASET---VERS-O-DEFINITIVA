package ed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArvoreAVL {
    private class No {
        Filme filme;
        No esquerda, direita;
        int altura;

        No(Filme filme) {
            this.filme = filme;
            this.altura = 1;
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
        } else if (cmp > 0) {
            no.direita = inserir(no.direita, filme);
        } else {
            no.direita = inserir(no.direita, filme);
            return no;
        }

        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
        int balance = getBalance(no);

        if (balance > 1 && filme.getTitulo().compareToIgnoreCase(no.esquerda.filme.getTitulo()) < 0) {
            return rotacionarDireita(no);
        }
        if (balance < -1 && filme.getTitulo().compareToIgnoreCase(no.direita.filme.getTitulo()) > 0) {
            return rotacionarEsquerda(no);
        }
        if (balance > 1 && filme.getTitulo().compareToIgnoreCase(no.esquerda.filme.getTitulo()) > 0) {
            no.esquerda = rotacionarEsquerda(no.esquerda);
            return rotacionarDireita(no);
        }
        if (balance < -1 && filme.getTitulo().compareToIgnoreCase(no.direita.filme.getTitulo()) < 0) {
            no.direita = rotacionarDireita(no.direita);
            return rotacionarEsquerda(no);
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

    private int altura(No no) {
        return no == null ? 0 : no.altura;
    }

    private int getBalance(No no) {
        return no == null ? 0 : altura(no.esquerda) - altura(no.direita);
    }

    private No rotacionarDireita(No y) {
        No x = y.esquerda;
        No T2 = x.direita;

        x.direita = y;
        y.esquerda = T2;

        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;

        return x;
    }

    private No rotacionarEsquerda(No x) {
        No y = x.direita;
        No T2 = y.esquerda;

        y.esquerda = x;
        x.direita = T2;

        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public int altura() {
        return altura(raiz);
    }
}