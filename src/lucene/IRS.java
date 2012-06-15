package lucene; 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader; 
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader; 
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.Date; 
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document; 
import org.apache.lucene.document.Field; 
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;

import org.apache.lucene.util.Version;

import ICTCLAS.I3S.AC.ICTCLAS50;

import segment.MyChineseAnalyzer;
/** 
* This class demonstrate the process of creating index with Lucene 
* for text files 
*/ 
public class IRS {
	static int MAXTERM = 10;
	static String file_in = "in";
	static String file_out = "out";
	static Version matchVersion = Version.LUCENE_36;
	static String rootPath = "D:\\IRS\\";
	static String indexPath = rootPath+"index";
	static String filePath =  rootPath+"files\\parsed";
	//static Analyzer analyzer = new SmartChineseAnalyzer(matchVersion);
	static Analyzer analyzer = new MyChineseAnalyzer();
	 
	// Returns the contents of the file in a byte array.
	public static String getFileString(File file) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		String content = null;
		String line = null;
		while ( (line=br.readLine())!=null ) {
			content+=line+"\n";
		}
		//System.out.println(content);
		return content;
	}

	
	public static void CreateIndex () throws Exception {
		System.out.println(indexPath);
		System.out.println(filePath);
		Directory dir = FSDirectory.open(new File(indexPath));
		File dataDir = new File(filePath);
		File[] dataFiles = dataDir.listFiles();
		
		for ( int i=0 ; i<dataFiles.length; i++ ) {
			if ( dataFiles[i].isFile() && dataFiles[i].getName().endsWith(".txt") && 
					dataFiles[i].getName().compareTo("080cb19584deba83-dbb908a255a73ab0.txt") == 0 ||
					dataFiles[i].getName().compareTo("10915ce157cd3a32-75f2acb909810bc0.txt") == 0 ||
					dataFiles[i].getName().compareTo("173e42910f4f24c1-bf8e056905f04e60.txt") == 0 ||
					dataFiles[i].getName().compareTo("1ff8d6aa4ee4d6ba-9e3babeae8a76980.txt") == 0 ||
					dataFiles[i].getName().compareTo("283ba8cc3c80964f-ec544679165c4ed0.txt") == 0 ||
					dataFiles[i].getName().compareTo("31300e474d636d0f-c9d21245fd3b7e10.txt") == 0 ||
					dataFiles[i].getName().compareTo("3aa87749d3cd0854-74200988ba5525d0.txt") == 0 ||
					dataFiles[i].getName().compareTo("43ebfabcc16be759-11c769f189104e10.txt") == 0 ||
					dataFiles[i].getName().compareTo("4e9c0677fa262510-7d3eca0a24e9c1b0.txt") == 0 ||
					dataFiles[i].getName().compareTo("57444231381c25de-05454725c2ee9a80.txt") == 0 ||
					dataFiles[i].getName().compareTo("602b4161054cfd49-3b6cfcc99d3ac1f0.txt") == 0 ||
					dataFiles[i].getName().compareTo("69189463b5d49e24-88e8fe9c9585ce80.txt") == 0 ||
					dataFiles[i].getName().compareTo("7173fca8a4aa6c4c-c6adeb5fb3b11620.txt") == 0 ||
					dataFiles[i].getName().compareTo("7a87e80bd30980a2-22e249f276d65d40.txt") == 0 ||
					dataFiles[i].getName().compareTo("83b7a6ffab692f01-c4263cb58ee2f830.txt") == 0 ||
					dataFiles[i].getName().compareTo("8d04a2124d78e17d-add3a79781cfe790.txt") == 0 ||
					dataFiles[i].getName().compareTo("94652be550a1699f-4131999261409cb0.txt") == 0 ||
					dataFiles[i].getName().compareTo("9e3a1cd7d8621c25-18f9ff642c6e0d20.txt") == 0 ||
					dataFiles[i].getName().compareTo("b4da6d8df10f6803-2e137189a1acd500.txt") == 0 ||
					dataFiles[i].getName().compareTo("ca6c0d2c4e436594-f515d9a362314a50.txt") == 0 ||
					dataFiles[i].getName().compareTo("e1088b7a43ed6378-a4a2192e0d569800.txt") == 0 ||
					dataFiles[i].getName().compareTo("f7ded0bdb41a9d38-b04279cd2b9a62a0.txt") == 0){
				IndexWriterConfig iwc = new IndexWriterConfig(matchVersion,analyzer);
				//iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);//always re-create;
				IndexWriter iw = new IndexWriter(dir, iwc);
				Document doc = new Document();
				String docPath = new String(new File(dataFiles[i].getAbsoluteFile().toString()).getName().getBytes(),"UTF-8");
				//System.out.println("\n"+docPath);
				Field pathField = new Field("path",docPath,Store.YES,Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
				String content = getFileString(new File(dataFiles[i].getAbsoluteFile().toString()));
				//System.out.println(content+"\n");
				String title = content.substring(content.indexOf("<title>")+"<title>".length(),content.indexOf("<body>")-1);
				//System.out.println("title: "+title);
				Field titleField = new Field("title",title,Store.YES,Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
				String body = content.substring(content.indexOf("<body>")+"<body>".length());
				//System.out.println("body:" +body);
				Field bodyField = new Field("body",body,Store.YES,Index.ANALYZED,Field.TermVector.WITH_POSITIONS_OFFSETS);
				doc.add(pathField);
				doc.add(titleField);
				doc.add(bodyField);
				iw.addDocument(doc);
				iw.close();
			}
		}
	}
	public static boolean isNoun ( String term ) throws Exception {
		
		ICTCLAS50 splitWord = new ICTCLAS50();
		splitWord.ICTCLAS_Init(".".getBytes("UTF8"));
		byte nativeBytes[] = splitWord.ICTCLAS_ParagraphProcess(term.getBytes("utf-8"), 0, 1);
		String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf-8");
		//System.out.println(nativeStr);
		splitWord.ICTCLAS_Exit();
		String[] parts = nativeStr.trim().split("/");
		if ( parts[1].contains("n"))
			return true;
		return false;
	}
	public static Double GetOffsetScore ( TermVectorOffsetInfo[] query_offsets, 
										  int[] query_pos,
										  TermVectorOffsetInfo[] term_offsets, 
										  int[] term_pos ) {
		double offset_score = 0;
		for ( int p = 0 ; p<term_pos.length ; p++ ) {
			int min_offset = Integer.MAX_VALUE;
			for ( int k = 0 ; k<query_pos.length ; k++ ) {
				int t;
				if ( query_offsets[k].getEndOffset() < term_offsets[p].getStartOffset() ) {
					t = term_offsets[p].getStartOffset() - query_offsets[k].getEndOffset();
				}
				else {
					t = query_offsets[k].getStartOffset() - term_offsets[p].getEndOffset();
				}
				if ( t<min_offset ) min_offset = t;
			}
			offset_score+=1.0/(double)min_offset;
		}
		return offset_score/term_pos.length;
	}
	public static String Search (String queryString) throws Exception {
		String ret = null;
		Directory dir = FSDirectory.open(new File(indexPath),null);
		IndexReader reader = IndexReader.open(dir);
		IndexSearcher is = new IndexSearcher(reader);
		String[] fields={"path","title","body"};
		
		Map<String,Integer> mpTF=new TreeMap<String, Integer>();
		Map<String,Double> mpTFIDF=new HashMap<String, Double>();
		
		
		Map<String,Double> mpTermOffset = new TreeMap<String, Double>();
		Map<String,Double> mpScore = new HashMap<String, Double>();
		ValueComparator bvc =  new ValueComparator(mpScore);
		Map<String,Double> sorted_mpScore = new TreeMap(bvc);
		Map<String,Integer> mp_termDoc = new HashMap<String,Integer>();
		//System.out.println("Total documents: "+is.maxDoc());
		//System.out.print("Query:");
		
		
		
		QueryParser qp = new MultiFieldQueryParser(matchVersion,fields,analyzer);
		Query query = qp.parse(queryString);
		TopDocs tDocs = is.search(query,10);
		
		//High lighter
		/*
		BoldFormatter formatter = new BoldFormatter();
		Scorer fragmentScorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter,fragmentScorer);
		Fragmenter fragmenter = new SimpleFragmenter (50); //highlight range
		highlighter.setTextFragmenter(fragmenter);
		*/
			
		//int numTotalHits = tDocs.totalHits;
		//System.out.println("Totally hits "+numTotalHits+" results");
		//System.out.println(tDocs.scoreDocs.length);
		
		for ( int i=0 ; i<tDocs.scoreDocs.length ; i++ ) {
			int docId = tDocs.scoreDocs[i].doc;
			Document doc = is.doc(docId);
			//System.out.println(doc.get("path"));
			//String title = doc.get("title");
			//String body = doc.get("body");
			//System.out.print(body);
			//int maxNumFragmentsRequired = 5;
			//String fragmentSeparator = "...";
			//String hc = highlighter.getBestFragment(paoAnalyzer,"content",content);
			//System.out.println("hc:"+hc);
			//if (hc==null) {
			//	hc = content.substring(0,Math.min(50,content.length()));
			//}
			
			//Field contentField=(Field) doc.getFieldable("content");
			//contentField.setValue(hc);
			Map<String,Integer> temp_mp = new HashMap<String,Integer>();
		
			{//histogram for title
				TermPositionVector tpv = (TermPositionVector) reader.getTermFreqVector(docId, "title");
				TermVectorOffsetInfo[] query_offsets = tpv.getOffsets(tpv.indexOf(queryString));
				int[] query_pos = tpv.getTermPositions(tpv.indexOf(queryString));
				String[] terms = tpv.getTerms();
				int[] termFreqs = tpv.getTermFrequencies();
				
				for ( int j=0 ; j<terms.length ; j++ ) {
					//if ( !isNoun(terms[j].trim())) continue;
					//System.out.println(terms[j]);
					temp_mp.put(terms[j], new Integer(1));
					TermVectorOffsetInfo[] term_offsets = tpv.getOffsets(j);
					int[] term_pos = tpv.getTermPositions(j);
					
					double offset_score = GetOffsetScore(query_offsets,query_pos,term_offsets,term_pos);
					
					int t1 = 0;
					if (mpTF.containsKey(terms[j])) {
						t1 = (int)mpTF.get(terms[j]);
					}
					mpTF.put(terms[j],new Integer(t1+termFreqs[j]));
					
					double t2 = 0;
					if (mpTermOffset.containsKey(terms[j])) {
						t2 = (double)mpTermOffset.get(terms[j]);
					}
					mpTermOffset.put(terms[j],new Double(t2+offset_score));
				}
			}
			{//histogram for body
				TermPositionVector tpv = (TermPositionVector) reader.getTermFreqVector(docId, "body");
				TermVectorOffsetInfo[] query_offsets = tpv.getOffsets(tpv.indexOf(queryString));
				int[] query_pos = tpv.getTermPositions(tpv.indexOf(queryString));
				String[] terms = tpv.getTerms();
				int[] termFreqs = tpv.getTermFrequencies();
				for ( int j=0 ; j<terms.length ; j++ ) {
					//if ( !isNoun(terms[j].trim())) continue;
					temp_mp.put(terms[j], new Integer(1));
					TermVectorOffsetInfo[] term_offsets = tpv.getOffsets(j);
					int[] term_pos = tpv.getTermPositions(j);
					
					double offset_score = GetOffsetScore(query_offsets,query_pos,term_offsets,term_pos);

					int t1 = 0;
					if (mpTF.containsKey(terms[j])) {
						t1 = (int)mpTF.get(terms[j]);
						
					}
					mpTF.put(terms[j],new Integer(t1+termFreqs[j]));
					
					double t2 = 0;
					if (mpTermOffset.containsKey(terms[j])) {
						t2 = (double)mpTermOffset.get(terms[j]);
					}
					mpTermOffset.put(terms[j],new Double(t2+offset_score));
				}
					
			}
			for ( String term : temp_mp.keySet()) {
				if ( temp_mp.get(term) == null ) continue;
				int t = temp_mp.get(term);
				int t1 = 0 ;
				if ( mp_termDoc.containsKey(term) && mp_termDoc.get(term)!=null ) { 
					t1 = mp_termDoc.get(term);
				}
				mp_termDoc.put(term, new Integer(t+t1));
			}
			
		}
		
		double MAXTFIDF=0;
		for ( String term : mpTF.keySet()) {
			int tf = (int)mpTF.get(term);
			int df = reader.docFreq(new Term("body",(String)term));
			double tfidf = tf*Math.log((double)is.maxDoc()/(double)df);
			if ( Double.isInfinite(tfidf) ) continue;
			
			if ( term.length()>=2 && queryString.indexOf(term)==-1 &&
					term.indexOf(queryString)==-1) {
				if ( tfidf > MAXTFIDF )
					MAXTFIDF = tfidf;
				mpTFIDF.put(term, tfidf);
			}
			//System.out.println(term+" "+tfidf);
		}
		//System.out.print(MAXTFIDF);
		
		//Normalize TFIDF
		
		for ( String term: mpTFIDF.keySet() ) {
			//if ( term.equals("null")) continue;
			if ( mpTFIDF.get(term) == null || mpTermOffset.get(term)==null ) continue;
			double tfidf = mpTFIDF.get(term);
			double offset_score = mpTermOffset.get(term);
			int norm = mp_termDoc.get(term);
			mpScore.put(term, tfidf/MAXTFIDF*offset_score/(double)norm);
			//mpTFIDF.put(term,new Double(tfidf/MAXTFIDF));
			//System.out.printf("%s %f\n",term,tfidf/MAXTFIDF);
		}
		//Add offset attributes
		sorted_mpScore.putAll(mpScore);
		int cnt = 0 ;
		//ret=String.format("%d\n", MAXTERM);
		String temp_ret = null;
		for ( String term : sorted_mpScore.keySet()) {
			if ( cnt >= MAXTERM  ) break;
			//System.out.println(term);
			if(sorted_mpScore.get(term) == null) continue;
			//System.out.println(cnt);
			

			//double value = sorted_mpTFIDF.get(term);
			//System.out.println(term+" "+value);
			//System.out.println(queryString+term+sorted_mpScore.get(term));
			
			query = qp.parse(queryString+term);
			qp.setDefaultOperator(QueryParser.OR_OPERATOR.AND);
			if ( query == null ) continue;
			
			tDocs = is.search(query, 1);
			if (tDocs.scoreDocs.length<1 ) continue;
			cnt++;
			if (temp_ret==null) temp_ret = queryString+term+"\n";
			else temp_ret+=queryString+term+"\n";
			for ( int i=0 ; i<tDocs.scoreDocs.length ; i++ ) {
				//cnt++;
				int docId = tDocs.scoreDocs[i].doc;
				Document doc = is.doc(docId);
				String path = doc.get("path");
				String content = doc.get("body");
				temp_ret+=path.substring(0,path.indexOf("."))+"\n";
				//System.out.println(path);
				//System.out.println(content);
				/*
				TermPositionVector tpv = (TermPositionVector) reader.getTermFreqVector(docId, "body");
				TermVectorOffsetInfo[] query_offsets = tpv.getOffsets(tpv.indexOf(queryString));
				TermVectorOffsetInfo[] term_offsets = tpv.getOffsets(tpv.indexOf(term));
				int[] query_pos = tpv.getTermPositions(tpv.indexOf(queryString));
				int[] term_pos = tpv.getTermPositions(tpv.indexOf(term));
				int query_position=0, term_position=0;
				int min_offset = Integer.MAX_VALUE;
				// search the nearest pair
				for ( int p = 0 ; p<term_pos.length ; p++ ) {
					
					for ( int k = 0 ; k<query_pos.length ; k++ ) {
						int t;
						if ( query_offsets[k].getEndOffset() < term_offsets[p].getStartOffset() ) {
							t = term_offsets[p].getStartOffset() - query_offsets[k].getEndOffset();
						}
						else {
							t = query_offsets[k].getStartOffset() - term_offsets[p].getEndOffset();
						}
						if ( t<min_offset ) {
							min_offset = t;
							query_position = k;
							term_position = p;
						}
					}
				}
				
				String abst = "null";
				int b=0,e=0;
				if ( query_offsets[query_position].getEndOffset() < term_offsets[term_position].getStartOffset() ) {
					b = query_offsets[query_position].getStartOffset() - 20;
					e = term_offsets[term_position].getEndOffset() + 20;
					abst = content.substring(b<0?0:b,e>content.length()-1?content.length()-1:e);
				}
				else{
					b = term_offsets[term_position].getStartOffset() - 20;
					e = query_offsets[query_position].getEndOffset() + 20;
					abst = content.substring(b<0?0:b,e>content.length()-1?content.length()-1:e);
				}
				System.out.println(b+" "+e);
				System.out.println(abst);
				ret+=abst+"\n";
				*/
				//准备高亮
		     Formatter formatter = new SimpleHTMLFormatter("","");
		     Scorer scorer = new QueryScorer(query);
		     Highlighter hg = new Highlighter(formatter,scorer);
		     Fragmenter fragmenter = new SimpleFragmenter(50);
		     hg.setTextFragmenter(fragmenter);

		     //返回高亮后的结果，如果当前属性值中没有出现关键字，会返回null
		     String hc =hg.getBestFragment(new SmartChineseAnalyzer(matchVersion),"body",replaceBlank(content));
		     
		     if(hc ==null){
		        temp_ret+="\n";
		     }
		     else {
		    	 temp_ret+=hc+"\n";
		     }
			}
		}
		System.out.println(Math.min(cnt,MAXTERM));
		ret = String.valueOf(Math.min(cnt,MAXTERM)) +"\n"+ temp_ret;
		return ret;
	}
	public static String replaceBlank(String s)    
	{    
	     Pattern p = Pattern.compile("\\s*|\t|\r|\n");    
	     //System.out.println("before:"+s);    
	     Matcher m = p.matcher(s);    
	     String after = m.replaceAll("");    
	     return after;  
	}
	public static void main(String[] args) throws Exception{
		//IRS.CreateIndex();
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file_in),"utf-8");
		BufferedReader br = new BufferedReader(isr);
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file_out),"utf-8");
		BufferedWriter bw = new BufferedWriter(osw); 
		String line = br.readLine();
		if ( line != null ) {
			int num = Integer.parseInt(line);
			for ( int k = 1 ; k<=num ; k++ ) {
				System.out.println(k);
				bw.write(String.valueOf(k)+"\n");
				line = br.readLine().trim();
				String ret = IRS.Search(line);
				String utf_ret = new String(ret.getBytes("utf-8"),"utf-8");
				bw.write(utf_ret);
			}
		}
	    bw.close();
        br.close();
        osw.close();
        isr.close();
        
	}
}
class ValueComparator implements Comparator {

	  Map base;
	  public ValueComparator(Map base) {
	      this.base = base;
	  }

	  public int compare(Object a, Object b) {

	    if((Double)base.get(a) < (Double)base.get(b)) {
	      return 1;
	    } else if((Double)base.get(a) == (Double)base.get(b)) {
	      return 0;
	    } else {
	      return -1;
	    }
	  }
	}