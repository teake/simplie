# About

SimpLie is an easy-to-use program for Lie algebra. It has been specifically written for level decompositions of infinite-dimensional Lie algebras, but it is not limited to that task. Its current features are:

*  Calculation of Lie algebra properties based on Dynkin diagrams:
   * Type
   * Dimension
   * Rank
   * (Dual) Coxeter labels and numbers.
   * Roots
* Calculation of highest weight representations.
* Level decompositions.
* Visualization of root systems by means of [Screenshots Coxeter projections and Hasse diagrams].

On the user end, it has the following benefits:

* A graphical user interface, making it easy to use.
* Cross-platform (written in Java).
* LaTeX & EPS export capability.
* Saving and loading of calculation results (root systems).

See https://github.com/teake/simplie/blob/wiki/Papers.md for a list of papers that have used SimpLie.

# Build instructions #

## Pre-compiled binaries ##

There are pre-compiled binaries for OS X and Windows for the [latest release](https://github.com/teake/simplie/releases/latest).

## Gradle ##

If you have [Gradle](http://www.gradle.org/), building and running SimpLie amounts to just checking out the source and running the `run` Gradle task:

```
git clone https://github.com/teake/simplie.git simplie
cd simplie
gradle run
```


## Netbeans ##

SimpLie can also be build with [NetBeans](https://netbeans.org/). If you want to edit the GUI panels you'll need to get NetBeans 7, as older version have dropped support for the Swing Application Framework which SimpLie uses.

You will also need to create three libraries upon which SimpLie relies, namely:

  * [BSAF](https://kenai.com/projects/bsaf/)
  * [Jama](http://math.nist.gov/javanumerics/jama/)
  * [EPSgraphics](http://math.nist.gov/javanumerics/jama/)
