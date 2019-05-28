#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "csapp.h"

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>

  /* Recommended max cache and object sizes */
#define MAX_CACHE_SIZE 1049000
#define MAX_OBJECT_SIZE 102400

char cache[MAX_CACHE_SIZE];
char fullRequestHeader[2000];
int inMemory = 0;


struct Node * node;

typedef struct Node {
  char * fileName;
  char * data;
  char * hostName;
  struct Node * next;
  int size;
}
Node;

Node * head;
Node * tail;

char method[MAXLINE], uri[MAXLINE], tempLine[MAXLINE],portNumberSTR[MAXLINE];
char firstLine[MAXLINE], portNum[MAXLINE], buf[MAXLINE], fileName[MAXLINE];
char fileName[MAXLINE],  hostName[MAXLINE], hostHeader[MAXLINE], headbuf[MAXLINE];
char clientHeaders[MAXLINE];

/* You won't lose style points for including this long line in your code */
static const char * user_agent_hdr = "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:10.0.3) Gecko/20120305 Firefox/10.0.3\r\n";
int serverPortNumber = 0;
char *portNumber;
static const char * connectionHeader = "Connection: close\r\n";
const char * version = "HTTP/1.0";
static const char * proxyConnectionHeader = "Proxy-Connection: close\r\n";
char servertxt[MAX_OBJECT_SIZE];
int clientfd;
rio_t rio, rio2;
int DocsInCache = 0;
int fileSize;

void clearAllbuffers() {
  memset(method, 0, MAXLINE);
  memset(uri, 0, MAXLINE);
  memset(portNumberSTR, 0, MAXLINE);
  memset(fileName, 0, MAXLINE);
  memset(hostHeader, 0, MAXLINE);
  memset(hostName, 0, MAXLINE);
  memset(portNumberSTR, 0, MAXLINE);
  memset(headbuf, 0, MAXLINE);
  memset(clientHeaders, 0, MAXLINE);
}


void enque() {
  DocsInCache++;
  Node * newNode = (Node * ) malloc(sizeof(Node));
  //printf("ENQUING\n");
  newNode -> data = malloc(strlen(servertxt) + 1);
  newNode -> fileName = malloc(strlen(fileName) + 1);
  newNode -> hostName = malloc(strlen(hostName) + 1);
  strcpy(newNode -> hostName, hostName);
  strcpy(newNode -> data, servertxt);
  strcpy(newNode -> fileName, fileName);
  newNode -> size = fileSize;
  //printf("Copied\n");
  if (head == NULL) {
    //printf("InsertingHead\n");
    head = newNode;
    tail = newNode;
    head -> next = NULL;
    tail -> next = NULL;
  } else {
    tail -> next = newNode;
    tail = newNode;
  }
}

void deque() {
  inMemory -= head->size;
  Node * temp = head -> next;
  free(head);
  head = temp;

}

Node * checkCache() {
  //printf("checkingCache\n");
  if (head == NULL) return NULL;
  Node * current = head;
  
  for (int i = 0; i < DocsInCache; current = current -> next, i++) {

    //printf("searchname%s\n", current - > fileName);
    //printf("current name%s\n", fileName);
    if (!(strcmp(current -> fileName, fileName))) {
      if (!(strcmp(current -> hostName, hostName)) && strlen(fileName) == strlen(current->fileName)) {
        printf("FOUND IN CACHE\n");
        fileSize = current -> size;
        return current;
      }
    }

  }

  //printf("LEAVING CHECK CACHE\n");
  return NULL;
}

void sendBack() {
  printf("Right before sending back\n");
  int status = rio_writen(clientfd, servertxt, fileSize);
  if(status <0) printf("problem sennding back");
  clearAllbuffers();
  close(clientfd);
}

void createHeadersToSend() {
char * get = "Host: ";
  char * newLine = "\r\n";
  char * space = " ";

  strcat(headbuf, method);
  strcat(headbuf, space);
  strcat(headbuf, fileName);
  strcat(headbuf, space);
  strcat(headbuf, version);
  strcat(headbuf, newLine);

  strcpy(hostHeader, get);
  strcat(hostHeader, hostName);
  strcat(hostHeader, newLine);
 
  //strcat(headbuf, clientHeaders);
 // strcat(clientHeaders, newLine);
  strcat(headbuf, firstLine);
  strcat(headbuf, hostHeader);
  strcat(headbuf, user_agent_hdr);
  strcat(headbuf, connectionHeader);
  strcat(headbuf, proxyConnectionHeader);
  //strcat(clientHeaders, newLine);
  //strcat(headbuf, clientHeaders);

 
  strcat(headbuf, newLine);

}




void serveRequest() {

  Node * node = checkCache();
  if (node != NULL) {
    //printf("sendingback\n");
    strcpy(servertxt, node -> data);
    sendBack();
    return;
  }

  createHeadersToSend();
  printf("headers sending: \n\n%s\n", headbuf);
 
  int status;
  int fd = open_clientfd(hostName, portNum);
  if (fd < 1) {
    printf("problem processing. Opening cleint\n");
    close(clientfd);
    clearAllbuffers();
    return;

  }
  printf("FOund SERVER %s\n", hostName);
//rio2 is from server
     rio_readinitb(&rio2, fd);
  status = rio_writen(fd, headbuf, sizeof(headbuf));
  if (status < 1) {
    printf("problem processing\n");
    clearAllbuffers();
    return;
  }
  printf("Sending request\n");  
  fileSize = rio_readnb(&rio2, servertxt, MAX_OBJECT_SIZE);
  if (fileSize < 0) {
    printf("Connection reset\n");
    clearAllbuffers();
    close(clientfd);
    return;
  }


  // printf("\n\nGOT BACK: %s\n", servertxt);
  //printf("ABOUTTO ENQUE\n");
  if (!(sizeof(servertxt) > MAX_OBJECT_SIZE)) {
    while (inMemory + sizeof(servertxt) >= MAX_CACHE_SIZE) {
      printf("dequing\n");
      deque();
    }
    enque();
    inMemory += fileSize;
    //printf("dequing%d", inMemory);
  }
  printf("got data back. Sending to client\n");
    sendBack(); // send back clears buffers so have to enueu it first


  printf("Got Back: \n\n\n\n\n\n\n%s", servertxt);

  

}

int parse_uri(char * uri) {

  int foundPort = 0;
  int i = strcspn(uri, "http://");
  int pos = i + 7;
  int length = strlen(uri);
  int placer = 0;

  for (; pos < length; pos++) {

    if (uri[pos] == '/') {
      break;
    } else if (uri[pos] == ':') {
      int placer = 0;
      foundPort = 1;
      for (pos = pos + 1; pos < length; pos++, placer++) {
        if (uri[pos] == '/') goto FILE;
        else portNum[placer] = uri[pos];
      }
    } else {
      hostName[pos - 7] = uri[pos];
    }
  }

  FILE:
    fileName[0] = '/';
  for (pos = pos + 1; pos <= length; pos++, placer++) {
    fileName[placer + 1] = uri[pos];
  }

  if (!foundPort) {
    char * port = "80";
    strcpy(portNum, port);
  }

  return atoi(portNum);

}

void extractClientHeaders() {
  int i = 0;
  int length = 0;

  while (1) {
      length = rio_readlineb( &rio, tempLine, MAXLINE);
      if (length == 2 || length == 0) { // length is 2 if \r\n
        break;
      }

      if (strncmp(tempLine, "Host:", 4) && strncmp(tempLine, "Conn", 4) && strncmp(tempLine,      "Proxy", 4) && strncmp(tempLine, "User", 4)) {
        strcat(clientHeaders, tempLine); 
      }
      memset(tempLine, 0, MAXLINE);
      i++;
    }
}


void listenForRequests(int listenfd) {
  struct sockaddr_in clientaddr;
  socklen_t clientlen;

  while (1) {
    clientlen = sizeof(clientaddr);
    clientfd = Accept(listenfd, (SA * ) & clientaddr, & clientlen);
    Rio_readinitb(&rio, clientfd);
    Rio_readlineb( & rio, buf, MAXLINE);
    printf("\n\nRecieved a request\n%s\n", buf);
    sscanf(buf, "%s %s", method, uri);

    if (strcmp("GET", method)) {
      printf("Only get supported\n");
      close(clientfd);
      continue;
    }

    sscanf(firstLine, "%s", buf);

    //printf("METHOD %s\n URI %s\n, VERSION %s\n", method,uri,version);
    extractClientHeaders();
    serverPortNumber = parse_uri(uri);

    //printf("Filename: %s, portNum: %d\n, host: %s\n", fileName, serverPortNumber, hostName);
    serveRequest();

  }

}



int main(int argc, char ** argv) {
  portNumber = (argv[1]);
  if(argc >= 3 || argc<=1) {
     printf("Invalid arguments\n");
     exit(-1);
   }
  int listenfd = open_listenfd(portNumber); //11.4.7
  if(listenfd <0) {
    printf("Invalid port");
  }
  listenForRequests(listenfd);
  printf("should not get here.\n");

}
