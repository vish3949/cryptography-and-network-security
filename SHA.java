import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;

public class SHA {
    public static String hmacSHA(String algorithm, String key, String message) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        mac.init(secretKey);
        byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter message: ");
        String message = scanner.nextLine();
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        System.out.print("Choose Algorithm (SHA-256/SHA-512): ");
        String algorithm = scanner.nextLine().equalsIgnoreCase("SHA-256") ? "HmacSHA256" : "HmacSHA512";

        long startTime = System.nanoTime();
        String hmac = hmacSHA(algorithm, key, message);
        long endTime = System.nanoTime();

        System.out.println("HMAC: " + hmac);
        System.out.println("Time taken (nanoseconds): " + (endTime - startTime));
    }
}
