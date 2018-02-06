package HW2;

public class Multi extends Thread{
    private static final double V1 = 1.0;
    private static final double V2 = 2.0;
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
                for (int k = 0; k < N; k++) {
                    M3[i][j] += M1[i][k] * M2[k][j];
                }
            }
        }
    }
    public static void main(String args[]){
        
        int N = Integer.parseInt(args[0]);
        int N_thread = Integer.parseInt(args[1]);
        
        double[][] _M1 = new double[N][N];
        double[][] _M2 = new double[N][N];
        double[][] _M3 = new double[N][N];
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                _M1[i][j] = V1;
                _M2[i][j] = V2;
                _M3[i][j] = 0.0;
            }
        }

        Multi[] threads = new Multi[N_thread];
        for( int i = 0; i<N_thread; i++){
            threads[i] = new Multi(_M1, _M2, _M3, N, N_thread, i);
            threads[i].start();
        }

        for( int i = 0; i<N_thread; i++){
            try{
                threads[i].join();
            }
            catch(InterruptedException e){}
        }

        int n_incorrect = 0;
        for(int i = 0; i<N; i++){
            for(int j = 0; j<N; j++){
                if(_M3[i][j]!= N * V1 * V2){
                    n_incorrect++;
                }
            }
        }
        System.out.println(String.format("Elements incorrect: %d", n_incorrect));
        
        

    }
}
