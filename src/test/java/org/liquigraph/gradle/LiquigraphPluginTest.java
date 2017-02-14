package org.liquigraph.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static org.gradle.internal.impldep.org.testng.AssertJUnit.assertEquals;
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.Assert.assertTrue;

/**
 * @author Didier Broca
 */
public class LiquigraphPluginTest {

	@Rule public final TemporaryFolder testProjectDir = new TemporaryFolder();
    private File storeDirectory;

    @Before
	public void prepare() throws IOException {
        writeBuildFile();
    }

    @Test
	public void should_perform_migrations() throws Exception {
		BuildResult result = GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("runMigrations")
                    .withPluginClasspath()
                    .withDebug(false)
                    .build();

		assertEquals(result.task(":runMigrations").getOutcome(), SUCCESS);
        GraphDatabaseService graphDb = null;
		try {
            graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase(storeDirectory);
            try (Result queryResult = graphDb.execute("MATCH (n:Sentence) RETURN n.text AS content");
                 ResourceIterator<String> content = queryResult.columnAs("content")) {

                assertTrue(content.hasNext());
                assertEquals("Hello world!", content.next());
            }
        }
        finally {
            if (graphDb != null) {
                graphDb.shutdown();
            }
        }
    }

    private void writeBuildFile() throws IOException {
        testProjectDir.create();
        File buildFile = testProjectDir.newFile("build.gradle");
        storeDirectory = testProjectDir.newFolder("neo4j");
        String dbPath = storeDirectory.getCanonicalPath().replace("\\", "/");
        String buildFileContent = "import org.liquigraph.gradle.*\n" +
                "\n" +
                "plugins {id 'org.liquigraph.gradle'}\n" +
                "\n" +
                "task runMigrations(type: LiquigraphTaskRun) {\n" +
                "    changelog= 'changelog.xml'\n" +
                "    jdbcUri= 'jdbc:neo4j:file: " + dbPath + "'\n" +
                "}";

        Files.write(buildFile.toPath(), buildFileContent.getBytes("UTF-8"));
    }
}
