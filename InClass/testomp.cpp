#include <omp.h>
#include <iostream>

int main(int argc, char* argv[]){

    #ifdef _OPENMP
    int threadId;
    #pragma omp parallel private(threadId)
    {
        threadId = omp_get_thread_num();
        if(threadId==0){
            std::cout << "Thread test\n";
            int max = omp_get_max_threads();
            int actual = omp_get_num_threads();
            std::cout << "Max threads " << max << "\n";
            std::cout << "Actual threads " << actual << "\n";
        }  
    }//pragma
    #endif
}//main
