import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class DividirEConquistar {

    private static int iteracoes; //leva em conta operacoes aritmeticas, comparacoes e chamadas recursivas
    private static int repeticoes = 500;
    private static List<String[]> data = new ArrayList<>();

    public static void main(String [] args) {
        long t1 = 0, t2 = 0, t = 0;

        BigInteger num1 = new BigInteger("5368635887");
        BigInteger num2 = new BigInteger("3524672352");
        num1.multiply(num2);

        t1 = currentTime();
        analise("mergeSort");
        analise("maxVal1");
        analise("maxVal2");
        analise("multiply");
        
        t2 = currentTime();
        t = t2-t1;

        System.out.printf("Tempo Total: %ds", t/1000000000);
    }

    public static void writeCsv(String fileName, List<String[]> data) {
        File file = new File(fileName);

        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("Failed to delete existing file: " + fileName);
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("metodo,tempo,iteracoes");
            for (String[] row : data) {
                writer.newLine();
                writer.write(String.join(",", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void analise(String metodo) {

        for (int i = 0; i < repeticoes; i++) {
            switch (metodo) {
                case "multiply":
                    analiseMultiply(metodo);
                    break;
                case "mergeSort":
                    genericAnalise(metodo, (list) -> mergeSort(list));
                    break;
                case "maxVal1":
                    genericAnalise(metodo, (list) -> maxVal1(list));
                    break;
                case "maxVal2":
                    genericAnalise(metodo, (list) -> maxVal2(list, 0, list.size() - 1));
                    break;
                default:
                    System.out.println("Método não reconhecido.");
                    break;
            }
        }
        
        writeCsv("dadosDividirEConquistar.csv", data);

        System.out.println("-------------------------------");
    }

    private static void genericAnalise(String metodo, Consumer<List<Integer>> function) {
        int[] valores = new int[]{32, 2048, 1048576};

        for (int valor : valores) {
            System.out.println("----" + metodo.toUpperCase() + "----");
            System.out.printf("Lista com %d numeros.%n", valor);
            iteracoes = 0;

            List<Integer> list = geraLista(valor / 2, valor / 2);

            long t1 = currentTime();
            function.accept(list);
            long t2 = currentTime();
            long t = t2 - t1;

            System.out.printf("Tempo decorrido: %d ns.%n", t);
            System.out.printf("Operações: %d%n%n", iteracoes);
            data.add(createData(metodo.concat("_" + String.valueOf(valor)+"_numeros"), t, iteracoes));
        }
    }

    private static void analiseMultiply(String metodo) {
        long[] valores_multiply = new long[6];
        Random rnd = new Random();
        int[] bitLengths = {4, 8, 16};
    
        for (int i = 0; i < valores_multiply.length; i++) {
            int bitLength = bitLengths[i / 2];  
            
            long maxVal = (1L << bitLength) - 1;
            long rand = rnd.nextLong() & maxVal; 
            valores_multiply[i] = rand;
        }
    
        for (int i = 0; i < valores_multiply.length - 1; i += 2) {
            System.out.println("----MULTIPLY----");
            long atual = valores_multiply[i];
            long prox = valores_multiply[i + 1];
            iteracoes = 0;
    
            System.out.printf("Multiplicando %d e %d: ", atual, prox);
            long t1 = currentTime();
            System.out.println(multiply(atual, prox, numBits(atual)));
    
            long t2 = currentTime();
            long t = t2 - t1;
            System.out.printf("Tempo decorrido: %d ns.%n", t);
            System.out.printf("Operações: %d%n%n", iteracoes);
            data.add(createData(metodo.concat(String.valueOf("_"+numBits(atual))+"_bits"), t, iteracoes));
        }
    }

    private static String[] createData(String metodo, long t, int iteracoes) {
        String[] dados = new String[]{metodo, String.valueOf(t), String.valueOf(iteracoes)};
        
        return dados;
    }

    private static int numBits(long n) {
        if (n == 0)
            return 0;
        if (n == 1) 
            return 1;
        return 1 + numBits(n / 2);
    }

    public static int maxVal1(List<Integer> A) {  
        int max = A.get(0);
        for (int i = 1; i < A.size(); i++) {  
            if( A.get(i) > max ) {
                iteracoes++;
                max = A.get(i);
            }
            iteracoes++;
        }
        return max;
    }

    public static int maxVal2(List<Integer> A, int init, int end) {  
        if (end - init <= 1){
            iteracoes++;
            return max(A.get(init), A.get(end));  
        }
        else {
              int m = (init + end)/2;
              int v1 = maxVal2(A,init,m);   
              int v2 = maxVal2(A,m+1,end);
              iteracoes += 3;  
              return max(v1,v2);
             }
    }

    private static int max(int a, int b) {
        if (a >= b) {
            iteracoes++;
            return a;
        }
        return b;
    }

    private static long currentTime() {
        return System.nanoTime();
    }

    public static long multiply(long x, long y, long n) {
        if (n == 1){
            iteracoes++;
            return x * y;
        }

        else if (n > 1){
            long m = n / 2;
            long a = x >> m;
            long b = x & ((1 << m) - 1);
            long c = y >> m;
            long d = y & ((1 << m) - 1);
            iteracoes += 9;
        
            long e = multiply(a, c, m);
            long f = multiply(b, d, m);
            long g = multiply(b, c, m);
            long h = multiply(a, d, m);
    
            iteracoes += 4;
        
            iteracoes++;
            return (e << (2 * m)) + ((g + h) << m) + f;
        }
        return 0;
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
        if (list.size() <= 1) {
            iteracoes++;
            return list;
        }

        int mid = list.size() / 2;
        List<Integer> left = mergeSort(list.subList(0, mid));
        List<Integer> right = mergeSort(list.subList(mid, list.size()));

        iteracoes += 3;

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
            iteracoes += 4;
        }
        iteracoes++;

        while (i < l1.size()) {
            mergedList.add(l1.get(i));
            i++;
            iteracoes += 2;
        }
        iteracoes++;

        while (j < l2.size()) {
            mergedList.add(l2.get(j));
            j++;
            iteracoes += 2;
        }
        iteracoes++;

        return mergedList;
    }
}