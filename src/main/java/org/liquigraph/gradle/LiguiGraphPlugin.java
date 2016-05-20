package org.liquigraph.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class LiguiGraphPlugin implements Plugin<Project>{

	@Override
	public void apply(Project project) {
		project.getTasks().create("org.liquigraph.gradle.run", LiquiGraphTaskRun.class);
		project.getTasks().create("org.liquigraph.gradle.dry-run", LiquiGraphTaskDryRun.class);
	}

}
