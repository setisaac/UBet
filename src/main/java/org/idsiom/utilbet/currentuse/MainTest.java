package org.idsiom.utilbet.currentuse;

import static org.idsiom.utilbet.currentuse.constantes.ConstantesCurrent.MINS_PROXIMIDAD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.idsiom.utilbet.currentuse.bo.CurrentPOddsPortal;
import org.idsiom.utilbet.currentuse.bo.ListPartidosSerializable;
import org.idsiom.utilbet.currentuse.bo.ListPartidosSerializablePyckio;
import org.idsiom.utilbet.currentuse.bo.PartidoPyckioBO;
import org.idsiom.utilbet.currentuse.bo.ResultadoPartidoBO;
import org.idsiom.utilbet.currentuse.interlocutor.OddsPortalInterCurrentUseImpl;
import org.idsiom.utilbet.currentuse.interlocutor.PickioInterlocutorImpl;
import static org.idsiom.utilbet.currentuse.constantes.ConstantesCurrent.RUTA_ARCHIVO;

public class MainTest {

	public static void main(String[] args) {
		Boolean useSerializables = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
		Date now = new Date();
		Long esteMomento = (now).getTime();

		String strTope = sdf.format((esteMomento + (MINS_PROXIMIDAD * 60 * 1000)));

		System.out.println("strTope " + strTope);
		List<PartidoPyckioBO> list;
		List<CurrentPOddsPortal> listOP;
		PickioInterlocutorImpl inter = null;
		try {

			if (!useSerializables) {
				OddsPortalInterCurrentUseImpl interOP = OddsPortalInterCurrentUseImpl.getInstance();
				inter = PickioInterlocutorImpl.getInstance();

				Thread.sleep(5000);
				ListPartidosSerializable listSer = new ListPartidosSerializable();
				Boolean cargada = false;
				while(!cargada) {
					listSer = interOP.getPs(true);
					
					if( listSer.getListaPsHoyFuturo().size() == 0 ) {
						cargada = false;
						System.out.println("Es necesario repetir el proceso");
					} else {
						cargada = true;
					}
					
				}
				
				
				
				
				Thread.sleep(5000);
				list = inter.getPartidosPorHora(0L);

				System.out.println("ANTES DE VALIDAR SEGUIMIENTO");

				for (CurrentPOddsPortal aux : listSer.getListaPsHoyFuturo()) {
					System.out.println(aux);
				}

				listOP = validarSeguimiento(listSer);

				listSer.setListaPsHoyFuturo(listOP);

				ListPartidosSerializable listSer_2 = new ListPartidosSerializable();
				listSer_2.setListaPsHoyFuturo(listOP);
				guardarSerializadoOP(listSer);

				/*
				System.out.println(
						"qq[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[-------------   "
								+ listSer_2.getListaPsHoyFuturo().size());
				 */
				
				ListPartidosSerializablePyckio listPIO = new ListPartidosSerializablePyckio();
				listPIO.setPartidos(list);
				guardarSerializadoP(listPIO);

			} else {
				listOP = leerSerializadoOP().getListaPsHoyFuturo();
				list = leerSerializadoP().getPartidos();

			}

			System.out.println("LISTA PYCKIO " + list.size());
			for (PartidoPyckioBO p : list) {
				System.out.println("__________>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");

				System.out.println(p.toString());

				System.out.println(" ");

			}

			System.out.println("LISTA ODDSPORTAL " + listOP.size());
			PartidoPyckioBO pEquivalente;
			int iAux;
			int contador = 1;
			for (CurrentPOddsPortal pop : listOP) {
				System.out.println("ODDSPORTAL = " + pop.toString());
				
				pEquivalente = inter.findTraduction( list, pop );
				System.out.println("       " );
				System.out.println("     *******     ******   EQUIVALENTE PYCKIO = " + pEquivalente);
				System.out.println("       " );
				System.out.println("       " );
				System.out.println("       " );
				
				//
				if(pEquivalente != null) {
					iAux = contador % 3;
					ResultadoPartidoBO resultBuscado;
					Double inversa = 0.0;
					if(iAux == 0) {
						resultBuscado = ResultadoPartidoBO.VISITANTE;
						inversa = 1 / pop.getC2();
					} else if (iAux == 1 ){
						resultBuscado = ResultadoPartidoBO.LOCAL;
						inversa = 1 / pop.getC1();
					} else {
						resultBuscado = ResultadoPartidoBO.EMPATE;
						inversa = 1 / pop.getcX();
					}
					
					int stake = (int)(((inversa + 0.1) * 10));
					
					try {
						
						System.out.println("Se intentara montar el pyck, con stake = " + stake + " ; resultB = " + resultBuscado.getAbreviatura());
						
						inter.montarPick(pEquivalente, resultBuscado, stake);
					} catch(Exception ex) {
						ex.printStackTrace();
						
						System.out.println("No fue posible montar el pyck");
					}
					
					
					
					contador++;
				}
				
				
				
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	private static List<CurrentPOddsPortal> validarSeguimiento(ListPartidosSerializable lNews) {
		List<CurrentPOddsPortal> proxPartidos = lNews.getListaPsHoyFuturo();
		List<CurrentPOddsPortal> resultPartidos = new ArrayList<CurrentPOddsPortal>();

		Boolean existeProximo = false;

		for (CurrentPOddsPortal p : proxPartidos) {
			if (empiezaEnProxMins(p)) {
				existeProximo = true;
			} else {
				existeProximo = false;
			}

			if (existeProximo) {
				resultPartidos.add(p);
			}
		}

		return resultPartidos;
	}

	public static boolean empiezaEnProxMins(CurrentPOddsPortal p) {

		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar momentoHasta = new GregorianCalendar();

		momentoHasta.add(GregorianCalendar.MINUTE, MINS_PROXIMIDAD);

		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
		 * System.out.println("  --------------->>>>>>>>>>> ");
		 * System.out.println("partidoFecha     " + sdf.format(
		 * p.getFechaGC().getTime() )); System.out.println("Momento Desde :: " +
		 * sdf.format(now.getTime()) + " ;   Momento Hasta :: " +
		 * sdf.format(momentoHasta.getTime()) ); System.out.println("C1 :: " +
		 * now.before(p.getFechaGC()) ); System.out.println("C2 :: " +
		 * p.getFechaGC().before(momentoHasta) ); System.out.println(
		 * "  --->>>>>>>>>>> ");
		 */

		if (now.before(p.getFechaGC()) && p.getFechaGC().before(momentoHasta)) {
			return true;
		}

		return false;
	}

	private static ListPartidosSerializable leerSerializadoOP() {
		ListPartidosSerializable result = null;

		File fichero = new File(RUTA_ARCHIVO + "/Test_Partidos_OP.srz");
		if (!fichero.exists()) {
			System.out.println("El archivo no existe... " + fichero.getAbsolutePath());
			return null;
		}

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));

			// Se lee el primer objeto
			Object aux;
			aux = ois.readObject();

			if (aux instanceof ListPartidosSerializable) {
				result = (ListPartidosSerializable) aux;
			}

			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	private static ListPartidosSerializablePyckio leerSerializadoP() {
		ListPartidosSerializablePyckio result = null;

		File fichero = new File(RUTA_ARCHIVO + "/Test_Partidos_P.srz");
		if (!fichero.exists()) {
			System.out.println("El archivo no existe... " + fichero.getAbsolutePath());
			return null;
		}

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));

			// Se lee el primer objeto
			Object aux;
			aux = ois.readObject();

			if (aux instanceof ListPartidosSerializablePyckio) {
				result = (ListPartidosSerializablePyckio) aux;
			}

			ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;

	}

	private static void guardarSerializadoOP(ListPartidosSerializable listSer) {
		File dir = new File(RUTA_ARCHIVO);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File fichero = new File(dir.getAbsolutePath() + "/Test_Partidos_OP.srz");
		if (fichero.exists()) {
			fichero.delete();
		}

		try {
			if (!fichero.exists()) {
				fichero.createNewFile();
				System.out.println("Archivo creado vacio exitosamente :: " + fichero.getAbsolutePath());
			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
			oos.writeObject(listSer);
			System.out.println(" Archivo creado exitosamente :: " + fichero.getAbsolutePath());

			oos.close();
			oos = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void guardarSerializadoP(ListPartidosSerializablePyckio listPIO) {
		File dir = new File(RUTA_ARCHIVO);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File fichero = new File(dir.getAbsolutePath() + "/Test_Partidos_P.srz");
		if (fichero.exists()) {
			fichero.delete();
		}

		try {
			if (!fichero.exists()) {
				fichero.createNewFile();
				System.out.println("Archivo creado vacio exitosamente :: " + fichero.getAbsolutePath());
			}

			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero));
			oos.writeObject(listPIO);
			System.out.println(" Archivo creado exitosamente :: " + fichero.getAbsolutePath());

			oos.close();
			oos = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
