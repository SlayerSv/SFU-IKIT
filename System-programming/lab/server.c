#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>
#include "process_values.h"

int serve(int clientSocket) {
    int size1;
    int size2;
    read(clientSocket, &size1, sizeof(size1));
    read(clientSocket, &size2, sizeof(size2));
    char value1[size1 + 1];
    char value2[size2 + 1];
    read(clientSocket, value1, size1);
    read(clientSocket, value2, size2);
    value1[size1] = '\0';
    value2[size2] = '\0';
    char result[512];
    result[0] = '\0';
    if (strcasecmp(value1, "shut") == 0 && strcasecmp(value2, "down") == 0) {
        return 1;
    }
    processValues(result, value1, size1, value2, size2);
    printf("%s\n", result);
    return 0;
}

int main(int argc, char* const argv[]) {
    if (argc < 2) {
        printf("Provide a socket name.\n");
        return EXIT_FAILURE;
    }
    unlink(argv[1]);
    int socketFD = socket(AF_LOCAL, SOCK_STREAM, 0);
    if (socketFD < 0) {
        printf("Error creating socket\n");
        return EXIT_FAILURE;
    }
    struct sockaddr_un name;
    name.sun_family = AF_LOCAL;
    const char* socketName = argv[1];
    strcpy (name.sun_path, socketName);
    int binded = bind(socketFD, (const struct sockaddr *)&name, SUN_LEN(&name));
    if (binded < 0) {
        printf("Error binding\n");
        return EXIT_FAILURE;
    }
    listen(socketFD, 1);
    int exit = 0;
    
    while (exit != 1) {
        struct sockaddr_un clientName;
        socklen_t clientNameLength;
        int clientSocketFD = accept(socketFD,
            (struct sockaddr*)&clientName, &clientNameLength);
        if (clientSocketFD < 0) {
            printf("Error accepting\n");
            return EXIT_FAILURE;
        }
        exit = serve(clientSocketFD);
        close(clientSocketFD);
    }
    close(socketFD);
    unlink(socketName);
    printf("Server shutting down.\n");
    return 0;
}