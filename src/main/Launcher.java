package main;

import output.Draw;
import output.ExcelWriter;
import output.TextWriter;
import main.fields.Anisotropy;
import main.fields.Circular;

public class Launcher {

	public static void main(String...strings) {

		double theta = Math.PI / 4;
		double phi = 0;
		double h = 0.1;
		double w = 1;

		//Создание экземпляра калькулятора и заполнение его полями
		CartesianCalculation c = new CartesianCalculation(
				new Anisotropy(theta, phi),
				new Circular(w, h)
		);

		//Запуск расчета
		c.run(300, 400);

		//Рисование трека
		new Draw(c.getArray(), new Vector(theta, phi), 0.4 * Math.PI, 0.4 * Math.PI, 0, "PNG Coordinates").drawTraectory(true);

		//Запись в текстовые файлы
		TextWriter.writeTraectorysCoordinates(c.getArray(), "TXT Coordinates");

		//Запись в эксель
		ExcelWriter ew = new ExcelWriter();
		ew.addVectorList("Track", c.getArray().subList(0, 65500));//В массиве должно быть меньше 65535 значений
		ew.write("Excel coordinates");

		//Вывод данных непосредственно в консоль
		System.out.println("Energy = " + c.getEnergy());
		System.out.println("Average X = " + c.getM_aver().getX());
		System.out.println("Average Y = " + c.getM_aver().getY());
		System.out.println("Average Z = " + c.getM_aver().getZ());

	}

}
