import java.util.Arrays;

public class AESround {
    private static final int[][] sBox = {
            { 0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76 },
            { 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0 },
            { 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15 },
            { 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75 },
            { 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84 },
            { 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf },
            { 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8 },
            { 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2 }
    };

    private static int[][] shiftRows(int[][] state) {
        state[1] = new int[] { state[1][1], state[1][2], state[1][3], state[1][0] };
        state[2] = new int[] { state[2][2], state[2][3], state[2][0], state[2][1] };
        state[3] = new int[] { state[3][3], state[3][0], state[3][1], state[3][2] };
        return state;
    }

    private static int galoisMultiply(int a, int b) {
        int p = 0;
        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                p ^= a;
            }
            boolean hiBitSet = (a & 0x80) != 0;
            a <<= 1;
            if (hiBitSet) {
                a ^= 0x1B;
            }
            b >>= 1;
        }
        return p & 0xFF;
    }

    private static int[][] mixColumns(int[][] state) {
        int[][] fixedMatrix = {
                { 0x02, 0x03, 0x01, 0x01 },
                { 0x01, 0x02, 0x03, 0x01 },
                { 0x01, 0x01, 0x02, 0x03 },
                { 0x03, 0x01, 0x01, 0x02 }
        };
        int[][] newState = new int[4][4];

        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                newState[row][col] = galoisMultiply(fixedMatrix[row][0], state[0][col]) ^
                        galoisMultiply(fixedMatrix[row][1], state[1][col]) ^
                        galoisMultiply(fixedMatrix[row][2], state[2][col]) ^
                        galoisMultiply(fixedMatrix[row][3], state[3][col]);
            }
        }
        return newState;
    }

    public static void main(String[] args) {
        int[][] state = {
                { 0xDB, 0xF2, 0x9F, 0x47 },
                { 0x13, 0x0A, 0x88, 0x5B },
                { 0x53, 0x22, 0xA1, 0xC3 },
                { 0x45, 0x7C, 0x6E, 0x01 }
        };

        System.out.println("Initial State:");
        for (int[] row : state) {
            System.out.println(Arrays.toString(row));
        }

        state = shiftRows(state);
        System.out.println("\nAfter ShiftRows:");
        for (int[] row : state) {
            System.out.println(Arrays.toString(row));
        }

        state = mixColumns(state);
        System.out.println("\nAfter MixColumns:");
        for (int[] row : state) {
            System.out.println(Arrays.toString(row));
        }
    }
}