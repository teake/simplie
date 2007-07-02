/*
 * fraction.java
 *
 * Created on 31 maart 2007, 16:02
 *
 */

package edu.rug.hep.simplie.math;

/**
 * Based on the fraction class written by Doug Lea, which is available
 * in the EDU.oswego.cs.dl.util.concurrent.misc package.
 *
 * Changes from the original:
 * - Added a new constructor where the denominator is always 1.
 * - Added a new constructor for doubles.
 * - Added in-place methods for multiplying, dividing, adding and subtracting.
 * - Added asLong() & asInt() methods.*
 *
 * @author Teake Nutma
 */

public class fraction implements Cloneable, Comparable, java.io.Serializable
{
	private long numerator;
	private long denominator;
	
	/** Return the numerator */
	public final long numerator()
	{
		return numerator;
	}
	
	/** Return the denominator */
	public long denominator()
	{
		return denominator;
	}
	
	/** Used for normalizing while setting the numerator and denominator */
	private void normalizeAndSet(long num, long den)
	{
		// normalize while constructing
		boolean numNonnegative = (num >= 0);
		boolean denNonnegative = (den >= 0);
		long a = numNonnegative? num : -num;
		long b = denNonnegative? den : -den;
		long g = gcd(a, b);
		numerator	= (numNonnegative == denNonnegative) ? (a / g) : (-a / g);
		denominator = b / g;
	}
	
	/** Create a fraction equal in value to num / den */
	public fraction(long num, long den)
	{
		normalizeAndSet(num, den);
	}
	
	/** Create a fraction equal in value to num / 1 */
	public fraction(long num)
	{
		numerator	= num;
		denominator	= 1;
	}
	
	/** Create a fraction with the same value as fraction f */
	public fraction(fraction f)
	{
		numerator	= f.numerator();
		denominator	= f.denominator();
	}
	
	/** 
	 * Create a fraction from a double. 
	 * The double has to be rational (i.e. x = y / z, with y,z integers),
	 * or else this constructor might take a while to complete.
	 */
	public fraction(double x)
	{
		long den = 1;
		while(true)
		{
			double remainder =  (den * x) % 1;
			if(remainder < 0.00001 || remainder > 0.99999 )
				break;
			den++;
		}
		numerator	= Math.round(den * x);
		denominator = den;
	}
	
	public String toString()
	{
		if (denominator() == 1)
			return "" + numerator();
		else
			return numerator() + "/" + denominator();
	}
	
	public Object clone()
	{
		return new fraction(this);
	}
	
	/** Return the value of the fraction as a double */
	public double asDouble()
	{
		return ((double)(numerator())) / ((double)(denominator()));
	}
	
	/** Returns the fraction as a long, rounding it if necessary. */
	public long asLong()
	{
		return Math.round(asDouble());
	}
	
	/** Returns the fraction as an integer, rounding it if necessary. */
	public int asInt()
	{
		return (int) asLong();
	}
	
	/** Is the fraction an integer or not? */
	public boolean isInt()
	{
		if(numerator() % denominator() == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Compute the nonnegative greatest common divisor of a and b.
	 * (This is needed for normalizing fractions, but can be
	 * useful on its own.)
	 */
	public static long gcd(long a, long b)
	{
		long x;
		long y;
		
		if (a < 0) a = -a;
		if (b < 0) b = -b;
		
		if (a >= b)
		{
			x = a;
			y = b;
		}
		else
		{
			x = b;
			y = a;
		}
		
		while (y != 0)
		{
			long t = x % y;
			x = y;
			y = t;
		}
		return x;
	}
	
	
	/********************************
	 * In-place operators
	 ********************************/
	
	/** Adds a fraction to this one in place. */
	public void add(fraction b)
	{
		long an = numerator();
		long ad = denominator();
		long bn = b.numerator();
		long bd = b.denominator();
		normalizeAndSet(an*bd+bn*ad, ad*bd);
	}
	
	/** Adds n to this fraction in place. */
	public void add(long n)
	{
		long an = numerator();
		long ad = denominator();
		normalizeAndSet(an+n*ad, ad);
	}
	
	/** Subtract n from this fraction in place. */
	public void subtract(long n)
	{
		add(-n);
	}
	
	/** Subtract the fraction b from this one in place. */
	public void subtract(fraction b)
	{
		add(b.negative());
	}
	
	/** Multiply the fraction with b in place. */
	public void multiply(fraction b)
	{
		long an = numerator();
		long ad = denominator();
		long bn = b.numerator();
		long bd = b.denominator();
		normalizeAndSet(an*bn, ad*bd);
	}
	
	/** Multiply the fraction with n in place. */
	public void multiply(long n)
	{
		normalizeAndSet(numerator()*n, denominator());
	}
	
	/** Divide the fraction with b in place. */
	public void divide(fraction b)
	{
		multiply(b.inverse());
	}
	
	/** Divide the fraction with b in place. */
	public void divide(long n)
	{
		normalizeAndSet(numerator(),denominator()*n);
	}
	
	
	/********************************
	 * New fractions from old fractions
	 ********************************/
	
	
	/** return a fraction representing the negated value of this fraction */
	public fraction negative()
	{
		long an = numerator();
		long ad = denominator();
		return new fraction(-an, ad);
	}
	
	/** return a fraction representing 1 / this fraction */
	public fraction inverse()
	{
		return new fraction(denominator(),numerator());
	}
	
	/** return a fraction representing this fraction plus b */
	public fraction plus(fraction b)
	{
		long an = numerator();
		long ad = denominator();
		long bn = b.numerator();
		long bd = b.denominator();
		return new fraction(an*bd+bn*ad, ad*bd);
	}
	
	/** return a fraction representing this fraction plus n */
	public fraction plus(long n)
	{
		long an = numerator();
		long ad = denominator();
		return new fraction(an+n*ad, ad);
	}
	
	/** return a fraction representing this fraction minus n */
	public fraction minus(long n)
	{
		return plus(-n);
	}
	
	/** return a fraction representing this fraction minus b */
	public fraction minus(fraction b)
	{
		return plus(b.negative());
	}
	
	/** return a fraction representing this fraction times b */
	public fraction times(fraction b)
	{
		long an = numerator();
		long ad = denominator();
		long bn = b.numerator();
		long bd = b.denominator();
		return new fraction(an*bn, ad*bd);
	}
	
	/** return a fraction representing this fraction times n */
	public fraction times(long n)
	{
		return new fraction(numerator()*n, denominator());
	}
	
	/** return a fraction representing this fraction divided by b */
	public fraction dividedBy(fraction b)
	{
		return times(b.inverse());
	}
	
	/** return a fraction representing this fraction divided by n */
	public fraction dividedBy(long n)
	{
		return new fraction(numerator(),denominator()*n);
	}
	
	
	
	/********************************
	 * Override default implementations.
	 ********************************/
	
	
	/**
	 * return a number less, equal, or greater than zero
	 * reflecting whether this fraction is less, equal or greater than
	 * the value of fraction other.
	 */
	public int compareTo(Object other)
	{
		fraction b = (fraction)(other);
		long an = numerator();
		long ad = denominator();
		long bn = b.numerator();
		long bd = b.denominator();
		long l = an*bd;
		long r = bn*ad;
		return (l < r)? -1 : ((l == r)? 0: 1);
	}
	
	/**
	 * return a number less, equal, or greater than zero
	 * reflecting whether this fraction is less, equal or greater than n.
	 */
	public int compareTo(long n)
	{
		long an = numerator();
		long ad = denominator();
		long bn = n;
		long bd = 1;
		long l = an*bd;
		long r = bn*ad;
		return (l < r)? -1 : ((l == r)? 0: 1);
	}
	
	public boolean equals(Object other)
	{
		return compareTo((fraction)other) == 0;
	}
	
	public boolean equals(long n)
	{
		return compareTo(n) == 0;
	}
	
	public int hashCode()
	{
		return (int) (numerator ^ denominator);
	}
	
}

