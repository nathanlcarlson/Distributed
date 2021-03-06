package HW2;

public class Single{

    public static double[][] multiply(double[][] M1, double[][] M2){
        int N = M1.length;
        double[][] M3 = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                     M3[i][j] = M3[i][j] + M1[i][k] * M2[k][j];
                }
            }
        }
        return M3;

    }
    public static void PrintMatrix(double[][] M){
        int N = M.length;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                System.out.println(M[i][j]);
            }
        }
    }
    public static void FillRandom(double[][] M){
        int N = M.length;;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                M[i][j] = Math.random();
            }
        }
    }
    public static void main(String args[]){


        if(args.length != 1){
          System.out.println("Usage: java Single [Square Matrix Width]");
          return;
        }
        int N;
        try{
          N = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e){
          System.out.println("Usage: java Single [Square Matrix Width]");
          System.out.println("[Square Matrix Width] must be an integer");
          return;
        }
        double[][] M1 = new double[N][N];
        double[][] M2 = new double[N][N];
        FillRandom(M1);
        FillRandom(M2);
        final long startTime = System.nanoTime();
        multiply(M1, M2);
        final long endTime = System.nanoTime();
        System.out.println("Total time to multiply: " + (endTime - startTime) + " ns" );
    }
}
