

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
   scanf("%d",&g);
   

    
    copy_fd(g);
    getpid();
    uname(0);

}