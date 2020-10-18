public class Hello {

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        int sum = a + b;
        int sub = 0;
        if (a > b) {
            sub = a - b;
        } else {
            sub = b - a;
        }
        int mul = a * b;
        int div = b / a;
        int allSum = 0;
        for (int i = 0;i < 5; i++) {
            allSum = allSum + i;
        }
    }
}
