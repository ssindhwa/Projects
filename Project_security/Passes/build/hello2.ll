; ModuleID = 'hello1.ll'
source_filename = "../../hello.c"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@.str = private unnamed_addr constant [6 x i8] c"hello\00", align 1
@.str.1 = private unnamed_addr constant [2 x i8] c"d\00", align 1
@.str.2 = private unnamed_addr constant [7 x i8] c"tp.txt\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.0 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.1 = private unnamed_addr constant [8 x i8] c"copy_fd\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.2 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.3 = private unnamed_addr constant [8 x i8] c"copy_fd\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.4 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.5 = private unnamed_addr constant [8 x i8] c"copy_fd\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.6 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.7 = private unnamed_addr constant [8 x i8] c"copy_fd\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.8 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.9 = private unnamed_addr constant [8 x i8] c"copy_fd\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.10 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.11 = private unnamed_addr constant [8 x i8] c"copy_fd\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.12 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.13 = private unnamed_addr constant [5 x i8] c"main\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.14 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.15 = private unnamed_addr constant [5 x i8] c"main\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.16 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.17 = private unnamed_addr constant [5 x i8] c"main\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.18 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.19 = private unnamed_addr constant [5 x i8] c"main\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.20 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.21 = private unnamed_addr constant [5 x i8] c"main\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.22 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.23 = private unnamed_addr constant [5 x i8] c"main\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.24 = private unnamed_addr constant [34 x i8] c"BasicBlock start in function: %s\0A\00", align 1
@anon.08f7fd6e74875b09ef179660eba4a92f.25 = private unnamed_addr constant [5 x i8] c"main\00", align 1

; Function Attrs: noinline nounwind uwtable
define void @copy_fd(i32 noundef %0) #0 {
  %2 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.0, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.1)
  %3 = alloca i32, align 4
  store i32 %0, ptr %3, align 4
  %4 = load i32, ptr %3, align 4
  %5 = icmp eq i32 %4, 0
  br i1 %5, label %6, label %8

6:                                                ; preds = %1
  %7 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.2, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.3)
  br label %24

8:                                                ; preds = %1
  %9 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.4, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.5)
  %10 = call i64 @syscall(i64 462, i64 2080)
  %11 = call i32 (ptr, ...) @printf(ptr noundef @.str)
  %12 = call i64 @syscall(i64 462, i64 2080)
  %13 = call i32 (ptr, ...) @printf(ptr noundef @.str)
  %14 = load i32, ptr %3, align 4
  %15 = icmp sgt i32 %14, 3
  br i1 %15, label %16, label %20

16:                                               ; preds = %8
  %17 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.6, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.7)
  %18 = call i64 @syscall(i64 462, i64 2080)
  %19 = call i32 (ptr, ...) @printf(ptr noundef @.str.1)
  br label %20

20:                                               ; preds = %16, %8
  %21 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.8, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.9)
  %22 = load i32, ptr %3, align 4
  %23 = add nsw i32 %22, -1
  store i32 %23, ptr %3, align 4
  call void @copy_fd(i32 noundef %23)
  br label %24

24:                                               ; preds = %20, %6
  %25 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.10, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.11)
  ret void
}

declare i32 @printf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind uwtable
define i32 @main() #0 {
  %1 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.12, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.13)
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  %7 = alloca i32, align 4
  %8 = alloca i32, align 4
  store i32 0, ptr %2, align 4
  store i32 1, ptr %3, align 4
  store i32 2, ptr %4, align 4
  store i32 3, ptr %5, align 4
  %9 = call i64 @syscall(i64 462, i64 1107)
  %10 = call i32 @brk(ptr noundef null) #3
  %11 = call i64 @syscall(i64 462, i64 2021)
  %12 = call i32 @pipe(ptr noundef null) #3
  %13 = call i64 @syscall(i64 462, i64 2614)
  %14 = call i32 @socket(i32 noundef 0, i32 noundef 0, i32 noundef 0) #3
  %15 = load i32, ptr %4, align 4
  %16 = load i32, ptr %3, align 4
  %17 = sub nsw i32 %15, %16
  %18 = load i32, ptr %5, align 4
  %19 = sub nsw i32 %18, 1
  %20 = icmp eq i32 %17, %19
  br i1 %20, label %21, label %25

21:                                               ; preds = %0
  %22 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.14, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.15)
  %23 = call i64 @syscall(i64 462, i64 1185)
  %24 = call i32 @connect(i32 noundef 0, ptr noundef null, i32 noundef 0)
  br label %42

25:                                               ; preds = %0
  %26 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.16, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.17)
  %27 = load i32, ptr %5, align 4
  %28 = load i32, ptr %4, align 4
  %29 = sub nsw i32 %27, %28
  %30 = load i32, ptr %3, align 4
  %31 = icmp eq i32 %29, %30
  br i1 %31, label %32, label %36

32:                                               ; preds = %25
  %33 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.18, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.19)
  %34 = call i64 @syscall(i64 462, i64 2384)
  %35 = call i64 @read(i32 noundef 0, ptr noundef null, i64 noundef 0)
  br label %40

36:                                               ; preds = %25
  %37 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.20, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.21)
  %38 = call i64 @syscall(i64 462, i64 3081)
  %39 = call i64 @write(i32 noundef 2, ptr noundef @.str.2, i64 noundef 5)
  br label %40

40:                                               ; preds = %36, %32
  %41 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.22, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.23)
  br label %42

42:                                               ; preds = %40, %21
  %43 = call i32 (ptr, ...) @printf(ptr @anon.08f7fd6e74875b09ef179660eba4a92f.24, ptr @anon.08f7fd6e74875b09ef179660eba4a92f.25)
  %44 = call i64 @syscall(i64 462, i64 1234)
  %45 = call i32 @dup(i32 noundef 0) #3
  store i32 358, ptr %7, align 4
  %46 = load i32, ptr %7, align 4
  call void asm sideeffect "mov $$462, %eax\0Amov $0, %edi\0Asyscall\0A", "r,~{rax},~{rdi},~{rcx},~{r11},~{dirflag},~{fpsr},~{flags}"(i32 %46) #3, !srcloc !7
  %47 = call i32 asm sideeffect "mov $$39, %eax\0Asyscall\0Amov %eax, $0\0A", "=r,~{rax},~{rcx},~{r11},~{dirflag},~{fpsr},~{flags}"() #3, !srcloc !8
  store i32 %47, ptr %8, align 4
  %48 = load i32, ptr %6, align 4
  call void @copy_fd(i32 noundef %48)
  %49 = call i64 @syscall(i64 462, i64 1537)
  %50 = call i32 @getpid() #3
  %51 = call i64 @syscall(i64 462, i64 2945)
  %52 = call i32 @uname(ptr noundef null) #3
  %53 = load i32, ptr %2, align 4
  ret i32 %53
}

; Function Attrs: nounwind
declare i32 @brk(ptr noundef) #2

; Function Attrs: nounwind
declare i32 @pipe(ptr noundef) #2

; Function Attrs: nounwind
declare i32 @socket(i32 noundef, i32 noundef, i32 noundef) #2

declare i32 @connect(i32 noundef, ptr noundef, i32 noundef) #1

declare i64 @read(i32 noundef, ptr noundef, i64 noundef) #1

declare i64 @write(i32 noundef, ptr noundef, i64 noundef) #1

; Function Attrs: nounwind
declare i32 @dup(i32 noundef) #2

; Function Attrs: nounwind
declare i32 @getpid() #2

; Function Attrs: nounwind
declare i32 @uname(ptr noundef) #2

declare i64 @syscall(i64)

attributes #0 = { noinline nounwind uwtable "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #2 = { nounwind "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #3 = { nounwind }

!llvm.module.flags = !{!0, !1, !2, !3, !4, !5}
!llvm.ident = !{!6}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 8, !"PIC Level", i32 2}
!2 = !{i32 7, !"uwtable", i32 2}
!3 = !{i32 7, !"frame-pointer", i32 2}
!4 = !{i32 1, !"ThinLTO", i32 0}
!5 = !{i32 1, !"EnableSplitLTOUnit", i32 1}
!6 = !{!"Debian clang version 18.1.8 (12)"}
!7 = !{i64 3560, i64 3626, i64 3692}
!8 = !{i64 3992, i64 4057, i64 4110}

^0 = module: (path: "[Regular LTO]", hash: (0, 0, 0, 0, 0))
^1 = gv: (name: "getpid") ; guid = 321299767020889832
^2 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.17", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 2045457854074618345
^3 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.2", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 2246143964626657898
^4 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.4", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 2528241040429544503
^5 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.20", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 4384613374424514237
^6 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.3", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 4676058904893388484
^7 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.15", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 4980817357453877998
^8 = gv: (name: "syscall") ; guid = 5708696911471316322
^9 = gv: (name: "socket") ; guid = 5748192229220086369
^10 = gv: (name: "copy_fd", summaries: (function: (module: ^0, flags: (linkage: external, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 0, canAutoHide: 0), insts: 28, funcFlags: (readNone: 0, readOnly: 0, noRecurse: 0, returnDoesNotAlias: 0, noInline: 1, alwaysInline: 0, noUnwind: 1, mayThrow: 0, hasUnknownCall: 0, mustBeUnreachable: 0), calls: ((callee: ^15, relbf: 1408), (callee: ^8, relbf: 400), (callee: ^10, relbf: 160)), refs: (^13, ^17, ^3, ^6, ^4, ^26, ^21, ^14, ^24, ^23, ^12, ^29, ^38, ^40)))) ; guid = 6261364076022825687
^11 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.12", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 6349301800846833539
^12 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.8", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 7000520355739270981
^13 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.0", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 7104044235569404190
^14 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.6", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 7371530195404317898
^15 = gv: (name: "printf") ; guid = 7383291119112528047
^16 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.14", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 7862039339156430468
^17 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.1", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 8349224627532381264
^18 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.22", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 8994622344882500848
^19 = gv: (name: "pipe") ; guid = 9037631216465642016
^20 = gv: (name: "read") ; guid = 9513132370838138604
^21 = gv: (name: ".str", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 10020523082937724387
^22 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.24", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 10342704725285354316
^23 = gv: (name: ".str.1", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 10623064228550193122
^24 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.7", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 11129413011881400435
^25 = gv: (name: "connect") ; guid = 11863149126166266038
^26 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.5", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 12095444349515053750
^27 = gv: (name: "uname") ; guid = 12285847186102108224
^28 = gv: (name: ".str.2", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 12529638326624903925
^29 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.9", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 13005922540010770136
^30 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.18", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 13697103460113970260
^31 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.23", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 14743926847370722138
^32 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.19", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 14946954380135025594
^33 = gv: (name: "brk") ; guid = 15059987308766194328
^34 = gv: (name: "write") ; guid = 15399970846829621999
^35 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.13", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 15475730452829370109
^36 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.21", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 15691030927005604526
^37 = gv: (name: "main", summaries: (function: (module: ^0, flags: (linkage: external, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 0, canAutoHide: 0), insts: 62, funcFlags: (readNone: 0, readOnly: 0, noRecurse: 0, returnDoesNotAlias: 0, noInline: 1, alwaysInline: 0, noUnwind: 1, mayThrow: 0, hasUnknownCall: 1, mustBeUnreachable: 0), calls: ((callee: ^15, relbf: 1024), (callee: ^8, relbf: 1792), (callee: ^33, relbf: 256), (callee: ^19, relbf: 256), (callee: ^9, relbf: 256), (callee: ^25, relbf: 128), (callee: ^20, relbf: 64), (callee: ^34, relbf: 64), (callee: ^39, relbf: 256), (callee: ^10, relbf: 256), (callee: ^1, relbf: 256), (callee: ^27, relbf: 256)), refs: (^11, ^35, ^16, ^7, ^42, ^2, ^30, ^32, ^5, ^36, ^28, ^18, ^31, ^22, ^41)))) ; guid = 15822663052811949562
^38 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.10", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 16724274226289341003
^39 = gv: (name: "dup") ; guid = 16751626174917943054
^40 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.11", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 17139504015107458843
^41 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.25", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 17402418010091931317
^42 = gv: (name: "anon.08f7fd6e74875b09ef179660eba4a92f.16", summaries: (variable: (module: ^0, flags: (linkage: private, visibility: default, notEligibleToImport: 1, live: 0, dsoLocal: 1, canAutoHide: 0), varFlags: (readonly: 1, writeonly: 0, constant: 1)))) ; guid = 18053729617414250297
^43 = flags: 8
^44 = blockcount: 0
