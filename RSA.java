import java.util.Scanner;

public class RSA {
    public static int[] extendedEuclid(int a, int b) {
        if (b == 0) {
            return new int[] { a, 1, 0 };
        }
        int[] result = extendedEuclid(b, a % b);
        int gcd = result[0];
        int x1 = result[1];
        int y1 = result[2];
        int x = y1;
        int y = x1 - (a / b) * y1;
        return new int[] { gcd, x, y };
    }

    public static int mulInverse(int e, int phi) {
        int[] result = extendedEuclid(e, phi);
        int gcd = result[0];
        if (gcd != 1) {
            return -1;
        }
        return (result[1] + phi) % phi;
    }

    public static int eulerTotient(int p, int q) {
        return (p - 1) * (q - 1);
    }

    public static int[] generateKeys(int p, int q, int e) {
        int n = p * q;
        int phi = eulerTotient(p, q);
        int d = mulInverse(e, phi);
        if (d == -1) {
            System.out.println("Invalid e value - Multiplicative inverse of e does not exist.");
            System.exit(0);
        }
        int[] keys = new int[4];
        keys[0] = e;
        keys[1] = n;
        keys[2] = d;
        keys[3] = n;
        return keys;
    }

    public static int encrypt(int e, int n, int plainText) {
        return modExp(plainText, e, n);
    }

    public static int decrypt(int d, int n, int cipherText) {
        return modExp(cipherText, d, n);
    }

    public static int modExp(int base, int exp, int mod) {
        int result = 1;
        base = base % mod;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            exp = exp >> 1;
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter two prime numbers (p and q): ");
        int p = scanner.nextInt();
        int q = scanner.nextInt();
        System.out.print("Enter public key (e should be valid): ");
        int e = scanner.nextInt();
        int[] keys = generateKeys(p, q, e);
        int publicKeyE = keys[0];
        int publicKeyN = keys[1];
        int privateKeyD = keys[2];
        int privateKeyN = keys[3];
        System.out.println("\nPublic Key: (" + publicKeyE + ", " + publicKeyN + ")");
        System.out.println("Private Key: (" + privateKeyD + ", " + privateKeyN + ")");
        System.out.print("\nEnter plain text (integer): ");
        int plainText = scanner.nextInt();
        int cipherText = encrypt(publicKeyE, publicKeyN, plainText);
        System.out.println("Encrypted Text (Cipher Text): " + cipherText);
        int decryptedText = decrypt(privateKeyD, privateKeyN, cipherText);
        System.out.println("Decrypted Text (Plain Text): " + decryptedText);
        scanner.close();
    }
}