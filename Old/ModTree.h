#include <iostream>
#include <iomanip>
#include <fstream>
#include <istream>
#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <NTL/ZZ.h>
using namespace std;
using namespace NTL;


//////////////////////////////////////////
// Helpers
//////////////////////////////////////////

// String <-> ZZ converters
string ZZtos(ZZ n)
{
   stringstream stringer;
   string asString;
   stringer << n;
   stringer >> asString;
   return asString;
}
ZZ stoZZ(string str)
{
   stringstream stringer;
   ZZ asZZ;
   stringer << str;
   stringer >> asZZ;
   return asZZ;
}


/*********************************************
int getNNInt( prompt, error message)
   - get non-negative integer from user
**********************************************/
int getNNInt(string prompt, string err = "Input a valid non-negative integer.")
{
   int num = 0;

   cout << prompt;
   cin >> num;

   while (cin.fail() || num < 0)
   {
      cout << err << endl;
      if (cin.fail())
      {
         cin.clear ();
         cin.ignore (80, '\n');
      }
      cout << prompt;
      cin >> num;
   }

   cin.ignore(80,'\n');  // remove left over newline character from input buffer;   
   return num;
}

/*********************************************
int getPosInt( prompt, error message)
   - get positive integer from user
**********************************************/
int getPosInt(string prompt, string err = "Input a valid positive integer.")
{
   int num = 0;

   cout << prompt;
   cin >> num;
   while (cin.fail() || num < 1)
   {
      cout << err << endl;
      if (cin.fail())
      {
         cin.clear ();
         cin.ignore (80, '\n');
      }
      cout << prompt;
      cin >> num;
   }
   cin.ignore(80,'\n');  // remove left over newline character from input buffer;   

   return num;
}


/*************************************************
int gcd( a, b )
   - finds the greatest common divisor, obviously
   (needed to test relative primality)
**************************************************/
int gcd(int a, int b)
{
   if (b == 0)
      return a;
   else
      return gcd(b, a % b);
}

/***********************************************
bool relprime( vector<int> )
   - return whether the input vector of numbers
     are all relatively prime to one another
************************************************/
bool relPrime(vector<int>& vect)
{
   for (int i = 0; i < vect.size() - 1; i++)
   {
      for (int j = i+1; j < vect.size(); j++)
      {
         if (gcd(vect[i],vect[j]) != 1)
            return false;
      }
   }
   return true;
}




//////////////////////////////////////////
// The Modulus Tree Class (copypasta)
//////////////////////////////////////////



/*
   Modulus Tree Class (DIMINISHED prime modulus tree)
   - reads a series of relatively-prime moduli from a file, and 
   uses them to build a FINITE tree

   - contains methods to traverse the tree and get info about it
   
*/
class ModulusTree
{
public:
   ModulusTree(string file);   //read moduli from file (\n delimited)
   ModulusTree(int* values);

   int getMod(int n);   //"getPrime(n)", gets nth modulus 
   ZZ getModorial(int n);   //"getPrimorial(n)", gets nth mod product

   ZZ getLevelMax(int lvl) { return getModorial(lvl);} //Alias... what's the biggest on this level?

   int getLocalIndex(ZZ n, int lvl) { return n % getMod(lvl); }   //i = n % mod
   int getLocalIndex(int n, int lvl) { return n % getMod(lvl); }   //i = n % mod

   ZZ getLevelIndex(ZZ n, int lvl);
   ZZ getLevelIndex(int n, int lvl) { return getLevelIndex(to_ZZ(n), lvl); }

   ZZ getByLevelIndex(ZZ index, int lvl);
   ZZ getByLevelIndex(int index, int lvl) { return getByLevelIndex(to_ZZ(index), lvl); }

   ZZ getParent(ZZ n, int lvl); 
   ZZ getParent(int n, int lvl) { return getParent(to_ZZ(n), lvl); }

   ZZ* getChildren(ZZ n, int lvl);
   ZZ* getChildren(int n, int lvl) { return getChildren(to_ZZ(n), lvl); }

   int getHeight() { return height; }
   
private:
   int* moduli;
   ZZ* modorials;
   int height;
};

/*************************************************
ModulusTree(string file)
   - tree "constructor" (only constructs the object)
   - takes numbers from a file
   - not used in this program, but here for utility
**************************************************/
// ModulusTree::ModulusTree(string file)
// {
// 
//    ifstream inFile;
//    inFile.open(file);
//       if (inFile.fail())
//       {
//          cout << "open file error. " << endl;
//          while (true);
//          exit(1);
//       }
// 
//       vector<int> tempModuli;
//       int number = 0;
//       while (inFile >> number)
//          tempModuli.push_back(number);
// 
//       height = tempModuli.size();
//       moduli = new int[height];
//       modorials = new ZZ[height];
//       ZZ currentModorial = to_ZZ(1);
//       //store the primes and primorials
//       for (int i = 0; i < height; i++)
//       {
//          moduli[i] = tempModuli[i];
//          currentModorial *= tempModuli[i];
//          modorials[i] = currentModorial;
//       }
//    inFile.close();
// }

/*************************************************
ModulusTree(int* values)
   - tree "constructor" (only constructs the object)
   - takes numbers from an integer array, where:
     - the first number is the height of the tree
     - the rest are the relatively-prime list
**************************************************/
ModulusTree::ModulusTree(int* values)
{
   height = values[0];
   moduli = new int[height];
   modorials = new ZZ[height];
   ZZ currentModorial = to_ZZ(1);
   //store the primes and primorials
   for (int i = 0; i < height; i++)
   {
      moduli[i] = values[i + 1];
      currentModorial *= values[i + 1];
      modorials[i] = currentModorial;
   }
}


/*************************************************
int getMod(int n)
   - if it were the PMT, this'd be getPrime(n)
   - returns the nth relative-prime number
**************************************************/
int ModulusTree::getMod(int n)
{
   if (n < 0)
      return -1;   //bad input
   if (n == 0)
      return 1;   //1 for "0th prime"

   if (n < height)
      return moduli[n - 1];   //return nth pseudoprime (of the "key" prime list)
   else
      return moduli[height - 1];   //refuse to go further than the last tree level
}

/*************************************************
ZZ getModorial(int n)
   - if this were the PMT, this'd be getPrimorial(n)
   - returns the product of the first n relative-prime numbers
**************************************************/
ZZ ModulusTree::getModorial(int n)
{
   if (n < 0)
      return to_ZZ(-1);   //bad input
   if (n == 0)
      return to_ZZ(1);   // 1 i the 0th modorial/primorial
   if (n > height)
      n = height;   //demand the last one - you can't go past the leaves!

   return modorials[n - 1];
}


/*************************************************
ZZ getLevelIndex(ZZ n, int lvl)
   - gets level index from left to right of n
**************************************************/
ZZ ModulusTree::getLevelIndex(ZZ n, int lvl)
{
   if (n == 1)
      return to_ZZ(0);
   
   ZZ prod = to_ZZ(1);
   ZZ sum = to_ZZ(0);
   ZZ index;

   index = getLocalIndex(n, lvl);
   sum += index;

   while (lvl > 0)
   {
      prod *= getMod(lvl);
      lvl--;
      index = getLocalIndex(n, lvl);
      sum += (prod * index);
   }  
   return sum;
}


//   gets parent of n
ZZ ModulusTree::getParent(ZZ n, int lvl)
{
   if (lvl == 0)
        return to_ZZ(-1);
   if (lvl == 1)
      return to_ZZ(1);
     
   ZZ prevModorial = getModorial(lvl - 1);
   ZZ modorial = getModorial(lvl);
   int mod = getMod(lvl);
   ZZ a = n;
   ZZ b;
   
   while (true)   
   {   
      // continue looping through children until you find the smallest
      // * the first time you wrap around, that sibling == the parent
      b = (a + prevModorial) % modorial;
      if (b < a)
         return b;
      a = b;
   }

}

//   gets array of n's children
ZZ* ModulusTree::getChildren(ZZ n, int lvl)
{
   int modulus = getMod(lvl + 1);   // get the next pseudoprime
   ZZ* outArray = new ZZ[modulus];   // (which is the child count)
   ZZ modorial = getModorial(lvl);
   ZZ nextModorial = getModorial(lvl + 1);
   for(int i = 0; i < modulus; i++)
   { 
      n += modorial;   // the children are their parent's modorial/primorial apart
      // mod by the current modorial for the diminished tree
      outArray[n % modulus] = n % nextModorial; 
      if (outArray[n % modulus] == 0)
         outArray[n % modulus] = nextModorial;
   } 
      
    return outArray;
}


/*************************************************
ZZ getByLevelIndex(ZZ index, int lvl)
   - gets n by level index from left to right
**************************************************/
ZZ ModulusTree::getByLevelIndex(ZZ index, int lvl)
{
   
   //error or easy-checking
   if (lvl == 0)
      return to_ZZ(1);

   if (index == to_ZZ(0))
      return getModorial(lvl);

   ZZ indexRange = getModorial(lvl);

   if (index >= indexRange)
   {
      cout << "\nERROR: Invalid index for level.\n";
        cout << "level " << lvl << "'s last index is [" << indexRange - 1 << "].\n";
        return to_ZZ(-1);
   }

   //---
   
   index += 1;
   int currentLevel = 0;
   ZZ divisor = getModorial(lvl);
    ZZ n = to_ZZ(1);
   int nextIndex = 0;

   while (currentLevel < lvl)
    {
      divisor = divisor / (getMod(currentLevel + 1));
      while (index > divisor)
      {
         index -= divisor;
         nextIndex++;
      }
      
      n = getChildren(n,currentLevel)[nextIndex];     
      nextIndex = 0;
      currentLevel++;
      
   }
   divisor = getModorial(lvl);
   return n % divisor;   //mod by the Modorial to get the diminished tree
}