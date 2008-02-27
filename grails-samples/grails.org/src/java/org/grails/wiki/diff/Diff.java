package org.grails.wiki.diff;

import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * @author Graeme Rocher
 */
public class Diff {

    public static String diff(String first, String second) {

        // read in lines of each file
        String[] x = first.split("\\n");
        String[] y = second.split("\\n");

        // number of lines of each file
        int M = x.length;
        int N = y.length;

        // opt[i][j] = length of LCS of x[i..M] and y[j..N]
        int[][] opt = new int[M+1][N+1];

        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (x[i].equals(y[j]))
                    opt[i][j] = opt[i+1][j+1] + 1;
                else
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
            }
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        // recover LCS itself and print out non-matching lines to standard output
        int i = 0, j = 0;
        while(i < M && j < N) {
            if (x[i].equals(y[j])) {
                i++;
                j++;
            }
            else if (opt[i+1][j] >= opt[i][j+1]) pw.println("+ " + x[i++]);
            else                                 pw.println("- " + y[j++]);
        }

        // dump out one remainder of one string if the other is exhausted
        while(i < M || j < N) {
            if      (i == M) pw.println("+ " + y[j++]);
            else if (j == N) pw.println("- " + x[i++]);
        }

        return sw.toString();
    }

}