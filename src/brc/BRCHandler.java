package brc;

import java.util.ArrayList;
import java.util.TreeMap;

public class BRCHandler {

    public BRCHandler() {
    
    }
    
    /* Constroi o BRC da funcao */
    public static BRC builFunctionRepresentationCode(ArrayList<ArrayList<String>> cubes, TreeMap<String, BRC> basicRepresentationCodes) {
       
        BRC first_code;
        BRC second_code;
        BRC directCode;
        BRC invCode;
        ArrayList<BRC> cubesCode = new ArrayList<BRC>();
        
        // Cria o BRC de cada cubo
        for(ArrayList<String> cube: cubes) {
            for(String literal: cube) {
                if(!basicRepresentationCodes.containsKey(literal)) {
                    if(literal.charAt(0) == '!') {
                        directCode = basicRepresentationCodes.get(literal.substring(1));
                        directCode = directCode.clone();
                        invCode = BRCHandler.not(directCode);
                        basicRepresentationCodes.put(literal, invCode);
                    }
                    else {
                        System.out.println("Error, any BRC was not found in the TrreMap!");
                    }
                }
                
            }
            cubesCode.add(buildCubesRepresentationCode(cube, basicRepresentationCodes));
        }
        
        // Calcula o BRC da funcao
        first_code = cubesCode.get(0);
        for(int i=0; i< cubesCode.size(); i++) {
            second_code = cubesCode.get(i);
            first_code = BRCHandler.or(first_code, second_code);   
        }
        
        return first_code;
    }

    /* Cria o BRC de cada cubo */
    public static BRC buildCubesRepresentationCode(ArrayList<String> cube, TreeMap<String, BRC> basicRepresentationCodes) {
        
        BRC first_code = basicRepresentationCodes.get(cube.get(0));
        BRC second_code;
        for(int i=1; i < cube.size(); i++) {
            second_code = basicRepresentationCodes.get(cube.get(i));
            first_code = BRCHandler.and(first_code, second_code);   
        }
        
        return first_code;
    }
    
    public static BRC and(BRC obj1, BRC obj2) {
        int and;
        BRC brc = new BRC();

        for (int i = 0; i < obj1.sizeBRC(); i++) {
            and = (obj1.getBRC(i)) & (obj2.getBRC(i));
            brc.setBRC(i, and);
        }

        return brc;
    }

    public static BRC or(BRC obj1, BRC obj2) {
        int or;
        BRC brc = new BRC();

        for (int i = 0; i < obj1.sizeBRC(); i++) {
            or = (obj1.getBRC(i)) | (obj2.getBRC(i));
            brc.setBRC(i, or);
        }

        return brc;
    }

    public static BRC not(BRC obj) {
        int inverter;
        BRC brc = new BRC();

        for (int i = 0; i < obj.sizeBRC(); i++) {
            inverter = ~(obj.getBRC(i));
            brc.setBRC(i, inverter);
        }

        return brc;
    }

    public static boolean equal(BRC obj1, BRC obj2) {

        for (int i = 0; i < obj1.sizeBRC(); i++) {
            if (obj1.getBRC(i) != obj2.getBRC(i)) {
                return false;
            }
        }

        return true;
    }
    
    public static String toHexaString(BRC brc) {
        
        String hexa = "";
        
        for(int i = 0; i < brc.sizeBRC(); i++) {
            hexa += Integer.toHexString(brc.getBRC(i));
        }
        
        return hexa;
        
    }
    
    public static String toIntegerString(BRC brc) {
        
        String str = "";
        
        for(int i = 0; i < brc.sizeBRC(); i++) {
            str += brc.getBRC(i) + " ";
        }
        
        return str;
        
    }
    
    public static void displayInteger(BRC brc) {
        
        for(int i=0; i < brc.sizeBRC(); i++) {
            System.out.print(brc.getBRC(i) + " ");
        }
        
        System.out.println();        
    }
    
    public static void displayBinary(BRC brc) {

        int value;
        
        for(int i=0; i < brc.sizeBRC(); i++) {
            
            value = brc.getBRC(i);

            /*cria um valor inteiro com 1 no bit mais a esquerda e 0s em outros locais*/
            int displayMask = 1 << 31;

            /*para cada bit exibe 0 ou 1*/
            for (int bit = 1; bit <= 32; bit++) {
                /*utiliza displayMask para isolar o bit*/
                System.out.print((value & displayMask) == 0 ? '0' : '1');

                value <<= 1; //desloca o valor uma posição para a esquerda

                if (bit % 8 == 0) {
                    System.out.print(' ');//espaço a cada 8 bits
                }
            }
        }
        
        System.out.println();
    }
    
}
