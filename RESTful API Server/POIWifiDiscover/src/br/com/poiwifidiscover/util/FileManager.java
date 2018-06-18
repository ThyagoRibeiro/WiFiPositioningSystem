package br.com.poiwifidiscover.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

	public static String readString(String filePath) {

		try {
			BufferedReader buffRead = new BufferedReader(new FileReader(filePath));

			String line = "";
			String content = "";
			while (true) {
				if (line != null) {
					content += line;

				} else
					break;
				line = buffRead.readLine();
			}
			buffRead.close();
			return content;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void writeString(String filePath, String content, boolean append) {
		try {
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(filePath, append));
			buffWrite.append(content);
			buffWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}