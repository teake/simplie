/*
 * This file is part of SimpLie.
 * 
 * SimpLie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SimpLie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SimpLie.  If not, see <http://www.gnu.org/licenses/>.
 */
 
/*
 * Factorization.java
 *
 * Created on 4-jan-2010, 16:23:17
 */

package edu.simplie.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Factorization of a number into primes. Also
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class Factorization implements Collection<Integer>
{

	public final int n;
	public final boolean duplicateFactors;

	private Collection<Integer> factors;

	public Factorization(int n)
	{
		if(n <= 0)
			throw new UnsupportedOperationException("Only positive integers can be factorized.");
		
		// Set the integer.
		this.n = n;
		// Set duplicates to false. Gets calculated later.
		boolean duplicates = false;
		// Copy the integer for the calculation.
		int m = n;
		int lastFactor = 1;

		// Start factorizing.
		factors = new ArrayList<Integer>();

		// By definition, the factor of 1 is 1.
		if (m == 1)
			factors.add(1);

		// for each potential factor i
		for (int i = 2; i <= m / i; i++)
		{
			// if i is a factor of m, repeatedly divide it out
			while (m % i == 0)
			{
				factors.add(i);
				m = m / i;
				// Check for duplicity
				if (!duplicates)
				{
					if(lastFactor == i)
						duplicates = true;
					lastFactor = i;
				}
			}
		}

		// if biggest factor occurs only once, m > 1
		if (m > 1)
			factors.add(m);

		duplicateFactors = duplicates;
	}

	public int MobiusMu()
	{
		if(duplicateFactors)
			return 0;
		// The only exception is 1.
		if(n == 1)
			return 1;
		return (int) Math.pow(-1, size());
	}


	/*
	 * Set implementing functions.
	 */

	public int size() {
		return factors.size();
	}

	public boolean isEmpty() {
		return factors.isEmpty();
	}

	public boolean contains(Object o) {
		return factors.contains(o);
	}

	public Iterator<Integer> iterator() {
		return factors.iterator();
	}

	public Object[] toArray() {
		return factors.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return factors.toArray(a);
	}

	public boolean containsAll(Collection<?> c) {
		return factors.containsAll(c);
	}

	/*
	 * Unsupported methods.
	 */

	public boolean add(Integer o) {
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean addAll(Collection<? extends Integer> c) {
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("Not allowed.");
	}

	public void clear() {
		throw new UnsupportedOperationException("Not allowed.");
	}

	@Override
	public String toString()
	{
		String output = "Integer: " + n + "\n";
		output += "  Number of factors:\t " + factors.size() + "\n";
		output += "  Factorization:\t";
		Iterator<Integer> it = factors.iterator();
		while (it.hasNext())
		{
			output += " " + it.next();
		}
		output += "\n";
		output += "  Mobius function:\t " + MobiusMu();
		return output;
	}
}
