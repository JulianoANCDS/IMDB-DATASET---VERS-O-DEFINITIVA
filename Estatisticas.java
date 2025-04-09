package ed;

import java.io.FileWriter;
import java.io.IOException;

public class Estatisticas {
    private static final String ARQUIVO_ESTATISTICAS = "estatisticas.txt";
    
    public static void salvarEstatistica(String tipo, String algoritmo, String termo, 
                                       long tempo, int comparacoes, int trocas, int tamanho) {
        try (FileWriter writer = new FileWriter(ARQUIVO_ESTATISTICAS, true)) {
            writer.write(String.format("%s|%s|%s|%d|%d|%d|%d\n", 
                tipo, algoritmo, termo, tempo, comparacoes, trocas, tamanho));
        } catch (IOException e) {
            System.err.println("Erro ao salvar estatísticas: " + e.getMessage());
        }
    }
}