package celltraffic_vorlage;

public class Auto {
	public int nr; // fortlaufende Nummer
	public int pos; // Position im Feld
	public int geschw;
	public String kennzeichen;
	public static int statisch = 0;

	public Auto(int nr, int pos, int geschw) {
		this.nr = nr;
		this.pos = pos;
		this.geschw = geschw;
		this.kennzeichen = "";
	}

	public Auto(int pos, int geschw) {
		this (++statisch, pos, geschw);
	}
}
