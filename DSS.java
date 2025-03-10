import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class DSS {
    private BigInteger p, g, x, y;
    private SecureRandom random = new SecureRandom();

    public DSS(int bitLength) {
        p = BigInteger.probablePrime(bitLength, random);
        g = new BigInteger(bitLength - 1, random).mod(p);
        x = new BigInteger(bitLength - 2, random);
        y = g.modPow(x, p);
    }

    public BigInteger[] sign(BigInteger message) {
        BigInteger k;
        do {
            k = new BigInteger(p.bitLength() - 1, random);
        } while (!k.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE));

        BigInteger r = g.modPow(k, p);
        BigInteger kInverse = k.modInverse(p.subtract(BigInteger.ONE));
        BigInteger s = kInverse.multiply(message.subtract(x.multiply(r))).mod(p.subtract(BigInteger.ONE));
        return new BigInteger[] { r, s };
    }

    public boolean verify(BigInteger message, BigInteger r, BigInteger s) {
        if (r.compareTo(BigInteger.ONE) < 0 || r.compareTo(p) >= 0)
            return false;
        if (s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(p.subtract(BigInteger.ONE)) >= 0)
            return false;

        BigInteger v1 = p.modPow(message, p);
        BigInteger v2 = (y.modPow(r, p).multiply(r.modPow(s, p))).mod(p);
        return v1.equals(v2);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter message (as a number): ");
        BigInteger message = new BigInteger(scanner.nextLine());

        DSS elGamal = new DSS(512);
        BigInteger[] signature = elGamal.sign(message);

        System.out.println("Signature: ");
        System.out.println("r: " + signature[0]);
        System.out.println("s: " + signature[1]);

        boolean isValid = elGamal.verify(message, signature[0], signature[1]);
        System.out.println("Signature valid: " + isValid);
    }
}
