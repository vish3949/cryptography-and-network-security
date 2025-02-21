import java.util.Scanner;

public class hillcipher {
    private int[][] keyMatrix;
    private int dimension;

    public hillcipher(int[][] key) {
        this.keyMatrix = key;
        this.dimension = key.length;
    }

    public String encrypt(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        while (text.length() % dimension != 0) {
            text += "X";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += dimension) {
            int[] vector = new int[dimension];
            for (int j = 0; j < dimension; j++) {
                vector[j] = text.charAt(i + j) - 'A';
            }

            int[] encrypted = multiplyMatrix(vector);
            for (int value : encrypted) {
                result.append((char) ((value % 26 + 26) % 26 + 'A'));
            }
        }
        return result.toString();
    }

    private int[] multiplyMatrix(int[] vector) {
        int[] result = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                result[i] += keyMatrix[i][j] * vector[j];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the dimension of the key matrix : ");
        int dimension = scanner.nextInt();

        int[][] keyMatrix = new int[dimension][dimension];
        System.out.println("Enter the key matrix elements row by row:");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.printf("Enter element [%d][%d]: ", i, j);
                keyMatrix[i][j] = scanner.nextInt();
            }
        }

        scanner.nextLine();
        hillcipher cipher = new hillcipher(keyMatrix);

        System.out.print("Enter the text to encrypt: ");
        String plaintext = scanner.nextLine();

        String encrypted = cipher.encrypt(plaintext);
        System.out.println("Encrypted text: " + encrypted);

        scanner.close();
    }
}