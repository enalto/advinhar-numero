package com.enalto;

/**
 * projeto esta disponivel no github
 * <p>
 * https://github.com/enalto/advinhar-numero
 */

import java.util.Random;
import java.util.Scanner;
import java.util.logging.*;

public class AdvinharNumeros {

    static {
        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }

    private int countTentativas = 0;
    private GeradorDeNumeroAleatorio geradorDeNumeroAleatorio;
    private static final Logger logger = Logger.getLogger(AdvinharNumeros.class.getName());

    public static void main(String[] args) {
        AdvinharNumeros advinharNumeros = new AdvinharNumeros();
        advinharNumeros.run();
    }


    /**
     * Start da aplicacao
     */
    private void run() {
        logger.setLevel(Level.INFO);

        String thin = """
                                                                                                    \s
                .   .     o,---.                                                                    \s
                |   |,---..|    ,---.,---..   .,-.-.,---.,---.                                      \s
                |   ||   |||    |---'`---.|   || | |,---||                                          \s
                `---'`   '``---'`---'`---'`---'` ' '`---^`                                          \s
                                                                                                    \s
                                                                                                    \s
                    |                       |         ,---.    |      o     |                       \s
                    |,---.,---.,---.    ,---|,---.    |---|,---|.    ,.,---.|---.,---.,---.,---.,---.
                    ||   ||   ||   |    |   |,---|    |   ||   | \\  / ||   ||   |,---||    ,---||   |
                `---'`---'`---|`---'    `---'`---^    `   '`---'  `'  ``   '`   '`---^`---'`---^`---'
                          `---'                                                                     \s
                
                                                              \s
                """;

        logger.info(thin);
        logger.info("Aluno: Enalto de Oliveira Gondrige");
        logger.info("RA: 22114039-5");
        logger.info("Bem-vindo ao Jogo da Advinhação!");
        logger.info("Tente adivinhar o número secreto.");
        logger.info("Digite 'sair' para encerrar o jogo.");

        int inputNumber = 0;
        boolean entradaInvalida = true;
        String min = "";
        String max = "";

        do {

            min = lerString("Digite o intervalo inicial para gerar o numero secreto ou (sair): ");
            if (isShutdown(min))
                return;

            entradaInvalida = !checkInput(min);
            if (entradaInvalida)
                continue;

            max = lerString("Digite o intervalo final para gerar o numero secreto ou (sair): ");
            if (isShutdown(max))
                return;

            entradaInvalida = !checkInput(max);

            if (!(Integer.parseInt(max) > Integer.parseInt(min))) {
                System.out.println("Intervalo incorreto, informe novamente!");
                entradaInvalida = true;
            }

        } while (entradaInvalida);

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

            if (input.equalsIgnoreCase("sair")) {
                logger.info("Numero de tentativas= " + this.countTentativas);
                logger.info("Jogo finalizado.");
                break;
            } else {
                try {
                    inputNumber = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Digite um numero válido!!!");
                    continue;
                }
                this.countTentativas++;

                int intervaloInicial = geradorDeNumeroAleatorio.getMinimo();
                int intervaloFinal = geradorDeNumeroAleatorio.getMaximo();
                if (!validaIntervalo(inputNumber)) {
                    System.out.println("Numero digitado está fora do intervalo [" + intervaloInicial + "-" + intervaloFinal + "] definido!!!");
                    continue;
                }

                InputCompare inputCompare = geradorDeNumeroAleatorio.compareWith(inputNumber);

                if (inputCompare.equals(InputCompare.IGUAL)) {
                    logger.info("Você acertou!!! o numero secreto é= " + geradorDeNumeroAleatorio.getNumeroGerado() + ", parabens!");
                    logger.info("Numero de tentativas= " + this.countTentativas);
                    break;
                } else if (inputCompare.equals(InputCompare.MAIOR)) {
                    System.out.println("Numero secreto é MENOR, tente novamente");
                } else {
                    System.out.println("Numero secreto é MAIOR, tente novamente");
                }
            }
        }
    }

    private boolean validaIntervalo(int inputNumber) {
        if (!(geradorDeNumeroAleatorio.maximoEhMaiorQue(inputNumber)
                && geradorDeNumeroAleatorio.minimoEhMenorQue(inputNumber))) {
            return false;
        }
        return true;
    }

    /**
     * Validar se o numero é valido
     *
     * @param input
     * @return boolean
     */
    private boolean checkInput(String input) {
        int inputNumber;
        try {
            inputNumber = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Entrada Inválida. Digite um numero valido.");
            return false;
        }
        return true;
    }

    /**
     * Checar se o usuario quer sair do jogo
     *
     * @param input
     * @return
     */
    private boolean isShutdown(String input) {
        if (input.equalsIgnoreCase("sair")) {
            logger.info("Numero de tentativas= " + this.countTentativas);
            logger.info("Jogo finalizado.");
            return true;
        }
        return false;
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
 * Classe GeradorDeNumeroAleatorio, para gerar o numero aleatorio
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

    public boolean maximoEhMaiorQue(int other) {
        return this.maximo >= other;
    }

    public boolean minimoEhMenorQue(int other) {
        return this.minimo <= other;
    }

    public int getMinimo() {
        return minimo;
    }

    public int getMaximo() {
        return maximo;
    }

    /**
     * Padrão builder para criar objeto Gerador de Numero Aleatorio
     */
    public static class Builder {
        private int minimo;
        private int maximo;
        private int numeroGerado;

        public Builder comIntervaloInicial(int minimo) {
            if (minimo <= 0) {
                throw new RuntimeException("Intervalo inicial deve ser maior que zero.");
            }
            this.minimo = minimo;
            return this;
        }

        public Builder comIntervaloFinal(int maximo) {
            if (minimo <= 0) {
                throw new RuntimeException("Intervalo final deve ser maior que zero.");
            }
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