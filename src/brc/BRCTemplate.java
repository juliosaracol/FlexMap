package brc;

import java.util.ArrayList;

public class BRCTemplate {

    public BRCTemplate() {
    }

    public static ArrayList<BRC> getBRCStructure(int amountVariable) {

        ArrayList<BRC> struct = new ArrayList<BRC>();

        switch (amountVariable) {
            case 1:
                struct.add(new BRC());
                struct.get(0).setBRC(0, 1);
                break;
                
            case 2:
                struct.add(new BRC());
                struct.get(0).setBRC(0, 5);
                struct.add(new BRC());
                struct.get(1).setBRC(0, 3);
                break;

            case 3:
                struct.add(new BRC());
                struct.get(0).setBRC(0, 85);
                struct.add(new BRC());
                struct.get(1).setBRC(0, 51);
                struct.add(new BRC());
                struct.get(2).setBRC(0, 15);
                break;

            case 4:
                struct.add(new BRC());
                struct.get(0).setBRC(0, 21845);
                struct.add(new BRC());
                struct.get(1).setBRC(0, 13107);
                struct.add(new BRC());
                struct.get(2).setBRC(0, 3855);
                struct.add(new BRC());
                struct.get(3).setBRC(0, 255);
                break;

            case 5:
                struct.add(new BRC());
                struct.get(0).setBRC(0, 1431655765);
                struct.add(new BRC());
                struct.get(1).setBRC(0, 858993459);
                struct.add(new BRC());
                struct.get(2).setBRC(0, 252645135);
                struct.add(new BRC());
                struct.get(3).setBRC(0, 16711935);
                struct.add(new BRC());
                struct.get(4).setBRC(0, 65535);
                break;
            default:
                System.err.println("\n Error!");
        }

        return struct;
    }
    
    public static Integer getIntegerBRC(int amountVariable) {

        int temp = 0;

        switch (amountVariable) {
            case 1:
                temp = 1431655765;
                break;
            case 2:
                temp = 858993459;
                break;
            case 3:
                temp = 252645135;
                break;
            case 4:
                temp = 16711935;
                break;
            case 5:
                temp = 65535;
                break;
            default:
                System.err.println("\n Error!");
        }

        return temp;
    }
    
}
