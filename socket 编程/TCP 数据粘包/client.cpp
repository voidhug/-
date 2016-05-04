#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>

#define BUF_SIZE 100

int main(){
    //创建套接字
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    
    //向服务器（特定的IP和端口）发起请求
    struct sockaddr_in serv_addr;
    memset(&serv_addr, 0, sizeof(serv_addr));  //每个字节都用0填充
    serv_addr.sin_family = AF_INET;  //使用IPv4地址
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");  //具体的IP地址
    serv_addr.sin_port = htons(1234);  //端口
    connect(sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr));
    
    //获取用户输入的字符串并发送给服务器
    char bufSend[BUF_SIZE] = {0};
    printf("Input a string: ");
    gets(bufSend);
    for(int i=0; i<3; i++){
        write(sock, bufSend, strlen(bufSend));
    }
    //接收服务器传回的数据
    char bufRecv[BUF_SIZE] = {0};
    read(sock, bufRecv, BUF_SIZE);
    
    //输出接收到的数据
    printf("Message form server: %s\n", bufRecv);
    
    close(sock); //关闭套接字
    
    return 0;
}