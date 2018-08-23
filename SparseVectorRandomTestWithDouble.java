import java.util.Random;

/**
 * A class to test SparseVector. 
 * It uses instances of vectors of FDoubles, a wrapper of the type double.
 *
 */
public class SparseVectorRandomTestWithDouble {

	/**
	 * Dimension of the created vectors.
	 */
	private static final int DIM = 100; 
	
	/**
	 * Density of the cretaed vectors.
	 */
	private static final double DENSITY = 0.1; 

	/**
	 * Elements to put in the vectors.
	 */
	private static final double[] doubles;

	/**
	 * Initialization of the doubles array.
	 */
	private static final double STEP = 2.5;
	static{
		doubles = new double[(int)(DIM*DENSITY*2)];
		doubles[0] = 0.0;
		for (int i = 1; i< doubles.length; i++)
			doubles[i] =  doubles[i-1] + STEP;
	}


	public static void main(String[] args) {	
		new SparseVectorRandomTestWithDouble(10101, 3).run();
	}

	/**
	 * Our random object to generate operations and doubles.
	 */
	private Random rand;

	/**
	 * The vector manipuated during the test execution.
	 */
	private SparseVector<FDouble> v;
	
	/**
	 * The number of operation calls to perform during the test execution.
	 */
	private int howManyCalls;
	
	public SparseVectorRandomTestWithDouble(int howManyTests, int howManySets) {
		this.rand  = new Random ();
		this.howManyCalls = howManyTests;
		this.v = createVector();
	}

	public void run () {
		for (int i = 0; i < howManyCalls; i++) {
			switch (rand.nextInt(8)) {
			case 0: //set
				FDouble f = newFDouble();
				int j = rand.nextInt(DIM-1)+1; //tinha aqui DIM q vai contra a precondição
											  // o ConGu descobriu
				System.out.print ("set("+ j+ ","+ f+")=");
				v.set(j, f);
				break;
			case 1: //dim
				System.out.print ("dim "+v.dim()+"  ");
				break;
			case 2: //get
				j = rand.nextInt(DIM-1)+1;		//idem aqui numa versao posterior
				System.out.print ("get("+ j + ") = "+v.get(j)+"  ");
				break;
			case 3: //sumV
				SparseVector<FDouble> v2 = createVector();
				System.out.print ("sumV( "+v2 + ") ");
				v.sum(v2);
				break;
			case 4: //scalarProd
				f = new FDouble(rand.nextInt(4)); //to avoid to get big numbers for which field properties do not hold
				System.out.print ("scalarProd("+ f + ") ");
				v.scalarProd(f);
				break;
			case 5: //dotProd
				v2 = createVector();
				System.out.print ("dotProd( "+v2 + ")= "+ v.dotProd(v2) + " ");
				break;
			case 6: //els
				System.out.print ("dim "+v.els()+"  ");
				break;
			case 7: //sym
				System.out.print ("sym  ");
				v.sym();
				break;
			}
			System.out.println(v);
		}
	}

	/**
	 * Creates a new SparseVector and fills it with some elements
	 * @return The newly created vector.
	 */
	private SparseVector<FDouble> createVector () {
		SparseVector<FDouble> result = new SparseVector<FDouble>(DIM, new FDouble());
		for (int i = 0; i < (int)(DIM*DENSITY); i++) {
			FDouble f = newFDouble();
			int j = rand.nextInt(DIM-1)+1;
			result.set(j, f);
		}
		return result;
	}

	/**
	 * Creates a new FDouble with one random value from doubles array.
	 * @return The newly created FDouble.
	 */
	private FDouble newFDouble() {
		return new FDouble(doubles[rand.nextInt(doubles.length)]);
	}

}
