package main;

import output.Draw;
import output.ExcelWriter;
import output.TextWriter;
import main.fields.Anisotropy;
import main.fields.Circular;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

public class Launcher {

	public static void main(String...strings) {
		ZipUtils.saveCodeInHistory();

//		double theta = Math.PI / 4;
//		double phi = 0;
//		double h = 0.1;
//		double w = 1;
//
//		//Создание экземпляра калькулятора и заполнение его полями
//		CartesianCalculation c = new CartesianCalculation(
//				new Anisotropy(theta, phi),
//				new Circular(w, h)
//		);
//
//		//Запуск расчета
//		c.run(300, 400);
//
//		//Рисование трека
//		new Draw(c.getArray(), new Vector(theta, phi), 0.4 * Math.PI, 0.4 * Math.PI, 0, "PNG Coordinates").drawTraectory(true);
//
//		//Запись в текстовые файлы
//		TextWriter.writeTraectorysCoordinates(c.getArray(), "TXT Coordinates");
//
//		//Запись в эксель
//		ExcelWriter ew = new ExcelWriter();
//		ew.addVectorList("Track", c.getArray().subList(0, 65500));//В массиве должно быть меньше 65535 значений
//		ew.write("Excel coordinates");
//
//		//Вывод данных непосредственно в консоль
//		System.out.println("Energy = " + c.getEnergy());
//		System.out.println("Average X = " + c.getM_aver().getX());
//		System.out.println("Average Y = " + c.getM_aver().getY());
//		System.out.println("Average Z = " + c.getM_aver().getZ());



//		double theta = 0;
//		double phi = 0;
//		double h = 0.2;
//
//		ArrayList<Double> list = new ArrayList<>();
//
//		for (double w = 0.1; w <= 2; w = round(w + 0.1, 1)) {
//			System.out.println("w = " + w);
//			CartesianCalculation c = new CartesianCalculation(
//				new Anisotropy(theta, phi),
//				new Circular(w, h)
//			);
//			c.run();
//			list.add(c.getEnergy());
//		}
//		TextWriter.writeDoubleList(list, "TXT Coordinates");


		double angleStep = 0.05;
		double fi = 0;
		double h = 0.2;
		ArrayList<ArrayList<Double>> list_all = new ArrayList<>();
		//double teta = 0;
		//for (double fi = angleStep; fi < 1; fi = round(fi + angleStep, 2))
			for (double teta = angleStep; teta < 1; teta = round(teta + angleStep, 2)) {
				double angleTheta = Math.acos(2 * teta - 1);
				double angleFi = 2 * Math.PI * fi;
				ArrayList<Double> energy_list = new ArrayList<>();

				System.out.println(new Date());
				System.out.println(Calculation.IS_ALPHA +
						"" + Calculation.IS_SIGMA +
						"" + Calculation.IS_NU);
				System.out.println("teta="+teta);
				System.out.println();

				for (double w = 0.01; w <= 3; w = round(w + 0.01, 2)) {
					CartesianCalculation c = new CartesianCalculation(
							new Anisotropy(angleTheta, angleFi),
							new Circular(w, h)
					);
					c.run();
					energy_list.add(c.getEnergy());
				}
				list_all.add(energy_list);
			}

		ArrayList<Double> energy_list = new ArrayList<>();
		for (int i = 0; i < 310; i++)
			energy_list.add(0d);


		for (ArrayList<Double> list : list_all) {
			int i = 0;
			for (double e : list) {
				energy_list.set(i, energy_list.get(i) + e);
				i++;
			}
		}
		TextWriter.writeDoubleList(energy_list,
				"(" + Calculation.IS_ALPHA +
						"" + Calculation.IS_SIGMA +
						"" + Calculation.IS_NU +
						")final");
	}


	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
