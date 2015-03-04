#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ArrayList.h"

#define DEFAULT_INIT_LEN 10


/*
* This function creates an empty ArrayList of size Length.
* If Length is less than DEFAULT_INIT_LEN, an ArrayList of DEFAULT_INIT_LEN will be made.
* All array pointers will be initialized to null and the ArrayList size will be set to 0
* and the capacity to either Length or DEFUALT_INIT_LEN. A printf statement will occur if
* the creation of a new ArrayList was successful or if anything went wrong, a NULL pointer is returned.
*
* TERMINATING CONDITIONS:
*   Any malloc calls failed:    return NULL
*/
ArrayList *createArrayList(int Length)
{
    int i;
    ArrayList* list = NULL;

    //Check if Length is greater than default size.
    if (Length > DEFAULT_INIT_LEN)
    {
        //Allocate memory for the ArrayList and the list's array.
        list = malloc(sizeof(ArrayList));
        list->array = malloc(sizeof(char*) * Length);

        if (list == NULL || list->array == NULL)
            return NULL;

        //Assign NULL to the list's arrays.
        for (i = 0; i < Length; i++)
            list->array[i] = NULL;

        //Update the size and capacity fields of the list.
        list->size = 0;
        list->capacity = Length;

        printf("-> Created new ArrayList of size %d.\n", Length);
        return list;
    }

    //Allocate memory for the ArrayList and the list's array.
    list = malloc(sizeof(ArrayList));
    list->array = malloc(sizeof(char*) * (DEFAULT_INIT_LEN));

    if (list == NULL || list->array == NULL)
            return NULL;

    //Initialize pointers to NULL.
    for (i = 0; i < DEFAULT_INIT_LEN; i++)
            list->array[i] = NULL;

    list->size = 0;
    list->capacity = DEFAULT_INIT_LEN;

    printf("-> Created new ArrayList of size %d.\n", DEFAULT_INIT_LEN);
    return list;
}

/*
* This function will free any memory in use by the ArrayList passed through it.
* If the ArrayList passed through it was NULL, NULL is returned.
*
* TERMINATING CONDITIONS:
*   list is NULL:   return NULL
*/
ArrayList *destroyArrayList(ArrayList *list)
{
    int i;

    if (list == NULL)
        return NULL;

    //Goes through the ArrayList and frees pointers in the array that are not NULL
    for (i= 0; i < (list->capacity); i++)
        if(list->array[i] != NULL)
            free(list->array[i]);

    free(list->array);
    free(list);

    return NULL;
}

/*
* This function expands the ArrayList passed through it to the specified size.
* This is achieved by creating a new char** array and copying the pointers
* from the original array to the new one, and replacing the old array in the list.
* Additionally, the old array is destroyed, the capacity of the list is updated,
* and a printf message is made.
*
* TERMINATING CONDITIONS:
*   list is NULL:                               return NULL
*   Length is less than the current capacity:   return NULL
*   Any malloc calls failed:                    return NULL
*/
ArrayList *expandArrayList(ArrayList *list, int Length)
{
    int i;
    char** newArray = NULL;

    if (list == NULL || Length <= list->capacity)
        return;

    newArray = malloc(sizeof(char**) * Length);

    if (newArray == NULL)
        return NULL;

    //Initialize all new pointers to NULL.
    for(i = 0; i < Length; i++)
    {
        newArray[i] = NULL;
    }

    //Assign the old pointers to the new array.
    for (i = 0; i < list->size; i++)
    {
        newArray[i] = list->array[i];
    }

    list->capacity = Length;

    //Destroy the old array.
    free(list->array);
    list->array = newArray;

    printf("-> Expanded ArrayList to size %d.\n", list->capacity);
    return list;
}

/*
* This function removes any empty spaces in the array. This is done by
* creating a new char** array of the appropriate size and copying the old list's
* array into the new one, similar to expandArrayList. The old array is destroyed,
* the list's capacity is updated, and a printf message is made upon completion.
*
* TERMINATING CONDITIONS:
*   list is NULL:               return NULL
*   Any malloc calls failed:    return NULL
*/
ArrayList *trimArrayList(ArrayList *list)
{
    int i;
    char** newArray;

    if (list == NULL)
        return NULL;

    //Check if the list has more spaces than items
    if (list->capacity > list->size)
    {
        newArray = malloc(sizeof(char**) * list->size);

        if (newArray == NULL)
            return NULL;


        //Assign the old list's pointers to the new one
        for (i = 0; i < list->size; i++)
        {
            newArray[i] = list->array[i];
        }

        //Destroy the old array
        free(list->array);
        list->array = newArray;
        list->capacity = list->size;

        printf("-> Trimmed ArrayList to size %d.\n", list->size);
        return list;
    }
    return list;
}

/*
* This function copies str into the first empty slot in the ArrayList and increments the list's size by one.
* If the ArrayList does not have sufficient space, expandArrayList is called to make space.
*
* TERMINATING CONDITIONS:
*   list is NULL:               return NULL
*   str is NULL:                return NULL
*   Any malloc calls failed:    return NULL
*/
char *put(ArrayList *list, char *str)
{
    int i, length;

    if (list == NULL || str == NULL)
        return NULL;

    //Expand the list if there is no room.
    if (list->size >= list->capacity)
        list = expandArrayList(list, list->capacity * 2 + 1);

    length = strlen(str);

    //Look for an empty slot in the array.
    //When a spot is found, copy the string into the slot and break out of the loop.
    for (i = list->size; i < list->capacity; i++)
    {
       if (list->array[i] == NULL)
        {
            list->array[i] = malloc(sizeof(char) * (length + 1));

            if(list->array[i] == NULL)
                return NULL;

            strcpy(list->array[i], str);
            break;
        }
    }

    list->size++;
    return list->array[i];
}

/*
* This function returns the pointer at the specified index in the list's array.
*
* TERMINATING CONDITIONS:
*   list is NULL:               return NULL
*   index is out of bounds:     return NULL
*/
char *get(ArrayList *list, int index)
{
    if (list == NULL || index - 1 >= list->size || index < 0)
        return NULL;

    return list->array[index];
}

/*
* This function overwrites the contents of the specified index with a copy of str.
*
* TERMINATING CONDITIONS:
*   list is NULL:               return NULL
*   index is out of bounds:     return NULL
*   list->array[index] is NULL: return NULL
*   Any malloc calls failed:    return NULL
*/
char *set(ArrayList *list, int index, char *str)
{
    int length;

    if (list == NULL || index > list->capacity || list->array[index] == NULL || index < 0)
        return NULL;

    length = strlen(str);

    //Free the specified index slot, allocate the appropriate
    //amount of memory, and copy str into it.
    free(list->array[index]);
    list->array[index] = malloc(sizeof(char) * (length + 1));

    if(list->array[index] == NULL)
        return NULL;

    strcpy(list->array[index], str);

    return list->array[index];
}

/*
* This function inserts a copy of str into the specified index and moves all other
* pointers in the array over by one to make room. Also increments the list's size by one.
*
* TERMINATING CONDITIONS:
*   list is NULL:                       return NULL
*   str is NULL:                        return NULL
*   index is out of bounds:             return NULL
*   Any malloc calls failed:            return NULL
*   index is beyond current list size:  call put(), return list
*/
char *insertElement(ArrayList *list, int index, char *str)
{

    int i, length;

    if (list == NULL || str == NULL || index < 0)
        return NULL;

    //Expand the list if there is no room
    if (list->size == list->capacity)
        list = expandArrayList(list, list->capacity * 2 + 1);

    if (index + 1 > list->size)
    {
        put(list, str);
        return list->array[index];
    }

    //Any pointers after the selected index are moved
    //down the list by one.
    for (i = list->size; i > index; i--)
    {
        length = strlen(list->array[i-1]);
        free(list->array[i]);
        list->array[i] = malloc(sizeof(char) * (length + 1));

        if (list->array[i] == NULL)
            return NULL;

        strcpy(list->array[i], list->array[i-1]);
    }

    //Copies str into the slot at the specified index
    free(list->array[index]);
    list->array[index] = malloc(sizeof(char) * (length + 1));

    if (list->array[index] == NULL)
            return NULL;

    strcpy(list->array[index], str);

    list->size++;

    return list->array[index];
}

/*
* This function removes the pointer at the specified index
* and moves all pointers after the index up by one so there
* is are no empty spaces. Also decrements the list's size by one.
*
* TERMINATING CONDITIONS:
*   list is NULL:               return 0
*   index is out of bounds:     return 0
*   Any malloc calls failed:    return 0;
*/
int removeElement(ArrayList *list, int index)
{
    int i, length;
    char** newArray = NULL;

    if (list == NULL || index + 1 > list->size || index < 0)
        return 0;

    newArray = malloc(sizeof(char**) * list->capacity);

    if (newArray == NULL)
        return 0;

    //Initiate all new pointers to NULL
    for (i = 0; i < list->capacity; i++)
        newArray[i] = NULL;

    //Copies all pointers before index into the new array
    for (i = 0; i < index; i++)
    {
        length = strlen(list->array[i]);
        newArray[i] = malloc(sizeof(char) * (length + 1));

        if (newArray[i] == NULL)
            return 0;

        strcpy(newArray[i], list->array[i]);
    }

    //Copies all pointers after index into newArray[i-1]
    for (i = index; i < list->size - 1; i++)
    {
        length = strlen(list->array[i+1]);
        newArray[i] = malloc(sizeof(char) * (length + 1));

        if (newArray[i] == NULL)
            return 0;

        strcpy(newArray[i], list->array[i+1]);
    }

    //Destroy the old array and point the list to the new one
    free(list->array);
    list->array = newArray;

    list->size--;
    return 1;
}


/*
* This function returns the size of the list.
*
* TERMINATING CONDITIONS:
*   list is NULL:   return -1
*/
int getSize(ArrayList *list)
{
    return (list == NULL) ? -1 : list->size;
}

/*
* This function prints all current items in the list with new line character.
*
* TERMINATING CONDITIONS:
*   list is NULL:       print empty list, return
*   list->size is 0:    print empty list, return
*/
void printArrayList(ArrayList *list)
{
    int i;

    if (list == NULL || list->size == 0)
    {
        printf("(empty list)\n");
        return;
    }


    for (i = 0; i < list->size; i++)
        printf("%s\n", list->array[i]);

    return;
}
