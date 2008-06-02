LICENSE
-------

SimpLie, a simple program for Lie algebras.
Copyright (C) 2007  Teake Nutma

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.


ALGEBRA SETUP
-------------

Within the white Dynkin diagram panel of the Algebra Setup tab one
can interactively 'click' a Dykin diagram together. The relevant
commands are:

    * Left mouse-click: Add a node. Hold shift to automatically add
      a connection to the previous node.
    * Middle mouse-click or alt-left: Toggle a node.
    * Right mouse-click: Bring up context menu. From the context menu,
      you can:
          o Add/remove a node if the mouse is on an empty location.
          o Add/remove a connection if the mouse is over a node. 
            Left click on a second node to finish the action.
          o Toggle a node.
          o Remove a node.

A decomposition is specified by disabling one or more nodes. The 
'level-nodes' are black, the 'internal nodes' are orange, and the 
regular subalgebra nodes remain white.


ALGEBRA INFO
------------

This tab gives some basic information about the previously entered Lie algebra.
You can select whether to view the information for the full algebra, the regular
subalgebra, or for the interal algebra.

    * Matrices: Displays some of the relevant matrices for the algebra.
    * Roots: lists all the roots of the algebra.
    * Representations: Here it is possible to enter Dynkin labels for a 
      representation and the computer calculate the dimension and all the weights.


LEVEL DECOMPOSITION
-------------------

In this tab a level decomposition of the Dynkin diagram set up previously 
can be calculated. The columns in the subalgebra representations panel
are the following:

    * l : Level(s).
    * p_r : Dynkin labels of the regular subalgebra part.
    * p_i : Dynkin labels of the internal part.
    * vector : Root vector.
    * a^2 : Root norm.
    * d_r : Dimension of the regular subalgebra part.
    * d_i : Dimension of the internal part.
    * mult : The root multiplicity.
    * mu : The outer multiplicity of the representation.
    * h : The height of lowest / highest weight.


VISUALIZATION
-------------

In this tab any Lie algebra can be "visualized": a 3D Hasse diagram can be drawn
of the root system of the algebra. The diagram interaction is as follows:

    * Left-button mouse drag: Translate.
    * Mouse scroll: Zoom.
    * Right-button mouse drag or alt-left-button drag: rotate. 