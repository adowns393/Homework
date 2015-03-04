/*
 * Andrew Downs
 * a2890722
 * Assignment 4
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "Trie.h"

//A string linked list will be used to store one copy of each
//unique word in the corpus file. This list will be used to determine coocurrences.
typedef struct StringListNode
{
	char *data;
	struct StringListNode *next;
} StringListNode;

//Keeping track of only the head will be sufficient for the scope of this program.
typedef struct StringList
{
    StringListNode *head;
} StringList;

//Returns a newly created string list pointer.
StringList *CreateStringList()
{
    return calloc(1, sizeof(StringList));
}

//Returns a newly created node pointer with its data field initialized
//to the specified value.
StringListNode *createNode(char *str)
{
	StringListNode *ptr = malloc(sizeof(StringListNode));

	ptr->data = malloc(sizeof(char) * (strlen(str) + 1));
	strcpy(ptr->data, str);
	ptr->next = NULL;

	return ptr;
}

//Inserts a list node at the head of a list.
StringListNode *headInsert(StringListNode *head, char *data)
{
	StringListNode *temp;

	temp = createNode(data);
	temp->next = head;

	return temp;
}

//Frees all nodes in the word list.
StringListNode *destroyList(StringListNode *head)
{
	if (head == NULL)
		return NULL;

	destroyList(head->next);
	free(head);

	return NULL;
}


TrieNode *createTrieNode(void)
{
    return calloc(1, sizeof(TrieNode));
}

//Returns the index of a letter. e.g.: a = 0, b = 1, c = 2...
int getIndex(char letter)
{
    int i;
    char check = 'a';

    for (i = 0; i < 26; i++)
    {
        if(tolower(letter) == check)
            break;
        check++;
    }

    return i;
}

//Returns the node of a word in a trie.
TrieNode *getNode(TrieNode *root, char *str)
{
    TrieNode *temp;
    int i;

    if(root == NULL || str == NULL)
        return;

    temp = root;

    for(i = 0; i < strlen(str); i++)
    {
        if(temp->children[getIndex(str[i])] != NULL)
        {
            temp = temp->children[getIndex(str[i])];
        }
        else
        {
            return NULL;
        }
    }

    if(temp->count != 0)
        return temp;

    return NULL;
}

//Returns the word in a string list at the nth position in the list
char *getWord(StringList *list, int n)
{
    int i;
    StringListNode *temp;

    if(list == NULL)
        return NULL;

    temp = list->head;

    for(i = 0; i < n; i++)
        temp = temp->next;

    return temp->data;
}

// Helper function called by printTrie()
void printTrieHelper(TrieNode *root, char *buffer, int k)
{
	int i;

	if (root == NULL)
		return;

	if (root->count > 0)
		printf("%s (%d)\n", buffer, root->count);

	buffer[k + 1] = '\0';

	for (i = 0; i < 26; i++)
	{
		buffer[k] = 'a' + i;

		printTrieHelper(root->children[i], buffer, k + 1);
	}

	buffer[k] = '\0';
}

// If printing a subtrie, the second parameter should be 1; otherwise, 0.
void printTrie(TrieNode *root, int useSubtrieFormatting)
{
	char buffer[1026];

	if (useSubtrieFormatting)
	{
		strcpy(buffer, "- ");
		printTrieHelper(root, buffer, 2);
	}
	else
	{
		strcpy(buffer, "");
		printTrieHelper(root, buffer, 0);
	}
}


//Check if the target word co occurs with the subword. If it does, return the number of times it co occurs.
//Otherwise, return 0.
int getCoocurrence(char *fileName, char *subWord, char *targetWord)
{
	FILE *ifp;
	char buffer[1024];
	StringList *colist;
	int numWords = 0;
	int numOcurrances = 0;
	int subWordPresent = 0;
	int i = 0;

	if(fileName == NULL || subWord == NULL || targetWord == NULL)
		return 0;

    if(strcmp(subWord, targetWord) == 0)
        return 0;

	ifp = fopen(fileName, "r");


	while(!feof(ifp))
	{
	    subWordPresent = 0;
        numWords = 0;
	    colist = CreateStringList();

	    fscanf(ifp, "%s", buffer);

        //convert the string to lower case
	    for(i = 0; i < strlen(buffer); i++)
            buffer[i] = tolower(buffer[i]);

	    if(buffer[0] == '.')
            break;

	    //builds a list of words in a sentence
		while(buffer[0] != '.')
		{
			colist->head = headInsert(colist->head, buffer);

			if(buffer[0] != '.')
                numWords++;

            fscanf(ifp, "%s", buffer);

            for(i = 0; i < strlen(buffer); i++)
                buffer[i] = tolower(buffer[i]);
		}

        //cycle through the list of words and check if the subword is in the list
		for(i = 0; i < numWords; i++)
            if(strcmp(subWord, getWord(colist, i)) == 0)
            {
                subWordPresent = 1;
                break;
            }

        //if the subword is in the list, find the number of times the target
        //word is in the list with it.
        if(subWordPresent)
        {
            for(i = 0; i < numWords; i++)
            {

                if(strcmp(targetWord, getWord(colist, i)) == 0)
                    numOcurrances++;
            }

        }

        colist->head = destroyList(colist->head);
        free(colist);
	}


    fclose(ifp);

    return numOcurrances;
}

//Builds a trie based upon the corpus file.
TrieNode *buildTrie(char *filename)
{
    FILE *ifp;

    char buffer[1024];
	char *targetWord;
    char *subWord;

    int i;
	int j;
	int k;
	int numCoocurrences;

    int numWords = 0;
    int index;
    int subtrieIsPopulated;

    TrieNode *root;
    TrieNode *copy;
	TrieNode *subCopy;

    StringList *wordList;

    if(filename == NULL)
        return NULL;

    ifp = fopen(filename, "r");

    root = createTrieNode();
    copy = root;    //Preserve the head of the tree.

    //This list will contain one unique instance of every word that
    //occurs in the text file.
    wordList = CreateStringList();

    while(!feof(ifp))
    {
        fscanf(ifp, "%s", buffer);

        //convert the string to all lower case.
        for(i = 0; i < strlen(buffer); i++)
            buffer[i] = tolower(buffer[i]);

        if(buffer[0] != '.')
        {
            //Navigates from the head of the trie to the correct child.
            for(i = 0; i < strlen(buffer); i++)
            {
                index = getIndex(buffer[i]);

                if(root->children[index] == NULL)
                {
                    root->children[index] = createTrieNode();
                    root = root->children[index];
                }
                else

                root = root->children[index];
            }

            //If root count is zero, this word has not been seen before, add it
            //to the wordlist and increment the number of unique words.
            if(root->count == 0)
            {
                wordList->head = headInsert(wordList->head, buffer);
                numWords++;
            }

            root->count++;
            root = copy;    //Return to the root of the trie.
        }
    }

	//Cycle through each word in the list and create a subtrie for it.
    for(i = 0; i < numWords; i++)
    {
        root = getNode(root, getWord(wordList, i));
        subWord = getWord(wordList, i);

        root->subtrie = createTrieNode();
		root = root->subtrie;
		subtrieIsPopulated = 0;

		subCopy = root; //Preserve the head of this subtrie

		//Cycle through each word in the list and add it to the subtrie
		//of the current word if it co ocurrs with it.
		for(j = 0; j < numWords; j++)
		{
		    targetWord = getWord(wordList, j);

			//getCoocurrence returns the number of times the target word co occurs with the subword.
			//place this word in the subtrie however many times it co occurs.
			numCoocurrences = getCoocurrence(filename, subWord, targetWord);

			if (numCoocurrences > 0)
			{
				strcpy(buffer, targetWord);

				for(k = 0; k < strlen(buffer); k++)
           	 	{
           	 	    index = getIndex(buffer[k]);

                	if(root->children[index] == NULL)
                	{
                    	root->children[index] = createTrieNode();
                    	root = root->children[index];
                	}
                	else

               	 	root = root->children[index];
            	}

				root->count += numCoocurrences;

				//return to the head of the subtrie
				root = subCopy;
			}
		}

        //Check through each child of the subtrie to determine if any
        //strings were added to the subtrie.
		for(j = 0; j < 26; j++)
        {
            if(root->children[j] != NULL)
            {
                subtrieIsPopulated = 1;
                break;
            }
        }

        if(subtrieIsPopulated == 0)
        {
            root->count = -1;
        }

        //Return to the head of the trie.
        root = copy;
    }

    wordList->head = destroyList(wordList->head);
    free(wordList);
    fclose(ifp);

    //copy preserved the head of the entire tree, return it.
    return copy;
}








int main(int argc, char **argv)
{
    TrieNode *root;
    TrieNode *copy;

    char buffer[1024];

    FILE *ifp;

    root = buildTrie(argv[1]);
    copy = root;

    ifp = fopen(argv[2], "r");

    fscanf(ifp, "%s", buffer);

    while(!feof(ifp))
    {
        root = copy;

        //Print the entire trie
        if(buffer[0] == '!')
        {
            printTrie(root, 0);
            fscanf(ifp, "%s", buffer);
            continue;
        }

        //Navigate to the specified word's node.
        root = getNode(root, buffer);

        //Try and print that word's subtrie.
        if(root == NULL)
        {
            printf("%s\n(INVALID STRING)\n", buffer);
            fscanf(ifp, "%s", buffer);
            continue;
        }
        else if(root->subtrie->count == -1)
        {
            printf("%s\n(EMPTY)\n", buffer);
            fscanf(ifp, "%s", buffer);
            continue;
        }
        else
        {
            printf("%s\n", buffer);
            printTrie(root->subtrie, 1);
            fscanf(ifp, "%s", buffer);
        }
    }

    fclose(ifp);

    return 0;
}
