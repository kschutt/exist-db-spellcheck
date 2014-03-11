exist-db-spellcheck
===================

This branch is built upon the work done [here](http://www.javacodegeeks.com/2010/05/did-you-mean-feature-lucene-spell.html).

Thanks to the guys on the exist-open listserve for pointing this reference out.

# Current State #

This [branch]() is currently what I was able to get running based on the documentation from [JavacodeGeeks](http://www.javacodegeeks.com/2010/05/did-you-mean-feature-lucene-spell.html) with a few modifications to make it work with Lucene 3.6.1 which is included in eXist-db 2.1.

Unfortunately, the code still uses a flat text file that is pre-generated. The first time the code executes, Lucene generates an index of this file in the directory specified.

Ideally, we would want to generate the dictionary based on one of the indexes already stored in eXist-db by Lucene (either the `ngram`, `range`, or `qname` based on your preference). And let Lucene re-index this dictionary for spell checking. 

# Overview #
The goal of this module is to allow the user to perform spell-checking on a query using Lucene's Spell Check module. For this, we are using Lucene 3.6.1.

## Installation ##
1. Copy the `*.java` files to the directory `extensions/modules/src/org/exist/xquery/modules/spellcheck` directory. You may need to create it.
2. Edit your `conf.xml` in `$EXIST_HOME` to include the following line under the `<builtin-modules>` tag:

		<module uri="http://exist-db.org/xquery/spellcheck" class="org.exist.xquery.modules.spellcheck.SpellCheckModule" />

3. Copy the following files from `extensions/indexes/lucene/lib/` to `exist/lib/core`:

		lucene-analyzers-3.6.1.jar
		lucene-core-3.6.1.jar
		lucene-queries-3.6.1.jar

4. Download `lucene-spellchecker-3.6.1.jar` into  `exist/lib/core` from [here](http://grepcode.com/snapshot/repo1.maven.org/maven2/org.apache.lucene/lucene-spellchecker/3.6.1)

5. Rebuild and restart eXist-db.
6. If all goes well, then you can use the following code to access the module from XQuery:

		xquery version "3.0";
		import module namespace dcn="http://exist-db.org/xquery/spellcheck";
		spellcheck:spellcheck("diabts","","") 

7. And receive the following output:

		diabetes
		diact
		diabasic
		diabetic
		diabolism

*Note:* The documentation [here](http://exist-db.org/exist/apps/doc/extensions.xml) states that modules that wish to use Lucene indexes should be placed in the `extensions/indexes` sub-directory, but I was unable to get this to work properly since eXist-db would never pick up the right dependencies which is why I copied everything over to `exist/lib/core`. 


