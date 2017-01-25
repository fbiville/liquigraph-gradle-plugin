package org.liquigraph.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

public class LiguigraphPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		TaskContainer tasks = project.getTasks();
		tasks.create("org.liquigraph.gradle.run", LiquigraphTaskRun.class);
		tasks.create("org.liquigraph.gradle.dry-run", LiquigraphTaskDryRun.class);
	}

}
