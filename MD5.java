import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class MD5 {
    private static final int[] S = {
            7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
            5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
            4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
            6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
    };
    private static final int[] K = new int[64];
    static {
        for (int i = 0; i < 64; i++) {
            K[i] = (int) (Math.abs(Math.sin(i + 1)) * (1L << 32));
        }
    }

    public static byte[] md5(byte[] message) {
        int originalLength = message.length * 8;
        int newLength = ((originalLength + 64) / 512 + 1) * 512;
        byte[] padded = new byte[newLength / 8];
        System.arraycopy(message, 0, padded, 0, message.length);
        padded[message.length] = (byte) 0x80;
        for (int i = 0; i < 8; i++) {
            padded[padded.length - 8 + i] = (byte) (originalLength >>> (i * 8));
        }
        int[] buffer = { 0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476 };
        int[] X = new int[16];
        for (int i = 0; i < padded.length / 64; i++) {
            for (int j = 0; j < 16; j++) {
                X[j] = ((padded[i * 64 + j * 4] & 0xFF)) |
                        ((padded[i * 64 + j * 4 + 1] & 0xFF) << 8) |
                        ((padded[i * 64 + j * 4 + 2] & 0xFF) << 16) |
                        ((padded[i * 64 + j * 4 + 3] & 0xFF) << 24);
            }
            int A = buffer[0], B = buffer[1], C = buffer[2], D = buffer[3];
            for (int j = 0; j < 64; j++) {
                int F, g;
                if (j < 16) {
                    F = (B & C) | (~B & D);
                    g = j;
                } else if (j < 32) {
                    F = (D & B) | (~D & C);
                    g = (5 * j + 1) % 16;
                } else if (j < 48) {
                    F = B ^ C ^ D;
                    g = (3 * j + 5) % 16;
                } else {
                    F = C ^ (B | ~D);
                    g = (7 * j) % 16;
                }
                int temp = D;
                D = C;
                C = B;
                B += Integer.rotateLeft(A + F + K[j] + X[g], S[j]);
                A = temp;
            }
            buffer[0] += A;
            buffer[1] += B;
            buffer[2] += C;
            buffer[3] += D;
        }
        byte[] result = new byte[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i * 4 + j] = (byte) (buffer[i] >>> (j * 8));
            }
        }
        return result;
    }

    public static byte[] hmacMD5(byte[] key, byte[] message) {
        if (key.length > 64) {
            key = md5(key);
        }
        byte[] ipad = new byte[64], opad = new byte[64];
        for (int i = 0; i < key.length; i++) {
            ipad[i] = (byte) (key[i] ^ 0x36);
            opad[i] = (byte) (key[i] ^ 0x5C);
        }
        return md5(concat(opad, md5(concat(ipad, message))));
    }

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter message: ");
        String message = scanner.nextLine();
        System.out.print("Enter key: ");
        String key = scanner.nextLine();
        scanner.close();

        byte[] mac = hmacMD5(key.getBytes(StandardCharsets.UTF_8), message.getBytes(StandardCharsets.UTF_8));
        System.out.print("HMAC-MD5: ");
        for (byte b : mac) {
            System.out.printf("%02x", b);
        }
    }
}
