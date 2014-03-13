package org.exist.xquery.modules.spellcheck;

import java.util.List;
import java.util.Map;

import org.exist.xquery.AbstractInternalModule;
import org.exist.xquery.FunctionDef;

public class SpellCheckModule extends AbstractInternalModule {

	public final static String NAMESPACE_URI = "http://exist-db.org/xquery/spellcheck";

	public final static String PREFIX = "spellcheck";
	public final static String INCLUSION_DATE = "2014-03-10";
	public final static String RELEASED_IN_VERSION = "eXist-2.1";

	private final static FunctionDef[] functions = {
			new FunctionDef(SpellCheckFunction.signature,
					SpellCheckFunction.class) };

	public SpellCheckModule(Map<String, List<? extends Object>> parameters) {
		super(functions, parameters);
	}

	public String getNamespaceURI() {
		return NAMESPACE_URI;
	}

	public String getDefaultPrefix() {
		return PREFIX;
	}

	public String getDescription() {
		return "A module for showing good examples of module usage";
	}

	public String getReleaseVersion() {
		return RELEASED_IN_VERSION;
	}

}