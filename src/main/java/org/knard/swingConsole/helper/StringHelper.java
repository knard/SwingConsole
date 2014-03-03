package org.knard.swingConsole.helper;

import java.util.ArrayList;
import java.util.List;

public class StringHelper {

	public static final class StringPart {

		public int start;
		public String part;
		public int end;

		public StringPart(String substring, int startIndex, int endIndex) {
			this.part = substring;
			this.start = startIndex;
			this.end = endIndex;
		}

	}

	public static List<StringPart> split(String s) {
		ArrayList<StringPart> results = new ArrayList<StringPart>();
		int startIndex = 0;
		int endIndex = 0;
		boolean escaped = false;
		boolean quoted = false;
		for(int i = 0 ; i < s.length() ; i++ ) {
			endIndex++;
			char c = s.charAt(i);
			if( escaped ) {
				escaped = false;
			} else if( c == '\\' ) {
				escaped = true;
			} else if( c == '"' ) {
				quoted = !quoted;
			} else if( c == ' ' && !quoted) {
				if( endIndex>startIndex+1 ) {
					results.add(new StringPart(s.substring(startIndex, i), startIndex, i));
				}
				startIndex = endIndex;
			}
		}
		if( startIndex != endIndex ) {
			results.add(new StringPart(s.substring(startIndex, endIndex), startIndex, endIndex));
		}
		return results;
	}
	
}
