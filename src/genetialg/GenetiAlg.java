package genetialg;

import java.io.*;
import java.util.*;

public class GenetiAlg {

    public static LinkedHashMap sortHashMapByValues(HashMap passedMap, boolean ascending) {//сортеровочка :)

        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        if (!ascending) {
            Collections.reverse(mapValues);
        }

        LinkedHashMap someMap = new LinkedHashMap();
        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                if (passedMap.get(key).toString().equals(val.toString())) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    someMap.put(key, val);
                    break;
                }
            }
        }
        return someMap;
    }

    public static int ocenka(int numberOfSkobka, int lenthOfOsob, String s, List<List<Integer>> skobki) {//оцeнка особи
        int score = 0;
        for (int j = 0; j < numberOfSkobka; j++) {//оценка особи
            List<Integer> local = skobki.get(j);
            for (int it = 0; it < lenthOfOsob; it++) {
                if ((s.charAt(it) == '0' && Integer.toString(local.get(it)).equals("-1")) || (s.charAt(it) == '1' && Integer.toString(local.get(it)).equals("1"))) {
                    score++;
                    break;
                }
            }
        }
        return score;
    }

    public static void main(String[] args) throws IOException, ConcurrentModificationException {
        File file = new File("inputP.txt");
        Scanner in = new Scanner(file);

        File param = new File("param.txt");
        Scanner params = new Scanner(param);

        int lenthOfOsob = in.nextInt();//длинна особи
        int numberOfSkobka = in.nextInt();//количество скобок
        //System.out.println(numberOfSkobka + " " + lenthOfOsob);

        List<List<Integer>> skobki = new ArrayList<>();

        for (int i = numberOfSkobka; i > 0; i--) {
            String s = "";
            List<Integer> lol = new ArrayList<>();
            for (int j = lenthOfOsob; j > 0; j--) {
                lol.add(in.nextInt());
            }
            skobki.add(lol);
        }
        int iteration = params.nextInt();
        int numberOfOsob = params.nextInt();

        System.out.println("Количество особей = " + numberOfOsob);

        HashMap<String, Integer> osobi = new HashMap<String, Integer>();
        for (int i = 0; i >= 0; i++) {
            String s = "";
            for (int j = 0; j < lenthOfOsob; j++) {//генерация набора 0 и 1
                int one = (int) (Math.random() + 0.5);
                s += one;
            }
            int score = ocenka(numberOfSkobka, lenthOfOsob, s, skobki);//s это особь
            osobi.put(s, score);
            if (osobi.size() == numberOfOsob) {
                break;
            }
        }
        HashMap<String, Integer> sortMap = sortHashMapByValues(osobi, true);
        System.out.println("Начальная популяция:");
        for (Map.Entry entry : sortMap.entrySet()) {
            String key = (String) entry.getKey();
            int value = (int) entry.getValue();
            System.out.println(key + " " + value);
        }

        //в цикле порадить новых (мутирование скрещивание)
        for (int i = 0; i < iteration; i++) {
                int chec = 0;
                String takeOsob1="";
                String takeOsob2="";
                for (Iterator<Map.Entry<String, Integer>> it = sortMap.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, Integer> entry = it.next();
                    if (chec > 0) {
                        takeOsob2 = entry.getKey();
                       // System.out.println("out");
                        break;
                    }
                    takeOsob1=entry.getKey();
                    chec++;
                    it.remove();
                }
                
                String answSelection = selection(lenthOfOsob, takeOsob1, takeOsob2);
                       answSelection = mutant(answSelection);      
                       int score = ocenka(numberOfSkobka, lenthOfOsob, answSelection, skobki);
                sortMap.put(answSelection, score);
                sortMap = sortHashMapByValues(sortMap, true);
                while(sortMap.size()<numberOfOsob){
                    String mutOsob="";
                    for (Iterator<Map.Entry<String, Integer>> it = sortMap.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, Integer> entry = it.next();
                        mutOsob = entry.getKey();
                        break;
                    }
                    mutOsob=mutant(mutOsob);
                    int score1 = ocenka(numberOfSkobka, lenthOfOsob, mutOsob, skobki);
                    sortMap.put(mutOsob, score1);
                    sortMap = sortHashMapByValues(sortMap, true);
                }
            }
        System.out.println("Конечная популяция:");
        for (Map.Entry entry : sortMap.entrySet()) {
            String key = (String) entry.getKey();
            int value = (int) entry.getValue();
            System.out.println(key + " " + value);
        }
        //вывести лучшее решение
    }

    public static String selection(int lenthOfOsob, String osob1, String osob2) {
        int number = (lenthOfOsob / 2);
        String newString1 = osob1.substring(number);
        String newString2 = osob2.substring(0, number);
        String answer = newString1 + newString2;
        return answer;
    }

    public static String mutant(String osob1) {
        int bit = (int) (Math.random() * osob1.length() - 1) + 1;
        char c = osob1.charAt(bit);
        if (c == '0') {
            c = '1';
        } else {
            c = '0';
        }
        return osob1.substring(0, bit) + c + osob1.substring(bit + 1);
    }
}
