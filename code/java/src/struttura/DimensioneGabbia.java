package struttura;

public enum DimensioneGabbia {
	SMALL(1),MEDIUM(2),LARGE(4);
	private int  metriq;
	
	 DimensioneGabbia(int m) {
		metriq=m;
	}
	 
	 public int getMetriq() {
		 return metriq;
	 }
	 
	 
	/*Assegnazione Gabbie:
	 * Small: Per pennuti e criceti
	 * Medium: Per gatti e cani di taglia piccola
	 * Large: Cani di taglia medio grande
	 * */
	 
}
