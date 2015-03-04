/*
 * Andrew Downs
 * a2890722
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "ListString.h"

//Creates a new node with the data field set to the passed argument.
node *createNode(char* str)
{
    node *temp = NULL;
    temp = malloc(sizeof(node));

    temp->data = (char) str;
    temp->next = NULL;

    return temp;
}

//Inserts a new node into the end of the passed through list with the specified
//string argument as its data
node *insertNode(node *head, char* data)
{
	node *temp;

	if (head == NULL)
		return createNode(data);

	for (temp = head; temp->next != NULL; temp = temp->next);

	temp->next = createNode(data);
	return head;
}

//Converts the string passed through into a linked list and returns the head of the list
node *stringToList(char *str)
{
    int i;
    node* head = NULL;

    //Goes through the string's individual indices and inserts
    //them into a linked list.
    for(i = 0; i < strlen(str); i++)
        head = insertNode(head, (char*) str[i]);

    return head;
}

//Removes any nodes that do not have any data in them from the linked list.
node* cleanUpList(node* head)
{
    node* current = NULL;
    node* previous = NULL;

    //Cycles through each node in the list and removes them if their data is NULL.
    for(current = head; current->next != NULL; previous = current, current = current->next)
    {
        if(current->data == NULL)
        {
            //If previous is NULL, the head of the list has no data,
            //set the head of the list over by one.
            if(previous == NULL)
            {
                head = current->next;
            }
            //If current->next is not null and previous is not null, we are
            //somewhere in the middle of the list. Set previous->next to current->next
            //to have the list skip over this node.
            else if(current->next != NULL)
            {
                previous->next = current->next;
            }
            //If we reach here, we are at the end of the list.
            //Set previous->next to NULL.
            else
            {
                free(current);
                previous->next = NULL;
            }
        }
    }

    return head;
}

//Replaces all instance of key with the specified string.
//If NULL is passed through as the string, remove all instances of key.
node *replaceChar(node *head, char key, char *str)
{
    node* temp = NULL;
    node* newNode = NULL;
    int i;

    //Cycle through all nodes
    for(temp = head; temp != NULL; temp = temp->next)
    {
        //Check if the key matches the data.
        if (temp->data == key)
        {
            //If string is NULL, remove the data in the node
            if (str == NULL)
            {
                free(temp->data);
                temp->data = NULL;
            }
            //If the string is only one character, set the data in the node to that character
            else if (strlen(str) == 1)
            {
               temp->data = str[0];
            }
            //Otherwise, we will need to insert new nodes to make room for the string
            else
            {
                //Repeats for however long the string is.
                for(i = 0; i < strlen(str); i++)
                {
                    //Set the data field of the node
                    temp->data = str[i];

                    //If we're at the end of the list, create a new
                    //node and advance forward in the list.
                    if(temp->next == NULL)
                    {
                        temp->next = createNode(NULL);
                        temp = temp->next;
                    }
                    else

                    //Otherwise, insert a new node and set list pointers accordingly.
                    if(i != strlen(str) - 1)
                    {
                        newNode = createNode(NULL);
                        newNode->next = temp->next;
                        temp->next = newNode;
                        temp = temp->next;
                    }
                }
            }


        }
    }

    //If string was null and we removed data, there may be empty nodes.
    //Run cleanUpList to remove these nodes.
    if(str == NULL)
        head = cleanUpList(head);

    return head;
}

//Reverses the passed through list.
node *reverseList(node *head)
{
    node* copy = NULL;
    node* next;

   while(head != NULL)
   {
       next = head->next;
       head->next = copy;
       copy = head;
       head = next;
   }
    return copy;
}

//Prints the linked list
void printList(node *head)
{

    node* copy = head;  //Pointer to preserve the original head of the list.
    int wasPrinted = 0; //Boolean to check that something was printed

    if (copy == NULL)
	{
		printf("(empty string)\n");
		return;
	}

    while(head)
    {
        //Makes sure that only valid data will be printed
        if(head->data != NULL && head->data != ' ')
        {
            printf("%c", head->data);
            wasPrinted = 1;
        }
        head = head->next;
    }

    //In the event nothing was printed, print default message.
    if(wasPrinted == 0)
        printf("(empty string)\n");

    printf("\n");

    //reset the head of the list to its original position
    head = copy;
    return;
}

int main(int argc, char **argv)
{
    FILE* ifp;

    char buffer[1024];
    char* string;

    char code[2];
    char key[2];
    char* strKey = malloc(sizeof(char) * 1025);

    node* head;

    int i;

    ifp = fopen(argv[1], "r");

    fscanf(ifp, "%s ", buffer);
    string = malloc(sizeof(char) * (strlen(buffer) + 1));
    strcpy(string, buffer);

    head = stringToList(string);

    //Repeat until the end of the file is reached.
    while (!feof(ifp))
    {
        //Read in the code.
        fscanf(ifp, "%s ", code);

        //Switch case to determine what should be done based on the code read in.
        switch(code[0])
        {
            //Replace instances of key with a string.
            case '@':
                fscanf(ifp, "%s %s ", key, buffer);
                strcpy(strKey, buffer);
                head = replaceChar(head, key[0], strKey);
                break;

            //Reverse the list.
            case '~':
                head = reverseList(head);
                break;

            //Remove all instances of key from the list.
            case '-':
                fscanf(ifp, "%s ", key);
                head = replaceChar(head, key[0], NULL);
                break;

            //Print the list.
            case '!':
                printList(head);
                break;

            default:
                break;
        }
    }

    //Close the file.
    fclose(ifp);
    return 0;
}


