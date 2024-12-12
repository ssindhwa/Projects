// // C program for Implementing prefix sum array
// #include <stdio.h>

// void foo(){
// 	printf("haan bhai");
// }

// // Fills prefix sum array
// void fillPrefixSum(int arr[], int n, int prefixSum[])
// {
// 	prefixSum[0] = arr[0];

// 	// Adding present element with previous elementc
// 	for (int i = 1; i < n; i++){
// 		prefixSum[i] = prefixSum[i - 1] + arr[i];
// 	}
// 	if(prefixSum[0]>1)
// 	printf("ee");
// 	else 
// 	printf("e");

// 	foo();	
// }

// // Driver Code		
// int main()
// {
// 	int arr[] = { 10, 4, 16, 20 };
// 	printf("wow");
// 	printf("wow");
// 	int n = sizeof(arr) / sizeof(arr[0]);
// 	int prefixSum[n];

// 	// Function call
// 	 fillPrefixSum(arr, n, prefixSum);

// 	 fillPrefixSum(arr, n, prefixSum);

//  fillPrefixSum(arr, n, prefixSum);
	
// 	int x;
// 	scanf("%d",&x);
// 	if(x > 5) 
// 		printf("wow");
// 	else {

// 	for (int i = 0; i < n; i++)
// 		{
// 		printf("%d ", prefixSum[i]);
//     	scanf("%d",&n);
// 	}
// 	}
// 	printf("d");
// }
// #include<stdio.h>
// #include<stdlib.h>
// #include<sys/socket.h>
// #include <sys/utsname.h> 

// int foo()
// {	
//     struct utsname *buf;
//     return uname(buf);
// }

// int main()
// {
//     char buf[10];
//     int sock = socket(AF_INET,SOCK_STREAM,0);
//     if(sock != -1)
//     {
//         //read(0,buf,5);
//         int copy_sock = dup(sock);
//     }
//     else
//     {
//         write(2,"failed\n",7);
//         foo();
//     }
//     return 0;
// }

// #include<stdio.h>
// #include<stdlib.h>
#include <stdio.h>         // For printf(), perror(), etc.
#include <stdlib.h>        // For exit(), brk(), etc.
#include <unistd.h>        // For pipe(), dup(), getpid(), write(), read(), etc.
#include <fcntl.h>         // For open(), openat(), O_RDONLY, etc.
#include <sys/socket.h>    // For socket(), connect(), etc.
#include <sys/utsname.h>   // For uname()
// void copy_fd(int x)
// {
// 	printf("wow");
//     if(x == 0){
// 		printf("wow");
//         return;
// 	}
//     printf("de");
//   //  int file_desc = openat("libc.txt");
//    // int copy_desc = dup(file_desc);
//     copy_fd(--x);
// }

// int main()
// {
//     int x = 1, y = 2, z = 3;
//     brk(0);
//     pipe(0);
//     socket(0,0,0);

//     if(y-x == z-1){
// 		printf("wow");
// 		connect(0,0,0);
// 	}
        
//     else if(z-y == x){
// 		printf("wow");
// 		read(0,0,0);
// 	}
        
//     else{
// 		printf("wow");
// 		write(2,"hello",5);
// 	}
        
//     dup(0);
//     getpid();
//     uname(0);

// }


#include<stdio.h>
#include<stdlib.h>
#include <stdio.h>         // For printf(), perror(), etc.
#include <stdlib.h>        // For exit(), brk(), etc.
#include <unistd.h>        // For pipe(), dup(), getpid(), write(), read(), etc.
#include <fcntl.h>         // For open(), openat(), O_RDONLY, etc.
#include <sys/socket.h>    // For socket(), connect(), etc.
#include <sys/utsname.h>   // For uname()

void copy_fd(int x)
{
    if(x == 0)
        return;
    printf("hello");
     printf("hello");
     if(x>3) 
    printf("d");
    // int file_desc = openat("test");
    // int copy_desc = dup(file_desc);
    copy_fd(--x);
}

int main()
{
    int x = 1, y = 2, z = 3;
    brk(0);
    pipe(0);
    socket(0,0,0);

    if(y-x == z-1)
        connect(0,0,0);
    else if(z-y == x)
        read(0,0,0);
    else
        write(2,"tp.txt",5);
       //  write(2,"tp.txt",5);
    dup(0);
    int g;
   //scanf("%d",&g);
    //scanf("%d",&g);
  int param = 358;
    int pid;

    // Inline assembly for system call number 462
    asm volatile(
        "mov $462, %%eax\n"  // Load syscall number 462 into rax
        "mov %0, %%edi\n"    // Load the parameter (369) into rdi
        "syscall\n"          // Make the system call
        :                    // No output operands
        : "r"(param)         // Input operand: param
        : "rax", "rdi", "rcx", "r11" // Clobbered registers
    );

    // Inline assembly for system call getpid (number 39)
    asm volatile(
        "mov $39, %%eax\n"   // Load syscall number 39 into rax
        "syscall\n"          // Make the system call
        "mov %%eax, %0\n"    // Store the return value (pid) in 'pid'
        : "=r"(pid)          // Output operand: pid
        :                    // No input operands
        : "rax", "rcx", "r11" // Clobbered registers
    );





    
    copy_fd(g);
    getpid();
    uname(0);

}