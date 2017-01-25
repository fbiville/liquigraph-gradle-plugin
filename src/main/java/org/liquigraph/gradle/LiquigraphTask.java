package org.liquigraph.gradle;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.GradleException;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.TaskAction;
import org.liquigraph.core.api.Liquigraph;
import org.liquigraph.core.configuration.Configuration;
import org.liquigraph.core.configuration.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class LiquigraphTask extends AbstractTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(LiquigraphTask.class);

	/**
	 * Classpath location of the master changelog file
	 */
	private String changelog;

	/**
	 * Graph JDBC URI
	 * <ul>
	 *  <li>jdbc:neo4j://&lt;host&gt;:&lt;port&gt;/</li>
	 *  <li>jdbc:neo4j:file:/path/to/db</li>
	 *  <li>jdbc:neo4j:mem</li>
	 *  <li>jdbc:neo4j:mem:name</li>
	 * </ul>
	 */
	private String jdbcUri;

	/**
	 * Graph connection username.
	 */
	private String username;

	/**
	 * Graph connection password.
	 */
	private String password;

	/**
	 * Comma-separated execution context list.
	 * If no context is specified, all Liquigraph changeset contexts will match.
	 * If contexts are defined, all Liquigraph changesets whose at least 1 declared context will match.
	 * Please note that Liquigraph changesets that define no context will always match.
	 */
	private String executionContexts = "";

	@Input
	public String getChangelog() {
		return changelog;
	}

	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}

	@Input
	public String getJdbcUri() {
		return jdbcUri;
	}

	public void setJdbcUri(String jdbcUri) {
		this.jdbcUri = jdbcUri;
	}

	@Input
	@Optional
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Input
	@Optional
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Input
	@Optional
	public String getExecutionContexts() {
		return executionContexts;
	}

	public void setExecutionContexts(String executionContexts) {
		this.executionContexts = executionContexts;
	}

	@TaskAction
	public void executeTask() {
		Configuration configuration;
		try {
			URL url = getProject().getBuildDir().getParentFile().toURI().toURL();
			configuration = withExecutionMode(new ConfigurationBuilder()
					.withChangelogLoader(changelogFilename -> this.getClass().getResourceAsStream(changelogFilename))
					.withExecutionContexts(executionContexts(executionContexts))
					.withMasterChangelogLocation(changelog)
					.withUsername(username)
					.withPassword(password)
					.withUri(jdbcUri))
					.build();

			LOGGER.debug("Running Liquigraph migration with :\n"
					+ "\tbuild dir {}\n"
					+ "\texecution context {}\n"
					+ "\tchangelog {}\n"
					+ "\tusername {}\n"
					+ "\tpassword {}\n"
					+ "\tjdbc URI {}\n"
					, getProject().getBuildDir(), executionContexts, changelog, username, "******", jdbcUri);

			new Liquigraph().runMigrations(configuration);
		} catch (MalformedURLException e) {
			LOGGER.error("Error durring liquid graph migration", e);
			throw new GradleException(e.getMessage());
		}
	}

	protected abstract ConfigurationBuilder withExecutionMode(ConfigurationBuilder configurationBuilder);

	private Collection<String> executionContexts(String executionContexts) {
		if (executionContexts.isEmpty()) {
			return emptyList();
		}
		Collection<String> result = new ArrayList<>();
		for (String context : asList(executionContexts.split(","))) {
			result.add(context.trim());
		}
		return result;
	}

}
