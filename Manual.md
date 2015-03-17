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