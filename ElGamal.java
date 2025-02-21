import java.math.BigInteger;
import java.util.Scanner;

public class ElGamal {

    public static BigInteger generatePrivateKey(BigInteger p) {
        BigInteger privateKey = new BigInteger(p.bitLength(), new java.security.SecureRandom());
        return privateKey.mod(p.subtract(BigInteger.ONE));
    }

    public static BigInteger generatePublicKey(BigInteger g, BigInteger privateKey, BigInteger p) {
        return g.modPow(privateKey, p);
    }

    public static BigInteger[] encrypt(BigInteger m, BigInteger g, BigInteger y, BigInteger p, BigInteger privateKey) {
        BigInteger k = new BigInteger(p.bitLength(), new java.security.SecureRandom());
        while (k.compareTo(p.subtract(BigInteger.ONE)) >= 0 || k.equals(BigInteger.ZERO)) {
            k = new BigInteger(p.bitLength(), new java.security.SecureRandom());
        }

        BigInteger c1 = g.modPow(k, p);
        BigInteger c2 = m.multiply(y.modPow(k, p)).mod(p);

        return new BigInteger[] { c1, c2 };
    }

    public static BigInteger decrypt(BigInteger c1, BigInteger c2, BigInteger privateKey, BigInteger p) {
        BigInteger c1PrivateKey = c1.modPow(privateKey, p);
        BigInteger c1PrivateKeyInverse = c1PrivateKey.modInverse(p);
        return c2.multiply(c1PrivateKeyInverse).mod(p);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a large prime number p: ");
        BigInteger p = new BigInteger(scanner.nextLine());

        System.out.print("Enter a generator g (a primitive root modulo p): ");
        BigInteger g = new BigInteger(scanner.nextLine());

        BigInteger privateKey = generatePrivateKey(p);
        BigInteger publicKey = generatePublicKey(g, privateKey, p);

        System.out.println("\nPublic Key: (" + publicKey + ")");
        System.out.println("Private Key: (" + privateKey + ")");

        System.out.print("\nEnter the message (text): ");
        String message = scanner.nextLine();

        BigInteger[] encryptedText = new BigInteger[message.length() * 2];

        for (int i = 0; i < message.length(); i++) {
            BigInteger m = BigInteger.valueOf(message.charAt(i));
            BigInteger[] encryptedChar = encrypt(m, g, publicKey, p, privateKey);
            encryptedText[i * 2] = encryptedChar[0];
            encryptedText[i * 2 + 1] = encryptedChar[1];
            System.out.println("Encrypted Character " + message.charAt(i) + ": c1 = " + encryptedText[i * 2] + ", c2 = "
                    + encryptedText[i * 2 + 1]);
        }

        System.out.print("\nDecrypted Text: ");
        for (int i = 0; i < encryptedText.length; i += 2) {
            BigInteger c1 = encryptedText[i];
            BigInteger c2 = encryptedText[i + 1];
            BigInteger decryptedChar = decrypt(c1, c2, privateKey, p);
            System.out.print((char) decryptedChar.intValue());
        }

        scanner.close();
    }
}