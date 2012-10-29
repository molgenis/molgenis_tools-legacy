package demultiplexer;

public class Helper {

	/**
	 * Remove barcode
	 * @param read
	 * @return
	 */
	public String removeBarcode(String[] read)
	{
//		    #
//		    # Remove the barcode nucleotides from the sequence.
//		    #
//		    read[2] = substr(read[2], barcode.length + 1, nchar(read[2]))
//		    #
//		    # Remove quality scores for the nucleotides of the barcode too.
//		    #
//		    read[4] = substr(read[4], barcode.length + 1, nchar(read[4]))   
//		    #
//		    # Return trimmed sequence read.
//		    #
//		    read
		
		read[1] = read[1].substring(read[1].length());
		return null;
	}
	
	

	
}
