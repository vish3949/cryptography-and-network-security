import java.util.Scanner;

public class vigenerecipher {
    private String key;

    public vigenerecipher(String key) {
        this.key = key.toUpperCase().replaceAll("[^A-Z]", "");
    }

    public String encrypt(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char c : text.toCharArray()) {
            int shift = key.charAt(keyIndex) - 'A';
            char encrypted = (char) ((c - 'A' + shift) % 26 + 'A');
            result.append(encrypted);
            keyIndex = (keyIndex + 1) % key.length();
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the key: ");
        String key = scanner.nextLine();

        vigenerecipher cipher = new vigenerecipher(key);

        System.out.print("Enter the text: ");
        String text = scanner.nextLine();

        String result;
        result = cipher.encrypt(text);
        System.out.println("Encrypted text: " + result);

        scanner.close();
    }
}