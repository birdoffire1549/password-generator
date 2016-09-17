import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.firebirdcss.tools.password_generator.PasswordGenerator;

/**
 * 
 * @author Scott Griffis
 *
 */
public class TestParseArguments {
	private static Method testMethod = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testMethod = PasswordGenerator.class.getDeclaredMethod("parseArguments", String[].class);
		testMethod.setAccessible(true);
	}

	@Test
	public void validArgumentsTest() {
		String[] validArguments = 
			{
				"--first", "AS", 
				"--last", "DS",
				"-r", "2",
				"--length", "16", 
				"--max-length", "32",
				"-d", "5",
				"-u", "2",
				"-c",
				"-l", "5",
				"-s", "5", 
				"--specials-only", "!@#$%^&*()_+"
			};
						
		try {
			@SuppressWarnings("unchecked")
			Map<String/*Switch*/, String/*Args*/> parsedValues = (Map<String, String>) testMethod.invoke(null, (new Object[] {validArguments}));
			Assert.assertNotNull(parsedValues);
			
			Assert.assertEquals("AS", parsedValues.get("--first"));
			Assert.assertEquals("DS", parsedValues.get("--last"));
			Assert.assertEquals("2", parsedValues.get("-r"));
			Assert.assertEquals("16", parsedValues.get("--length"));
			Assert.assertEquals("32", parsedValues.get("--max-length"));
			Assert.assertEquals("5", parsedValues.get("-d"));
			Assert.assertEquals("2", parsedValues.get("-u"));
			Assert.assertTrue(parsedValues.containsKey("-c"));
			Assert.assertEquals(null, parsedValues.get("-c"));
			Assert.assertEquals("5", parsedValues.get("-l"));
			Assert.assertEquals("5", parsedValues.get("-s"));
			Assert.assertEquals("!@#$%^&*()_+", parsedValues.get("--specials-only"));
		} catch (Exception e) {
			Assert.fail("An exception was thrown while invoking method: '" + e.getMessage() + "'");
		}
	}

}
