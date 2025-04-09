package ed;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CatalogoFilmes catalogo = new CatalogoFilmes();
        try {
            System.out.println("Carregando dados do IMDb...");
            catalogo.carregarDados("title.basics.tsv", "title.ratings.tsv");
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return;
        }
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== SISTEMA DE FILMES IMDb ===");
            System.out.println("1. Buscar filme por título");
            System.out.println("2. Ver filmes melhor avaliados");
            System.out.println("3. Comparar algoritmos de busca");
            System.out.println("4. Comparar algoritmos de ordenação");
            System.out.println("5. Comparar estruturas de dados");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1: buscarFilme(catalogo, scanner); break;
                case 2: recomendarFilmes(catalogo, scanner); break;
                case 3: compararAlgoritmosBusca(catalogo, scanner); break;
                case 4: catalogo.compararAlgoritmosOrdenacao(); break;
                case 5: catalogo.compararEstruturas(); break;
                case 6: System.out.println("Saindo do sistema..."); scanner.close(); return;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void buscarFilme(CatalogoFilmes catalogo, Scanner scanner) {
        System.out.print("\nDigite o título do filme: ");
        String titulo = scanner.nextLine();
        
        System.out.println("\nSelecione o método de busca:");
        System.out.println("1. Pesquisa Sequencial");
        System.out.println("2. Pesquisa Binária");
        System.out.println("3. Árvore Binária de Busca (BST)");
        System.out.println("4. Árvore AVL");
        System.out.print("Opção: ");
        int metodo = scanner.nextInt();
        scanner.nextLine();
        
        long inicio = System.currentTimeMillis();
        List<Filme> resultados = switch (metodo) {
            case 1 -> catalogo.buscarPesquisaSequencial(titulo);
            case 2 -> catalogo.buscarPesquisaBinaria(titulo);
            case 3 -> catalogo.buscarBST(titulo);
            case 4 -> catalogo.buscarAVL(titulo);
            default -> { System.out.println("Método inválido!"); yield List.of(); }
        };
        
        long tempo = System.currentTimeMillis() - inicio;
        int comparacoes = switch (metodo) {
            case 1, 2 -> Busca.getComparacoes();
            case 3 -> catalogo.getArvoreTitulos().getComparacoes();
            case 4 -> catalogo.getArvoreAVL().getComparacoes();
            default -> 0;
        };
        
        System.out.println("\nResultados para '" + titulo + "':");
        System.out.println("Tempo: " + tempo + "ms | Comparações: " + comparacoes);
        if (resultados.isEmpty()) {
            System.out.println("Nenhum filme encontrado.");
        } else {
            System.out.println("\nFilmes encontrados (" + resultados.size() + "):");
            for (int i = 0; i < resultados.size(); i++) {
                System.out.println((i+1) + ". " + resultados.get(i));
            }
        }
    }

    private static void recomendarFilmes(CatalogoFilmes catalogo, Scanner scanner) {
        System.out.print("\nQuantos filmes você quer ver? ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("\nOrdenar por:");
        System.out.println("1. Quantidade de avaliações (votos)");
        System.out.println("2. Melhores avaliações (rating)");
        System.out.println("3. Ordem alfabética (título)");
        System.out.println("4. Ano de lançamento (mais recentes)");
        System.out.print("Escolha o critério: ");
        int criterio = scanner.nextInt();
        scanner.nextLine();
        
        List<Filme> recomendados = catalogo.recomendarTopFilmes(quantidade, criterio);
        System.out.println("\nTop " + quantidade + " filmes:");
        for (int i = 0; i < recomendados.size(); i++) {
            System.out.println((i+1) + ". " + recomendados.get(i));
        }
    }

    private static void compararAlgoritmosBusca(CatalogoFilmes catalogo, Scanner scanner) {
        System.out.print("\nDigite um título para comparar algoritmos de busca: ");
        catalogo.compararAlgoritmosBusca(scanner.nextLine());
    }
}