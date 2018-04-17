#include <random>
#include <string>
#include <cstdlib>
#include <iostream>
#include <omp.h>
int main(int argc, char** argv){

    // initialize engine and distribution
    const int seed = 1;
    std::mt19937 engine(seed);
    std::uniform_int_distribution<int> dist(-5, 5);


    int row, col;
    for(int i=0; i<argc; ++i){
        std::string arg = argv[i];
        if(arg == "-row"){
            row = atoi(argv[i+1]);
        }
        if(arg == "-col"){
            col = atoi(argv[i+1]);
        }
    }
    std::cout << "Rows: " << row << "\n";
    std::cout << "Cols: " << col << "\n";
    
    int** mat = new int*[row];
    for(int i=0; i<row; ++i){
        mat[i] = new int[col];
        for(int j=0; j<col; ++j){
            #ifdef TEST
                mat[i][j] = 1;
            #else
                mat[i][j] = dist(engine);
            #endif
        }
    }
    int* vec = new int[col];
    for(int i=0; i<col; ++i){
        #ifdef TEST
            vec[i] = 1;
        #else
            vec[i] = dist(engine);
        #endif
    }
      
    int* prod = new int[row];
    #pragma omp parallel for
    for(int i=0; i<row; ++i){
        for(int j=0; j<col; ++j){
            prod[i] += vec[j]*mat[i][j];
        }
    }
    for(int i=0; i<row; ++i){
        std::cout << prod[i] << "\n";
    }
    
    for(int i=0; i<row; ++i){
        delete[] mat[i];
    }
    delete[] mat;
    delete[] vec;
    delete[] prod;
    return 0;

}
