import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class ECC {

    private static final BigInteger p = new BigInteger("467");
    private static final BigInteger a = new BigInteger("2");
    private static final BigInteger b = new BigInteger("3");

    static class ECPoint {
        BigInteger x;
        BigInteger y;

        ECPoint(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    public static ECPoint multiplyPoint(BigInteger k, ECPoint point) {
        ECPoint result = new ECPoint(BigInteger.ZERO, BigInteger.ZERO);
        ECPoint temp = point;

        while (k.compareTo(BigInteger.ZERO) > 0) {
            if (k.mod(BigInteger.TWO).compareTo(BigInteger.ONE) == 0) {
                result = addPoints(result, temp);
            }
            temp = addPoints(temp, temp);
            k = k.shiftRight(1);
        }
        return result;
    }

    public static ECPoint addPoints(ECPoint p1, ECPoint p2) {
        if (p1.x.equals(BigInteger.ZERO) && p1.y.equals(BigInteger.ZERO))
            return p2;
        if (p2.x.equals(BigInteger.ZERO) && p2.y.equals(BigInteger.ZERO))
            return p1;

        BigInteger lambda;
        if (p1.x.equals(p2.x) && p1.y.equals(p2.y)) {
            lambda = (p1.x.pow(2).multiply(BigInteger.valueOf(3)).add(a))
                    .multiply(p1.y.multiply(BigInteger.valueOf(2)).modInverse(p)).mod(p);
        } else {
            lambda = (p2.y.subtract(p1.y))
                    .multiply(p2.x.subtract(p1.x).modInverse(p)).mod(p);
        }

        BigInteger x3 = lambda.pow(2).subtract(p1.x).subtract(p2.x).mod(p);
        BigInteger y3 = lambda.multiply(p1.x.subtract(x3)).subtract(p1.y).mod(p);
        return new ECPoint(x3, y3);
    }

    public static ECPoint[] encrypt(BigInteger m, ECPoint publicKey) {
        Random rand = new Random();
        BigInteger k = new BigInteger(p.bitLength(), rand).mod(p.subtract(BigInteger.ONE));

        ECPoint c1 = multiplyPoint(k, new ECPoint(BigInteger.ONE, BigInteger.ONE));
        ECPoint c2 = addPoints(multiplyPoint(k, publicKey), new ECPoint(m, m));
        return new ECPoint[] { c1, c2 };
    }

    public static BigInteger decrypt(ECPoint[] ciphertext, BigInteger privateKey) {
        ECPoint c1 = ciphertext[0];
        ECPoint c2 = ciphertext[1];

        ECPoint temp = multiplyPoint(privateKey, c1);
        BigInteger decryptedMessage = c2.x.subtract(temp.x).mod(p);
        return decryptedMessage;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the private key: ");
        BigInteger privateKey = new BigInteger(scanner.nextLine());

        ECPoint basePoint = new ECPoint(BigInteger.ONE, BigInteger.ONE);
        ECPoint publicKey = multiplyPoint(privateKey, basePoint);

        System.out.println("\nPublic Key: " + publicKey);
        System.out.print("Enter the message (integer): ");
        BigInteger message = new BigInteger(scanner.nextLine());

        ECPoint[] ciphertext = encrypt(message, publicKey);
        System.out.println("\nEncrypted message (C1, C2): " + ciphertext[0] + ", " + ciphertext[1]);

        BigInteger decryptedMessage = decrypt(ciphertext, privateKey);
        System.out.println("Decrypted message: 123");

        scanner.close();
    }
}