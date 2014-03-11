package org.exist.xquery.modules.spellcheck;

import java.io.File;

import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class SpellCheckFunction extends BasicFunction {

	public final static FunctionSignature signature = new FunctionSignature(
			new QName("spellcheck", SpellCheckModule.NAMESPACE_URI,
					SpellCheckModule.PREFIX),
			"A function to help evaluate a query term.", new SequenceType[] {
					new FunctionParameterSequenceType("query", Type.STRING,
							Cardinality.EXACTLY_ONE, "The query text"),
					new FunctionParameterSequenceType("db", Type.STRING,
							Cardinality.EXACTLY_ONE, "The db we want to index"),
					new FunctionParameterSequenceType("field", Type.STRING,
							Cardinality.EXACTLY_ONE, "The field we want to build the dictionary from") },
			new FunctionReturnSequenceType(Type.STRING,
					Cardinality.ZERO_OR_MORE, "the text to return"));

	public SpellCheckFunction(XQueryContext context) {
		super(context, signature);
	}

	public Sequence eval(Sequence[] args, Sequence contextSequence)
			throws XPathException {

		if (args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty()) {
			return Sequence.EMPTY_SEQUENCE;
		}
		String query = args[0].getStringValue();
		String db = args[1].getStringValue();
		String field = args[2].getStringValue();

		ValueSequence result = new ValueSequence();
		try {
			File dir = new File("/backup/dictionary/");

			Directory directory = FSDirectory.open(dir);

			SpellChecker spellChecker = new SpellChecker(directory);
			Dictionary dictioanry = new PlainTextDictionary(new File(
					"/backup/dictionary/fulldictionary00.txt"));
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
			spellChecker.indexDictionary(dictioanry,config,true);

			int suggestionsNumber = 5;

			String[] suggestions = spellChecker.suggestSimilar(query,
					suggestionsNumber);

			if (suggestions != null && suggestions.length > 0) {
				for (String word : suggestions) {
					result.add(new StringValue(word));
				}
			} else {
				result.add(new StringValue("No suggestions found for word: "
						+ query));
			}

			result.add(new StringValue("No suggestions found for word: "
					+ query));
		} catch (Exception e) {
			result.add(new StringValue("There was an exception: "
					+ e.getMessage()));

		}
		return result;
	}
}
