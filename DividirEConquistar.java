import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DividirEConquistar {
    public static void main(String [] args) {
        analise("mergeSort");
        analise("maxVal1");
        analise("maxVal2");

    }

    private static void analise(String metodo) {
        int[] valores = new int[] {32, 2048, 1048576};
        long t1 = 0;
        long t2 = 0;
        long t;

        for (int i = 0; i < valores.length; i++) {
            System.out.println("----" + metodo + "----");
            System.out.printf("Lista com " + valores[i] + " numeros.");
            System.out.println();
            
            List<Integer> list = geraLista(valores[i]/2, valores[i]/2);

            if (metodo.equals("mergeSort")){
                t1 = currentTime();
                mergeSort(list);
            } 
            else if (metodo.equals("maxVal1")){
                t1 = currentTime();
                maxVal1(list);
            } 
            else if (metodo.equals("maxVal2")){
                t1 = currentTime();
                maxVal2(list, 0, list.size()-1);
            }

            t2 = currentTime();
            t = t2-t1;

            System.out.printf("Tempo decorrido : " + t + "ms");
            System.out.println();
            System.out.println();
        }
        System.out.println("-------------------------------");
    }

    public static int maxVal1(List<Integer> A) {  
        int max = A.get(0);
        for (int i = 1; i < A.size(); i++) {  
            if( A.get(i) > max ) 
               max = A.get(i);
        }
        return max;
    }

    public static int maxVal2(List<Integer> A, int init, int end) {  
        if (end - init <= 1)
            return max(A.get(init), A.get(end));  
        else {
              int m = (init + end)/2;
              int v1 = maxVal2(A,init,m);   
              int v2 = maxVal2(A,m+1,end);  
              return max(v1,v2);
             }
    }

    private static int max(int a, int b) {
        if (a >= b) 
            return a;
        return b;
    }

    private static Long currentTime() {
        return System.currentTimeMillis();
    }

    private static List<Integer> geraLista(int nroPares, int nroImpares) {
        List<Integer> res = new ArrayList<>();
        int contPar = 0, contImpar = 0;
        Random rnd = new Random();

        if (nroPares >= 0 && nroImpares >= 0 && (nroPares + nroImpares > 0)) {
            while (contPar < nroPares || contImpar < nroImpares) {
                int novoNum = rnd.nextInt(98) + 1;

                if (novoNum % 2 == 0 && contPar < nroPares) {
                    res.add(novoNum);
                    contPar++;
                } else if (novoNum % 2 == 1 && contImpar < nroImpares) {
                    res.add(novoNum);
                    contImpar++;
                }
            }
        }
        return res;
    }

    public static List<Integer> mergeSort(List<Integer> list) {
        // Base case: a lista com 1 ou nenhum elemento já está ordenada
        if (list.size() <= 1) 
            return list;

        int mid = list.size() / 2;
        List<Integer> left = mergeSort(list.subList(0, mid));
        List<Integer> right = mergeSort(list.subList(mid, list.size()));

        return merge(left, right);
    }

    private static List<Integer> merge(List<Integer> l1, List<Integer> l2) {
        List<Integer> mergedList = new ArrayList<>();
        int i = 0, j = 0;

        while (i < l1.size() && j < l2.size()) {
            if (l1.get(i) <= l2.get(j)) {
                mergedList.add(l1.get(i));
                i++;
            } else {
                mergedList.add(l2.get(j));
                j++;
            }
        }

        while (i < l1.size()) {
            mergedList.add(l1.get(i));
            i++;
        }

        while (j < l2.size()) {
            mergedList.add(l2.get(j));
            j++;
        }

        return mergedList;
    }
}