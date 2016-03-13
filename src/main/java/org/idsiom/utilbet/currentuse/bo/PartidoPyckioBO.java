package org.idsiom.utilbet.currentuse.bo;

import java.io.Serializable;

public class PartidoPyckioBO implements Serializable {
    private static final long serialVersionUID = 6171090211553693547L;

	private String fechaStr;
     
     private String deporte;
     
     private String pais;
     
     private String liga;
     
     private String equipoLocal;
     
     private String equipoVisitante;
     
     /*
     public Long getFechaLong() {
 		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm");
 		
 		try {
             // En caso que la fecha no cumple con el formato, lo mas probable es que el juego est� 
 			// en curso, con una fecha como la siguiente: "20160202 73'"
 			if(fecha.length() != 14) {
 				// Si no cumple el formato, se devuelve un Long que asegure que no se tome en cuenta el juego
 				return (new Date()).getTime() + (3*MINS_PROXIMIDAD * 60 * 1000);
 			} else {
 				// En caso de cumplir el formato, se convierte la fecha y se devuelve la misma
 				Date date = (Date) formatter.parse(fecha);
 	            return date.getTime();
 			}
 			
         } catch (ParseException e) {
         	e.printStackTrace();
         	return null;
         }

 	}
    */

	public String getFechaStr() {
		return fechaStr;
	}

	public void setFechaStr(String fechaStr) {
		this.fechaStr = fechaStr;
	}

	public String getDeporte() {
		return deporte;
	}

	public void setDeporte(String deporte) {
		this.deporte = deporte;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getLiga() {
		return liga;
	}

	public void setLiga(String liga) {
		this.liga = liga;
	}

	public String getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(String equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public String getEquipoVisitante() {
		return equipoVisitante;
	}

	public void setEquipoVisitante(String equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}
     
	@Override
    public String toString() {
		String result;
		StringBuffer sb = new StringBuffer();
		
		sb.append("<fecha = ");
		sb.append(this.fechaStr);
		sb.append(", pais = ");
		sb.append(this.pais);
		sb.append(", liga = ");
		sb.append(this.liga);
		sb.append(", EqLocal = ");
		sb.append(this.equipoLocal);
		sb.append(", EqVisitante = ");
		sb.append(this.equipoVisitante);
		sb.append(">");
		
		result = sb.toString();
		sb = null;
		
		return result;
    }
     
     
     
}