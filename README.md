exist-db-spellcheck
===================

A project to allow for spellchecking from XQuery in eXist-db

## Current State ##

This [branch]() is currently what I was able to get running based on the documentation from [JavacodeGeeks](http://www.javacodegeeks.com/2010/05/did-you-mean-feature-lucene-spell.html) with a few modifications to make it work with Lucene 3.6.1 which is included in eXist-db 2.1.

Unfortunately, the code still uses a flat text file that is pre-generated. The first time the code executes, Lucene generates an index of this file in the directory specified.

Ideally, we would want to generate the dictionary based on one of the indexes already stored in eXist-db by Lucene (either the `ngram`, `range`, or `qname` based on your preference). And let Lucene re-index this dictionary for spell checking. 