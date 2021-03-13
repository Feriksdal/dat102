package no.hvl.dat102.mengde.tabell;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import no.hvl.dat102.exception.EmptyCollectionException;
import no.hvl.dat102.mengde.adt.MengdeADT;

public class TabellMengde<T> implements MengdeADT<T> {
	// ADT-en Mengde implementert som tabell
	//
	private final static Random tilf = new Random();
	private final static int STDK = 100;
	private int antall;
	private T[] tab;

	public TabellMengde() {
		this(STDK);
	}

	public TabellMengde(int start) {
		antall = 0;
		tab = (T[]) (new Object[start]);
	}

	@Override
	public int antall() {
		return antall;
	}

	@Override
	public boolean erTom() {
		return (antall == 0);
	}

	@Override
	public void leggTil(T element) {
		if (!inneholder(element)) {
			if (antall == tab.length) {
				utvidKapasitet();
			}
			tab[antall] = element;
			antall++;
		}
	}

	private void utvidKapasitet() {
		T[] hjelpetabell = (T[]) (new Object[2 * tab.length]);
		for (int i = 0; i < tab.length; i++) {
			hjelpetabell[i] = tab[i];
		}
		tab = hjelpetabell;
	}

	@Override
	public T fjernTilfeldig() {
		if (erTom())
			throw new EmptyCollectionException("mengde");

		T svar = null;
		int indeks = tilf.nextInt(antall);
		svar = tab[indeks];
		tab[indeks] = tab[antall - 1];
		antall--;

		return svar;
	}

	@Override
	public T fjern(T element) {
		//TODO

		if (erTom())
			throw new EmptyCollectionException("mengde");

		boolean funnet = false;
		T svar = null;

		for (int i = 0; i < tab.length; i++) {
			if (this.tab[i].equals(element)) {
				svar = this.tab[i];
				this.tab[i] = null;
				antall--;
				return svar;
			}
		}
		return svar;
	}

	@Override
	public boolean inneholder(T element) {
		boolean funnet = false;
		for (int i = 0; (i < antall) && (!funnet); i++) {
			if (tab[i].equals(element)) {
				funnet = true;
			}
		}
		return (funnet);
	}

	/*
	 * N?r vi overkj?rer (override) equals- meteoden er det anbefalt at vi ogs?
	 * overkj?rer hascode-metoden da en del biblioterker burker hascode sammen med
	 * equals. Vi kommer tilbake til forklaring og bruk av hascode senere i faget.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + antall;
		result = prime * result + Arrays.deepHashCode(tab);
		return result;
	}

	@Override
	public boolean equals(Object m2) {

		boolean likeMengder = true;
		T element;

//		samme plass i minnet, hvis ja, return true, ellers fortsett
		if (this == m2) {return true;}

//		er m2 null, hvis nei return false, ellers fortsett
		if (m2 == null) {return false;}

//		er de av samme klasse, hvis nei, return false, ellers fortsett
		if (this.getClass() != m2.getClass()) {return false;}

		MengdeADT<T> compareMengde = (TabellMengde<T>) m2;

//		har de samme antall elementer, hvis ikke return false, ellers fortsett
		if (this.antall != compareMengde.antall()) {return false;}

//		gå gjennom alle elementer i mengdene og sammelign, hvis ulikeheter return false, ellers fortsett
		Iterator<T> teller = compareMengde.oppramser();
		while (teller.hasNext()){
			element = teller.next();
			if (!this.inneholder(element)) {return false;}

		}
		return true;
	}

	@Override
	public void leggTilAlle(MengdeADT<T> m2) {
		Iterator<T> teller = m2.oppramser();
		while (teller.hasNext())
			leggTil(teller.next());
	}

	/*
	 * Denne versjonen av unionen er lite effekktiv
	 *
	 * @Override public MengdeADT<T> union(MengdeADT<T> m2) { TabellMengde<T> begge
	 * = new TabellMengde<T>(); for (int i = 0; i < antall; i++) {
	 * begge.leggTil(tab[i]); } Iterator<T> teller = m2.oppramser();
	 *
	 * while (teller.hasNext()) { begge.leggTil(teller.next()); } return
	 * (MengdeADT<T>)begge; }
	 */
	@Override

	public MengdeADT<T> union(MengdeADT<T> m2) {
		MengdeADT<T> begge = new TabellMengde<T>();
		T element = null;
//		Oppretter iterator
		Iterator<T> teller = m2.oppramser();
//		Alle Elementer fra mengde 1(this) legges i returmengden

		for (int i = 0; i < antall; i++) {
			((TabellMengde<T>)begge).settInn(tab[i]);
		}

		while (teller.hasNext()) {
			element = teller.next();
			((TabellMengde<T>)begge).leggTil(element);

		}
		return begge;
	}//

	@Override
	public MengdeADT<T> snitt(MengdeADT<T> m2) {
		MengdeADT<T> snittM = new TabellMengde<T>();
		T element = null;
		Iterator<T> teller = m2.oppramser();
		while (teller.hasNext()) {
			element = teller.next();
			if (this.inneholder(element)) {
				((TabellMengde<T>) snittM).settInn(element);
			}
		}
		return snittM;
	}

	@Override
	public MengdeADT<T> differens(MengdeADT<T> m2) {
		//TODO
		MengdeADT<T> differensM = new TabellMengde<T>();
		T element;

		Iterator<T> teller = m2.oppramser();
		while (teller.hasNext()) {
			element = teller.next();
			if (!this.inneholder(element)) {
				((TabellMengde<T>) differensM).settInn(element);
			}
		}
		return differensM;
	}


	@Override
	public boolean undermengde(MengdeADT<T> m2) {
		//TODO
		boolean erUnderMengde = true;
		// ...
		return false;
	}

	@Override
	public Iterator<T> oppramser() {
		return new TabellIterator<T>(tab, antall);
	}

	private void settInn(T element) {
		if (antall == tab.length) {
			utvidKapasitet();
		}
		tab[antall] = element;
		antall++;
	}

}// class
