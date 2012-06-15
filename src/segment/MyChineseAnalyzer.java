package segment;
import java.util.Set;
import java.io.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.util.Version;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class MyChineseAnalyzer extends Analyzer{
	private Set stopWords;
	//stop words list
	public static String[] STOP_WORDS = {
		"a","an","and","are","as","at","be","but","by",
		"for","if","in","into","is","it",
		"no","not","of","on","or","s","such",
		"t","that","the","their","then","there","these",
		"they","this","to","was","will","with","nbsp",
		"我","我们","的","了","在","是","我","有","和",
		"就","不","人","都","一","一个","上",
		"也","很","到","说","要","去","你",
		"会","着","没有","看","好","自己","这",
		"了","他","她","它","回复","留言","他们","它们","她们"
	};
	
	public MyChineseAnalyzer() {
		stopWords = StopFilter.makeStopSet(STOP_WORDS);
	}
	
	public String readerToString (Reader reader) throws IOException {
		BufferedReader buffer = new BufferedReader(reader);
		String a = null;
		String b = null;
		while  ( (a = buffer.readLine())!=null ) {
			b+=a;
		}
		return b;
	}
	
	public TokenStream tokenStream (String fieldName, Reader reader) {
		try {
			ICTCLAS50 splitWord = new ICTCLAS50();
			String argu = ".";
			if (splitWord.ICTCLAS_Init(argu.getBytes("UTF8")) == false) {
				System.out.println("Init My Chinese Analyzer Fails!");
				return null;
			}
			String sInput = readerToString(reader);
			byte nativeBytes[] = splitWord.ICTCLAS_ParagraphProcess(sInput.getBytes("utf-8"), 0, 0);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf-8");
			/*
			String[] termList = nativeStr.trim().split(" ");
			String retStr = null;
			
			for ( int i=0 ; i<termList.length ; i++ ) {
				String[] ab = termList[i].split("/");
				if ( ab[1].contains("n")) {
					//System.out.printf("%s %s %s\n", termList[i],ab[0],ab[1]);
					if ( retStr == null )
						retStr = ab[0];
					else 
						retStr += " "+ab[0];
				}
			}
			*/
			//System.out.println(nativeStr);
			splitWord.ICTCLAS_Exit();
			
			return new StopFilter(Version.LUCENE_33,new LowerCaseTokenizer(new StringReader(nativeStr)),stopWords);

		}
		catch (Exception e) {
			System.out.println("Translate error");
			return null;
		}
	}
	
}
