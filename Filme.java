package ed;

public class Filme {
    private String id;
    private String titulo;
    private int ano;
    private String[] generos;
    private double avaliacao;
    private int votos;

    public Filme(String id, String titulo, int ano, String[] generos, double avaliacao, int votos) {
        this.id = id;
        this.titulo = titulo;
        this.ano = ano;
        this.generos = generos;
        this.avaliacao = avaliacao;
        this.votos = votos;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getAno() { return ano; }
    public String[] getGeneros() { return generos; }
    public double getAvaliacao() { return avaliacao; }
    public int getVotos() { return votos; }

    @Override
    public String toString() {
        return String.format("%s (%d) - ★ %.1f (%,d votos) %s", 
               titulo, ano, avaliacao, votos, String.join(", ", generos));
    }
}