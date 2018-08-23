/* 
 * ADT that represents a vector space over a field 
 * (with some adaptations to make it implementable in Java).
 * 
 * @author antonialopes
 * @version 1.0 - 15/Mar/2015
 */
specification Vector[Field]
	sorts
		Vector[Field]
	constructors
		newVector: int Field -->? Vector[Field]; 				//see (*)
		set: Vector[Field] int Field -->? Vector[Field];
	observers
	    dim: Vector[Field] --> int;
		get: Vector[Field] int  -->? Field;
		sumV: Vector[Field] Vector[Field] --> Vector[Field];
		scalarProd: Vector[Field] Field  --> Vector[Field];
		dotProd: Vector[Field] Vector[Field] -->? Field;
	others	
		zero: Vector[Field] --> Field; 							//see (**)
		density: Vector[Field] --> int;
		symVector: Vector[Field] --> Vector[Field];
    domains
  		i: int; f: Field; V, V1: Vector[Field];  	
  			
    	newVector(i,f) if i > 0;
    	set(V, i, f) if i > 0 and i <= dim(V);
		get(V, i) if i > 0 and i <= dim(V);
		dotProd(V, V1) if dim(V) = dim(V1) and zero(V) = zero(V1);
  	axioms
   		i, j, n: int; z, f, f1, f2: Field; V, V1: Vector[Field];
		
		dim(newVector(n,f)) = n;
		dim(set(V, i, f)) = dim(V);
		
		get(newVector(n,z), j) = z;
		get(set(V, i, f), j) = f when i=j else get(V, j);
		
		scalarProd(newVector(n,z), f) = newVector(n,z);
		scalarProd(set(V, i, f1), f) = set(scalarProd(V, f), i, prod(f1,f));

		sumV(newVector(n,z), V) = V;
		sumV(set(V1, i, f1), V) = set(sumV(V1, V), i, sum(f1,get(V, i)));
		
		dotProd(newVector(n,z), V) = z;
		dotProd(set(V1, i, f1), V) = sum(prod(f1,get(V,i)), dotProd(V1,V)) if get(V1,i) = zero(V);
		
		zero(newVector(n,z)) = z;
		zero(set(V, i, f)) = zero(V);	
		
		//axioms that capture that the zero(V) is indeed the zero of the field
		sum(zero(V),f) = f;
		zero(V) = sum(f1,f2) if f2 = sym(f1);
		
		density(newVector(n,f)) = 0;
		density(set(V, i, f)) = 1 + density(V) if get(V,i) = zero(V) and f != zero(V);
				
		symVector(newVector(n,f)) = newVector(n,f);
		symVector(set(V, i, f)) = set(symVector(V), i, sym(f));
		
		set(V, i , f) = V if f = zero(V) and get(V, i) = zero(V);
		set(set(V, i, f), j, f1) = set(V, i, f1) when i = j 
					else set(set(V, j, f1), i, f);				
end specification

// (*) The second parameter represents the zero of the field 
//     which needs to be given during the construction of the vector
// 	   since an interface in Java cannot impose constraints over the
//     contructors of the subtypes.

// (**) added to get the zero that was provided during the construction of a vector