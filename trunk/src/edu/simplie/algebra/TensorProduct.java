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
 * TensorProduct.java
 *
 * Created on 8-jun-2009, 14:41:01
 */

package edu.simplie.algebra;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Computes the tensor product of two highest weight representations.
 * The algorithm used is based on the Racah-Speiser algorithm.
 *
 * @author Teake Nutma
 * @version $Revision$, $Date$
 */
public class TensorProduct implements Set<HighestWeightRep>
{
	/* The smallest representation. */
	private HighestWeightRep smallRep;
	/* The biggest representation. */
	private HighestWeightRep bigRep;
	public final Algebra algebra;

	private HashSet<HighestWeightRep> product;
	private int[] Lambda;
	private int rank;

	public TensorProduct(HighestWeightRep rep1, HighestWeightRep rep2)
	{
		// Check if the underlying algebras and root systems match.
		if(!rep1.algebra.equals(rep2.algebra))
		{
			throw new IllegalArgumentException("Root systems do not match.");
		}
		this.algebra = rep1.algebra;

		// Determine which one's the smallest representation.
		if(rep1.dim > rep2.dim)
		{
			this.smallRep	= rep2;
			this.bigRep		= rep1;
		}
		else
		{
			this.smallRep	= rep1;
			this.bigRep		= rep2;
		}

		product = new HashSet<HighestWeightRep>();

		// Construct the smallest rep fully.
		smallRep.construct(0);

		Lambda	= bigRep.highestWeight.dynkinLabels;
		rank	= algebra.rank;
		// Loop over all the weights in it.
		for (int i = 0; i < smallRep.size(); i++)
		{
			Collection<Weight> weights = smallRep.get(i);
			Iterator<Weight> iterator = weights.iterator();
			while (iterator.hasNext())
			{
				processWeight(iterator.next());
			}
		}
	}

	private void processWeight(Weight weight)
	{
		int[] lambda	= weight.dynkinLabels;
		int[] mu		= Lambda.clone();
		for (int j = 0; j < rank; j++)
		{
			mu[j] += lambda[j] + 1;
			// If one of the Dynkin labels is zero, this weight
			// does not contribute to the sum.
			if(mu[j] == 0)
				return;
		}
		// Reflect mu into the dominant Weyl chamber.
		int sign = 1;
		for (int j = 0; j < rank; j++)
		{
			if(mu[j] == 0)
				return;
			if(mu[j] < 0)
			{
				mu = algebra.simpWeylReflWeight(mu, j);
				sign = sign * -1;
				j = -1;
			}
		}
		// Subtract the Weyl vector
		for (int j = 0; j < rank; j++)
		{
			mu[j] -= 1;
		}
		HighestWeightRep newRep = new HighestWeightRep(algebra, mu);
		newRep.setOuterMult(0);
		if(!product.add(newRep))
		{
			// Search for the old one
			Iterator<HighestWeightRep> iter = product.iterator();
			while(iter.hasNext())
			{
				HighestWeightRep oldRep = iter.next();
				if(oldRep.equals(newRep))
				{
					newRep = oldRep;
					break;
				}
			}
		}
		newRep.setOuterMult(newRep.getOuterMult() + (sign * weight.getMult()));		
	}


	/*
	 * Set implementing functions.
	 */

	public int size()
	{
		return product.size();
	}

	public boolean isEmpty()
	{
		return product.isEmpty();
	}

	public boolean contains(Object o)
	{
		return product.contains(o);
	}

	public Iterator<HighestWeightRep> iterator()
	{
		return product.iterator();
	}

	public Object[] toArray()
	{
		return product.toArray();
	}

	public <T> T[] toArray(T[] a)
	{
		return product.toArray(a);
	}

	public boolean containsAll(Collection<?> c)
	{
		return product.containsAll(c);
	}


	/*
	 * Unsupported methods.
	 */

	public boolean add(HighestWeightRep o)
	{
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean remove(Object o)
	{
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean addAll(Collection<? extends HighestWeightRep> c)
	{
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean retainAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Not allowed.");
	}

	public boolean removeAll(Collection<?> c)
	{
		throw new UnsupportedOperationException("Not allowed.");
	}

	public void clear()
	{
		throw new UnsupportedOperationException("Not allowed.");
	}
}
