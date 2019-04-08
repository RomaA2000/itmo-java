import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class FastScanner {
    private InputStreamReader in;
    private int MAX_BUFFER_SIZE = 32;
    char[] buffer = new char[MAX_BUFFER_SIZE];
    private Queue<Character> queue = new LinkedList<Character>();

    public FastScanner() {
        in = new InputStreamReader(System.in);
    }

    public String readString() {
        StringBuilder myString = new StringBuilder("");
        char symbol;
        if (queue.isEmpty()) {
            read();
        }
        try {
            while (queue.peek() != -1) {
                symbol = queue.poll();
                if (symbol == '\n') {
                    break;
                }
                myString.append((char) symbol);
                if (queue.isEmpty()) {
                    read();
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Error in input string");
        }
        return myString.toString();
    }

    public boolean hasNextByte() {
        if (queue.isEmpty()) {
            read();
        }
        return !(queue.isEmpty());
    }

    public ArrayList<Integer> nextInts() {
        String input = this.readString();
        input += " ";
        int last = 0;
        char[] charArrayInput = input.toCharArray();
        ArrayList<Integer> array = new ArrayList<Integer>();
        for (int k = 0; k < charArrayInput.length; k++) {
            if ((isNumberOrMinus(charArrayInput[k])) && !(isNumberOrMinus(charArrayInput[k + 1]))) {
                array.add(strToInt(input.substring(last, k + 1)));
                last = k + 1;
            }
        }
        return array;
    }

    private static int strToInt(String str) {
        char[] charArrayInput = str.toCharArray();
        String input = new String(charArrayInput);
        input = input.replaceAll("\\s+", "");
        int term = 0;
        if (input.length() != 0) {
            try {
                term = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error in input");
            }
        }
        return term;
    }

    public static boolean isNumberOrMinus(char symbol) {
        return !(((symbol < (int) '0') || (symbol > (int) '9')) && (symbol != '-'));
    }

    public void closeInput() {
        if (in == null) {
            return;
        }
        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Can't close input");
        }
    }

    private void read() {
        int counter = 0;
        try {
            while (counter == 0) {
                counter = in.read(buffer, 0, MAX_BUFFER_SIZE);
            }
        } catch (IOException e) {
            System.out.println("Can't read from Buffer");
        }
        for (int i = 0; i < counter; i++) {
            queue.add(buffer[i]);
        }
    }

}
