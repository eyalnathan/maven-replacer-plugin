import org.junit.Test

class CheckFilesTest {
	
	def checkForTokensAndValues = { filename, token="token", value="value" ->
		println "Inspecting file: ${filename}"
		String content = new File(filename).getText()
		
		assert !content.contains(token)
		println "${filename} ${token} removed"
		assert content.contains(value)
		println "${filename} ${value} found"
	}
	
	def checkFileSizeMatches = { filename1, filename2 ->
		assert new File(filename1).size() == new File(filename2).size()
		println "File sizes match between ${filename1} and ${filename2}"
	}

	@Test	
	void shouldHaveFilesReplaced() {
		checkFileSizeMatches "src/main/resources/simple.txt", "target/classes/simple.txt"
		checkFileSizeMatches "src/main/resources/largefile.txt", "target/classes/largefile.txt"
		checkFileSizeMatches "src/main/resources/regex.txt","target/classes/regex.txt"
		
		println "Checking contents"
		new File("target/classes").eachFileMatch(~/.*\.txt/) { checkForTokensAndValues it.getPath() }
		checkForTokensAndValues "target/classes/nesteddir/multiple-files1.txt"
		checkForTokensAndValues "target/classes/nesteddir/multiple-files2.txt"
		checkForTokensAndValues "target/classes/nesteddir-for-abs/multiple-files1.txt"
		checkForTokensAndValues "target/classes/nesteddir-for-abs/multiple-files2.txt"
		checkForTokensAndValues "target/simple-outputfile-remove.txt", "token", ""
		checkForTokensAndValues "target/multiple-tokens-for-map-outputfile.txt", "token1", "value1"
		checkForTokensAndValues "target/multiple-tokens-for-map-outputfile.txt", "token2", "value2"
		checkForTokensAndValues "target/classes/newdir/simple-outputfile.txt"
		checkForTokensAndValues "target/classes/special/multiple-tokens-to-replace.txt", "token1", "value1"
		checkForTokensAndValues "target/classes/special/multiple-tokens-to-replace.txt", "token2", "value2"
		checkForTokensAndValues "target/classes/include2"
		checkForTokensAndValues "target/classes/file1"
		checkForTokensAndValues "target/classes/file2"
		checkForTokensAndValues "target/classes/outdir/simple.txt"
		checkForTokensAndValues "target/classes/simple-absolute.txt"
		checkForTokensAndValues "target/classes/linematch.txt", "replace=token", "replace=value"
		checkForTokensAndValues "target/classes/special/variableTokenValueMap.txt", "token1", "value1"
		checkForTokensAndValues "target/classes/special/variableTokenValueMap.txt", "token2", "value2"
		checkForTokensAndValues "target/classes/target/outputDir/nesteddir/multiple-files2.txt", "token", "value"
		checkForTokensAndValues "target/classes/timestamped.toReplace", "project.version", "0.0.1-SNAPSHOT"
		checkForTokensAndValues "target/outputBasedir/outdir/simple.txt"
		checkForTokensAndValues "target/xpath-out.xml"
		checkForTokensAndValues "target/xpath-in-replacements-out.xml"
		checkForTokensAndValues "target/classes/simple.txt.output"
		checkForTokensAndValues "/tmp/simple.txt"
	}
}