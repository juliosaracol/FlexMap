package brc;

import java.util.ArrayList;

public class BRCTemplate {

    public BRCTemplate() {
    }

    public static ArrayList<BRC> getBRCStructure(int amountVariable) {

        ArrayList<BRC> struct = new ArrayList<BRC>();
        int nBits = (int)(Math.pow(2, amountVariable));

        switch (amountVariable) {
            case 1:
                struct.add(new BRC(nBits));
                struct.get(0).setBRC(0, 2L); // 1
                break;
                
            case 2:
                struct.add(new BRC(nBits));
                struct.get(0).setBRC(0, 10L); // 5
                struct.add(new BRC(nBits));
                struct.get(1).setBRC(0, 12L); // 3
                break;

            case 3:
                struct.add(new BRC(nBits));
                struct.get(0).setBRC(0, 170L); // 85
                struct.add(new BRC(nBits));
                struct.get(1).setBRC(0, 204L); // 51
                struct.add(new BRC(nBits));
                struct.get(2).setBRC(0, 240L); // 15
                break;

            case 4:
                struct.add(new BRC(nBits));
                struct.get(0).setBRC(0, 43690L); // 21845
                struct.add(new BRC(nBits));
                struct.get(1).setBRC(0, 52428L); // 13107
                struct.add(new BRC(nBits));
                struct.get(2).setBRC(0, 61680L); // 3855
                struct.add(new BRC(nBits));
                struct.get(3).setBRC(0, 65280L); // 255
                break;

            case 5:
                struct.add(new BRC(nBits));
                struct.get(0).setBRC(0, 2863311530L); // 1431655765
                struct.add(new BRC(nBits));
                struct.get(1).setBRC(0, 3435973836L); // 858993459
                struct.add(new BRC(nBits));
                struct.get(2).setBRC(0, 4042322160L); // 252645135
                struct.add(new BRC(nBits));
                struct.get(3).setBRC(0, 4278255360L); // 16711935
                struct.add(new BRC(nBits));
                struct.get(4).setBRC(0, 4294901760L); // 65535
                break;
                
            case 6:
                struct.add(new BRC(nBits));
                struct.get(0).setBRC(0, -6148914691236517206L); 
                struct.add(new BRC(nBits));
                struct.get(1).setBRC(0, -3689348814741910324L);
                struct.add(new BRC(nBits));
                struct.get(2).setBRC(0, -1085102592571150096L);
                struct.add(new BRC(nBits));
                struct.get(3).setBRC(0, -71777214294589696L);
                struct.add(new BRC(nBits));
                struct.get(4).setBRC(0, -281470681808896L);
                struct.add(new BRC(nBits));
                struct.get(5).setBRC(0, -4294967296L);
                break;
                
            default:
                System.err.println("\n Error!");
        }

        return struct;
    }
    
    public static long getIntegerBRC(int amountVariable) {

        long temp = 0;

        switch (amountVariable) {
            case 1:
                temp = -6148914691236517206L;
                break;
            case 2:
                temp = -3689348814741910324L;
                break;
            case 3:
                temp = -1085102592571150096L;
                break;
            case 4:
                temp = -71777214294589696L;
                break;
            case 5:
                temp = -281470681808896L;
                break;
            case 6:
                temp = -4294967296L;
                break;
            default:
                System.err.println("\n Error!");
        }

        return temp;
    }
    
}
