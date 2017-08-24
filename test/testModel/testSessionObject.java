package testModel;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import uk.co.sunbyte.model.Session;

public class testSessionObject {

	@Test
	public void testSessionObject() throws IOException {
		// goes for the second constructor
		Session session = new Session("test data");
		
		assertEquals("Does test data exists? ", true, Files.exists(Paths.get("test data")));

	}

}
