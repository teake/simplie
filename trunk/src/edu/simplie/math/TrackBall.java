package edu.simplie.math; 

/*
 * Trackball code:
 *
 * Implementation of a virtual TrackBall.
 * Implemented by Gavin Bell, lots of ideas from Thant Tessman and
 *   the August '88 issue of Siggraph's "Computer Graphics," pp. 121-129.
 *
 * Vector manip code:
 *
 * Original code from:
 * David M. Ciemiewicz, Mark Grossman, Henry Moreton, and Paul Haeberli
 *
 * Much mucking with by:
 * Gavin Bell
 *
 * Java version by Tim Lambert, following the method names used
 * in Magician, and adding some more methods to make it easier to use
 * 
 * Modified by Teake Nutma, added zoom and translate controls.
 * 
 */
import com.sun.opengl.util.Animator;
import java.awt.event.*;
import javax.media.opengl.GLJPanel;

/** A TrackBall.  Allows interactive rotation of 3d views or objects  */
public class TrackBall extends MouseAdapter implements MouseMotionListener, MouseWheelListener
{
	private final float trackballSize;
	private final static int EPS2 = 25;  //only spin if mouse moved this far
	private int prevX;
	private int prevY;
	private int startX;
	private int startY;
	private float transX;
	private float transY;
	private float[] curQuat;
	private float[] lastQuat;
	private boolean spin;
	private boolean rotate;
	private float zoom = 1.0f;
	private Animator animator;
	private GLJPanel component;

	public TrackBall(GLJPanel component)
	{
		trackballSize = 0.8f;
		component.addMouseListener(this);
		component.addMouseMotionListener(this);
		component.addMouseWheelListener(this);
		this.component = component;
		animator = new Animator(component);
		reset();
	}

	public float getZoom()
	{
		return zoom;
	}
	
	public float getTransX()
	{
		return transX;
	}
	public float getTransY()
	{
		return transY;
	}
	
	/** return rotation Matrix representing current rotation of TrackBall */
	public float[] getRotMatrix()
	{
		float[] rotMat = buildMatrix(curQuat);
		if(spin)
		{
			curQuat = addQuats(lastQuat, curQuat);
		}
		return rotMat;
	}	
	
	public void reset()
	{
		prevX = prevY = startX = startY = 0;
		transX = transY = 0;
		curQuat = buildQuaternion(0.0f, 0.0f, 0.0f, 0.0f);
		lastQuat = curQuat;
		spin = rotate = false;
		zoom = 1.0f;
		animator.stop();
	}
	
	// deal with Mouse events

	@Override
	public void mouseReleased(MouseEvent evt)
	{
		int dx = startX - evt.getX();
		int dy = startY - evt.getY();
		spin = rotate && (dx * dx + dy * dy > EPS2);
		if(spin && !animator.isAnimating())
			animator.start();
			
	}

	@Override
	public void mousePressed(MouseEvent evt)
	{
		startX = prevX = evt.getX();
		startY = prevY = evt.getY();
		spin	= false;
		rotate	= (evt.getButton() == MouseEvent.BUTTON3) || evt.isAltDown();
		if(animator.isAnimating())
			animator.stop();
	}

	public void mouseMoved(MouseEvent evt)
	{
	}

	public void mouseDragged(MouseEvent evt)
	{
		float aWidth	= (float) evt.getComponent().getSize().width;
		float aHeight	= (float) evt.getComponent().getSize().height;
		int currX = evt.getX();
		int currY = evt.getY();

		if(rotate)
		{
			lastQuat =
					buildQuaternion((float) (2.0f * prevX - aWidth) / aWidth,
					(float) (aHeight - 2.0f * prevY) / aHeight,
					(float) (2.0f * currX - aWidth) /aWidth,
					(float) (aHeight - 2.0f * currY) / aHeight);
			curQuat = addQuats(lastQuat, curQuat);
		}
		else
		{
			transX += 8.0f * (float)(currX-prevX) / (aWidth * zoom);
			transY += 8.0f * (float)(prevY-currY) / (aHeight * zoom);			
		}
		prevX = currX;
		prevY = currY;
		
		component.repaint();		
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		zoom *= 1.0f - (float) e.getWheelRotation() / 12;
		component.repaint();
	}

	/*
	 * Ok, simulate a track-ball.  Project the points onto the virtual
	 * TrackBall, then figure out the axis of rotation, which is the cross
	 * product of P1 P2 and O P1 (O is the center of the ball, 0,0,0)
	 * Note:  This is a deformed TrackBall-- is a TrackBall in the center,
	 * but is deformed into a hyperbolic sheet of rotation away from the
	 * center.  This particular function was chosen after trying out
	 * several variations.
	 *
	 * It is assumed that the arguments to this routine are in the range
	 * (-1.0 ... 1.0)
	 */
	public float[] buildQuaternion(float p1x, float p1y, float p2x, float p2y)
	{
		float[] a = new float[3]; /* Axis of rotation */
		float phi;  /* how much to rotate about axis */
		float[] p1 = new float[3];
		float[] p2 = new float[3];
		float[] d = new float[3];
		float t;

		if(p1x == p2x && p1y == p2y)
		{
			/* Zero rotation */
			float[] q =
			{
				0.0f, 0.0f, 0.0f, 1.0f
			};
			return q;
		}

		/*
		 * First, figure out z-coordinates for projection of P1 and P2 to
		 * deformed sphere
		 */
		vset(p1, p1x, p1y, projectToSphere(trackballSize, p1x, p1y));
		vset(p2, p2x, p2y, projectToSphere(trackballSize, p2x, p2y));

		/*
		 *  Now, we want the cross product of P1 and P2
		 */
		vcross(p2, p1, a);

		/*
		 *  Figure out how much to rotate around that axis.
		 */
		vsub(p1, p2, d);
		t = vlength(d) / (2.0f * trackballSize);

		/*
		 * Avoid problems with out-of-control values...
		 */
		if(t > 1.0)
		{
			t = 1.0f;
		}
		if(t < -1.0)
		{
			t = -1.0f;
		}
		phi = 2.0f * (float) Math.asin(t);

		return axisToQuat(a, phi);
	}

	/** Create a unit quaternion that represents the rotation about axis
	by theta */
	public float[] axisToQuat(float[] axis, float theta)
	{
		float[] q = new float[4];
		q[3] = (float) Math.cos(theta / 2.0f); //scalar part
		vnormal(axis);
		vcopy(axis, q);
		vscale(q, (float) Math.sin(theta / 2.0));
		return q;
	}

	public float[] renormalizeQuat(float[] q)
	{
		float len = 0.0f;
		for(int i = 0; i < q.length; i++)
		{
			len += q[i] * q[i];
		}
		len = (float) Math.sqrt(len);
		float[] ans = new float[q.length];
		for(int i = 0; i < q.length; i++)
		{
			ans[i] = q[i] / len;
		}
		return ans;
	}
	/**
	 * Given two rotations, e1 and e2, expressed as quaternion rotations,
	 * figure out the equivalent single rotation and stuff it into dest.
	 *
	 * This routine also normalizes the result every RENORMCOUNT times it is
	 * called, to keep error from creeping in.
	 *
	 * NOTE: This routine is written so that q1 or q2 may be the same
	 * as dest (or each other).
	 */
	private static final int RENORMCOUNT = 97;
	private int count = 0;

	public float[] addQuats(float[] q1, float[] q2)
	{
		float[] ans = new float[4];
		ans[3] = q2[3] * q1[3] - q2[0] * q1[0] - q2[1] * q1[1] - q2[2] * q1[2];
		ans[0] = q2[3] * q1[0] + q2[0] * q1[3] + q2[1] * q1[2] - q2[2] * q1[1];
		ans[1] = q2[3] * q1[1] + q2[1] * q1[3] + q2[2] * q1[0] - q2[0] * q1[2];
		ans[2] = q2[3] * q1[2] + q2[2] * q1[3] + q2[0] * q1[1] - q2[1] * q1[0];
		if(++count > RENORMCOUNT)
		{
			count = 0;
			renormalizeQuat(ans);
		}
		return ans;
	}

	/**
	 * Project an x,y pair onto a sphere of radius r OR a hyperbolic sheet
	 * if we are away from the center of the sphere.
	 */
	public float projectToSphere(float r, float x, float y)
	{
		float z;
		float d = (float) Math.sqrt(x * x + y * y);
		if(d < r * 0.70710678118654752440f)
		{    /* Inside sphere */
			z = (float) Math.sqrt(r * r - d * d);
		}
		else
		{           /* On hyperbola */
			float t = r / 1.41421356237309504880f;
			z = t * t / d;
		}
		return z;
	}


	/*
	 * Build a rotation matrix, given a quaternion rotation.
	 *
	 */
	public float[] buildMatrix(float q[])
	{
		float[] m = new float[16];
		m[0] = 1.0f - 2.0f * (q[1] * q[1] + q[2] * q[2]);
		m[1] = 2.0f * (q[0] * q[1] - q[2] * q[3]);
		m[2] = 2.0f * (q[2] * q[0] + q[1] * q[3]);
		m[3] = 0.0f;

		m[4] = 2.0f * (q[0] * q[1] + q[2] * q[3]);
		m[5] = 1.0f - 2.0f * (q[2] * q[2] + q[0] * q[0]);
		m[6] = 2.0f * (q[1] * q[2] - q[0] * q[3]);
		m[7] = 0.0f;

		m[8] = 2.0f * (q[2] * q[0] - q[1] * q[3]);
		m[9] = 2.0f * (q[1] * q[2] + q[0] * q[3]);
		m[10] = 1.0f - 2.0f * (q[1] * q[1] + q[0] * q[0]);
		m[11] = 0.0f;

		m[12] = 0.0f;
		m[13] = 0.0f;
		m[14] = 0.0f;
		m[15] = 1.0f;
		return m;
	}

	/* our own collectio of 3D vector functions */
	public static void vzero(float[] v)
	{
		v[0] = 0.0f;
		v[1] = 0.0f;
		v[2] = 0.0f;
	}

	public static void vset(float[] v, float x, float y, float z)
	{
		v[0] = x;
		v[1] = y;
		v[2] = z;
	}

	public static void vsub(float[] src1, float[] src2, float[] dst)
	{
		dst[0] = src1[0] - src2[0];
		dst[1] = src1[1] - src2[1];
		dst[2] = src1[2] - src2[2];
	}

	public static void vcopy(float[] v1, float[] v2)
	{
		for(int i = 0; i < 3; i++)
		{
			v2[i] = v1[i];
		}
	}

	public static void vcross(float[] v1, float[] v2, float[] cross)
	{
		float[] temp = new float[3];

		temp[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
		temp[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
		temp[2] = (v1[0] * v2[1]) - (v1[1] * v2[0]);
		vcopy(temp, cross);
	}

	public static float vlength(float[] v)
	{
		return (float) Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
	}

	public static void vscale(float[] v, float div)
	{
		v[0] *= div;
		v[1] *= div;
		v[2] *= div;
	}

	public static void vnormal(float[] v)
	{
		vscale(v, 1.0f / vlength(v));
	}

	public static float vdot(float[] v1, float[] v2)
	{
		return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
	}

	public static void vadd(float[] src1, float[] src2, float[] dst)
	{
		dst[0] = src1[0] + src2[0];
		dst[1] = src1[1] + src2[1];
		dst[2] = src1[2] + src2[2];
	}
}
