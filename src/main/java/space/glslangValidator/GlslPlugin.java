package space.glslangValidator;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.plugins.JavaBasePlugin;
import org.gradle.api.plugins.JavaPluginConvention;

import javax.inject.Inject;
import java.io.File;

public class GlslPlugin implements Plugin<Project> {
	
	@Inject
	public GlslPlugin() {
	}
	
	@Override
	public void apply(Project project) {
		project.getPluginManager().apply(JavaBasePlugin.class);
		
		project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().all(sourceSet -> {
			final GlslSourcesSet glslSourcesSet = new GlslSourcesSet("glsl", ((DefaultSourceSet) sourceSet).getDisplayName(), project.getObjects());
			glslSourcesSet.getGlsl().srcDir("src/" + sourceSet.getName() + "/glsl");
			new DslObject(sourceSet).getConvention().getPlugins().put("glsl", glslSourcesSet);
			
			sourceSet.getResources().getFilter().exclude(element -> glslSourcesSet.getGlsl().contains(element.getFile()));
			
			final String sourceSetChildPath = "classes/java/" + sourceSet.getName();
			glslSourcesSet.getGlsl().setOutputDir(project.provider(() -> new File(project.getBuildDir(), sourceSetChildPath)));
			
			GlslCompileTask compileTask = project.getTasks().create(sourceSet.getCompileTaskName("glsl"), GlslCompileTask.class, glslCompileTask ->
					glslCompileTask.setSourcesSet(glslSourcesSet.getGlsl()));
			project.getTasks().named(sourceSet.getClassesTaskName(), task -> task.dependsOn(compileTask));
		});
	}
}
