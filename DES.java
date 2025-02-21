import java.util.Scanner;

public class DES {

    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final int[] SHIFT_SCHEDULE = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    public static String hexToBin(String hexKey) {
        StringBuilder binary = new StringBuilder();
        for (char c : hexKey.toCharArray()) {
            binary.append(String.format("%4s", Integer.toBinaryString(Integer.parseInt(String.valueOf(c), 16)))
                    .replace(' ', '0'));
        }
        return binary.toString();
    }

    public static String permute(String key, int[] table) {
        StringBuilder permutedKey = new StringBuilder();
        for (int pos : table) {
            permutedKey.append(key.charAt(pos - 1));
        }
        return permutedKey.toString();
    }

    public static String leftShift(String keyHalf, int shifts) {
        return keyHalf.substring(shifts) + keyHalf.substring(0, shifts);
    }

    public static String generateRoundKey(String hexKey, int roundNum) {
        if (roundNum < 1 || roundNum > 16) {
            throw new IllegalArgumentException("Round number must be between 1 and 16.");
        }

        String binaryKey = hexToBin(hexKey);
        String key56Bit = permute(binaryKey, PC1);

        String C = key56Bit.substring(0, 28);
        String D = key56Bit.substring(28);

        for (int i = 0; i < roundNum; i++) {
            int shiftAmount = SHIFT_SCHEDULE[i];
            C = leftShift(C, shiftAmount);
            D = leftShift(D, shiftAmount);
        }

        String combinedKey = C + D;

        return permute(combinedKey, PC2);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 64-bit key in HEX (16 characters): ");
        String hexKey = scanner.nextLine().trim();

        System.out.print("Enter the round number (1-16): ");
        int roundNum = scanner.nextInt();

        String roundKey = generateRoundKey(hexKey, roundNum);

        System.out.println("Round " + roundNum + " Key (48-bit Binary): " + roundKey);

        try {
            long decimalValue = Long.parseLong(roundKey, 2);
            System.out.println("Round " + roundNum + " Key (Hex): " + Long.toHexString(decimalValue).toUpperCase());
        } catch (NumberFormatException e) {
            System.out.println("Error: Generated round key is not a valid binary string.");
        }

        scanner.close();
    }
}