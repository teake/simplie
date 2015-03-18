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


# Usage #

## Algebra Setup ##

Within the white Dynkin diagram panel of the Algebra Setup tab one can interactively 'click' a Dykin diagram together. The relevant commands are:

  * Left mouse-click: Add a node. Hold shift to automatically add a connection to the previous node.
  * Middle mouse-click or alt-left: Toggle a node.
  * Right mouse-click: Bring up context menu. From the context menu, you can:
    1. Add/remove a node if the mouse is on an empty location.
    1. Add/remove a connection if the mouse is over a node.
    1. Left click on a second node to finish the action.
    1. Toggle a node.
    1. Remove a node.

A decomposition is specified by disabling one or more nodes. The 'level-nodes' are black, the 'internal nodes' are orange, and the regular subalgebra nodes remain white.

By default, the subscripts of the nodes indicate the node ordering. Using the radio buttons on the right it is possible to display to (dual) Coxeter labels. If the Coxeter labels differ from their duals, the Coxeter label get displayed in bold below the dual.

The default ordering on of the nodes is from bottom to top. Using the radio buttons on the right it is possible to change it to from top to bottom.

## Algebra Info ##

This tab gives some basic information about the previously entered Lie algebra. You can select whether to view the information for the full algebra, the regular subalgebra, or for the interal algebra.

  * Matrices: Displays some of the relevant matrices for the algebra.
  * Roots: lists all the roots of the algebra.
  * Representations: Here it is possible to enter Dynkin labels for a representation and the computer calculate the dimension and all the weights.

## Level Decomposition ##

In this tab a level decomposition of the Dynkin diagram set up previously can be calculated. The columns in the subalgebra representations panel are the following:

  * l : Level(s).
  * p\_r : Dynkin labels of the regular subalgebra part.
  * p\_i : Dynkin labels of the internal part.
  * vector : Root vector.
  * a^2 : Root norm.
  * d\_r : Dimension of the regular subalgebra part.
  * d\_i : Dimension of the internal part.
  * mult : The root multiplicity.
  * mu : The outer multiplicity of the representation.
  * h : The height of lowest / highest weight.

## Visualization ##

In this tab any Lie algebra can be "visualized": either a Coxeter plane projection can be made, or a Hasse diagram can be drawn of the root system of the algebra.
When choosing the Coxeter plane projection, the full algebra gets projected w.r.t its regular subalgebra as specified in the Dynkin diagram.

# License #

SimpLie is distributed under the [GNU GPL V3 license](http://www.gnu.org/licenses/gpl.html).

