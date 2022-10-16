import java.util.*;

class Hamming {
    public static void main(String[] args) {

        System.out.print("Введите количество бит в слове: ");
        var scanner = new Scanner(System.in);
        var numOfBits = scanner.nextInt();
        var dataWord = new int[numOfBits];

        var numOfControlBits = calcNumOfControlBits(numOfBits);
        System.out.println("Количество контрольных бит: " + numOfControlBits);

        System.out.println("Введите слово:");
        for (var i = 0; i < numOfBits; i++)
            dataWord[i] = scanner.nextInt();

        var dataWordWithControlBits = new int[numOfBits + numOfControlBits];
        calcSenderCodeWord(dataWordWithControlBits, dataWord, numOfControlBits, numOfBits);

        // вывод контрольных битов
        codeWord(dataWordWithControlBits, numOfBits, numOfControlBits);

        System.out.println("Слово с контрольными битами:");
        for (var i = 0; i < (numOfBits + numOfControlBits); i++)
            System.out.print(dataWordWithControlBits[i] + " ");

        var recCodeWord = new int[numOfBits + numOfControlBits];

        System.out.println("\n\nВведите слово с ошибкой: ");
        for (var i = 0; i < (numOfBits + numOfControlBits); i++)
            recCodeWord[i] = scanner.nextInt();

        var controlBitsArray = new int[numOfControlBits];

        // ищем ошибку
        var err = detectError(recCodeWord, controlBitsArray, numOfBits, numOfControlBits);

        if (err) {
            System.out.println("\nОшибки в слове не найдены.");

            System.out.println("\nИсходное слово: ");
            for (var i = 0; i < numOfBits; i++)
                System.out.print(dataWord[i] + " ");

        } else {
            System.out.println("Обнаружены ошибки в слове!.");

            correctError(recCodeWord, controlBitsArray, dataWord, numOfBits, numOfControlBits);

            System.out.println("Исходное слово с контрольными битами, после исправления:");
            for (var i = 0; i < (numOfBits + numOfControlBits); i++)
                System.out.print(recCodeWord[i] + " ");

            System.out.println("\nИсходное слово:");
            for (var i = 0; i < numOfBits; i++)
                System.out.print(dataWord[i] + " ");
        }
    }

    public static int calcNumOfControlBits(int d) {
        int i, r = 1;
        for (i = 1; i <= d; i++) {
            if (Math.pow(2, i) >= (d + i + 1)) {
                r = i;
                break;
            }
        }
        return r;
    }

    public static void calcSenderCodeWord(int[] senderCodeWord, int[] dataWord, int r, int d) {
        int i, j, k = d - 1, ind = 1;
        boolean flag;
        for (i = (d + r - 1); i >= 0; i--) {
            flag = false;
            for (j = 0; j < ind; j++) {
                if ((int) Math.pow(2, j) == ind) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                senderCodeWord[i] = 0;
            } else {
                senderCodeWord[i] = dataWord[k];
                k--;
            }
            ind++;
        }
    }

    public static void codeWord(int[] senderCodeWord, int d, int r) {
        System.out.println("\nЗначение контрольных бит:");
        int i, ind = 1, j, k, xor, count;
        boolean flag;
        for (i = (d + r - 1); i >= 0; i--) {
            flag = false;
            xor = 0;
            for (j = 0; j < ind; j++) {
                if ((int) Math.pow(2, j) == ind) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                k = d + r - ind;
                count = ind;
                while (k >= 0) {
                    xor = xor ^ (senderCodeWord[k]);
                    k--;
                    count--;
                    if (count == 0) {
                        k = k - ind;
                        count = ind;
                    }
                }
                System.out.println("R" + (i - (d + r)) + " = " + xor);
                senderCodeWord[i] = xor;
            }
            ind++;

        }
        System.out.println();
    }

    public static boolean detectError(int[] recCodeWord, int[] redBits, int d, int r) {

        // вывод контрольных битов
        codeWord(recCodeWord, d, r);

        int i, j, ind = 1, rind = r - 1;
        boolean flag;
        for (i = (d + r - 1); i >= 0; i--) {
            flag = false;
            for (j = 0; j < ind; j++) {
                if ((int) Math.pow(2, j) == ind) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                redBits[rind] = recCodeWord[i];
                rind--;
            }
            ind++;
        }
        for (i = 0; i < r; i++) {
            if (redBits[i] == 1) {
                return false;
            }
        }
        return true;
    }

    public static void correctError(int[] recCodeWord, int[] redBits, int[] dataWord, int d, int r) {
        var binary = new StringBuilder();
        for (int i = 0; i < r; i++) {
            binary.append(redBits[i]);
        }
        int decimal = Integer.parseInt(binary.toString(), 2);
        System.out.println("Ошибка обнаружена в бите " + decimal);
        int index = d + r - decimal;
        if (recCodeWord[index] == 0) {
            recCodeWord[index] = 1;
        } else {
            recCodeWord[index] = 0;
        }
        int i, j, ind = 1, dind = d - 1;
        boolean flag;
        for (i = (d + r - 1); i >= 0; i--) {
            flag = false;
            for (j = 0; j < ind; j++) {
                if ((int) Math.pow(2, j) == ind) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            } else {
                dataWord[dind] = recCodeWord[i];
                dind--;
            }
            ind++;
        }
    }

}

