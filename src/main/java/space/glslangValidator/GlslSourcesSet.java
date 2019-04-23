package space.glslangValidator;

import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.model.ObjectFactory;

public class GlslSourcesSet {
	
	private final SourceDirectorySet glsl;
	
	public GlslSourcesSet(String name, String displayName, ObjectFactory objectFactory) {
		glsl = objectFactory.sourceDirectorySet(name, displayName + " glsl source");
		glsl.getFilter()
			.include("**/*.vert", "**/*.tesc", "**/*.tese", "**/*.geom", "**/*.frag", "**/*.comp", "**/*.mesh", "**/*.task")
			.include("**/*.rgen", "**/*.rint", "**/*.rahit", "**/*.rchit", "**/*.rmiss", "**/*.rcall");
	}
	
	public SourceDirectorySet getGlsl() {
		return glsl;
	}
}

