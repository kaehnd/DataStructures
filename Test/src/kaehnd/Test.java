package kaehnd;


import java.io.File;
import java.io.PrintWriter;
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.
        7List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Test {


    public static void main(String[] args) throws Exception{

        System.out.println("Dodrugs");

        try(Scanner in = new Scanner(new File("theFile.txt")); PrintWriter out = new PrintWriter(new File("tonystuff.csv"))) {
            while (in.hasNextLine()) {
                double a = in.nextDouble();
                double b = in.nextDouble();
                double c = in.nextDouble();

                out.println(a + "," + b + "," + c);

            }
        }
        System.out.println("done");
    }


    private static List<Integer> doDrugs(List<Double> integerList){
        int s = integerList.stream()
                .mapToInt(d -> d.intValue())
                .sum();
        return null;
    }



    private static void doStringDrugs(List<String> stringList){
        List<String> newStringList = stringList.stream()
                .filter(s -> s.length() > 5)
                .collect(Collectors.toList());
    }
}
