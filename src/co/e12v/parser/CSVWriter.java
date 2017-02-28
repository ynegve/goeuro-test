package co.e12v.goeuro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CSVWriter {

	private char separator = ',';
	private char quoteCharacter = '"';
	private String csvFile;
	private static String header[] = { "_id", "name", "type", "longitude", "latitude" };

	public CSVWriter(String csvFile) {
		this.csvFile = csvFile;
	}

	public CSVWriter(char separator, char quoteCharacter, String csvFile) {
		this.separator = separator;
		this.quoteCharacter = quoteCharacter;
		this.csvFile = csvFile;
	}

	public void writeCsv(Iterable<List<Object>> source) throws Exception {
		Iterator<List<Object>> it = source.iterator();
		if (it != null) {
			final File file = new File(csvFile);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			try {
				writeLine(writer, getHeaderRow(), separator, quoteCharacter);
				while (it.hasNext()) {
					List<Object> row = (List<Object>) it.next();
					writeLine(writer, row, separator, quoteCharacter);
				}
			} finally {
				writer.close();
			}
		}
	}

	private static void writeLine(BufferedWriter writer, List<Object> row,
			char separator, char quoteCharacter) throws IOException {
		int i = 0;
		for (Object value : row) {
			if (i++ > 0)
				writer.write(separator);
			if (value != null)
				writeValue(writer, value.toString(), separator, quoteCharacter);
		}
		writer.write("\r\n");
	}

	private static void writeValue(BufferedWriter writer, String value,
			char separator, char quoteCharacter) throws IOException {
		int quoteIndex = value.indexOf(quoteCharacter);
		if (quoteIndex != -1) {
			writer.write(quoteCharacter);
			int lastQuoteIndex = 0;
			while (quoteIndex != -1) {
				writer.write(value.substring(lastQuoteIndex, quoteIndex + 1));
				lastQuoteIndex = quoteIndex;
				quoteIndex = value.indexOf(quoteCharacter, quoteIndex + 1);
			}
			writer.write(value.substring(lastQuoteIndex));
			writer.write(quoteCharacter);
		} else if (value.indexOf(separator) != -1 || value.indexOf('\r') != -1
				|| value.indexOf('\n') != -1) {
			writer.write(quoteCharacter);
			writer.write(value);
			writer.write(quoteCharacter);
		} else {
			writer.write(value);
		}
	}

	private static List<Object> getHeaderRow() {
		List<Object> headRow = new ArrayList<Object>();
		headRow.addAll(Arrays.asList(header));
		return headRow;
	}
}
