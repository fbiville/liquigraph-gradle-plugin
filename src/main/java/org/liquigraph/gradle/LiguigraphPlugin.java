package org.liquigraph.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class LiguigraphPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.getTasks().create("org.liquigraph.gradle.run", LiquigraphTaskRun.class);
		project.getTasks().create("org.liquigraph.gradle.dry-run", LiquigraphTaskDryRun.class);
	}

}
