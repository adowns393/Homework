#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#include "Fibonacci.h"

/*
*   This function will add the two HugeIntegers passed through to it
*   and return a pointer to a new HugeInteger that contains the result.
*
*   TERMINATING CONDTIONS:
*   Either argument is NULL:        return NULL
*   Any mem allocation calls fail:  return NULL
*/
HugeInteger *hugeAdd(HugeInteger *p, HugeInteger *q)
{
    HugeInteger* ptr = NULL;
    int i;
    int length;

    //Check for terminating conditions
    if (p == NULL || q == NULL)
        return NULL;

    //Set the length variable to longest hugeInt passed through
    length = (p->length > q->length) ? p->length : q->length;

    //Allocate space for a new HugeInt that will store the result,
	//initialize its attributes accordingly and check if the allocation failed
    ptr = calloc(1, sizeof(HugeInteger));
    if(ptr == NULL)
        return NULL;
    ptr->digits = calloc(length, sizeof(int));
	if(ptr->digits == NULL)
		return NULL;

    ptr->length = length;

    //This for loop adds together the digits of the two passed through hugeInts.
    //If the result of the addition is greater than 10, carry the one to the next digit.
    //If i is ever greater than the length of one of the two ints, directly add
    //the value of the remaining int into the next digit. If at the last digit the
    //sum is greater than 10, ptr->digits will be realloced to make room.
    for(i = 0; i < length; i++)
    {
        if (i <= p->length - 1 && i <= q->length - 1)
        {
            ptr->digits[i] += p->digits[i] + q->digits[i];

            if (ptr->digits[i] >= 10 && i + 1 > ptr->length - 1)
            {
                ptr->digits = realloc(ptr->digits, sizeof(int) * ++ptr->length);
                if(ptr->digits == NULL)
                    return NULL;
                ptr->digits[i+1] = 1;
                ptr->digits[i] %= 10;
            }
            else if(ptr->digits[i] >= 10)
            {
                ptr->digits[i+1] += 1;
                ptr->digits[i] %= 10;
            }
            else

            continue;
        }
        else if (i > p->length - 1)
        {
            ptr->digits[i] += q->digits[i];
            continue;
        }
        else
        {
            ptr->digits[i] += p->digits[i];
            continue;
        }
    }

    return ptr;
}

/*
*   This function frees any memory in use by the HugeInteger passed through it
*   and returns NULL
*/
HugeInteger *hugeDestroyer(HugeInteger *p)
{
    if(p == NULL)
        return NULL;

    free(p->digits);
    free(p);
    return NULL;
}

/*
*   This function takes a string argument consisting of only ASCII digits 0 through 9
*   and creates a new HugeInteger pointer and returns it. It is assumed there are no
*   preceding 0's in the string.
*
*   TERMINATING CONDITIONS:
*   str is NULL:                    return NULL
*   Any mem allocation call fails:  return NULL
*/
HugeInteger *parseString(char *str)
{
    HugeInteger* ptr = NULL;
    int i;
    int n;

    if (str == NULL)
        return NULL;

    ptr = calloc(1, sizeof(HugeInteger));
	if (ptr == NULL)
		return NULL;
    ptr->digits = calloc(strlen(str), sizeof(int));
	if(ptr->digits == NULL)
		return NULL;

	//Copies the indexed character into the digits index.
	//'0' is subtracted from the character to convert
	//into the correct value.
    for(i = strlen(str), n = 0; i > 0; i--, n++)
        ptr->digits[i-1] = str[n] - '0';

    ptr->length = strlen(str);

    return ptr;
}

/*
*   This function converts the unsigned int passed through into a HugeInteger and returns
*   its pointer.
*
*   TERMINATING CONDITIONS:
*   Any mem allocation call fails:   return NULL
*/
HugeInteger *parseInt(unsigned int n)
{
    HugeInteger* ptr;
    int size = 0;
    int i = 0;
    unsigned int copy = n;

	//If n is 0, just return a HugeInteger with 1 digit of value 0.
    if(n == 0)
    {
        ptr = calloc(1, sizeof(HugeInteger));
		if(ptr == NULL)
			return NULL;
        ptr->digits = calloc(1, sizeof(int));
		if(ptr->digits == NULL)
			return NULL;

        ptr->length = 1;
        ptr->digits[0] = 0;
        return ptr;
    }

	//This loop determines how many digits are in n.
    while(copy > 0)
    {
        copy /= 10;
        size++;
    }

    ptr = calloc(1, sizeof(HugeInteger));
	if(ptr == NULL)
		return NULL;
    ptr->digits = calloc(size, sizeof(int));
	if(ptr->digits == NULL)
		return NULL;

    ptr->length = size;

    //This loop copies the remainder of n after dividing by 10 into
    //the HugeInteger's digits, then divides by 10 and truncates,
	//and increments the index by 1.
    while (n > 0)
    {
        ptr->digits[i] = n % 10;
        n /= 10;
        i++;
    }

    return ptr;
}

/*  This function compares the size of the HugeIntegers a to b.
*   It returns 1 if a is greater, 0 if b is greater, or 0 if they are equal.
*/
int compareHugeIntegers(HugeInteger *a, HugeInteger *b)
{
    int i;

    //Check if there are more digits in either hugeInteger and return if one is bigger
    if(a->length > b->length)
        return 1;
    else if(b->length > a->length)
        return 0;
    else

    //If their lengths are equal, compare the values of the individual
    //digits starting with the last digit in the numbers. If they are not equal
    //the integer with the greater digit will be the larger one.
    for(i = a->length - 1; i > 0; i--)
    {
        if(a->digits[i] != b->digits[i])
            return (a->digits[i] > b->digits[i]) ? 1 : 0;
    }

    //If there was no return during the for loop then the Integers are equal.
    return 0;
}

/*
*	Power function to use in place of the C library pow() function.
*/
int power(int b, int x)
{
    if(x == 0)
		return 1;
	if(x == 1)
		return b;

	return b * power(b, x - 1);
}

/*
*   This function converts the passed through HugeInteger into an unsigned int and returns
*   the new unsigned int's pointer
*
*   TERMINATING CONDITIONS:
*   p does not fit in an unsigned int:   return NULL
*   p is NULL:                           return NULL
*/
unsigned int *toUnsignedInt(HugeInteger *p)
{
    unsigned int* ptr = NULL;
    int i;
    HugeInteger* intLimit = NULL;

    if (p == NULL)
        return NULL;

    //Create a HugeInteger the size of the system's value for a max unsigned int
    intLimit = parseInt(UINT_MAX);

    if(intLimit == NULL)
        return NULL;

    //Check if p is greater than the system's max unsigned int value
    if (compareHugeIntegers(p, intLimit))
        return NULL;

    //Allocate memory for the return unsigned int and check if it worked
    ptr = calloc(1, sizeof(unsigned int));
    if (ptr == NULL)
        return NULL;

    //Add the HugeInteger's digits into the unsigned int, multiplying by 10 as necessary
    for(i = 0; i <= p->length - 1; i++)
    {
        *ptr += p->digits[i] * power(10, i);
    }

    return ptr;
}

/*
* This function calculates the value of Fib(n) and returns
* its value in a HugeInteger. It is assumed n will be a non-negative integer.
*/
HugeInteger *fib(int n)
{
    HugeInteger* a = NULL;
    HugeInteger* a_1 = NULL;
    HugeInteger* a_2 = NULL;
    int i;

    //a is fib(n)
    //a_1 is fib(n - 1)
    //a_2 is fib(n - 2)

    a_1 = parseInt(1);
    a_2 = parseInt(0);

    if(n == 0)
        return a_2;
    if(n == 1)
        return a_1;

    for (i = 0; i < n - 1; i++)
    {
		free(a);
        a = hugeAdd(a_1, a_2);

        //We don't need to remember what a_2 was so we free it.
		free(a_2->digits);

        //The digits pointer in a_2 will now point to the digits of a_1
        //and the digits of a_1 will now point to the digits of a.
        a_2->digits = a_1->digits;
        a_2->length = a_1->length;

        a_1->digits = a->digits;
        a_1->length = a->length;
    }

    return a;
}

