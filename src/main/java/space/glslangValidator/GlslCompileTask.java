package space.glslangValidator;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.internal.ExecException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class GlslCompileTask extends DefaultTask {
	
	private SourceDirectorySet sourcesSet;
	
	public SourceDirectorySet getSourcesSet() {
		return sourcesSet;
	}
	
	public void setSourcesSet(SourceDirectorySet sourcesSet) {
		this.sourcesSet = sourcesSet;
	}
	
	@InputFiles
	public FileTree getSources() {
		return sourcesSet.getAsFileTree();
	}
	
	@OutputDirectory
	public File getDestinationDir() {
		return sourcesSet.getOutputDir();
	}
	
	@TaskAction
	protected void compile() throws IOException {
		for (File srcDir : sourcesSet.getSrcDirs()) {
			if (!srcDir.exists())
				return;
			
			Files.find(srcDir.toPath(), Integer.MAX_VALUE, (path, att) -> att.isRegularFile()).forEach(path -> {
				File src = path.toFile();
				File target = new File(sourcesSet.getOutputDir(), srcDir.toPath().relativize(path).toString() + ".spv");
				//noinspection ResultOfMethodCallIgnored
				target.getParentFile().mkdirs();
				
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				try {
					getProject().exec(execSpec -> {
						try {
							execSpec.commandLine("glslangValidator", "-V", src.getCanonicalPath(), "-o", target.getCanonicalPath());
							execSpec.setStandardOutput(outputStream);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
					});
				} catch (ExecException e) {
					throw new ExecException(e.getMessage() + "\n" + outputStream.toString(), e);
				}
			});
		}
	}
}
