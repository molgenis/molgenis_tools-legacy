package csvtobin.sources;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class VerifyCsv
{
	private static int maxStringLength = 127;
	
	public static int[] verify(File inputFile, String valueType) throws Exception
	{
//		try
//		{
			int[] dims = verifyAndGetRowColDimensions(inputFile);

			verifyHeaderUniqueness(inputFile);

			verifyHeaderNamesAllowed(inputFile);

			verifyHeaderLenghtsAllowed(inputFile);

			if (valueType.equals("Decimal"))
			{
				verifyDoubleValueType(inputFile);
			}
			else
			{
				verifyTextElementLenghtsAllowed(inputFile);
			}
			return dims;
//		}
//		catch (Exception e)
//		{
//			throw new VerifyCsvException(e.getMessage());
//		}
	}
	
	private static int[] verifyAndGetRowColDimensions(File inputFile) throws Exception
	{
		int[] rowAndColLength = new int[2];

		// final IntegerWrapper elementLength = new IntegerWrapper(0);

		final IntegerWrapper nrOfCols = new IntegerWrapper(-1);
		final IntegerWrapper nrOfRows = new IntegerWrapper(0);

		new CsvFileReader(inputFile).parse(new CsvReaderListener()
		{
			public void handleLine(int line_number, Tuple line) throws Exception
			{
				if (line_number != 0)
				{
					if (nrOfCols.get() == -1)
					{
						nrOfCols.set(line.size() - 1);
					}
					else
					{
						if (nrOfCols.get() != line.size() - 1)
						{
							throw new ParseException("Number of columns unequal. Expected " + nrOfCols.get()
									+ " but found " + (line.size() - 1) + " at line " + line_number, line_number);
						}
						// else: continue
					}
					nrOfRows.set(nrOfRows.get() + 1);
				}
			}
		});

		rowAndColLength[0] = nrOfRows.get();
		rowAndColLength[1] = nrOfCols.get();
		
		if(rowAndColLength[0] < 1){
			throw new Exception("Number of rows must be greater than 0. Found: " + rowAndColLength[0]);
		}
		if(rowAndColLength[1] < 1){
			throw new Exception("Number of columns must be greater than 0. Found: " + rowAndColLength[1]);
		}

		return rowAndColLength;
	}

	private static void verifyDoubleValueType(File inputFile) throws Exception
	{
		new CsvFileReader(inputFile).parse(new CsvReaderListener()
		{
			public void handleLine(int line_number, Tuple line) throws Exception
			{
				if (line_number != 0)
				{
					for (int i = 1; i < line.size(); i++)
					{
						line.getDouble(i);
					}
				}
			}
		});
	}
	
	private static void verifyTextElementLenghtsAllowed(File inputFile) throws Exception
	{
		new CsvFileReader(inputFile).parse(new CsvReaderListener()
		{
			public void handleLine(int line_number, Tuple line) throws Exception
			{
				if (line_number != 0)
				{
					for (int i = 1; i < line.size(); i++)
					{
						if(line.getString(i) != null && line.getString(i).length() > maxStringLength){
							throw new Exception("Text element bigger than "+maxStringLength+" characters: " + line.getString(i));
						}
						
					}
				}
			}
		});	
	}
	
	private static void verifyHeaderLenghtsAllowed(File inputFile) throws Exception
	{
		CsvFileReader csvFile = new CsvFileReader(inputFile);
		List<String> colNames = csvFile.colnames();
		List<String> rowNames = csvFile.rownames();
		for (String colName : colNames.subList(1, colNames.size()))
		{
			if(colName.length() > maxStringLength){
				throw new Exception("Column header bigger than "+maxStringLength+" characters: " + colName);
			}
		}
		for (String rowName : rowNames)
		{
			if(rowName.length() > maxStringLength){
				throw new Exception("Row header bigger than "+maxStringLength+" characters: " + rowName);
			}
		}	
	}

	private static void verifyHeaderUniqueness(File inputFile) throws Exception
	{
		
		//TODO: it is now allowed to have two different types of entities with the same name
		//this is okay: Individual x Individual, refs to ind1 x ind1
		//this is not okay: Individual x Marker, refs to bla1 x bla1, since the names are unique

		CsvFileReader csvFile = new CsvFileReader(inputFile);
		List<String> colNames = csvFile.colnames();
		List<String> rowNames = csvFile.rownames();

		List<String> uniqueColNames = new ArrayList<String>();
		List<String> uniqueRowNames = new ArrayList<String>();

		for (String colName : colNames.subList(1, colNames.size()))
		{
			if (!uniqueColNames.contains(colName))
			{
				uniqueColNames.add(colName);
			}
			else
			{
				throw new Exception("Duplicate column header: " + colName);
			}
		}
		for (String rowName : rowNames)
		{
			if (!uniqueRowNames.contains(rowName))
			{
				uniqueRowNames.add(rowName);
			}
			else
			{
				throw new Exception("Duplicate row header: " + rowName);
			}
		}

	}

	private static void verifyHeaderNamesAllowed(File inputFile) throws Exception {
		CsvFileReader csvFile = new CsvFileReader(inputFile);
		List<String> colNames = csvFile.colnames();
		List<String> rowNames = csvFile.rownames();

		for (String colName : colNames.subList(1, colNames.size()))
		{
			//FIXME: the strict should only be applied when application is an XGAP
			NameConvention.validateEntityNameStrict(colName);
		}
		for (String rowName : rowNames)
		{
			//FIXME: the strict should only be applied when application is an XGAP
			NameConvention.validateEntityNameStrict(rowName);
		}	
	}
}