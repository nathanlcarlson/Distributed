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
        
        int n_args = args.length;
        
        for(int i=0; i<n_args; ++i){
            int N = Integer.parseInt(args[i]);
            double[][] M1 = new double[N][N];
            double[][] M2 = new double[N][N];
            FillRandom(M1);
            FillRandom(M2);
            multiply(M1, M2);
        }

    }
}
