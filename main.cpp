#include <iostream>
#include <iomanip>
#include <fstream>
#include <istream>
#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <NTL/ZZ.h>
#include "ModTree.h"
using namespace std;
using namespace NTL;

/**************************************************
int main()
   - simply calls the gui class
**************************************************/
int main()
{
   vector<int> vect;
   vect.push_back(2);
   vect.push_back(3);
   vect.push_back(5);
   vect.push_back(7);
   vect.push_back(11);
   vect.push_back(13);
   vect.push_back(17);
   vect.push_back(19);
   vect.push_back(23);
   vect.push_back(31);
   vect.push_back(37);
   vect.push_back(41);
   vect.push_back(43);
   vect.push_back(47);
   vect.push_back(53);
   vect.push_back(59);
   vect.push_back(61);
   vect.push_back(67);
   vect.push_back(71);
   vect.push_back(73);
   vect.push_back(79);
   vect.push_back(83);
   vect.push_back(89);
   vect.push_back(97);
   vect.push_back(101);
   vect.push_back(103);
   vect.push_back(107);
   
   int* modList = new int[vect.size()+1];
   modList[0] = vect.size();
   for (int i = 0; i < vect.size(); i++)
      modList[i+1] = vect[i];
   
   ModulusTree* tree = new ModulusTree::ModulusTree(modList);
   
   
   char buffer[16];
    ifstream myFile ("data.txt", ios::in | ios::binary);
    myFile.read (buffer, 16);
    ZZ value = ZZFromBytes((const unsigned char *)buffer,16);
   ZZ encrypted = tree->getLevelIndex(value, 27);
   ZZ decrypted = tree->getByLevelIndex(encrypted, 27);
   
   cout << "Original: " << value << endl 
           << "Encrypted: " << encrypted << endl 
           << "Decrypted: " << decrypted << endl;
    
    char outBuffer[16];
    BytesFromZZ((unsigned char*) outBuffer, decrypted, 16);
    ofstream myFile2 ("decrypted.txt", ios::out | ios::binary);
    myFile2.write (outBuffer, 16);
   return 0;
}