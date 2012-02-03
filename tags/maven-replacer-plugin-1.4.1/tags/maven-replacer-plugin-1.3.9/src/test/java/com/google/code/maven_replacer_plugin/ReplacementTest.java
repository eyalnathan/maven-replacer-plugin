package com.google.code.maven_replacer_plugin;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.code.maven_replacer_plugin.file.FileUtils;


@RunWith(MockitoJUnitRunner.class)
public class ReplacementTest {
	private static final String UNESCAPED = "test\\n123\\t456";
	private static final String ESCAPED = "test\n123\t456";
	private static final String FILE = "some file";
	private static final String VALUE = "value";
	private static final String TOKEN = "token";
	
	@Mock
	private FileUtils fileUtils;
	@Mock
	private DelimiterBuilder delimiter;

	@Test
	public void shouldReturnConstructorParameters() throws Exception {
		Replacement context = new Replacement(fileUtils, TOKEN, VALUE, false);
		
		assertThat(context.getToken(), equalTo(TOKEN));
		assertThat(context.getValue(), equalTo(VALUE));
		verifyZeroInteractions(fileUtils);
	}
	
	@Test
	public void shouldApplyToTokenDelimeterIfExists() throws Exception {
		when(delimiter.apply(TOKEN)).thenReturn("new token");
		Replacement context = new Replacement(fileUtils, TOKEN, VALUE, false).withDelimiter(delimiter);
		
		assertThat(context.getToken(), equalTo("new token"));
		assertThat(context.getValue(), equalTo(VALUE));
		verifyZeroInteractions(fileUtils);
	}
	
	@Test
	public void shouldUseEscapedTokensAndValues() {
		Replacement context = new Replacement(fileUtils, UNESCAPED, UNESCAPED, true);
		
		assertThat(context.getToken(), equalTo(ESCAPED));
		assertThat(context.getValue(), equalTo(ESCAPED));
		verifyZeroInteractions(fileUtils);
	}
	
	@Test
	public void shouldUseEscapedTokensAndValuesFromFiles() throws Exception {
		when(fileUtils.readFile(FILE)).thenReturn(UNESCAPED);

		Replacement context = new Replacement(fileUtils, null, null, true);
		context.setTokenFile(FILE);
		context.setValueFile(FILE);
		
		assertThat(context.getToken(), equalTo(ESCAPED));
		assertThat(context.getValue(), equalTo(ESCAPED));
	}

	@Test
	public void shouldUseTokenFromFileUtilsIfGiven() throws Exception {
		when(fileUtils.readFile(FILE)).thenReturn(TOKEN);

		Replacement context = new Replacement(fileUtils, null, VALUE, false);
		context.setTokenFile(FILE);
		assertThat(context.getToken(), equalTo(TOKEN));
		assertThat(context.getValue(), equalTo(VALUE));
	}

	@Test
	public void shouldUseValueFromFileUtilsIfGiven() throws Exception {
		when(fileUtils.readFile(FILE)).thenReturn(VALUE);

		Replacement context = new Replacement(fileUtils, TOKEN, null, false);
		context.setValueFile(FILE);
		assertThat(context.getToken(), equalTo(TOKEN));
		assertThat(context.getValue(), equalTo(VALUE));
	}
	
	@Test
	public void shouldSetAndGetSameValues() {
		Replacement context = new Replacement();
		
		context.setToken(TOKEN);
		context.setValue(VALUE);
		assertThat(context.getToken(), equalTo(TOKEN));
		assertThat(context.getValue(), equalTo(VALUE));
	}
}