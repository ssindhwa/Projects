#include "LibcCallGraph.h"
#include "llvm/Transforms/Utils/HelloWorld.h"
#include "llvm/IR/Function.h"
#include <iostream>
#include "llvm/IR/Instruction.h"
#include "llvm/IR/Instructions.h"
#include "llvm/IR/BasicBlock.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/Module.h"
#include "llvm/IR/Instructions.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/IR/InlineAsm.h"
#include "llvm/Pass.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/CFG.h"
#include "llvm/IR/Instructions.h"
#include "llvm/Support/raw_ostream.h"
#include "llvm/IR/CFG.h" 
#include "llvm/IR/IRBuilder.h"
#include "llvm/Passes/PassBuilder.h"
#include "llvm/Passes/PassPlugin.h"
#include <set>
#include <map>
#include <fstream>
#include <vector>
#include <regex>
using namespace llvm;

int hello;
 int global = -1;

  bool isUserDefined(Function *F) {
    errs()<<F->getName().str()<<"wakka\n";
    return !(F->isDeclaration());
  }


  void Merge(BasicBlock * BB,std::map< BasicBlock*, std :: vector<int>> &blockMap,std::map<BasicBlock*, int> &visited,int global,std::ofstream & dotFile)
{
  if(visited[BB] == 1) return;
  visited[BB] = 1;
 std :: vector <int> end;
    for (auto *Pred : predecessors(BB)) {
        if(blockMap[Pred][0]!=-1) end.insert(end.end(),blockMap[Pred].begin(),blockMap[Pred].end());
        else {
          errs()<<BB->getName()<<" merge se 1  \n";
          Merge(Pred,blockMap,visited,global+2,dotFile);
          
          end.insert(end.end(),blockMap[Pred].begin(),blockMap[Pred].end());
   bool flag = 0;
   for (auto i : end) {
    if ( i != -1) {
      flag = 1;
      break;
    }
    if (flag) { end.clear(); end.push_back(-1);}
   }

        }
    }
    errs()<<BB->getName()<<" : ";
    for (auto i : end) errs()<<i<<" ";
    errs()<<"\n";
    if(end.size())
    blockMap[BB] = end;
}


void processFunction(Function  * F,int & global,std::ofstream & dotFile,std::map<BasicBlock*, std :: vector<int>> &  blockMap,std::map<BasicBlock*, int> &  StartMap,
                                        
                      std::map<Function*,int> & Start,std::map<Function*,int> & End ) {

    global++;
  int zero = global;
  int funprev = global;
   int prev = global;
    
    
    
    
        std::set<std::string> libcFunctions;
 //std::regex funcRegex(R"(\b(\w+)\s*\()"); 
     std::regex funcRegex(R"(\b(\w+)\s*\()");  
    // Read strings from a text file
    std::ifstream infile("/home/sakshi/Project/libc.txt");
    std::string line;
    // Check if the file is opened successfully
    if (!infile.is_open()) {
      errs() << "Error opening file\n";
    }

    // Read each line from the file and insert it into the set
    while (std::getline(infile, line)) {
     // errs()<<line<<"libc functiopn\n";
      
        // Store the function name (match[1] is the first captured group, the function name)
          libcFunctions.insert(line);
        
     
    }

    // Close the file
    infile.close();
    
    // Traverse all instructions in the function
    for (BasicBlock &BB : *F) {
     
      prev = -1;
      StartMap[&BB] = -1;
      for (Instruction &I : BB) {
        if (auto *callInst = dyn_cast<CallInst>(&I)) {
          if (Function *calledFunc = callInst->getCalledFunction()) {
            std::string funcName = calledFunc->getName().str();
            
            if(funcName!="syscall")
            //errs()<<funcName<<"\n"<<global;
            // Check if the function is a libc function or user-defined
            if (libcFunctions.find(funcName) != libcFunctions.end()) {
                errs()<<"wow "<<funcName<<" "<<global<<" and "<<prev<<"\n";
              // If it's a libc function, add an edge in the DOT file using unique IDs
               int current = global + 1;
              errs()<<"current : "<<current<<"\n";
                global++;
                if(prev!=-1){
                    dotFile << "    " << current << " [label=\""<<funcName<< "\"];\n";
                  dotFile << "    " << prev << " -> " << current << ";\n";
                }
                else {
                  if(Start.find(F) == Start.end()) 
                    Start[F] = current;
                  StartMap[&BB] = current;
                     dotFile << "    " << current << " [label=\"dummy "<<funcName<< "\"];\n";
                
                }
               prev = current;
               errs()<<"prev : "<<prev<<"\n";
              
              }
              else if (isUserDefined(calledFunc)) {
                errs()<<calledFunc->getName().str()<<" wakka \n";
                int current = global + 1;
                global++;
                //std::string str = calledFunc->getName().str();
                dotFile << "    " << current << " [label=\"start_" << calledFunc->getName().str() << "\"];\n";

                dotFile << "    " << prev << " -> " << current << ";\n";
                prev = current;
   
            }
            else if (calledFunc->isDeclaration()){
                  
                  if (calledFunc->isDeclaration()) {
    errs() << "External library function: " << calledFunc->getName() << "\n";
} else {
    errs() << "Internal function (defined in module): " << calledFunc->getName() << "\n";
    // Now access and print the basic blocks of the internal function
    for (BasicBlock &LibBB : *calledFunc) {
        errs() << "BasicBlock: " << LibBB.getName() << "\n";
        for (Instruction &LibInst : LibBB) {
            errs() << LibInst << "\n";
        }
    }
}



            }
            
            } 
          }
        }
    
        if(prev!=-1){   
       blockMap[&BB].push_back(prev);
      }
      else blockMap[&BB].push_back(-1);
    }
    

        for (BasicBlock &BB :*F) {
        //BasicBlock *BB = entry.first; // Key: BasicBlock*
        auto vec = blockMap[&BB];  

      if (vec[0]==-1) {
        errs()<<BB.getName();
        if(BB.getName().str() == "entry") {blockMap[&BB].push_back(0);}
        else {
        std::map<BasicBlock*, int> visited;
        Merge(&BB,blockMap,visited,global,dotFile);
       //  errs()<<"bc Debug line"<<F->getName()<<"\n";
      }
      }
      auto vec1 = blockMap[&BB];
      errs()<<BB.getName()<<" : ";
 
        }
       
  
  if(1){

    for(BasicBlock &BB : *F){
      int numPredecessors=0;
          for (BasicBlock *pred : predecessors(&BB)) {
        ++numPredecessors;
    }
     auto *terminator = BB.getTerminator();
    int numSuccessors;
    // If the terminator exists, count and print the successors
    if (terminator) {
        numSuccessors = terminator->getNumSuccessors();
    }

errs()<<BB.getName()<<" : Successors "<<numSuccessors<<" pred : "<<numPredecessors<<"Start map "<<StartMap[&BB]<<"\n";
      
      if(StartMap[&BB]!=-1){
      if(numPredecessors == 0) {
        dotFile << "    " << funprev << " -> " << StartMap[&BB] << ";\n";
      }

      else if (numSuccessors == 0) {
              errs()<<"basic block : "<<BB.getName()<<"\n";
      for (BasicBlock *pred : predecessors(&BB)) {
  
        for (auto e : blockMap[pred]){
          if (e != -1)
            dotFile << "    " << e << " -> " << StartMap[&BB] << ";\n";
          
        }
    }
     
         dotFile << "    " << blockMap[&BB][0] << " -> " << ++global << ";\n";
      }
      else {

      for (BasicBlock *pred : predecessors(&BB)) {
        for (auto e : blockMap[pred]){
           dotFile << "    " << e << " -> " << StartMap[&BB] << ";\n";  
        }

    }

      }
      }
      else {

            if(numPredecessors == 0) {

        dotFile << "    " << zero << " -> " << ++global  << ";\n";
      }
           else if (numSuccessors == 0) {
            int leaf = ++global;
            End[F] = leaf;
              errs()<<"basic block : "<<BB.getName()<<"\n";
      for (BasicBlock *pred : predecessors(&BB)) {
        
        for (auto e : blockMap[pred]){
          if(e != -1)
            dotFile << "    " << e << " -> " << leaf << ";\n";
          
        }
    }
     
      //   dotFile << "    " << blockMap[&BB][0] << " -> " << ++global << ";\n";
      }
 

      }

    }


  
  }







    return ;
    
    }
  


void Fun(Function & F){
  int count = 0;
errs()<<F.getName()<<"\n";
std::string folder = "/home/sakshi/Project/";
std::string str =  folder + F.getName().str() + ".dot";
  std::ofstream dotFile(str);
    
    // Write the opening of the DOT file format
    dotFile << "digraph CFG {\n";

  int prev = global;

 std::map<BasicBlock*, std :: vector<int>> blockMap;
 std::map<BasicBlock*,int> StartMap;
  std::map<Function*,int> Start;
   std::map<Function*,int> End;
 processFunction(&F,global,dotFile,blockMap,StartMap,Start,End);
   dotFile << "}\n";

    // Close the DOT file
    dotFile.close();
}






PreservedAnalyses Inst::run(Module &M,
                                            ModuleAnalysisManager &FAM) {
  for (Function &F : M) {
    if(F.getName() == "main" || isUserDefined(&F)){
Fun(F);
  }
  }
return PreservedAnalyses::all();
}


llvm::PassPluginLibraryInfo getInstPluginInfo() {
{
  return {LLVM_PLUGIN_API_VERSION, "libc-call-graph", LLVM_VERSION_STRING,
          [](PassBuilder &PB) {
            PB.registerPipelineParsingCallback(
                [](StringRef Name, ModulePassManager &FM,
                   ArrayRef<PassBuilder::PipelineElement>) {
                  if (Name == "libc-call-graph") {
                    FM.addPass(Inst());
                    return true;
                  }
                  return false;
                });
            PB.registerPipelineStartEPCallback(
                [](ModulePassManager &MPM, OptimizationLevel Level) {
                  MPM.addPass(Inst());
                  //MPM.addPass(LibcPolicyInsertDummy());
                });
          }};
}
}

extern "C" LLVM_ATTRIBUTE_WEAK ::llvm::PassPluginLibraryInfo
llvmGetPassPluginInfo() {
  return getInstPluginInfo();
}