cd Passes/build

clang-18 -fPIC -Xclang -disable-O0-optnone -S -emit-llvm  ../../hello.c  -o hello.ll

clang-18 -flto -static  -S -emit-llvm -fpass-plugin=lib/libLibcPolicy.so hello.ll -o hello1.ll         

clang-18  -flto -static  -S -fpass-plugin=lib/libInst.so hello1.ll -o hello2.ll 

clang-18 hello2.ll 

cd ../..

python3 callgraph.py

dot -Tpng -o output1.png ans.dot
dot -Tpng -o output.png ans_2.dot
dot -Tpng -o output3.png final.dot
