#include <stddef.h>


#ifndef DATASTORE_DATASTORE_H
#define DATASTORE_DATASTORE_H

struct Car {
    long price;
    int uniqueId;
    char make[9];
    short year;
    char model[9];

};

int addCar(char* make, char* model, short year, long price, int uniqueID);
int setMaxMemory(size_t bytes);
struct Car* getCarById(int id);
struct Car* getAllCarsInMemory();

int deleteCarById(int id);
int modifyCarById(int id, struct Car* myCar);
int getNumberOfCarsInMemory();
int getAmountOfUsedMemory();
int getNumberOfCarsOnDisk();
struct Car* getAllCarsOnDisk();


#endif //DATASTORE_DATASTORE_H
