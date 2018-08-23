import java.util.Iterator;

/**
 * A mutable implementation of sparse vector (i.e., a vector with few non-zero
 * components) based on a linked list.
 * 
 * @param <F> The type of the elements in the vector. This type must be a Field.
 * 
 * @author Grupo 092
 */

public class SparseVector<F extends Field<F>> implements
		Iterable<Pair<Integer, F>>, Cloneable {

	private static class Node<T> {

		private T item;
		private Node<T> next;

		private Node(T item, Node<T> next) {
			this.item = item;
			this.next = next;
		}
	}

	private Node<Pair<Integer, F>> first;
	private int dim, nodeSize;
	private F zero;

	public SparseVector(int n, F f) {
		dim = n;
		zero = f;
		nodeSize = 0;
	}

	/**
	 * Define o valor de "f" para o indice "i" do vector
	 * 
	 * @param i
	 *            Indice do vector a definir
	 * @param f
	 *            Valor a definir no Indice "i"
	 */
	public void set(int i, F f) {

		if (i > 0 && i <= this.dim) {
			if (f != this.zero) {
				Pair<Integer, F> pair = new Pair<>(i, f);

				if (nodeSize == 0) {
					first = new Node<>(pair, null);
					nodeSize++;
				} else {
					if (!existeIndice(i, f)) {
						organizaIndice(pair);
						nodeSize++;
					}
				}
			}
		}
	}

	/**
	 * Introduz o Pair "pair" no SparseVector
	 * 
	 * @param pair Pair a introduzir no SparseVector
	 */
	private void organizaIndice(Pair<Integer, F> pair) {
		Node<Pair<Integer, F>> prev = first;
		Node<Pair<Integer, F>> next = first.next;

		if (prev.item.getFst() > pair.getFst()) {
			first = new Node<>(pair, prev);
		} else {
			boolean notSet = true;
			while (prev != null && notSet) {
				if (prev.item.getFst() < pair.getFst()) {
					if (next == null || pair.getFst() < next.item.getFst()) {
						prev.next = new Node<>(pair, next);
						notSet = false;
					} else {
						prev = next;
						next = next.next;
					}
				}
			}
		}
	}

	/**
	 * Verifica se o indice "i" ja esta iniciado no SparseVector
	 * 
	 * @param i Indice a verificar
	 * @param f valor a substituir no indice "i", caso este já esteja iniciado
	 * @return Valor boolean, true caso ja exista, false caso contrario
	 */
	private boolean existeIndice(int i, F f) {
		boolean res = false;
		Node<Pair<Integer, F>> temp = first;

		while (temp != null && !res) {

			if (temp.item.getFst() == i) {
				res = true;
				temp.item = new Pair<>(i, f);
			}
			temp = temp.next;
		}
		return res;
	}

	/**
	 * Retorna a dimensao do vector
	 * 
	 * @return Retorna a dimensao do vector
	 */
	public int dim() {
		return this.dim;
	}

	/**
	 * Devolve o elemento no indice "i"
	 * 
	 * @param i Indice do elemento a devolver
	 * @return Elemento presente no Indice "i"
	 */
	public F get(int i) {

		Node<Pair<Integer, F>> temp = first;

		while (temp != null) {
			if (temp.item.getFst() == i)
				return temp.item.getSnd();
			temp = temp.next;
		}
		return zero;
	}

	/**
	 * Soma dois SparseVector, guardando o resultado
	 * 
	 * @param v SparseVector a somar
	 * @requires this.dim == v.dim
	 */
	public void sum(SparseVector<F> v) {
		boolean found = false;
		Iterator<Pair<Integer, F>> itr = v.iterator();

		for (Pair<Integer, F> par : this) {

			while (itr.hasNext() && !found) {

				Pair<Integer, F> par2 = itr.next();

				if (par.getFst() == par2.getFst()) {
					this.set(par.getFst(), par.getSnd().sum(par2.getSnd()));
					found = true;
				} else {
					found = false;
				}
			}
			if (!found)
				this.set(par.getFst(), par.getSnd());
			found = false;
		}

		itr = this.iterator();
		for (Pair<Integer, F> par : v) {

			while (itr.hasNext() && !found) {
				Pair<Integer, F> par2 = itr.next();
				if (par.getFst() == par2.getFst()) {
					found = true;
				}
			}
			if (!found)
				this.set(par.getFst(), par.getSnd());
			found = false;
		}
	}

	/**
	 * Calcula o produto escalar do sparseVector com "f"
	 * 
	 * @param f Valor a utilizar para calcular o produto escalar
	 */
	public void scalarProd(F f) {

		Node<Pair<Integer, F>> temp = first;

		while (temp != null) {
			temp.item.getSnd().prod(f);
			temp = temp.next;
		}
	}

	/**
	 * Retorna o produto interno deste vector com o vector "v"
	 * @param v Outro vector a utilizar para calcular o Produto Interno
	 * @return O produto interno deste vector como vector "v"
	 */
	public F dotProd(SparseVector<F> v) {
		
		F result = zero;

		Node<Pair<Integer, F>> temp = first;

		while (temp != null) {
			F a = temp.item.getSnd();
			F b = v.get(temp.item.getFst());

			result = result.sum(a.prod(b));

			temp = temp.next;
		}

		return result;
	}

	/**
	 * Retorna o Zero do Field
	 * 
	 * @return Retorna o Zero do Field
	 */
	public F getZero() {
		return this.zero;
	}

	/**
	 * Retorna o numero de componentes nao-nulas (densidade do vector)
	 * 
	 * @return Numero de componentes nao-nulas
	 */
	public int els() {
		return this.nodeSize;
	}

	/**
	 * Transforma todas as componentes deste vector nas suas simetricas
	 */
	public void sym() {

		Node<Pair<Integer, F>> temp = first;
		
		while (temp != null) {
			temp.item.getSnd().sym();
			temp = temp.next;
		}
	}

	/**
	 * Testa este SparseVector para ver se é igual a outro Objecto o
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		return o instanceof SparseVector && (equalsVector((SparseVector<F>) o));
	}

	// Metodo auxiliar ao metodo equals(Object o)
	private boolean equalsVector(SparseVector<F> o) {
		if (this.dim != o.dim)
			return false;

		for (Pair<Integer, F> p : this) {
			for (Pair<Integer, F> pr : o) {
				if (!p.equals(pr))
					return false;
			}
		}
		return true;
	}

	/**
	 * Clona este SparseVector
	 * 
	 * @see java.lang.Object#clone(java.lang.Object)
	 */
	public SparseVector<F> clone() {
		SparseVector<F> result = new SparseVector<F>(this.dim, this.zero);
		Node<Pair<Integer, F>> temp = first;

		while (temp != null) {
			result.set(temp.item.getFst(), temp.item.getSnd());
		}
		return result;
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("< ");
		for (Pair<Integer, F> p : this) {
			sb.append(p.getSnd().toString() + ", ");
		}
		sb.append(" >");
		return sb.toString();
	}

	/**
	 * Retorna um iterador para este SparseVector
	 */
	@Override
	public Iterator<Pair<Integer, F>> iterator() {
		return new SparseVectorIterator();
	}

	private class SparseVectorIterator implements Iterator<Pair<Integer, F>> {

		private Node<Pair<Integer, F>> current;

		public SparseVectorIterator() {
			current = first;
		}

		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		
		@Override
		public Pair<Integer, F> next() {
			Pair<Integer, F> par = current.item;
			current = current.next;

			return par;
		}

		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
