package com.enalto;

import java.util.Random;
import java.util.Scanner;

public class AdvinharNumeros {

    private int countTentativas = 0;
    private GeradorDeNumeroAleatorio geradorDeNumeroAleatorio;


    public static void main(String[] args) {
        AdvinharNumeros advinharNumeros = new AdvinharNumeros();
        advinharNumeros.run();
    }




    /**
     * Start da aplicacao
     */
    private void run() {
        int inputNumber = 0;
        String min = lerString("Digite o intervalo inicial para gerar o numero secreto ou (sair): ");
        if (!checkInput(min)) return;

        String max = lerString("Digite o intervalo final para gerar o numero secreto ou (sair): ");
        if (!checkInput(max)) return;

        try {
            this.geradorDeNumeroAleatorio =
                    gerarNumeroAleatorio(Integer.parseInt(min), Integer.parseInt(max));

        } catch (Exception e) {
            System.out.println("Numero de tentativas= " + this.countTentativas);
            System.out.println("Entrada inválida. [" + e.getMessage() + "]");
            return;
        }

        while (true) {

            String input = lerString("Digite o numero secreto ou [sair] para finalizar: ");
            // int numeroAleatorio = geradorDeNumeroAleatorio.getNumeroGerado();

            if (input.equalsIgnoreCase("sair")) {
                System.out.println("Numero de tentativas= " + this.countTentativas);
                break;
            } else {
                try {
                    inputNumber = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Digite um numero !!!");
                    continue;
                }
                this.countTentativas++;

                InputCompare inputCompare = geradorDeNumeroAleatorio.compareWith(inputNumber);

                if (inputCompare.equals(InputCompare.IGUAL)) {
                    System.out.println("Você acertou!!! o numero secreto é= " + geradorDeNumeroAleatorio.getNumeroGerado() + ", parabens!");
                    System.out.println("Numero de tentativas= " + this.countTentativas);
                    break;
                } else if (inputCompare.equals(InputCompare.MAIOR)) {
                    System.out.println("Numero secreto é menor, tente novamente");
                } else {
                    System.out.println("Numero secreto é maior, tente novamente");
                }
            }
        }
    }

    private boolean checkInput(String input) {
        int inputNumber;
        if (input.equalsIgnoreCase("sair")) {
            System.out.println("Numero de tentativas= " + this.countTentativas);
            return false;
        } else {
            try {
                inputNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada Inválida.");
                System.out.println("Numero de tentativas= " + this.countTentativas);
                return false;
            }
        }
        return true;
    }


    /**
     * Cria um objeto GeradorDeNumeroAleatorio que contem o numero aleatorio
     * gerado
     *
     * @param min
     * @param max
     * @return GeradorDeNumeroAleatorio
     */
    private GeradorDeNumeroAleatorio gerarNumeroAleatorio(int min, int max) {
        var numeroAleatorio = new GeradorDeNumeroAleatorio.Builder()
                .comIntervaloInicial(min)
                .comIntervaloFinal(max)
                .build();
        return numeroAleatorio;
    }


    /**
     * Leitura das entradas digitadas pelo usuario
     *
     * @param mensagem
     * @return String
     */
    private String lerString(String mensagem) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(mensagem);
        return scanner.nextLine();
    }

}


/**
 * Builder para gerar o numero aleatorio num determinado intervalo
 *
 * @return int com numero aleatorio
 */
class GeradorDeNumeroAleatorio {
    private int minimo;
    private int maximo;
    private int numeroGerado;

    private GeradorDeNumeroAleatorio() {
    }

    private GeradorDeNumeroAleatorio(Builder builder) {
        this.minimo = builder.minimo;
        this.maximo = builder.maximo;
        this.numeroGerado = builder.numeroGerado;
    }

    public int getNumeroGerado() {
        return numeroGerado;
    }

    public boolean isEqual(int other) {
        return this.numeroGerado == other;
    }

    public boolean isHighest(int other) {
        return this.numeroGerado > other;
    }

    public boolean isLowest(int other) {
        return this.numeroGerado < other;
    }

    public static class Builder {
        private int minimo;
        private int maximo;
        private int numeroGerado;

        public Builder comIntervaloInicial(int minimo) {
//            if (minimo <= 0) {
//                throw new RuntimeException("Intervalo inicial deve ser maior que zero.");
//            }
            this.minimo = minimo;
            return this;
        }

        public Builder comIntervaloFinal(int maximo) {
//            if (minimo <= 0) {
//                throw new RuntimeException("Intervalo final deve ser maior que zero.");
//            }
            if (maximo <= minimo) {
                throw new RuntimeException("Intervalo final deve ser maior que inicial.");
            }
            this.maximo = maximo;
            return this;
        }

        private void gerarNumeroAleatorio() {
            Random random = new Random();
            var numeroAleatorio = random.nextInt(this.maximo - this.minimo + 1) + this.minimo;
            this.numeroGerado = numeroAleatorio;
        }

        public GeradorDeNumeroAleatorio build() {
            gerarNumeroAleatorio();
            return new GeradorDeNumeroAleatorio(this);
        }

    }

    public InputCompare compareWith(Integer input) {

        return switch (Integer.compare(input, this.numeroGerado)) {
            case -1 -> InputCompare.MENOR;
            case 0 -> InputCompare.IGUAL;
            case 1 -> InputCompare.MAIOR;
            default ->
                    throw new IllegalStateException("Unexpected value: " + Integer.compare(input, this.numeroGerado));
        };

    }


}

enum InputCompare {
    IGUAL,
    MAIOR,
    MENOR,

}