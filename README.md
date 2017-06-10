# What is JDolly?
JDOLLY is a Java program generator that exhaustively generates programs, up to a given scope. The Alloy specification language is employed as the formal infrastructure for generating programs; a metamodel for Java is encoded in Alloy, and the Alloy Analyzer finds solutions, which are translated into programs by JDOLLY, for user-specified constraints. JDolly can be used to automate the test input generation of systems that uses programs as inputs, such as refactoring engines.

# JDolly in Researches and Articles

JDolly is used in some Researches and Articles related to refactoring and transformation in Java Programs. Take a look at some of them below.

### Automated Behavioral Testing Of Refactoring Engines: [Link to the article](http://ieeexplore.ieee.org/document/6175911/?reload=true)
In this article, we face the problem that proving refactoring correctness (i.e. the refactoring don't change the behavior of the program) for the entire language is a challenge and the hardness of define the preconditions to ensure behavior preservation of the refactoring. Consequently, the refactorings are commonly implemented in ad hoc manner. 

In order to improve the confidence in the behavior preservation of certain transformation, the authors propose a technique using SafeRefactor ([Link to the article of this tool](http://ieeexplore.ieee.org/document/6062127/)) as a oracle to evaluate the correctness of the transformation and JDolly as the input (i.e. java programs) generator to test Java Refactoring Engines.

Curious about the results of combinating the both tools? See the image below.
![JDolly and Saferefactor results](https://raw.githubusercontent.com/gustavoasoares/jdolly/master/jdolly01.png)

