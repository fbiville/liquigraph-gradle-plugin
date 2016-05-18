package org.liquigraph.gradle;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.InvalidRunnerConfigurationException;
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


/**
 * cedric.champeau@gmail.com
 * @cedricchampeau
 * @author didier
 *
 */
public class TestGradlePlugin {


	public final TemporaryFolder testProjectDir = new TemporaryFolder();
	private File buildFile;

	@Before
	public void setup() throws IOException {
		testProjectDir.create();
		buildFile = testProjectDir.newFile("build.gradle");
	}

	@Test
	public void testHelloWorldTask() throws IOException {
		String buildFileContent = "import org.liquigraph.gradle.*"
				+ "\n plugins {"
				+ "\n id 'org.liquigraph.gradle'}" +
				"\n task testV1(type: LiquiGraphTask) {" +
				"\n    inputDir= file('.')"+
				"\n    outputDir= file(\"$buildDir/log\")"+
				"\n    changelog= 'changelog.xml'"+
				"\n    jdbcUri= 'jdbc:neo4j://localhost:7474'"+
				"\n}";
		writeFile(buildFile, buildFileContent);

		BuildResult result = null;
		try {
			result = GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("testV1")
                    .withPluginClasspath(fileList())
                    .withDebug(true)
                    .build();
		} catch (InvalidRunnerConfigurationException e) {
			//fail("FAIL !!! " + e.getMessage());
			e.printStackTrace();
		} catch (UnexpectedBuildFailure unexpectedBuildFailure) {

			//fail("FAIL !!! " + unexpectedBuildFailure.toString() + "/" + unexpectedBuildFailure.getMessage());
			unexpectedBuildFailure.printStackTrace();
		}

		//assertTrue(result.getOutput().contains("Hello world!"));
		//assertEquals(result.task(":helloWorld").getOutcome(), SUCCESS);
	}

	private Iterable<? extends File> fileList() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/plugin-classpath.txt")));
		
		List<File> files = new ArrayList<>();
		String filePath;
		try {
			while((filePath = reader.readLine()) != null) {
				files.add(new File(filePath));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return files;
	}

	private void writeFile(File destination, String content) throws IOException {
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(destination));
			output.write(content);
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}
}
