package interpretfunction;
/**
 *
 * @author renato
 */
public class Heap {

    public static int leftHeap(int father) {
        int left = father;
        left <<= 1;
        return left;
    }

    public static int rightHeap(int father) {
        int right = father;
        right <<= 1;
        right |= 1;
        return right;
    }
    
}
