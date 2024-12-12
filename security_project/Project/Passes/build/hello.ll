; ModuleID = '../../hello.c'
source_filename = "../../hello.c"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

@.str = private unnamed_addr constant [6 x i8] c"hello\00", align 1
@.str.1 = private unnamed_addr constant [2 x i8] c"d\00", align 1
@.str.2 = private unnamed_addr constant [7 x i8] c"tp.txt\00", align 1

; Function Attrs: noinline nounwind uwtable
define void @copy_fd(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = icmp eq i32 %3, 0
  br i1 %4, label %5, label %6

5:                                                ; preds = %1
  br label %16

6:                                                ; preds = %1
  %7 = call i32 (ptr, ...) @printf(ptr noundef @.str)
  %8 = call i32 (ptr, ...) @printf(ptr noundef @.str)
  %9 = load i32, ptr %2, align 4
  %10 = icmp sgt i32 %9, 3
  br i1 %10, label %11, label %13

11:                                               ; preds = %6
  %12 = call i32 (ptr, ...) @printf(ptr noundef @.str.1)
  br label %13

13:                                               ; preds = %11, %6
  %14 = load i32, ptr %2, align 4
  %15 = add nsw i32 %14, -1
  store i32 %15, ptr %2, align 4
  call void @copy_fd(i32 noundef %15)
  br label %16

16:                                               ; preds = %13, %5
  ret void
}

declare i32 @printf(ptr noundef, ...) #1

; Function Attrs: noinline nounwind uwtable
define i32 @main() #0 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  %7 = alloca i32, align 4
  store i32 0, ptr %1, align 4
  store i32 1, ptr %2, align 4
  store i32 2, ptr %3, align 4
  store i32 3, ptr %4, align 4
  %8 = call i32 @brk(ptr noundef null) #3
  %9 = call i32 @pipe(ptr noundef null) #3
  %10 = call i32 @socket(i32 noundef 0, i32 noundef 0, i32 noundef 0) #3
  %11 = load i32, ptr %3, align 4
  %12 = load i32, ptr %2, align 4
  %13 = sub nsw i32 %11, %12
  %14 = load i32, ptr %4, align 4
  %15 = sub nsw i32 %14, 1
  %16 = icmp eq i32 %13, %15
  br i1 %16, label %17, label %19

17:                                               ; preds = %0
  %18 = call i32 @connect(i32 noundef 0, ptr noundef null, i32 noundef 0)
  br label %30

19:                                               ; preds = %0
  %20 = load i32, ptr %4, align 4
  %21 = load i32, ptr %3, align 4
  %22 = sub nsw i32 %20, %21
  %23 = load i32, ptr %2, align 4
  %24 = icmp eq i32 %22, %23
  br i1 %24, label %25, label %27

25:                                               ; preds = %19
  %26 = call i64 @read(i32 noundef 0, ptr noundef null, i64 noundef 0)
  br label %29

27:                                               ; preds = %19
  %28 = call i64 @write(i32 noundef 2, ptr noundef @.str.2, i64 noundef 5)
  br label %29

29:                                               ; preds = %27, %25
  br label %30

30:                                               ; preds = %29, %17
  %31 = call i32 @dup(i32 noundef 0) #3
  store i32 358, ptr %6, align 4
  %32 = load i32, ptr %6, align 4
  call void asm sideeffect "mov $$462, %eax\0Amov $0, %edi\0Asyscall\0A", "r,~{rax},~{rdi},~{rcx},~{r11},~{dirflag},~{fpsr},~{flags}"(i32 %32) #3, !srcloc !5
  %33 = call i32 asm sideeffect "mov $$39, %eax\0Asyscall\0Amov %eax, $0\0A", "=r,~{rax},~{rcx},~{r11},~{dirflag},~{fpsr},~{flags}"() #3, !srcloc !6
  store i32 %33, ptr %7, align 4
  %34 = load i32, ptr %5, align 4
  call void @copy_fd(i32 noundef %34)
  %35 = call i32 @getpid() #3
  %36 = call i32 @uname(ptr noundef null) #3
  %37 = load i32, ptr %1, align 4
  ret i32 %37
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

attributes #0 = { noinline nounwind uwtable "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #2 = { nounwind "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #3 = { nounwind }

!llvm.module.flags = !{!0, !1, !2, !3}
!llvm.ident = !{!4}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 8, !"PIC Level", i32 2}
!2 = !{i32 7, !"uwtable", i32 2}
!3 = !{i32 7, !"frame-pointer", i32 2}
!4 = !{!"Debian clang version 18.1.8 (12)"}
!5 = !{i64 3560, i64 3626, i64 3692}
!6 = !{i64 3992, i64 4057, i64 4110}
