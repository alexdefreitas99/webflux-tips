package com.alex.springtips;

public class TestSpringSerialization {
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        System.out.println("Setando o nome " + nome);
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "TestSpringSerialization{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
