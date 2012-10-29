package demultiplexer;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Run.main(new String[]{"--bcs 'AGAGAT,TAATTT,TCAGTT,TGACTT' --mpr1 readOneFromPair.fq.gz --mpr2 readTwoFromPair.fq.gz --dmr1 'readOneFromPair_AGAGAT.fq.gz,readOneFromPair_TAATTT.fq.gz,readOneFromPair_TCAGTT.fq.gz,readOneFromPair_TGACTT.fq.gz' --dmr2 'readTwoFromPair_AGAGAT.fq.gz,readTwoFromPair_TAATTT.fq.gz,readTwoFromPair_TCAGTT.fq.gz,readTwoFromPair_TGACTT.fq.gz' --ukr1 readOneFromPair_UNKNOWN.fq.gz --ukr2 readTwoFromPair_UNKNOWN.fq.gz --ll WARNING"});

		Run.main(new String[]{"-bcs AGAGAT,TAATTT,TCAGTT,TGACTT -mpr1 readOneFromPair.fq.gz -mpr2 readTwoFromPair.fq.gz -dmr1 readOneFromPair_AGAGAT.fq.gz,readOneFromPair_TAATTT.fq.gz,readOneFromPair_TCAGTT.fq.gz,readOneFromPair_TGACTT.fq.gz -dmr2 readTwoFromPair_AGAGAT.fq.gz,readTwoFromPair_TAATTT.fq.gz,readTwoFromPair_TCAGTT.fq.gz,readTwoFromPair_TGACTT.fq.gz -ukr1 readOneFromPair_UNKNOWN.fq.gz -ukr2 readTwoFromPair_UNKNOWN.fq.gz -ll WARNING"});
	}

}
