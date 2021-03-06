/*
 * WGAFileMatrixGenotype.java
 *
 * Created on July 9, 2007, 5:08 PM
 *
 */

package umcg.genetica.io.trityper;

import java.io.*;

/**
 *
 * @author ludefranke
 */
public class WGAFileMatrixImputedDosage {
    
    public int nrSNPs = 0;
    public int nrInds = 0;
    private java.io.File fileName = null;
    private RandomAccessFile file = null;
    
    /**
     * Creates a new instance of WGAFileMatrixGenotype
     */
    public WGAFileMatrixImputedDosage(int nrSNPs, int nrInds, File fileName, boolean readOnly) {
        this.nrSNPs = nrSNPs;
        this.nrInds = nrInds;
        this.fileName = fileName;
        try {
            if (readOnly) {
                file = new RandomAccessFile(fileName, "r");
            } else {
                file = new RandomAccessFile(fileName, "rw");
            }
        } catch (IOException e) {
            System.out.println("Cannot get hold of random access file: '" + fileName.getAbsoluteFile() + "': " + e.getMessage());
            e.printStackTrace();
        }       
        long fileSize = (long) 1 * nrSNPs * (long) nrInds;
        if (!readOnly) {
            try {
                if (file.length()!=fileSize) {
                    //Generate file with the size, such that this is appropriate:
                    file.setLength(fileSize);
                    file.seek(0);
                    byte byteString[] = new byte[1000]; for (int g=0; g<1000; g++) byteString[g] = 0;
                    for (long x=0; x<fileSize - 1000; x+=1000) {
                        file.write(byteString);
                    }
                    long remainder = fileSize % 1000;
                    byte byteSingle[] = new byte[1]; byteSingle[0] = 0;
                    for (long x=0; x<remainder; x++) {
                        file.write(byteSingle);
                    }
                    System.out.println("Size matrix:\t" + fileSize + "\tFile size:\t" + file.length());
                }
            } catch (IOException e) {
                System.out.println("Cannot get hold of random access file: '" + fileName.getAbsoluteFile() + "': " + e.getMessage());
                e.printStackTrace();
            }       
        }        
    }
    
    private long getElement(int snp, int ind) {
        return 1 * (long) snp * (long) nrInds + (long) ind;
    }
    
    public void close() {
        try {
            file.close();
        } catch (IOException e) {
            System.out.println("Cannot close random access file: '" + fileName.getAbsoluteFile() + "': " + e.getMessage());
            e.printStackTrace();
        }        
    }
    
    public byte getDosage(int snp, int ind) {
        try {
            file.seek(getElement(snp, ind));
            return file.readByte();
        } catch (IOException e) {
            System.out.println("Cannot get element: " + e.getMessage());
        }  
        return 0;
    }

    public void setDosage(int snp, int ind, byte[] value) {
        try {
            file.seek(getElement(snp, ind));
            file.write(value);
        } catch (IOException e) {
            System.out.println("Cannot set element: " + e.getMessage());
        } 
    }

}
