package ed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class CatalogoFilmes {
    private List<Filme> filmes;
    private ArvoreBinariaBusca arvoreTitulos;
    private ArvoreAVL arvoreAVL;

    public CatalogoFilmes() {
        this.filmes = new ArrayList<>();
        this.arvoreTitulos = new ArvoreBinariaBusca();
        this.arvoreAVL = new ArvoreAVL();
    }

    public void carregarDados(String caminhoBasico, String caminhoAvaliacoes) throws IOException {
        Map<String, Double> avaliacoes = new HashMap<>();
        Map<String, Integer> votos = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoAvaliacoes))) {
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split("\t");
                if (partes.length >= 3) {
                    avaliacoes.put(partes[0], Double.parseDouble(partes[1]));
                    votos.put(partes[0], Integer.parseInt(partes[2]));
                }
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoBasico))) {
            br.readLine();
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split("\t");
                if (partes.length >= 9 && "movie".equals(partes[1]) && 
                    avaliacoes.containsKey(partes[0]) && 
                    votos.get(partes[0]) >= 20000) {
                    
                    String[] generos = partes[8].equals("\\N") ? new String[0] : partes[8].split(",");
                    Filme filme = new Filme(
                        partes[0], 
                        partes[2], 
                        partes[5].equals("\\N") ? -1 : Integer.parseInt(partes[5]), 
                        generos,
                        avaliacoes.get(partes[0]), 
                        votos.get(partes[0])
                    );
                    filmes.add(filme);
                    arvoreTitulos.inserir(filme);
                    arvoreAVL.inserir(filme);
                }
            }
            System.out.println("Carregados " + filmes.size() + " filmes com pelo menos 20.000 votos.");
        }
    }

    public List<Filme> buscarPesquisaSequencial(String titulo) {
        return Busca.pesquisaSequencial(filmes, titulo);
    }

    public List<Filme> buscarPesquisaBinaria(String titulo) {
        List<Filme> copia = new ArrayList<>(filmes);
        Ordenacao.quickSort(copia, Ordenacao.comparadorPorTitulo());
        return Busca.pesquisaBinaria(copia, titulo);
    }

    public List<Filme> buscarBST(String titulo) {
        return arvoreTitulos.buscar(titulo);
    }

    public List<Filme> buscarAVL(String titulo) {
        return arvoreAVL.buscar(titulo);
    }

    public List<Filme> recomendarTopFilmes(int quantidade, int criterioOrdenacao) {
        List<Filme> copia = new ArrayList<>(filmes);
        
        switch (criterioOrdenacao) {
            case 1: Ordenacao.heapSort(copia, Ordenacao.comparadorPorVotos()); break;
            case 2: Ordenacao.mergeSort(copia); break;
            case 3: Ordenacao.quickSort(copia, Ordenacao.comparadorPorTitulo()); break;
            case 4: Ordenacao.quickSort(copia, Ordenacao.comparadorPorAno()); break;
            default: Ordenacao.mergeSort(copia);
        }
        
        Estatisticas.salvarEstatistica("ORDENACAO", "RECOMENDACAO", 
            new String[]{"VOTOS", "AVALIACAO", "TITULO", "ANO"}[criterioOrdenacao-1], 
            0, Ordenacao.getOperacoes(), Ordenacao.getTrocas(), copia.size());
        
        return copia.stream().limit(quantidade).collect(Collectors.toList());
    }

    public void compararAlgoritmosBusca(String titulo) {
        System.out.println("\n=== COMPARAÇÃO DE ALGORITMOS DE BUSCA ===");
        System.out.printf("Buscando: '%s'\n", titulo);
        System.out.println("Método\t\tTempo (ms)\tComparações");
        System.out.println("---------------------------------------");
        
        testarBusca("Sequencial", () -> buscarPesquisaSequencial(titulo), titulo);
        testarBusca("Binária", () -> buscarPesquisaBinaria(titulo), titulo);
        testarBusca("BST", () -> buscarBST(titulo), titulo);
        testarBusca("AVL", () -> buscarAVL(titulo), titulo);
    }

    public void compararAlgoritmosOrdenacao() {
        System.out.println("\n=== COMPARAÇÃO DE ALGORITMOS DE ORDENAÇÃO ===");
        System.out.printf("Ordenando %,d filmes (mín. 20,000 votos)...\n", filmes.size());
        System.out.println("Algoritmo\t\tTempo (ms)\tComparações\tTrocas\tComplexidade");
        System.out.println("---------------------------------------------------------------");
        
        testarOrdenacao("QuickSort (Título)", () -> Ordenacao.quickSort(new ArrayList<>(filmes), Ordenacao.comparadorPorTitulo()));
        testarOrdenacao("MergeSort (Avaliação)", () -> Ordenacao.mergeSort(new ArrayList<>(filmes)));
        testarOrdenacao("HeapSort (Votos)", () -> Ordenacao.heapSort(new ArrayList<>(filmes), Ordenacao.comparadorPorVotos()));
        testarOrdenacao("ShellSort (Título)", () -> Ordenacao.shellSort(new ArrayList<>(filmes), Ordenacao.comparadorPorTitulo()));
        testarOrdenacao("InsertionSort (Título)", () -> Ordenacao.insertionSort(new ArrayList<>(filmes), Ordenacao.comparadorPorTitulo()));
        testarOrdenacao("SelectionSort (Título)", () -> Ordenacao.selectionSort(new ArrayList<>(filmes), Ordenacao.comparadorPorTitulo()));
        testarOrdenacao("BubbleSort (Avaliação)", () -> Ordenacao.bubbleSort(new ArrayList<>(filmes), Ordenacao.comparadorPorAvaliacao()));
    }

    public void compararEstruturas() {
        System.out.println("\n=== COMPARAÇÃO DE ESTRUTURAS ===");
        System.out.println("BST vs AVL");
        System.out.println("Altura BST: " + arvoreTitulos.altura());
        System.out.println("Altura AVL: " + arvoreAVL.altura());
        System.out.println("\nQuanto menor a altura, mais eficiente são as buscas.");
    }

    private void testarBusca(String nome, Runnable metodo, String termo) {
        long inicio = System.currentTimeMillis();
        metodo.run();
        long tempo = System.currentTimeMillis() - inicio;
        
        int comparacoes = nome.equals("Sequencial") ? Busca.getComparacoes() : 
                         nome.equals("Binária") ? Busca.getComparacoes() :
                         nome.equals("BST") ? arvoreTitulos.getComparacoes() :
                         arvoreAVL.getComparacoes();
        
        Estatisticas.salvarEstatistica("BUSCA", nome, termo, tempo, comparacoes, 0, filmes.size());
        System.out.printf("%-12s\t%6d\t\t%9d\n", nome, tempo, comparacoes);
    }

    private void testarOrdenacao(String nome, Runnable metodo) {
        long inicio = System.currentTimeMillis();
        metodo.run();
        long tempo = System.currentTimeMillis() - inicio;
        
        String complexidade = nome.contains("QuickSort") || nome.contains("MergeSort") || 
                            nome.contains("HeapSort") || nome.contains("ShellSort") 
                            ? "O(n log n)" : "O(n²)";
        
        Estatisticas.salvarEstatistica("ORDENACAO", nome.split(" ")[0], "", 
            tempo, Ordenacao.getOperacoes(), Ordenacao.getTrocas(), filmes.size());
        
        System.out.printf("%-18s\t%6d\t\t%9d\t%6d\t%s\n", 
            nome, tempo, Ordenacao.getOperacoes(), Ordenacao.getTrocas(), complexidade);
    }

    public List<Filme> getFilmes() {
        return filmes;
    }

    public ArvoreBinariaBusca getArvoreTitulos() {
        return arvoreTitulos;
    }

    public ArvoreAVL getArvoreAVL() {
        return arvoreAVL;
    }
}