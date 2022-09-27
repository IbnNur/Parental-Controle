package www.nazirdaudo.controleparental.Modelos;

public class Usuarios
{
    String nome, cell, senha;

    public Usuarios()
    {

    }

    public Usuarios(String nome, String cell, String senha) {
        this.nome = nome;
        this.cell = cell;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
