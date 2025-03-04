//==============================================================================
// FILE:
//    LibcPolicy.h
//
// DESCRIPTION:
//    Declares the LibcPolicy pass for the new and the legacy pass managers.
//
// License: MIT
//==============================================================================
#ifndef LLVM_TUTOR_INSTRUMENT_BASIC_H
#define LLVM_TUTOR_INSTRUMENT_BASIC_H

#include "llvm/IR/PassManager.h"
#include "llvm/Pass.h"

//------------------------------------------------------------------------------
// New PM interface
//------------------------------------------------------------------------------
struct LibcCallGraph : public llvm::PassInfoMixin<LibcCallGraph> {

  llvm::PreservedAnalyses run(llvm::Module &M,
                              llvm::ModuleAnalysisManager &FM);

  static bool isRequired() { return true; }
};


struct Inst : public llvm::PassInfoMixin<Inst> {

  llvm::PreservedAnalyses run(llvm::Module &M,
                              llvm::ModuleAnalysisManager &FM);

  static bool isRequired() { return true; }
};


// struct LibcPolicyInsertDummy : public llvm::PassInfoMixin<LibcPolicyInsertDummy> {
//   llvm::PreservedAnalyses run(llvm::Module &F,
//                               llvm::ModuleAnalysisManager &FM);

//   static bool isRequired() { return true; }
// };

#endif