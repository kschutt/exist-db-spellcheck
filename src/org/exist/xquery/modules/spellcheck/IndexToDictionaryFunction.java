package org.exist.xquery.modules.spellcheck;

import java.io.File;
import java.io.PrintWriter;

import org.exist.dom.DocumentSet;
import org.exist.dom.QName;
import org.exist.indexing.lucene.LuceneIndexWorker;
import org.exist.util.Occurrences;
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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class IndexToDictionaryFunction extends BasicFunction {
	private LuceneIndexWorker _indexWorker;

	public final static FunctionSignature signature = new FunctionSignature(
			new QName("indextodictionary", SpellCheckModule.NAMESPACE_URI,
					SpellCheckModule.PREFIX),
			"A function to help evaluate a query term.", new SequenceType[] {
					new FunctionParameterSequenceType("query", Type.STRING,
							Cardinality.EXACTLY_ONE, "The query text"),
					new FunctionParameterSequenceType("db", Type.DOCUMENT,
							Cardinality.ONE_OR_MORE, "The query text"),
					new FunctionParameterSequenceType("field", Type.STRING,
							Cardinality.EXACTLY_ONE, "The query text") },
			new FunctionReturnSequenceType(Type.STRING,
					Cardinality.ZERO_OR_MORE, "the text to return"));

	public IndexToDictionaryFunction(XQueryContext context) {
		super(context, signature);
		_indexWorker = (LuceneIndexWorker) context.getBroker()
				.getIndexController().getWorkerByIndexName("lucene-index");
	}

	public Sequence eval(Sequence[] args, Sequence contextSequence)
			throws XPathException {

		if (args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty()) {
			return Sequence.EMPTY_SEQUENCE;
		}
		ValueSequence result = new ValueSequence();
		try {
			String query = args[0].getStringValue();
			DocumentSet documents = (DocumentSet) args[1];
			String field = args[2].getStringValue();
			Occurrences[] items = _indexWorker.scanIndex(super.getContext(),
					documents, null, null);
			PrintWriter writer = new PrintWriter(
					"/backup/dictionary/dictionary.txt", "UTF-8");
			for (int i = 0; i < items.length; i++) {
				Occurrences occurrence = items[i];
				writer.println(occurrence.getTerm());
			}
			writer.close();
			result.add(new StringValue("Indexed this many items: " + items.length));

		} catch (Exception e) {
			result.add(new StringValue("There was an exception: "
					+ e.getMessage()));

		}
		return result;
	}
}
