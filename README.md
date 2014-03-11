exist-db-spellcheck
===================

This branch is built upon the work done [here](http://www.javacodegeeks.com/2010/05/did-you-mean-feature-lucene-spell.html).

Thanks to the guys on the exist-open listserve for pointing this reference out.

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


