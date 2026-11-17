import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        double[] chuvas = new double[7];
        String[] dias = {
                "Segunda-feira", "Terça-feira", "Quarta-feira",
                "Quinta-feira", "Sexta-feira", "Sábado", "Domingo"
        };
        double[][] umidade = new double[4][4];

        boolean dadosCadastrados = false;
        int opcao;

        do {
            System.out.println("AGROJAVA - Sistema Integrado de Agronegócio");
            System.out.println();
            System.out.print("Seja bem-vindo(a) ao AGROJAVA, leia as opções e em seguida digite qual deseja para continuar: \n");
            System.out.println("Digite 1 - Para cadastrar dados (Chuvas e Umidade)");
            System.out.println("Digite 2 - Para exibir mapa do campo e relatório de chuvas");
            System.out.println("Digite 3 - Para relatório de alertas de irrigação");
            System.out.println("Digite 4 - Para registrar semana agrícola"); // RF04
            System.out.println("Digite 5 - Para consultar histórico de chuvas"); // RF05
            System.out.println("Digite 6 - Para identificar semana mais chuvosa"); // RF06
            System.out.println("Digite 7 - Para sair");
            System.out.println("Escolha uma opcao: ");

            opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("CADASTRO DE PLUVIOSIDADE (7 dias)");
                    for (int i = 0; i < 7; i++) {
                        System.out.print("Volume de chuva em " + dias[i] + " (mm): ");
                        chuvas[i] = entrada.nextDouble();
                    }
                    System.out.println();
                    System.out.println("CADASTRO DE UMIDADE DOS TALHÕES");
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            System.out.print("Informe a umidade do talhão [" + (i + 1) + "][" + (j + 1) + "] (%): ");
                            umidade[i][j] = entrada.nextDouble();
                        }
                    }
                    dadosCadastrados = true;
                    System.out.println("Dados cadastrados com sucesso!");
                    System.out.println();
                    break;

                case 2:
                    if (!dadosCadastrados) {
                        System.out.println("Cadastre os dados primeiro selecionando a opção 1.");
                        break;
                    }

                    double somaChuva = 0;
                    double maiorChuva = chuvas[0];
                    int diaMaiorChuva = 0;

                    for (int i = 0; i < 7; i++) {
                        somaChuva += chuvas[i];
                        if (chuvas[i] > maiorChuva) {
                            maiorChuva = chuvas[i];
                            diaMaiorChuva = i;
                        }
                    }
                    double mediaChuva = somaChuva / 7;

                    System.out.println("RELATÓRIO DE PLUVIOSIDADE");
                    System.out.printf("Média semanal de precipitação: %.2f mm\n", mediaChuva);
                    System.out.println("Dia com maior índice de chuva: " + dias[diaMaiorChuva] + " (" + maiorChuva + " mm)");

                    System.out.println("MAPA DE UMIDADE DO CAMPO");
                    System.out.println("            Col 1 Col 2 Col 3 Col 4");
                    for (int i = 0; i < 4; i++) {
                        System.out.print("Talhão Linha " + (i + 1) + "");
                        for (int j = 0; j < 4; j++) {
                            System.out.printf(" %.1f", umidade[i][j]);
                        }
                        System.out.println();
                    }
                    break;

                case 3:
                    if (!dadosCadastrados) {
                        System.out.println("Cadastre os dados primeiro selecionando a opção 1.");
                        break;
                    }

                    System.out.println("RELATÓRIO DE ALERTAS DE IRRIGAÇÃO");
                    boolean alertaEncontrado = false;

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (umidade[i][j] < 30.0) {
                                System.out.printf("ALERTA: O talhão [%d][%d] está com %.1f%% de umidade e necessita de irrigação imediata!\n",
                                        (i + 1), (j + 1), umidade[i][j]);
                                alertaEncontrado = true;
                            }
                        }
                    }

                    if (!alertaEncontrado) {
                        System.out.println("Todos os talhões estão com umidade adequada (>= 30%). Nenhuma irrigação necessária no momento.");
                    }
                    break;

                case 4:
                    entrada.nextLine(); // Limpa o buffer do teclado
                    System.out.println("REGISTRAR SEMANA AGRÍCOLA"); // RF04
                    System.out.print("Informe a data da semana (ex: 10/11/2026): ");
                    String dataSemana = entrada.nextLine();

                    double[] chuvasSemana = new double[7];
                    double totalSemana = 0;

                    for (int i = 0; i < 7; i++) {
                        System.out.print(" Chuva de " + dias[i] + " (mm): ");
                        chuvasSemana[i] = entrada.nextDouble();
                        totalSemana += chuvasSemana[i];
                    }

                    double mediaSemana = totalSemana / 7.0;

                    File arquivo = new File("semanas_agricolas.csv");
                    boolean arquivoNovo = !arquivo.exists() || arquivo.length() == 0;

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
                        if (arquivoNovo) {
                            writer.write("data,seg,ter,qua,qui,sex,sab,dom,media,total");
                            writer.newLine();
                        }

                        String registro = String.format(Locale.US, "%s,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.0f,%.1f,%.0f",
                                dataSemana,
                                chuvasSemana[0], chuvasSemana[1], chuvasSemana[2], chuvasSemana[3],
                                chuvasSemana[4], chuvasSemana[5], chuvasSemana[6],
                                mediaSemana, totalSemana);

                        writer.write(registro);
                        writer.newLine();
                        System.out.println("Semana agrícola registrada com sucesso no arquivo CSV!");
                    } catch (IOException e) {
                        System.out.println("Erro ao gravar o arquivo CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 5:
                    System.out.println("HISTÓRICO DE CHUVAS"); // RF05
                    File arquivoHistorico = new File("semanas_agricolas.csv");

                    if (!arquivoHistorico.exists()) {
                        System.out.println("Nenhum registro encontrado. Cadastre uma semana na opção 4 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoHistorico))) {
                        String linha = reader.readLine(); // Lê e ignora o cabeçalho
                        boolean possuiRegistros = false;

                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            String data = colunas[0];
                            double media = Double.parseDouble(colunas[8]);
                            double total = Double.parseDouble(colunas[9]);

                            System.out.println("Semana: " + data);
                            System.out.printf(Locale.US, "Média: %.0f mm\n", media);
                            System.out.printf(Locale.US, "Total: %.0f mm\n", total);
                            System.out.println("-------------------------");
                            possuiRegistros = true;
                        }

                        if (!possuiRegistros) {
                            System.out.println("O arquivo está vazio. Nenhum registro salvo.");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 6:
                    System.out.println("IDENTIFICAR SEMANA MAIS CHUVOSA"); // RF06
                    File arquivoMaisChuvosa = new File("semanas_agricolas.csv");

                    if (!arquivoMaisChuvosa.exists()) {
                        System.out.println("Nenhum registro encontrado. Cadastre uma semana na opção 4 primeiro.");
                        System.out.println();
                        break;
                    }

                    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoMaisChuvosa))) {
                        String linha = reader.readLine();
                        String semanaMaisChuvosa = "";
                        double maiorTotal = -1;

                        while ((linha = reader.readLine()) != null) {
                            if (linha.trim().isEmpty()) {
                                continue;
                            }
                            String[] colunas = linha.split(",");
                            String data = colunas[0];
                            double total = Double.parseDouble(colunas[9]);

                            if (total > maiorTotal) {
                                maiorTotal = total;
                                semanaMaisChuvosa = data;
                            }
                        }

                        if (maiorTotal >= 0) {
                            System.out.println("Semana mais chuvosa: " + semanaMaisChuvosa);
                            System.out.printf(Locale.US, "Total: %.0f mm\n", maiorTotal);
                        } else {
                            System.out.println("Nenhum registro válido encontrado no arquivo.");
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao analisar o arquivo CSV: " + e.getMessage());
                    }
                    System.out.println();
                    break;

                case 7:
                    System.out.println("Encerrando o sistema.");
                    break;

                default:
                    System.out.println("Opção inválida! Escolha um número entre 1 e 7.");
            }
        } while (opcao != 7);

        entrada.close();
    }
}