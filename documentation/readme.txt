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



RUNNING SIMPLIE
---------------

For finite-dimensional Lie algebra it is sufficient to run SimpLie 
with the default Java parameters. You can do so with the following
command:

	java -jar SimpLie.jar

If, however, you plan to do calculations with infinite-dimensional
algebras, it is wise to reserve extra memory for SimpLie by running

	java -jar -Xms128M -Xmx768M SimpLie.jar

You can of course adjust the 128 megabyte minimum and 768 megabyte
maximum memory settings to your liking.



ALGEBRA SETUP
-------------

Within the white Dynkin diagram panel of the Algebra Setup tab one
can interactively 'click' a Dykin diagram together. The relevant
commands are:

    * Left mouse-click: Add a node. Hold shift to automatically add
      a connection to the previous node.
    * Middle mouse-click: Toggle a node.
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



LEVEL DECOMPOSITION
-------------------

In this tab a level decomposition of the Dynkin diagram set up 
previously can be calculated.

    * Settings:
          o Sign convention: whether to scan for lowest weight 
            representations (p = - A m) or highest weight representation 
            (p = + A m).
          o Show zero multiplicity roots: show representations of 
            which the lowest (highest) weight has zero multiplicity 
            as a root.
          o Show zero mu representations: show representations which
            have zero outer multiplicity (mu).
          o Calculate root representations: Calculate the root multiplicities
            while scanning.
          o Calculate representation multiplicities: Calculate the outer
            multiplicities while scanning.
    * Scan levels:
          o Maximum level: The maximum level to which to scan.
          o Minimum level: The mininum level to which to scan.
          o Scan levels: This button start the scanning procedure.
    * Columns in the subalgebra representations panel:
          o l : Level(s).
          o p_r : Dynkin labels of the regular subalgebra part.
          o p_i : Dynkin labels of the internal part.
          o vector : Root vector.
          o a^2 : Root norm.
          o d_r : Dimension of the regular subalgebra part.
          o d_i : Dimension of the internal part.
          o mult : The root multiplicity.
          o mu : The outer multiplicity of the representation.
          o h : The height of lowest / highest weight.
    * Weights of selected representation:
          o Dominant weights: Show the dominant weights of the representation
            selected in the representation table.
          o All weightst: Show all the weights of a representation in the
            "Weights of selected representation" panel, instead of only
            the dominant weights.