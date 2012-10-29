package demultiplexer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Run {

	enum ThreadingMode {
		ST, MP
	}
	
	enum LogLevel
	{
		FINEST, FINER, FINE, DEBUG, INFO, WARNING, ERROR, CRITICAL
	}
	
	/**
	 * 
	 * Work in progress!
	 * 
	 * See:
	 * http://www.bbmriwiki.nl/svn/ngs_scripts/trunk/demultiplex.R
	 */
	public static void main(String[] args) {

		Options options = new Options();
		
		options.addOption(OptionBuilder.withArgName("list of values").hasArgs().isRequired().withValueSeparator(',').withDescription("List of nucleotide barcodes.").create("bcs"));
		options.addOption(OptionBuilder.withArgName("integer").hasArgs().withDescription("Maximum number of allowed mismatches in the barcode (defaults to auto detection of max mismatches allowed for unambiguous detection of barcodes).").create("mms"));
		options.addOption(OptionBuilder.withDescription("Force maximum number of allowed mismatches in the barcode. Overrules check for max mismatches allowed to prevent unambiguous detection of barcodes.Use only for reads with lot's of bad quality base calls in the barcodes and when you know what you are doing!").create("force"));
		options.addOption(OptionBuilder.withArgName("file").hasArg().isRequired().withDescription("Required input FastQ file containing MultiPlexed sequence Reads.These are either the only reads in case of single read sequencing or all reads from one end of a paired end or mate pair library.File may be compressed with gzip.").create("mpr1"));
		options.addOption(OptionBuilder.withArgName("file").hasArgs().withDescription("Optional input FastQ file containing MultiPlexed sequence Reads.These are all reads from the other end of a paired end or mate pair library.Note: --mpr[1|2] FastQ files maybe in supplied in gzipped format if the filename extension is *.fq.File may be compressed with gzip.").create("mpr2"));
		options.addOption(OptionBuilder.withDescription("Disable read ID consistency check for paired end or mate pair libraries.Do not throw an error when the sequence read IDs for both ends of a sequence pair are not identical.").create("nocheck"));
		options.addOption(OptionBuilder.withArgName("list of files").hasArgs().isRequired().withValueSeparator(',').withDescription("Required list of gzipped FastQ output file names for the DeMultiplexed sequence Reads.These will contain either the only reads in case of single read sequencing or all reads from one end of a paired end or mate pair library. Files will be compressed with gzip.").create("dmr1"));
		options.addOption(OptionBuilder.withArgName("list of files").hasArgs().withValueSeparator(',').withDescription("Optional list of gzipped FastQ output file names for the DeMultiplexed sequence Reads.These will contain all reads from the other end of a paired end or mate pair library.Note: the order of the --dmr[1|2] output file names is important and shpould match the order of the barcodes supplied with --bcs.Files will be compressed with gzip.").create("dmr2"));
		options.addOption(OptionBuilder.withArgName("file").hasArgs().isRequired().withDescription("Required gzipped FastQ output file name for the UnKnown sequence Reads in which none of the supplied barcodes was detected.These are either the only UnKnown Reads in case of single read sequencing or all UnKnown Reads from one end of a paired end or mate pair library.File will be compressed with gzip.").create("ukr1"));
		options.addOption(OptionBuilder.withArgName("file").hasArgs().withDescription("Optional gzipped FastQ output file name for the UnKnown sequence Reads in which none of the supplied barcodes was detected.These are all UnKnown Reads from the other end of a paired end or mate pair library.File will be compressed with gzip.").create("ukr2"));
		options.addOption(OptionBuilder.withArgName("ST or MP").hasArgs().withDescription("Threading mode: ST, MP (default).ST: Single Thread. Decompression (in case the input was compressed), demultiplexing and compression of the output is all done in R on a single CPU/core.MP: Multiple Pipes. In addition to a thread for demultiplexing in R, additional threads (one per file) are created.Decompressing input and compressing output is handled outside R by gzip and uncompressed data is streamed through a pipe resulting in additional threads.").create("tm"));
		options.addOption(OptionBuilder.withArgName("value").hasArgs().withDescription("Log level (optional). One of FINEST, FINER, FINE, DEBUG, INFO (default), WARNING, ERROR or CRITICAL.").create("ll"));

		List<String> bcs = new ArrayList<String>();
		Integer mms;
		boolean force = false;
		File mpr1;
		File mpr2;
		boolean nocheck = false;
		List<File> dmr1;
		List<File> dmr2;
		File ukr;
		File ukr2;
		ThreadingMode tm = ThreadingMode.MP;
		LogLevel ll = LogLevel.INFO;
		
		
		
		
		
		// create the parser
	    CommandLineParser parser = new GnuParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse( options, args );
	        
	     // has the buildfile argument been passed?
	        if( line.hasOption( "force" ) ) {
	        	force = true;
	        }
	        
	        
	       for(Option o : line.getOptions())
	       {
	    	   System.out.println(o.getOpt() + " VALUE: " + printStringArr(line.getOptionValues(o.getOpt())));
	       }
	    }
	    catch( ParseException exp ) {
	        // oops, something went wrong
	        System.err.println( "Bad arguments:  " + exp.getMessage() );
	    }
		
	    
	    // automatically generate the help statement
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp( "demultiplexer", options , true);
	    System.out.println("example:\njava -jar demultiplexer.jar -Xmx1g -bcs AGAGAT,TAATTT,TCAGTT,TGACTT -mpr1 readOneFromPair.fq.gz -mpr2 readTwoFromPair.fq.gz -dmr1 readOneFromPair_AGAGAT.fq.gz,readOneFromPair_TAATTT.fq.gz,readOneFromPair_TCAGTT.fq.gz,readOneFromPair_TGACTT.fq.gz -dmr2 readTwoFromPair_AGAGAT.fq.gz,readTwoFromPair_TAATTT.fq.gz,readTwoFromPair_TCAGTT.fq.gz,readTwoFromPair_TGACTT.fq.gz -ukr1 readOneFromPair_UNKNOWN.fq.gz -ukr2 readTwoFromPair_UNKNOWN.fq.gz -ll WARNING");
	}
	
	static String printStringArr(String[] arr)
	{
		String r = "";
		for(String s : arr)
		{
			r += s + " ";
		}
		return r;
	}

}
