import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class ManInMiddle {
    private static final SecureRandom random = new SecureRandom();

    public static BigInteger generatePublicKey(BigInteger alpha, BigInteger q, BigInteger privateKey) {
        return alpha.modPow(privateKey, q);
    }

    public static BigInteger computeSharedKey(BigInteger publicKey, BigInteger q, BigInteger privateKey) {
        return publicKey.modPow(privateKey, q);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter large prime number (q): ");
        BigInteger q = scanner.nextBigInteger();
        System.out.print("Enter primitive root (alpha): ");
        BigInteger alpha = scanner.nextBigInteger().mod(q);

        System.out.print("Enter private key of A: ");
        BigInteger Xa = scanner.nextBigInteger();
        System.out.print("Enter private key of B: ");
        BigInteger Xb = scanner.nextBigInteger();

        BigInteger Ya = generatePublicKey(alpha, q, Xa);
        BigInteger Yb = generatePublicKey(alpha, q, Xb);

        System.out.println("\nPublic key of A: " + Ya);
        System.out.println("Public key of B: " + Yb);

        System.out.print("Enter 1 to perform MITM attack, else 0: ");
        int mitm = scanner.nextInt();

        if (mitm == 1) {
            System.out.print("\nEnter attacker's private keys with A and B: ");
            BigInteger Xd1 = scanner.nextBigInteger();
            BigInteger Xd2 = scanner.nextBigInteger();

            BigInteger Yd1 = generatePublicKey(alpha, q, Xd1);
            BigInteger Yd2 = generatePublicKey(alpha, q, Xd2);

            System.out.println("\nAttacker's public key with A: " + Yd1);
            System.out.println("Attacker's public key with B: " + Yd2);

            BigInteger K1 = computeSharedKey(Yd1, q, Xa);
            BigInteger K2 = computeSharedKey(Yd2, q, Xb);

            System.out.println("\nAttacker's shared secret key with A: " + K1);
            System.out.println("Attacker's shared secret key with B: " + K2);

            BigInteger Kd1 = computeSharedKey(Ya, q, Xd1);
            BigInteger Kd2 = computeSharedKey(Yb, q, Xd2);

            System.out.println("Secret session key of A: " + Kd1);
            System.out.println("Secret session key of B: " + Kd2);
            System.out.println("Diffie-Hellman key exchange is compromised by Man-in-the-Middle Attack!");
        } else {
            BigInteger Ka = computeSharedKey(Yb, q, Xa);
            BigInteger Kb = computeSharedKey(Ya, q, Xb);

            System.out.println("\nShared session key of A: " + Ka);
            System.out.println("Shared session key of B: " + Kb);
            System.out.println("Diffie-Hellman key exchange is secure.");
        }
    }
}
