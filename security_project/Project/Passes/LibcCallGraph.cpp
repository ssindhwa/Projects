//===- InstructionNamer.cpp - Give anonymous instructions names -----------===//
//
// Part of the LLVM Project, under the Apache License v2.0 with LLVM Exceptions.
// See https://llvm.org/LICENSE.txt for license information.
// SPDX-License-Identifier: Apache-2.0 WITH LLVM-exception
//
//===----------------------------------------------------------------------===//
//
// This is a little utility pass that gives instructions names, this is mostly
// useful when diffing the effect of an optimization because deleting an
// unnamed instruction can change all other instruction numbering, making the
// diff very noisy.
//
//===----------------------------------------------------------------------===//
#include "LibcCallGraph.h"
#include "llvm/Transforms/Utils/InstructionNamer.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/PassManager.h"
#include "llvm/IR/Type.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/IR/Instructions.h"
#include "llvm/IR/Module.h"
#include "llvm/Pass.h"
#include "llvm/Support/raw_ostream.h" 
#include "llvm/IR/Function.h"
#include "llvm/IR/IRBuilder.h"
#include "llvm/Passes/PassBuilder.h"
#include "llvm/Passes/PassPlugin.h"
#include <set>
#include <map>
#include <fstream>
#include <vector>
#include <regex>

#define DEBUG_TYPE "libc-call-graph"
using namespace llvm;

namespace {
void nameInstructions(Function &F) {


 LLVMContext &context = F.getContext();
      Module *module = F.getParent(); 

      // Declare printf if it is not declared already.
      FunctionType *printfType = FunctionType::get(IntegerType::getInt32Ty(context),
                                                   PointerType::get(Type::getInt8Ty(context), 0),
                                                   true);
      FunctionCallee printfFunc = module->getOrInsertFunction("printf", printfType);

      // Iterate through all basic blocks in the function.
      for (BasicBlock &BB : F) {
        // Insert at the beginning of each basic block.
        Instruction *firstInst = &*(BB.begin()); // Get the first instruction in the block.

        // Prepare the format string for printf.
        IRBuilder<> builder(firstInst); // Create an IRBuilder at the position of the first instruction.
        Value *formatStr = builder.CreateGlobalStringPtr("BasicBlock start in function: %s\n");

        // Get the function name (optional, for display purposes).
        Value *funcName = builder.CreateGlobalStringPtr(F.getName());

        // Prepare the arguments for printf.
        std::vector<Value *> printfArgs;
        printfArgs.push_back(formatStr);
        printfArgs.push_back(funcName);

        // Insert the call to printf.
        builder.CreateCall(printfFunc, printfArgs);
      }

}

} // namespace

PreservedAnalyses LibcCallGraph::run(Module &M,
                                            ModuleAnalysisManager &FAM) {


        std::vector<std::string> libcCalls;
 //std::regex funcRegex(R"(\b(\w+)\s*\()"); 
     std::regex funcRegex(R"(\b(\w+)\s*\()");  
    // Read strings from a text file
    std::ifstream infile("/home/dev/Project/libc.txt");
    std::string line;
    // Check if the file is opened successfully
    if (!infile.is_open()) {
      errs() << "Error opening file\n";
    }

    // Read each line from the file and insert it into the set
    int ind = 0;
    while (std::getline(infile, line)) {
     // errs()<<line<<"libc functiopn\n";
        // Store the function name (match[1] is the first captured group, the function name)
          libcCalls.push_back(line);
    }








    // Close the file
    infile.close();

for(Function &F : M){

  LLVMContext &Context = F.getContext();

        // Find or declare the syscall function in the module
        Module *M = F.getParent();


Function *syscallFunc = cast<Function>(M->getOrInsertFunction(
    "syscall",
    FunctionType::get(
        Type::getInt64Ty(Context),    // Return type (int32 for the syscall result)
        {Type::getInt64Ty(Context)}, // Single argument: int32
        false                        // Not a variadic function
    )).getCallee());




        

        for (BasicBlock &BB : F) {
            for (auto I = BB.begin(), E = BB.end(); I != E; ++I) {
                // Check if it's a call instruction
                if (CallInst *callInst = dyn_cast<CallInst>(&*I)) {
               if (Function *calledFunc = callInst->getCalledFunction()) {
            std::string funcName = calledFunc->getName().str();

    std::string searchStr = funcName;

    // Use std::find to get an iterator to the element
    auto it = std::find(libcCalls.begin(), libcCalls.end(), searchStr);

    if (it != libcCalls.end()) {
        // Compute the index of the found element
        int index = std::distance(libcCalls.begin(), it);
      

       IRBuilder<> builder(callInst);

                    // Insert the system call before the call instruction
                    Value *syscallNum = builder.getInt64(462); // Syscall number 999
                    Value *arg = builder.getInt64(index);       // Example integer argument (1234)

                    // Insert the syscall: syscall(999, 1234)
                    builder.CreateCall(syscallFunc, {syscallNum, arg});

                    // Now the system call is inserted before every function call.





    } 



               }








             
                }
            }
        }


}
for (Function &F : M) {

  nameInstructions(F);
}
  return PreservedAnalyses::all();
}

llvm::PassPluginLibraryInfo getLibcPolicyPluginInfo() {
{
  return {LLVM_PLUGIN_API_VERSION, "libc-call-graph", LLVM_VERSION_STRING,
          [](PassBuilder &PB) {
            PB.registerPipelineParsingCallback(
                [](StringRef Name, ModulePassManager &FM,
                   ArrayRef<PassBuilder::PipelineElement>) {
                  if (Name == "libc-call-graph") {
                    FM.addPass(LibcCallGraph());
                    return true;
                  }
                  return false;
                });
            PB.registerPipelineStartEPCallback(
                [](ModulePassManager &MPM, OptimizationLevel Level) {
                  MPM.addPass(LibcCallGraph());
                  //MPM.addPass(LibcPolicyInsertDummy());
                });
          }};
}
}

extern "C" LLVM_ATTRIBUTE_WEAK ::llvm::PassPluginLibraryInfo
llvmGetPassPluginInfo() {
  return getLibcPolicyPluginInfo();
}
