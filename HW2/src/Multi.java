package HW2;

public class Multi extends Thread{

    double[][] M1;
    double[][] M2;
    double[][] M3;
    int N;
    int N_thread;
    int id;
    public Multi(double[][] _M1, double[][] _M2, double[][] _M3, int _N, int _N_thread, int _id){
        N_thread = _N_thread;
        id = _id; // [0, N_thread)
        N = _N;
        M1 = _M1;
        M2 = _M2;
        M3 = _M3;
        FillN(M1, 1.0);
        FillN(M2, 2.0);
        FillZero(M3);
        
    }
   
    // Multiply 
    public void run(){
        //System.out.println(M3.length);
        
        int n_min = N / N_thread;
        
        int n_diff = N % N_thread;

        int start = 0;
        for(int i = 0; i < id; i++){
            start += n_min;
            if(i < n_diff) start++;
        }

        int stop = start + n_min;
        if(id < n_diff) stop++;
        System.out.println(String.format("Id: %2d [%2d, %2d)", id, start, stop));
        for (int i = start; i < stop; i++) {
            for (int j = 0; j < N; j++) {
                double sum = 0;
                for (int k = 0; k < N; k++) {
                    sum += M1[i][k] * M2[k][j];
                }
                M3[i][j] = sum;
            }
        }
    }
    
    private void FillRandom(double[][] M){
        int N = M.length;;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                M[i][j] = Math.random();
            }
        }
    }
    private void FillN(double[][] M, double _n){
        int N = M.length;;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                M[i][j] = _n;
            }
        }
    }
    private void FillZero(double[][] M){
        int N = M.length;;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                M[i][j] = 0.0;
            }
        }
    }
    public static void main(String args[]){
        
        int N = Integer.parseInt(args[0]);
        int N_thread = Integer.parseInt(args[1]);
        
        double[][] M1 = new double[N][N];
        double[][] M2 = new double[N][N];
        double[][] M3 = new double[N][N];
        Multi[] threads = new Multi[N_thread];
        for( int i = 0; i<N_thread; i++){
            threads[i] = new Multi(M1, M2, M3, N, N_thread, i);
            threads[i].start();
        }
        
        for( int i = 0; i<N_thread; i++){
            try{
                threads[i].join();
            }            
                
            catch(InterruptedException e){              
            }
        }
        //System.out.println(M3[0][0]);
        int n_incorrect = 0;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                if(M3[i][j]!=60.0){
                    n_incorrect++;
                }
            }
        }
        System.out.println(String.format("Elements incorrect: %d", n_incorrect));
        
        

    }
}
