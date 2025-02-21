import java.util.Scanner;

public class playfair {
    private char[][] matrix = new char[5][5];
    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

    public playfair(String key) {
        generateMatrix(key.toUpperCase());
    }

    private void generateMatrix(String key) {
        String cleanKey = key.replaceAll("[^A-Z]", "").replace("J", "I");
        String matrixInput = cleanKey + ALPHABET;
        boolean[] used = new boolean[26];
        int row = 0, col = 0;

        for (char c : matrixInput.toCharArray()) {
            if (!used[c - 'A'] && c != 'J') {
                matrix[row][col] = c;
                used[c - 'A'] = true;
                col++;
                if (col == 5) {
                    col = 0;
                    row++;
                }
                if (row == 5)
                    break;
            }
        }
    }

    public String encrypt(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        if (text.length() % 2 != 0)
            text += "X";

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);

            if (posA[0] == posB[0]) {
                result.append(matrix[posA[0]][(posA[1] + 1) % 5]);
                result.append(matrix[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) {
                result.append(matrix[(posA[0] + 1) % 5][posA[1]]);
                result.append(matrix[(posB[0] + 1) % 5][posB[1]]);
            } else {
                result.append(matrix[posA[0]][posB[1]]);
                result.append(matrix[posB[0]][posA[1]]);
            }
        }
        return result.toString();
    }

    private int[] findPosition(char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == c) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the key: ");
        String key = scanner.nextLine();

        playfair cipher = new playfair(key);

        System.out.print("Enter the text to encrypt: ");
        String plaintext = scanner.nextLine();

        String encrypted = cipher.encrypt(plaintext);
        System.out.println("Encrypted text: " + encrypted);

        scanner.close();
    }
}