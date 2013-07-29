package aig;

import FlexMap.Algorithms;
import expanderExpression.BuilderTree;
import graph.*;
import handleExpression.*;
import io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Classe que transforma descrição EQN em AIGER novamente 
 * @author joaojrmachado
 */
public class Eqn extends Graph {

    protected ArrayList<ArrayList<String>> definitionEqn;
    protected int numInputs, numOutputs, numAndGates, numMaxVarIndex, numLatch;

    //
    /**
     * Construtor
     * @param fileName (nomeArquivo de Eqn em ascii '.eqn')
     */
    public Eqn(String fileName) throws FileNotFoundException, IOException {
        System.out.println("#EQN carregado com sucesso");
        this.parserEqn(fileName);
    }

    public void parserEqn(String file) throws FileNotFoundException, IOException {
        StringTokenizer lines;

        //vai tentar abrir o arquivo passado por parâmetro
        String fileInput = Readfile.LoadFileToString(file);

        //se o arquivo nao for encontrado, mensagem de erro
        if (fileInput == null) {
            System.out.println("O arquivo não foi carregado com sucesso.");
            System.exit(1);
        }

        lines = new StringTokenizer(fileInput, "\n");                           //o token delimitado eh uma quebra de linha

        ArrayList<String> inputs = new ArrayList<String>();
        ArrayList<String> outputs = new ArrayList<String>();
        ArrayList<String> andGates = new ArrayList<String>();

        this.definitionEqn = new ArrayList<ArrayList<String>>();
        while (lines.hasMoreElements()) //enquanto encontrar quebra de linha
        {
            String subline = lines.nextToken();                                                                  //subline recebe a linha apontada pelo lines
            StringTokenizer line = new StringTokenizer(subline, " ");                                            //line token delimitado por espacos

            while (line.hasMoreElements()) //enquanto nao chegou o fim da linha   
            {
                if (!line.toString().contains("#")) //se nao tiver '#' que representa comentarios
                {
                    String token = line.nextToken();                                                             //token eh uma string que recebe o proximo token de line
                    /**
                     * BEGIN INPUTS *
                     */
                    if (token.contains("INORDER")) //if token contem a string "INORDER"
                    {
                        while (!token.contains(";")) //while for diferente de ';'
                        {
                            token = line.nextToken();                                                            //string token recebe próximo token de line
                            if (token.contains("pi_")) //se token conter 'pi_'
                            {
                                inputs.add(token.replace("pi_", ""));                                          //subtitui 'pi_' por ''                                
                                this.numInputs++;
                            }
                        }
                        inputs.set(inputs.size() - 1, inputs.get(inputs.size() - 1).replace(";", ""));  //substitui ultimo elemento que ficou com ';' no final, por ''
                        this.definitionEqn.add(inputs);
                    }
                    /**
                     * END INPUTS *
                     */
                    /**
                     * BEGIN OUTPUTS *
                     */
                    if (token.contains("OUTORDER")) //if token contem a string "INORDER"
                    {
                        while (!token.contains(";")) //while for diferente de ';'
                        {
                            token = line.nextToken();                                                            //string token recebe próximo token de line
                            if (token.contains("po_")) //se token conter 'pi_'
                            {
                                outputs.add(token.replace("po_", ""));                                          //subtitui 'pi_' por ''                                
                                this.numOutputs++;
                            }
                        }
                        outputs.set(outputs.size() - 1, outputs.get(outputs.size() - 1).replace(";", ""));  //substitui ultimo elemento que ficou com ';' no final, por ''
                        this.definitionEqn.add(outputs);
                    }
                    /**
                     * END INPUTS *
                     */
                }
            }
            if (numInputs != 0 && numOutputs != 0) {
                for (int i = 0; i < (numInputs + numOutputs); i++) {
                    lines.nextToken();
                }

                while (lines.hasMoreElements()) {
                    String lineCurrent = lines.nextToken();
                    lineCurrent = lineCurrent.replace("[", "");
                    lineCurrent = lineCurrent.replace("]", "");
                    lineCurrent = lineCurrent.replace("=", " ");
                    lineCurrent = lineCurrent.replace(";", "");
                    lineCurrent = lineCurrent.replace(";", "");
                    StringTokenizer tokenLine = new StringTokenizer(lineCurrent, " ");
                    tokenLine.nextToken();
                    lineCurrent = tokenLine.nextToken();
                    System.out.println(lineCurrent);
                    int index  = Algorithms.findRoot(lineCurrent);
                    System.out.println(lineCurrent.charAt(index));
                    BuilderTree myTree = new BuilderTree();
                    myTree.run(lineCurrent);
                    myTree.showQueue();
//                    lineCurrent = lineCurrent.replace("*", "");
//
//                    if (lineCurrent.contains("!")) {
//                        StringTokenizer tokenLine = new StringTokenizer(lineCurrent, " ");
//                        String lineCorrect = "";
//                        String inversor;
//                        while (tokenLine.hasMoreElements()) {
//                            inversor = tokenLine.nextToken();
//                            if (inversor.contains("!")) {
//                                StringTokenizer removeInversor = new StringTokenizer(inversor, "!");
//                                inversor = removeInversor.nextToken();
//                                lineCorrect += String.valueOf(Integer.parseInt(inversor) + 1);
//                            } else {
//                                lineCorrect += inversor;
//                            }
//                            lineCorrect += " ";
//                        }
//                        andGates.add(lineCorrect);
//                    } else {
//                        StringTokenizer tokenLine = new StringTokenizer(lineCurrent, " ");
//                        String lineCorrect = "";
//                        String inversor;
//                        while (tokenLine.hasMoreElements()) {
//                            inversor = tokenLine.nextToken();
//                            lineCorrect += inversor;
//                            lineCorrect += " ";
//                        }
//                        andGates.add(lineCorrect);
//                        lineCorrect = "";
//                    }
//                    System.out.println(andGates.get(andGates.size()-1));
//                    this.numAndGates++;
//                }
//
//                this.definitionEqn.add(andGates);
//                numMaxVarIndex = numInputs + numAndGates;
//
//                String fileOutput = "";
//                fileOutput += ("aag " + numMaxVarIndex + " " + numInputs + " " + numLatch + " " + numOutputs + " " + numAndGates + "\n");
//                for (ArrayList<String> elementExt : definitionEqn) {
//                    for (String elementIn : elementExt) {
//                        fileOutput += elementIn + "\n";
//                    }
                }
//                fileOutput = fileOutput.substring(0, (fileOutput.length() - 1));
//                String newFile = file.replace(".eqn", ".aag");
//                System.out.println(fileOutput);
//                Logs.LogsWriteAig(fileOutput, newFile);
                break;
            }
        }
    }
}