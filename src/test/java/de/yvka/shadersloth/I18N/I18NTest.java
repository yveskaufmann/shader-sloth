package de.yvka.shadersloth.I18N;


import org.junit.Test;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class I18NTest {

	@Test
	public void testGetResourceBundle() throws Exception {
		ResourceBundle bundle = I18N.getResourceBundle();
		assertThat(bundle, is(notNullValue()));
	}

	@Test
	public void testGetString() throws Exception {
		String appName = I18N.getString("app.title");
		assertThat(appName, instanceOf(String.class));
	}

	@Test
	public void testGetStringWithUnknownKey() throws Exception {
		String notKnownKey = "___NOT_KNOWN_KEY___";
		assertThat( I18N.getString(notKnownKey), is(equalTo("%" + notKnownKey)));
	}
}
