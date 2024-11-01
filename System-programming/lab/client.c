#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <unistd.h>

void WriteText(int socketFD, const char* value1, const char* value2) {
    int length1 = strlen(value1);
    int length2 = strlen(value2);
    write(socketFD, &length1, sizeof(length1));
    write(socketFD, &length2, sizeof(length2));
    write(socketFD, value1, length1);
    write(socketFD, value2, length2);
}

int main(int argc, char* const argv[]) {
    if (argc != 4) {
        printf("Must provide socketname and 2 values.\n");
        return EXIT_FAILURE;
    }
    struct sockaddr_un name;
    name.sun_family = AF_LOCAL;
    const char* const socket_name = argv[1];
    strcpy(name.sun_path, socket_name);
    int socketFD = socket(AF_LOCAL, SOCK_STREAM, 0);
    if (socketFD < 0) {
        printf("Error creating socket\n");
        return EXIT_FAILURE;
    }
    int connected = connect(socketFD, (const struct sockaddr *)&name, SUN_LEN(&name));
    if (connected < 0) {
        printf("Error connecting to socket\n");
        return EXIT_FAILURE;
    }
    const char* value1 = argv[2];
    const char* value2 = argv[3];
    WriteText(socketFD, value1, value2);
    close(socketFD);
    return 0;
}