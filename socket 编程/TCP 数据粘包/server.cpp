#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define BUF_SIZE 100

int main(){
    //创建套接字
    int serv_sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    
    //将套接字和IP、端口绑定
    struct sockaddr_in serv_addr;
    memset(&serv_addr, 0, sizeof(serv_addr));  //每个字节都用0填充
    serv_addr.sin_family = AF_INET;  //使用IPv4地址
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");  //具体的IP地址
    serv_addr.sin_port = htons(1234);  //端口
    bind(serv_sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr));
    
    //进入监听状态
    listen(serv_sock, 20);
    
    //接收客户端请求
    struct sockaddr_in clnt_addr;
    socklen_t clnt_addr_size = sizeof(clnt_addr);
    int clnt_sock = accept(serv_sock, (struct sockaddr*)&clnt_addr, &clnt_addr_size);
    
    sleep(3);  //注意这里，让程序暂停10秒
    
    //接收客户端发来的数据，并原样返回
    char buffer[BUF_SIZE];  //缓冲区
    int strlen = read(clnt_sock, buffer, BUF_SIZE); //接收客户端发来的数据
    write(clnt_sock, buffer, strlen);  //将数据原样返回
    
    //关闭套接字并终止DLL的使用
    close(clnt_sock);
    close(serv_sock);
    
    return 0;
}