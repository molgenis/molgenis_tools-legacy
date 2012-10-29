/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package umcg.genetica.math.stats;

/**
 *
 * @author harmjan
 */
public class Correlation {

    public static double[][] m_correlationToZScore;

    public static void correlationToZScore(int maxNrSamples) {
	//Fast look-up service to determine P-Value for individual Spearman correlation coefficient, given sample size:
	double[][] correlationToZScore = new double[maxNrSamples + 1][2001];

	cern.jet.random.engine.RandomEngine randomEngine = new cern.jet.random.engine.DRand();

	for (int nrSamples = 0; nrSamples <= maxNrSamples; nrSamples++) {
	    if (nrSamples < 3) {
		for (int sc = 0; sc <= 2000; sc++) {
		    correlationToZScore[nrSamples][sc] = 0;
		}
	    } else {
		cern.jet.random.StudentT tDistColt = new cern.jet.random.StudentT(nrSamples - 2, randomEngine);
		//JSci.maths.statistics.TDistribution tDist = new JSci.maths.statistics.TDistribution(nrSamples - 2);
		for (int s = 0; s <= 2000; s++) {

		    //Determine Spearman correlation R:
		    double spearman = (double) (s - 1000) / 1000d;
		    if (spearman == -1) {
			spearman = -0.9999;
		    }
		    if (spearman == +1) {
			spearman = +0.9999;
		    }

		    //Calculate T score:
		    double t = spearman / (Math.sqrt((1 - spearman * spearman) / (nrSamples - 2)));

		    //Look up P value, avoid complementation, due to Double inaccuracies.
		    //And lookup Z score, and avoid complementation here as well.
		    double pValueColt = 1;
		    double zScoreColt = 0;
		    if (t < 0) {
			pValueColt = tDistColt.cdf(t);
			if (pValueColt < 2.0E-323) {
			    pValueColt = 2.0E-323;
			}
			zScoreColt = cern.jet.stat.Probability.normalInverse(pValueColt);
			//zScoreColt = normDist.inverse(pValueColt);
		    } else {
			pValueColt = tDistColt.cdf(-t);
			if (pValueColt < 2.0E-323) {
			    pValueColt = 2.0E-323;
			}
			zScoreColt = -cern.jet.stat.Probability.normalInverse(pValueColt);
			//zScoreColt = -normDist.inverse(pValueColt);
		    }


		    //Store Z score:
		    correlationToZScore[nrSamples][s] = zScoreColt;
		}
	    }
	}

	m_correlationToZScore = correlationToZScore;

    }

    public static double correlate(double[] x, double[] y, double varX, double varY) {
	double denominator = Math.sqrt(varX * varY);
	double covarianceInterim = 0;
	for (int i = 0; i < x.length; i++) {
	    covarianceInterim += x[i] * y[i];
	}
	double covariance = covarianceInterim / (x.length - 1);
	double correlation = covariance / denominator;
	return correlation;
    }

    public static double correlate(double[] x, double[] y) {
	double meanX = Descriptives.mean(x);

	double varX = Descriptives.variance(x, meanX);

	double meanY = Descriptives.mean(y);
	double varY = Descriptives.variance(y, meanY);
	for (int i = 0; i < x.length; i++) {
	    x[i] -= meanX;
	    y[i] -= meanY;
	}
	return correlate(x, y, varX, varY);
    }

    public static double convertCorrelationToZScore(int length, double correlation) {
	int correlationIndex = (int) Math.round(((correlation + 1.0d) * 1000d));

	if (m_correlationToZScore == null || m_correlationToZScore.length < length) {
	    System.out.println("ERROR: correlation to z-score table not correctly initialized. The table has " + m_correlationToZScore.length + " entries, while " + length + " requested.");
	    System.exit(-1);
	}
	if (correlationIndex > m_correlationToZScore[length].length - 1) {
	    System.err.println("ERROR! correlation: " + correlation + " does not fit Z-score table for " + m_correlationToZScore[length] + " samples (length is: " + m_correlationToZScore[length].length + ")");
	}

	return m_correlationToZScore[length][correlationIndex];
    }
}
