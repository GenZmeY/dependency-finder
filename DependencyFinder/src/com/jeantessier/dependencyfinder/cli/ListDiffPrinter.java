/*
 *  Copyright (c) 2001-2002, Jean Tessier
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *  	* Redistributions of source code must retain the above copyright
 *  	  notice, this list of conditions and the following disclaimer.
 *  
 *  	* Redistributions in binary form must reproduce the above copyright
 *  	  notice, this list of conditions and the following disclaimer in the
 *  	  documentation and/or other materials provided with the distribution.
 *  
 *  	* Neither the name of Jean Tessier nor the names of his contributors
 *  	  may be used to endorse or promote products derived from this software
 *  	  without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jeantessier.dependencyfinder.cli;

import java.util.*;

import org.apache.oro.text.perl.*;

public class ListDiffPrinter {
	public static final boolean DEFAULT_COMPRESS    = false;
    public static final String  DEFAULT_DTD_PREFIX  = "http://depfind.sourceforge.net/dtd";

	private static final Perl5Util perl = new Perl5Util();
	
	private StringBuffer buffer = new StringBuffer();

	private String  indent_text  = "    ";
	private int     indent_level = 0;
	private boolean compress;

	private String     name        = "";
	private String     old_version = "";
	private String     new_version = "";
	private Collection removed     = new TreeSet();
	private Collection added       = new TreeSet();
	
	public ListDiffPrinter() {
		this(DEFAULT_COMPRESS, DEFAULT_DTD_PREFIX);
	}
	
	public ListDiffPrinter(boolean compress) {
		this(compress, DEFAULT_DTD_PREFIX);
	}
	
	public ListDiffPrinter(String default_dtd) {
		this(DEFAULT_COMPRESS, default_dtd);
	}
	
	public ListDiffPrinter(boolean compress, String dtd_prefix) {
		this.compress    = compress;

		AppendHeader(dtd_prefix);
	}

	public String IndentText() {
		return indent_text;
	}

	public void IndentText(String indent_text) {
		this.indent_text = indent_text;
	}

	private void AppendHeader(String dtd_prefix) {
		Append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").EOL();
		EOL();
		Append("<!DOCTYPE list-diff SYSTEM \"").Append(dtd_prefix).Append("/list-diff.dtd\">").EOL();
		EOL();
	}

	public String Name() {
		return name;
	}

	public void Name(String name) {
		this.name = name;
	}

	public String OldVersion() {
		return old_version;
	}

	public void OldVersion(String old_version) {
		this.old_version = old_version;
	}

	public String NewVersion() {
		return new_version;
	}

	public void NewVersion(String new_version) {
		this.new_version = new_version;
	}
	
	public Collection Removed() {
		return Collections.unmodifiableCollection(removed);
	}
	
	public void RemoveAll(Collection removed) {
		this.removed.addAll(removed);
	}
	
	public void Remove(String line) {
		this.removed.add(line);
	}
	
	public Collection Added() {
		return Collections.unmodifiableCollection(added);
	}

	public void AddAll(Collection added) {
		this.added.addAll(added);
	}
	
	public void Add(String line) {
		this.added.add(line);
	}

	protected ListDiffPrinter Append(boolean b) {
		buffer.append(b);
		return this;
	}

	protected ListDiffPrinter Append(char c) {
		buffer.append(c);
		return this;
	}

	protected ListDiffPrinter Append(char[] str) {
		buffer.append(str);
		return this;
	}

	protected ListDiffPrinter Append(char[] str, int offset, int len) {
		buffer.append(str, offset, len);
		return this;
	}

	protected ListDiffPrinter Append(double d) {
		buffer.append(d);
		return this;
	}

	protected ListDiffPrinter Append(float f) {
		buffer.append(f);
		return this;
	}

	protected ListDiffPrinter Append(int i) {
		buffer.append(i);
		return this;
	}

	protected ListDiffPrinter Append(long l) {
		buffer.append(l);
		return this;
	}

	protected ListDiffPrinter Append(Object obj) {
		buffer.append(obj);
		return this;
	}

	protected ListDiffPrinter Append(String str) {
		buffer.append(str);
		return this;
	}

	protected ListDiffPrinter Indent() {
		for (int i=0; i<indent_level; i++) {
			Append(IndentText());
		}

		return this;
	}

	protected ListDiffPrinter EOL() {
		return Append(System.getProperty("line.separator", "\n"));
	}

	protected void RaiseIndent() {
		indent_level++;
	}

	protected void LowerIndent() {
		indent_level--;
	}

	public String toString() {
		Indent().Append("<list-diff>").EOL();
		RaiseIndent();
		
		Indent().Append("<name>").Append(Name()).Append("</name>").EOL();
		Indent().Append("<old>").Append(OldVersion()).Append("</old>").EOL();
		Indent().Append("<new>").Append(NewVersion()).Append("</new>").EOL();
		
		Indent().Append("<removed>").EOL();
		RaiseIndent();
		PrintLines(buffer, Removed());
		LowerIndent();
		Indent().Append("</removed>").EOL();
		
		Indent().Append("<added>").EOL();
		RaiseIndent();
		PrintLines(buffer, Added());
		LowerIndent();
		Indent().Append("</added>").EOL();
		
		LowerIndent();
		Indent().Append("</list-diff>").EOL();
		
		return buffer.toString();
	}

	private void PrintLines(StringBuffer buffer, Collection lines) {
		String   previous = "\n\n\n\n";
		
		Iterator i = lines.iterator();
		while (i.hasNext()) {
			String line = (String) i.next();
			if (!(compress && Contains(previous, line))) {
				int pos = line.lastIndexOf(" [");
				if (pos != -1) {
					Indent().Append("<line>").Append(line.substring(0, pos)).Append("</line>").EOL();
				} else {
					Indent().Append("<line>").Append(line).Append("</line>").EOL();
				}
				previous = line;
			}
		}
	}

	private boolean Contains(String container, String contained) {
		boolean result = false;

		if (contained.endsWith(" [P]")) {
			result = false;
		} else if (container.endsWith(" [P]") && contained.endsWith(" [C]")) {
			String package_name = "";
			int pos = contained.lastIndexOf('.');
			if (pos != -1) {
				package_name = contained.substring(0, pos);
			}
			
			result = container.equals(package_name + " [P]");
		} else if (container.endsWith(" [P]") && contained.endsWith(" [F]")) {
			String package_name = "";
			synchronized (perl) {
				if (perl.match("/^(.*)\\.[^\\.]*\\.[^\\.]*\\(.*\\)/", contained)) {
					package_name = perl.group(1);
				} else if (perl.match("/^(.*)\\.[^\\.]*\\.[\\^.]*/", contained)) {
					package_name = perl.group(1);
				}
			}
			
			result = container.equals(package_name + " [P]");
		} else if (container.endsWith(" [C]") && contained.endsWith(" [F]")) {
			String class_name = "";
			synchronized (perl) {
				if (perl.match("/^(.*)\\.[^\\.]*\\(.*\\)/", contained)) {
					class_name = perl.group(1);
				} else if (perl.match("/^(.*)\\.[\\^.]*/", contained)) {
					class_name = perl.group(1);
				}
			}
			
			result = container.equals(class_name + " [C]");
		}
		
		return result;
	}
}
