package ed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Ordenacao {
    private static int operacoes;
    private static int trocas;

    public static void quickSort(List<Filme> filmes, Comparator<Filme> comparator) {
        operacoes = 0;
        trocas = 0;
        quickSort(filmes, 0, filmes.size() - 1, comparator);
    }

    private static void quickSort(List<Filme> filmes, int inicio, int fim, Comparator<Filme> comparator) {
        if (inicio < fim) {
            int pivo = particionar(filmes, inicio, fim, comparator);
            quickSort(filmes, inicio, pivo - 1, comparator);
            quickSort(filmes, pivo + 1, fim, comparator);
        }
    }

    private static int particionar(List<Filme> filmes, int inicio, int fim, Comparator<Filme> comparator) {
        Filme pivo = filmes.get(fim);
        int i = inicio - 1;
        
        for (int j = inicio; j < fim; j++) {
            operacoes++;
            if (comparator.compare(filmes.get(j), pivo) <= 0) {
                i++;
                trocar(filmes, i, j);
            }
        }
        trocar(filmes, i + 1, fim);
        return i + 1;
    }

    public static void mergeSort(List<Filme> filmes) {
        operacoes = 0;
        trocas = 0;
        mergeSort(filmes, 0, filmes.size() - 1);
    }

    private static void mergeSort(List<Filme> filmes, int inicio, int fim) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            mergeSort(filmes, inicio, meio);
            mergeSort(filmes, meio + 1, fim);
            merge(filmes, inicio, meio, fim);
        }
    }

    private static void merge(List<Filme> filmes, int inicio, int meio, int fim) {
        List<Filme> temp = new ArrayList<>(fim - inicio + 1);
        int i = inicio, j = meio + 1;
        
        while (i <= meio && j <= fim) {
            operacoes++;
            if (filmes.get(i).getAvaliacao() >= filmes.get(j).getAvaliacao()) {
                temp.add(filmes.get(i++));
            } else {
                temp.add(filmes.get(j++));
            }
        }
        
        while (i <= meio) temp.add(filmes.get(i++));
        while (j <= fim) temp.add(filmes.get(j++));
        
        for (int k = 0; k < temp.size(); k++) {
            filmes.set(inicio + k, temp.get(k));
            trocas++;
        }
    }

    public static void heapSort(List<Filme> filmes, Comparator<Filme> comparator) {
        operacoes = 0;
        trocas = 0;
        int n = filmes.size();
        
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(filmes, n, i, comparator);
        }
        
        for (int i = n - 1; i > 0; i--) {
            trocar(filmes, 0, i);
            heapify(filmes, i, 0, comparator);
        }
    }

    private static void heapify(List<Filme> filmes, int n, int i, Comparator<Filme> comparator) {
        int maior = i;
        int esq = 2 * i + 1;
        int dir = 2 * i + 2;
        
        operacoes++;
        if (esq < n && comparator.compare(filmes.get(esq), filmes.get(maior)) > 0) {
            maior = esq;
        }
        
        operacoes++;
        if (dir < n && comparator.compare(filmes.get(dir), filmes.get(maior)) > 0) {
            maior = dir;
        }
        
        if (maior != i) {
            trocar(filmes, i, maior);
            heapify(filmes, n, maior, comparator);
        }
    }

    public static void insertionSort(List<Filme> filmes, Comparator<Filme> comparator) {
        operacoes = 0;
        trocas = 0;
        for (int i = 1; i < filmes.size(); i++) {
            Filme chave = filmes.get(i);
            int j = i - 1;
            
            while (j >= 0 && comparator.compare(filmes.get(j), chave) > 0) {
                operacoes++;
                filmes.set(j + 1, filmes.get(j));
                trocas++;
                j--;
            }
            operacoes++;
            filmes.set(j + 1, chave);
            trocas++;
        }
    }

    public static void shellSort(List<Filme> filmes, Comparator<Filme> comparator) {
        operacoes = 0;
        trocas = 0;
        int n = filmes.size();
        int gap = 1;
        
        while (gap < n / 3) gap = 3 * gap + 1;
        
        while (gap >= 1) {
            for (int i = gap; i < n; i++) {
                Filme temp = filmes.get(i);
                int j;
                operacoes++;
                for (j = i; j >= gap && comparator.compare(filmes.get(j - gap), temp) > 0; j -= gap) {
                    operacoes++;
                    filmes.set(j, filmes.get(j - gap));
                    trocas++;
                }
                filmes.set(j, temp);
                trocas++;
            }
            gap /= 3;
        }
    }

    public static void selectionSort(List<Filme> filmes, Comparator<Filme> comparator) {
        operacoes = 0;
        trocas = 0;
        for (int i = 0; i < filmes.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < filmes.size(); j++) {
                operacoes++;
                if (comparator.compare(filmes.get(j), filmes.get(min)) < 0) {
                    min = j;
                }
            }
            trocar(filmes, i, min);
        }
    }

    public static void bubbleSort(List<Filme> filmes, Comparator<Filme> comparator) {
        operacoes = 0;
        trocas = 0;
        int n = filmes.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                operacoes++;
                if (comparator.compare(filmes.get(j), filmes.get(j + 1)) > 0) {
                    trocar(filmes, j, j + 1);
                }
            }
        }
    }

    private static void trocar(List<Filme> filmes, int i, int j) {
        Filme temp = filmes.get(i);
        filmes.set(i, filmes.get(j));
        filmes.set(j, temp);
        trocas++;
    }

    public static int getOperacoes() {
        return operacoes;
    }

    public static int getTrocas() {
        return trocas;
    }

    public static Comparator<Filme> comparadorPorTitulo() {
        return (f1, f2) -> f1.getTitulo().compareToIgnoreCase(f2.getTitulo());
    }

    public static Comparator<Filme> comparadorPorAvaliacao() {
        return (f1, f2) -> Double.compare(f2.getAvaliacao(), f1.getAvaliacao());
    }

    public static Comparator<Filme> comparadorPorVotos() {
        return (f1, f2) -> Integer.compare(f2.getVotos(), f1.getVotos());
    }

    public static Comparator<Filme> comparadorPorAno() {
        return (f1, f2) -> {
            if (f1.getAno() == -1 && f2.getAno() == -1) return 0;
            if (f1.getAno() == -1) return 1;
            if (f2.getAno() == -1) return -1;
            return Integer.compare(f2.getAno(), f1.getAno());
        };
    }
}