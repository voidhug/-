#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define BUF_SIZE 100

int main(){
    //向服务器发起请求
    struct sockaddr_in serv_addr;
    memset(&serv_addr, 0, sizeof(serv_addr));  //每个字节都用0填充
    serv_addr.sin_family = PF_INET;
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    serv_addr.sin_port = htons(1234);
    
    char bufSend[BUF_SIZE] = {0};
    char bufRecv[BUF_SIZE] = {0};
    
    while(1){
        //创建套接字
        int sock = socket(AF_INET, SOCK_STREAM, 0);
        connect(sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr));
        //获取用户输入的字符串并发送给服务器
        printf("Input a string: ");
        gets(bufSend);
        write(sock, bufSend, strlen(bufSend));
        //接收服务器传回的数据
        read(sock, bufRecv, BUF_SIZE);
        //输出接收到的数据
        printf("Message form server: %s\n", bufRecv);
        
        memset(bufSend, 0, BUF_SIZE);  //重置缓冲区
        memset(bufRecv, 0, BUF_SIZE);  //重置缓冲区
        close(sock);
    }
    
    return 0;
}