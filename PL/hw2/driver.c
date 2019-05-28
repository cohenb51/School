#include <stdio.h>
#include <stdlib.h>
#include <mem.h>
#include "dataStore.c"



static int printCarsOnDisk() {
    printf("\nPrinting all cars in Disk\n");
    struct Car* cars = getAllCarsOnDisk();

    for(int i=0; i< getNumberOfCarsOnDisk();  i++) {
        printf("Car %d\n", cars[i].uniqueId);

    }
    free(cars);
    printf("\n");

}
static int println() {
    printf("________________________________\n\n");

}

static int printCarsinMemory() {
    printf("\nPrinting All Cars In Memory\n");
    struct Car* cars = getAllCarsInMemory();
    for(int i=0; i< getNumberOfCarsInMemory();  i++) {
        printf("Car %d\n", cars[i].uniqueId);
    }
    //free(cars); // Freeing these WILL lead to a disaster, hopefully it will segfault.

}


int main() {

    char *filename = "cars1.txt";
    if (filename != NULL) {
        remove(filename);
    }


    printf("Adding 5 cars\n");
    addCar("A", "do", 19, 2000, 4);
    addCar("B", "blah", 19, 2000, 5);
    addCar("C", "blah", 19, 2000, 6);
    addCar("D", "blah", 19, 2000, 7);
    addCar("E", "blah", 19, 2000, 8);

    printCarsOnDisk();
    println();
    printf("Adding 64 bytes of memory\n");
    setMaxMemory(64);
    printCarsinMemory();
    printCarsOnDisk();
    println();
    printf("setting to 2048\n");
    setMaxMemory(2048);
    printCarsinMemory();
    printCarsOnDisk();
    println();
    printf("resetting back to 2^8 and evict LRU (most recently added to RAM)\n");
    setMaxMemory(64);
    printCarsinMemory();
    printCarsOnDisk();
    println();
    printf("getting car6 and moving car 6 into MRU spot \n");
    struct Car* car = getCarById(6);
    printf("Car model: %s\n", car->model);
    printf("Car make: %s\n", car->make);
    printf("Car price: %d\n", car->price);
    printf("Car year: %d\n", car->year);
    printf("Car id: %d\n", car->uniqueId);
    printCarsinMemory();
    printCarsOnDisk();
    println();
    printf("modifying car modifies it directly in the db\n");
    car->year = 2014;
    car = getCarById(6);
    printf("Car year: %d\n", car->year);
    printCarsinMemory();
    printCarsOnDisk();

    printf("Modifying car 4 by id\n");
    struct Car *car2 = malloc(sizeof(struct Car));
    strncpy(car2->make, "I don't", sizeof(car2->make));
    car2->make[sizeof(car2->make) - 1] = '\0';
    strncpy(car2->model, "know", sizeof(car2->model));
    car2->model[sizeof(car->model) - 1] = '\0';
    car2->year = 1993;
    car2->price = 123;
    car2->uniqueId = 4;

    modifyCarById(4, car2);
    car = getCarById(4);
    printf("Car price: %d\n", car->price);
    printf("Car make: %s\n", car->make);

    printCarsinMemory();
    println();
    println();

    printf("and deleteing car 4...");
    deleteCarById(4);

    printCarsinMemory();
    printCarsOnDisk();
    printf("And 5\n");
    deleteCarById(5);
    printCarsinMemory();
    printCarsOnDisk();
    printf("Memory using right now: %d\n", getAmountOfUsedMemory());
    printf("Cars in RAM: %d\n", getNumberOfCarsInMemory());
    printf("size of car: %d\n", sizeof(struct Car));
    printf("CARS ON DISK: %d\n", getNumberOfCarsOnDisk());



}