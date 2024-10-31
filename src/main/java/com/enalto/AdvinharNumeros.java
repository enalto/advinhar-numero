package com.enalto;

/**
 * projeto esta disponivel no github
 * https://github.com/enalto/advinhar-numero
 * Projeto REQUER Java 15 ou superior
 */

import java.util.Optional;
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
    private static final String javaVersion = System.getProperty("java.version");

    public static void main(String[] args) {
        AdvinharNumeros advinharNumeros = new AdvinharNumeros();
        advinharNumeros.run();
    }

    /**
     * Start da aplicacao
     */
    private void run() {
        printImageASCII();
        Optional<Pair> pair = leituraDoIntervaloNumeroAleatorio();
        if (pair.isPresent()) {
            try {
                geradorDeNumeroAleatorio = gerarNumeroAleatorio(pair.get());
                avaliarChute();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        logger.info("fim de jogo!");
        logger.info("Tentativas= " + countTentativas);

    }

    private void avaliarChute() {
        while (true) {
            Optional<Integer> chute = leituraDoChute();
            if (chute.isEmpty()) break;

            InputCompare inputCompare = geradorDeNumeroAleatorio.compareWith(chute.get());
            if (inputCompare.equals(InputCompare.IGUAL)) {
                System.out.println("Parabens você acertou !!!, numero secreto= "
                        + geradorDeNumeroAleatorio.getNumeroGerado());
                break;
            } else if (inputCompare.equals(InputCompare.MENOR)) {
                System.out.println("Numero secreto é maior!, tente novamente");
            } else {
                System.out.println("Numero secreto é menor!, tente novamente");
            }
        }
    }

    private Optional<Integer> leituraDoChute() {
        Scanner scanner = new Scanner(System.in);
        Optional<Integer> result = Optional.empty();
        int chuteValue = 0;
        while (true) {
            String chute = ValidarInput
                    .validateInput(scanner, "Entre com numero secreto ou [enter]=sair ",
                            (String s) -> s.isEmpty() | s.matches("^\\d+$"));
            if (chute.isEmpty()) break;

            chuteValue = Integer.parseInt(chute);
            this.countTentativas++;

            if (!validaIntervalo(chuteValue)) {
                System.out.println("Chute deve estar no intervalo entre " + geradorDeNumeroAleatorio.getMinimo()
                        + " e " + geradorDeNumeroAleatorio.getMaximo());
                continue;
            }
            result = Optional.of(chuteValue);
            break;
        }
        return result;
    }

    private Optional<Pair> leituraDoIntervaloNumeroAleatorio() {
        Scanner scanner = new Scanner(System.in);
        Optional<Pair> pair = Optional.empty();
        while (true) {
            String start = ValidarInput.validateInput(scanner, "Digite o intervalo inicial ou [Enter]=sair: ",
                    (String s) -> s.matches("^\\d+$") | s.isEmpty());
            if (start.isEmpty()) break;

            String end = ValidarInput.validateInput(scanner, "Digite o intervalo final ou [Enter]=sair: ",
                    (String s) -> s.matches("^\\d+$") | s.isEmpty());
            if (end.isEmpty()) break;

            if ((Integer.parseInt(start) > Integer.parseInt(end))) {
                System.out.println("Intervalo incorreto, informe novamente!");
                continue;
            }
            return pair = Optional.of(new Pair(Integer.parseInt(start), Integer.parseInt(end)));
        }
        return pair;
    }


    private static void printImageASCII() {
        /**
         * Requerido JVM 15 ou superior
         *
         *
         * Caso a versão da JVM seja inferior a 15
         * Este bloco de codigo deve ser excluido
         *
         */
        if (Integer.parseInt(javaVersion) >= 15) {
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
        }
        logger.info("Aluno: Enalto de Oliveira Gondrige");
        logger.info("RA: 22114039-5");
        logger.info("Bem-vindo ao Jogo da Advinhação!");
        logger.info("Tente adivinhar o número secreto.");

    }

    private boolean validaIntervalo(int inputNumber) {
        if (!(geradorDeNumeroAleatorio.maximoEhMaiorQue(inputNumber)
                && geradorDeNumeroAleatorio.minimoEhMenorQue(inputNumber))) {
            return false;
        }
        return true;
    }

    /**
     * Cria um objeto GeradorDeNumeroAleatorio que contem o numero aleatorio
     * gerado
     *
     * @return GeradorDeNumeroAleatorio
     */
    private GeradorDeNumeroAleatorio gerarNumeroAleatorio(Pair pair) {
        var numeroAleatorio = new GeradorDeNumeroAleatorio.Builder()
                .comIntervaloInicial(pair.start)
                .comIntervaloFinal(pair.end)
                .build();
        this.geradorDeNumeroAleatorio = numeroAleatorio;
        return numeroAleatorio;
    }

    private record Pair(int start, int end) {
    }

}


/**
 * Classe GeradorDeNumeroAleatorio, para gerar o numero aleatorio
 */
class GeradorDeNumeroAleatorio {
    private int minimo;
    private int maximo;
    private int numeroGerado;

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
                    throw new IllegalStateException("Unexpected value: "
                            + Integer.compare(input, this.numeroGerado));
        };

    }

}

enum InputCompare {
    IGUAL,
    MAIOR,
    MENOR,
}